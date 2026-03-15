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

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.maven.dist.tools.JsonRetry;
import org.apache.maven.dist.tools.committers.MavenCommittersRepository.Committer;
import org.apache.maven.doxia.sink.Sink;
import org.eclipse.jetty.client.Request;
import org.eclipse.jetty.reactive.client.ReactiveRequest;
import org.eclipse.jetty.reactive.client.ReactiveResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static java.util.Map.entry;

public abstract class MLStats {

    private final Logger log = LoggerFactory.getLogger(MLStats.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private static final String ML_STATS_ADDRES = "https://lists.apache.org/api/stats.lua";

    private static final Map<String, String> STANDARD_QUERY_PARAMS = Map.ofEntries(
            entry("d", "lte=1d"), // for stats 1 day is enough
            entry("domain", "maven.apache.org"));

    protected abstract boolean describeList(Sink sink);

    protected abstract List<Map<String, String>> getQueryParamsList(Committer committer);

    protected void linkList(Sink sink, String list) {
        sink.link("https://lists.apache.org/list.html?" + list + "@maven.apache.org");
        sink.text(list);
        sink.link_();
    }

    protected void describe(Sink sink) {
        sink.text("list ");
        boolean name = describeList(sink);
        sink.text(" and header_from " + (name ? "committer name" : "<committerId>@apache.org"));
    }

    public Mono<String> getLastAsync(Committer committer) {
        List<URI> uris = getQueryParamsList(committer).stream()
                .map(this::prepareStatsURI)
                .toList();

        return Flux.fromIterable(uris)
                .flatMapSequential(uri -> {
                    Request request = JsonRetry.getInstance()
                            .getHttpClient()
                            .newRequest(uri.toString())
                            .headers(f -> f.add("Accept", "application/json"))
                            .timeout(60, TimeUnit.SECONDS);
                    ReactiveRequest reactiveRequest =
                            ReactiveRequest.newBuilder(request).build();
                    return Mono.from(reactiveRequest.response(ReactiveResponse.Content.asString()))
                            .flatMap(json -> {
                                try {
                                    Optional<String> last = parseLastFromNode(OBJECT_MAPPER.readTree(json));
                                    log.info("Query: {}, returns: {}", uri, last);
                                    return Mono.justOrEmpty(last);
                                } catch (Exception e) {
                                    return Mono.error(e);
                                }
                            })
                            .onErrorResume(e -> {
                                log.warn("Query: {}, error: {}", uri, e.getMessage());
                                return Mono.empty();
                            });
                })
                .collect(Collectors.maxBy(Comparator.naturalOrder()))
                .map(opt -> opt.orElse("-"));
    }

    public String getLast(Committer committer) {
        return getLastAsync(committer).block();
    }

    private Optional<String> parseLastFromNode(JsonNode node) {
        JsonNode lastYearNode = node.get("lastYear");
        JsonNode lastMonthNode = node.get("lastMonth");
        if (lastYearNode == null || lastMonthNode == null) {
            return Optional.empty();
        }
        int lastYear = lastYearNode.asInt();
        int lastMonth = lastMonthNode.asInt();
        if (lastYear == 1970 && lastMonth == 1) {
            return Optional.empty();
        }
        return Optional.of(String.format("%04d-%02d", lastYear, lastMonth));
    }

    private URI prepareStatsURI(Map<String, String> queryParams) {
        return URI.create(ML_STATS_ADDRES + "?"
                + Stream.concat(STANDARD_QUERY_PARAMS.entrySet().stream(), queryParams.entrySet().stream())
                        .map(entry ->
                                entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&")));
    }
}
