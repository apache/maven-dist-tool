#!/bin/env bash

#
#  Licensed to the Apache Software Foundation (ASF) under one or more
#  contributor license agreements.  See the NOTICE file distributed with
#  this work for additional information regarding copyright ownership.
#  The ASF licenses this file to You under the Apache License, Version 2.0
#  (the "License"); you may not use this file except in compliance with
#  the License.  You may obtain a copy of the License at
#
#       http://www.apache.org/licenses/LICENSE-2.0
#
#  Unless required by applicable law or agreed to in writing, software
#  distributed under the License is distributed on an "AS IS" BASIS,
#  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
#  See the License for the specific language governing permissions and
#  limitations under the License.
#

# repo init -u https://github.com/apache/maven-sources.git
SRC=$(pwd)/../../plugins

OUT=$(pwd)/src/site/markdown/plugins-maven4.md
LOG=$(pwd)/build

# use SDKMan
mvn3Version=3.9.16
mvnVersions="$mvn3Version 3.10.0-rc-1 4.0.0-SNAPSHOT"
javaVersion=25
javaVersionJlink=21
source "$HOME/.sdkman/bin/sdkman-init.sh"
sdk use java $javaVersion
export LANG=en
#set -x
#pushd $SRC/../core/maven-4.0.x && mvn -DdistributionTargetDir="$HOME/.sdkman/candidates/maven/4.0.0-SNAPSHOT" clean package && popd

# sdk use failures are silent below, and would run a build with the previously selected JDK or Maven
for v in $javaVersion $javaVersionJlink
do
  [ -d "$HOME/.sdkman/candidates/java/$v" ] || { echo "Java $v is not installed: sdk install java $v <path to a JDK $v>"; exit 1; }
done
for v in $mvnVersions
do
  [ -d "$HOME/.sdkman/candidates/maven/$v" ] && continue
  case $v in
    *-SNAPSHOT)
      echo "Maven $v is not installed: build it from the matching checkout, e.g. cd $SRC/../core/maven-4.0.x && mvn -DdistributionTargetDir=\"\$HOME/.sdkman/candidates/maven/$v\" clean package"
      ;;
    *)
      echo "Maven $v is not installed: sdk install maven $v"
  esac
  exit 1
done

head -19 $(pwd)/src/site/markdown/index.md > $OUT # license header
echo "# Maven 3 Plugins Build Results for Maven 4 Compatibility Check" >> $OUT
echo >> $OUT

echo "WIP:

- [maven-doxia-sitetools#655](https://github.com/apache/maven-doxia-sitetools/pull/655), Maven 4 specific \`site.xml\` resolution, reaches \`maven-site-plugin\` on Doxia's own release cycle
- the remaining core fixes are collected in the [Maven 4.0.0-RC7 milestone](https://github.com/apache/maven/milestone/131)

" >> $OUT

checkMvn() {
  local cat=$1
  local version=$2
  local logdir=$LOG/$cat/$(basename $(pwd))
  mkdir -p $logdir
  local log=$logdir/build-$version.log
  # a rebuilt distribution, SNAPSHOT or not, invalidates the log it produced.
  # compare against the candidate directory, not its contents: a reproducible build stamps
  # every file it unpacks with project.build.outputTimestamp, a date in the past
  if [ ! -f $log ] || [ $log -ot "$HOME/.sdkman/candidates/maven/$version" ]
  then
    case "$(basename $(pwd))" in
      "maven-jlink-plugin")
        sdk use java $javaVersionJlink > /dev/null
        ;;
      *)
        sdk use java $javaVersion > /dev/null
    esac
    sdk use maven $version > /dev/null
    mvn -V -B -Prun-its clean verify > $log 2>&1
  fi
  if [ $(tail $log | grep "\[INFO\] BUILD SUCCESS" | wc -l) -eq 1 ]
  then
    echo -n ":white_check_mark:<br/>"
    echo -n "$(tail $log | grep "\[INFO\] Total time:" | cut -d ' ' -f '5-' | sed -e 's/min/m/' -e 's/.. s/ s/')"
  elif [ $(tail -20 $log | grep "\[INFO\] BUILD FAILURE" | wc -l) -eq 1 ]
  then
    echo -n ":x:<br/>"
    echo -n "$(tail -20 $log | grep "\[INFO\] Total time:" | cut -d ' ' -f '5-' | sed -e 's/min/m/' -e 's/.. s/ s/')"
  else
    echo -n ":warning:"
  fi
}

check() {
  local cat=$1
  local dir=$2
  cd $dir
  echo -n "| [$(basename $(pwd))]($(git config --get remote.origin.url | sed 's/.git$//')/tree/$(git rev-parse --abbrev-ref HEAD))" >> $OUT
  sdk use maven $mvn3Version > /dev/null
  echo -n "<br/>$(mvn -B -N help:evaluate -Dexpression=project.version -q -DforceStdout)" >> $OUT
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
