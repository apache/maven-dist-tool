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
                    String status = lastBuild != null ? lastBuild.get("result").asText() : "UNKNOWN";
                    String buildUrl = n.get("url").asText()
                            + n.get("lastBuild").get("number").asText();
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
            case "UNKNOWN" -> "&#2754;"; // White Question Mark Ornament (same as default)
            case "UNSTABLE" -> "&#9888;&#65039;"; // WARNING SIGN rendered as yellow
            default -> "&#2754;"; // White Question Mark Ornament (same as Unknown)
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
        sink.link(MAVENBOX_JOBS_BASE_URL + "..");
        sink.text("Jenkins jobs");
        sink.link_();
        sink.text(" for master branch sorted by status of last build:");
        sink.list();

        Map<String, List<Result>> groupedResults =
                repoStatus.stream().collect(Collectors.groupingBy(Result::getStatus));

        groupedResults.entrySet().stream()
                .sorted(Map.Entry.comparingByKey(resultComparator()))
                .forEach(e -> {
                    sink.listItem();
                    int size = e.getValue().size();
                    sink.text(size + " job" + (size > 1 ? "s" : "") + " with status " + e.getKey() + ":");
                    sink.list();
                    e.getValue().forEach(r -> renderJobResult(sink, r));
                    sink.list_();

                    sink.listItem_();
                });

        sink.list_();
        sink.body_();
    }

    private void renderJobResult(Sink sink, Result r) {
        sink.listItem();
        sink.rawText(r.getIcon());

        sink.rawText("<span");
        if ((r.getLastBuild() == null)
                || r.getLastBuild().isBefore(ZonedDateTime.now().minusMonths(1))) {
            sink.rawText(" class=\"text-red\"");
        }
        sink.rawText(">("
                + ((r.getLastBuild() == null) ? "-" : r.getLastBuild().format(DateTimeFormatter.ISO_LOCAL_DATE))
                + ")</span> ");

        sink.link(r.getBuildUrl());
        sink.rawText(r.getRepositoryName());
        sink.link_();
        sink.text(" (see also GH ");
        sink.link("https://github.com/apache/" + r.getRepositoryName());
        sink.rawText(getLocalBadge(r.getStatus()));
        sink.link_();
        sink.text(")");
        sink.listItem_();
    }

    private Comparator<String> resultComparator() {
        final List<String> orderedStatus = Arrays.asList("FAILURE", "UNSTABLE", "UNKNOWN", "SUCCESS");
        return (l, r) -> {
            return Integer.compare(orderedStatus.indexOf(l), orderedStatus.indexOf(r));
        };
    }

    private ZonedDateTime getLastBuild(String lastSuccess, String lastFailure) {
        ZonedDateTime success = null;
        if (!"-".equals(lastSuccess)) {
            success = ZonedDateTime.parse(lastSuccess);
        }
        ZonedDateTime failure = null;
        if (!"-".equals(lastFailure)) {
            failure = ZonedDateTime.parse(lastFailure);
        }

        if (success == null) {
            return failure;
        } else if (failure == null) {
            return success;
        } else if (success.compareTo(failure) >= 0) {
            return success;
        } else {
            return failure;
        }
    }

    private String getLocalBadge(String status) {
        return switch (status) {
            case "FAILURE" -> prepareBadge("failure", "#dd4343");
            case "SUCCESS" -> prepareBadge("passing", "#4b0");
            case "UNKNOWN" -> prepareBadge("unknown", "#57606a");
            case "UNSTABLE" -> prepareBadge("unstable", "#ffff00");
            default -> prepareBadge("unknown", "#57606a");
        };
    }

    private String prepareBadge(String buildStatus, String color) {
        // Replace with text-block, when maven-plugin-plugin supports needed Java version
        String badge =
                "<svg xmlns=\"http://www.w3.org/2000/svg\" width=\"98\" height=\"20\" role=\"img\" aria-label=\"checks: VAR_STATUS\">"
                        + "  <title>checks: VAR_STATUS</title>"
                        + "      <filter id=\"blur\">"
                        + "          <feGaussianBlur stdDeviation=\"16\"/>"
                        + "      </filter>"
                        + "      <linearGradient id=\"s\" x2=\"0\" y2=\"100%\">"
                        + "          <stop offset=\"0\" stop-color=\"#bbb\" stop-opacity=\".1\"/>"
                        + "          <stop offset=\"1\" stop-opacity=\".1\"/>"
                        + "      </linearGradient>"
                        + "      <clipPath id=\"r\">"
                        + "          <rect width=\"98\" height=\"20\" rx=\"3\"/>"
                        + "      </clipPath>"
                        + "      <g clip-path=\"url(#r)\">"
                        + "          <rect width=\"47\" height=\"20\" fill=\"#555\"/>"
                        + "          <rect x=\"47\" width=\"51\" height=\"20\" fill=\"VAR_COLOR\"/>"
                        + "          <rect width=\"98\" height=\"20\" fill=\"url(#s)\"/>"
                        + "      </g>"
                        + "      <g fill=\"#fff\" text-anchor=\"middle\" font-family=\"Verdana,Geneva,DejaVu Sans,sans-serif\" text-rendering=\"geometricPrecision\" font-size=\"100\">"
                        + "          <g transform=\"scale(.1)\">"
                        + "               <g aria-hidden=\"true\" fill=\"#010101\">"
                        + "               <text x=\"245\" y=\"150\" fill-opacity=\".8\" filter=\"url(#blur)\" textLength=\"370\">checks</text>"
                        + "               <text x=\"245\" y=\"150\" fill-opacity=\".3\" textLength=\"370\">checks</text>"
                        + "               </g>"
                        + "              <text x=\"245\" y=\"140\" textLength=\"370\">checks</text>"
                        + "          </g>"
                        + "          <g transform=\"scale(.1)\">"
                        + "              <g aria-hidden=\"true\" fill=\"#010101\">"
                        + "              <text x=\"715\" y=\"150\" fill-opacity=\".8\" filter=\"url(#blur)\" textLength=\"410\">VAR_STATUS</text>"
                        + "              <text x=\"715\" y=\"150\" fill-opacity=\".3\" textLength=\"410\">VAR_STATUS</text>"
                        + "              </g>"
                        + "              <text x=\"715\" y=\"140\" textLength=\"410\">VAR_STATUS</text>"
                        + "          </g>"
                        + "      </g>"
                        + "    </svg>";

        badge = badge.replaceAll("VAR_STATUS", buildStatus);
        badge = badge.replaceAll("VAR_COLOR", color);

        return badge;
    }
}
