package org.apache.maven.dist.tools.masterjobs;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.dist.tools.JsoupRetry;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Generate report with build status of the Jenkins job for the master branch of every Git repository in
 * <a href="https://builds.apache.org/job/maven-box/">{@code maven-box} Apache Hosted Git Folder job</a>.
 *
 * @author Robert Scholte
 */
@Mojo( name = "list-master-jobs", requiresProject = false )
public class ListMasterJobsMojo extends AbstractMavenReport
{
    private String gitboxUrl = "https://gitbox.apache.org/repos/asf";
    private String mavenboxJobsBaseUrl = "https://builds.apache.org/job/maven-box/job/";
    
    private Collection<String> excluded = Arrays.asList( "maven-integration-testing", // runs with maven
                                                         "maven-jenkins-env",
                                                         "maven-jenkins-lib",
                                                         "maven-sources",
                                                         "maven-studies" );

    @Override
    public String getOutputName()
    {
        return "dist-tool-master-jobs";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Dist Tool> List Master Jobs";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Shows the status of Jenkins job for the master branch of every Git repository on one page";
    }

    @Override
    protected void executeReport( Locale locale )
        throws MavenReportException
    {
        Collection<String> repositoryNames;
        try
        {
            repositoryNames = repositoryNames();
        }
        catch ( IOException e )
        {
            throw new MavenReportException( "Failed to extract repositorynames from Gitbox", e );
        }
        
        List<Result> repoStatus = new ArrayList<>( repositoryNames.size() );
        
        Collection<String> included = repositoryNames.stream()
                                                     .filter( s -> !excluded.contains( s ) )
                                                     .collect( Collectors.toList() );
        
        for ( String repository : included )
        {
            final String repositoryJobUrl = mavenboxJobsBaseUrl + repository;

            try
            {
                Document doc = JsoupRetry.get( repositoryJobUrl );
                
                Result result = new Result( repository, repositoryJobUrl );
                
                Element masterRow = doc.getElementById( "job_master" );
                if ( masterRow == null )
                {
                    getLog().warn( mavenboxJobsBaseUrl + repository + " is missing id job_master" );
                }
                else if ( masterRow.hasClass( "job-status-red" ) || masterRow.hasClass( "job-status-red-anime" ) )
                {
                    result.setStatus( "FAILURE" );
                }
                else if ( masterRow.hasClass( "job-status-yellow" ) || masterRow.hasClass( "job-status-yellow-anime" ) )
                {
                    result.setStatus( "UNSTABLE" );
                }
                else if ( masterRow.hasClass( "job-status-blue" ) || masterRow.hasClass( "job-status-blue-anime" ) )
                {
                    result.setStatus( "SUCCESS" );
                }
                else
                {
                    result.setStatus( "UNKNOWN" );
                }
                result.setIcon( masterRow.select( "img" ).first().outerHtml() );
                
                repoStatus.add( result );
            }
            catch ( IOException e )
            {
                getLog().warn( "Failed to read status for " + repository + " Jenkins job " + repositoryJobUrl  );
            }
        }
        
        generateReport( repoStatus );
    }
    
    private void generateReport( List<Result> repoStatus )
    {
        Sink sink = getSink();
        
        sink.head();
        sink.title();
        sink.text( "List Master Jobs" );
        sink.title_();
        sink.head_();
        
        sink.body();
        sink.text( "Jenkins jobs for master branch sorted by status of last build:" );
        sink.list();
        
        Map<String, List<Result>> groupedResults = repoStatus.stream()
                                                             .collect( Collectors.groupingBy( Result::getStatus ) );
        
        groupedResults.entrySet()
                      .stream()
                      .sorted( Map.Entry.comparingByKey( resultComparator() ) )
                      .forEach( e -> 
            {
                sink.listItem();
                int size = e.getValue().size();
                sink.text( size + " job" + ( size > 1 ? "s" : "" ) + " with status " + e.getKey() + ":" );
                sink.list();
                e.getValue().forEach( r -> 
                {
                    sink.listItem();
                    sink.rawText( r.getIcon() );
                    sink.link( r.getBuildUrl() );
                    sink.rawText( r.getRepositoryName() );
                    sink.link_();
                    sink.listItem_();
                } );
                sink.list_();
                
                sink.listItem_();
            } );
        
        sink.list_();
        sink.body_();
    }
    
    private Comparator<String> resultComparator()
    {
        final List<String> orderedStatus = Arrays.asList( "FAILURE", "UNSTABLE", "UNKNOWN", "SUCCESS" );
        return ( l, r ) -> 
            {
                return Integer.compare( orderedStatus.indexOf( l ), orderedStatus.indexOf( r ) );
            };
    }

    /**
     * Extract Git repository names for Apache Maven from
     * <a href="https://gitbox.apache.org/repos/asf">Gitbox main page</a>.
     *
     * @return the list of repository names (without ".git")
     * @throws IOException
     */
    protected Collection<String> repositoryNames()
        throws IOException
    {
        List<String> names = new ArrayList<>( 100 );
        Document doc = JsoupRetry.get( gitboxUrl );
        // find Apache Maven table
        Element apacheMavenTable = doc.getElementsMatchingText( "^Apache Maven$" ).parents().get( 0 );

        Elements gitRepo = apacheMavenTable.select( "tbody td:first-child a" );

        for ( Element element : gitRepo )
        {
            names.add( element.text().split( "\\.git" )[0] );
        }

        return names;
    }
}
