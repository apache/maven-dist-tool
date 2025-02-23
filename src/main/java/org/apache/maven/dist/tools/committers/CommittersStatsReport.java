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
package org.apache.maven.dist.tools.committers;

import javax.inject.Inject;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.maven.dist.tools.IconsUtils;
import org.apache.maven.dist.tools.committers.MavenCommittersRepository.Committer;
import org.apache.maven.doxia.sink.Sink;
import org.apache.maven.doxia.sink.impl.SinkEventAttributeSet.Semantics;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.reporting.AbstractMavenReport;
import org.apache.maven.reporting.AbstractMavenReportRenderer;
import org.apache.maven.reporting.MavenReportException;

/**
 * Generate a Committers statistic
 */
@Mojo(name = "committers-stats", requiresProject = false)
public class CommittersStatsReport extends AbstractMavenReport {

    public static final int LAST_ACTIVITY_MONTHS_ERROR = 4 * 12;

    public static final int LAST_ACTIVITY_MONTHS_WARNING = 2 * 12;

    private final Map<String, MLStats> mlStats;

    private final MavenCommittersRepository mavenCommitters;

    @Inject
    public CommittersStatsReport(Map<String, MLStats> mlStats, MavenCommittersRepository mavenCommitters) {
        this.mlStats = mlStats;
        this.mavenCommitters = mavenCommitters;
    }

    enum ActivityLevel {
        SUCCESS,
        WARNING,
        ERROR;
    }

    class Renderer extends AbstractMavenReportRenderer {

        private final String title;

