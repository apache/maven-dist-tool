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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.artifact.Artifact;
import org.apache.maven.dist.tools.checkers.HTMLChecker;
import org.apache.maven.dist.tools.checkers.HTMLCheckerFactory;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

/**
 *
 * @author skygo
 */
@Mojo( name = "check-site" )
public class DistCheckSiteMojo extends AbstractDistCheckMojo
{
    private static final String MAVEN_SITE = "http://maven.apache.org";
    private static final int HTTP_OK = 200;

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
                        text += "Nothing";
                    }
                }
                
                if ( isSkink( "Fluido" ) )
                {
                    sink.text( "Fluido" );
                }
                else if ( isSkink( "Stylus" ) )
                {
                    sink.text( "Stylus" );
                }
                else 
                {
                    sink.text( "Not determined" );
                }
                sink.monospaced();
                sink.text( text );
                sink.monospaced_();
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

        private boolean isSkink( String skinName )
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
    }
    // keep result
    private List<DistCheckSiteResult> results = new LinkedList<>();
    private final List<HTMLChecker> checker = HTMLCheckerFactory.getCheckers();
    
    @Override
    protected void executeReport( Locale locale ) throws MavenReportException
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
        sink.rawText( "This is to help maintaining some coherence. How many site are skin fluido, stylus, where they have version (right left)" );
        sink.rawText( "All sun icons in one column is kind of objective." );
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
        sink.rawText( "URL" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "Skins and comments on top of html (helping for date but not always)" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "Precise and overkill contents check summary details on your left ==>" );
        sink.tableHeaderCell_();
        for ( HTMLChecker c : checker )
        {
            sink.tableHeaderCell();
            sink.rawText( c.getName() );
            sink.tableHeaderCell_();
        }

        sink.tableRow_();
        for ( DistCheckSiteResult csr : results )
        {
            sink.tableRow();
            sink.tableCell();
            sink.rawText( csr.getConfigurationLine().getGroupId() );
            sink.tableCell_();

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
            sink.tableCell_();
            
            sink.tableHeaderCell();
            csr.getSkins( sink );
            sink.tableHeaderCell_();
            
            sink.tableHeaderCell();
            csr.getOverall( sink );
            sink.tableHeaderCell_();

            for ( HTMLChecker c : checker )
            {
                sink.tableCell();
                if ( csr.getCheckMap().get( c ) != null )
                {
                    if ( csr.getCheckMap().get( c ) )
                    {
                        iconSuccess( sink );
                    }
                    else
                    {
                        iconWarning( sink );
                    }
                }
                else
                {
                    iconError( sink );
                }

                sink.tableCell_();
            }
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

    private void checkSite( String repourl, ConfigurationLineInfo r, String version )
    {
        DistCheckSiteResult result = new DistCheckSiteResult( r, version );
        results.add( result );
        StringBuilder message = new StringBuilder();
        try
        {
            Artifact pluginArtifact = artifactFactory.createProjectArtifact( r.getGroupId(), r.getArtifactId(), version );
            MavenProject pluginProject = mavenProjectBuilder.buildFromRepository( pluginArtifact, artifactRepositories, localRepository, false );

            result.setUrl( pluginProject.getUrl() );
            Document doc = Jsoup.connect( pluginProject.getUrl() ).get();
            
            message.append( "Site for " ).append( pluginProject.getArtifactId() ).append( " at " ).append( pluginProject.getUrl() ).append( " seek for" ).append( pluginProject.getVersion() ).append( "    " );
            for ( HTMLChecker c : checker )
            {
                result.getCheckMap().put( c, c.isOk( doc, version ) );
                message.append( "[" ).append( c.getName() ).append( c.isOk( doc, version ) ).append( "]" );
            }
            result.setDocument( doc );
            getLog().warn( message.toString() );

        }
        catch ( HttpStatusException hes )
        {
            getLog().warn( hes.getStatusCode() + " for " + hes.getUrl() );
            result.setHTTPErrorUrl( hes.getStatusCode() );
        }
        catch ( Exception ex )
        {
            //continue for  other artifact
            getLog().error( ex.getMessage() );
            getLog().error( ex );
        }

    }

    @Override
    void checkArtifact( ConfigurationLineInfo configLine, String repoBaseUrl ) throws MojoExecutionException
    {
        try ( BufferedReader input = new BufferedReader( new InputStreamReader( new URL( configLine.getMetadataFileURL( repoBaseUrl ) ).openStream() ) ) )
        {
            JAXBContext context = JAXBContext.newInstance( MavenMetadata.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MavenMetadata metadata = ( MavenMetadata ) unmarshaller.unmarshal( input );
            configLine.addMetadata( metadata );
            getLog().info( "Checking for site for artifact : " + configLine.getGroupId() + ":" + configLine.getArtifactId() + ":" + metadata.versioning.latest );
            // revert sort versions (not handling alpha and complex vesion scheme but more usefull version are displayed left side
            Collections.sort( metadata.versioning.versions, Collections.reverseOrder() );
            getLog().warn( metadata.versioning.versions + " version(s) detected " + repoBaseUrl );

            // central
            checkSite( configLine.getVersionnedPomFileURL( repoBaseUrl, metadata.versioning.latest ), configLine, metadata.versioning.latest );

        }
        catch ( MalformedURLException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
        catch ( IOException | JAXBException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
    }

    @Override
    public void execute() throws MojoExecutionException
    {
        //resolve only to what we set
        

        super.execute();
    }
}
