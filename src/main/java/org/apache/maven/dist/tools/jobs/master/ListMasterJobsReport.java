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

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.dist.tools.JsoupRetry;
import org.apache.maven.dist.tools.jobs.AbstractJobsReport;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Generate report with build status of the Jenkins job for the master branch of every Git repository in
 * <a href="https://ci-maven.apache.org/job/Maven/job/maven-box/">{@code maven-box} Apache Hosted Git Folder job</a>.
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

        List<Result> repoStatus = new ArrayList<>(repositoryNames.size());

        for (String repository : repositoryNames) {
            final String repositoryJobUrl = MAVENBOX_JOBS_BASE_URL + "job/" + repository;

            try {
                Document doc = JsoupRetry.get(repositoryJobUrl);

                Result result = new Result(repository, repositoryJobUrl);

                Element masterRow = doc.getElementById("job_master");
                if (masterRow == null) {
                    getLog().warn(MAVENBOX_JOBS_BASE_URL + repository + " is missing id job_master");
                    continue;
                } else if (masterRow.hasClass("job-status-red") || masterRow.hasClass("job-status-red-anime")) {
                    result.setStatus("FAILURE");
                } else if (masterRow.hasClass("job-status-yellow") || masterRow.hasClass("job-status-yellow-anime")) {
                    result.setStatus("UNSTABLE");
                } else if (masterRow.hasClass("job-status-blue") || masterRow.hasClass("job-status-blue-anime")) {
                    result.setStatus("SUCCESS");
                } else {
                    result.setStatus("UNKNOWN");
                }
                result.setIcon(masterRow
                        .select("span.build-status-icon__wrapper")
                        .first()
                        .outerHtml());

                result.setLastBuild(getLastBuild(
                        masterRow.child(3).attr("data"), masterRow.child(4).attr("data")));

                repoStatus.add(result);
            } catch (IOException e) {
                getLog().warn("Failed to read status for " + repository + " Jenkins job " + repositoryJobUrl);
            }
        }

        generateReport(repoStatus);
    }

    private void generateReport(List<Result> repoStatus) {
        Sink sink = getSink();

        sink.head();
        sink.title();
        sink.text("List Master Jobs");
        sink.title_();
        sink.head_();

        sink.body();
        sink.text("Jenkins jobs for master branch sorted by status of last build:");
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
                    e.getValue().forEach(r -> {
                        sink.listItem();
                        sink.rawText(r.getIcon());

                        sink.rawText("<span");
                        if ((r.getLastBuild() == null)
                                || r.getLastBuild().isBefore(ZonedDateTime.now().minusMonths(1))) {
                            sink.rawText(" style=\"color:red\"");
                        }
                        sink.rawText(">("
                                + ((r.getLastBuild() == null)
                                        ? "-"
                                        : r.getLastBuild().format(DateTimeFormatter.ISO_LOCAL_DATE))
                                + ")</span> ");

                        sink.link(r.getBuildUrl());
                        sink.rawText(r.getRepositoryName());
                        sink.link_();
                        sink.listItem_();
                    });
                    sink.list_();

                    sink.listItem_();
                });

        sink.list_();
        sink.body_();
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
}
