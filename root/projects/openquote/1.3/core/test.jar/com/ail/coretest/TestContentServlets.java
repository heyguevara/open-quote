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

import static com.ail.core.Functions.loadUrlContentAsString;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * The core provides servlets giving access to content in CMS. The tests defined in the test case check that the servlets are
 * present and operating as expected.
 * 
 * @version $Revision$
 * @date $Date$
 * @source $Source: /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestTypeXpath.java,v $
 */
public class TestContentServlets extends TestCase {

    /** Constructs a test case with the given name. */
    public TestContentServlets(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestContentServlets.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * "Guest" servlets provide access to content without needing authentication. This test
     * checks that we can access content using a node-reference.
     * @throws MalformedURLException
     * @throws IOException
     */
    public void testGuestServletsContentExistsNodeRef() throws MalformedURLException, IOException {
        String content;
        content=loadUrlContentAsString(new URL("http://localhost:8080/alfresco/d/d/workspace/SpacesStore/fc6d1198-fc28-11dc-b2b2-4798120be4e3/Alfresco-Tutorial.pdf"));
        assertEquals("Wrong number of characters returned.", 4351571, content.length());
    }

    /**
     * "Guest" servlets provide access to content without needing authentication. This test
     * checks that a reference to content using a node-reference that does not exist throws
     * the anticipated exception.
     * @throws MalformedURLException
     * @throws IOException
     */
    public void testGuestServletsContentNotExistsNodeRef() throws MalformedURLException, IOException {
        try {
            loadUrlContentAsString(new URL("http://localhost:8080/alfresco/guestDownload/direct/workspace/SpacesStore/a4b5f79fd-dec3-11dc-ae45-833fd23b6aba/ThingThatDoesNotExist.pdf"));
            fail("Downloaded content that doesn't exist!");
        }
        catch(IOException e) {
            // this is what we expect
        }
    }

    /**
     * "Guest" servlets provide access to content without needing authentication. This test
     * checks that we can access content using a path URL.
     * @throws MalformedURLException
     * @throws IOException
     */
    public void testGuestServletsContentExistsPathRef() throws MalformedURLException, IOException {
        String content;
        
        // access some content that does exist using path
        content=loadUrlContentAsString(new URL("http://localhost:8080/alfresco/guestDownload/direct?path=/Company%20Home/Guest%20Home/Alfresco-Tutorial.pdf"));
        assertEquals("Wrong number of characters returned.", 4351571, content.length());
    }

    /**
     * "Guest" servlets provide access to content without needing authentication. This test
     * checks that attempting to access content which does not exist using a path reference
     * throws the expected exception.
     * @throws MalformedURLException
     * @throws IOException
     */
    public void testGuestServletsContentNotExistPathRef() throws MalformedURLException, IOException {
        try {
            loadUrlContentAsString(new URL("http://localhost:8080/alfresco/guestDownload/direct?path=/Company%20Home/Guest%20Home/ThingThatDoesNotExist.pdf"));
            fail("Downloaded content that doesn't exist!");
        }
        catch(IOException e) {
            // this is what we expect
        }
    }
}
