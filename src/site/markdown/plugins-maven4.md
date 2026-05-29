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

WIP (should be in [Maven 4.0.0-RC6](https://github.com/apache/maven/milestone/127)):

- [PR #11868](https://github.com/apache/maven/pull/11868) for `maven-source-plugin`
- [PR #11869](https://github.com/apache/maven/pull/11869) for `plugin-tools`
- [issue #11973](https://github.com/apache/maven/issues/11973) for `maven-toolchain-plugin`


## core

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [maven-clean-plugin](https://github.com/apache/maven-clean-plugin/tree/maven-clean-plugin-3.x) 3.5.1-SNAPSHOT | :white_check_mark:<br> 14.606 s | :white_check_mark:<br> 19.966 s | :white_check_mark:<br> 21.886 s | :white_check_mark:<br> 23.705 s | :white_check_mark:<br> 24.762 s |
| [maven-compiler-plugin](https://github.com/apache/maven-compiler-plugin/tree/maven-compiler-plugin-3.x) 3.15.1-SNAPSHOT | :white_check_mark:<br> 02:04 min | :x:<br> 02:33 min | :white_check_mark:<br> 02:59 min | :x:<br> 03:05 min | :x:<br> 03:22 min |
| [maven-deploy-plugin](https://github.com/apache/maven-deploy-plugin/tree/maven-deploy-plugin-3.x) 3.1.5-SNAPSHOT | :white_check_mark:<br> 51.145 s | :white_check_mark:<br> 01:06 min | :x:<br> 01:14 min | :x:<br> 01:18 min | :x:<br> 01:21 min |
| [maven-install-plugin](https://github.com/apache/maven-install-plugin/tree/maven-install-plugin-3.x) 3.1.5-SNAPSHOT | :white_check_mark:<br> 34.772 s | :x:<br> 50.204 s | :white_check_mark:<br> 52.051 s | :white_check_mark:<br> 56.665 s | :white_check_mark:<br> 57.382 s |
| [maven-resources-plugin](https://github.com/apache/maven-resources-plugin/tree/maven-resources-plugin-3.x) 3.5.1-SNAPSHOT | :white_check_mark:<br> 28.234 s | :white_check_mark:<br> 38.424 s | :x:<br> 43.651 s | :x:<br> 46.586 s | :x:<br> 48.037 s |
| [maven-site-plugin](https://github.com/apache/maven-site-plugin/tree/master) 3.22.1-SNAPSHOT | :white_check_mark:<br> 02:29 min | :x:<br> 02:44 min | :x:<br> 03:13 min | :x:<br> 03:23 min | :x:<br> 03:24 min |
| [maven-verifier-plugin](https://github.com/apache/maven-verifier-plugin/tree/master) 3.0.0-SNAPSHOT | :white_check_mark:<br> 5.310 s | :white_check_mark:<br> 7.622 s | :white_check_mark:<br> 7.293 s | :white_check_mark:<br> 8.600 s | :white_check_mark:<br> 8.947 s |
| [surefire](https://github.com/apache/maven-surefire/tree/HEAD) 3.6.0-SNAPSHOT | :x:<br> 05:36 min | :x:<br> 06:09 min | :x:<br> 03:06 min | :x:<br> 03:19 min | :x:<br> 03:19 min |

## packaging

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [maven-acr-plugin](https://github.com/apache/maven-acr-plugin/tree/master) 3.2.1-SNAPSHOT | :white_check_mark:<br> 8.593 s | :white_check_mark:<br> 12.050 s | :white_check_mark:<br> 12.287 s | :white_check_mark:<br> 13.241 s | :white_check_mark:<br> 14.382 s |
| [maven-ear-plugin](https://github.com/apache/maven-ear-plugin/tree/master) 3.4.1-SNAPSHOT | :white_check_mark:<br> 49.641 s | :white_check_mark:<br> 01:01 min | :x:<br> 03:21 min | :x:<br> 03:36 min | :x:<br> 03:41 min |
| [maven-ejb-plugin](https://github.com/apache/maven-ejb-plugin/tree/master) 3.3.1-SNAPSHOT | :white_check_mark:<br> 15.911 s | :white_check_mark:<br> 20.212 s | :white_check_mark:<br> 21.478 s | :white_check_mark:<br> 24.481 s | :white_check_mark:<br> 25.737 s |
| [maven-jar-plugin](https://github.com/apache/maven-jar-plugin/tree/maven-jar-plugin-3.x) 3.5.1-SNAPSHOT | :white_check_mark:<br> 55.243 s | :white_check_mark:<br> 01:05 min | :white_check_mark:<br> 01:14 min | :white_check_mark:<br> 01:20 min | :white_check_mark:<br> 01:22 min |
| [maven-jlink-plugin](https://github.com/apache/maven-jlink-plugin/tree/master) 3.3.1-SNAPSHOT | :x:<br> 01:23 min | :x:<br> 01:27 min | :x:<br> 01:37 min | :x:<br> 01:40 min | :x:<br> 01:42 min |
| [maven-jmod-plugin](https://github.com/apache/maven-jmod-plugin/tree/master) 3.0.1-SNAPSHOT | :white_check_mark:<br> 29.940 s | :white_check_mark:<br> 36.597 s | :x:<br> 39.967 s | :x:<br> 42.569 s | :x:<br> 44.909 s |
| [maven-rar-plugin](https://github.com/apache/maven-rar-plugin/tree/master) 3.1.1-SNAPSHOT | :white_check_mark:<br> 8.092 s | :white_check_mark:<br> 10.641 s | :white_check_mark:<br> 9.977 s | :white_check_mark:<br> 11.791 s | :white_check_mark:<br> 12.190 s |
| [maven-shade-plugin](https://github.com/apache/maven-shade-plugin/tree/master) 3.6.3-SNAPSHOT | :white_check_mark:<br> 02:03 min | :white_check_mark:<br> 02:32 min | :x:<br> 02:43 min | :x:<br> 02:49 min | :x:<br> 02:54 min |
| [maven-source-plugin](https://github.com/apache/maven-source-plugin/tree/master) 3.4.1-SNAPSHOT | :white_check_mark:<br> 31.936 s | :white_check_mark:<br> 39.853 s | :x:<br> 44.391 s | :x:<br> 47.596 s | :white_check_mark:<br> 51.866 s |
| [maven-war-plugin](https://github.com/apache/maven-war-plugin/tree/master) 3.5.2-SNAPSHOT | :white_check_mark:<br> 54.112 s | :white_check_mark:<br> 01:03 min | :white_check_mark:<br> 01:13 min | :white_check_mark:<br> 01:16 min | :white_check_mark:<br> 01:19 min |

## reporting

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [jxr](https://github.com/apache/maven-jxr/tree/master) 3.6.1-SNAPSHOT | :white_check_mark:<br> 27.792 s | :white_check_mark:<br> 30.551 s | :white_check_mark:<br> 32.697 s | :white_check_mark:<br> 35.094 s | :white_check_mark:<br> 36.029 s |
| [maven-changelog-plugin](https://github.com/apache/maven-changelog-plugin/tree/master) 3.0.0-M3-SNAPSHOT | :white_check_mark:<br> 14.585 s | :white_check_mark:<br> 16.745 s | :white_check_mark:<br> 16.166 s | :white_check_mark:<br> 18.401 s | :white_check_mark:<br> 18.731 s |
| [maven-changes-plugin](https://github.com/apache/maven-changes-plugin/tree/master) 3.0.0-M4-SNAPSHOT | :white_check_mark:<br> 31.414 s | :white_check_mark:<br> 38.627 s | :white_check_mark:<br> 38.714 s | :white_check_mark:<br> 45.822 s | :white_check_mark:<br> 47.177 s |
| [maven-checkstyle-plugin](https://github.com/apache/maven-checkstyle-plugin/tree/master) 3.6.1-SNAPSHOT | :white_check_mark:<br> 01:26 min | :white_check_mark:<br> 01:38 min | :white_check_mark:<br> 01:49 min | :white_check_mark:<br> 01:58 min | :white_check_mark:<br> 02:01 min |
| [maven-doap-plugin](https://github.com/apache/maven-doap-plugin/tree/master) 3.0.0-M2-SNAPSHOT | :white_check_mark:<br> 15.701 s | :white_check_mark:<br> 23.188 s | :white_check_mark:<br> 15.724 s | :white_check_mark:<br> 17.724 s | :white_check_mark:<br> 17.281 s |
| [maven-javadoc-plugin](https://github.com/apache/maven-javadoc-plugin/tree/master) 3.12.1-SNAPSHOT | :x:<br> 03:46 min | :x:<br> 04:03 min | :x:<br> 04:23 min | :x:<br> 04:31 min | :x:<br> 04:36 min |
| [maven-jdeps-plugin](https://github.com/apache/maven-jdeps-plugin/tree/master) 3.2.1-SNAPSHOT | :x:<br> 13.683 s | :white_check_mark:<br> 15.904 s | :x:<br> 18.213 s | :x:<br> 20.435 s | :x:<br> 21.249 s |
| [maven-pmd-plugin](https://github.com/apache/maven-pmd-plugin/tree/master) 3.28.1-SNAPSHOT | :white_check_mark:<br> 02:22 min | :white_check_mark:<br> 02:29 min | :white_check_mark:<br> 02:42 min | :white_check_mark:<br> 02:52 min | :white_check_mark:<br> 02:53 min |
| [maven-project-info-reports-plugin](https://github.com/apache/maven-project-info-reports-plugin/tree/master) 3.9.1-SNAPSHOT | :white_check_mark:<br> 01:28 min | :white_check_mark:<br> 01:36 min | :white_check_mark:<br> 01:38 min | :white_check_mark:<br> 01:42 min | :white_check_mark:<br> 01:44 min |

## tools

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [archetype](https://github.com/apache/maven-archetype/tree/master) 3.4.2-SNAPSHOT | :white_check_mark:<br> 01:31 min | :x:<br> 01:37 min | :white_check_mark:<br> 02:05 min | :white_check_mark:<br> 02:08 min | :x:<br> 02:09 min |
| [enforcer](https://github.com/apache/maven-enforcer/tree/master) 3.6.4-SNAPSHOT | :white_check_mark:<br> 02:18 min | :white_check_mark:<br> 02:52 min | :white_check_mark:<br> 03:32 min | :white_check_mark:<br> 03:33 min | :white_check_mark:<br> 03:43 min |
| [maven-antrun-plugin](https://github.com/apache/maven-antrun-plugin/tree/master) 3.2.1-SNAPSHOT | :white_check_mark:<br> 32.530 s | :white_check_mark:<br> 41.818 s | :white_check_mark:<br> 49.233 s | :white_check_mark:<br> 52.936 s | :white_check_mark:<br> 54.971 s |
| [maven-artifact-plugin](https://github.com/apache/maven-artifact-plugin/tree/master) 3.6.2-SNAPSHOT | :white_check_mark:<br> 40.304 s | :white_check_mark:<br> 48.772 s | :x:<br> 1.529 s | :x:<br> 45.246 s | :x:<br> 45.122 s |
| [maven-assembly-plugin](https://github.com/apache/maven-assembly-plugin/tree/master) 3.8.1-SNAPSHOT | :white_check_mark:<br> 03:44 min | :x:<br> 04:34 min | :x:<br> 04:55 min | :x:<br> 05:12 min | :x:<br> 05:27 min |
| [maven-dependency-plugin](https://github.com/apache/maven-dependency-plugin/tree/master) 3.11.1-SNAPSHOT | :white_check_mark:<br> 02:45 min | :white_check_mark:<br> 03:00 min | :white_check_mark:<br> 03:16 min | :white_check_mark:<br> 03:24 min | :white_check_mark:<br> 03:57 min |
| [maven-gpg-plugin](https://github.com/apache/maven-gpg-plugin/tree/master) 3.2.9-SNAPSHOT | :white_check_mark:<br> 01:23 min | :white_check_mark:<br> 01:30 min | :x:<br> 1.257 s | :white_check_mark:<br> 01:51 min | :white_check_mark:<br> 01:53 min |
| [maven-help-plugin](https://github.com/apache/maven-help-plugin/tree/master) 3.5.2-SNAPSHOT | :white_check_mark:<br> 38.799 s | :white_check_mark:<br> 49.313 s | :x:<br> 56.315 s | :x:<br> 56.723 s | :x:<br> 01:01 min |
| [maven-invoker-plugin](https://github.com/apache/maven-invoker-plugin/tree/master) 3.10.2-SNAPSHOT | :white_check_mark:<br> 03:00 min | :white_check_mark:<br> 03:57 min | :white_check_mark:<br> 04:44 min | :white_check_mark:<br> 04:58 min | :white_check_mark:<br> 05:07 min |
| [maven-jarsigner-plugin](https://github.com/apache/maven-jarsigner-plugin/tree/master) 3.1.1-SNAPSHOT | :x:<br> 4.568 s | :x:<br> 4.754 s | :x:<br> 4.391 s | :x:<br> 4.831 s | :x:<br> 4.770 s |
| [maven-jdeprscan-plugin](https://github.com/apache/maven-jdeprscan-plugin/tree/master) 3.0.1-SNAPSHOT | :white_check_mark:<br> 13.869 s | :white_check_mark:<br> 16.316 s | :x:<br> 16.256 s | :x:<br> 17.445 s | :x:<br> 18.080 s |
| [maven-remote-resources-plugin](https://github.com/apache/maven-remote-resources-plugin/tree/master) 3.3.1-SNAPSHOT | :white_check_mark:<br> 32.203 s | :white_check_mark:<br> 34.277 s | :white_check_mark:<br> 43.358 s | :white_check_mark:<br> 46.267 s | :white_check_mark:<br> 47.539 s |
| [maven-scm-publish-plugin](https://github.com/apache/maven-scm-publish-plugin/tree/master) 3.3.1-SNAPSHOT | :white_check_mark:<br> 19.005 s | :white_check_mark:<br> 22.470 s | :x:<br> 16.718 s | :x:<br> 18.129 s | :x:<br> 19.913 s |
| [maven-scripting-plugin](https://github.com/apache/maven-scripting-plugin/tree/master) 3.1.1-SNAPSHOT | :white_check_mark:<br> 8.593 s | :white_check_mark:<br> 10.029 s | :white_check_mark:<br> 11.284 s | :white_check_mark:<br> 12.154 s | :white_check_mark:<br> 12.156 s |
| [maven-stage-plugin](https://github.com/apache/maven-stage-plugin/tree/master) 1.1-SNAPSHOT | :white_check_mark:<br> 3.367 s | :white_check_mark:<br> 3.572 s | :white_check_mark:<br> 3.205 s | :white_check_mark:<br> 3.679 s | :white_check_mark:<br> 3.516 s |
| [maven-toolchains-plugin](https://github.com/apache/maven-toolchains-plugin/tree/master) 3.2.1-SNAPSHOT | :white_check_mark:<br> 10.817 s | :white_check_mark:<br> 14.038 s | :x:<br> 15.052 s | :x:<br> 17.549 s | :x:<br> 18.255 s |
| [plugin-tools](https://github.com/apache/maven-plugin-tools/tree/master) 4.0.0-beta-3-SNAPSHOT | :white_check_mark:<br> 01:51 min | :white_check_mark:<br> 02:08 min | :white_check_mark:<br> 02:19 min | :white_check_mark:<br> 02:29 min | :white_check_mark:<br> 02:31 min |
| [release](https://github.com/apache/maven-release/tree/master) 3.3.2-SNAPSHOT | :white_check_mark:<br> 02:03 min | :white_check_mark:<br> 02:18 min | :white_check_mark:<br> 02:37 min | :white_check_mark:<br> 02:43 min | :white_check_mark:<br> 02:59 min |
| [scm](https://github.com/apache/maven-scm/tree/master) 2.2.2-SNAPSHOT | :white_check_mark:<br> 02:22 min | :white_check_mark:<br> 02:30 min | :white_check_mark:<br> 02:25 min | :white_check_mark:<br> 02:24 min | :white_check_mark:<br> 02:26 min |

