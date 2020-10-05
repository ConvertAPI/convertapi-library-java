#!/bin/sh

branch_to_release="master"
set -e

echo "Set private key..."
cp -vr /gpg ~/.gnupg
echo "pinentry-mode loopback" > ~/.gnupg/gpg.conf
cp -vpr /maven ~/.m2
cp -vr /ssh ~/.ssh

echo "Cloning convertapi-java..."
ssh-keyscan github.com >> ~/.ssh/known_hosts
git clone git@github.com:ConvertAPI/convertapi-java.git
cd convertapi-java

echo "Switching to the release branch..."
git fetch
git checkout $branch_to_release

echo "Preparing release..."
mvn release:prepare -DreleaseVersion=${VERSION} -DdevelopmentVersion=${NEXT_VERSION} -Dtag=v${VERSION} -Dresume=false -Darguments=-Dgpg.passphrase=${GPG_PASSPHRASE}

echo "Performing release..."
mvn release:perform

echo "Push the new release tag..."
git push --tags

echo "Push release commits..."
git push

echo "-------------------------------------------------------------"
echo "SUCCESSFULLY released ${VERSION} to Maven Central repository."
echo "-------------------------------------------------------------"
