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
| [maven-clean-plugin](https://github.com/apache/maven-clean-plugin/tree/maven-clean-plugin-3.x)<br>3.5.1-SNAPSHOT | :white_check_mark:<br>14.6 s | :white_check_mark:<br>19.9 s | :white_check_mark:<br>21.8 s | :white_check_mark:<br>23.7 s | :white_check_mark:<br>24.7 s |
| [maven-compiler-plugin](https://github.com/apache/maven-compiler-plugin/tree/maven-compiler-plugin-3.x)<br>3.15.1-SNAPSHOT | :white_check_mark:<br>02:03 m | :white_check_mark:<br>02:31 m | :white_check_mark:<br>02:59 m | :x:<br>03:08 m | :white_check_mark:<br>03:13 m |
| [maven-deploy-plugin](https://github.com/apache/maven-deploy-plugin/tree/maven-deploy-plugin-3.x)<br>3.1.5-SNAPSHOT | :white_check_mark:<br>51.1 s | :white_check_mark:<br>01:06 m | :x:<br>01:14 m | :x:<br>01:18 m | :x:<br>01:21 m |
| [maven-install-plugin](https://github.com/apache/maven-install-plugin/tree/maven-install-plugin-3.x)<br>3.1.5-SNAPSHOT | :white_check_mark:<br>36.5 s | :white_check_mark:<br>44.8 s | :white_check_mark:<br>53.2 s | :white_check_mark:<br>57.2 s | :white_check_mark:<br>58.6 s |
| [maven-resources-plugin](https://github.com/apache/maven-resources-plugin/tree/maven-resources-plugin-3.x)<br>3.5.1-SNAPSHOT | :white_check_mark:<br>28.2 s | :white_check_mark:<br>38.4 s | :x:<br>43.6 s | :x:<br>46.5 s | :x:<br>48.0 s |
| [maven-site-plugin](https://github.com/apache/maven-site-plugin/tree/master)<br>3.22.1-SNAPSHOT | :white_check_mark:<br>02:37 m | :white_check_mark:<br>02:48 m | :x:<br>03:21 m | :x:<br>03:28 m | :x:<br>03:38 m |
| [maven-verifier-plugin](https://github.com/apache/maven-verifier-plugin/tree/master)<br>3.0.0-SNAPSHOT | :white_check_mark:<br>5.3 s | :white_check_mark:<br>7.6 s | :white_check_mark:<br>7.2 s | :white_check_mark:<br>8.6 s | :white_check_mark:<br>8.9 s |
| [surefire](https://github.com/apache/maven-surefire/tree/HEAD)<br>3.6.0-SNAPSHOT | :x:<br>05:36 m | :x:<br>06:09 m | :x:<br>03:06 m | :x:<br>03:19 m | :x:<br>03:19 m |

## packaging

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [maven-acr-plugin](https://github.com/apache/maven-acr-plugin/tree/master)<br>3.2.1-SNAPSHOT | :white_check_mark:<br>8.5 s | :white_check_mark:<br>12.0 s | :white_check_mark:<br>12.2 s | :white_check_mark:<br>13.2 s | :white_check_mark:<br>14.3 s |
| [maven-ear-plugin](https://github.com/apache/maven-ear-plugin/tree/master)<br>3.4.1-SNAPSHOT | :white_check_mark:<br>49.6 s | :white_check_mark:<br>01:01 m | :x:<br>03:21 m | :x:<br>03:36 m | :x:<br>03:41 m |
| [maven-ejb-plugin](https://github.com/apache/maven-ejb-plugin/tree/master)<br>3.3.1-SNAPSHOT | :white_check_mark:<br>15.9 s | :white_check_mark:<br>20.2 s | :white_check_mark:<br>21.4 s | :white_check_mark:<br>24.4 s | :white_check_mark:<br>25.7 s |
| [maven-jar-plugin](https://github.com/apache/maven-jar-plugin/tree/maven-jar-plugin-3.x)<br>3.5.1-SNAPSHOT | :white_check_mark:<br>55.2 s | :white_check_mark:<br>01:05 m | :white_check_mark:<br>01:14 m | :white_check_mark:<br>01:20 m | :white_check_mark:<br>01:22 m |
| [maven-jlink-plugin](https://github.com/apache/maven-jlink-plugin/tree/master)<br>3.3.1-SNAPSHOT | :x:<br>01:23 m | :x:<br>01:27 m | :x:<br>01:37 m | :x:<br>01:40 m | :x:<br>01:42 m |
| [maven-jmod-plugin](https://github.com/apache/maven-jmod-plugin/tree/master)<br>3.0.1-SNAPSHOT | :white_check_mark:<br>29.9 s | :white_check_mark:<br>36.5 s | :x:<br>39.9 s | :x:<br>42.5 s | :x:<br>44.9 s |
| [maven-rar-plugin](https://github.com/apache/maven-rar-plugin/tree/master)<br>3.1.1-SNAPSHOT | :white_check_mark:<br>8.0 s | :white_check_mark:<br>10.6 s | :white_check_mark:<br>9.9 s | :white_check_mark:<br>11.7 s | :white_check_mark:<br>12.1 s |
| [maven-shade-plugin](https://github.com/apache/maven-shade-plugin/tree/master)<br>3.6.3-SNAPSHOT | :white_check_mark:<br>02:03 m | :white_check_mark:<br>02:32 m | :x:<br>02:43 m | :x:<br>02:49 m | :x:<br>02:54 m |
| [maven-source-plugin](https://github.com/apache/maven-source-plugin/tree/master)<br>3.4.1-SNAPSHOT | :white_check_mark:<br>31.9 s | :white_check_mark:<br>39.8 s | :x:<br>44.3 s | :x:<br>47.5 s | :white_check_mark:<br>51.8 s |
| [maven-war-plugin](https://github.com/apache/maven-war-plugin/tree/master)<br>3.5.2-SNAPSHOT | :white_check_mark:<br>54.1 s | :white_check_mark:<br>01:03 m | :white_check_mark:<br>01:13 m | :white_check_mark:<br>01:16 m | :white_check_mark:<br>01:19 m |

## reporting

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [jxr](https://github.com/apache/maven-jxr/tree/master)<br>3.6.1-SNAPSHOT | :white_check_mark:<br>27.7 s | :white_check_mark:<br>30.5 s | :white_check_mark:<br>32.6 s | :white_check_mark:<br>35.0 s | :white_check_mark:<br>36.0 s |
| [maven-changelog-plugin](https://github.com/apache/maven-changelog-plugin/tree/master)<br>3.0.0-M3-SNAPSHOT | :white_check_mark:<br>14.5 s | :white_check_mark:<br>16.7 s | :white_check_mark:<br>16.1 s | :white_check_mark:<br>18.4 s | :white_check_mark:<br>18.7 s |
| [maven-changes-plugin](https://github.com/apache/maven-changes-plugin/tree/master)<br>3.0.0-M4-SNAPSHOT | :white_check_mark:<br>31.4 s | :white_check_mark:<br>38.6 s | :white_check_mark:<br>38.7 s | :white_check_mark:<br>45.8 s | :white_check_mark:<br>47.1 s |
| [maven-checkstyle-plugin](https://github.com/apache/maven-checkstyle-plugin/tree/master)<br>3.6.1-SNAPSHOT | :white_check_mark:<br>01:26 m | :white_check_mark:<br>01:38 m | :white_check_mark:<br>01:49 m | :white_check_mark:<br>01:58 m | :white_check_mark:<br>02:01 m |
| [maven-doap-plugin](https://github.com/apache/maven-doap-plugin/tree/master)<br>3.0.0-M2-SNAPSHOT | :white_check_mark:<br>15.7 s | :white_check_mark:<br>23.1 s | :white_check_mark:<br>15.7 s | :white_check_mark:<br>17.7 s | :white_check_mark:<br>17.2 s |
| [maven-javadoc-plugin](https://github.com/apache/maven-javadoc-plugin/tree/master)<br>3.12.1-SNAPSHOT | :x:<br>03:46 m | :x:<br>04:03 m | :x:<br>04:23 m | :x:<br>04:31 m | :x:<br>04:36 m |
| [maven-jdeps-plugin](https://github.com/apache/maven-jdeps-plugin/tree/master)<br>3.2.1-SNAPSHOT | :x:<br>13.6 s | :white_check_mark:<br>15.9 s | :x:<br>18.2 s | :x:<br>20.4 s | :x:<br>21.2 s |
| [maven-pmd-plugin](https://github.com/apache/maven-pmd-plugin/tree/master)<br>3.28.1-SNAPSHOT | :white_check_mark:<br>02:22 m | :white_check_mark:<br>02:29 m | :white_check_mark:<br>02:42 m | :white_check_mark:<br>02:52 m | :white_check_mark:<br>02:53 m |
| [maven-project-info-reports-plugin](https://github.com/apache/maven-project-info-reports-plugin/tree/master)<br>3.9.1-SNAPSHOT | :white_check_mark:<br>01:28 m | :white_check_mark:<br>01:36 m | :white_check_mark:<br>01:38 m | :white_check_mark:<br>01:42 m | :white_check_mark:<br>01:44 m |

## tools

|                  | 3.9.15 | 3.10.0-SNAPSHOT | 4.0.0-rc-4 | 4.0.0-rc-5 | 4.0.0-SNAPSHOT |
| ---------------- | -------- | -------- | -------- | -------- | -------- |
| [archetype](https://github.com/apache/maven-archetype/tree/master)<br>3.4.2-SNAPSHOT | :white_check_mark:<br>01:31 m | :x:<br>01:37 m | :white_check_mark:<br>02:05 m | :white_check_mark:<br>02:08 m | :x:<br>02:09 m |
| [enforcer](https://github.com/apache/maven-enforcer/tree/master)<br>3.6.4-SNAPSHOT | :white_check_mark:<br>02:18 m | :white_check_mark:<br>02:52 m | :white_check_mark:<br>03:32 m | :white_check_mark:<br>03:33 m | :white_check_mark:<br>03:43 m |
| [maven-antrun-plugin](https://github.com/apache/maven-antrun-plugin/tree/master)<br>3.2.1-SNAPSHOT | :white_check_mark:<br>32.5 s | :white_check_mark:<br>41.8 s | :white_check_mark:<br>49.2 s | :white_check_mark:<br>52.9 s | :white_check_mark:<br>54.9 s |
| [maven-artifact-plugin](https://github.com/apache/maven-artifact-plugin/tree/master)<br>3.6.2-SNAPSHOT | :white_check_mark:<br>40.3 s | :white_check_mark:<br>48.7 s | :x:<br>1.5 s | :x:<br>45.2 s | :x:<br>45.1 s |
| [maven-assembly-plugin](https://github.com/apache/maven-assembly-plugin/tree/master)<br>3.8.1-SNAPSHOT | :white_check_mark:<br>03:44 m | :x:<br>04:34 m | :x:<br>04:55 m | :x:<br>05:12 m | :x:<br>05:27 m |
| [maven-dependency-plugin](https://github.com/apache/maven-dependency-plugin/tree/master)<br>3.11.1-SNAPSHOT | :white_check_mark:<br>02:45 m | :white_check_mark:<br>03:00 m | :white_check_mark:<br>03:16 m | :white_check_mark:<br>03:24 m | :white_check_mark:<br>03:57 m |
| [maven-gpg-plugin](https://github.com/apache/maven-gpg-plugin/tree/master)<br>3.2.9-SNAPSHOT | :white_check_mark:<br>01:23 m | :white_check_mark:<br>01:30 m | :x:<br>1.2 s | :white_check_mark:<br>01:51 m | :white_check_mark:<br>01:53 m |
| [maven-help-plugin](https://github.com/apache/maven-help-plugin/tree/master)<br>3.5.2-SNAPSHOT | :white_check_mark:<br>39.3 s | :white_check_mark:<br>49.0 s | :x:<br>56.3 s | :x:<br>59.1 s | :x:<br>01:04 m |
| [maven-invoker-plugin](https://github.com/apache/maven-invoker-plugin/tree/master)<br>3.10.2-SNAPSHOT | :white_check_mark:<br>03:00 m | :white_check_mark:<br>03:57 m | :white_check_mark:<br>04:44 m | :white_check_mark:<br>04:58 m | :white_check_mark:<br>05:07 m |
| [maven-jarsigner-plugin](https://github.com/apache/maven-jarsigner-plugin/tree/master)<br>3.1.1-SNAPSHOT | :x:<br>4.5 s | :x:<br>4.7 s | :x:<br>4.3 s | :x:<br>4.8 s | :x:<br>4.7 s |
| [maven-jdeprscan-plugin](https://github.com/apache/maven-jdeprscan-plugin/tree/master)<br>3.0.1-SNAPSHOT | :white_check_mark:<br>13.8 s | :white_check_mark:<br>16.3 s | :x:<br>16.2 s | :x:<br>17.4 s | :x:<br>18.0 s |
| [maven-remote-resources-plugin](https://github.com/apache/maven-remote-resources-plugin/tree/master)<br>3.3.1-SNAPSHOT | :white_check_mark:<br>32.2 s | :white_check_mark:<br>34.2 s | :white_check_mark:<br>43.3 s | :white_check_mark:<br>46.2 s | :white_check_mark:<br>47.5 s |
| [maven-scm-publish-plugin](https://github.com/apache/maven-scm-publish-plugin/tree/master)<br>3.3.1-SNAPSHOT | :white_check_mark:<br>19.0 s | :white_check_mark:<br>22.4 s | :x:<br>16.7 s | :x:<br>18.1 s | :x:<br>19.9 s |
| [maven-scripting-plugin](https://github.com/apache/maven-scripting-plugin/tree/master)<br>3.1.1-SNAPSHOT | :white_check_mark:<br>8.5 s | :white_check_mark:<br>10.0 s | :white_check_mark:<br>11.2 s | :white_check_mark:<br>12.1 s | :white_check_mark:<br>12.1 s |
| [maven-stage-plugin](https://github.com/apache/maven-stage-plugin/tree/master)<br>1.1-SNAPSHOT | :white_check_mark:<br>3.3 s | :white_check_mark:<br>3.5 s | :white_check_mark:<br>3.2 s | :white_check_mark:<br>3.6 s | :white_check_mark:<br>3.5 s |
| [maven-toolchains-plugin](https://github.com/apache/maven-toolchains-plugin/tree/master)<br>3.2.1-SNAPSHOT | :white_check_mark:<br>10.8 s | :white_check_mark:<br>14.0 s | :x:<br>15.0 s | :x:<br>17.5 s | :x:<br>18.2 s |
| [plugin-tools](https://github.com/apache/maven-plugin-tools/tree/master)<br>4.0.0-beta-3-SNAPSHOT | :white_check_mark:<br>01:51 m | :white_check_mark:<br>02:08 m | :white_check_mark:<br>02:19 m | :white_check_mark:<br>02:29 m | :white_check_mark:<br>02:31 m |
| [release](https://github.com/apache/maven-release/tree/master)<br>3.3.2-SNAPSHOT | :white_check_mark:<br>02:03 m | :white_check_mark:<br>02:18 m | :white_check_mark:<br>02:37 m | :white_check_mark:<br>02:43 m | :white_check_mark:<br>02:59 m |
| [scm](https://github.com/apache/maven-scm/tree/master)<br>2.2.2-SNAPSHOT | :white_check_mark:<br>02:22 m | :white_check_mark:<br>02:30 m | :white_check_mark:<br>02:25 m | :white_check_mark:<br>02:24 m | :white_check_mark:<br>02:26 m |

