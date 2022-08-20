package org.apache.maven.dist.tools.masterjobs;

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

import java.time.ZonedDateTime;

/**
 * Represent build result of a Jenkins job for a Git master branch.
 *
 * @author Robert Scholte
 */
public class Result
{
    private String repositoryName;

    private String status;

    private String buildUrl;

    private String icon;
    
    private ZonedDateTime lastBuild;

    /**
     * <p>Constructor for Result.</p>
     *
     * @param repositoryName a {@link java.lang.String} object
     * @param buildUrl a {@link java.lang.String} object
     */
    public Result( String repositoryName, String buildUrl )
    {
        this.repositoryName = repositoryName;
        this.buildUrl = buildUrl;
    }

    /**
     * <p>Setter for the field <code>status</code>.</p>
     *
     * @param status a {@link java.lang.String} object
     */
    public void setStatus( String status )
    {
        this.status = status;
    }

    /**
     * <p>Setter for the field <code>icon</code>.</p>
     *
     * @param icon a {@link java.lang.String} object
     */
    public void setIcon( String icon )
    {
        this.icon = icon;
    }

    /**
     * <p>Setter for the field <code>lastBuild</code>.</p>
     *
     * @param lastBuild a {@link java.time.ZonedDateTime} object
     */
    public void setLastBuild( ZonedDateTime lastBuild )
    {
        this.lastBuild = lastBuild;
    }
    
    /**
     * <p>Getter for the field <code>lastBuild</code>.</p>
     *
     * @return a {@link java.time.ZonedDateTime} object
     */
    public ZonedDateTime getLastBuild()
    {
        return lastBuild;
    }
    
    /**
     * <p>Getter for the field <code>repositoryName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRepositoryName()
    {
        return repositoryName;
    }

    /**
     * <p>Getter for the field <code>status</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getStatus()
    {
        return status;
    }

    /**
     * <p>Getter for the field <code>buildUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBuildUrl()
    {
        return buildUrl;
    }

    /**
     * <p>Getter for the field <code>icon</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIcon()
    {
        return icon;
    } 
}
