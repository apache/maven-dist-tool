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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.maven.doxia.sink.Sink;
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
@Mojo( name = "check-aggregator", requiresProject = false )
public class DistCheckAggregatorsSiteMojo
        extends AbstractDistCheckMojo
{
    static final String FAILURES_FILENAME = "check-aggregator.log";

    private static final String DIST_AREA = "http://www.apache.org/dist/maven/";
    //private static final String DIST_SVNPUBSUB = "https://dist.apache.org/repos/dist/release/maven/";
    private static final Map<String, Object[]> HARDCODEDAGGREGATEREF;

    static
    {
        Map<String, Object[]> aMap = new HashMap<>();
        aMap.put( "A1", new Object[]
        {
            "http://maven.apache.org/plugins/", "Plugins", 2, 3
        } );
        aMap.put( "A2", new Object[]
        {
            "http://maven.apache.org/shared/", "Shared", 1, 2
        } );
        HARDCODEDAGGREGATEREF = Collections.unmodifiableMap( aMap );
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
        return "dist-tool-checkaggregator";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Disttool> Aggregator Check";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Verification aggregators";
    }

    @Override
    boolean useDetailed()
    {
        return true;
    }

    
    private static class DistCheckAggregatorSite
        extends AbstractCheckResult
    {

        private String versionAggr;
        private String dateAggr;
        
        public DistCheckAggregatorSite( ConfigurationLineInfo r, String version )
        {
            super( r, version );
        }

        private void setAggregatedVersion( String ownText )
        {
            this.versionAggr = ownText;
        }

        private void setAggregatedDate( String ownText )
        {
            this.dateAggr = ownText;
        }
    }
    private final Map<String, List<DistCheckAggregatorSite>> results = new HashMap<>();


    private void reportLine( Sink sink, DistCheckAggregatorSite csr )
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
        if ( csr.getVersion().equals( csr.versionAggr ) )
        {
            iconSuccess( sink );
        }
        else
        {
            iconError( sink );
            sink.rawText( csr.versionAggr );
        }
        sink.tableCell_();

        // DATE column
        sink.tableCell();
        sink.rawText( csr.getConfigurationLine().getReleaseFromMetadata() );
        if ( csr.getConfigurationLine().getReleaseFromMetadata().equals( csr.dateAggr ) )
        {
            iconSuccess( sink );
        }
        else
        {
            iconError( sink );
            sink.rawText( csr.dateAggr );
        }
        sink.tableCell_();
        // central column
        
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

        for ( String key : results.keySet() )
        {
            sink.paragraph();
            sink.text( (String) HARDCODEDAGGREGATEREF.get( key )[1] );
            sink.paragraph_();
            sink.table();
            sink.tableRow();
            sink.tableHeaderCell();
            sink.rawText( "LATEST" );
            sink.tableHeaderCell_();
            sink.tableHeaderCell();
            sink.rawText( "DATE" );
            sink.tableHeaderCell_();
            sink.tableHeaderCell();
            sink.rawText( "VERSION" );
            sink.tableHeaderCell_();
            sink.tableRow_();
            for ( DistCheckAggregatorSite csr : results.get( key ) )
            {
               reportLine( sink, csr );
            }
            sink.table_();
        }
 
        sink.body_();
        sink.flush();
        sink.close();
    }

    private void checkAggregate( ConfigurationLineInfo cli ,  
            DistCheckAggregatorSite r, Object[] inf ) throws IOException
    {
        try
        {
            Document doc = Jsoup.connect( (String) inf[0] ).get();
            Elements a = doc.select( "tr > td > a[href]:not(.externalLink)" );
            for ( Element e : a )
            {
                // skins do not have release date
                String art = e.attr( "href" );
                if ( art.contains( cli.getArtifactId() ) )
                {
                    r.setAggregatedVersion( e.parent().parent().child( ( Integer ) inf[2] ).ownText() );
                    r.setAggregatedDate( e.parent().parent().child( ( Integer ) inf[3] ).ownText() );   
               }
            }
        }
        catch ( IOException ioe )
        {
            throw new IOException( "IOException while reading " + (String) inf[0] , ioe );
        }
    }

    @Override
    protected void checkArtifact( ConfigurationLineInfo configLine, String version )
            throws MojoExecutionException
    {
        try
        {
            DistCheckAggregatorSite result = new DistCheckAggregatorSite( configLine, version );

            if ( configLine.getAggregatedCode() != null )
            {
                if ( results.get( configLine.getAggregatedCode() ) == null )
                {
                    results.put( configLine.getAggregatedCode(), new LinkedList<DistCheckAggregatorSite>() );
                } 
                results.get( configLine.getAggregatedCode() ).add( result );
                checkAggregate( configLine, result, HARDCODEDAGGREGATEREF.get( configLine.getAggregatedCode() ) );
            }
        }
        catch ( IOException ex )
        {
            throw new MojoExecutionException( ex.getMessage(), ex );
        }
    }
}
