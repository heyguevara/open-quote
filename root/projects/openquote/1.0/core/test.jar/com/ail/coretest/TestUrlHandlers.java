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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * The core provides a number of URL handlers to simplify access to resources. The tests
 * defined here test those handlers. 
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:24 $
 * @source $Source:
 *         /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestTypeXpath.java,v $
 */
public class TestUrlHandlers extends TestCase {

    /** Constructs a test case with the given name. */
    public TestUrlHandlers(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestUrlHandlers.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    protected void setUp() throws Exception {
        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
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
}
