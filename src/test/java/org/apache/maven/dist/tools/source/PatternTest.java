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
package org.apache.maven.dist.tools.source;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * PatternTest
 *
 * @author skygo
 */
class PatternTest {

    /**
     * Test of getGroupId method, of class ConfigurationLineInfo.
     */
    @Test
    void testGetGroupId() {
        String q = DistCheckSourceReleaseReport.getSourceReleasePattern("doxia");

        assertTrue("doxia-1.4-source-release.zip.asc".matches(q));
        assertTrue("doxia-1.4-source-release.zip.md5".matches(q));
        assertTrue("doxia-1.4-source-release.zip".matches(q));
        assertFalse("doxia-sitetools-1.4-source-release.zip.asc".matches(q));
        assertFalse("doxia-sitetools-1.4-source-release.zip.md5".matches(q));
        assertFalse("doxia-sitetools-1.4-source-release.zip".matches(q));

        String r = DistCheckSourceReleaseReport.getSourceReleasePattern("doxia-sitetools");

        assertFalse("doxia-1.4-source-release.zip.asc".matches(r));
        assertFalse("doxia-1.4-source-release.zip.md5".matches(r));
        assertFalse("doxia-1.4-source-release.zip".matches(r));
        assertTrue("doxia-sitetools-1.4-source-release.zip.asc".matches(r));
        assertTrue("doxia-sitetools-1.4-source-release.zip.md5".matches(r));
        assertTrue("doxia-sitetools-1.4-source-release.zip".matches(r));
    }
}
