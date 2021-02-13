package org.apache.maven.dist.tools.branches;

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
 * Represent build result of a Jenkins job for a Git master branch.
 * 
 * @author Robert Scholte
 */
public class Result
{
    private final String repositoryName;

    private final String buildUrl;
    
    private int masterBranchesGit;
    private int masterBranchesJenkins;
    
    private int jiraBranchesGit;
    private int jiraBranchesJenkins;
    
    private int dependabotBranchesGit;
    private int dependabotBranchesJenkins;
    
    private int restGit;
    private int restJenkins;
    
    public Result( String repositoryName, String buildUrl )
    {
        this.repositoryName = repositoryName;
        this.buildUrl = buildUrl;
    }

    public int getMasterBranchesGit()
    {
        return masterBranchesGit;
    }

    public void setMasterBranchesGit( int masterBranches )
    {
        this.masterBranchesGit = masterBranches;
    }

    public int getMasterBranchesJenkins()
    {
        return masterBranchesJenkins;
    }
    
    public void setMasterBranchesJenkins( int masterBranchesJenkins )
    {
        this.masterBranchesJenkins = masterBranchesJenkins;
    }

    public int getJiraBranchesGit()
    {
        return jiraBranchesGit;
    }

    public void setJiraBranchesGit( int jiraBranches )
    {
        this.jiraBranchesGit = jiraBranches;
    }

    public int getJiraBranchesJenkins()
    {
        return jiraBranchesJenkins;
    }
    
    public void setJiraBranchesJenkins( int jiraBranchesJenkins )
    {
        this.jiraBranchesJenkins = jiraBranchesJenkins;
    }
    
    public int getDependabotBranchesGit()
    {
        return dependabotBranchesGit;
    }

    public void setDependabotBranchesGit( int dependabotBranches )
    {
        this.dependabotBranchesGit = dependabotBranches;
    }
    
    public int getDependabotBranchesJenkins()
    {
        return dependabotBranchesJenkins;
    }
    
    public void setDependabotBranchesJenkins( int dependabotBranchesJenkins )
    {
        this.dependabotBranchesJenkins = dependabotBranchesJenkins;
    }

    public int getRestGit()
    {
        return restGit;
    }

    public void setRestGit( int rest )
    {
        this.restGit = rest;
    }
    
    public int getRestJenkins()
    {
        return restJenkins;
    }
    
    public void setRestJenkins( int restJenkins )
    {
        this.restJenkins = restJenkins;
    }

    public String getRepositoryName()
    {
        return repositoryName;
    }

    public String getBuildUrl()
    {
        return buildUrl;
    }
    
    public final int getTotalGit()
    {
        return masterBranchesGit + jiraBranchesGit + dependabotBranchesGit + restGit;
    }
    
    public final int getTotalJenkins()
    {
        return masterBranchesJenkins + jiraBranchesJenkins + dependabotBranchesJenkins + restJenkins;
    }
 
}
