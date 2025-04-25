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
package org.apache.maven.dist.tools.jobs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.maven.dist.tools.JsoupRetry;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.MavenReportException;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public abstract class AbstractJobsReport extends AbstractMavenReport {
    protected static final String GITBOX_URL = "https://gitbox.apache.org/repos/asf";

    protected static final String MAVENBOX_JOBS_BASE_URL = "https://ci-maven.apache.org/job/Maven/job/maven-box/job/";

    private static final Collection<String> EXCLUDED = Arrays.asList(
            "maven-blog",
            "maven-build-helper-plugin",
            "maven-gh-actions-shared",
            "maven-hocon-extension",
            "maven-integration-testing", // runs with Maven core job
            "maven-jenkins-env",
            "maven-jenkins-lib",
            "maven-metric-extension",
            "maven-mvnd",
            "maven-sources",
            "maven-studies",
            "maven-xinclude-extension");

    /**
     * Extract Git repository names for Apache Maven from
     * <a href="https://gitbox.apache.org/repos/asf">Gitbox main page</a>,
     * with some excludes.
     *
     * @return the list of repository names (without ".git")
     * @throws MavenReportException problem with reading repository index
     */
    protected Collection<String> repositoryNames() throws MavenReportException {
        try {
            List<String> names = new ArrayList<>(100);
            Document doc = JsoupRetry.get(GITBOX_URL);
            // find Apache Maven table
            Element apacheMavenTable =
                    doc.getElementsMatchingText("^Apache Maven$").parents().get(0);

            Elements gitRepo =
                    apacheMavenTable.select("tbody tr").not("tr.disabled").select("td:first-child a");

            for (Element element : gitRepo) {
                names.add(element.text().split("\\.git")[0]);
            }

            return names.stream().filter(s -> !EXCLUDED.contains(s)).collect(Collectors.toList());
        } catch (IOException e) {
            throw new MavenReportException("Failed to extract repositorynames from Gitbox " + GITBOX_URL, e);
        }
    }
}
