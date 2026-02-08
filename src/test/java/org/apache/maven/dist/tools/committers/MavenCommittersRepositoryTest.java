/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.maven.dist.tools.committers;

import java.util.List;

import com.github.tomakehurst.wiremock.junit5.WireMockRuntimeInfo;
import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import org.apache.maven.dist.tools.committers.MavenCommittersRepository.Committer;
import org.junit.jupiter.api.Test;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static org.assertj.core.api.Assertions.assertThat;

@WireMockTest
class MavenCommittersRepositoryTest {

    private static final String GROUP = """
            {
              "test1": [
                "test1",
                "test2"
              ],
              "maven": [
                "m1",
                "m2",
                "cstamas"
              ],
              "maven-pmc": [
                "cstamas"
              ]
            }
            """;

    private static final String NAMES = """
            {
              "test1": "test 1 name",
              "m1": "M1 name",
              "test2": "test 2 name",
              "m2": "M2 name",
              "test3": "test 3 name",
              "cstamas": "Tamas Cservenak",
              "test4": "test 4 name",
              "test5": "test 5 name",
            }
            """;

    @Test
    void dataLoad(WireMockRuntimeInfo wireMockRuntimeInfo) {

        stubFor(get("/json/foundation/groups.json")
                .willReturn(aResponse().withStatus(200).withBody(GROUP)));
        stubFor(get("/json/foundation/people_name.json")
                .willReturn(aResponse().withStatus(200).withBody(NAMES)));

        MavenCommittersRepository mavenCommittersRepository =
                new MavenCommittersRepository(wireMockRuntimeInfo.getHttpBaseUrl());
        assertThat(mavenCommittersRepository.getCommitters())
                .containsExactly(
                        new Committer("cstamas", List.of("Tamas Cservenak", "Tamás Cservenák"), true),
                        new Committer("m1", List.of("M1 name"), false),
                        new Committer("m2", List.of("M2 name"), false));
    }
}
