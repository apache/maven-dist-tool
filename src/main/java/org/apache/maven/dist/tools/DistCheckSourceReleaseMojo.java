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
import java.util.Locale;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import org.apache.maven.doxia.sink.Sink;
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
@Mojo( name = "check-source-release" )
public class DistCheckSourceReleaseMojo extends AbstractDistCheckMojo
{
//Artifact metadata retrieval done y hands.

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

    class DistCheckSourceRelease extends AbstractCheckResult
    {

        private List<String> central;
        private List<String> dist;

        public DistCheckSourceRelease( ConfigurationLineInfo r, String version )
        {
            super( r, version );
        }

        private void setMissingDistSourceRelease( List<String> checkRepos )
        {
            dist = checkRepos;
        }

        private void setMisingCentralSourceRelease( List<String> checkRepos )
        {
            central = checkRepos;
        }
    }
    private List<DistCheckSourceRelease> results = new LinkedList<>();

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
        sink.text( "Check source release" );
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.rawText( "Missing Source Release (less you have better the result)" );
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
        sink.rawText( "Central " + repoBaseUrl );
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText( "Dist" );
        sink.tableHeaderCell_();
        sink.tableRow_();
        for ( DistCheckSourceRelease csr : results )
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
            sink.bold();
            sink.lineBreak();
            for ( String missing : csr.central )
            {
                sink.rawText( missing );
                sink.lineBreak();
            }

            sink.bold_();
            sink.tableCell_();
            sink.tableCell();
            sink.bold();
            sink.lineBreak();
            for ( String missing : csr.dist )
            {
                sink.rawText( missing );
                sink.lineBreak();
            }

            sink.bold_();
            sink.tableCell_();

            sink.tableRow_();
        }
        sink.table_();
        sink.body_();
        sink.flush();
        sink.close();
    }

    private List<String> checkRepos( String repourl, ConfigurationLineInfo r, String version ) throws IOException
    {
        Document doc = Jsoup.connect( repourl ).get();
        Elements links = doc.select( "a[href]" );
        List<String> expectedFile = new LinkedList<>();
        List<String> retrievedFile = new LinkedList<>();
        // http://maven.apache.org/developers/release/maven-project-release-procedure.html#Copy_the_source_release_to_the_Apache_Distribution_Area
        // build source artifact name
        expectedFile.add( r.getArtifactId() + "-" + version + "-" + "source-release.zip" );
        expectedFile.add( r.getArtifactId() + "-" + version + "-" + "source-release.zip.asc" );
        expectedFile.add( r.getArtifactId() + "-" + version + "-" + "source-release.zip.md5" );



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
        return expectedFile;
    }

    @Override
    void checkArtifact( ConfigurationLineInfo r, String repoBaseUrl ) throws MojoExecutionException
    {
        try ( BufferedReader input = new BufferedReader( new InputStreamReader( new URL( r.getMetadataFileURL( repoBaseUrl ) ).openStream() ) ) )
        {
            JAXBContext context = JAXBContext.newInstance( MavenMetadata.class );
            Unmarshaller unmarshaller = context.createUnmarshaller();
            MavenMetadata metadata = ( MavenMetadata ) unmarshaller.unmarshal( input );

            getLog().info( "Checking for artifact : " + r.getGroupId() + ":" + r.getArtifactId() + ":" + metadata.versioning.latest );
            // revert sort versions (not handling alpha and complex vesion scheme but more usefull version are displayed left side
            Collections.sort( metadata.versioning.versions, Collections.reverseOrder() );
            getLog().warn( metadata.versioning.versions + " version(s) detected " + repoBaseUrl );
            DistCheckSourceRelease result = new DistCheckSourceRelease( r, metadata.versioning.latest );
            results.add( result );
            // central
            result.setMisingCentralSourceRelease( checkRepos( r.getVersionnedFolderURL( repoBaseUrl, metadata.versioning.latest ), r, metadata.versioning.latest ) );
            //dist
            result.setMissingDistSourceRelease( checkRepos( r.getDist(), r, metadata.versioning.latest ) );
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
}
