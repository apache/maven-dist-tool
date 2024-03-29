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
package org.apache.maven.dist.tools.site.checkers;

import org.apache.maven.dist.tools.site.HTMLChecker;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Check if artifact version is present in fluido skin.
 *
 * @author skygo
 */
public class FluidoHTMLChecker implements HTMLChecker {
    /**
     * Fluido Html Checker
     */
    public FluidoHTMLChecker() {}

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Fluido";
    }

    /** {@inheritDoc} */
    @Override
    public String getSkin() {
        return "Fluido";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDisplayedArtifactVersionOk(Document doc, String version) {
        Element links = doc.select("li#projectVersion").first();
        return (links != null) && links.text().contains(version);
    }
}
