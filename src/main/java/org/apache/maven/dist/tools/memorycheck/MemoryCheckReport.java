package org.apache.maven.dist.tools.memorycheck;

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

import org.apache.maven.dist.tools.AbstractDistCheckMojo;
import org.apache.maven.dist.tools.ConfigurationLineInfo;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributes;
import org.apache.maven.doxia.sink.impl.SinkEventAttributeSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GHWorkflowJob;
import org.kohsuke.github.GHWorkflowRun;
import org.kohsuke.github.GitHub;

import java.io.IOException;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Generate from now a single page with a badge from
 * <a href="https://github.com/quick-perf/maven-test-bench">Maven Test Bench</a> project, which runs a daily
 * memory check based on running `mvn validate` on a third party project.
 *
 * @author Patrice Cavezzan
 */

@Mojo( name = "memory-check", requiresProject = false )
public class MemoryCheckReport extends AbstractDistCheckMojo
{

    private static final String GITHUB_REPOSITORY = "quick-perf/maven-test-bench";
    public static final String MEMORY_CHECK_GITHUB_ACTION_WORKFLOW_NAME = "Daily Memory Check";
    private static final int BUILD_HISTORY_SIZE = 10;
    private static final int GITHUB_ACTION_JOB_PAGE_SIZE = 10;

    @Override
    protected void executeReport( Locale locale ) throws MavenReportException
    {
        final Sink sink = getSink();

        sink.head();
        generateTitle( sink );
        sink.head_();

        sink.body();
        generateMavenTestBenchIntroduction( sink );
        sink.paragraph();
        generateMavenTestBenchBuildStatusResult( sink );
        sink.paragraph_();
        sink.body_();
    }

    private void generateTitle( Sink sink )
    {
        sink.title();
        sink.text( "Memory Check" );
        sink.title_();
    }

    private void generateMavenTestBenchIntroduction( Sink sink )
    {
        final SinkEventAttributes mavenQuickPerfLinkAttributes = new SinkEventAttributeSet();
        mavenQuickPerfLinkAttributes.addAttribute( SinkEventAttributes.TITLE, "Memory Check" );
        // open in new tab to avoid being blocked by iframe
        mavenQuickPerfLinkAttributes.addAttribute( SinkEventAttributes.TARGET , "_blank" );
        sink.link( "https://github.com/quick-perf/maven-test-bench/", mavenQuickPerfLinkAttributes );
        sink.text( "Memory Check" );
        sink.link_( );
        sink.text( "is running " );
        sink.rawText( "mvn validate" );
        sink.text( " on a massive multi module project, and we make sure that memory allocation stay " );
        final SinkEventAttributes sampleLinkAttributes = new SinkEventAttributeSet();
        sampleLinkAttributes.addAttribute( SinkEventAttributes.TITLE, "Threashold set" );
        // open in new tab to avoid being blocked by iframe
        sampleLinkAttributes.addAttribute( SinkEventAttributes.TARGET , "_blank" );
        sink.link(
                "https://github.com/quick-perf/maven-test-bench/blob/master/maven-perf/src/test/java/org/quickperf/"
                        + "maven/bench/head/MvnValidateMaxAllocation.java#L52",
                sampleLinkAttributes
        );
        sink.text( "under a certain threshold." );
        sink.link_();
    }

    private void generateMavenTestBenchBuildStatusResult( Sink sink )
    {
        sink.lineBreak();
        sink.text( "Current build status: " );
        List<GHWorkflowJob> status = getLatestBuildStatus();
        sink.list();
        status.forEach( s ->
        {
            sink.listItem();
            sink.link( s.getHtmlUrl().toString() );
            sink.text( DateTimeFormatter.ISO_LOCAL_DATE
                    .withZone( ZoneId.of( "UTC" ) )
                    .format( s.getStartedAt().toInstant() ) );
            if ( s.getConclusion() == GHWorkflowRun.Conclusion.SUCCESS )
            {
                iconSuccess( sink );
            }
            else
            {
                iconError( sink );
            }
            sink.link_();
            sink.listItem_();
        } );
        sink.list_();
    }

    @Override
    protected boolean isIndexPageCheck()
    {
        return false;
    }

    @Override
    protected void checkArtifact( ConfigurationLineInfo request, String repoBase ) throws MojoExecutionException
    {
    }

    @Override
    protected String getFailuresFilename()
    {
        return null;
    }

    @Override
    public String getOutputName()
    {
        return "dist-tool-memory-check";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Dist Tool> Memory Check";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Display daily memory result from a QuickPerf sub project on github";
    }

    private List<GHWorkflowJob> getLatestBuildStatus()
    {
        final List<GHWorkflowJob> result = new ArrayList<>();
        try
        {
            GitHub github = GitHub.connect();
            GHRepository repo = github.getRepository( GITHUB_REPOSITORY );
            int index = 0;
            for ( GHWorkflowRun workflowRun : repo.queryWorkflowRuns().list()
                    .withPageSize( GITHUB_ACTION_JOB_PAGE_SIZE ) )
            {
                if ( index > BUILD_HISTORY_SIZE )
                {
                    break;
                }

                if ( workflowRun.getName().equals( MEMORY_CHECK_GITHUB_ACTION_WORKFLOW_NAME ) )
                {
                    result.addAll( workflowRun.listJobs().toList() );
                }
                index++;
            }
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }

        return result;
    }
}
