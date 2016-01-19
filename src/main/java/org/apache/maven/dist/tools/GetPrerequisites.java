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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * @author Karl Heinz Marbaiase
 *
 */
public class GetPrerequisites
{
    /**
     * Currently hard code should be somehow extracted from the configuration file....
     */
    public static final String[] PLUGIN_NAMES = { 
        "maven-acr-plugin", 
        "maven-ant-plugin", 
        "maven-antrun-plugin",
        "maven-archetype-plugin",
        "maven-assembly-plugin",
        "maven-changelog-plugin",
        "maven-changes-plugin",
        "maven-checkstyle-plugin",
        "maven-clean-plugin",
        "maven-compiler-plugin",
        "maven-dependency-plugin",
        "maven-deploy-plugin",
        "maven-doap-plugin",
        "maven-docck-plugin",
        "maven-ear-plugin",
        // "maven-eclipse-plugin", retired
        "maven-ejb-plugin",
        "maven-enforcer-plugin",
        "maven-failsafe-plugin",
        "maven-gpg-plugin",
        "maven-help-plugin",
        "maven-install-plugin",
        "maven-invoker-plugin",
        "maven-jar-plugin",
        "maven-jarsigner-plugin",
        "maven-javadoc-plugin",
        "maven-jxr-plugin",
        "maven-linkcheck-plugin",
        "maven-patch-plugin",
        "maven-pdf-plugin",
        "maven-plugin-plugin",
        "maven-pmd-plugin",
        "maven-project-info-reports-plugin",
        "maven-rar-plugin",
        "maven-release-plugin",
        "maven-remote-resources-plugin",
        "maven-repository-plugin",
        "maven-resources-plugin",
        "maven-scm-plugin",
        "maven-scm-publish-plugin",
        "maven-shade-plugin",
        "maven-site-plugin",
        "maven-source-plugin",
        "maven-stage-plugin",
        "maven-surefire-plugin",
        "maven-surefire-report-plugin",
        "maven-toolchains-plugin",
        "maven-verifier-plugin",
        "maven-war-plugin",
    };

    private static final String BASEURL = "http://maven.apache.org/plugins/";

    public String getPluginInfoUrl( String pluginName )
    {
        return BASEURL + pluginName + "/plugin-info.html";
    }

    public PluginPrerequisites getPluginPrerequisites( String pluginName )
        throws IOException
    {
        String url = getPluginInfoUrl( pluginName );

        Document doc = Jsoup.connect( url ).get();

        Elements select = doc.select( "table.bodyTable" ); // Stylus skin

        if ( select.size() < 1 )
        {
            select = doc.select( "table.table-striped" ); // Fluido skin
        }

        if ( select.size() < 1 )
        {
            System.err.println( "Could not find expected plugin info for " + url );
            return new PluginPrerequisites( pluginName, "?", "?", "?" );
        }

        Element tableInfo = select.get( 1 );
        Elements elementsByAttributeA = tableInfo.getElementsByAttributeValue( "class", "a" );
        Elements elementsByAttributeB = tableInfo.getElementsByAttributeValue( "class", "b" );
        String mavenVersion = elementsByAttributeA.first().text();
        String jdkVersion = elementsByAttributeB.first().text();
        
        //FIXME: Sometimes it happens that the indexes are swapped (I don't know why...I have to find out why...)
        if ( mavenVersion.startsWith( "JDK" ) )
        {
            String tmp = jdkVersion;
            jdkVersion = mavenVersion;
            mavenVersion = tmp;
        }

        //Leave only version part...
        mavenVersion = mavenVersion.replace( "Maven ", "" );
        jdkVersion = jdkVersion.replace( "JDK ", "" );

        String pluginVersion = doc.select( "pre" ).text();
        int index = pluginVersion.indexOf( "<version>" );
        if ( index < 0 )
        {
            pluginVersion = "";
        }
        else
        {
            pluginVersion = pluginVersion.substring( index + "<version>".length() );
            pluginVersion = pluginVersion.substring( 0, pluginVersion.indexOf( "</version>" ) );
        }

        return new PluginPrerequisites( pluginName, pluginVersion, mavenVersion, jdkVersion );
    }

    public List<PluginPrerequisites> getPrequisites()
    {
        List<PluginPrerequisites> result = new ArrayList<PluginPrerequisites>();

        for ( String pluginName : PLUGIN_NAMES )
        {
            try
            {
                result.add( getPluginPrerequisites( pluginName ) );
            }
            catch ( IOException e )
            {
                //What could happen?
                //check it...
            }
        }
        return result;
    }

    public Map<ArtifactVersion, List<PluginPrerequisites>> getGroupedPrequisites()
    {
        Map<ArtifactVersion, List<PluginPrerequisites>> result =
            new HashMap<ArtifactVersion, List<PluginPrerequisites>>();

        for ( PluginPrerequisites pluginPrerequisites : getPrequisites() )
        {
            if ( !result.containsKey( pluginPrerequisites.getMavenVersion() ) )
            {
                result.put( pluginPrerequisites.getMavenVersion(), new ArrayList<PluginPrerequisites>() );
            }
            result.get( pluginPrerequisites.getMavenVersion() ).add( pluginPrerequisites );
        }

        return result;
    }
}
