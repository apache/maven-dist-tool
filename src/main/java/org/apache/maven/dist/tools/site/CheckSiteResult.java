package org.apache.maven.dist.tools.site;

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

import java.util.HashMap;
import java.util.Map;

import org.apache.maven.dist.tools.AbstractCheckResult;
import org.apache.maven.dist.tools.AbstractDistCheckReport;
import org.apache.maven.dist.tools.ConfigurationLineInfo;
import org.apache.maven.doxia.sink.Sink;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

class CheckSiteResult
    extends AbstractCheckResult
{
    private String url;

    private Map<HTMLChecker, Boolean> checkMap = new HashMap<>();

    private int statusCode = -1;

    private String comment;

    private String screenshotName;

    CheckSiteResult( ConfigurationLineInfo r, String version )
    {
        super( r, version );
    }

    void setUrl( String url )
    {
        this.url = url;
    }

    /**
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * @return the checkMap
     */
    public Map<HTMLChecker, Boolean> getCheckMap()
    {
        return checkMap;
    }

    void setHTTPErrorUrl( int status )
    {
        this.statusCode = status;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode()
    {
        return statusCode;
    }

    void renderDetectedSkin( Sink sink )
    {
        if ( statusCode != DistCheckSiteReport.HTTP_OK )
        {
            sink.text( "None" );
        }
        else 
        {
            sink.text( "skin: " );
            if ( isSkin( "Fluido" ) )
            {
                sink.text( "Fluido" );
            }
            else if ( isSkin( "Stylus" ) )
            {
                sink.text( "Stylus" );
            }
            else 
            {
                sink.text( "Not determined" );
            }
            sink.verbatim( null );
            sink.text( comment.trim().replace( " |", "|" ).replace( "| ", "" ) );
            sink.verbatim_();
        }
    }

    void renderDisplayedArtifactVersion( Sink sink )
    {
        if ( statusCode != DistCheckSiteReport.HTTP_OK )
        {
            AbstractDistCheckReport.iconError( sink );
        }
        else
        {
            boolean found = false;
            for ( Map.Entry<HTMLChecker, Boolean> e : checkMap.entrySet() )
            {
                if ( e.getValue() )
                {
                    AbstractDistCheckReport.iconSuccess( sink );
                    sink.text( ": " + e.getKey().getName() );
                    found = true;
                }
            }
            if ( !found )
            {
                AbstractDistCheckReport.iconWarning( sink );
                sink.text( ": artifact version not found" );
            }
        }
    }

    private boolean isSkin( String skinName )
    {
        boolean tmp = false;
        for ( Map.Entry<HTMLChecker, Boolean> e : checkMap.entrySet() )
        {
            if ( e.getKey().getSkin().equals( skinName ) )
            {
                tmp = tmp || e.getValue();
            }
        }
        return tmp;
    }

    void setDocument( Document doc )
    {
        comment = extractComment( doc );
        statusCode = ( doc == null ) ? -1 : DistCheckSiteReport.HTTP_OK;
    }

    static String extractComment( Document document )
    {
        String text = "";
        Elements htmlTags = document.select( "html " );
        for ( Element htmlTag : htmlTags )
        {
            Node n = htmlTag.previousSibling();
            if ( n instanceof Comment )
            {
                text += ( ( Comment ) n ).getData();
            }
            else
            {
                text += " ";
            }
        }
        return text;
    }

    void setScreenShot( String fileName )
    {
        this.screenshotName = fileName;
    }
    String getScreenShot()
    {
        return screenshotName;
    }
}