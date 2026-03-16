<!---
 Licensed to the Apache Software Foundation (ASF) under one or more
 contributor license agreements.  See the NOTICE file distributed with
 this work for additional information regarding copyright ownership.
 The ASF licenses this file to You under the Apache License, Version 2.0
 (the "License"); you may not use this file except in compliance with
 the License.  You may obtain a copy of the License at

      http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
-->

Apache [Maven Distribution Tool][report]
============

[![Apache License, Version 2.0, January 2004](https://img.shields.io/github/license/apache/maven.svg?label=License)][license]
[![Jenkins Status](https://img.shields.io/jenkins/s/https/ci-maven.apache.org/job/Maven/job/maven-box/job/maven-dist-tool/job/master.svg)][build]


Maven Distribution Tool is a tool executed daily on Maven CI server to produce [a report][report]
of the different checks done on our releases.

Quick Build
-------
```
mvn verify site
```

Configuration
-------

### API_TOKEN

The `API_TOKEN` environment variable is required to authenticate HTTP requests against the
[Apache Jenkins instance](https://ci-maven.apache.org/) and other Apache infrastructure.
It is a Base64-encoded `username:apitoken` string used for HTTP Basic Authentication.

#### Generating the Token

1. Log in to the [Apache Jenkins instance](https://ci-maven.apache.org/).
2. Click your username in the top-right corner to open your user profile.
3. Navigate to **Security** (or go directly to `https://ci-maven.apache.org/user/<your-username>/security/`).
4. Under **API Token**, click **Add new Token**, give it a name, and click **Generate**.
5. Copy the generated token — it will not be shown again.

#### Setting the Environment Variable

Base64-encode your Jenkins username and API token, then export it:

```bash
export API_TOKEN=$(echo -n 'your-username:your-api-token' | base64)
```

You can add this to your shell profile (`~/.bashrc`, `~/.zshrc`, etc.) for persistence.

#### Usage

- **Local build with site reports:** `mvn verify site` — the reports use `API_TOKEN` to fetch data from Jenkins.
- **Integration tests:** `mvn failsafe:integration-test` — the token is passed to the failsafe plugin via the `API_TOKEN` environment variable.
- **CI (Jenkins):** The token is automatically injected via the `withCredentials` binding in the `Jenkinsfile` using the `API_TOKEN` credential ID.

[report]: https://ci-maven.apache.org/job/Maven/job/maven-box/job/maven-dist-tool/job/master/site/
[license]: https://www.apache.org/licenses/LICENSE-2.0
[build]: https://ci-maven.apache.org/job/Maven/job/maven-box/job/maven-dist-tool/job/master

