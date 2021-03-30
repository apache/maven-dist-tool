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

About dist-tool-plugin
=====

The dist-tool-plugin checks that [Maven release process][6] has been fully applied across every artifact, as listed in [configuration file][4].

Results are displayed in 4 reports:

* [Check Source Release][2] report, for checks about artifacts [source release publication][5],

* [Check Sites][1] report, for checks about documentation sites associated to artifacts,

* [Check Index page][3] report, for checks about index pages,

* [Check Errors][8] report, to display errors found in previous checks.

In addition, dist-tool-plugin provides report for some interesting information about Maven artifacts:

* [List Plugins Prerequisites][7] report, displaying plugins' Maven and JDK version prerequisites,

or [MavenBox Jenkins Jobs](https://ci-builds.apache.org/job/Maven/job/maven-box/):

* [List Master Jobs][9] report, displaying the status of Jenkins jobs for the master branch of every Git repository on one page,

* [List Branches][10] report, displaying Jenkins branches vs Git branches by types of branches (master, Jira, dependabot, other).

Notice that this plugin is actually intended for Maven itself only: if interest is expressed to use it
in other context, it would require more configurations.

## Maven Core Performance Checks 

* [Memory Check](https://github.com/quick-perf/maven-test-bench/) [![Daily Memory Check](https://github.com/quick-perf/maven-test-bench/actions/workflows/memorycheck.yml/badge.svg)](https://github.com/quick-perf/maven-test-bench/actions/workflows/memorycheck.yml), 
running `mvn validate` on a massive multi module project, and we make sure that [memory allocation stay under a certain threshold](https://github.com/quick-perf/maven-test-bench/blob/master/maven-perf/src/test/java/org/quickperf/maven/bench/head/MvnValidateMaxAllocation.java#L52). 

_Powered by [QuickPerf](https://github.com/quick-perf/)_

[1]: ./dist-tool-check-site.html
[2]: ./dist-tool-check-source-release.html
[3]: ./dist-tool-check-index-page.html
[4]: ./dist-tool.conf.html
[5]: http://maven.apache.org/developers/release/maven-project-release-procedure.html#Copy_the_source_release_to_the_Apache_Distribution_Area
[6]: http://maven.apache.org/developers/release/releasing.html
[7]: ./dist-tool-prerequisites.html
[8]: ./dist-tool-check-errors.html
[9]: ./dist-tool-master-jobs.html
[10]: ./dist-tool-branches.html
