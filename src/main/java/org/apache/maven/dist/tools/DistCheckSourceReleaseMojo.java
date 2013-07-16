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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.maven.doxia.markup.HtmlMarkup;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributeSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
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
    static final String FAILURES_FILENAME = "check-source-release.log";

    private static final String DIST_AREA = "http://www.apache.org/dist/maven/";
    //private static final String DIST_SVNPUBSUB = "https://dist.apache.org/repos/dist/release/maven/";
    @Override
    boolean useDetailed()
    {
        return false;
    }
    /**
     * Ignore dist failure for <code>artifactId</code> or <code>artifactId:version</code>
     */
    @Parameter
    protected List<String> ignoreDistFailures;

    protected String getFailuresFilename()
    {
        return FAILURES_FILENAME;
    }

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

    private static class DistCheckSourceRelease
        extends AbstractCheckResult
    {

        private List<String> central;
        private List<String> dist;
        private List<String> distOlder;

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

        private void setDistOlderSourceRelease( List<String> checkRepos )
        {
            distOlder = checkRepos;
        }
    }
    private final List<DistCheckSourceRelease> results = new LinkedList<>();

    private static class DirectoryStatistics
    {
        final String directory;
        int artifactsCount = 0;
        int centralMissing = 0;
        int distError = 0;
        int distMissing = 0;
        int distOlder = 0;

        public DirectoryStatistics( String directory )
        {
            this.directory = directory;
        }

        public boolean contains( DistCheckSourceRelease csr )
        {
            return csr.getConfigurationLine().getDirectory().equals( directory );
        }

        public void addArtifact( DistCheckSourceRelease result )
        {
            artifactsCount++;
            if ( !result.central.isEmpty() )
            {
                centralMissing++;
            }
            if ( !result.dist.isEmpty() || !result.distOlder.isEmpty() )
            {
                distError++;
            }
            if ( !result.dist.isEmpty() )
            {
                distMissing++;
            }
            if ( !result.distOlder.isEmpty() )
            {
                distOlder++;
            }
        }
    }

    private void reportLine( Sink sink, DistCheckSourceRelease csr )
    {
        ConfigurationLineInfo cli = csr.getConfigurationLine();

        sink.tableRow();
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
        String directory = cli.getDirectory() + ( cli.isSrcBin() ? ( "/" + csr.getVersion() + "/source" ) : "" );
        sink.link( DIST_AREA + directory );
        sink.text( directory );
        sink.link_();
        sink.text( "source-release" );
        if ( csr.dist.isEmpty() && csr.distOlder.isEmpty() )
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
            if ( !csr.central.contains( missing ) )
            {
                // if the release distribution is in central repository, we can get it from there...
                cliMissing.append( "\nwget " ).append( cli.getVersionnedFolderURL( repoBaseUrl, csr.getVersion() ) ).
                        append( "/" ).append( missing );
                cliMissing.append( "\nsvn add " ).append( missing );
            }
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
        for ( String missing : csr.distOlder )
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

        DirectoryStatistics stats = new DirectoryStatistics( "" ); // global stats

        List<DirectoryStatistics> statistics = new ArrayList<>();
        DirectoryStatistics current = null;
        for ( DistCheckSourceRelease csr : results )
        {
            if ( ( current == null ) || !current.contains( csr ) )
            {
                current = new DirectoryStatistics( csr.getConfigurationLine().getDirectory() );
                statistics.add( current );
            }
            current.addArtifact( csr );
            stats.addArtifact( csr );
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
        sink.paragraph();
        sink.text( "Older artifacts exploration is Work In Progress..." );
        sink.paragraph_();
        sink.section1_();
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.rawText( "groupId/artifactId: " + String.valueOf( stats.artifactsCount ) );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "LATEST" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "DATE" );
        sink.tableHeaderCell_();
        reportStatisticsHeader( stats, sink );
        sink.tableRow_();

        Iterator<DirectoryStatistics> dirs = statistics.iterator();
        current = null;

        for ( DistCheckSourceRelease csr : results )
        {
            if ( ( current == null ) || !current.contains( csr ) )
            {
                current = dirs.next();

                sink.tableRow();
                sink.tableHeaderCell();
                // shorten groupid
                sink.rawText( csr.getConfigurationLine().getGroupId().replaceAll( "org.apache.maven", "o.a.m" ) + ": "
                    + String.valueOf( current.artifactsCount ) );
                sink.tableHeaderCell_();
                sink.tableHeaderCell();
                sink.rawText( " " );
                sink.tableHeaderCell_();
                sink.tableHeaderCell();
                sink.rawText( " " );
                sink.tableHeaderCell_();
                reportStatisticsHeader( current, sink );
                sink.tableRow_();
            }

            reportLine( sink, csr );
        }

        sink.table_();
        sink.body_();
        sink.flush();
        sink.close();
    }

    private void reportStatisticsHeader( DirectoryStatistics current, Sink sink )
    {
        sink.tableHeaderCell();
        sink.rawText( "central: " + String.valueOf( current.artifactsCount - current.centralMissing ) );
        iconSuccess( sink );
        if ( current.centralMissing > 0 )
        {
            sink.rawText( "/" + String.valueOf( current.centralMissing ) );
            iconWarning( sink );
        }
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "dist: " + String.valueOf( current.artifactsCount - current.distError ) );
        iconSuccess( sink );
        if ( current.distError > 0 )
        {
            sink.rawText( "/" + String.valueOf( current.distError ) );
            iconWarning( sink );
            sink.rawText( "= " + String.valueOf( current.distMissing ) );
            iconError( sink );
            sink.rawText( "/" + String.valueOf( current.distOlder ) );
            iconRemove( sink );
        }
        sink.tableHeaderCell_();
    }

    /**
     * Report a pattern for an artifact source release.
     *
     * @param artifact artifact name
     * @return regex
     */
    protected static String getSourceReleasePattern( String artifact )
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

    private List<String> checkContainsOld( String url, ConfigurationLineInfo cli, String version )
            throws IOException
    {
        Elements links = selectLinks( url );

        List<String> retrievedFile = new LinkedList<>();
        for ( Element e : links )
        {
            String art = e.attr( "href" );
            if ( art.matches( getSourceReleasePattern( cli.getArtifactId() ) ) )
            {
                retrievedFile.add( e.attr( "href" ) );
            }
        }

        List<String> expectedFiles = cli.getExpectedFilenames( version, true );

        retrievedFile.removeAll( expectedFiles );

        if ( !retrievedFile.isEmpty() )
        {
            // write the following output in red so it's more readable in jenkins console
            addErrorLine( cli, version, ignoreDistFailures,
                          "Older version than " + version + " for " + cli.getArtifactId() + " still available in "
                              + url );
            for ( String sourceItem : retrievedFile )
            {
                addErrorLine( cli, version, ignoreDistFailures, " > " + sourceItem + " <" );
            }
        }

        return retrievedFile;
    }

    /**
     * Check that url points to a directory index containing expected release files
     * @param url
     * @param cli
     * @param version
     * @return missing files
     * @throws IOException
     */
    private List<String> checkDirectoryIndex( String url, ConfigurationLineInfo cli, String version, boolean dist )
            throws IOException
    {
        List<String> retrievedFile = new LinkedList<>();
        Elements links = selectLinks( url );
        for ( Element e : links )
        {
            retrievedFile.add( e.attr( "href" ) );
        }

        List<String> missingFiles;
        // initialize missing files with expected release file names
        missingFiles = cli.getExpectedFilenames( version, dist );

        // removed retrieved files
        missingFiles.removeAll( retrievedFile );

        if ( !missingFiles.isEmpty() )
        {
            addErrorLine( cli, version, ignoreDistFailures, "Missing file for " + cli.getArtifactId() + " in " + url );
            for ( String sourceItem : missingFiles )
            {
                addErrorLine( cli, version, ignoreDistFailures, " > " + sourceItem + " <" );
            }
        }

        return missingFiles;
    }

    @Override
    protected void checkArtifact( ConfigurationLineInfo configLine, String version )
            throws MojoExecutionException
    {
        try
        {
            DistCheckSourceRelease result = new DistCheckSourceRelease( configLine, version );
            results.add( result );

            // central
            String centralUrl = configLine.getVersionnedFolderURL( repoBaseUrl, version );
            result.setMissingCentralSourceRelease( checkDirectoryIndex( centralUrl, configLine, version, false ) );

            // dist
            String distUrl =
                DIST_AREA + configLine.getDirectory() + ( configLine.isSrcBin() ? ( "/" + version + "/source" ) : "" );
            result.setMissingDistSourceRelease( checkDirectoryIndex( distUrl, configLine, version, true ) );
            result.setDistOlderSourceRelease( checkContainsOld( distUrl, configLine, version ) );
        }
        catch ( IOException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
    }
}
