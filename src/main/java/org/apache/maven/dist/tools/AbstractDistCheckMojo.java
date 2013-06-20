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
import java.util.LinkedList;
import java.util.List;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.artifact.repository.ArtifactRepositoryPolicy;
import org.apache.maven.artifact.repository.MavenArtifactRepository;
import org.apache.maven.artifact.repository.layout.DefaultRepositoryLayout;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.siterenderer.Renderer;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.reporting.AbstractMavenReport;

/**
 *
 * @author skygo
 */
public abstract class AbstractDistCheckMojo extends AbstractMavenReport
{
    private static final String MAVEN_DB = "db/mavendb.csv";

    /**
     * URL of repository where artifacts are stored. 
     */
    @Parameter( property = "repositoryUrl", defaultValue = "http://repo.maven.apache.org/maven2/" )
    protected String repoBaseUrl;

    /**
     * List of configuration line for specific inspection.
     * groupId:artifactId:distributionurl.
     *
     */
    @Parameter( property = "configurationLines", defaultValue = "" )
    private List<String> configurationLines;

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

    /**
     * Local repository.
     */
    @Parameter( defaultValue = "${localRepository}", required = true, readonly = true )
    protected ArtifactRepository localRepository;

    /**
     * Artifact factory.
     */
    @Component
    protected ArtifactFactory artifactFactory;

    /**
     * Maven project builder.
     */
    @Component
    protected MavenProjectBuilder mavenProjectBuilder;
    
    /**
     * list of artifacts repositories.
     */
    protected List<ArtifactRepository> artifactRepositories = new LinkedList<>();
    
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
        ArtifactRepository aa = new MavenArtifactRepository( "central",
                repoBaseUrl,
                new DefaultRepositoryLayout(),
                new ArtifactRepositoryPolicy(
                false,
                ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN ),
                new ArtifactRepositoryPolicy(
                true,
                ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS, ArtifactRepositoryPolicy.CHECKSUM_POLICY_WARN ) );
        artifactRepositories.add( aa );
        if ( configurationLines.isEmpty() )
        {
            try ( BufferedReader input = new BufferedReader( 
                    new InputStreamReader( 
                    Thread.currentThread().getContextClassLoader().getResource( MAVEN_DB ).openStream() ) ) )
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
                checkArtifact(
                        new ConfigurationLineInfo( artifactInfo[0], artifactInfo[1], artifactInfo[2] ), repoBaseUrl );
            }
        }
    }

    /**
     * add an error icon.
     *
     * @param sink doxiasink
     */
    protected void iconError( Sink sink )
    {
        icon( sink, "error" );
    }
    
    /**
     * add a warning icon.
     *
     * @param sink doxiasink
     */
    protected void iconWarning( Sink sink )
    {
        icon( sink, "warning" );
    }
    
    /**
     * add an succses icon.
     *
     * @param sink doxiasink
     */
    protected void iconSuccess( Sink sink )
    {
        icon( sink, "success" );
    }

    private void icon( Sink sink, String level )
    {
        sink.figure();
        sink.figureCaption();
        sink.text( level );
        sink.figureCaption_();
        sink.figureGraphics( "images/icon_" + level + "_sml.gif" );
        sink.figure_();
    }
}
