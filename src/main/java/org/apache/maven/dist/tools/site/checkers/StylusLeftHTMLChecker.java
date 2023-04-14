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
 * Check if artifact version is present on left side in stylus skin.
 *
 * @author skygo
 */
public class StylusLeftHTMLChecker implements HTMLChecker {
    /**
     * Stylus Left Html Checker
     */
    public StylusLeftHTMLChecker() {}

    /** {@inheritDoc} */
    @Override
    public String getName() {
        return "Stylus left side";
    }

    /** {@inheritDoc} */
    @Override
    public String getSkin() {
        return "Stylus";
    }

    /** {@inheritDoc} */
    @Override
    public boolean isDisplayedArtifactVersionOk(Document doc, String version) {
        Element links = doc.select("div.xleft").first();

        return (links != null) && links.text().contains(version);
    }
}
