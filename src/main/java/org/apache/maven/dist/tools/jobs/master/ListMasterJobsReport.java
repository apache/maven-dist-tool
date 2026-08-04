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
package org.apache.maven.dist.tools.jobs.master;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.maven.dist.tools.JsonRetry;
import org.apache.maven.dist.tools.jobs.AbstractJobsReport;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Generate report with build status of the Jenkins job for the master branch of every Git repository in
 * <a href="https://ci-maven.apache.org/job/Maven/job/maven-box/">{@code maven-box} Apache Hosted Git Folder job</a>.
 * TODO also add maintenance branches (4 vs 3)
 *
 * @author Robert Scholte
 */
@Mojo(name = "list-master-jobs", requiresProject = false)
public class ListMasterJobsReport extends AbstractJobsReport {

    /**
     * <p>Constructor for DistCheckSiteReport.</p>
     */
    public ListMasterJobsReport() {}

    /** {@inheritDoc} */
    @Override
    public String getOutputName() {
        return "dist-tool-master-jobs";
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> List Master Jobs";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Shows the status of Jenkins job for the master branch of every Git repository on one page";
    }

    /** {@inheritDoc} */
    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        Collection<String> repositoryNames = repositoryNames();

        List<Result> repoStatus = Flux.fromIterable(repositoryNames)
                .flatMap(
                        repo -> JsonRetry.getAsync(MAVENBOX_JOBS_BASE_URL + repo
                                        + "/api/json?tree=jobs[name,url,color,lastBuild[result,number,timestamp]]")
                                .flatMap(jsonNode -> buildResult(repo, jsonNode))
                                .onErrorResume(e -> {
                                    getLog().warn("Failed to read status for " + repo + " Jenkins job "
                                            + MAVENBOX_JOBS_BASE_URL + repo);
                                    return Mono.empty();
                                }),
                        concurrency)
                .collectList()
                .block();

        generateReport(repoStatus);
    }

    private Mono<Result> buildResult(String repository, JsonNode jsonNode) {
        if (!(jsonNode instanceof ObjectNode objectNode)) {
            getLog().warn("Failed to read JSON for " + repository + " Jenkins job " + MAVENBOX_JOBS_BASE_URL
                    + repository);
            return Mono.empty();
        }
        // find the master node
        return Mono.justOrEmpty(objectNode
                .get("jobs")
                .valueStream()
                .filter(n -> n.get("name").asText().equals("master"))
                .findFirst()
                .map(n -> {
                    JsonNode lastBuild = n.get("lastBuild");
                    // "result" is null while a build is still running, and lastBuild
                    // itself is absent for a job that has never run. Dereferencing
                    // either unconditionally dropped the repository from the report.
                    JsonNode resultNode = lastBuild != null ? lastBuild.get("result") : null;
                    String status = (resultNode == null || resultNode.isNull()) ? "UNKNOWN" : resultNode.asText();
                    String buildUrl = lastBuild != null
                            ? n.get("url").asText() + lastBuild.get("number").asText()
                            : n.get("url").asText();
                    Result result = new Result(repository, buildUrl);
                    result.setStatus(status);
                    result.setIcon(retrieveIcon(status));

                    long timestamp =
                            lastBuild != null ? lastBuild.get("timestamp").asLong() : 0L;
                    if (timestamp != 0L) {
                        result.setLastBuild(
                                ZonedDateTime.ofInstant(Instant.ofEpochMilli(timestamp), ZoneId.systemDefault()));
                    }

                    return result;
                }));
    }

    private String retrieveIcon(String status) {
        return switch (status) {
            case "FAILURE" -> "&#10060;"; // (red) CROSS MARK
            case "SUCCESS" -> "&#9989;"; // (green) WHITE HEAVY CHECK MARK
            case "UNKNOWN" -> "&#10068;"; // White Question Mark Ornament
            case "UNSTABLE" -> "&#9888;&#65039;"; // WARNING SIGN rendered as yellow
            default -> "&#10068;"; // White Question Mark Ornament (same as Unknown)
        };
    }

    private void generateReport(List<Result> repoStatus) {
        Sink sink = getSink();

        sink.head();
        sink.title();
        sink.text("List Master Jobs");
        sink.title_();
        sink.head_();

        sink.body();

        Map<String, List<Result>> groupedResults =
                repoStatus.stream().collect(Collectors.groupingBy(Result::getStatus));

        sink.paragraph();
        sink.link(MAVENBOX_JOBS_BASE_URL + "..");
        sink.text("Jenkins jobs");
        sink.link_();
        sink.text(" for the master branch of " + repoStatus.size() + " repositories, worst status first: ");
        sink.text(groupedResults.entrySet().stream()
                        .sorted(Map.Entry.comparingByKey(resultComparator()))
                        .map(e -> e.getValue().size() + " " + e.getKey().toLowerCase(Locale.ROOT))
                        .collect(Collectors.joining(", "))
                + ".");
        sink.paragraph_();

        sink.table();
        sink.tableRows(new int[] {Sink.JUSTIFY_CENTER, Sink.JUSTIFY_LEFT, Sink.JUSTIFY_LEFT, Sink.JUSTIFY_LEFT}, true);

        sink.tableRow();
        for (String header : new String[] {"Status", "Repository", "Last build", "Sources"}) {
            sink.tableHeaderCell();
            sink.text(header);
            sink.tableHeaderCell_();
        }
        sink.tableRow_();

        groupedResults.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(resultComparator()))
                .forEach(e -> e.getValue().stream()
                        // Results arrive in completion order, which differs on every
                        // run; sort so the page is stable and diffable.
                        .sorted(Comparator.comparing(Result::getRepositoryName))
                        .forEach(r -> renderJobResult(sink, r)));

        sink.tableRows_();
        sink.table_();
        sink.body_();
    }

    private void renderJobResult(Sink sink, Result r) {
        sink.tableRow();

        sink.tableCell();
        sink.rawText(r.getIcon() + " " + r.getStatus());
        sink.tableCell_();

        sink.tableCell();
        sink.link(r.getBuildUrl());
        sink.rawText(r.getRepositoryName());
        sink.link_();
        sink.tableCell_();

        sink.tableCell();
        boolean stale = (r.getLastBuild() == null)
                || r.getLastBuild().isBefore(ZonedDateTime.now().minusMonths(1));
        sink.rawText("<span"
                + (stale ? " class=\"text-red\" title=\"no build in the last month\"" : "")
                + ">"
                + ((r.getLastBuild() == null) ? "-" : r.getLastBuild().format(DateTimeFormatter.ISO_LOCAL_DATE))
                + "</span>");
        sink.tableCell_();

        sink.tableCell();
        sink.link("https://github.com/apache/" + r.getRepositoryName());
        sink.text("GitHub");
        sink.link_();
        sink.tableCell_();

        sink.tableRow_();
    }

    private Comparator<String> resultComparator() {
        final List<String> orderedStatus = Arrays.asList("FAILURE", "UNSTABLE", "UNKNOWN", "SUCCESS");
        return (l, r) -> {
            return Integer.compare(orderedStatus.indexOf(l), orderedStatus.indexOf(r));
        };
    }
}
