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
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.reporting.AbstractMavenReport;

/**
 *
 * @author skygo
 */
public abstract class AbstractDistCheckMojo extends AbstractMavenReport
{
    private static final String MAVEN_DB = "db/mavendb.csv";

    @Parameter( property = "repositoryUrl", defaultValue = "http://repo.maven.apache.org/maven2/" )
    protected String repoBaseUrl;

    @Parameter( property = "configurationLines", defaultValue = "" )
    private List<String> configurationLines;

    @Component
    protected Renderer siteRenderer;

    @Parameter( property = "project.reporting.outputDirectory", required = true )
    protected File outputDirectory;

    @Component
    protected MavenProject project;

    abstract void checkArtifact( ConfigurationLineInfo request, String repoBase ) throws MojoExecutionException;

    @Override
    protected String getOutputDirectory()
    {
        return outputDirectory.getAbsolutePath();
    }

    @Override
    protected Renderer getSiteRenderer()
    {
        return siteRenderer;
    }

    @Override
    protected MavenProject getProject()
    {
        return project;
    }

    @Override
    public void execute() throws MojoExecutionException
    {
        if ( configurationLines.isEmpty() )
        {
            try ( BufferedReader input = new BufferedReader( new InputStreamReader( Thread.currentThread().getContextClassLoader().getResource( MAVEN_DB ).openStream() ) ) )
            {
                String text;
                while ( ( text = input.readLine() ) != null )
                {
                    configurationLines.add( text );
                }
            }
            catch ( IOException ex )
            {
                throw new MojoExecutionException( ex.getMessage(), ex );
            }
        }

        for ( String line : configurationLines )
        {
            if ( line.startsWith( "##" ) )
            {
                getLog().info( line );
            }
            else
            {
                String[] artifactInfo = line.split( ";" );
                checkArtifact( new ConfigurationLineInfo( artifactInfo[0], artifactInfo[1], artifactInfo[2] ), repoBaseUrl );
            }
        }
    }

    protected void iconError( Sink sink )
    {
        icon( sink, "error" );
    }

    protected void iconWarning( Sink sink )
    {
        icon( sink, "warning" );
    }

    protected void iconSuccess( Sink sink )
    {
        icon( sink, "success" );
    }

    protected void icon( Sink sink, String level )
    {
        sink.figure();
        sink.figureCaption();
        sink.text( level );
        sink.figureCaption_();
        sink.figureGraphics( "images/icon_" + level + "_sml.gif" );
        sink.figure_();
    }
}
