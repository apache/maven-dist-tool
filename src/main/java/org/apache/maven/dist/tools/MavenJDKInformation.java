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

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * @author Karl Heinz Marbaise
 *
 */
public class MavenJDKInformation
{
    private String pluginName;

    private String pluginVersion;

    private ArtifactVersion mavenVersion;

    private String jdkVersion;

    public MavenJDKInformation( String pluginName, String pluginVersion, String mavenVersion, String jdkVersion )
    {
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.mavenVersion = new DefaultArtifactVersion( mavenVersion );
        this.jdkVersion = jdkVersion;
    }

    public ArtifactVersion getMavenVersion()
    {
        return mavenVersion;
    }

    public void setMavenVersion( String mavenVersion )
    {
        this.mavenVersion = new DefaultArtifactVersion( mavenVersion );
    }

    public String getJdkVersion()
    {
        return jdkVersion;
    }

    public void setJdkVersion( String jdkVersion )
    {
        this.jdkVersion = jdkVersion;
    }

    public String getPluginName()
    {
        return pluginName;
    }

    public String getPluginVersion()
    {
        return pluginVersion;
    }

    public void setPluginName( String pluginName )
    {
        this.pluginName = pluginName;
    }

}
