package org.apache.maven.dist.tools;

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

/**
 * <p>AbstractCheckResult class.</p>
 *
 * @author skygo
 */
public class AbstractCheckResult
{

    private final ConfigurationLineInfo configLine;
    private final String version;

    /**
     * <p>Constructor for AbstractCheckResult.</p>
     *
     * @param r a {@link org.apache.maven.dist.tools.ConfigurationLineInfo} object
     * @param version a {@link java.lang.String} object
     */
    public AbstractCheckResult( ConfigurationLineInfo r, String version )
    {
        this.configLine = r;
        this.version = version;
    }

    /**
     * <p>getConfigurationLine.</p>
     *
     * @return the request
     */
    public ConfigurationLineInfo getConfigurationLine()
    {
        return configLine;
    }

    /**
     * <p>Getter for the field <code>version</code>.</p>
     *
     * @return the version
     */
    public String getVersion()
    {
        return version;
    }
}
