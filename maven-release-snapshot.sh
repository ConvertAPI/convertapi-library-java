#!/bin/sh

# Usage: ./maven-release-snapshot.sh 2.0-SNAPSHOT

branch_to_release="develop"

echo "Switching to the release branch..."
git fetch
git checkout $branch_to_release

echo "Pulling changes..."
git pull --ff-only

echo "Push commits..."
git push

echo "Revert uncommitted changes..."
git checkout .

echo "Cleaning..."
mvn clean

echo "Preparing release..."
if [ $# -eq 1 ]
then
	echo "\n\n" | mvn release:prepare -DreleaseVersion=$1 -Dtag=staging -Dresume=false
else
	echo "Usage: ./maven-release-snapshot.sh [snapshot version], e.g.: 2.0-SNAPSHOT"
fi

echo "Performing release..."
mvn release:perform

echo "Remove local staging tag..."
git tag -d staging

echo "Revert release commits..."
git reset --hard origin/$branch_to_release
