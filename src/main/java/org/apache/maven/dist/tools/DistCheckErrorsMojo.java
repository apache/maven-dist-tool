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
import java.io.IOException;
import java.util.Locale;

import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.FileUtils;

/**
 *
 * @author skygo
 */
@Mojo( name = "check-errors", requiresProject = false )
public class DistCheckErrorsMojo
    extends AbstractDistCheckMojo
{
    private static final String[] FAILURES_FILENAMES = { DistCheckSourceReleaseMojo.FAILURES_FILENAME,
        DistCheckSiteMojo.FAILURES_FILENAME, DistCheckIndexPageMojo.FAILURES_FILENAME };

    private static final String EOL = System.getProperty( "line.separator" );

    @Override
    boolean isIndexPageCheck()
    {
        return false;
    }

    boolean isDummyFailure()
    {
        return false;
    }

    private boolean checkError( String failuresFilename )
        throws MavenReportException
    {
        File failureFile = new File( failuresDirectory, failuresFilename );

        try
        {
            if ( failureFile.exists() )
            {
                String content = FileUtils.fileRead( failureFile );

                if ( isDummyFailure() )
                {
                    getLog().error( failuresFilename + " error log not empty:" + EOL + content );
                }
                else
                {
                    String failure = failuresFilename.substring( 0, failuresFilename.length() - 4 );
                    getSink().section2();
                    getSink().sectionTitle2();
                    getSink().link( "dist-tool-" + failure + ".html" );
                    getSink().text( failure );
                    getSink().link_();
                    getSink().sectionTitle2_();
                    getSink().verbatim( true );
                    getSink().rawText( content );
                    getSink().verbatim_();
                    getSink().section2_();
                }
            }

            return failureFile.exists();
        }
        catch ( IOException ioe )
        {
            throw new MavenReportException( "Cannot read " + failureFile, ioe );
        }
    }

    @Override
    protected void executeReport( Locale locale )
        throws MavenReportException
    {
        boolean failure = false;
        // if failures log file is present, throw exception to fail build
        for ( String failuresFilename : FAILURES_FILENAMES )
        {
            failure |= checkError( failuresFilename );
        }

        if ( failure )
        {
            if ( isDummyFailure() )
            {
                throw new MavenReportException( "Dist Tool> Checks found inconsistencies in some released "
                    + "artifacts, see https://builds.apache.org/job/dist-tool-plugin/site/dist-tool-check-errors.html "
                    + "for more information" );
            }
        }
        else
        {
            getSink().paragraph();
            getSink().text( "No issue found." );
            getSink().paragraph_();
        }
    }

    protected String getFailuresFilename()
    {
        return "dummy";
    }

    @Override
    public String getOutputName()
    {
        return "dist-tool-check-errors";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Dist Tool> Check Errors";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Dist Tool report to display inconsistencies found by any check report";
    }

    @Override
    protected void checkArtifact( ConfigurationLineInfo request, String repoBase )
        throws MojoExecutionException
    {
    }
}
