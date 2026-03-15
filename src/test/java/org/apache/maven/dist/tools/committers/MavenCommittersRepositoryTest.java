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

import java.net.InetSocketAddress;
import java.util.List;

import org.apache.maven.dist.tools.committers.MavenCommittersRepository.Committer;
import org.eclipse.jetty.http.HttpHeader;
import org.eclipse.jetty.io.Content;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.util.Callback;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

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

    private Server server;
    private String baseUrl;

    @BeforeEach
    void startServer() throws Exception {
        server = new Server(new InetSocketAddress("localhost", 0));
        server.setHandler(new Handler.Abstract() {
            @Override
            public boolean handle(Request request, Response response, Callback callback) throws Exception {
                String path = request.getHttpURI().getPath();
                String body;
                if ("/json/foundation/groups.json".equals(path)) {
                    body = GROUP;
                } else if ("/json/foundation/people_name.json".equals(path)) {
                    body = NAMES;
                } else {
                    Response.writeError(request, response, callback, 404);
                    return true;
                }
                response.setStatus(200);
                response.getHeaders().put(HttpHeader.CONTENT_TYPE, "application/json");
                Content.Sink.write(response, true, body, callback);
                return true;
            }
        });
        server.start();
        int port = ((ServerConnector) server.getConnectors()[0]).getLocalPort();
        baseUrl = "http://localhost:" + port;
    }

    @AfterEach
    void stopServer() throws Exception {
        server.stop();
    }

    @Test
    void dataLoad() {
        MavenCommittersRepository repo = new MavenCommittersRepository(baseUrl);
        assertThat(repo.getCommitters())
                .containsExactly(
                        new Committer("cstamas", List.of("Tamas Cservenak", "Tamás Cservenák"), true),
                        new Committer("m1", List.of("M1 name"), false),
                        new Committer("m2", List.of("M2 name"), false));
    }
}
