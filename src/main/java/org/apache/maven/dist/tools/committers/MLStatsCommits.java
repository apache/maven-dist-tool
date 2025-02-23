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

import javax.inject.Named;
import javax.inject.Singleton;

import java.util.List;
import java.util.Map;

import org.apache.maven.dist.tools.committers.MavenCommittersRepository.Committer;
import org.apache.maven.doxia.sink.Sink;

import static java.util.Map.entry;
import static java.util.Map.ofEntries;

@Named("Commits")
@Singleton
public class MLStatsCommits extends MLStats {

    @Override
    protected boolean describeList(Sink sink) {
        linkList(sink, "commits");
        sink.text(" or ");
        linkList(sink, "site-commits");
        return false;
    }

    @Override
    protected List<Map<String, String>> getQueryParamsList(Committer committer) {
        return List.of(
                ofEntries(entry("list", "commits"), entry("header_from", committer.id() + "@apache.org")),
                ofEntries(entry("list", "site-commits"), entry("header_from", committer.id() + "@apache.org")));
    }
}
