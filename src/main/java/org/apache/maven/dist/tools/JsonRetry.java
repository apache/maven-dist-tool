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
package org.apache.maven.dist.tools;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.Request;
import org.eclipse.jetty.reactive.client.ReactiveRequest;
import org.eclipse.jetty.reactive.client.ReactiveResponse;
import reactor.core.publisher.Mono;

public class JsonRetry {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private HttpClient httpClient = new HttpClient();

    private JsonRetry() {
        try {
            this.httpClient.start();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static class JsonRetryHolder {
        private static final JsonRetry INSTANCE = new JsonRetry();
    }

    public static JsonRetry getInstance() {
        return JsonRetryHolder.INSTANCE;
    }

    public static JsonNode get(String url) throws Exception {
        Request request = getInstance().httpClient.newRequest(url);
        String apiToken = System.getenv("API_TOKEN");
        if (StringUtils.isNotBlank(apiToken)) {
            request.headers(httpFields -> httpFields.add("Authorization", "Basic " + apiToken));
        }
        String json = request.send().getContentAsString();
        return json != null ? OBJECT_MAPPER.readTree(json) : null;
    }

    public static Mono<JsonNode> getAsync(String url) {
        Request request = getInstance().httpClient.newRequest(url);
        String apiToken = System.getenv("API_TOKEN");
        if (StringUtils.isNotBlank(apiToken)) {
            request.headers(httpFields -> httpFields.add("Authorization", "Basic " + apiToken));
        }
        ReactiveRequest reactiveRequest = ReactiveRequest.newBuilder(request).build();
        return Mono.from(reactiveRequest.response(ReactiveResponse.Content.asString()))
                .flatMap(json -> {
                    try {
                        return Mono.justOrEmpty(OBJECT_MAPPER.readTree(json));
                    } catch (Exception e) {
                        return Mono.error(e);
                    }
                });
    }
}
