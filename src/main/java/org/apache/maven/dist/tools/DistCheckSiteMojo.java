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
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.HttpStatusException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 *
 * @author skygo
 */
@Mojo( name = "check-site" )
public class DistCheckSiteMojo extends AbstractDistCheckMojo
{
// common type for site checker

    @Parameter( property = "localRepository", required = true, readonly = true )
    protected ArtifactRepository localRepository;
    @Component
    private ArtifactFactory artifactFactory;
    @Component
    private MavenProjectBuilder mavenProjectBuilder;

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

    interface HTMLChecker
    {

        /**
         * name of the checker.
         *
         * @return
         */
        String getName();

        /**
         * true if checker find pattern in document
         *
         * @param doc
         * @param version
         * @return
         */
        boolean isOk( Document doc, String version );
    }

    class DistCheckSiteResult extends AbstractCheckResult
    {

        private String url;
        private Map<HTMLChecker, Boolean> checkMap = new HashMap<>();
        private int statusCode = 200;

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

        private void setHTTPErrorUrl( int statusCode )
        {
            this.statusCode = statusCode;
        }

        /**
         * @return the statusCode
         */
        public int getStatusCode()
        {
            return statusCode;
        }

        private void getOverall( Sink sink )
        {

            if ( statusCode != 200 )
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
    }
    // keep result
    private List<DistCheckSiteResult> results = new LinkedList<>();
    private List<HTMLChecker> checker = new LinkedList<>();
    private List<ArtifactRepository> artifactRepositories = new LinkedList<>();

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
        sink.rawText( "Checked sites" );
        sink.section1_();
        sink.table();
        sink.tableRow();
        sink.tableHeaderCell();
        sink.rawText( "groupId:artifactId (from conf file)" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "Latest version from metadata" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "URL" );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "check summary" );
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
            sink.rawText( csr.getConfigurationLine().getGroupId() + ":" );
            sink.rawText( csr.getConfigurationLine().getArtifactId() );
            sink.tableCell_();
            sink.tableCell();
            sink.rawText( csr.getVersion() );
            sink.tableCell_();
            sink.tableCell();
            if ( csr.getStatusCode() != 200 )
            {
                iconError( sink );
                sink.rawText( "[" + csr.getStatusCode() + "]" + csr.getUrl() );
            }
            else
            {
                sink.rawText( csr.getUrl() );
            }
            sink.tableCell_();
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
        try (BufferedReader input = new BufferedReader( new InputStreamReader( new URL( configLine.getMetadataFileURL( repoBaseUrl ) ).openStream() ) ))
        {
            JAXBContext context = JAXBContext.newInstance( MavenMetadata.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MavenMetadata metadata = ( MavenMetadata ) unmarshaller.unmarshal( input );

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
        ArtifactRepository aa = new MavenArtifactRepository( "central", repoBaseUrl, new DefaultRepositoryLayout(), new ArtifactRepositoryPolicy( false, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN ), new ArtifactRepositoryPolicy( true, ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN ) );
        artifactRepositories.add( aa );

        //add  html checker
        checker.add( new HTMLChecker()
        {
            @Override
            public String getName()
            {
                return "Stylus Skin";
            }

            @Override
            public boolean isOk( Document doc, String version )
            {
                Element links = doc.select( "div.xright" ).first();
                if ( links != null )
                {
                    return links.text().contains( version );
                }
                else
                {
                    return false;
                }
            }
        } );
        checker.add( new HTMLChecker()
        {
            @Override
            public String getName()
            {
                return "Fluido Skin";
            }

            @Override
            public boolean isOk( Document doc, String version )
            {
                Element links = doc.select( "li#projectVersion" ).first();
                if ( links != null )
                {
                    return links.text().contains( version );
                }
                else
                {
                    return false;
                }
            }
        } );
        super.execute();
    }

    private void iconError( Sink sink )
    {
        sink.figure();
        sink.figureCaption();
        sink.text( "error" );
        sink.figureCaption_();
        sink.figureGraphics( "images/icon_error_sml.gif" );
        sink.figure_();
    }

    private void iconWarning( Sink sink )
    {
        sink.figure();
        sink.figureCaption();
        sink.text( "warning" );
        sink.figureCaption_();
        sink.figureGraphics( "images/icon_warning_sml.gif" );
        sink.figure_();
    }

    private void iconSuccess( Sink sink )
    {
        sink.figure();
        sink.figureCaption();
        sink.text( "success" );
        sink.figureCaption_();
        sink.figureGraphics( "images/icon_success_sml.gif" );
        sink.figure_();
    }
}
