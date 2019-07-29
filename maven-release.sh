#!/bin/sh

# Usage: ./maven-release.sh v2.0 2.1-SNAPSHOT 2.0

branch_to_release="master"

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
	echo "\n\n" | mvn release:prepare -Dtag=$1 -Dresume=false
elif [ $# -eq 2 ] 
then
	echo "\n" | mvn release:prepare -DdevelopmentVersion=$2 -Dtag=$1 -Dresume=false
elif [ $# -eq 3 ] 
then
	mvn release:prepare -DreleaseVersion=$3 -DdevelopmentVersion=$2 -Dtag=$1 -Dresume=false
else
	echo "\n\n\n" | mvn release:prepare -Dresume=false
fi

echo "Performing release..."
mvn release:perform

echo "Push the new release tag..."
git push --tags

echo "Push release commits..."
git push
