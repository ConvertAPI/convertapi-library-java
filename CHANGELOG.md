# Changelog

All notable changes to this project will be documented in this file.

The format is based on Keep a Changelog and, from 2.13.1 on, this project adheres to Semantic Versioning.

## [Unreleased]

### Fixed
- `ConvertApi.convertRemoteFile` and `convertRemoteFileToDir` failed with `404 page not found`: they called
  the `upload-from-url` endpoint, which the API no longer has. They now use `upload?url=`.
- `ConvertApi.convertFile(Path, String, Config, Param...)` (and the `String` path overloads that call it)
  uploaded the input file with `Config.defaults()` instead of the given `Config`, so with a custom host the
  conversion could not find the uploaded file.

### Changed
- Releases are now cut from a GitHub Release `v{version}` and published to Maven Central through the
  Central Portal by `.github/workflows/publish.yml`; the version comes from the tag. The old
  `maven-release-plugin` / OSSRH Docker release (`Dockerfile`, `maven-release.sh`) is removed.
- The library is built with the Maven wrapper on JDK 17, 21 and 25 and still compiled for Java 8.
- POM metadata: the SCM, license and issue URLs point to `convertapi-library-java`.

## [2.13] — 2025-06-18

Last version released by hand. See the
[GitHub history](https://github.com/ConvertAPI/convertapi-library-java/commits/v2.13) for earlier changes.

[Unreleased]: https://github.com/ConvertAPI/convertapi-library-java/compare/v2.13...main
[2.13]: https://github.com/ConvertAPI/convertapi-library-java/releases/tag/v2.13
