#!/bin/sh

branch_to_release="master"
set -e

echo "Set private key..."
cp -vr /gpg ~/.gnupg
echo "pinentry-mode loopback" > ~/.gnupg/gpg.conf
cp -vpr /maven ~/.m2

echo "Cloning convertapi-java..."
git clone https://${GIT_USERNAME}:${GIT_SECRET}@github.com/ConvertAPI/convertapi-java.git
cd convertapi-java

echo "Switching to the release branch..."
git fetch
git checkout $branch_to_release

echo "Preparing release..."
mvn release:prepare -Prelease-sign-artifacts -DreleaseVersion=${VERSION} -DdevelopmentVersion=${NEXT_VERSION} -Dtag=v${VERSION} -Dresume=false -Darguments=-Dgpg.passphrase=${GPG_PASSPHRASE}

echo "Performing release..."
mvn release:perform -Prelease-sign-artifacts

echo "Push the new release tag..."
git push --tags

echo "Push release commits..."
git push

echo "-------------------------------------------------------------"
echo "SUCCESSFULLY released ${VERSION} to Maven Central repository."
echo "-------------------------------------------------------------"
