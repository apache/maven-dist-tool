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
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
    private File db;
    private List<Request> requestList = new LinkedList<Request>();
    // parameters for future usage

    private enum CheckType
    {

        SOURCE
    }

    private void checkRepos( String repourl, Request r, String version, CheckType ct ) throws IOException
    {
        Document doc = Jsoup.connect( repourl ).get();
        Elements links = doc.select( "a[href]" );
        List<String> source = new LinkedList<String>();
        List<String> central = new LinkedList<String>();
        switch ( ct )
        {
            case SOURCE:
            {
                // http://maven.apache.org/developers/release/maven-project-release-procedure.html#Copy_the_source_release_to_the_Apache_Distribution_Area
                // build source artifact name
                source.add( r.artifactId + "-" + version + "-" + "source-release.zip" );
                source.add( r.artifactId + "-" + version + "-" + "source-release.zip.asc" );
                source.add( r.artifactId + "-" + version + "-" + "source-release.zip.md5" );
            }
            break;
            default:
                getLog().warn( "For future extensions" );

        }

        for ( Element e : links )
        {
            central.add( e.attr( "href" ) );
        }
        source.removeAll( central );
        if ( !source.isEmpty() )
        {
            for ( String sourceItem : source )
            {
                getLog().error( "Missing:" + sourceItem + " in " + repourl );
            }
        }
    }

    private void checkArtifact( Request r, CheckType ct )
    {
        InputStream input = null;
        try
        {
            URL url = new URL( repoBaseUrl + r.getGroupId().replaceAll( "\\.", "/" ) + "/" + r.getArtifactId() + "/maven-metadata.xml" );
            URLConnection conn = url.openConnection();
            input = conn.getInputStream();
            JAXBContext context = JAXBContext.newInstance( MavenMetadata.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MavenMetadata metadata = ( MavenMetadata ) unmarshaller.unmarshal( input );

            getLog().info( "Checking: " + r.getGroupId() + ":" + r.getArtifactId() + " " + metadata.versioning.latest );
            getLog().warn( "all version in central " + metadata.versioning.versions );

// central
            checkRepos( repoBaseUrl + r.getGroupId().replaceAll( "\\.", "/" ) + "/" + r.getArtifactId() + "/" + metadata.versioning.latest, r, metadata.versioning.latest, ct );
            //dist
            checkRepos( r.dist, r, metadata.versioning.latest, ct );
        }
        catch ( MalformedURLException ex )
        {
            Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch ( IOException ex )
        {
            Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, ex );
        }
        catch ( JAXBException ex )
        {
            Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, ex );
        }
        finally
        {
            try
            {
                if ( input != null )
                {
                    input.close();
                }
            }
            catch ( IOException ex )
            {
                Logger.getLogger( DistMojo.class
                        .getName() ).log( Level.SEVERE, null, ex );
            }
        }
    }

    public void execute() throws MojoExecutionException, MojoFailureException
    {
        BufferedReader reader = null;
        try
        {
            if ( db.getName().equals( "mavendb.csv" ) )
            {
                reader = new BufferedReader(
                        new InputStreamReader( Thread.currentThread().getContextClassLoader().getResourceAsStream( "db/mavendb.csv" ) ) );
            }
            else
            {
                reader = new BufferedReader( new FileReader( db ) );
            }

            String text;
            while ( (text = reader.readLine()) != null )
            {
                String[] first = text.split( ";" );
                String[] artifactInfo = first[0].split( ":" );
                requestList.add( new Request( artifactInfo[0], artifactInfo[1], first[1] ) );


            }
        }
        catch ( FileNotFoundException e )
        {
            Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, e );
        }
        catch ( IOException e )
        {
            Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, e );
        }
        finally
        {
            try
            {
                if ( reader != null )
                {
                    reader.close();
                }
            }
            catch ( IOException e )
            {
                Logger.getLogger( DistMojo.class.getName() ).log( Level.SEVERE, null, e );
            }
        }
        for ( Request r : requestList )
        {
            checkArtifact( r, CheckType.SOURCE );
        }


    }

    private static class Request
    {

        private final String groupId;
        private final String artifactId;
        private final String dist;

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
    }
}
