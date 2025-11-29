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
package org.apache.maven.dist.tools.jobs.branches;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import org.apache.maven.dist.tools.JsoupRetry;
import org.apache.maven.dist.tools.jobs.AbstractJobsReport;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.SinkEventAttributes;
import org.apache.maven.doxia.sink.impl.SinkEventAttributeSet;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.MavenReportException;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Ref;
import org.jsoup.nodes.Document;

/**
 * Generate report with build status of the Jenkins job for the master branch of every Git repository in
 * <a href="https://ci-maven.apache.org/job/Maven/job/maven-box/">{@code maven-box} Apache Hosted Git Folder job</a>.
 *
 * @author Robert Scholte
 */
@Mojo(name = "list-branches", requiresProject = false)
public class ListBranchesReport extends AbstractJobsReport {
    private static final String JIRA_BASE_URL = "https://issues.apache.org/jira/projects/";

    private static final String GITHUB_URL = "https://github.com/apache/";

    private static final String DEPENDABOT_CONFIG = ".github/dependabot.yml";

    private static final Map<String, String> JIRAPROJECTS = new HashMap<>();

    static {
        JIRAPROJECTS.put("maven", "MNG");
        JIRAPROJECTS.put("maven-acr-plugin", "MACR");
        JIRAPROJECTS.put("maven-antrun-plugin", "MANTRUN");
        JIRAPROJECTS.put("maven-apache-parent", "MPOM");
        JIRAPROJECTS.put("maven-archetype", "ARCHETYPE");
        JIRAPROJECTS.put("maven-archetypes", "ARCHETYPE");
        JIRAPROJECTS.put("maven-archiver", "MSHARED");
        JIRAPROJECTS.put("maven-artifact-plugin", "MARTIFACT");
        // JIRAPROJECTS.put("maven-artifact-transfer", "MSHARED"); retired
        JIRAPROJECTS.put("maven-assembly-plugin", "MASSEMBLY");
        JIRAPROJECTS.put("maven-build-cache-extension", "MBUILDCACHE");
        JIRAPROJECTS.put("maven-changelog-plugin", "MCHANGELOG");
        JIRAPROJECTS.put("maven-changes-plugin", "MCHANGES");
        JIRAPROJECTS.put("maven-checkstyle-plugin", "MCHECKSTYLE");
        JIRAPROJECTS.put("maven-clean-plugin", "MCLEAN");
        JIRAPROJECTS.put("maven-common-artifact-filters", "MSHARED");
        JIRAPROJECTS.put("maven-compiler-plugin", "MCOMPILER");
        // JIRAPROJECTS.put("maven-default-skin", "MSKINS"); retired
        JIRAPROJECTS.put("maven-dependency-analyzer", "MSHARED");
        JIRAPROJECTS.put("maven-dependency-plugin", "MDEP");
        JIRAPROJECTS.put("maven-dependency-tree", "MSHARED");
        JIRAPROJECTS.put("maven-deploy-plugin", "MDEPLOY");
        JIRAPROJECTS.put("maven-doap-plugin", "MDOAP");
        JIRAPROJECTS.put("maven-doxia", "DOXIA");
        JIRAPROJECTS.put("maven-doxia-book-maven-plugin", "DOXIA");
        // JIRAPROJECTS.put("maven-doxia-book-renderer", "DOXIA"); retired
        JIRAPROJECTS.put("maven-doxia-converter", "DOXIATOOLS");
        // JIRAPROJECTS.put("maven-doxia-linkcheck", "DOXIATOOLS"); retired
        JIRAPROJECTS.put("maven-doxia-site", "DOXIA");
        JIRAPROJECTS.put("maven-doxia-sitetools", "DOXIASITETOOLS");
        JIRAPROJECTS.put("maven-ear-plugin", "MEAR");
        JIRAPROJECTS.put("maven-ejb-plugin", "MEJB");
        JIRAPROJECTS.put("maven-enforcer", "MENFORCER");
        JIRAPROJECTS.put("maven-file-management", "MSHARED");
        JIRAPROJECTS.put("maven-filtering", "MSHARED");
        JIRAPROJECTS.put("maven-fluido-skin", "MSKINS");
        JIRAPROJECTS.put("maven-gpg-plugin", "MGPG");
        JIRAPROJECTS.put("maven-help-plugin", "MHELP");
        JIRAPROJECTS.put("maven-indexer", "MINDEXER");
        JIRAPROJECTS.put("maven-install-plugin", "MINSTALL");
        JIRAPROJECTS.put("maven-integration-testing", "MNG");
        JIRAPROJECTS.put("maven-invoker", "MSHARED");
        JIRAPROJECTS.put("maven-invoker-plugin", "MINVOKER");
        JIRAPROJECTS.put("maven-jar-plugin", "MJAR");
        JIRAPROJECTS.put("maven-jarsigner", "MSHARED");
        JIRAPROJECTS.put("maven-jarsigner-plugin", "MJARSIGNER");
        JIRAPROJECTS.put("maven-javadoc-plugin", "MJAVADOC");
        JIRAPROJECTS.put("maven-jdeprscan-plugin", "MJDEPRSCAN");
        JIRAPROJECTS.put("maven-jdeps-plugin", "MJDEPS");
        JIRAPROJECTS.put("maven-jlink-plugin", "MJLINK");
        JIRAPROJECTS.put("maven-jmod-plugin", "MJMOD");
        JIRAPROJECTS.put("maven-jxr", "JXR");
        //        JIRAPROJECTS.put("maven-linkcheck-plugin", "MLINKCHECK"); retired
        JIRAPROJECTS.put("maven-mapping", "MSHARED");
        JIRAPROJECTS.put("maven-parent", "MPOM");
        // JIRAPROJECTS.put("maven-patch-plugin", "MPATCH"); retired
        // JIRAPROJECTS.put("maven-pdf-plugin", "MPDF"); retired
        JIRAPROJECTS.put("maven-plugin-testing", "MPLUGINTESTING");
        JIRAPROJECTS.put("maven-plugin-tools", "MPLUGIN");
        JIRAPROJECTS.put("maven-pmd-plugin", "MPMD");
        JIRAPROJECTS.put("maven-project-info-reports-plugin", "MPIR");
        // JIRAPROJECTS.put("maven-project-utils", "MSHARED"); retired
        JIRAPROJECTS.put("maven-rar-plugin", "MRAR");
        JIRAPROJECTS.put("maven-release", "MRELEASE");
        JIRAPROJECTS.put("maven-remote-resources-plugin", "MRRESOURCES");
        JIRAPROJECTS.put("maven-reporting-api", "MSHARED");
        JIRAPROJECTS.put("maven-reporting-exec", "MSHARED");
        JIRAPROJECTS.put("maven-reporting-impl", "MSHARED");
        JIRAPROJECTS.put("maven-resolver", "MRESOLVER");
        JIRAPROJECTS.put("maven-resolver-ant-tasks", "MRESOLVER");
        JIRAPROJECTS.put("maven-resources-plugin", "MRESOURCES");
        JIRAPROJECTS.put("maven-scm", "SCM");
        JIRAPROJECTS.put("maven-scm-publish-plugin", "MSCMPUB");
        JIRAPROJECTS.put("maven-script-interpreter", "MSHARED");
        JIRAPROJECTS.put("maven-scripting-plugin", "MSCRIPTING");
        JIRAPROJECTS.put("maven-shade-plugin", "MSHADE");
        JIRAPROJECTS.put("maven-shared-incremental", "MSHARED");
        JIRAPROJECTS.put("maven-shared-io", "MSHARED");
        JIRAPROJECTS.put("maven-shared-jar", "MSHARED");
        JIRAPROJECTS.put("maven-shared-resources", "MSHARED");
        JIRAPROJECTS.put("maven-shared-utils", "MSHARED");
        JIRAPROJECTS.put("maven-site", "MNGSITE");
        JIRAPROJECTS.put("maven-site-plugin", "MSITE");
        JIRAPROJECTS.put("maven-source-plugin", "MSOURCES");
        // JIRAPROJECTS.put("maven-stage-plugin", "MSTAGE"); retired
        JIRAPROJECTS.put("maven-surefire", "SUREFIRE");
        JIRAPROJECTS.put("maven-toolchains-plugin", "MTOOLCHAINS");
        JIRAPROJECTS.put("maven-verifier", "MSHARED");
        // JIRAPROJECTS.put("maven-verifier-plugin", "MVERIFIER"); retired
        JIRAPROJECTS.put("maven-wagon", "WAGON");
        JIRAPROJECTS.put("maven-war-plugin", "MWAR");
        JIRAPROJECTS.put("maven-wrapper", "MWRAPPER");
    }

