#!/bin/bash

if [ "$#" -ne 1 ] 
then

  echo "Usage: MFAnalytics.sh <githubpages directory>"
  exit 1
fi

githubdir="$1"

java -cp "./*:." org.bala.MFAnalytics.MFAnalyticsDataDownload /root/githubpages/bala-github.github.io

cd "${githubdir}"

git add "current/*"

git add "rollup/*"

today=`date`
message="Changes on $today" 

git commit -m "$message"

git push origin master


