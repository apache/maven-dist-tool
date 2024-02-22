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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.dist.tools.JsoupRetry;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * <p>GetPrerequisites class.</p>
 *
 * @author Karl Heinz Marbaiase
 */
public class GetPrerequisites {
    /**
     * TODO Currently hard code should be somehow extracted from the configuration file....
     */
    public static final String[] PLUGIN_NAMES = {
        "maven-acr-plugin",
        //        "maven-ant-plugin", // retired
        "maven-antrun-plugin",
        "maven-archetype-plugin",
        "maven-artifact-plugin",
        "maven-assembly-plugin",
        "maven-changelog-plugin",
        "maven-changes-plugin",
        "maven-checkstyle-plugin",
        "maven-clean-plugin",
        "maven-compiler-plugin",
        "maven-dependency-plugin",
        "maven-deploy-plugin",
        "maven-doap-plugin",
        // "maven-docck-plugin", retired
        "maven-ear-plugin",
        // "maven-eclipse-plugin", retired
        "maven-ejb-plugin",
        "maven-enforcer-plugin",
        "maven-failsafe-plugin",
        "maven-gpg-plugin",
        "maven-help-plugin",
        "maven-install-plugin",
        "maven-invoker-plugin",
        "maven-jar-plugin",
        "maven-jarsigner-plugin",
        "maven-javadoc-plugin",
        "maven-jdeprscan-plugin",
        "maven-jdeps-plugin",
        "maven-jlink-plugin",
        "maven-jmod-plugin",
        "maven-jxr-plugin",
        "maven-linkcheck-plugin",
        "maven-patch-plugin",
        "maven-pdf-plugin",
        "maven-plugin-plugin",
        "maven-pmd-plugin",
        "maven-project-info-reports-plugin",
        "maven-rar-plugin",
        "maven-release-plugin",
        "maven-remote-resources-plugin",
        // "maven-repository-plugin", retired
        "maven-resources-plugin",
        "maven-scm-plugin",
        "maven-scm-publish-plugin",
        "maven-scripting-plugin",
        "maven-shade-plugin",
        "maven-site-plugin",
        "maven-source-plugin",
        "maven-stage-plugin",
        "maven-surefire-plugin",
        "maven-surefire-report-plugin",
        "maven-toolchains-plugin",
        "maven-verifier-plugin",
        "maven-war-plugin",
        "maven-wrapper-plugin",
    };

    private static final String BASEURL = "https://maven.apache.org/plugins/";

    /**
     * Get Prerequisites for the given plugin name.
     */
    public GetPrerequisites() {}

    /**
     * <p>getPluginInfoUrl.</p>
     *
     * @param pluginName a {@link java.lang.String} object
     * @return a {@link java.lang.String} object
     */
    public String getPluginInfoUrl(String pluginName) {
        return BASEURL + pluginName + "/plugin-info.html";
    }

    /**
     * <p>getPluginPrerequisites.</p>
     *
     * @param pluginName a {@link java.lang.String} object
     * @return a {@link org.apache.maven.dist.tools.prerequisites.PluginPrerequisites} object
     * @throws java.io.IOException if any.
     */
    public PluginPrerequisites getPluginPrerequisites(String pluginName) throws IOException {
        String url = getPluginInfoUrl(pluginName);

        Document doc = JsoupRetry.get(url);

        String releaseDate = "?";
        Elements breadcrumbs = doc.select("div[id=breadcrumbs]"); // breadcrumbs
        if (breadcrumbs.size() >= 0) {
            String text = breadcrumbs.get(0).text();
            int index = text.indexOf("Last Published: ");
            releaseDate = text.substring(index + 16).substring(0, 10);
        }

        // select table(s):
        // - first one is the goals table
        // - second one is system requirements
        // - (optional) third one is system requirements history
        Elements select = doc.select("table.bodyTable"); // Stylus skin

        if (select.size() < 1) {
            select = doc.select("table.table-striped"); // Fluido skin
        }

        if (select.size() < 1) {
            System.err.println("Could not find expected plugin info for " + url);
            return new PluginPrerequisites(pluginName, "?", "?", "?", "?", 0, null);
        }

        // extract system requirements
        Element systemRequirementsTable = select.get(1);
        Elements elementsByAttributeA = systemRequirementsTable.getElementsByAttributeValue("class", "a");
        Elements elementsByAttributeB = systemRequirementsTable.getElementsByAttributeValue("class", "b");
        String mavenVersion = elementsByAttributeA.first().text();
        String jdkVersion = elementsByAttributeB.first().text();

        // FIXME: Sometimes it happens that the indexes are swapped (I don't know why...I have to find out why...)
        if (mavenVersion.startsWith("JDK")) {
            String tmp = jdkVersion;
            jdkVersion = mavenVersion;
            mavenVersion = tmp;
        }

        // Leave only version part...
        mavenVersion = mavenVersion.replace("Maven ", "");
        jdkVersion = jdkVersion.replace("JDK ", "").replace("1.", "");

        String pluginVersion = doc.select("pre").text();
        int index = pluginVersion.indexOf("<version>");
        if (index < 0) {
            pluginVersion = "";
        } else {
            pluginVersion = pluginVersion.substring(index + "<version>".length());
            pluginVersion = pluginVersion.substring(0, pluginVersion.indexOf("</version>"));
        }

        // extract system requirements history
        int systemRequirementsHistorySize = 0;
        String oldest = null;
        if (select.size() > 2) {
            Elements systemRequirementsHistoryTrs = select.get(2).select("tr");
            systemRequirementsHistorySize = systemRequirementsHistoryTrs.size() - 1;

            Elements td = systemRequirementsHistoryTrs
                    .get(systemRequirementsHistorySize)
                    .select("td");
            oldest = "requires Maven " + td.get(1).text() + " + JDK "
                    + td.get(2).text().replace("1.", "");
        }

        return new PluginPrerequisites(
                pluginName,
                pluginVersion,
                releaseDate,
                mavenVersion,
                jdkVersion,
                systemRequirementsHistorySize,
                oldest);
    }

    /**
     * <p>getPrequisites.</p>
     *
     * @return a {@link java.util.List} object
     */
    public List<PluginPrerequisites> getPrequisites() {
        List<PluginPrerequisites> result = new ArrayList<>();

        for (String pluginName : PLUGIN_NAMES) {
            try {
                result.add(getPluginPrerequisites(pluginName));
            } catch (IOException e) {
                // What could happen?
                // check it...
            }
        }
        return result;
    }

    /**
     * <p>getGroupedPrequisites.</p>
     *
     * @return a {@link java.util.Map} object
     */
    public Map<ArtifactVersion, List<PluginPrerequisites>> getGroupedPrequisites() {
        return getPrequisites().stream().collect(Collectors.groupingBy(PluginPrerequisites::getMavenVersion));
    }
}
