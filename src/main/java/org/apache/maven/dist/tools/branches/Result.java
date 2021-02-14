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

import java.util.Collection;

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
    
    private Collection<String> jiraBranchesGit;
    private Collection<String> jiraBranchesJenkins;
    
    private Collection<String> dependabotBranchesGit;
    private Collection<String> dependabotBranchesJenkins;
    
    private Collection<String> restGit;
    private Collection<String> restJenkins;
    
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

    public Collection<String> getJiraBranchesGit()
    {
        return jiraBranchesGit;
    }

    public void setJiraBranchesGit( Collection<String> jiraBranches )
    {
        this.jiraBranchesGit = jiraBranches;
    }

    public Collection<String> getJiraBranchesJenkins()
    {
        return jiraBranchesJenkins;
    }
    
    public void setJiraBranchesJenkins( Collection<String> jiraBranchesJenkins )
    {
        this.jiraBranchesJenkins = jiraBranchesJenkins;
    }
    
    public Collection<String> getDependabotBranchesGit()
    {
        return dependabotBranchesGit;
    }

    public void setDependabotBranchesGit( Collection<String> dependabotBranches )
    {
        this.dependabotBranchesGit = dependabotBranches;
    }
    
    public Collection<String> getDependabotBranchesJenkins()
    {
        return dependabotBranchesJenkins;
    }
    
    public void setDependabotBranchesJenkins( Collection<String> dependabotBranchesJenkins )
    {
        this.dependabotBranchesJenkins = dependabotBranchesJenkins;
    }

    public Collection<String> getRestGit()
    {
        return restGit;
    }

    public void setRestGit( Collection<String> rest )
    {
        this.restGit = rest;
    }
    
    public Collection<String> getRestJenkins()
    {
        return restJenkins;
    }
    
    public void setRestJenkins( Collection<String> restJenkins )
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
        return masterBranchesGit + jiraBranchesGit.size() + dependabotBranchesGit.size() + restGit.size();
    }
    
    public final int getTotalJenkins()
    {
        return masterBranchesJenkins + jiraBranchesJenkins.size() + dependabotBranchesJenkins.size()
            + restJenkins.size();
    }
 
}
