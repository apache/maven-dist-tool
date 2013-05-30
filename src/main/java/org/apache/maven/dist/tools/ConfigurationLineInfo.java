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

/**
 *
 * @author skygo
 */
class ConfigurationLineInfo
{

    private final String groupId;
    private final String artifactId;
    private final String dist;
    private static final String URLSEP = "/";

    public ConfigurationLineInfo( String groupId, String artifactId, String dist )
    {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.dist = dist;
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
     * @return the dist
     */
    public String getDist()
    {
        return dist;
    }

    private String getBaseURL( String repoBaseUrl, String folder )
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
}
