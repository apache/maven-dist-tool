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

import javax.inject.Named;
import javax.inject.Singleton;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Fetch a Maven committers
 */
@Named
@Singleton
public class MavenCommittersRepository {

    private static final Logger LOG = LoggerFactory.getLogger(MavenCommittersRepository.class);

    public record Committer(String id, List<String> names, boolean pmc) {
        Committer(String id, boolean pmc) {
            this(id, new ArrayList<>(), pmc);
        }
    }

    private static final String ASF_PROJECT_URL = "https://projects.apache.org";

    private static final String ASF_GROUP_FILE = "/json/foundation/groups.json";

    private static final String ASF_PEOPLE_FILE = "/json/foundation/people_name.json";

    private final Map<String, Committer> committers = new TreeMap<>();

    private final String asfProjectUrl;

    MavenCommittersRepository() {
        this(ASF_PROJECT_URL);
    }

    MavenCommittersRepository(String asfProjectUrl) {
        this.asfProjectUrl = asfProjectUrl;
        try {
            loadData();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    public Collection<Committer> getCommitters() {
        return committers.values();
    }

    private void loadData() throws IOException {

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .uri(URI.create(asfProjectUrl + ASF_GROUP_FILE))
                    .timeout(Duration.ofSeconds(60))
                    .build();

            LOG.info("Loading Maven groups");
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            loadMavenGroup(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        try (HttpClient client = HttpClient.newHttpClient()) {
            HttpRequest request = HttpRequest.newBuilder()
                    .GET()
                    .header("Accept", "application/json")
                    .uri(URI.create(asfProjectUrl + ASF_PEOPLE_FILE))
                    .timeout(Duration.ofSeconds(60))
                    .build();

            LOG.info("Loading Committers names");
            HttpResponse<InputStream> response = client.send(request, HttpResponse.BodyHandlers.ofInputStream());
            loadPeopleName(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(e);
        }

        loadPeopleNameSupplement();
    }

    private void loadPeopleNameSupplement() throws IOException {
        Properties props = new Properties();

        try (Reader in = new InputStreamReader(
                getClass().getResourceAsStream("/committers-names.properties"), StandardCharsets.UTF_8)) {
            props.load(in);
        }

        for (String key : props.stringPropertyNames()) {
            Committer committer = committers.get(key);
            if (committer != null) {
                Arrays.stream(props.getProperty(key).split(","))
                        .map(String::trim)
                        .forEach(name -> committer.names.add(name.trim()));
            }
        }
    }

    private void loadMavenGroup(InputStream input) throws IOException {

        List<String> ids = new ArrayList<>();
        List<String> pmcs = new ArrayList<>();

        try (JsonParser parser = new JsonFactory().createParser(input)) {
            while (parser.nextToken() != JsonToken.END_OBJECT && (ids.isEmpty() || pmcs.isEmpty())) {
                if ("maven".equals(parser.currentName()) && parser.currentToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        ids.add(parser.getText());
                    }
                }
                if ("maven-pmc".equals(parser.currentName()) && parser.currentToken() == JsonToken.START_ARRAY) {
                    while (parser.nextToken() != JsonToken.END_ARRAY) {
                        pmcs.add(parser.getText());
                    }
                }
            }
        }

        ids.stream()
                .map(id -> new Committer(id, pmcs.contains(id)))
                .forEach(committer -> committers.put(committer.id, committer));
    }

    private void loadPeopleName(InputStream input) throws IOException {
        int itemFounds = 0;
        try (JsonParser parser = new JsonFactory().createParser(input)) {
            while (parser.nextToken() != JsonToken.END_OBJECT && itemFounds < committers.size()) {
                if (parser.currentToken() == JsonToken.VALUE_STRING) {
                    String id = parser.currentName();
                    String name = parser.getText();
                    Committer committer = committers.get(id);
                    if (committer != null) {
                        committer.names.add(name);
                        itemFounds++;
                    }
                }
            }
        }
    }
}
