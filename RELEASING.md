# Releasing

`main` is the only long-lived branch. Changes reach it through pull requests, merge commits only, and
every pull request and push to `main` must pass the `build` check (`.github/workflows/build.yml`: compile
for Java 8, test, build the javadoc and sources jars and compile the examples on JDK 17, 21 and 25, then
package a CI-versioned jar). Merging publishes nothing.

The live tests (`LiveApiTest`) convert real files against the staging API (`https://stag-v2.convertapi.com/`)
with the `CONVERTAPI_STAGING_TOKEN` repository secret (also set as a Dependabot secret). Without the token,
as in a pull request from a fork or a local build, they are skipped and only the offline tests run. To
run them locally:

```bash
CONVERT_API_SECRET=... CONVERT_API_BASE_URI=https://stag-v2.convertapi.com/ ./mvnw verify
```

The build uses the Maven wrapper (`./mvnw`, Maven 3.9, pinned with a checksum) and needs JDK 17 or later.
The library itself is compiled for Java 8 (`maven.compiler.release`), and the build checks the class file
version.

## Publishing a version

A release is a **GitHub Release** whose tag is the version, published by `tomasr78`:

```bash
gh release create v2.13.1 --repo ConvertAPI/convertapi-library-java --target main --title v2.13.1 --generate-notes
```

That runs `.github/workflows/publish.yml` in the `maven-central` environment, which:

1. refuses a Release published or re-run by anyone else;
2. checks the tag is SemVer (`v2.13.1`, `v2.14.0-beta.1`), that the GitHub pre-release tick matches the
   version, that a stable version is a commit on `main`, and that Maven Central has never had that version;
3. sets `pom.xml` to the tag's version (`versions:set`), runs all the tests including the live ones,
   which must not be skipped here;
4. GPG-signs the jar, sources, javadoc and pom, uploads them to the Central Portal with
   `central-publishing-maven-plugin` (`-Prelease deploy`), waits until the Portal has validated and
   published the deployment, and then until `repo1.maven.org` serves it.

The version in `pom.xml` is a `0.0.0-SNAPSHOT` placeholder; the release version comes only from the tag.
Pull request builds package `0.0.0-ci.{run}.g{sha}` as a downloadable artifact so a change can be tried
before it is released. Update `CHANGELOG.md` in the pull request that changes behaviour, and the version
in the README and in `examples/pom.xml` (`convertapi.version`) after a release.

**Pre-releases** work the same way and may come from any branch:

```bash
gh release create v2.14.0-beta.1 --repo ConvertAPI/convertapi-library-java --target my-branch --prerelease --title v2.14.0-beta.1 --notes "Preview of ..."
```

Maven Central has no channels or dist-tags: a pre-release is public like any other version, and Maven
orders `2.14.0-beta.1` below `2.14.0`.

Maven Central versions are permanent. A bad version cannot be deleted or replaced; fix it by releasing
the next version. Never delete and re-create a release tag. Versions up to 2.13 used two-part numbers and
tags (`v2.13`); from 2.13.1 on they are three-part SemVer.

## Credentials

Maven Central has no OIDC trusted publishing, so `publish.yml` uses secrets of the `maven-central`
environment (which accepts only `v*` tags):

| Secret | What it is |
|---|---|
| `CENTRAL_TOKEN_USERNAME`, `CENTRAL_TOKEN_PASSWORD` | A Central Portal **user token** (not the login password) of an account that is a publisher of the `com.convertapi` namespace. Generate it at https://central.sonatype.com/usertoken (Account → Generate User Token). |
| `GPG_PRIVATE_KEY` | The ASCII-armoured private key that signs the artifacts. Its public key must be on `keyserver.ubuntu.com` (or `keys.openpgp.org`), where Central verifies signatures. |
| `GPG_PASSPHRASE` | The passphrase of that key. |

The namespace's publishers are listed at https://central.sonatype.com/publishing/namespaces. To add a
publisher, a current publisher of the namespace emails central-support@sonatype.com with the username to
add (the new user must first sign up on the Central Portal).

To rotate the signing key: generate a new one, publish its public key, and replace the two GPG secrets.
Old versions stay verifiable with the old public key, which must stay on the key server.

```bash
gh secret set CENTRAL_TOKEN_USERNAME --repo ConvertAPI/convertapi-library-java --env maven-central
gh secret set CENTRAL_TOKEN_PASSWORD --repo ConvertAPI/convertapi-library-java --env maven-central
gh secret set GPG_PRIVATE_KEY --repo ConvertAPI/convertapi-library-java --env maven-central < private-key.asc
gh secret set GPG_PASSPHRASE --repo ConvertAPI/convertapi-library-java --env maven-central
```

(`gh secret set` without input prompts for the value, so it is never echoed or kept in the shell history.)
