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
@Mojo( name = "failure-report", requiresProject = false )
public class DummyFailureMojo
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

    private boolean checkFailure( String failuresFilename )
        throws MavenReportException
    {
        File failureFile = new File( failuresDirectory, failuresFilename );

        try
        {
            if ( failureFile.exists() )
            {
                getLog().error( failuresFilename + " error log not empty:" + EOL + FileUtils.fileRead( failureFile ) );
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
            failure |= checkFailure( failuresFilename );
        }

        if ( failure )
        {
            throw new MavenReportException( "Dist tools check reports found inconsistencies in some released "
                + "artifacts, see https://builds.apache.org/job/dist-tool-plugin/site/ for more information" );
        }
    }

    protected String getFailuresFilename()
    {
        return "dummy";
    }

    @Override
    public String getOutputName()
    {
        return "dist-tool-failure";
    }

    @Override
    public String getName( Locale locale )
    {
        return "Failure Hack";
    }

    @Override
    public String getDescription( Locale locale )
    {
        return "Failure Hack";
    }

    @Override
    protected void checkArtifact( ConfigurationLineInfo request, String repoBase )
        throws MojoExecutionException
    {
    }
}