    /**
     * List Branches Constructor.
     */
    public ListBranchesReport() {}

    /** {@inheritDoc} */
    @Override
    public String getOutputName() {
        return "dist-tool-branches";
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> List Branches";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Shows the list of branches of every Git repository on one page";
    }

    /** {@inheritDoc} */
    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        Collection<String> repositoryNames = repositoryNames();

        List<Result> repoStatus = new ArrayList<>(repositoryNames.size());

        for (String repository : repositoryNames) {
            getLog().info("processing " + repository);
            final String repositoryJobUrl = MAVENBOX_JOBS_BASE_URL + repository;

            try {
                Document jenkinsBranchesDoc = JsoupRetry.get(repositoryJobUrl);
                Result result = new Result(repository, repositoryJobUrl);
                int masterBranchesGit = 0;
                int masterBranchesJenkins = 0;
                Collection<String> jiraBranchesGit = new ArrayList<>();
                Collection<String> jiraBranchesJenkins = new ArrayList<>();
                Collection<String> dependabotBranchesGit = new ArrayList<>();
                Collection<String> dependabotBranchesJenkins = new ArrayList<>();
                Collection<String> restGit = new ArrayList<>();
                Collection<String> restJenkins = new ArrayList<>();

                for (String branch : getBranches(repository)) {
                    if ("master".equals(branch)) {
                        masterBranchesGit++;

                        if (jenkinsBranchesDoc.getElementById("job_master") != null) {
                            masterBranchesJenkins++;
                        }
                    } else if (JIRAPROJECTS.containsKey(repository)
                            && branch.toUpperCase().startsWith(JIRAPROJECTS.get(repository) + '-')) {
                        jiraBranchesGit.add(branch);
                        if (jenkinsBranchesDoc.getElementById(URLEncoder.encode("job_" + branch, "UTF-8")) != null) {
                            jiraBranchesJenkins.add(branch);
                        }
                    } else if (branch.startsWith("dependabot/")) {
                        dependabotBranchesGit.add(branch);
                        if (jenkinsBranchesDoc.getElementById(URLEncoder.encode("job_" + branch, "UTF-8")) != null) {
                            dependabotBranchesJenkins.add(branch);
                        }
                    } else {
                        restGit.add(branch);
                        if (jenkinsBranchesDoc.getElementById(URLEncoder.encode("job_" + branch, "UTF-8")) != null) {
                            restJenkins.add(branch);
                        }
                    }
                }

                result.setMasterBranchesGit(masterBranchesGit);
                result.setMasterBranchesJenkins(masterBranchesJenkins);
                result.setJiraBranchesGit(jiraBranchesGit);
                result.setJiraBranchesJenkins(jiraBranchesJenkins);
                result.setDependabotBranchesGit(dependabotBranchesGit);
                result.setDependabotBranchesJenkins(dependabotBranchesJenkins);
                result.setRestGit(restGit);
                result.setRestJenkins(restJenkins);

                repoStatus.add(result);
            } catch (IOException | GitAPIException e) {
                getLog().warn("Failed to read status for " + repository + " Jenkins job " + repositoryJobUrl);
            }
        }

