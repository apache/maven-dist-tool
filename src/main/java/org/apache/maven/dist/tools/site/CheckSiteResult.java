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

    /**
     * 
     */
    private final DistCheckSiteMojo distCheckSiteMojo;
    private String url;
    private Map<HTMLChecker, Boolean> checkMap = new HashMap<>();
    private int statusCode = -1;
    private Document document;
    private String screenshotName;

    public CheckSiteResult( DistCheckSiteMojo distCheckSiteMojo, ConfigurationLineInfo r, String version )
    {
        super( r, version );
        this.distCheckSiteMojo = distCheckSiteMojo;
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

    void getSkins( Sink sink )
    {
        if ( statusCode != DistCheckSiteMojo.HTTP_OK )
        {
            sink.text( "None" );
        }
        else 
        {
            String text = "";
            Elements htmlTag = document.select( "html " );
            for ( Element htmlTa : htmlTag )
            {
                Node n = htmlTa.previousSibling();
                if ( n instanceof Comment )
                {
                    text += ( ( Comment ) n ).getData();
                }
                else
                {
                    text += " ";
                }
            }

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
            sink.text( text.trim().replace( " |", "|" ).replace( "| ", "" ) );
            sink.verbatim_();
        }
    }

    void getOverall( Sink sink )
    {
        if ( statusCode != DistCheckSiteMojo.HTTP_OK )
        {
            this.distCheckSiteMojo.iconError( sink );
        }
        else
        {
            boolean found = false;
            for ( Map.Entry<HTMLChecker, Boolean> e : checkMap.entrySet() )
            {
                if ( e.getValue() )
                {
                    this.distCheckSiteMojo.iconSuccess( sink );
                    sink.text( ": " + e.getKey().getName() );
                    found = true;
                }
            }
            if ( !found )
            {
                this.distCheckSiteMojo.iconWarning( sink );
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
        this.document = doc ;
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