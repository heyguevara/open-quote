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

package com.ail.coretest;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
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
     * Test raw access to the alfresco repository. This test uses the same method of access
     * as the alfresco content URL handler uses.
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
        url=new URL("product://localhost:8080/Demo/Demo/Welcome");
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
        
        url=new URL("classpath://com.ail.coretest/TestUrlContent.xml");
        
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
        
        url=new URL("classpath://com.ail.coretest/TestUrlContentThatDoesNotExist.xml");
        
        try {
            url.openStream();
            fail("Open a resource that doesn't exist!");
        }
        catch(FileNotFoundException e) {
            // This is what we want
        }
    }

    /**
     * Test raw access to the alfresco repository. This test uses the same method of access
     * as the alfresco content URL handler uses.
     * @throws Exception
     */
    @Test
    public void testAlfrescoUrlAccessWithoutATicket() throws Exception {
        URL url=null;

        // try to access the content without a ticket
        try {
            url=new URL("http://localhost:8080/alfresco/download/direct?path=/Company%20Home/Data%20Dictionary/Email%20Templates/invite_user_email.ftl");
            String p=Functions.loadUrlContentAsString(url);
            System.out.println("res:"+p);
            fail("got content even though we didn't pass a vaid ticket in");
        }
        catch(IOException e) {
            assertTrue("expected error to contain: 'HTTP response code: 401', but message was: '"+e.getMessage()+"'", e.getMessage().contains("HTTP response code: 401"));
        }
    }

    @Test
    public void testAlfrescoUrlAccessWithATicket() throws Exception {
        URL url=null;

        // get a ticket for admin/admin
        url=new URL("http://localhost:8080/alfresco/service/api/login?u=admin&pw=admin");
        String rawTicketResponse=Functions.loadUrlContentAsString(url);        
        assertTrue(rawTicketResponse.indexOf("<ticket>")>0);
        String ticket=rawTicketResponse.substring(rawTicketResponse.indexOf("<ticket>")+8, rawTicketResponse.lastIndexOf("<"));

        // try to access the content again, but with a ticket
        url=new URL("http://localhost:8080/alfresco/download/direct?path=/Company%20Home/Data%20Dictionary/Email%20Templates/invite_user_email.ftl&ticket="+ticket);
        Functions.loadUrlContentAsString(url);
    }

    /**
     * Test raw access to the alfresco repository with and without defining the language. This test uses the same method of access
     * as the alfresco content URL handler uses.
     * @throws Exception
     */
    @Test
    public void testAlfrescoUrlAccessWithLocale() throws Exception {
        URL url=null;

        // get a ticket for admin/admin
        url=new URL("http://localhost:8080/alfresco/service/api/login?u=admin&pw=admin");
        String rawTicketResponse=Functions.loadUrlContentAsString(url);        
        assertTrue(rawTicketResponse.indexOf("<ticket>")>0);
        String ticket=rawTicketResponse.substring(rawTicketResponse.indexOf("<ticket>")+8, rawTicketResponse.lastIndexOf("<"));

        // get content without specifying a local
        url=new URL("http://localhost:8080/alfresco/download/direct?path=/Company%20Home/Product/Demo/Demo/Welcome&ticket="+ticket);
        assertEquals("   Hello World!", Functions.loadUrlContentAsString(url));

        // get content with a locale for which content is defined
        url=new URL("http://localhost:8080/alfresco/download/direct?path=/Company%20Home/Product/Demo/Demo/Welcome&ticket="+ticket+"&language=de");
        assertEquals("Hallo Welt!", Functions.loadUrlContentAsString(url));

        // get content with a locale for which content is not defined
        url=new URL("http://localhost:8080/alfresco/download/direct?path=/Company%20Home/Product/Demo/Demo/Welcome&ticket="+ticket+"&language=fr");
        assertEquals("   Hello World!", Functions.loadUrlContentAsString(url));
    }

    /**
     * Test product access to the alfresco repository with and without defining the language.
     * @throws Exception
     */
    @Test
    public void testProductUrlAccessWithLocale() throws Exception {
        URL url=null;

         // get content without specifying a local
        com.ail.core.Locale.setThreadLocale(Locale.ENGLISH);
        url=new URL("product://localhost:8080/Demo/Demo/Welcome");
        assertEquals("   Hello World!", Functions.loadUrlContentAsString(url));

        // get content with a locale for which content is defined
        com.ail.core.Locale.setThreadLocale(Locale.GERMAN);
        url=new URL("product://localhost:8080/Demo/Demo/Welcome");
        assertEquals("Hallo Welt!", Functions.loadUrlContentAsString(url));

        // get content with a locale for which content is not defined
        com.ail.core.Locale.setThreadLocale(Locale.FRENCH);
        url=new URL("product://localhost:8080/Demo/Demo/Welcome");
        assertEquals("   Hello World!", Functions.loadUrlContentAsString(url));
    }
}
