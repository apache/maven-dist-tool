#!/bin/env bash

# repo init -u https://github.com/apache/maven-sources.git
SRC=$(pwd)/../../plugins

OUT=$(pwd)/src/site/markdown/plugins-maven4.md
LOG=$(pwd)/build

# use SDKMan
mvnVersions="3.9.15 4.0.0-rc-4 4.0.0-rc-5 4.0.0-SNAPSHOT"
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java 25
#set -x
# pushd $SRC/../core/maven-4.0.x && mvn -DdistributionTargetDir="$HOME/.sdkman/candidates/maven/4.0.0-SNAPSHOT" clean package && popd


echo "# Maven 3 Plugins Build Results for Maven 4 Compatibility Check" > $OUT
echo >> $OUT

echo "WIP (should be in [Maven 4.0.0-RC6](https://github.com/apache/maven/milestone/127)):

- [PR #11868](https://github.com/apache/maven/pull/11868) for \`maven-source-plugin\`
- [PR #11869](https://github.com/apache/maven/pull/11869) for \`plugin-tools\`

" >> $OUT

checkMvn() {
  local cat=$1
  local version=$2
  local logdir=$LOG/$cat/$(basename $(pwd))
  mkdir -p $logdir
  local log=$logdir/build-$version.log
  sdk use maven $version > /dev/null
  if [ ! -f $log ]
  then
    mvn -V -B -Prun-its clean verify > $log 2>&1
  fi
  if [ $(tail $log | grep "\[INFO\] BUILD SUCCESS" | wc -l) -eq 1 ]
  then
    echo -n ":white_check_mark:"
  elif [ $(tail -20 $log | grep "\[INFO\] BUILD FAILURE" | wc -l) -eq 1 ]
  then
    echo -n ":x:"
  else
    echo -n ":warning:"
  fi
}

check() {
  local cat=$1
  local dir=$2
  cd $dir
  echo -n "| [$(basename $(pwd))]($(git config --get remote.origin.url | sed 's/.git$//')/tree/$(git rev-parse --abbrev-ref HEAD))" >> $OUT
  sdk use maven 3.9.15 > /dev/null
  echo -n " $(mvn -B -N help:evaluate -Dexpression=project.version -q -DforceStdout)" >> $OUT
  for v in $mvnVersions
  do
    echo -n " | " >> $OUT
    checkMvn $cat $v >> $OUT
  done
  echo " |" >> $OUT
}

for cat in $SRC/core $SRC/packaging $SRC/reporting $SRC/tools
do
  cat=$(basename $cat)
  echo "## $cat" >> $OUT
  echo >> $OUT

  sep="| ---------------- |"
  echo -n "|                  " >> $OUT
  for v in $mvnVersions
  do
    echo -n "| $v " >> $OUT
    sep="$sep -------- |"
  done
  echo "|" >> $OUT
  echo "$sep" >> $OUT

  for p in $SRC/$cat/*/pom.xml
  do
    check $cat $(dirname $p)
  done

  echo >> $OUT
done
