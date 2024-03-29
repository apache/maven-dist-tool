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
package org.apache.maven.dist.tools;

import java.util.Locale;

import org.apache.maven.plugins.annotations.Mojo;

/**
 * <p>DummyFailureReport class.</p>
 *
 * @author skygo
 */
@Mojo(name = "failure-report", requiresProject = false)
public class DummyFailureReport extends DistCheckErrorsReport {
    /**
     * Dummy
     */
    public DummyFailureReport() {}

    @Override
    boolean isDummyFailure() {
        return true;
    }

    /**
     * <p>getFailuresFilename.</p>
     *
     * @return a {@link java.lang.String} object
     */
    protected String getFailuresFilename() {
        return "dummy";
    }

    /** {@inheritDoc} */
    @Override
    public String getOutputName() {
        return "dist-tool-failure";
    }

    /** {@inheritDoc} */
    @Override
    public String getName(Locale locale) {
        return "Dist Tool> Failure Hack";
    }

    /** {@inheritDoc} */
    @Override
    public String getDescription(Locale locale) {
        return "Dist Tool report to fail the build in case of inconsistency found by any check reports";
    }
}
