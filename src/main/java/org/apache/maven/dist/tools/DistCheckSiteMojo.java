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
import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.project.MavenProject;
import org.codehaus.plexus.util.xml.pull.XmlPullParserException;
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

    interface HTMLChecker
    {

        /**
         * name of the checker.
         *
         * @return
         */
        String getName();

        /**
         * true if checker find patter in document
         *
         * @param doc
         * @param version
         * @return
         */
        boolean isOk( Document doc, String version );
    }
    private List<HTMLChecker> checker = new LinkedList<>();

    private void checkSite( String repourl, ConfigurationLineInfo r, String version ) throws MojoExecutionException
    {
        StringBuilder message = new StringBuilder();
        try (BufferedReader input = new BufferedReader( new InputStreamReader( new URL( repourl ).openStream() ) ))
        {
            MavenXpp3Reader mavenreader = new MavenXpp3Reader();
            Model m = mavenreader.read( input );
            // need to have parent information and resolve properties 
            // 
            MavenProject project = new MavenProject( m );


            Document doc = Jsoup.connect( project.getUrl() ).get();
            message.append( "Site for " ).append( project.getArtifactId() ).append( " at " ).append( project.getUrl() ).append( " seek for" ).append( project.getVersion() ).append( "    " );
            for ( HTMLChecker c : checker )
            {
                message.append( "[" ).append( c.getName() ).append( c.isOk( doc, version ) ).append( "]" );
            }


            getLog().warn( message.toString() );

        }
        catch ( MalformedURLException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
        catch ( IOException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
        catch ( XmlPullParserException ex )
        {
            Logger.getLogger( DistCheckSiteMojo.class.getName() ).log( Level.SEVERE, null, ex );
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
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        //add  html checker
        checker.add( new HTMLChecker()
        {
            @Override
            public String getName()
            {
                return "Stylus Skin:";
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
                return "Fluido Skin:";
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
}
