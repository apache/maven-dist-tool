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
import java.util.LinkedList;
import java.util.List;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;

/**
 *
 * @author skygo
 */
class ConfigurationLineInfo
{
    private static final String URLSEP = "/";

    private final String directory;
    private final String groupId;
    private final boolean srcBin;

    private final String artifactId;
    private final VersionRange versionRange;
    
    private String forceVersion;
    private String indexPageId;
    private Metadata metadata;

    public ConfigurationLineInfo( String[] infos )
    {
        this.directory = infos[0];
        this.groupId = infos[1];
        this.srcBin = ( infos.length > 2 ) && "src+bin".equals( infos[2] );

        this.artifactId = null;
        this.versionRange = null;
        this.indexPageId = null;
    }

    public ConfigurationLineInfo( ConfigurationLineInfo group, String[] infos ) throws InvalidVersionSpecificationException
    {
        this.directory = group.getDirectory();
        this.groupId = group.getGroupId();
        this.srcBin = group.isSrcBin();

        this.artifactId = infos[0];
        this.versionRange = ( infos.length > 1 && !infos[1].startsWith( "IP" ) ) ? VersionRange.createFromVersionSpec( infos[1] ) : null;
        this.indexPageId = null;
        for ( String info : infos )
        {
            if ( info.startsWith( "IP" ) )
            {
                this.indexPageId = info;
            }
        }
        
    }

    public String getIndexPageId()
    {
        return indexPageId;
    }
    
    public String getForcedVersion()
    {
        return forceVersion;
    }
    
    public void setForceVersion( String forceVersion )
    {
        this.forceVersion = forceVersion;
    }
    
    public VersionRange getVersionRange()
    {
        return versionRange;
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

    public boolean isSrcBin()
    {
        return srcBin;
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

    void setMetadata( Metadata aMetadata )
    {
        this.metadata = aMetadata;
    }

    String getReleaseDateFromMetadata()
    {
        try
        {
            SimpleDateFormat dateFormatter = new SimpleDateFormat( "yyyyMMddkkmmss" );
            Date f = dateFormatter.parse( metadata.getVersioning().getLastUpdated() );
            // inverted for index page check
            SimpleDateFormat dateFormattertarget = new SimpleDateFormat( "yyyy-MM-dd" );
            return dateFormattertarget.format( f );
        }
        catch ( ParseException ex )
        {
            return "Cannot parse";
        }

    }

    String getSourceReleaseFilename( String version, boolean dist )
    {
        return artifactId + "-" + version
            + ( srcBin && ( dist || !"maven-ant-tasks".equals( artifactId ) ) ? "-src" : "-source-release" ) + ".zip";
    }

    List<String> getExpectedFilenames( String version, boolean dist )
    {
        String sourceReleaseFilename = getSourceReleaseFilename( version, dist );

        List<String> expectedFiles = new LinkedList<>();
        expectedFiles.add( sourceReleaseFilename );
        expectedFiles.add( sourceReleaseFilename + ".asc" );
        expectedFiles.add( sourceReleaseFilename + ".md5" );

        return expectedFiles;
    }
}
