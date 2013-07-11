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

import java.io.File;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.dist.tools.checkers.HTMLChecker;
import org.apache.maven.dist.tools.checkers.HTMLCheckerFactory;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributeSet;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author skygo
 */
@Mojo( name = "check-site", requiresProject = false )
public class DistCheckSiteMojo
    extends AbstractDistCheckMojo
{
    /**
     * Artifact factory.
     */
    @Component
    protected ArtifactFactory artifactFactory;

    /**
     * Local repository.
     */
    @Parameter( defaultValue = "${localRepository}", required = true, readonly = true )
    protected ArtifactRepository localRepository;

    /**
     * Maven project builder.
     */
    @Component
    protected MavenProjectBuilder mavenProjectBuilder;
    
    /**
     * Take screenshot with web browser
     */
    @Parameter( property = "screenshot", defaultValue = "false" )
    protected boolean screenShot;
    
    private static final String MAVEN_SITE = "http://maven.apache.org";
    /**
     * Http status ok code.
     */
    protected static final int HTTP_OK = 200;

    @Override
    public String getOutputName()
    {
        return "dist-tool-checksite";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Disttool> Sites";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Verification of various maven web sites";
    }

    class DistCheckSiteResult extends AbstractCheckResult
    {

        private String url;
        private Map<HTMLChecker, Boolean> checkMap = new HashMap<>();
        private int statusCode = HTTP_OK;
        private Document document;
        private String screenshotName;

        public DistCheckSiteResult( ConfigurationLineInfo r, String version )
        {
            super( r, version );
        }

        void setUrl( String url )
        {
            this.url = url;
        }

        /**
         * @return the url
         */
        public String getUrl()
        {
            return url;
        }

        /**
         * @return the checkMap
         */
        public Map<HTMLChecker, Boolean> getCheckMap()
        {
            return checkMap;
        }

        private void setHTTPErrorUrl( int status )
        {
            this.statusCode = status;
        }

        /**
         * @return the statusCode
         */
        public int getStatusCode()
        {
            return statusCode;
        }

        private void getSkins( Sink sink )
        {
            if ( statusCode != HTTP_OK )
            {
                sink.text( "None" );
            }
            else 
            {
                String text = "";
                Elements htmlTag = document.select( "html " );
                for ( Element htmlTa : htmlTag )
                {
                    Node n = htmlTa.previousSibling();
                    if ( n instanceof Comment )
                    {
                        text += ( ( Comment ) n ).getData();
                    }
                    else
                    {
                        text += " ";
                    }
                }

                sink.text( "skin: " );
                if ( isSkin( "Fluido" ) )
                {
                    sink.text( "Fluido" );
                }
                else if ( isSkin( "Stylus" ) )
                {
                    sink.text( "Stylus" );
                }
                else 
                {
                    sink.text( "Not determined" );
                }
                sink.verbatim( null );
                sink.text( text.trim() );
                sink.verbatim_();
            }
        }

        private void getOverall( Sink sink )
        {

            if ( statusCode != HTTP_OK )
            {
                iconError( sink );
            }
            else
            {
                boolean tmp = false;
                for ( Map.Entry<HTMLChecker, Boolean> e : checkMap.entrySet() )
                {
                    tmp = tmp || e.getValue();
                }
                if ( tmp )
                {
                    iconSuccess( sink );
                }
                else
                {
                    iconWarning( sink );
                }
            }
        }

        private boolean isSkin( String skinName )
        {
            boolean tmp = false;
            for ( Map.Entry<HTMLChecker, Boolean> e : checkMap.entrySet() )
            {
                if ( e.getKey().getSkin().equals( skinName ) )
                {
                    tmp = tmp || e.getValue();
                }
            }
            return tmp;
        }

        private void setDocument( Document doc )
        {
            this.document = doc ;
        }

        private void setScreenShot( String fileName )
        {
            this.screenshotName = fileName;
        }
        private String getScreenShot()
        {
            return screenshotName;
        }
    }

    // keep result
    private List<DistCheckSiteResult> results = new LinkedList<>();
    private final List<HTMLChecker> checker = HTMLCheckerFactory.getCheckers();
    private WebDriver driver;

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
        sink.text( "Check sites" );
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.rawText( "Checked sites, also do some basic checking in index.html contents." );
        sink.rawText( "This is to help maintaining some coherence. How many site are skin fluido, stylus,"
                + " where they have artifact version (right, left)" );
        sink.rawText( "All sun icons in one column is kind of objective." );
        sink.section1_();
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.rawText( "groupId/artifactId" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "LATEST" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "DATE" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "URL" );
        sink.lineBreak();
        sink.rawText( "Skins" );
        sink.lineBreak();
        sink.rawText( "Comments on top of html" );
        sink.tableHeaderCell_();
        if ( screenShot )
        {
            sink.tableHeaderCell();
            sink.rawText( "Screen" );
            sink.tableHeaderCell_();
        }
        sink.tableHeaderCell();
        sink.rawText( "Artifact version displayed" );
        sink.tableHeaderCell_();
        sink.tableRow_();

        String directory = null;
        for ( DistCheckSiteResult csr : results )
        {
            if ( !csr.getConfigurationLine().getDirectory().equals( directory ) )
            {
                directory = csr.getConfigurationLine().getDirectory();
                sink.tableRow();
                sink.tableHeaderCell();
                // shorten groupid
                sink.rawText( csr.getConfigurationLine().getGroupId().replaceAll( "org.apache.maven", "o.a.m" ) );
                sink.tableHeaderCell_();
                for ( int i = 0; i < 5 ; i++ )
                {
                    sink.tableHeaderCell();
                    sink.rawText( " " );
                    sink.tableHeaderCell_();
                }
                sink.tableRow_();
            }

            sink.tableRow();
            sink.tableCell();
            sink.rawText( csr.getConfigurationLine().getArtifactId() );
            sink.tableCell_();

            sink.tableCell();
            sink.rawText( csr.getVersion() );
            sink.tableCell_();
            
            sink.tableCell();
            sink.rawText( csr.getConfigurationLine().getReleaseFromMetadata() );
            sink.tableCell_();
            sink.tableCell();
            if ( csr.getStatusCode() != HTTP_OK )
            {
                iconError( sink );
                sink.rawText( "[" + csr.getStatusCode() + "] " );
            }
            sink.link( csr.getUrl() );
            sink.rawText( getSimplifiedUrl( csr.getUrl() ) );
            sink.link_();
            sink.lineBreak();
            csr.getSkins( sink );
            sink.tableCell_();
            if ( screenShot )
            {
                sink.tableCell();
                sink.figure( null );
                SinkEventAttributeSet atts = new SinkEventAttributeSet();
                // no direct attribute, override style only
                atts.addAttribute( "style", "height:200px;width:200px" );
                atts.addAttribute( "alt", getSimplifiedUrl( csr.getUrl() ) );
                sink.figureGraphics( csr.getScreenShot(), atts );
                sink.figure_();
                sink.tableCell_();
            }
            
            sink.tableCell();
            csr.getOverall( sink );
            for ( HTMLChecker c : checker )
            {
                if ( ( csr.getCheckMap().get( c ) != null ) && csr.getCheckMap().get( c ) )
                {
                    sink.text( ": " + c.getName() );
                }
            }
            sink.tableCell_();

            sink.tableRow_();
        }
        sink.table_();
        sink.body_();
        sink.flush();
        sink.close();
    }

    private String getSimplifiedUrl( String url )
    {
        if ( url.startsWith( MAVEN_SITE ) )
        {
            return url.substring( MAVEN_SITE.length() );
        }
        return url;
    }

    private void checkSite( ConfigurationLineInfo configLine, String version )
    {
        DistCheckSiteResult result = new DistCheckSiteResult( configLine, version );
        results.add( result );
        try
        {
            Artifact pluginArtifact =
                artifactFactory.createProjectArtifact( configLine.getGroupId(), configLine.getArtifactId(), version );
            MavenProject pluginProject =
                mavenProjectBuilder.buildFromRepository( pluginArtifact, artifactRepositories, localRepository, false );

            result.setUrl( pluginProject.getUrl() );
            Document doc = Jsoup.connect( pluginProject.getUrl() ).get();
            if ( screenShot )
            {
                driver.get( pluginProject.getUrl() );
                File scrFile = ( ( TakesScreenshot ) driver ).getScreenshotAs( OutputType.FILE );
                String fileName = "images" + File.separator
                        + configLine.getGroupId() + "_" + configLine.getArtifactId() + ".png";
                result.setScreenShot( fileName );
                FileUtils.copyFile( scrFile, new File( getReportOutputDirectory() + File.separator + fileName ) );
            }
            for ( HTMLChecker c : checker )
            {
                result.getCheckMap().put( c, c.isOk( doc, version ) );
            }
            result.setDocument( doc );
            
        }
        catch ( HttpStatusException hes )
        {
            addErrorLine( "HTTP result code: " + hes.getStatusCode() + " for " + hes.getUrl() );
            result.setHTTPErrorUrl( hes.getStatusCode() );
        }
        catch ( Exception ex )
        {
            //continue for  other artifact
            getLog().error( ex.getMessage() + configLine.getArtifactId() );
        }

    }

    @Override
    void checkArtifact( ConfigurationLineInfo configLine, String latestVersion )
        throws MojoExecutionException
    {
        checkSite( configLine, latestVersion );
    }

    @Override
    public void execute()
        throws MojoExecutionException
    {
        try
        {
            //resolve only to what we set
            if ( screenShot )
            {
                // create driver once reduce time to complete mojo
                driver = new FirefoxDriver();
            }
            super.execute();
        }
        finally
        {
            if ( screenShot )
            {
                driver.close();
            }
        }
    }
}
