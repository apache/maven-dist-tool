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

dist-tool Check Site Archives
=====

TODO: create a goal to automate detection of past minor releases to delete

idea: detect non-latest patch release site archive, to be deleted as we want to keep only 1 site archive per minor x.y

See [Maven website structure](https://maven.apache.org/developers/website/index.html) for details on site vs components...

## Maven website [components](https://svn.apache.org/repos/asf/maven/website/components/)

collections of Maven projects archives:
- [extensions-archives/](https://svn.apache.org/repos/asf/maven/website/components/extensions-archives/)
- [plugins-archives/](https://svn.apache.org/repos/asf/maven/website/components/plugins-archives/)
- [pom-archives/](https://svn.apache.org/repos/asf/maven/website/components/pom-archives/)
- [shared-archives/](https://svn.apache.org/repos/asf/maven/website/components/shared-archives/)
- [skins-archives/](https://svn.apache.org/repos/asf/maven/website/components/skins-archives/)
- [resolver-archives/](https://svn.apache.org/repos/asf/maven/website/components/resolver-archives/)

individual Maven project archives:
- [ant-tasks-archives/](https://svn.apache.org/repos/asf/maven/website/components/ant-tasks-archives/)
- [apache-resource-bundles-archives/](https://svn.apache.org/repos/asf/maven/website/components/apache-resource-bundles-archives/)
- [archetype-archives/](https://svn.apache.org/repos/asf/maven/website/components/archetype-archives/)
- [archetypes-archives/](https://svn.apache.org/repos/asf/maven/website/components/archetypes-archives/)
- [enforcer-archives/](https://svn.apache.org/repos/asf/maven/website/components/enforcer-archives/)
- [jxr-archives/](https://svn.apache.org/repos/asf/maven/website/components/jxr-archives/)
- [maven-indexer-archives/](https://svn.apache.org/repos/asf/maven/website/components/maven-indexer-archives/)
- [maven-release-archives/](https://svn.apache.org/repos/asf/maven/website/components/maven-release-archives/)
- [plugin-testing-archives/](https://svn.apache.org/repos/asf/maven/website/components/plugin-testing-archives/)
- [plugin-tools-archives/](https://svn.apache.org/repos/asf/maven/website/components/plugin-tools-archives/)
- [scm-archives/](https://svn.apache.org/repos/asf/maven/website/components/scm-archives/)
- [surefire-archives/](https://svn.apache.org/repos/asf/maven/website/components/surefire-archives/)
- [wagon-archives/](https://svn.apache.org/repos/asf/maven/website/components/wagon-archives/)
- [wrapper-archives/](https://svn.apache.org/repos/asf/maven/website/components/wrapper-archives/)

TODO: should we do the same for Maven core itself or not? = [ref/](https://svn.apache.org/repos/asf/maven/website/components/ref/)

## Doxia website [components](https://svn.apache.org/repos/asf/maven/doxia/website/components/)

Doxia projects archives:
- [doxia-archives/](https://svn.apache.org/repos/asf/maven/doxia/website/components/doxia-archives/)
- [doxia-sitetools-archives/](https://svn.apache.org/repos/asf/maven/doxia/website/components/doxia-sitetools-archives/)
- [doxia-tools-archives/](https://svn.apache.org/repos/asf/maven/doxia/website/components/doxia-tools-archives/)
