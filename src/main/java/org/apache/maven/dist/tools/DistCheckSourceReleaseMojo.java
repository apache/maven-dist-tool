package org.apache.maven.dist.tools;

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
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.maven.doxia.markup.HtmlMarkup;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributeSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * Check presence of source-release.zip in dist repo and central repo
 *
 * @author skygo
 */
@Mojo( name = "check-source-release", requiresProject = false )
public class DistCheckSourceReleaseMojo
        extends AbstractDistCheckMojo
{
//Artifact metadata retrieval done y hands.

    private static final String DIST_AREA = "http://www.apache.org/dist/maven";
    private static final String DIST_SVNPUBSUB = "https://dist.apache.org/repos/dist/release/maven";

    @Override
    public String getOutputName()
    {
        return "dist-tool-checksourcerelease";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Disttool> Source Release";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Verification of source release";
    }

    class DistCheckSourceRelease
            extends AbstractCheckResult
    {

        private List<String> central;
        private List<String> dist;
        private List<String> older;

        public DistCheckSourceRelease( ConfigurationLineInfo r, String version )
        {
            super( r, version );
        }

        private void setMissingDistSourceRelease( List<String> checkRepos )
        {
            dist = checkRepos;
        }

        private void setMissingCentralSourceRelease( List<String> checkRepos )
        {
            central = checkRepos;
        }

        private void setOlderSourceRelease( List<String> checkRepos )
        {
            older = checkRepos;
        }
    }
    private final List<DistCheckSourceRelease> results = new LinkedList<>();

    private void reportLine( Sink sink, DistCheckSourceRelease csr )
    {
        ConfigurationLineInfo cli = csr.getConfigurationLine();

        sink.tableRow();
        sink.tableCell();
        // shorten groupid
        sink.rawText( csr.getConfigurationLine().getGroupId().replaceAll( "org.apache.maven", "o.a.m" ) );
        sink.tableCell_();
        sink.tableCell();
        sink.rawText( csr.getConfigurationLine().getArtifactId() );
        sink.tableCell_();

        // LATEST column
        sink.tableCell();
        sink.link( cli.getMetadataFileURL( repoBaseUrl ) );
        sink.rawText( csr.getVersion() );
        sink.link_();
        sink.tableCell_();

        // DATE column
        sink.tableCell();
        sink.rawText( csr.getConfigurationLine().getReleaseFromMetadata() );
        sink.tableCell_();

        // central column
        sink.tableCell();
        sink.link( cli.getBaseURL( repoBaseUrl, "" ) );
        sink.text( "artifact" );
        sink.link_();
        sink.text( "/" );
        sink.link( cli.getVersionnedFolderURL( repoBaseUrl, csr.getVersion() ) );
        sink.text( csr.getVersion() );
        sink.link_();
        sink.text( "/" );
        if ( csr.central.isEmpty() )
        {
            iconSuccess( sink );
        }
        else
        {
            iconWarning( sink );
        }
        for ( String missing : csr.central )
        {
            sink.lineBreak();
            iconError( sink );
            sink.rawText( missing );
        }
        sink.tableCell_();

        // dist column
        sink.tableCell();
        sink.link( cli.getDist() );
        sink.text( cli.getDist().substring( DIST_AREA.length() ) );
        sink.link_();
        sink.text( "source-release" );
        if ( csr.dist.isEmpty() && csr.older.isEmpty() )
        {
            iconSuccess( sink );
        }
        else
        {
            iconWarning( sink );
        }
        StringBuilder cliMissing = new StringBuilder();
        for ( String missing : csr.dist )
        {
            sink.lineBreak();
            iconError( sink );
            sink.rawText( missing );
            cliMissing.append( "\nwget -O " ).append( cli.getVersionnedFolderURL( repoBaseUrl, csr.getVersion() ) ).
                    append( "/" ).append( missing );
            cliMissing.append( "\nsvn add " ).append( missing );
        }
        if ( !cliMissing.toString().isEmpty() )
        {
            sink.lineBreak();
            SinkEventAttributeSet atts = new SinkEventAttributeSet();
            sink.unknown( "pre", new Object[]
            {
                new Integer( HtmlMarkup.TAG_TYPE_START )
            }, atts );
            sink.text( cliMissing.toString() );
            sink.unknown( "pre", new Object[]
            {
                new Integer( HtmlMarkup.TAG_TYPE_END )
            }, null );
        }

        StringBuilder cliOlder = new StringBuilder();
        for ( String missing : csr.older )
        {
            sink.lineBreak();
            iconRemove( sink );
            sink.rawText( missing );
            cliOlder.append( "\nsvn rm " ).append( missing );
        }
        if ( !cliOlder.toString().isEmpty() )
        {
            sink.lineBreak();
            SinkEventAttributeSet atts = new SinkEventAttributeSet();
            sink.unknown( "pre", new Object[]
            {
                new Integer( HtmlMarkup.TAG_TYPE_START )
            }, atts );
            sink.text( cliOlder.toString() );
            sink.unknown( "pre", new Object[]
            {
                new Integer( HtmlMarkup.TAG_TYPE_END )
            }, null );
        }

        sink.tableCell_();
        sink.tableRow_();
    }

    @Override
    protected void executeReport( Locale locale )
            throws MavenReportException
    {
        if ( !outputDirectory.exists() )
        {
            outputDirectory.mkdirs();
        }
        try
        {
            this.execute();
        }
        catch ( MojoExecutionException ex )
        {
            throw new MavenReportException( ex.getMessage(), ex );
        }
        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text( "Check source release" );
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.paragraph();
        sink.text( "Check Source Release"
                + " (= artifactId + version + '-source-release.zip[.asc|.md5]') availability in:" );
        sink.paragraph_();
        sink.paragraph();
        sink.text( "cli command and olders artifact exploration is Work In Progress" );
        sink.paragraph_();
        sink.list();
        sink.listItem();
        sink.link( repoBaseUrl );
        sink.text( "central" );
        sink.link_();
        sink.listItem_();
        sink.listItem();
        sink.link( DIST_AREA );
        sink.text( "Apache distribution area" );
        sink.link_();
        sink.listItem_();
        sink.list_();
        sink.section1_();
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.rawText( "groupId" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "artifactId" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "LATEST" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "DATE" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "central" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "dist" );
        sink.tableHeaderCell_();
        sink.tableRow_();

        for ( DistCheckSourceRelease csr : results )
        {
            reportLine( sink, csr );

        }
        sink.table_();
        sink.body_();
        sink.flush();
        sink.close();
    }

    /**
     * Report a pattern for an artifact.
     *
     * @param artifact artifact name
     * @return regex
     */
    protected static String getArtifactPattern( String artifact )
    {
        /// not the safest
        return "^" + artifact + "-[0-9].*source-release.*$";
    }

    private Elements selectLinks( String repourl )
            throws IOException
    {
        try
        {
            Document doc = Jsoup.connect( repourl ).get();
            return doc.select( "a[href]" );
        }
        catch ( IOException ioe )
        {
            throw new IOException( "IOException while reading " + repourl, ioe );
        }
    }

    private List<String> checkOldinRepos( String repourl, ConfigurationLineInfo configLine, String version )
            throws IOException
    {
        Elements links = selectLinks( repourl );

        List<String> expectedFile = new LinkedList<>();

        expectedFile.add( configLine.getArtifactId() + "-" + version + "-source-release.zip" );
        expectedFile.add( configLine.getArtifactId() + "-" + version + "-source-release.zip.asc" );
        expectedFile.add( configLine.getArtifactId() + "-" + version + "-source-release.zip.md5" );

        List<String> retrievedFile = new LinkedList<>();
        for ( Element e : links )
        {
            String art = e.attr( "href" );
            if ( art.matches( getArtifactPattern( configLine.getArtifactId() ) ) )
            {
                retrievedFile.add( e.attr( "href" ) );
            }
        }

        retrievedFile.removeAll( expectedFile );

        if ( !retrievedFile.isEmpty() )
        {
            // write the following output in red so it's more readable in jenkins console
            getLog().error( "Older version than " + version + " for "
                    + configLine.getArtifactId() + " still available in " + repourl );
            for ( String sourceItem : retrievedFile )
            {
                getLog().error( " > " + sourceItem + " <" );
            }
        }

        return retrievedFile;
    }

    private List<String> checkRepos( String repourl, ConfigurationLineInfo configLine, String version )
            throws IOException
    {
        Elements links = selectLinks( repourl );

        List<String> expectedFile = new LinkedList<>();
        // build source artifact name
        expectedFile.add( configLine.getArtifactId() + "-" + version + "-source-release.zip" );
        expectedFile.add( configLine.getArtifactId() + "-" + version + "-source-release.zip.asc" );
        expectedFile.add( configLine.getArtifactId() + "-" + version + "-source-release.zip.md5" );

        List<String> retrievedFile = new LinkedList<>();
        for ( Element e : links )
        {
            retrievedFile.add( e.attr( "href" ) );
        }

        expectedFile.removeAll( retrievedFile );

        if ( !expectedFile.isEmpty() )
        {
            getLog().error( "Missing archive for " + configLine.getArtifactId() + " in " + repourl );
            for ( String sourceItem : expectedFile )
            {
                getLog().error( " > " + sourceItem + " <" );
            }
        }

        return expectedFile;
    }

    @Override
    void checkArtifact( ConfigurationLineInfo configLine, String latestVersion )
            throws MojoExecutionException
    {
        try
        {
            DistCheckSourceRelease result = new DistCheckSourceRelease( configLine,
                    latestVersion );
            results.add( result );
            // central
            result.setMissingCentralSourceRelease(
                    checkRepos( configLine.getVersionnedFolderURL(
                    repoBaseUrl, latestVersion ),
                    configLine, latestVersion ) );
            //dist
            result.setMissingDistSourceRelease(
                    checkRepos( configLine.getDist(), configLine, latestVersion ) );
            result.setOlderSourceRelease(
                    checkOldinRepos( configLine.getDist(), configLine, latestVersion ) );
        }
        catch ( IOException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
    }
}
