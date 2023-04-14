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
package org.apache.maven.dist.tools;

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

import org.apache.maven.artifact.repository.metadata.Metadata;
import org.apache.maven.artifact.versioning.InvalidVersionSpecificationException;
import org.apache.maven.artifact.versioning.VersionRange;
import org.apache.maven.dist.tools.index.DistCheckIndexPageReport;

/**
 * <p>ConfigurationLineInfo class.</p>
 *
 * @author skygo
 */
public class ConfigurationLineInfo {
    private static final String URLSEP = "/";

    private final String directory;
    private final String groupId;
    private final boolean srcBin;
    private final String groupIndexPageUrl;

    private final String artifactId;
    private final VersionRange versionRange;

    private String forceVersion;
    private String indexPageUrl;
    private Metadata metadata;

    /**
     * <p>Constructor for ConfigurationLineInfo.</p>
     *
     * @param infos an array of {@link java.lang.String} objects
     */
    public ConfigurationLineInfo(String[] infos) {
        this.directory = infos[0].replace('/', ' ').replace(':', ' ').trim();
        String g = infos[1];
        int index = g.indexOf(':');
        this.groupId = (index < 0) ? g : g.substring(0, index);
        this.srcBin = (infos.length > 2) && "src+bin".equals(infos[2]);
        this.groupIndexPageUrl = (!srcBin && (infos.length > 2)) ? infos[2] : null;

        this.artifactId = (index < 0) ? null : g.substring(index + 1);
        this.versionRange = null;
        this.indexPageUrl = DistCheckIndexPageReport.POMS_INDEX_URL; // in case of group parent pom artifact
    }

    /**
     * <p>Constructor for ConfigurationLineInfo.</p>
     *
     * @param group a {@link org.apache.maven.dist.tools.ConfigurationLineInfo} object
     * @param infos an array of {@link java.lang.String} objects
     * @throws org.apache.maven.artifact.versioning.InvalidVersionSpecificationException if any.
     */
    public ConfigurationLineInfo(ConfigurationLineInfo group, String[] infos)
            throws InvalidVersionSpecificationException {
        this.directory = group.getDirectory();
        this.groupId = group.getGroupId();
        this.srcBin = group.isSrcBin();
        this.groupIndexPageUrl = group.groupIndexPageUrl;

        this.artifactId = infos[0];
        this.versionRange = (infos.length > 1) ? VersionRange.createFromVersionSpec(infos[1]) : null;
        this.indexPageUrl = group.groupIndexPageUrl;
    }

    /**
     * <p>Getter for the field <code>indexPageUrl</code>.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getIndexPageUrl() {
        return indexPageUrl;
    }

    /**
     * <p>getForcedVersion.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getForcedVersion() {
        return forceVersion;
    }

    /**
     * <p>Setter for the field <code>forceVersion</code>.</p>
     *
     * @param forceVersion a {@link java.lang.String} object
     */
    public void setForceVersion(String forceVersion) {
        this.forceVersion = forceVersion;
    }

    /**
     * <p>Getter for the field <code>versionRange</code>.</p>
     *
     * @return a {@link org.apache.maven.artifact.versioning.VersionRange} object
     */
    public VersionRange getVersionRange() {
        return versionRange;
    }

    /**
     * <p>Getter for the field <code>groupId</code>.</p>
     *
     * @return the groupId
     */
    public String getGroupId() {
        return groupId;
    }

    /**
     * <p>Getter for the field <code>artifactId</code>.</p>
     *
     * @return the artifactId
     */
    public String getArtifactId() {
        return artifactId;
    }

    /**
     * <p>Getter for the field <code>directory</code>.</p>
     *
     * @return the directory
     */
    public String getDirectory() {
        return directory;
    }

    /**
     * <p>isSrcBin.</p>
     *
     * @return a boolean
     */
    public boolean isSrcBin() {
        return srcBin;
    }

    /**
     * <p>getBaseURL.</p>
     *
     * @param repoBaseUrl a {@link java.lang.String} object
     * @param folder a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public String getBaseURL(String repoBaseUrl, String folder) {
        return repoBaseUrl + groupId.replaceAll("\\.", URLSEP) + URLSEP + artifactId + URLSEP + folder;
    }

    /**
     * <p>getMetadataFileURL.</p>
     *
     * @param repoBaseUrl a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public String getMetadataFileURL(String repoBaseUrl) {
        return getBaseURL(repoBaseUrl, "maven-metadata.xml");
    }

    /**
     * <p>getVersionnedFolderURL.</p>
     *
     * @param repoBaseUrl a {@link java.lang.String} object
     * @param version a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public String getVersionnedFolderURL(String repoBaseUrl, String version) {
        return getBaseURL(repoBaseUrl, version) + '/';
    }

    String getVersionnedPomFileURL(String repoBaseUrl, String version) {
        return getBaseURL(repoBaseUrl, version + URLSEP + artifactId + "-" + version + ".pom");
    }

    void setMetadata(Metadata aMetadata) {
        this.metadata = aMetadata;
    }

    /**
     * <p>getReleaseDateFromMetadata.</p>
     *
     * @return a {@link java.lang.String} object
     */
    public String getReleaseDateFromMetadata() {
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyyMMddkkmmss");
        TemporalAccessor ta = dateFormatter.parse(metadata.getVersioning().getLastUpdated());

        // inverted for index page check
        return DateTimeFormatter.ISO_LOCAL_DATE.format(ta);
    }

    /**
     * <p>getSourceReleaseFilename.</p>
     *
     * @param version a {@link java.lang.String} object
     * @param dist a boolean
     * @return a {@link java.lang.String} object
     */
    public String getSourceReleaseFilename(String version, boolean dist) {
        return artifactId + "-" + version
                + (srcBin && (dist || !"maven-ant-tasks".equals(artifactId)) ? "-src" : "-source-release") + ".zip";
    }
}
