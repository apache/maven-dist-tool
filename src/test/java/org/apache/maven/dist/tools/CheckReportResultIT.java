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

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.FieldSource;

import static org.junit.jupiter.api.Assertions.fail;

class CheckReportResultIT {

    private static final Path ERROR_LOGS_PATH = Paths.get(
                    System.getProperty("basedir", new File(".").getAbsolutePath()))
            .resolve("target")
            .resolve("dist-tool")
            .normalize();

    @ParameterizedTest
    @FieldSource("org.apache.maven.dist.tools.DistCheckErrorsReport#FAILURES_FILENAMES")
    void shouldBeNoErrors(Path logFile) throws Exception {
        Path logPath = ERROR_LOGS_PATH.resolve(logFile);
        if (Files.isRegularFile(logPath)) {
            List<String> lines = Files.readAllLines(logPath);
            if (!lines.isEmpty()) {
                System.err.println(String.join(System.lineSeparator(), lines));
                fail();
            }
        }
    }
}
