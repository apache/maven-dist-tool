<!--
  Licensed to the Apache Software Foundation (ASF) under one
  or more contributor license agreements.  See the NOTICE file
  distributed with this work for additional information
  regarding copyright ownership.  The ASF licenses this file
  to you under the Apache License, Version 2.0 (the
  "License"); you may not use this file except in compliance
  with the License.  You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing,
  software distributed under the License is distributed on an
  "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
  KIND, either express or implied.  See the License for the
  specific language governing permissions and limitations
  under the License.
-->

# Maven 3 Plugins Build Results for Maven 4 Compatibility Check

## core

|                  | 3.9.16 | 3.10.0 | 4.0.0-rc-7 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- |
| [maven-clean-plugin](https://github.com/apache/maven-clean-plugin/tree/master)<br/>3.5.1-SNAPSHOT | :white_check_mark:<br/>15.5 s | :white_check_mark:<br/>18.4 s | :white_check_mark:<br/>22.2 s | :white_check_mark:<br/>22.1 s |
| [maven-compiler-plugin](https://github.com/apache/maven-compiler-plugin/tree/master)<br/>3.16.1-SNAPSHOT | :white_check_mark:<br/>02:16 m | :white_check_mark:<br/>02:44 m | :white_check_mark:<br/>03:18 m | :white_check_mark:<br/>03:20 m |
| [maven-deploy-plugin](https://github.com/apache/maven-deploy-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>51.7 s | :white_check_mark:<br/>01:10 m | :white_check_mark:<br/>01:27 m | :white_check_mark:<br/>01:27 m |
| [maven-install-plugin](https://github.com/apache/maven-install-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>36.2 s | :white_check_mark:<br/>46.2 s | :white_check_mark:<br/>56.9 s | :white_check_mark:<br/>57.2 s |
| [maven-resources-plugin](https://github.com/apache/maven-resources-plugin/tree/master)<br/>3.5.1-SNAPSHOT | :white_check_mark:<br/>28.2 s | :white_check_mark:<br/>42.9 s | :white_check_mark:<br/>52.3 s | :white_check_mark:<br/>52.7 s |
| [maven-site-plugin](https://github.com/apache/maven-site-plugin/tree/master)<br/>3.23.0-SNAPSHOT | :white_check_mark:<br/>02:27 m | :white_check_mark:<br/>02:33 m | :white_check_mark:<br/>03:00 m | :white_check_mark:<br/>03:01 m |
| [surefire](https://github.com/apache/maven-surefire/tree/master)<br/>3.6.2-SNAPSHOT | :x:<br/>05:40 m | :warning: | :warning: | :warning: |

## packaging

|                  | 3.9.16 | 3.10.0 | 4.0.0-rc-7 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- |
| [maven-acr-plugin](https://github.com/apache/maven-acr-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>11.8 s | :white_check_mark:<br/>15.5 s | :white_check_mark:<br/>18.0 s | :white_check_mark:<br/>18.1 s |
| [maven-ear-plugin](https://github.com/apache/maven-ear-plugin/tree/master)<br/>3.4.1-SNAPSHOT | :white_check_mark:<br/>02:19 m | :white_check_mark:<br/>03:12 m | :white_check_mark:<br/>03:57 m | :white_check_mark:<br/>04:04 m |
| [maven-ejb-plugin](https://github.com/apache/maven-ejb-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>16.2 s | :white_check_mark:<br/>21.1 s | :white_check_mark:<br/>24.7 s | :white_check_mark:<br/>25.4 s |
| [maven-jar-plugin](https://github.com/apache/maven-jar-plugin/tree/master)<br/>3.5.2-SNAPSHOT | :white_check_mark:<br/>54.7 s | :white_check_mark:<br/>01:14 m | :white_check_mark:<br/>01:28 m | :white_check_mark:<br/>01:29 m |
| [maven-jlink-plugin](https://github.com/apache/maven-jlink-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>01:23 m | :white_check_mark:<br/>01:24 m | :white_check_mark:<br/>01:37 m | :white_check_mark:<br/>01:37 m |
| [maven-jmod-plugin](https://github.com/apache/maven-jmod-plugin/tree/master)<br/>3.0.1-SNAPSHOT | :white_check_mark:<br/>30.8 s | :white_check_mark:<br/>41.4 s | :white_check_mark:<br/>49.4 s | :white_check_mark:<br/>49.3 s |
| [maven-rar-plugin](https://github.com/apache/maven-rar-plugin/tree/master)<br/>3.1.1-SNAPSHOT | :white_check_mark:<br/>11.1 s | :white_check_mark:<br/>14.5 s | :white_check_mark:<br/>16.3 s | :white_check_mark:<br/>16.3 s |
| [maven-shade-plugin](https://github.com/apache/maven-shade-plugin/tree/master)<br/>3.6.3-SNAPSHOT | :white_check_mark:<br/>02:15 m | :white_check_mark:<br/>02:24 m | :white_check_mark:<br/>02:56 m | :warning: |
| [maven-source-plugin](https://github.com/apache/maven-source-plugin/tree/master)<br/>3.4.1-SNAPSHOT | :white_check_mark:<br/>32.6 s | :white_check_mark:<br/>41.2 s | :white_check_mark:<br/>49.7 s | :white_check_mark:<br/>50.1 s |
| [maven-war-plugin](https://github.com/apache/maven-war-plugin/tree/master)<br/>3.5.2-SNAPSHOT | :white_check_mark:<br/>52.1 s | :white_check_mark:<br/>01:09 m | :white_check_mark:<br/>01:25 m | :white_check_mark:<br/>01:24 m |

## reporting

|                  | 3.9.16 | 3.10.0 | 4.0.0-rc-7 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- |
| [jxr](https://github.com/apache/maven-jxr/tree/master)<br/>3.6.1-SNAPSHOT | :white_check_mark:<br/>27.1 s | :white_check_mark:<br/>30.7 s | :white_check_mark:<br/>34.2 s | :white_check_mark:<br/>33.7 s |
| [maven-changelog-plugin](https://github.com/apache/maven-changelog-plugin/tree/master)<br/>3.0.0-M3-SNAPSHOT | :white_check_mark:<br/>15.3 s | :white_check_mark:<br/>18.0 s | :white_check_mark:<br/>19.4 s | :white_check_mark:<br/>19.4 s |
| [maven-changes-plugin](https://github.com/apache/maven-changes-plugin/tree/master)<br/>3.0.0-M4-SNAPSHOT | :white_check_mark:<br/>31.2 s | :white_check_mark:<br/>39.4 s | :white_check_mark:<br/>44.9 s | :white_check_mark:<br/>45.0 s |
| [maven-checkstyle-plugin](https://github.com/apache/maven-checkstyle-plugin/tree/master)<br/>3.6.1-SNAPSHOT | :white_check_mark:<br/>01:25 m | :white_check_mark:<br/>01:49 m | :white_check_mark:<br/>02:05 m | :white_check_mark:<br/>02:06 m |
| [maven-doap-plugin](https://github.com/apache/maven-doap-plugin/tree/master)<br/>3.0.0-M3-SNAPSHOT | :white_check_mark:<br/>14.6 s | :white_check_mark:<br/>18.4 s | :white_check_mark:<br/>17.9 s | :white_check_mark:<br/>18.7 s |
| [maven-javadoc-plugin](https://github.com/apache/maven-javadoc-plugin/tree/master)<br/>3.12.1-SNAPSHOT | :x:<br/>03:45 m | :warning: | :warning: | :warning: |
| [maven-jdeps-plugin](https://github.com/apache/maven-jdeps-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>12.5 s | :white_check_mark:<br/>17.1 s | :white_check_mark:<br/>19.4 s | :white_check_mark:<br/>19.9 s |
| [maven-pmd-plugin](https://github.com/apache/maven-pmd-plugin/tree/master)<br/>3.29.0-SNAPSHOT | :white_check_mark:<br/>02:13 m | :white_check_mark:<br/>02:35 m | :white_check_mark:<br/>02:50 m | :white_check_mark:<br/>02:50 m |
| [maven-project-info-reports-plugin](https://github.com/apache/maven-project-info-reports-plugin/tree/master)<br/>3.10.0-SNAPSHOT | :white_check_mark:<br/>01:31 m | :white_check_mark:<br/>01:37 m | :white_check_mark:<br/>01:45 m | :white_check_mark:<br/>01:45 m |

## tools

|                  | 3.9.16 | 3.10.0 | 4.0.0-rc-7 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- |
| [archetype](https://github.com/apache/maven-archetype/tree/master)<br/>3.5.0-SNAPSHOT | :white_check_mark:<br/>01:45 m | :white_check_mark:<br/>01:36 m | :white_check_mark:<br/>02:00 m | :white_check_mark:<br/>02:01 m |
| [enforcer](https://github.com/apache/maven-enforcer/tree/master)<br/>3.7.0-SNAPSHOT | :white_check_mark:<br/>02:22 m | :white_check_mark:<br/>02:43 m | :white_check_mark:<br/>03:29 m | :white_check_mark:<br/>03:30 m |
| [maven-antrun-plugin](https://github.com/apache/maven-antrun-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>33.0 s | :white_check_mark:<br/>41.6 s | :white_check_mark:<br/>51.0 s | :white_check_mark:<br/>51.1 s |
| [maven-artifact-plugin](https://github.com/apache/maven-artifact-plugin/tree/master)<br/>3.7.1-SNAPSHOT | :white_check_mark:<br/>49.5 s | :white_check_mark:<br/>57.6 s | :white_check_mark:<br/>01:08 m | :white_check_mark:<br/>01:08 m |
| [maven-assembly-plugin](https://github.com/apache/maven-assembly-plugin/tree/master)<br/>3.8.1-SNAPSHOT | :white_check_mark:<br/>03:58 m | :white_check_mark:<br/>05:17 m | :white_check_mark:<br/>06:12 m | :white_check_mark:<br/>06:13 m |
| [maven-dependency-plugin](https://github.com/apache/maven-dependency-plugin/tree/master)<br/>3.12.0-SNAPSHOT | :white_check_mark:<br/>01:06 m | :white_check_mark:<br/>01:04 m | :white_check_mark:<br/>01:13 m | :white_check_mark:<br/>01:14 m |
| [maven-gpg-plugin](https://github.com/apache/maven-gpg-plugin/tree/master)<br/>3.2.9-SNAPSHOT | :white_check_mark:<br/>01:33 m | :white_check_mark:<br/>01:42 m | :white_check_mark:<br/>01:58 m | :white_check_mark:<br/>01:59 m |
| [maven-help-plugin](https://github.com/apache/maven-help-plugin/tree/master)<br/>3.5.3-SNAPSHOT | :white_check_mark:<br/>41.9 s | :white_check_mark:<br/>48.4 s | :warning: | :warning: |
| [maven-invoker-plugin](https://github.com/apache/maven-invoker-plugin/tree/master)<br/>3.11.0-SNAPSHOT | :white_check_mark:<br/>03:25 m | :white_check_mark:<br/>04:20 m | :white_check_mark:<br/>05:30 m | :white_check_mark:<br/>05:32 m |
| [maven-jarsigner-plugin](https://github.com/apache/maven-jarsigner-plugin/tree/master)<br/>3.1.1-SNAPSHOT | :white_check_mark:<br/>20.0 s | :white_check_mark:<br/>25.1 s | :white_check_mark:<br/>28.2 s | :white_check_mark:<br/>28.4 s |
| [maven-jdeprscan-plugin](https://github.com/apache/maven-jdeprscan-plugin/tree/master)<br/>3.0.1-SNAPSHOT | :white_check_mark:<br/>13.8 s | :white_check_mark:<br/>16.7 s | :white_check_mark:<br/>19.1 s | :white_check_mark:<br/>19.1 s |
| [maven-remote-resources-plugin](https://github.com/apache/maven-remote-resources-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>36.9 s | :white_check_mark:<br/>40.9 s | :white_check_mark:<br/>48.1 s | :white_check_mark:<br/>48.7 s |
| [maven-scm-publish-plugin](https://github.com/apache/maven-scm-publish-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>19.7 s | :white_check_mark:<br/>23.3 s | :white_check_mark:<br/>24.8 s | :white_check_mark:<br/>25.0 s |
| [maven-scripting-plugin](https://github.com/apache/maven-scripting-plugin/tree/master)<br/>3.1.1-SNAPSHOT | :white_check_mark:<br/>8.7 s | :white_check_mark:<br/>10.2 s | :white_check_mark:<br/>11.9 s | :white_check_mark:<br/>11.9 s |
| [maven-toolchains-plugin](https://github.com/apache/maven-toolchains-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>12.2 s | :white_check_mark:<br/>15.7 s | :white_check_mark:<br/>18.2 s | :white_check_mark:<br/>18.3 s |
| [plugin-tools](https://github.com/apache/maven-plugin-tools/tree/maven-plugin-tools-3.x)<br/>3.17.0-SNAPSHOT | :white_check_mark:<br/>02:40 m | :white_check_mark:<br/>03:07 m | :white_check_mark:<br/>03:35 m | :white_check_mark:<br/>03:33 m |
| [release](https://github.com/apache/maven-release/tree/master)<br/>3.3.2-SNAPSHOT | :white_check_mark:<br/>02:02 m | :white_check_mark:<br/>02:16 m | :warning: | :warning: |
| [scm](https://github.com/apache/maven-scm/tree/master)<br/>2.2.2-SNAPSHOT | :x:<br/>52.0 s | :warning: | :warning: | :warning: |

