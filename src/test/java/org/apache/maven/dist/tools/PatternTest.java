package org.apache.maven.dist.tools;

/*
 * Copyright 2013 The Apache Software Foundation.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 *
 * @author skygo
 */
public class PatternTest
{
    
    /**
     * Test of getGroupId method, of class ConfigurationLineInfo.
     */
    @Test
    public void testGetGroupId()
    {
        String q = DistCheckSourceReleaseMojo.getSourceReleasePattern( "doxia" );

        assertTrue( "doxia-1.4-source-release.zip.asc".matches( q ) );
        assertTrue( "doxia-1.4-source-release.zip.md5".matches( q ) );
        assertTrue( "doxia-1.4-source-release.zip".matches( q ) );
        assertFalse( "doxia-sitetools-1.4-source-release.zip.asc".matches( q ) );
        assertFalse( "doxia-sitetools-1.4-source-release.zip.md5".matches( q ) );
        assertFalse( "doxia-sitetools-1.4-source-release.zip".matches( q ) );

        String r = DistCheckSourceReleaseMojo.getSourceReleasePattern( "doxia-sitetools" );

        assertFalse( "doxia-1.4-source-release.zip.asc".matches( r ) );
        assertFalse( "doxia-1.4-source-release.zip.md5".matches( r ) );
        assertFalse( "doxia-1.4-source-release.zip".matches( r ) );
        assertTrue( "doxia-sitetools-1.4-source-release.zip.asc".matches( r ) );
        assertTrue( "doxia-sitetools-1.4-source-release.zip.md5".matches( r ) );
        assertTrue( "doxia-sitetools-1.4-source-release.zip".matches( r ) );
    }

    

  
}
