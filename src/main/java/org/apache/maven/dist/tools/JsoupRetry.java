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

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * Reads a url with Jsoup, retrying multiple times in case of IOException.
 */
public class JsoupRetry
{
    public static final int MAX_RETRY = 3;

    public static final int WAIT_RETRY_SECONDS = 10;

    public static Document get( String url )
        throws IOException
    {
        for ( int i = 1; i <= MAX_RETRY; i++ )
        {
            try
            {
                return Jsoup.connect( url ).get();
            }
            catch ( IOException ioe )
            {
                System.err.println( "IOException try " + i + " while reading " + url + ": " + ioe.getClass() + " "
                    + ioe.getMessage() );

                if ( i == MAX_RETRY )
                {
                    throw new IOException( "IOException while reading " + url, ioe );
                }
            }

            try
            {
                Thread.sleep( WAIT_RETRY_SECONDS * 1000 );
            }
            catch ( InterruptedException e )
            {
                // not expected to happen
            }
        }

        return null;
    }
}