        /**
         * Default constructor.
         *
         * @param sink the sink to use.
         */
        Renderer(Sink sink, String title) {
            super(sink);
            this.title = title;
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        protected void renderBody() {
            Map<Committer, List<String>> committerStats = retrieveCommitterStats();

            startSection("Committers Stats");
            sink.paragraph();
            sink.text("Committer statistics are based on the searching at ");
            link("https://lists.apache.org/list.html?dev@maven.apache.org", "public mailing lists");
            sink.paragraph_();
            renderStatsTable(committerStats);
            endSection();

            startSection("Committers Stats - summary");
            renderStatsSummary(committerStats);
            endSection();

            startSection("Legend");
            renderActivityLegend();
            queryDescriptionLegend();
            endSection();
        }

        private Map<Committer, List<String>> retrieveCommitterStats() {
            Map<Committer, List<String>> result = new LinkedHashMap<>();
            for (Committer committer : mavenCommitters.getCommitters()) {
                List<String> lastDateList = mlStats.values().stream()
                        .map(ml -> ml.getLast(committer))
                        .toList();
                result.put(committer, lastDateList);
            }
            return result;
        }

        private void renderStatsTable(Map<Committer, List<String>> committerStats) {

            int[] justification = new int[mlStats.size() + 3];
            Arrays.fill(justification, Sink.JUSTIFY_CENTER);
            justification[1] = Sink.JUSTIFY_LEFT;
            justification[2] = Sink.JUSTIFY_LEFT;
            startTable(justification, false);

            List<String> headers = new ArrayList<>();
            headers.add("");
            headers.add("ID");
            headers.add("Names");
            headers.addAll(mlStats.keySet());
            tableHeader(headers.toArray(String[]::new));

            int lp = 1;
            for (Map.Entry<Committer, List<String>> entry : committerStats.entrySet()) {

                Committer committer = entry.getKey();
                List<String> lastDateList = entry.getValue();

                sink.tableRow();
                tableCell(String.valueOf(lp));

                sink.tableCell();
                sink.text(committer.id(), committer.pmc() ? Semantics.BOLD : null);
                printStatusIcon(getActivityLevel(lastDateList));
                sink.tableCell_();

                tableCell(String.join(", ", committer.names()));

                lastDateList.forEach(this::tableCellWithLastDate);

                sink.tableRow_();
                lp++;
            }
            endTable();
        }

        private void renderStatsSummary(Map<Committer, List<String>> committerStats) {

            Map<ActivityLevel, Long> pmcs = committerStats.entrySet().stream()
                    .filter(entry -> entry.getKey().pmc())
                    .collect(Collectors.groupingBy(entry -> getActivityLevel(entry.getValue()), Collectors.counting()));

            Map<ActivityLevel, Long> commiters = committerStats.entrySet().stream()
                    .filter(entry -> !entry.getKey().pmc())
                    .collect(Collectors.groupingBy(entry -> getActivityLevel(entry.getValue()), Collectors.counting()));

            startTable(
                    new int[] {Sink.JUSTIFY_CENTER, Sink.JUSTIFY_CENTER, Sink.JUSTIFY_CENTER, Sink.JUSTIFY_CENTER},
                    false);
            tableHeader(new String[] {"Activity", "Commiters", "PMCs", "Total"});
            long committersTotal = 0;
            long pcmsTotal = 0;
            for (ActivityLevel level : ActivityLevel.values()) {

                Long committersCount = Optional.ofNullable(commiters.get(level)).orElse(0L);
                Long pcmsCount = Optional.ofNullable(pmcs.get(level)).orElse(0L);

                sink.tableRow();
                sink.tableCell();
                printStatusIcon(level);
                sink.tableCell_();
                tableCell(String.valueOf(committersCount));
                tableCell(String.valueOf(pcmsCount));
                tableCell(String.valueOf(committersCount + pcmsCount));
                sink.tableRow_();
                committersTotal += committersCount;
                pcmsTotal += pcmsCount;
            }
            tableRow(new String[] {
                "Total",
                String.valueOf(committersTotal),
                String.valueOf(pcmsTotal),
                String.valueOf(committersTotal + pcmsTotal)
            });
            endTable();
        }

        private void renderActivityLegend() {
            startSection("Last activity status");
            sink.definitionList();

            sink.definedTerm();
            IconsUtils.success(sink);
            sink.definedTerm_();
            sink.definition();
            sink.text("last activity within last ");
            boldText(String.valueOf(LAST_ACTIVITY_MONTHS_WARNING));
            sink.text(" months");
            sink.definition_();

            sink.definedTerm();
            IconsUtils.warning(sink);
            sink.definedTerm_();
            sink.definition();
            sink.text("last activity between ");
            boldText(String.valueOf(LAST_ACTIVITY_MONTHS_WARNING));
            sink.text(" and ");
            boldText(String.valueOf(LAST_ACTIVITY_MONTHS_ERROR));
            sink.text(" months");
            sink.definition_();

            sink.definedTerm();
            IconsUtils.error(sink);
            sink.definedTerm_();
            sink.definition();
            sink.text("last activity before than ");
            boldText(String.valueOf(LAST_ACTIVITY_MONTHS_ERROR));
            sink.text(" months");
            sink.definition_();

            sink.definitionList_();
            endSection();
        }

        private void queryDescriptionLegend() {
            startSection("Query description");
            sink.definitionList();

            for (Map.Entry<String, MLStats> entry : mlStats.entrySet()) {
                sink.definedTerm();
                sink.text(entry.getKey());
                sink.definedTerm_();
                sink.definition();
                entry.getValue().describe(sink);
                sink.definition_();
            }

            sink.definitionList_();
            endSection();
        }

        private void boldText(String text) {
            sink.bold();
            sink.text(text);
            sink.bold_();
        }

        @SuppressWarnings("checkstyle:MissingSwitchDefault")
        private void printStatusIcon(ActivityLevel activityLevel) {
            sink.text(" ");
            switch (activityLevel) {
                case ERROR -> IconsUtils.error(sink);
                case WARNING -> IconsUtils.warning(sink);
                case SUCCESS -> IconsUtils.success(sink);
            }
        }

        private ActivityLevel getActivityLevel(List<String> dates) {
            return getActivityLevel(dates.stream()
                    .filter(date -> !"-".equals(date))
                    .max(Comparator.naturalOrder())
                    .orElse("-"));
        }

        private ActivityLevel getActivityLevel(String date) {
            long monthAgo = 99;

            if (date != null && !"-".equals(date)) {
                DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate localDate = LocalDate.parse(date + "-01", dateTimeFormatter);
                Period period = Period.between(localDate, LocalDate.now());
                monthAgo = period.getYears() * 12L + period.getMonths();
            }

            sink.text(" ");
            if (monthAgo >= LAST_ACTIVITY_MONTHS_ERROR) {
                return ActivityLevel.ERROR;
            } else if (monthAgo >= LAST_ACTIVITY_MONTHS_WARNING) {
                return ActivityLevel.WARNING;
            } else {
                return ActivityLevel.SUCCESS;
            }
        }

        private void tableCellWithLastDate(String date) {
            sink.tableCell();
            sink.text(date);
            printStatusIcon(getActivityLevel(date));
            sink.tableCell_();
        }
    }

    @Override
    protected void executeReport(Locale locale) throws MavenReportException {
        new Renderer(getSink(), getName(locale)).render();
    }

    @Override
    public String getOutputName() {
        return "dist-tool-committers-stats";
    }

    @Override
    public String getName(Locale locale) {
        return "Dist Tool> Committers Stats";
    }

    @Override
    public String getDescription(Locale locale) {
        return "";
    }
}
