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

fixes done in [Maven 4.0.0-RC6](https://github.com/apache/maven/milestone/127):

- core
  - [PR #12245](hhttps://github.com/apache/maven/pull/12245) reports inheritance for `maven-site-plugin` (site.xml [issue](https://github.com/apache/maven-doxia-sitetools/pull/655) remains)
- packaging
  - [PR #11868](https://github.com/apache/maven/pull/11868) for `maven-source-plugin`
- tools
  - [PR #11869](https://github.com/apache/maven/pull/11869) for `plugin-tools`
  - [issue #11973](https://github.com/apache/maven/issues/11973) for `maven-toolchain-plugin`
  - [PR #12405](https://github.com/apache/maven/issues/12405) for `maven-help-plugin`


## core

|                  | 3.9.15 | 3.10.0-rc-1 | 4.0.0-rc-5 | 4.0.0-rc-6 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [maven-clean-plugin](https://github.com/apache/maven-clean-plugin/tree/maven-clean-plugin-3.x)<br/>3.5.1-SNAPSHOT | :white_check_mark:<br/>14.4 s | :white_check_mark:<br/>19.9 s | :white_check_mark:<br/>24.5 s | :white_check_mark:<br/>24.4 s | :white_check_mark:<br/>24.3 s |
| [maven-compiler-plugin](https://github.com/apache/maven-compiler-plugin/tree/maven-compiler-plugin-3.x)<br/>3.15.1-SNAPSHOT | :white_check_mark:<br/>02:03 m | :white_check_mark:<br/>02:32 m | :white_check_mark:<br/>03:10 m | :white_check_mark:<br/>03:37 m | :white_check_mark:<br/>03:36 m |
| [maven-deploy-plugin](https://github.com/apache/maven-deploy-plugin/tree/maven-deploy-plugin-3.x)<br/>3.1.5-SNAPSHOT | :white_check_mark:<br/>51.2 s | :white_check_mark:<br/>01:07 m | :white_check_mark:<br/>01:23 m | :white_check_mark:<br/>01:36 m | :white_check_mark:<br/>01:36 m |
| [maven-install-plugin](https://github.com/apache/maven-install-plugin/tree/maven-install-plugin-3.x)<br/>3.1.5-SNAPSHOT | :white_check_mark:<br/>35.0 s | :white_check_mark:<br/>44.8 s | :white_check_mark:<br/>57.5 s | :white_check_mark:<br/>01:02 m | :white_check_mark:<br/>01:03 m |
| [maven-resources-plugin](https://github.com/apache/maven-resources-plugin/tree/maven-resources-plugin-3.x)<br/>3.5.1-SNAPSHOT | :white_check_mark:<br/>28.7 s | :white_check_mark:<br/>38.1 s | :white_check_mark:<br/>48.4 s | :white_check_mark:<br/>56.8 s | :white_check_mark:<br/>57.1 s |
| [maven-site-plugin](https://github.com/apache/maven-site-plugin/tree/master)<br/>3.22.1-SNAPSHOT | :white_check_mark:<br/>02:30 m | :white_check_mark:<br/>02:44 m | :x:<br/>03:18 m | :x:<br/>03:18 m | :x:<br/>03:21 m |
| [surefire](https://github.com/apache/maven-surefire/tree/master)<br/>3.6.0-M2-SNAPSHOT | :warning: | :warning: | :warning: | :warning: | :warning: |

## packaging

|                  | 3.9.15 | 3.10.0-rc-1 | 4.0.0-rc-5 | 4.0.0-rc-6 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [maven-acr-plugin](https://github.com/apache/maven-acr-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>8.7 s | :white_check_mark:<br/>11.6 s | :white_check_mark:<br/>12.9 s | :white_check_mark:<br/>14.2 s | :white_check_mark:<br/>14.0 s |
| [maven-ear-plugin](https://github.com/apache/maven-ear-plugin/tree/master)<br/>3.4.1-SNAPSHOT | :white_check_mark:<br/>48.6 s | :white_check_mark:<br/>01:01 m | :white_check_mark:<br/>03:41 m | :white_check_mark:<br/>04:29 m | :white_check_mark:<br/>04:30 m |
| [maven-ejb-plugin](https://github.com/apache/maven-ejb-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>15.3 s | :white_check_mark:<br/>19.7 s | :white_check_mark:<br/>23.5 s | :white_check_mark:<br/>26.0 s | :white_check_mark:<br/>26.1 s |
| [maven-jar-plugin](https://github.com/apache/maven-jar-plugin/tree/maven-jar-plugin-3.x)<br/>3.5.2-SNAPSHOT | :white_check_mark:<br/>54.6 s | :white_check_mark:<br/>01:07 m | :white_check_mark:<br/>01:22 m | :white_check_mark:<br/>01:34 m | :white_check_mark:<br/>01:34 m |
| [maven-jlink-plugin](https://github.com/apache/maven-jlink-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>01:21 m | :white_check_mark:<br/>01:26 m | :white_check_mark:<br/>01:39 m | :white_check_mark:<br/>01:40 m | :white_check_mark:<br/>01:41 m |
| [maven-jmod-plugin](https://github.com/apache/maven-jmod-plugin/tree/master)<br/>3.0.1-SNAPSHOT | :white_check_mark:<br/>29.5 s | :white_check_mark:<br/>36.4 s | :white_check_mark:<br/>44.6 s | :white_check_mark:<br/>50.9 s | :white_check_mark:<br/>51.4 s |
| [maven-rar-plugin](https://github.com/apache/maven-rar-plugin/tree/master)<br/>3.1.1-SNAPSHOT | :white_check_mark:<br/>7.9 s | :white_check_mark:<br/>10.2 s | :white_check_mark:<br/>11.8 s | :white_check_mark:<br/>11.9 s | :white_check_mark:<br/>12.1 s |
| [maven-shade-plugin](https://github.com/apache/maven-shade-plugin/tree/master)<br/>3.6.3-SNAPSHOT | :white_check_mark:<br/>02:06 m | :white_check_mark:<br/>02:22 m | :x:<br/>02:59 m | :x:<br/>03:11 m | :x:<br/>03:08 m |
| [maven-source-plugin](https://github.com/apache/maven-source-plugin/tree/maven-source-plugin-3.x)<br/>3.4.1-SNAPSHOT | :white_check_mark:<br/>31.0 s | :white_check_mark:<br/>40.7 s | :x:<br/>48.9 s | :white_check_mark:<br/>52.1 s | :white_check_mark:<br/>52.6 s |
| [maven-war-plugin](https://github.com/apache/maven-war-plugin/tree/master)<br/>3.5.2-SNAPSHOT | :white_check_mark:<br/>52.4 s | :white_check_mark:<br/>01:05 m | :white_check_mark:<br/>01:19 m | :white_check_mark:<br/>01:32 m | :white_check_mark:<br/>01:31 m |

## reporting

|                  | 3.9.15 | 3.10.0-rc-1 | 4.0.0-rc-5 | 4.0.0-rc-6 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [jxr](https://github.com/apache/maven-jxr/tree/HEAD)<br/>3.6.1-SNAPSHOT | :white_check_mark:<br/>27.3 s | :white_check_mark:<br/>29.5 s | :white_check_mark:<br/>33.1 s | :white_check_mark:<br/>34.2 s | :white_check_mark:<br/>34.7 s |
| [maven-changelog-plugin](https://github.com/apache/maven-changelog-plugin/tree/HEAD)<br/>3.0.0-M3-SNAPSHOT | :white_check_mark:<br/>15.7 s | :white_check_mark:<br/>17.4 s | :white_check_mark:<br/>19.2 s | :white_check_mark:<br/>19.5 s | :white_check_mark:<br/>19.5 s |
| [maven-changes-plugin](https://github.com/apache/maven-changes-plugin/tree/HEAD)<br/>3.0.0-M4-SNAPSHOT | :white_check_mark:<br/>30.9 s | :white_check_mark:<br/>35.5 s | :white_check_mark:<br/>40.9 s | :white_check_mark:<br/>45.9 s | :white_check_mark:<br/>46.2 s |
| [maven-checkstyle-plugin](https://github.com/apache/maven-checkstyle-plugin/tree/HEAD)<br/>3.6.1-SNAPSHOT | :white_check_mark:<br/>01:23 m | :white_check_mark:<br/>01:36 m | :white_check_mark:<br/>01:53 m | :white_check_mark:<br/>02:08 m | :white_check_mark:<br/>02:09 m |
| [maven-doap-plugin](https://github.com/apache/maven-doap-plugin/tree/HEAD)<br/>3.0.0-M2-SNAPSHOT | :white_check_mark:<br/>20.0 s | :white_check_mark:<br/>15.1 s | :white_check_mark:<br/>17.0 s | :white_check_mark:<br/>17.4 s | :white_check_mark:<br/>17.5 s |
| [maven-javadoc-plugin](https://github.com/apache/maven-javadoc-plugin/tree/HEAD)<br/>3.12.1-SNAPSHOT | :x:<br/>03:40 m | :x:<br/>03:54 m | :x:<br/>04:24 m | :x:<br/>04:35 m | :x:<br/>04:34 m |
| [maven-jdeps-plugin](https://github.com/apache/maven-jdeps-plugin/tree/HEAD)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>12.4 s | :white_check_mark:<br/>15.8 s | :white_check_mark:<br/>19.6 s | :white_check_mark:<br/>21.0 s | :white_check_mark:<br/>21.2 s |
| [maven-pmd-plugin](https://github.com/apache/maven-pmd-plugin/tree/HEAD)<br/>3.28.1-SNAPSHOT | :white_check_mark:<br/>02:16 m | :white_check_mark:<br/>02:29 m | :white_check_mark:<br/>02:42 m | :white_check_mark:<br/>02:57 m | :white_check_mark:<br/>02:56 m |
| [maven-project-info-reports-plugin](https://github.com/apache/maven-project-info-reports-plugin/tree/HEAD)<br/>3.9.1-SNAPSHOT | :white_check_mark:<br/>01:28 m | :white_check_mark:<br/>01:31 m | :white_check_mark:<br/>01:38 m | :white_check_mark:<br/>01:44 m | :white_check_mark:<br/>01:45 m |

## tools

|                  | 3.9.15 | 3.10.0-rc-1 | 4.0.0-rc-5 | 4.0.0-rc-6 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [archetype](https://github.com/apache/maven-archetype/tree/master)<br/>3.4.2-SNAPSHOT | :white_check_mark:<br/>01:30 m | :white_check_mark:<br/>01:39 m | :white_check_mark:<br/>02:03 m | :white_check_mark:<br/>02:02 m | :white_check_mark:<br/>02:05 m |
| [enforcer](https://github.com/apache/maven-enforcer/tree/master)<br/>3.6.4-SNAPSHOT | :white_check_mark:<br/>02:16 m | :white_check_mark:<br/>02:53 m | :white_check_mark:<br/>03:35 m | :white_check_mark:<br/>03:40 m | :white_check_mark:<br/>03:42 m |
| [maven-antrun-plugin](https://github.com/apache/maven-antrun-plugin/tree/master)<br/>3.2.1-SNAPSHOT | :white_check_mark:<br/>31.7 s | :white_check_mark:<br/>41.8 s | :white_check_mark:<br/>51.9 s | :white_check_mark:<br/>56.3 s | :white_check_mark:<br/>56.6 s |
| [maven-artifact-plugin](https://github.com/apache/maven-artifact-plugin/tree/master)<br/>3.6.2-SNAPSHOT | :white_check_mark:<br/>46.8 s | :white_check_mark:<br/>49.1 s | :white_check_mark:<br/>58.4 s | :white_check_mark:<br/>01:02 m | :white_check_mark:<br/>01:02 m |
| [maven-assembly-plugin](https://github.com/apache/maven-assembly-plugin/tree/master)<br/>3.8.1-SNAPSHOT | :white_check_mark:<br/>03:52 m | :white_check_mark:<br/>04:28 m | :x:<br/>05:32 m | :white_check_mark:<br/>06:28 m | :white_check_mark:<br/>06:26 m |
| [maven-dependency-plugin](https://github.com/apache/maven-dependency-plugin/tree/master)<br/>3.11.1-SNAPSHOT | :white_check_mark:<br/>02:47 m | :white_check_mark:<br/>02:54 m | :white_check_mark:<br/>03:26 m | :white_check_mark:<br/>03:34 m | :white_check_mark:<br/>03:34 m |
| [maven-gpg-plugin](https://github.com/apache/maven-gpg-plugin/tree/master)<br/>3.2.9-SNAPSHOT | :white_check_mark:<br/>01:25 m | :white_check_mark:<br/>01:30 m | :white_check_mark:<br/>01:51 m | :white_check_mark:<br/>02:00 m | :white_check_mark:<br/>02:00 m |
| [maven-help-plugin](https://github.com/apache/maven-help-plugin/tree/master)<br/>3.5.3-SNAPSHOT | :white_check_mark:<br/>39.5 s | :white_check_mark:<br/>49.7 s | :x:<br/>58.5 s | :x:<br/>59.2 s | :x:<br/>59.1 s |
| [maven-invoker-plugin](https://github.com/apache/maven-invoker-plugin/tree/master)<br/>3.10.2-SNAPSHOT | :white_check_mark:<br/>03:00 m | :white_check_mark:<br/>03:52 m | :white_check_mark:<br/>04:59 m | :white_check_mark:<br/>05:39 m | :white_check_mark:<br/>05:40 m |
| [maven-jarsigner-plugin](https://github.com/apache/maven-jarsigner-plugin/tree/master)<br/>3.1.1-SNAPSHOT | :white_check_mark:<br/>19.4 s | :white_check_mark:<br/>23.2 s | :white_check_mark:<br/>26.3 s | :white_check_mark:<br/>29.0 s | :white_check_mark:<br/>29.3 s |
| [maven-jdeprscan-plugin](https://github.com/apache/maven-jdeprscan-plugin/tree/master)<br/>3.0.1-SNAPSHOT | :white_check_mark:<br/>13.7 s | :white_check_mark:<br/>16.5 s | :white_check_mark:<br/>18.9 s | :white_check_mark:<br/>20.0 s | :white_check_mark:<br/>19.9 s |
| [maven-remote-resources-plugin](https://github.com/apache/maven-remote-resources-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>31.4 s | :white_check_mark:<br/>33.7 s | :white_check_mark:<br/>45.5 s | :white_check_mark:<br/>49.9 s | :white_check_mark:<br/>49.4 s |
| [maven-scm-publish-plugin](https://github.com/apache/maven-scm-publish-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>18.8 s | :white_check_mark:<br/>22.3 s | :x:<br/>23.3 s | :white_check_mark:<br/>25.1 s | :white_check_mark:<br/>25.2 s |
| [maven-scripting-plugin](https://github.com/apache/maven-scripting-plugin/tree/master)<br/>3.1.1-SNAPSHOT | :white_check_mark:<br/>8.4 s | :white_check_mark:<br/>10.4 s | :white_check_mark:<br/>11.9 s | :white_check_mark:<br/>12.2 s | :white_check_mark:<br/>12.0 s |
| [maven-toolchains-plugin](https://github.com/apache/maven-toolchains-plugin/tree/master)<br/>3.3.1-SNAPSHOT | :white_check_mark:<br/>11.5 s | :white_check_mark:<br/>15.1 s | :x:<br/>19.4 s | :white_check_mark:<br/>19.5 s | :white_check_mark:<br/>19.4 s |
| [plugin-tools](https://github.com/apache/maven-plugin-tools/tree/master)<br/>4.0.0-beta-3-SNAPSHOT | :white_check_mark:<br/>02:12 m | :white_check_mark:<br/>02:25 m | :x:<br/>02:13 m | :white_check_mark:<br/>03:05 m | :white_check_mark:<br/>03:04 m |
| [release](https://github.com/apache/maven-release/tree/master)<br/>3.3.2-SNAPSHOT | :white_check_mark:<br/>01:59 m | :white_check_mark:<br/>02:20 m | :x:<br/>02:54 m | :x:<br/>03:01 m | :x:<br/>02:59 m |
| [scm](https://github.com/apache/maven-scm/tree/master)<br/>2.2.2-SNAPSHOT | :x:<br/>54.4 s | :x:<br/>52.6 s | :x:<br/>52.2 s | :x:<br/>53.5 s | :x:<br/>52.3 s |

