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
import java.util.Locale;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.MavenReportException;

/**
 *
 * @author skygo
 */
@Mojo( name = "failure-report", requiresProject = false )
public class DummyFailureMojo extends AbstractDistCheckMojo
{
    /**
     * Site renderer.
     */
    @Component
    protected Renderer siteRenderer;

    /**
     * Reporting directory.
     */
    @Parameter( defaultValue = "target/site", required = true )
    protected File outputDirectory;

    /**
     * Maven project.
     */
    @Component
    protected MavenProject project;
    
    @Override
    public void execute() throws MojoExecutionException
    {
        // if logs.txt is present throw exception to fail build
        if ( new File( "target", "logs.txt" ).exists() )
        {
            throw new MojoExecutionException( "Dist tools report non empty please check: "
                    + " https://builds.apache.org/job/dist-tool-plugin/site/" );
        }
    }

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
            throw new MavenReportException( ex.getMessage() );
        }

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
    void checkArtifact( ConfigurationLineInfo request, String repoBase ) throws MojoExecutionException
    {
    }
  
}
