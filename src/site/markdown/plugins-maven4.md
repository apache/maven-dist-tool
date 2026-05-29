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
| [maven-clean-plugin](https://github.com/apache/maven-clean-plugin/tree/maven-clean-plugin-3.x)<br>3.5.1-SNAPSHOT | :white_check_mark:<br>&nbsp;14.606 s | :white_check_mark:<br>&nbsp;19.966 s | :white_check_mark:<br>&nbsp;21.886 s | :white_check_mark:<br>&nbsp;23.705 s | :white_check_mark:<br>&nbsp;24.762 s |
| [maven-compiler-plugin](https://github.com/apache/maven-compiler-plugin/tree/maven-compiler-plugin-3.x)<br>3.15.1-SNAPSHOT | :white_check_mark:<br>&nbsp;02:04 m | :x:<br>&nbsp;02:33 m | :white_check_mark:<br>&nbsp;02:59 m | :x:<br>&nbsp;03:05 m | :x:<br>&nbsp;03:22 m |
| [maven-deploy-plugin](https://github.com/apache/maven-deploy-plugin/tree/maven-deploy-plugin-3.x)<br>3.1.5-SNAPSHOT | :white_check_mark:<br>&nbsp;51.145 s | :white_check_mark:<br>&nbsp;01:06 m | :x:<br>&nbsp;01:14 m | :x:<br>&nbsp;01:18 m | :x:<br>&nbsp;01:21 m |
| [maven-install-plugin](https://github.com/apache/maven-install-plugin/tree/maven-install-plugin-3.x)<br>3.1.5-SNAPSHOT | :white_check_mark:<br>&nbsp;34.772 s | :x:<br>&nbsp;50.204 s | :white_check_mark:<br>&nbsp;52.051 s | :white_check_mark:<br>&nbsp;56.665 s | :white_check_mark:<br>&nbsp;57.382 s |
| [maven-resources-plugin](https://github.com/apache/maven-resources-plugin/tree/maven-resources-plugin-3.x)<br>3.5.1-SNAPSHOT | :white_check_mark:<br>&nbsp;28.234 s | :white_check_mark:<br>&nbsp;38.424 s | :x:<br>&nbsp;43.651 s | :x:<br>&nbsp;46.586 s | :x:<br>&nbsp;48.037 s |
| [maven-site-plugin](https://github.com/apache/maven-site-plugin/tree/master)<br>3.22.1-SNAPSHOT | :white_check_mark:<br>&nbsp;02:29 m | :x:<br>&nbsp;02:44 m | :x:<br>&nbsp;03:13 m | :x:<br>&nbsp;03:23 m | :x:<br>&nbsp;03:24 m |
| [maven-verifier-plugin](https://github.com/apache/maven-verifier-plugin/tree/master)<br>3.0.0-SNAPSHOT | :white_check_mark:<br>&nbsp;5.310 s | :white_check_mark:<br>&nbsp;7.622 s | :white_check_mark:<br>&nbsp;7.293 s | :white_check_mark:<br>&nbsp;8.600 s | :white_check_mark:<br>&nbsp;8.947 s |
| [surefire](https://github.com/apache/maven-surefire/tree/HEAD)<br>3.6.0-SNAPSHOT | :x:<br>&nbsp;05:36 m | :x:<br>&nbsp;06:09 m | :x:<br>&nbsp;03:06 m | :x:<br>&nbsp;03:19 m | :x:<br>&nbsp;03:19 m |

## packaging

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [maven-acr-plugin](https://github.com/apache/maven-acr-plugin/tree/master)<br>3.2.1-SNAPSHOT | :white_check_mark:<br>&nbsp;8.593 s | :white_check_mark:<br>&nbsp;12.050 s | :white_check_mark:<br>&nbsp;12.287 s | :white_check_mark:<br>&nbsp;13.241 s | :white_check_mark:<br>&nbsp;14.382 s |
| [maven-ear-plugin](https://github.com/apache/maven-ear-plugin/tree/master)<br>3.4.1-SNAPSHOT | :white_check_mark:<br>&nbsp;49.641 s | :white_check_mark:<br>&nbsp;01:01 m | :x:<br>&nbsp;03:21 m | :x:<br>&nbsp;03:36 m | :x:<br>&nbsp;03:41 m |
| [maven-ejb-plugin](https://github.com/apache/maven-ejb-plugin/tree/master)<br>3.3.1-SNAPSHOT | :white_check_mark:<br>&nbsp;15.911 s | :white_check_mark:<br>&nbsp;20.212 s | :white_check_mark:<br>&nbsp;21.478 s | :white_check_mark:<br>&nbsp;24.481 s | :white_check_mark:<br>&nbsp;25.737 s |
| [maven-jar-plugin](https://github.com/apache/maven-jar-plugin/tree/maven-jar-plugin-3.x)<br>3.5.1-SNAPSHOT | :white_check_mark:<br>&nbsp;55.243 s | :white_check_mark:<br>&nbsp;01:05 m | :white_check_mark:<br>&nbsp;01:14 m | :white_check_mark:<br>&nbsp;01:20 m | :white_check_mark:<br>&nbsp;01:22 m |
| [maven-jlink-plugin](https://github.com/apache/maven-jlink-plugin/tree/master)<br>3.3.1-SNAPSHOT | :x:<br>&nbsp;01:23 m | :x:<br>&nbsp;01:27 m | :x:<br>&nbsp;01:37 m | :x:<br>&nbsp;01:40 m | :x:<br>&nbsp;01:42 m |
| [maven-jmod-plugin](https://github.com/apache/maven-jmod-plugin/tree/master)<br>3.0.1-SNAPSHOT | :white_check_mark:<br>&nbsp;29.940 s | :white_check_mark:<br>&nbsp;36.597 s | :x:<br>&nbsp;39.967 s | :x:<br>&nbsp;42.569 s | :x:<br>&nbsp;44.909 s |
| [maven-rar-plugin](https://github.com/apache/maven-rar-plugin/tree/master)<br>3.1.1-SNAPSHOT | :white_check_mark:<br>&nbsp;8.092 s | :white_check_mark:<br>&nbsp;10.641 s | :white_check_mark:<br>&nbsp;9.977 s | :white_check_mark:<br>&nbsp;11.791 s | :white_check_mark:<br>&nbsp;12.190 s |
| [maven-shade-plugin](https://github.com/apache/maven-shade-plugin/tree/master)<br>3.6.3-SNAPSHOT | :white_check_mark:<br>&nbsp;02:03 m | :white_check_mark:<br>&nbsp;02:32 m | :x:<br>&nbsp;02:43 m | :x:<br>&nbsp;02:49 m | :x:<br>&nbsp;02:54 m |
| [maven-source-plugin](https://github.com/apache/maven-source-plugin/tree/master)<br>3.4.1-SNAPSHOT | :white_check_mark:<br>&nbsp;31.936 s | :white_check_mark:<br>&nbsp;39.853 s | :x:<br>&nbsp;44.391 s | :x:<br>&nbsp;47.596 s | :white_check_mark:<br>&nbsp;51.866 s |
| [maven-war-plugin](https://github.com/apache/maven-war-plugin/tree/master)<br>3.5.2-SNAPSHOT | :white_check_mark:<br>&nbsp;54.112 s | :white_check_mark:<br>&nbsp;01:03 m | :white_check_mark:<br>&nbsp;01:13 m | :white_check_mark:<br>&nbsp;01:16 m | :white_check_mark:<br>&nbsp;01:19 m |

## reporting

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [jxr](https://github.com/apache/maven-jxr/tree/master)<br>3.6.1-SNAPSHOT | :white_check_mark:<br>&nbsp;27.792 s | :white_check_mark:<br>&nbsp;30.551 s | :white_check_mark:<br>&nbsp;32.697 s | :white_check_mark:<br>&nbsp;35.094 s | :white_check_mark:<br>&nbsp;36.029 s |
| [maven-changelog-plugin](https://github.com/apache/maven-changelog-plugin/tree/master)<br>3.0.0-M3-SNAPSHOT | :white_check_mark:<br>&nbsp;14.585 s | :white_check_mark:<br>&nbsp;16.745 s | :white_check_mark:<br>&nbsp;16.166 s | :white_check_mark:<br>&nbsp;18.401 s | :white_check_mark:<br>&nbsp;18.731 s |
| [maven-changes-plugin](https://github.com/apache/maven-changes-plugin/tree/master)<br>3.0.0-M4-SNAPSHOT | :white_check_mark:<br>&nbsp;31.414 s | :white_check_mark:<br>&nbsp;38.627 s | :white_check_mark:<br>&nbsp;38.714 s | :white_check_mark:<br>&nbsp;45.822 s | :white_check_mark:<br>&nbsp;47.177 s |
| [maven-checkstyle-plugin](https://github.com/apache/maven-checkstyle-plugin/tree/master)<br>3.6.1-SNAPSHOT | :white_check_mark:<br>&nbsp;01:26 m | :white_check_mark:<br>&nbsp;01:38 m | :white_check_mark:<br>&nbsp;01:49 m | :white_check_mark:<br>&nbsp;01:58 m | :white_check_mark:<br>&nbsp;02:01 m |
| [maven-doap-plugin](https://github.com/apache/maven-doap-plugin/tree/master)<br>3.0.0-M2-SNAPSHOT | :white_check_mark:<br>&nbsp;15.701 s | :white_check_mark:<br>&nbsp;23.188 s | :white_check_mark:<br>&nbsp;15.724 s | :white_check_mark:<br>&nbsp;17.724 s | :white_check_mark:<br>&nbsp;17.281 s |
| [maven-javadoc-plugin](https://github.com/apache/maven-javadoc-plugin/tree/master)<br>3.12.1-SNAPSHOT | :x:<br>&nbsp;03:46 m | :x:<br>&nbsp;04:03 m | :x:<br>&nbsp;04:23 m | :x:<br>&nbsp;04:31 m | :x:<br>&nbsp;04:36 m |
| [maven-jdeps-plugin](https://github.com/apache/maven-jdeps-plugin/tree/master)<br>3.2.1-SNAPSHOT | :x:<br>&nbsp;13.683 s | :white_check_mark:<br>&nbsp;15.904 s | :x:<br>&nbsp;18.213 s | :x:<br>&nbsp;20.435 s | :x:<br>&nbsp;21.249 s |
| [maven-pmd-plugin](https://github.com/apache/maven-pmd-plugin/tree/master)<br>3.28.1-SNAPSHOT | :white_check_mark:<br>&nbsp;02:22 m | :white_check_mark:<br>&nbsp;02:29 m | :white_check_mark:<br>&nbsp;02:42 m | :white_check_mark:<br>&nbsp;02:52 m | :white_check_mark:<br>&nbsp;02:53 m |
| [maven-project-info-reports-plugin](https://github.com/apache/maven-project-info-reports-plugin/tree/master)<br>3.9.1-SNAPSHOT | :white_check_mark:<br>&nbsp;01:28 m | :white_check_mark:<br>&nbsp;01:36 m | :white_check_mark:<br>&nbsp;01:38 m | :white_check_mark:<br>&nbsp;01:42 m | :white_check_mark:<br>&nbsp;01:44 m |

## tools

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [archetype](https://github.com/apache/maven-archetype/tree/master)<br>3.4.2-SNAPSHOT | :white_check_mark:<br>&nbsp;01:31 m | :x:<br>&nbsp;01:37 m | :white_check_mark:<br>&nbsp;02:05 m | :white_check_mark:<br>&nbsp;02:08 m | :x:<br>&nbsp;02:09 m |
| [enforcer](https://github.com/apache/maven-enforcer/tree/master)<br>3.6.4-SNAPSHOT | :white_check_mark:<br>&nbsp;02:18 m | :white_check_mark:<br>&nbsp;02:52 m | :white_check_mark:<br>&nbsp;03:32 m | :white_check_mark:<br>&nbsp;03:33 m | :white_check_mark:<br>&nbsp;03:43 m |
| [maven-antrun-plugin](https://github.com/apache/maven-antrun-plugin/tree/master)<br>3.2.1-SNAPSHOT | :white_check_mark:<br>&nbsp;32.530 s | :white_check_mark:<br>&nbsp;41.818 s | :white_check_mark:<br>&nbsp;49.233 s | :white_check_mark:<br>&nbsp;52.936 s | :white_check_mark:<br>&nbsp;54.971 s |
| [maven-artifact-plugin](https://github.com/apache/maven-artifact-plugin/tree/master)<br>3.6.2-SNAPSHOT | :white_check_mark:<br>&nbsp;40.304 s | :white_check_mark:<br>&nbsp;48.772 s | :x:<br>&nbsp;1.529 s | :x:<br>&nbsp;45.246 s | :x:<br>&nbsp;45.122 s |
| [maven-assembly-plugin](https://github.com/apache/maven-assembly-plugin/tree/master)<br>3.8.1-SNAPSHOT | :white_check_mark:<br>&nbsp;03:44 m | :x:<br>&nbsp;04:34 m | :x:<br>&nbsp;04:55 m | :x:<br>&nbsp;05:12 m | :x:<br>&nbsp;05:27 m |
| [maven-dependency-plugin](https://github.com/apache/maven-dependency-plugin/tree/master)<br>3.11.1-SNAPSHOT | :white_check_mark:<br>&nbsp;02:45 m | :white_check_mark:<br>&nbsp;03:00 m | :white_check_mark:<br>&nbsp;03:16 m | :white_check_mark:<br>&nbsp;03:24 m | :white_check_mark:<br>&nbsp;03:57 m |
| [maven-gpg-plugin](https://github.com/apache/maven-gpg-plugin/tree/master)<br>3.2.9-SNAPSHOT | :white_check_mark:<br>&nbsp;01:23 m | :white_check_mark:<br>&nbsp;01:30 m | :x:<br>&nbsp;1.257 s | :white_check_mark:<br>&nbsp;01:51 m | :white_check_mark:<br>&nbsp;01:53 m |
| [maven-help-plugin](https://github.com/apache/maven-help-plugin/tree/master)<br>3.5.2-SNAPSHOT | :white_check_mark:<br>&nbsp;38.799 s | :white_check_mark:<br>&nbsp;49.313 s | :x:<br>&nbsp;56.315 s | :x:<br>&nbsp;56.723 s | :x:<br>&nbsp;01:01 m |
| [maven-invoker-plugin](https://github.com/apache/maven-invoker-plugin/tree/master)<br>3.10.2-SNAPSHOT | :white_check_mark:<br>&nbsp;03:00 m | :white_check_mark:<br>&nbsp;03:57 m | :white_check_mark:<br>&nbsp;04:44 m | :white_check_mark:<br>&nbsp;04:58 m | :white_check_mark:<br>&nbsp;05:07 m |
| [maven-jarsigner-plugin](https://github.com/apache/maven-jarsigner-plugin/tree/master)<br>3.1.1-SNAPSHOT | :x:<br>&nbsp;4.568 s | :x:<br>&nbsp;4.754 s | :x:<br>&nbsp;4.391 s | :x:<br>&nbsp;4.831 s | :x:<br>&nbsp;4.770 s |
| [maven-jdeprscan-plugin](https://github.com/apache/maven-jdeprscan-plugin/tree/master)<br>3.0.1-SNAPSHOT | :white_check_mark:<br>&nbsp;13.869 s | :white_check_mark:<br>&nbsp;16.316 s | :x:<br>&nbsp;16.256 s | :x:<br>&nbsp;17.445 s | :x:<br>&nbsp;18.080 s |
| [maven-remote-resources-plugin](https://github.com/apache/maven-remote-resources-plugin/tree/master)<br>3.3.1-SNAPSHOT | :white_check_mark:<br>&nbsp;32.203 s | :white_check_mark:<br>&nbsp;34.277 s | :white_check_mark:<br>&nbsp;43.358 s | :white_check_mark:<br>&nbsp;46.267 s | :white_check_mark:<br>&nbsp;47.539 s |
| [maven-scm-publish-plugin](https://github.com/apache/maven-scm-publish-plugin/tree/master)<br>3.3.1-SNAPSHOT | :white_check_mark:<br>&nbsp;19.005 s | :white_check_mark:<br>&nbsp;22.470 s | :x:<br>&nbsp;16.718 s | :x:<br>&nbsp;18.129 s | :x:<br>&nbsp;19.913 s |
| [maven-scripting-plugin](https://github.com/apache/maven-scripting-plugin/tree/master)<br>3.1.1-SNAPSHOT | :white_check_mark:<br>&nbsp;8.593 s | :white_check_mark:<br>&nbsp;10.029 s | :white_check_mark:<br>&nbsp;11.284 s | :white_check_mark:<br>&nbsp;12.154 s | :white_check_mark:<br>&nbsp;12.156 s |
| [maven-stage-plugin](https://github.com/apache/maven-stage-plugin/tree/master)<br>1.1-SNAPSHOT | :white_check_mark:<br>&nbsp;3.367 s | :white_check_mark:<br>&nbsp;3.572 s | :white_check_mark:<br>&nbsp;3.205 s | :white_check_mark:<br>&nbsp;3.679 s | :white_check_mark:<br>&nbsp;3.516 s |
| [maven-toolchains-plugin](https://github.com/apache/maven-toolchains-plugin/tree/master)<br>3.2.1-SNAPSHOT | :white_check_mark:<br>&nbsp;10.817 s | :white_check_mark:<br>&nbsp;14.038 s | :x:<br>&nbsp;15.052 s | :x:<br>&nbsp;17.549 s | :x:<br>&nbsp;18.255 s |
| [plugin-tools](https://github.com/apache/maven-plugin-tools/tree/master)<br>4.0.0-beta-3-SNAPSHOT | :white_check_mark:<br>&nbsp;01:51 m | :white_check_mark:<br>&nbsp;02:08 m | :white_check_mark:<br>&nbsp;02:19 m | :white_check_mark:<br>&nbsp;02:29 m | :white_check_mark:<br>&nbsp;02:31 m |
| [release](https://github.com/apache/maven-release/tree/master)<br>3.3.2-SNAPSHOT | :white_check_mark:<br>&nbsp;02:03 m | :white_check_mark:<br>&nbsp;02:18 m | :white_check_mark:<br>&nbsp;02:37 m | :white_check_mark:<br>&nbsp;02:43 m | :white_check_mark:<br>&nbsp;02:59 m |
| [scm](https://github.com/apache/maven-scm/tree/master)<br>2.2.2-SNAPSHOT | :white_check_mark:<br>&nbsp;02:22 m | :white_check_mark:<br>&nbsp;02:30 m | :white_check_mark:<br>&nbsp;02:25 m | :white_check_mark:<br>&nbsp;02:24 m | :white_check_mark:<br>&nbsp;02:26 m |

