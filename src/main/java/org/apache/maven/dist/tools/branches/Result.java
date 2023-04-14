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
package org.apache.maven.dist.tools.branches;

import java.util.Collection;

/**
 * Represent build result of a Jenkins job for a Git master branch.
 *
 * @author Robert Scholte
 */
public class Result {
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

    /**
     * <p>Constructor for Result.</p>
     *
     * @param repositoryName a {@link java.lang.String} object
     * @param buildUrl a {@link java.lang.String} object
     */
    public Result(String repositoryName, String buildUrl) {
        this.repositoryName = repositoryName;
        this.buildUrl = buildUrl;
    }

    /**
     * <p>Getter for the field <code>masterBranchesGit</code>.</p>
     *
     * @return a int
     */
    public int getMasterBranchesGit() {
        return masterBranchesGit;
    }

    /**
     * <p>Setter for the field <code>masterBranchesGit</code>.</p>
     *
     * @param masterBranches a int
     */
    public void setMasterBranchesGit(int masterBranches) {
        this.masterBranchesGit = masterBranches;
    }

    /**
     * <p>Getter for the field <code>masterBranchesJenkins</code>.</p>
     *
     * @return a int
     */
    public int getMasterBranchesJenkins() {
        return masterBranchesJenkins;
    }

    /**
     * <p>Setter for the field <code>masterBranchesJenkins</code>.</p>
     *
     * @param masterBranchesJenkins a int
     */
    public void setMasterBranchesJenkins(int masterBranchesJenkins) {
        this.masterBranchesJenkins = masterBranchesJenkins;
    }

    /**
     * <p>Getter for the field <code>jiraBranchesGit</code>.</p>
     *
     * @return a {@link java.util.Collection} object
     */
    public Collection<String> getJiraBranchesGit() {
        return jiraBranchesGit;
    }

    /**
     * <p>Setter for the field <code>jiraBranchesGit</code>.</p>
     *
     * @param jiraBranches a {@link java.util.Collection} object
     */
    public void setJiraBranchesGit(Collection<String> jiraBranches) {
        this.jiraBranchesGit = jiraBranches;
    }

    /**
     * <p>Getter for the field <code>jiraBranchesJenkins</code>.</p>
     *
     * @return a {@link java.util.Collection} object
     */
    public Collection<String> getJiraBranchesJenkins() {
        return jiraBranchesJenkins;
    }

    /**
     * <p>Setter for the field <code>jiraBranchesJenkins</code>.</p>
     *
     * @param jiraBranchesJenkins a {@link java.util.Collection} object
     */
    public void setJiraBranchesJenkins(Collection<String> jiraBranchesJenkins) {
        this.jiraBranchesJenkins = jiraBranchesJenkins;
    }

    /**
     * <p>Getter for the field <code>dependabotBranchesGit</code>.</p>
     *
     * @return a {@link java.util.Collection} object
     */
    public Collection<String> getDependabotBranchesGit() {
        return dependabotBranchesGit;
    }

    /**
     * <p>Setter for the field <code>dependabotBranchesGit</code>.</p>
     *
     * @param dependabotBranches a {@link java.util.Collection} object
     */
    public void setDependabotBranchesGit(Collection<String> dependabotBranches) {
        this.dependabotBranchesGit = dependabotBranches;
    }

    /**
     * <p>Getter for the field <code>dependabotBranchesJenkins</code>.</p>
     *
     * @return a {@link java.util.Collection} object
     */
    public Collection<String> getDependabotBranchesJenkins() {
        return dependabotBranchesJenkins;
    }

    /**
     * <p>Setter for the field <code>dependabotBranchesJenkins</code>.</p>
     *
     * @param dependabotBranchesJenkins a {@link java.util.Collection} object
     */
    public void setDependabotBranchesJenkins(Collection<String> dependabotBranchesJenkins) {
        this.dependabotBranchesJenkins = dependabotBranchesJenkins;
    }

    /**
     * <p>Getter for the field <code>restGit</code>.</p>
     *
     * @return a {@link java.util.Collection} object
     */
    public Collection<String> getRestGit() {
        return restGit;
    }

    /**
     * <p>Setter for the field <code>restGit</code>.</p>
     *
     * @param rest a {@link java.util.Collection} object
     */
    public void setRestGit(Collection<String> rest) {
        this.restGit = rest;
    }

    /**
     * <p>Getter for the field <code>restJenkins</code>.</p>
     *
     * @return a {@link java.util.Collection} object
     */
    public Collection<String> getRestJenkins() {
        return restJenkins;
    }

    /**
     * <p>Setter for the field <code>restJenkins</code>.</p>
     *
     * @param restJenkins a {@link java.util.Collection} object
     */
    public void setRestJenkins(Collection<String> restJenkins) {
        this.restJenkins = restJenkins;
    }

    /**
     * <p>Getter for the field <code>repositoryName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getRepositoryName() {
        return repositoryName;
    }

    /**
     * <p>Getter for the field <code>buildUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getBuildUrl() {
        return buildUrl;
    }

    /**
     * <p>getTotalGit.</p>
     *
     * @return a int
     */
    public final int getTotalGit() {
        return masterBranchesGit + jiraBranchesGit.size() + dependabotBranchesGit.size() + restGit.size();
    }

    /**
     * <p>getTotalJenkins.</p>
     *
     * @return a int
     */
    public final int getTotalJenkins() {
        return masterBranchesJenkins
                + jiraBranchesJenkins.size()
                + dependabotBranchesJenkins.size()
                + restJenkins.size();
    }
}
