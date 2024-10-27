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

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.dist.tools.index.DistCheckIndexPageReport;
import org.apache.maven.dist.tools.pgp.CheckPgpKeysReport;
import org.apache.maven.dist.tools.site.DistCheckSiteReport;
import org.apache.maven.dist.tools.source.DistCheckSourceReleaseReport;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.codehaus.plexus.util.FileUtils;

/**
 * <p>DistCheckErrorsReport class.</p>
 *
 * @author skygo
 */
@Mojo(name = "check-errors", requiresProject = false)
public class DistCheckErrorsReport extends AbstractDistCheckReport {
    private static final String[] FAILURES_FILENAMES = {
        DistCheckSourceReleaseReport.FAILURES_FILENAME,
        DistCheckSiteReport.FAILURES_FILENAME,
        DistCheckIndexPageReport.FAILURES_FILENAME,
        CheckPgpKeysReport.FAILURES_FILENAME
    };

    private static final String EOL = System.getProperty("line.separator");

    /**
     * Dist Check Errors Report.
     */
    public DistCheckErrorsReport() {}

    /** {@inheritDoc} */
    @Override
    protected boolean isIndexPageCheck() {
        return false;
    }

    boolean isDummyFailure() {
        return false;
    }

    private boolean checkError(String failuresFilename) throws MavenReportException {
        File failureFile = new File(failuresDirectory, failuresFilename);

        try {
            if (failureFile.exists()) {
                String content = FileUtils.fileRead(failureFile);

                if (isDummyFailure()) {
                    getLog().error(failuresFilename + " error log not empty:" + EOL + content);
                } else {
                    String failure = failuresFilename.substring(0, failuresFilename.length() - 4);
                    Sink s = getSink();
                    s.section2();
                    s.sectionTitle2();
                    s.link("dist-tool-" + failure + ".html");
                    s.text(failure);
                    s.link_();
                    s.sectionTitle2_();
                    s.verbatim();
                    s.rawText(content);
                    s.verbatim_();
                    s.section2_();

                    Set<String> urls = new HashSet<>();
                    Pattern p = Pattern.compile("https://[\\S]+");
                    Matcher m = p.matcher(content);
                    while (m.find()) {
                        urls.add(m.group());
                    }
                    if (!urls.isEmpty()) {
                        s.list();
                        for (String url : urls) {
                            s.listItem();
                            s.link(url);
                            s.text(url);
                            s.link_();
                            s.listItem_();
                        }
                        s.list_();
                    }
                }
            }

            return failureFile.exists();
        } catch (IOException ioe) {
            throw new MavenReportException("Cannot read " + failureFile, ioe);
        }
    }

    /** {@inheritDoc} */
    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        boolean failure = false;
        // if failures log file is present, throw exception to fail build
        for (String failuresFilename : FAILURES_FILENAMES) {
            failure |= checkError(failuresFilename);
        }

        if (failure) {
            if (isDummyFailure()) {
                throw new MavenReportException(
                        "Dist Tool> Checks found inconsistencies in some released artifacts, see "
                                + "https://ci-maven.apache.org/job/Maven/job/maven-box/job/maven-dist-tool/job/master/site/"
                                + "dist-tool-check-errors.html for more information");
            }
        } else {
            getSink().paragraph();
            getSink().text("No issue found.");
            getSink().paragraph_();
        }
    }

    /**
     * <p>getFailuresFilename.</p>
     *
     * @return a {@link java.lang.String} object
     */
    protected String getFailuresFilename() {
        return "dummy";
    }

    /** {@inheritDoc} */
    @Override
    public String getOutputName() {
        return "dist-tool-check-errors";
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> Check Errors";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Dist Tool report to display inconsistencies found by any check report";
    }

    /** {@inheritDoc} */
    @Override
    protected void checkArtifact(ConfigurationLineInfo request, String repoBase) {}
}
