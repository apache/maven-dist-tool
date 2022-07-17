package org.apache.maven.dist.tools.pgp;

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
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;
import java.net.URL;
import java.util.Iterator;
import java.util.Locale;

import org.apache.commons.io.IOUtils;
import org.apache.maven.dist.tools.AbstractDistCheckReport;
import org.apache.maven.dist.tools.ConfigurationLineInfo;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;

import static org.apache.maven.doxia.sink.impl.SinkEventAttributeSet.Semantics.BOLD;

/**
 * Check PGP KEYS files.
 */
@Mojo( name = "check-pgp-keys", requiresProject = false )
public class CheckPgpKeysReport
        extends AbstractDistCheckReport
{
    public static final String FAILURES_FILENAME = "check-pgp-keys.log";

    public static final String PROJECT_KEYS_URL = "https://svn.apache.org/repos/asf/maven/project/KEYS";

    public static final String DIST_KEYS_URL = "https://dist.apache.org/repos/dist/release/maven/KEYS";

    @Override
    protected String getFailuresFilename()
    {
        return FAILURES_FILENAME;
    }

    @Override
    public String getName( Locale locale )
    {
        return "Dist Tool> Check PGP KEYS";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Verification of PGP KEYS files";
    }

    @Override
    protected boolean isIndexPageCheck()
    {
        return false;
    }

    @Override
    protected void executeReport( Locale locale )
            throws MavenReportException
    {
        String projectKeys = fetchUrl( PROJECT_KEYS_URL );
        String distKeys = fetchUrl( DIST_KEYS_URL );

        if ( !projectKeys.equals( distKeys ) )
        {
            File failure = new File( failuresDirectory, FAILURES_FILENAME );
            try ( PrintWriter output = new PrintWriter( new FileWriter( failure ) ) )
            {
                output.println( "PGP KEYS files content is different: " + DIST_KEYS_URL + " vs " + PROJECT_KEYS_URL );
            }
            catch ( Exception e )
            {
                getLog().error( "Cannot append to " + getFailuresFilename() );
            }
        }

        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text( "Check PGP KEYS files" );
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.paragraph();
        sink.rawText( "Check that:" );
        sink.paragraph_();
        sink.list();
        sink.listItem();
        sink.rawText( "official Maven PGP KEYS file from distribution area (<b>PMC write only</b>) " );
        sink.link( DIST_KEYS_URL );
        sink.rawText( DIST_KEYS_URL );
        sink.link_();
        sink.listItem_();
        sink.listItem();
        sink.rawText( "intermediate <b>committer write</b> one in Maven Subversion tree " );
        sink.link( PROJECT_KEYS_URL );
        sink.rawText( PROJECT_KEYS_URL );
        sink.link_();
        sink.listItem_();
        sink.list_();
        sink.paragraph();
        sink.rawText( "Committers are supposed to write to project's KEYS then ask PMC for sync, but sometimes PMC"
            + " members directly add in distribution area, then future sync is not trivial any more." );
        sink.paragraph_();
        sink.paragraph();
        sink.rawText( "match: " );
        if ( projectKeys.equals( distKeys ) )
        {
            iconSuccess( sink );
        }
        else
        {
            iconError( sink );
        }
        sink.paragraph_();

        sink.numberedList( 0 );
        KeysIterator distIterator = new KeysIterator( distKeys );
        KeysIterator projectIterator = new KeysIterator( projectKeys );
        while ( distIterator.hasNext() || projectIterator.hasNext() )
        {
            String distKey = distIterator.hasNext() ? distIterator.next() : "";
            String projectKey = projectIterator.hasNext() ? projectIterator.next() : "";

            sink.numberedListItem();
            sink.verbatim( BOLD );
            sink.rawText( distKey );
            sink.verbatim_();
            if ( !projectKey.equals( distKey ) )
            {
                sink.rawText( "dist target (PMC) " );
                iconError( sink );
                sink.rawText( " project (committers)" );

                sink.verbatim( BOLD );
                sink.rawText( projectKey );
                sink.verbatim_();
            }
            sink.numberedListItem_();
        }
        sink.numberedList_();

        sink.section1_();
        sink.body_();
        sink.close();
    }

    @Override
    protected void checkArtifact( ConfigurationLineInfo request, String repoBase )
        throws MojoExecutionException
    {
    }

    private String fetchUrl( String url )
        throws MavenReportException
    {
        try ( InputStream in = new URL( url ).openStream();
              Reader reader = new InputStreamReader( in, "UTF-8" );
              StringWriter writer = new StringWriter() )
        {
            IOUtils.copy( reader, writer );
            return writer.toString();
        }
        catch ( IOException ioe )
        {
            throw new MavenReportException( "cannot fetch " + url, ioe );
        }
    }

    private static class KeysIterator
        implements Iterator<String>
    {
        private static final String BEGIN = "-----BEGIN PGP PUBLIC KEY BLOCK-----";
        private static final String END = "-----END PGP PUBLIC KEY BLOCK-----";

        private String content;

        KeysIterator( String content )
        {
            this.content = content.substring( content.indexOf( "---" ) + 3 );
        }

        @Override
        public boolean hasNext()
        {
            return content.length() > 0;
        }

        @Override
        public String next()
        {
            String id = content.substring( 0, content.indexOf( BEGIN ) ).trim();
            content = content.substring( content.indexOf( END ) + END.length() ).trim();
            return id;
        }
    }
}
