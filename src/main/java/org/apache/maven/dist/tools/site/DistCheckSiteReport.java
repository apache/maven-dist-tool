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
package org.apache.maven.dist.tools.site;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

import org.apache.maven.artifact.Artifact;
import org.apache.maven.artifact.factory.ArtifactFactory;
import org.apache.maven.artifact.repository.ArtifactRepository;
import org.apache.maven.dist.tools.AbstractDistCheckReport;
import org.apache.maven.dist.tools.ConfigurationLineInfo;
import org.apache.maven.dist.tools.JsoupRetry;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Component;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;
import org.apache.maven.project.MavenProjectBuilder;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.HttpStatusException;
import org.jsoup.nodes.Document;

/**
 * <p>DistCheckSiteReport class.</p>
 *
 * @author skygo
 */
@Mojo(name = "check-site", requiresProject = false)
public class DistCheckSiteReport extends AbstractDistCheckReport {
    /** Constant <code>FAILURES_FILENAME="check-site.log"</code> */
    public static final String FAILURES_FILENAME = "check-site.log";

    /**
     * <p>Constructor for DistCheckSiteReport.</p>
     */
    public DistCheckSiteReport() {}

    /**
     * Ignore site failure for <code>artifactId</code> or <code>artifactId:version</code>
     */
    @Parameter
    protected List<String> ignoreSiteFailures;

    /**
     * Artifact factory.
     */
    @Component
    protected ArtifactFactory artifactFactory;

    /**
     * Local repository.
     */
    @Parameter(defaultValue = "${localRepository}", required = true, readonly = true)
    protected ArtifactRepository localRepository;

    /**
     * Maven project builder.
     */
    @Component
    protected MavenProjectBuilder mavenProjectBuilder;

    /**
     * Http status ok code.
     */
    protected static final int HTTP_OK = 200;

    /** {@inheritDoc} */
    @Override
    protected boolean isIndexPageCheck() {
        return false;
    }

    /**
     * <p>getFailuresFilename.</p>
     *
     * @return a {@link java.lang.String} object
     */
    protected String getFailuresFilename() {
        return FAILURES_FILENAME;
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> Check Sites";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Verification of documentation site corresponding to artifact";
    }

    // keep result
    private List<CheckSiteResult> results = new LinkedList<>();
    private final List<HTMLChecker> checkers = HTMLCheckerFactory.getCheckers();

    /** {@inheritDoc} */
    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        if (!outputDirectory.exists()) {
            outputDirectory.mkdirs();
        }

        prepareReportData();

        Sink sink = getSink();
        sink.head();
        sink.title();
        sink.text("Check sites");
        sink.title_();
        sink.head_();

        sink.body();
        sink.section1();
        sink.rawText("Checked sites, also do some basic checking in index.html contents.");
        sink.rawText("This is to help maintaining some coherence. How many site are skin fluido, stylus,"
                + " where they have artifact version (right, left)");
        sink.rawText("All sun icons in one column is kind of objective.");
        sink.section1_();
        sink.table();
        sink.tableRows(null, true);
        sink.tableRow();
        sink.tableHeaderCell();
        sink.rawText("groupId/artifactId");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText("LATEST");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText("DATE");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText("URL");
        sink.lineBreak();
        sink.rawText("Skins");
        sink.lineBreak();
        sink.rawText("Comments on top of html");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.rawText("Artifact version displayed");
        sink.tableHeaderCell_();
        sink.tableRow_();

        String directory = null;
        for (CheckSiteResult csr : results) {
            ConfigurationLineInfo cli = csr.getConfigurationLine();

            if (!cli.getDirectory().equals(directory)) {
                directory = cli.getDirectory();
                sink.tableRow();
                sink.tableHeaderCell();
                // shorten groupid
                sink.rawText(cli.getGroupId().replaceAll("org.apache.maven", "o.a.m"));
                sink.tableHeaderCell_();
                for (int i = 0; i < 5; i++) {
                    sink.tableHeaderCell();
                    sink.rawText(" ");
                    sink.tableHeaderCell_();
                }
                sink.tableRow_();
            }

            sink.tableRow();
            sink.tableCell();
            sink.anchor(cli.getArtifactId());
            sink.rawText(cli.getArtifactId());
            sink.anchor_();
            sink.tableCell_();

            sink.tableCell();
            sink.rawText(csr.getVersion());
            sink.tableCell_();

            sink.tableCell();
            sink.rawText(cli.getReleaseDateFromMetadata());
            sink.tableCell_();
            sink.tableCell();
            if (csr.getStatusCode() != HTTP_OK) {
                iconError(sink);
                sink.rawText("[" + csr.getStatusCode() + "] ");
            }
            sink.link(csr.getUrl());
            sink.rawText(getSimplifiedUrl(csr.getUrl()));
            sink.link_();
            sink.lineBreak();
            csr.renderDetectedSkin(sink);
            sink.tableCell_();

            sink.tableCell();
            csr.renderDisplayedArtifactVersion(sink);
            sink.tableCell_();

            sink.tableRow_();
        }
        sink.tableRows_();
        sink.table_();
        sink.body_();
        sink.flush();
        sink.close();
    }

    private String getSimplifiedUrl(String url) {
        return url.replace("://maven.apache.org", "://m.a.o");
    }

    private void checkSite(ConfigurationLineInfo cli, String version) {
        CheckSiteResult result = new CheckSiteResult(cli, version);
        results.add(result);
        try {
            Artifact artifact = artifactFactory.createProjectArtifact(cli.getGroupId(), cli.getArtifactId(), version);
            MavenProject artifactProject =
                    mavenProjectBuilder.buildFromRepository(artifact, artifactRepositories, localRepository, false);

            String siteUrl = sites.get(cli.getArtifactId());
            if (siteUrl == null) {
                siteUrl = sites.get(cli.getArtifactId() + ':' + version);
                if (siteUrl == null) {
                    siteUrl = artifactProject.getUrl();
                }
            }

            result.setUrl(siteUrl);
            Document doc = JsoupRetry.get(siteUrl);
            for (HTMLChecker c : checkers) {
                result.getCheckMap().put(c, c.isDisplayedArtifactVersionOk(doc, version));
            }
            result.setDocument(doc);

        } catch (HttpStatusException hes) {
            addErrorLine(
                    cli,
                    version,
                    ignoreSiteFailures,
                    "HTTP result code: " + hes.getStatusCode() + " for " + cli.getArtifactId() + " site = "
                            + hes.getUrl());
            result.setHTTPErrorUrl(hes.getStatusCode());
        } catch (Exception ex) {
            // continue for  other artifact
            getLog().error(ex.getMessage() + cli.getArtifactId());
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void checkArtifact(ConfigurationLineInfo configLine, String latestVersion) {
        checkSite(configLine, latestVersion);
    }
}
