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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.maven.artifact.repository.metadata.Metadata;

/**
 *
 * @author skygo
 */
class ConfigurationLineInfo
{
    private static final String URLSEP = "/";

    private final String groupId;
    private final String directory;

    private final String artifactId;
    private final String forceVersion;

    private Metadata metadata;

    public ConfigurationLineInfo( String[] infos )
    {
        this.directory = infos[0];
        this.groupId = infos[1];

        this.artifactId = null;
        this.forceVersion = null;
    }

    public ConfigurationLineInfo( ConfigurationLineInfo group, String[] infos )
    {
        this.directory = group.getDirectory();
        this.groupId = group.getGroupId();

        this.artifactId = infos[0];
        this.forceVersion = ( infos.length > 1 ) ? infos[1] : null;
    }

    public String getForcedVersion()
    {
        return forceVersion;
    }

    /**
     * @return the groupId
     */
    public String getGroupId()
    {
        return groupId;
    }

    /**
     * @return the artifactId
     */
    public String getArtifactId()
    {
        return artifactId;
    }

    /**
     * @return the directory
     */
    public String getDirectory()
    {
        return directory;
    }

    String getBaseURL( String repoBaseUrl, String folder )
    {
        return repoBaseUrl + groupId.replaceAll( "\\.", URLSEP ) + URLSEP + artifactId + URLSEP + folder;
    }

    String getMetadataFileURL( String repoBaseUrl )
    {
        return getBaseURL( repoBaseUrl, "maven-metadata.xml" );
    }

    String getVersionnedFolderURL( String repoBaseUrl, String version )
    {
        return getBaseURL( repoBaseUrl, version );
    }

    String getVersionnedPomFileURL( String repoBaseUrl, String version )
    {
        return getBaseURL( repoBaseUrl, version + URLSEP + artifactId + "-" + version + ".pom" );
    }

    void addMetadata( Metadata aMetadata )
    {
        this.metadata = aMetadata;
    }

    String getReleaseFromMetadata()
    {

        try
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyyMMddkkmmss" );
            Date f = dateFormatter.parse( metadata.getVersioning().getLastUpdated() );
            SimpleDateFormat dateFormattertarget = new SimpleDateFormat( "MMM dd, yyyy" );
            return dateFormattertarget.format( f );
        }
        catch ( ParseException ex )
        {
            return "Cannot parse";
        }

    }
}
