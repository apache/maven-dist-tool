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

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class BranchesResponse {

    @JsonProperty("payload")
    private Payload payload;

    public Payload getPayload() {
        return payload;
    }

    public void setPayload(final Payload payload) {
        this.payload = payload;
    }

    public static class Payload {
        @JsonProperty("current_page")
        private int currentPage;

        @JsonProperty("has_more")
        private boolean hasMore;

        @JsonProperty("per_page")
        private int perPage;

        @JsonProperty("branches")
        private List<Branch> branches;

        public int getCurrentPage() {
            return currentPage;
        }

        public void setCurrentPage(final int currentPage) {
            this.currentPage = currentPage;
        }

        public boolean hasMore() {
            return hasMore;
        }

        public void setHasMore(final boolean hasMore) {
            this.hasMore = hasMore;
        }

        public int getPerPage() {
            return perPage;
        }

        public void setPerPage(final int perPage) {
            this.perPage = perPage;
        }

        public List<Branch> getBranches() {
            return branches;
        }

        public void setBranches(final List<Branch> branches) {
            this.branches = branches;
        }
    }

    public static class Branch {
        @JsonProperty("name")
        private String name;

        public String getName() {
            return name;
        }

        public void setName(final String name) {
            this.name = name;
        }
    }
}
