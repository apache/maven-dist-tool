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

import java.io.IOException;
import java.io.InputStream;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * SiteReportTest
 */
class SiteReportTest {

    private String readDocument(String resource) throws IOException {
        try (InputStream in = getClass().getResourceAsStream(resource)) {
            Document doc = Jsoup.parse(in, "UTF-8", "https://maven.apache.org/archetype/");
            return CheckSiteResult.extractComment(doc);
        }
    }

    /**
     * Test Fluido skin
     *
     * @throws IOException if the resource cannot be read
     */
    @Test
    public void testFluidoSkin() throws IOException {
        String comment = readDocument("fluido.html");
        assertTrue(comment.contains("Fluido"));
    }

    /**
     * test Stylus Right skin
     * *
     * @throws IOException if the resource cannot be read
     */
    @Test
    public void testStylusRightSkin() throws IOException {
        String comment = readDocument("stylus-right.html");
        assertTrue(comment.contains("Stylus"));
    }
}
