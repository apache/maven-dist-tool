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

import org.jsoup.nodes.Document;

/**
 * <p>HTMLChecker interface.</p>
 *
 * @author skygo
 */
public interface HTMLChecker {

    /**
     * name of the checker.
     *
     * @return name
     */
    String getName();

    /**
     * Get an id representing skin.
     *
     * @return string for skin
     */
    String getSkin();

    /**
     * Checks if the documents represents the provided artifact version.
     *
     * @param doc html document
     * @param version version to check against
     * @return true if version is found
     */
    boolean isDisplayedArtifactVersionOk(Document doc, String version);
}
