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
package org.apache.maven.dist.tools.prerequisites;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

/**
 * <p>PluginPrerequisites class.</p>
 *
 * @author Karl Heinz Marbaise
 */
public class PluginPrerequisites {
    private String pluginName;

    private String pluginVersion;

    private String releaseDate;

    private ArtifactVersion mavenVersion;

    private String jdkVersion;

    /**
     * <p>Constructor for PluginPrerequisites.</p>
     *
     * @param pluginName a {@link java.lang.String} object
     * @param pluginVersion a {@link java.lang.String} object
     * @param releaseDate a {@link java.lang.String} object
     * @param mavenVersion a {@link java.lang.String} object
     * @param jdkVersion a {@link java.lang.String} object
     */
    public PluginPrerequisites(
            String pluginName, String pluginVersion, String releaseDate, String mavenVersion, String jdkVersion) {
        this.pluginName = pluginName;
        this.pluginVersion = pluginVersion;
        this.releaseDate = releaseDate;
        this.mavenVersion = new DefaultArtifactVersion(mavenVersion);
        this.jdkVersion = jdkVersion;
    }

    /**
     * <p>Getter for the field <code>mavenVersion</code>.</p>
     *
     * @return a {@link org.apache.maven.artifact.versioning.ArtifactVersion} object
     */
    public ArtifactVersion getMavenVersion() {
        return mavenVersion;
    }

    /**
     * <p>Setter for the field <code>mavenVersion</code>.</p>
     *
     * @param mavenVersion a {@link java.lang.String} object
     */
    public void setMavenVersion(String mavenVersion) {
        this.mavenVersion = new DefaultArtifactVersion(mavenVersion);
    }

    /**
     * <p>Getter for the field <code>jdkVersion</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getJdkVersion() {
        return jdkVersion;
    }

    /**
     * <p>Setter for the field <code>jdkVersion</code>.</p>
     *
     * @param jdkVersion a {@link java.lang.String} object
     */
    public void setJdkVersion(String jdkVersion) {
        this.jdkVersion = jdkVersion;
    }

    /**
     * <p>Getter for the field <code>pluginName</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPluginName() {
        return pluginName;
    }

    /**
     * <p>Getter for the field <code>pluginVersion</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getPluginVersion() {
        return pluginVersion;
    }

    /**
     * <p>Getter for the field <code>releaseDate</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getReleaseDate() {
        return releaseDate;
    }

    /**
     * <p>Setter for the field <code>pluginName</code>.</p>
     *
     * @param pluginName a {@link java.lang.String} object
     */
    public void setPluginName(String pluginName) {
        this.pluginName = pluginName;
    }
}
