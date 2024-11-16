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

import org.apache.maven.doxia.sink.Sink;

/**
 * Print icons in reports.
 */
public class IconsUtils {

    /**
     * Utility class
     */
    private IconsUtils() {}

    /**
     * add an error icon.
     *
     * @param sink doxiasink
     */
    public static void error(Sink sink) {
        icon(sink, "icon_error_sml");
    }

    /**
     * add a warning icon.
     *
     * @param sink doxiasink
     */
    public static void warning(Sink sink) {
        icon(sink, "icon_warning_sml");
    }

    /**
     * add an success icon.
     *
     * @param sink doxiasink
     */
    public static void success(Sink sink) {
        icon(sink, "icon_success_sml");
    }

    /**
     * add a "remove" icon.
     *
     * @param sink doxiasink
     */
    public static void remove(Sink sink) {
        icon(sink, "remove");
    }

    private static void icon(Sink sink, String level) {
        sink.figureGraphics("images/" + level + ".gif");
    }
}