        generateReport(repoStatus);
    }

    private String getGitHubBranchesUrl(String repository) {
        return GITHUB_URL + repository + "/branches/all";
    }

    @SuppressWarnings("checkstyle:MethodLength")
    private void generateReport(List<Result> repoStatus) {
        AtomicInteger masterJenkinsTotal = new AtomicInteger();
        AtomicInteger masterGitTotal = new AtomicInteger();
        AtomicInteger jiraJenkinsTotal = new AtomicInteger();
        AtomicInteger jiraGitTotal = new AtomicInteger();
        AtomicInteger dependabotJenkinsTotal = new AtomicInteger();
        AtomicInteger dependabotGitTotal = new AtomicInteger();
        AtomicInteger restJenkinsTotal = new AtomicInteger();
        AtomicInteger restGitTotal = new AtomicInteger();

        Sink sink = getSink();

        sink.head();
        sink.title();
        sink.text("List Branches");
        sink.title_();
        sink.head_();

        sink.body();
        sink.paragraph();
        sink.rawText("Values are shown as <code>jenkinsBranches / gitBranches</code>, "
                + "because not all branches end up in Jenkins, this depends on the existence of the Jenkinsfile.<br>");
        sink.rawText("Hover over the values to see branch names, values link to its URL to Jenkins or Gitbox</br>");
        sink.rawText("For Dependabot an empty field means there's no <code>" + DEPENDABOT_CONFIG + "</code>");

        sink.paragraph();

        sink.table();
        sink.tableRows(null, true);
        sink.tableRow();
        sink.tableHeaderCell();
        sink.text("Repository");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("JIRA");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Branches:");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("master");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("JIRA");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Dependabot");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Rest");
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text("Total");
        sink.tableHeaderCell_();
        sink.tableRow_();

        repoStatus.stream()
                .sorted(Comparator.comparing(Result::getTotalJenkins)
                        .thenComparing(Result::getTotalGit)
                        .reversed())
                .forEach(r -> {
                    sink.tableRow();

                    // GitHub
                    sink.tableCell();
                    sink.text(r.getRepositoryName());
                    sink.tableCell_();

                    // Jira
                    sink.tableCell();
                    String jiraId = JIRAPROJECTS.get(r.getRepositoryName());
                    if (jiraId != null) {
                        sink.link(JIRA_BASE_URL + jiraId);
                        sink.rawText(jiraId);
                        sink.link_();
                    }
                    sink.tableCell_();

                    // branches:
                    sink.tableCell();
                    sink.tableCell_();

                    // master
                    sink.tableCell();
                    sink.text(r.getMasterBranchesJenkins() + " / " + r.getMasterBranchesGit());
                    sink.tableCell_();
                    masterJenkinsTotal.addAndGet(r.getMasterBranchesJenkins());
                    masterGitTotal.addAndGet(r.getMasterBranchesGit());

                    // jira branches
                    sink.tableCell();
                    if (r.getJiraBranchesGit().isEmpty()) {
                        sink.text("-");
                    } else {
                        SinkEventAttributes jiraLinkAttributes = new SinkEventAttributeSet();
                        jiraLinkAttributes.addAttribute(
                                SinkEventAttributes.TITLE,
                                r.getJiraBranchesJenkins().stream().collect(Collectors.joining("\n")));

                        SinkEventAttributes gitLinkAttributes = new SinkEventAttributeSet();
                        r.getJiraBranchesGit().stream()
                                .filter(n -> !r.getJiraBranchesJenkins().contains(n))
                                .reduce((n1, n2) -> n1 + "\n" + n2)
                                .ifPresent(t -> gitLinkAttributes.addAttribute(
                                        SinkEventAttributes.TITLE, "-- non-Jenkins branches --\n" + t));

                        sink.bold();
                        sink.link(r.getBuildUrl(), jiraLinkAttributes);
                        sink.rawText(String.valueOf(r.getJiraBranchesJenkins().size()));
                        sink.link_();
                        sink.text(" / ");
                        sink.link(getGitHubBranchesUrl(r.getRepositoryName()), gitLinkAttributes);
                        sink.rawText(String.valueOf(r.getJiraBranchesGit().size()));
                        sink.link_();
                        sink.bold_();

                        jiraJenkinsTotal.addAndGet(r.getJiraBranchesJenkins().size());
                        jiraGitTotal.addAndGet(r.getJiraBranchesGit().size());
                    }
                    sink.tableCell_();

                    // dependabot branches
                    sink.tableCell();
                    if (r.getDependabotBranchesGit().isEmpty()) {
                        try {
                            if (hasDependabotYml(r.getRepositoryName())) {
                                sink.text("-");
                            }
                        } catch (IOException e) {
                            sink.text("_");
                        }
                    } else {
                        SinkEventAttributes jenkinsLinkAttributes = new SinkEventAttributeSet();
                        jenkinsLinkAttributes.addAttribute(
                                SinkEventAttributes.TITLE,
                                r.getDependabotBranchesJenkins().stream().collect(Collectors.joining("\n")));

                        SinkEventAttributes gitLinkAttributes = new SinkEventAttributeSet();
                        r.getDependabotBranchesGit().stream()
                                .filter(n -> !r.getDependabotBranchesJenkins().contains(n))
                                .reduce((n1, n2) -> n1 + "\n" + n2)
                                .ifPresent(t -> gitLinkAttributes.addAttribute(
                                        SinkEventAttributes.TITLE, "-- non-Jenkins branches --\n" + t));

                        sink.bold();
                        sink.link(r.getBuildUrl(), jenkinsLinkAttributes);
                        sink.rawText(
                                String.valueOf(r.getDependabotBranchesJenkins().size()));
                        sink.link_();
                        sink.text(" / ");
                        sink.link(getGitHubBranchesUrl(r.getRepositoryName()), gitLinkAttributes);
                        sink.rawText(String.valueOf(r.getDependabotBranchesGit().size()));
                        sink.link_();

                        dependabotJenkinsTotal.addAndGet(
                                r.getDependabotBranchesJenkins().size());
                        dependabotGitTotal.addAndGet(
                                r.getDependabotBranchesGit().size());
                    }
                    sink.tableCell_();

                    // rest
                    sink.tableCell();
                    if (r.getRestGit().isEmpty()) {
                        sink.text("-");
                    } else {
                        SinkEventAttributes restLinkAttributes = new SinkEventAttributeSet();
                        restLinkAttributes.addAttribute(
                                SinkEventAttributes.TITLE,
                                r.getRestJenkins().stream().collect(Collectors.joining("\n")));

                        SinkEventAttributes gitLinkAttributes = new SinkEventAttributeSet();
                        r.getRestGit().stream()
                                .filter(n -> !r.getRestJenkins().contains(n))
                                .reduce((n1, n2) -> n1 + "\n" + n2)
                                .ifPresent(t -> gitLinkAttributes.addAttribute(
                                        SinkEventAttributes.TITLE, "-- non-Jenkins branches --\n" + t));

                        sink.bold();
                        sink.link(r.getBuildUrl(), restLinkAttributes);
                        sink.rawText(String.valueOf(r.getRestJenkins().size()));
                        sink.link_();
                        sink.text(" / ");
                        sink.link(getGitHubBranchesUrl(r.getRepositoryName()), gitLinkAttributes);
                        sink.rawText(String.valueOf(r.getRestGit().size()));
                        sink.link_();

                        restJenkinsTotal.addAndGet(r.getRestJenkins().size());
                        restGitTotal.addAndGet(r.getRestGit().size());
                    }
                    sink.tableCell_();

                    // total
                    sink.tableCell();
                    sink.text(r.getTotalJenkins() + " / " + r.getTotalGit());
                    sink.tableCell_();

                    sink.tableRow_();
                });

        sink.tableRow();
        sink.tableHeaderCell();
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.tableHeaderCell_();
        // branches:
        sink.tableCell();
        sink.text("Total");
        sink.tableCell_();
        sink.tableHeaderCell();
        sink.text(masterJenkinsTotal.get() + " / " + masterGitTotal.get());
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text(jiraJenkinsTotal.get() + " / " + jiraGitTotal.get());
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text(dependabotJenkinsTotal.get() + " / " + dependabotGitTotal.get());
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text(restJenkinsTotal.get() + " / " + restGitTotal.get());
        sink.tableHeaderCell_();
        sink.tableHeaderCell();
        sink.text((masterJenkinsTotal.get()
                        + jiraJenkinsTotal.get()
                        + dependabotJenkinsTotal.get()
                        + restJenkinsTotal.get())
                + " / "
                + (masterGitTotal.get() + jiraGitTotal.get() + dependabotGitTotal.get() + restGitTotal.get()));
        sink.tableHeaderCell_();
        sink.tableRow_();

        sink.tableRows_();
        sink.table_();
        sink.body_();
    }

    /**
     * <p>hasDependabotYml.</p>
     *
     * @param repositoryName a {@link java.lang.String} object
     * @return a boolean
     * @throws java.io.IOException if any.
     */
    protected static boolean hasDependabotYml(String repositoryName) throws IOException {
        URL url = new URL(GITHUB_URL + repositoryName + "/blob/master/" + DEPENDABOT_CONFIG);

        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("HEAD");

        return con.getResponseCode() == HttpURLConnection.HTTP_OK;
    }

    private Collection<String> getBranches(String repository) throws GitAPIException {
        final var refs = Git.lsRemoteRepository()
                .setHeads(true)
                .setTags(false)
                .setRemote(GITHUB_URL + repository + ".git")
                .call();

        final var branches = new ArrayList<String>();
        for (final Ref ref : refs) {
            final var name = ref.getName();
            var branch = name.substring(11);
            branches.add(branch);
            System.out.println(branch);
        }
        return branches;
    }
}
