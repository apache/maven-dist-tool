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

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.apache.maven.dist.tools.committers.MavenCommittersRepository.Committer;
import org.apache.maven.doxia.sink.Sink;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static java.util.Map.entry;

public abstract class MLStats {

    private final Logger log = LoggerFactory.getLogger(MLStats.class);

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

    public String getLast(Committer committer) {

        List<Map<String, String>> queryParamsList = getQueryParamsList(committer);
        return queryParamsList.stream()
                .map(this::prepareStatsURI)
                .map(this::getLastFromML)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .max(Comparator.naturalOrder())
                .orElse("-");
    }

    private Optional<String> getLastFromML(URI statsURI) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .uri(statsURI)
                    .timeout(Duration.ofSeconds(60))
                    .build();
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            Optional<String> last = parseLast(response.body());
            log.info("Query: {}, returns: {}", statsURI, last);
            return last;

        } catch (IOException e) {
            log.warn("Query: {}, error: {}", statsURI, e.getMessage());
            // try next one ...
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }
        return Optional.empty();
    }

    private Optional<String> parseLast(InputStream input) throws IOException {
        JsonFactory factory = new JsonFactory();
        Integer lastYear = null;
        Integer lastMonth = null;

        try (JsonParser parser = factory.createParser(input)) {
            while ((lastYear == null || lastMonth == null) && parser.nextToken() != JsonToken.END_OBJECT) {
                if (parser.currentToken() != JsonToken.VALUE_NUMBER_INT) {
                    continue;
                }
                String name = parser.currentName();
                switch (name) {
                    case "lastYear":
                        lastYear = parser.getValueAsInt();
                        break;
                    case "lastMonth":
                        lastMonth = parser.getValueAsInt();
                        break;
                    default:
                    // ignore
                }
            }
        }

        if (lastYear != null && lastMonth != null) {
            if (lastYear == 1970 && lastMonth == 1) {
                return Optional.empty();
            }
            return Optional.of(String.format("%04d-%02d", lastYear, lastMonth));
        }
        return Optional.empty();
    }

    private URI prepareStatsURI(Map<String, String> queryParams) {
        return URI.create(ML_STATS_ADDRES + "?"
                + Stream.concat(STANDARD_QUERY_PARAMS.entrySet().stream(), queryParams.entrySet().stream())
                        .map(entry ->
                                entry.getKey() + "=" + URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8))
                        .collect(Collectors.joining("&")));
    }
}
