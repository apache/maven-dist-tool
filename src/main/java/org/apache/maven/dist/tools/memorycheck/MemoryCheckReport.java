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
package org.apache.maven.dist.tools.memorycheck;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.maven.dist.tools.AbstractDistCheckReport;
import org.apache.maven.dist.tools.ConfigurationLineInfo;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributes;
import org.apache.maven.doxia.sink.impl.SinkEventAttributeSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHub;

/**
 * Generate from now a single page with an history of build status from
 * <a href="https://github.com/quick-perf/maven-test-bench">Maven Test Bench</a> project, which runs a daily
 * memory check based on running `mvn validate` on a third party project using Github Action platform.
 *
 * @author Patrice Cavezzan
 */
@Mojo(name = "memory-check", requiresProject = false)
public class MemoryCheckReport extends AbstractDistCheckReport {

    private static final String GITHUB_REPOSITORY = "quick-perf/maven-test-bench";
    /** Constant <code>GITHUB_REPOSITORY_URL="<a href="https://github.com/">...</a> + GITHUB_REPOSITORY"</code> */
    public static final String GITHUB_REPOSITORY_URL = "https://github.com/" + GITHUB_REPOSITORY;
    /** Constant <code>MEMORY_CHECK_GITHUB_ACTION_WORKFLOW_NAME="Daily Memory Check"</code> */
    public static final String MEMORY_CHECK_GITHUB_ACTION_WORKFLOW_NAME = "Daily Memory Check";

    private static final int BUILD_HISTORY_SIZE = 10;
    private static final int GITHUB_ACTION_JOB_PAGE_SIZE = 10;

    /**
     * Memory Check Report constructor.
     */
    public MemoryCheckReport() {}

    /** {@inheritDoc} */
    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        final Sink sink = getSink();

        sink.head();
        generateTitle(sink);
        sink.head_();

        sink.body();
        generateMavenTestBenchIntroduction(sink);
        sink.paragraph();
        generateMavenTestBenchBuildStatusResult(sink);
        sink.paragraph_();
        sink.body_();
    }

    private void generateTitle(Sink sink) {
        sink.title();
        sink.text("Memory Check");
        sink.title_();
    }

    private void generateMavenTestBenchIntroduction(Sink sink) {
        final SinkEventAttributes mavenQuickPerfLinkAttributes = new SinkEventAttributeSet();
        mavenQuickPerfLinkAttributes.addAttribute(SinkEventAttributes.TITLE, "Memory Check");
        // open in new tab to avoid being blocked by iframe
        mavenQuickPerfLinkAttributes.addAttribute(SinkEventAttributes.TARGET, "_blank");
        sink.link(GITHUB_REPOSITORY_URL, mavenQuickPerfLinkAttributes);
        sink.text("Memory Check");
        sink.link_();
        sink.text("is running ");
        sink.rawText("mvn validate");
        sink.text(" on a massive multi module project, and we make sure that memory allocation stay ");
        final SinkEventAttributes sampleLinkAttributes = new SinkEventAttributeSet();
        sampleLinkAttributes.addAttribute(SinkEventAttributes.TITLE, "Threashold set");
        // open in new tab to avoid being blocked by iframe
        sampleLinkAttributes.addAttribute(SinkEventAttributes.TARGET, "_blank");
        sink.link(
                GITHUB_REPOSITORY_URL + "/blob/master/maven-perf/src/test/java/org/quickperf/"
                        + "maven/bench/head/MvnValidateMaxAllocation.java#L52",
                sampleLinkAttributes);
        sink.text("under a certain threshold.");
        sink.link_();
    }

    private void generateMavenTestBenchBuildStatusResult(Sink sink) {
        sink.lineBreak();
        sink.text("Current build status: ");
        List<GHWorkflowJob> status = getLatestBuildStatus();
        sink.list();
        status.forEach(s -> {
            sink.listItem();
            sink.link(s.getHtmlUrl().toString());
            sink.text(DateTimeFormatter.ISO_LOCAL_DATE
                    .withZone(ZoneId.of("UTC"))
                    .format(s.getStartedAt().toInstant()));
            if (s.getConclusion() == GHWorkflowRun.Conclusion.SUCCESS) {
                iconSuccess(sink);
            } else {
                iconError(sink);
            }
            sink.link_();
            sink.listItem_();
        });
        sink.list_();
    }

    /** {@inheritDoc} */
    @Override
    protected boolean isIndexPageCheck() {
        return false;
    }

    /** {@inheritDoc} */
    @Override
    protected void checkArtifact(ConfigurationLineInfo request, String repoBase) throws MojoExecutionException {}

    /** {@inheritDoc} */
    @Override
    protected String getFailuresFilename() {
        return null;
    }

    /** {@inheritDoc} */
    @Override
    public String getOutputName() {
        return "dist-tool-memory-check";
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> Memory Check";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Display daily memory result from a QuickPerf sub project on github";
    }

    private List<GHWorkflowJob> getLatestBuildStatus() {
        try {
            return StreamSupport.stream(
                            GitHub.connect()
                                    .getRepository(GITHUB_REPOSITORY)
                                    .queryWorkflowRuns()
                                    .list()
                                    .withPageSize(GITHUB_ACTION_JOB_PAGE_SIZE)
                                    .spliterator(),
                            false)
                    .limit(BUILD_HISTORY_SIZE)
                    .filter(ghWorkflowRun -> ghWorkflowRun.getName().equals(MEMORY_CHECK_GITHUB_ACTION_WORKFLOW_NAME))
                    .flatMap(ghWorkflowRun -> {
                        try {
                            return Arrays.stream(ghWorkflowRun.listJobs().toArray());
                        } catch (IOException e) {
                            getLog().warn(e);
                            return Stream.empty();
                        }
                    })
                    .collect(Collectors.toList());
        } catch (IOException e) {
            getLog().warn(e);
            return new ArrayList<>();
        }
    }
}
