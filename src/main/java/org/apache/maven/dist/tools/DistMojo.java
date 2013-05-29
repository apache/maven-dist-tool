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
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author skygo
 */
@Mojo( name = "check-source" )
public class DistMojo extends AbstractMojo
{

    // parameters for future usage
    @Parameter( property = "repository.url", defaultValue = "http://repo1.maven.org/maven2/" )
    private String repoBaseUrl;
    @Parameter( property = "database.url", defaultValue = "db/mavendb.csv" )
    private String dbLocation;

    // parameters for future usage
    private enum CheckType
    {

        SOURCE
    }

    private void checkRepos( String repourl, Request r, String version, CheckType ct ) throws IOException
    {
        Document doc = Jsoup.connect( repourl ).get();
        Elements links = doc.select( "a[href]" );
        List<String> expectedFile = new LinkedList<>();
        List<String> retrievedFile = new LinkedList<>();
        switch ( ct )
        {
            case SOURCE:
            {
                // http://maven.apache.org/developers/release/maven-project-release-procedure.html#Copy_the_source_release_to_the_Apache_Distribution_Area
                // build source artifact name
                expectedFile.add( r.artifactId + "-" + version + "-" + "source-release.zip" );
                expectedFile.add( r.artifactId + "-" + version + "-" + "source-release.zip.asc" );
                expectedFile.add( r.artifactId + "-" + version + "-" + "source-release.zip.md5" );
            }
            break;
            default:
                getLog().warn( "For future extensions" );
        }

        for ( Element e : links )
        {
            retrievedFile.add( e.attr( "href" ) );
        }
        expectedFile.removeAll( retrievedFile );
        if ( !expectedFile.isEmpty() )
        {
            for ( String sourceItem : expectedFile )
            {
                getLog().error( "Missing:" + sourceItem + " in " + repourl );
            }
        }
    }

    private void checkArtifact( Request r, CheckType ct ) throws MojoExecutionException
    {
        try (BufferedReader input = new BufferedReader( new InputStreamReader( new URL( r.getMetadataUrl( repoBaseUrl ) ).openStream() ) ))
        {
            JAXBContext context = JAXBContext.newInstance( MavenMetadata.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MavenMetadata metadata = ( MavenMetadata ) unmarshaller.unmarshal( input );

            getLog().info( "Checking for artifact : " + r.getGroupId() + ":" + r.getArtifactId() + ":" + metadata.versioning.latest );
            // revert sort versions (not handling alpha and complex vesion scheme but more usefull version are displayed left side
            Collections.sort( metadata.versioning.versions, Collections.reverseOrder() );
            getLog().warn( metadata.versioning.versions + " version(s) detected " + repoBaseUrl );

            // central
            checkRepos( r.getVersionnedURL( repoBaseUrl, metadata.versioning.latest ), r, metadata.versioning.latest, ct );
            //dist
            checkRepos( r.getDist(), r, metadata.versioning.latest, ct );
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
    public void execute() throws MojoExecutionException, MojoFailureException
    {

        URL dbURL;
        if ( dbLocation.equals( "db/mavendb.csv" ) )
        {
            dbURL = Thread.currentThread().getContextClassLoader().getResource( "db/mavendb.csv" );
        }
        else
        {
            throw new MojoFailureException( "Custom data not implemented " );
        }


        try (BufferedReader input = new BufferedReader( new InputStreamReader( dbURL.openStream() ) ))
        {
            String text;
            while ( (text = input.readLine()) != null )
            {
                if ( text.startsWith( "##" ) )
                {
                    getLog().info( text );
                }
                else
                {
                    String[] artifactInfo = text.split( ";" );
                    checkArtifact( new Request( artifactInfo[0], artifactInfo[1], artifactInfo[2] ), CheckType.SOURCE );
                }

            }
        }
        catch ( IOException ex )
        {
            Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, ex );
        }




    }

    private static class Request
    {

        private final String groupId;
        private final String artifactId;
        private final String dist;
        private static final String URLSEP = "/";

        public Request( String groupId, String artifactId, String dist )
        {
            this.groupId = groupId;
            this.artifactId = artifactId;
            this.dist = dist;
        }

        /**
         * @return the groupId
         */
        public String getGroupId()
        {
            return groupId;
        }

        /**
         * @return the artifactId
         */
        public String getArtifactId()
        {
            return artifactId;
        }

        /**
         * @return the dist
         */
        public String getDist()
        {
            return dist;
        }

        private String getBaseURL( String repoBaseUrl, String folder )
        {
            return repoBaseUrl + groupId.replaceAll( "\\.", URLSEP ) + URLSEP + artifactId + URLSEP + folder;
        }

        private String getMetadataUrl( String repoBaseUrl )
        {
            return getBaseURL( repoBaseUrl, "maven-metadata.xml" );
        }

        private String getVersionnedURL( String repoBaseUrl, String version )
        {
            return getBaseURL( repoBaseUrl, version );
        }
    }
}
