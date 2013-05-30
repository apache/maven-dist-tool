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
import java.net.URL;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Parameter;

/**
 *
 * @author skygo
 */
public abstract class AbstractDistCheckMojo extends AbstractMojo
{

    @Parameter( property = "repository.url", defaultValue = "http://repo1.maven.org/maven2/" )
    private String repoBaseUrl;
    @Parameter( property = "database.url", defaultValue = "db/mavendb.csv" )
    private String dbLocation;

    abstract void checkArtifact( ConfigurationLineInfo request, String repoBase ) throws MojoExecutionException;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        URL dbURL;
        if ( dbLocation.equals( "db/mavendb.csv" ) )
        {
            dbURL = Thread.currentThread().getContextClassLoader().getResource( "db/mavendb.csv" );
        }
        else
        {
            throw new MojoFailureException( "Custom data not implemented " );
        }


        try (BufferedReader input = new BufferedReader( new InputStreamReader( dbURL.openStream() ) ))
        {
            String text;
            while ( (text = input.readLine()) != null )
            {
                if ( text.startsWith( "##" ) )
                {
                    getLog().info( text );
                }
                else
                {
                    String[] artifactInfo = text.split( ";" );
                    checkArtifact( new ConfigurationLineInfo( artifactInfo[0], artifactInfo[1], artifactInfo[2] ), repoBaseUrl );
                }

            }
        }
        catch ( IOException ex )
        {
            throw new MojoFailureException( ex.getMessage(), ex );
        }
    }
}
