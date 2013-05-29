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

import java.util.LinkedList;
import java.util.List;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author skygo
 */
@XmlRootElement( name = "metadata" )
public class MavenMetadata
{
// reverse eng from wagon metadata

    @XmlElement
    String groupId;
    @XmlElement
    String artifactId;
    @XmlElement
    VersioningTag versioning = new VersioningTag();

    public static class VersioningTag
    {

        @XmlElement
        String latest;
        @XmlElement
        String release;
        @XmlElement
        String lastUpdated;
        @XmlElementWrapper( name = "versions" )
        @XmlElement( name = "version" )
        List<String> versions = new LinkedList<String>();
    }
}
