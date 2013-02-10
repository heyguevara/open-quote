/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.ail.core.urlhandler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Functions;

/**
 * The core provides a number of URL handlers to simplify access to resources. The tests
 * defined here test those handlers. 
 */
public class TestUrlHandlers {
    
    @Before
    public void setUp() throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
    }

    /**
     * Test raw access to the product repository. 
     * @throws Exception
     */
    @Test
    public void testProdctUrlAccess() throws Exception {
        URL url=null;

        // try to access the content that does not exist
        try {
            url=new URL("product://localhost:8080/Demo/Demo/ContentThatDoesNotExist.html");
            Functions.loadUrlContentAsString(url);
            fail("got content which doesn't exist!");
        }
        catch(FileNotFoundException e) {
            // expected
        }
        catch(Throwable t) {
            fail("Caught unexpected "+t.getClass().getName());
        }

        // try to access the content that does exist
        url=new URL("product://localhost:8080/AIL/Base/HTML/ReferralNotification.html");
        Functions.loadUrlContentAsString(url);
    }

    /**
     * This test checks that a URL pointing at an existing resource correctly opens that
     * resource.
     * <ul>
     * <li>Open a stream to the URL "classpath://TestUrlContent.xml"</li>
     * <li>Count the number of bytes in available</li>
     * <li>If the count is anything by 14, fail</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testClasspathUrlHandlerGoodUrl() throws Exception {
        URL url=null;
        char[] buf=new char[18];
        
        url=new URL("classpath://com.ail.core.urlhandler/TestUrlContent.xml");
        
        InputStream in=url.openStream();
        
        InputStreamReader isr=new InputStreamReader(in); 
        isr.read(buf, 0, 18);
        
        assertEquals("<root>hello</root>", new String(buf));
    }

    /**
     * This test checks that a URL pointing at an existing resource correctly opens that
     * resource.
     * <ul>
     * <li>Open a stream to the URL "classpath://TestUrlContent.xml"</li>
     * <li>Count the number of bytes in available</li>
     * <li>If the count is anything by 14, fail</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testClasspathUrlHandlerBadUrl() throws Exception {
        URL url=null;
        
        url=new URL("classpath://com.ail.core/TestUrlContentThatDoesNotExist.xml");
        
        try {
            url.openStream();
            fail("Open a resource that doesn't exist!");
        }
        catch(FileNotFoundException e) {
            // This is what we want
        }
    }

    /**
     * Test product access to the product repository with and without defining the language.
     * @throws Exception
     */
    @Test
    public void testProductUrlAccessWithLocale() throws Exception {
        URL url=null;

         // get content without specifying a local
        com.ail.core.ThreadLocale.setThreadLocale(Locale.ENGLISH);
        url=new URL("product://localhost:8080/AIL/Base/HTML/ThankYou.html");
        assertTrue(Functions.loadUrlContentAsString(url).contains("Thank you"));

        // get content with a locale for which content is defined
        com.ail.core.ThreadLocale.setThreadLocale(Locale.GERMAN);
        url=new URL("product://localhost:8080/AIL/Base/HTML/ThankYou.html");
        assertTrue(Functions.loadUrlContentAsString(url).contains("Danke"));

        // get content with a locale for which content is not defined
        com.ail.core.ThreadLocale.setThreadLocale(Locale.KOREAN);
        url=new URL("product://localhost:8080/AIL/Base/HTML/ThankYou.html");
        assertTrue(Functions.loadUrlContentAsString(url).contains("Thank you"));
    }
}
