/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

package com.ail.core.xmlbinding;


import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.document.StyleDocumentService.StyleDocumentCommand;
import com.ail.core.document.StyleDocumentCommandImpl;
import com.ail.core.CoreUserBaseCase;

/**
 * Test the core system's XML data mapping facilities.
 */
public class TestCoreXMLStringMapping extends CoreUserBaseCase {

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() throws Exception {
        super.setupSystemProperties();
        setVersionEffectiveDate(new VersionEffectiveDate());
        tidyUpTestData();
        setCore(new Core(this));
        getCore().resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        ConfigurationHandler.resetCache();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
		tidyUpTestData();
    }

    /**
     * Objects that contain instances of XMLString need special handling if the data in the XMLString
     * is to be represented 'in line' within the XML produced by toXML. This test checks that the 
     * handling is working.
     */
    @Test
    public void testSimpleToXML() throws Exception {
        StyleDocumentCommand c=new StyleDocumentCommandImpl();

        c.setMergedDataArg(new XMLString("<hello>plop</hello>"));
        System.out.println(getCore().toXML(c));
        
        assertTrue(getCore().toXML(c).toString().contains("<hello>plop</hello>"));
    }

    @Test
    public void testSimpleFromXML() throws Exception {
        XMLString input=new XMLString(
                "<styleDocumentCommand xsi:type='java:com.ail.core.document.StyleDocumentCommandImpl' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"+
                "<mergedDataArg><hello>plop</hello></mergedDataArg>"+
                "</styleDocumentCommand>");
        
        StyleDocumentCommand sdc=getCore().fromXML(StyleDocumentCommandImpl.class, input);
        assertEquals("<hello>plop</hello>", sdc.getMergedDataArg().toString());
    }
}


