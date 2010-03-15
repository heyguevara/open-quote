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

package com.ail.coretest;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.Core;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.document.generatedocument.StyleDocumentCommand;

/**
 * Test the core system's XML data mapping facilities.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class TestCoreXMLStringMapping extends CoreUserTestCase {

    /**
     * Constructs a test case with the given name.
     */
    public TestCoreXMLStringMapping(String name) {
        super(name);
    }

    public static Test suite() {
		return new TestSuite(TestCoreXMLStringMapping.class);
    }

	public static void main (String[] args) {
		TestRunner.run(suite());
	}


    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() throws Exception {
        super.setupSystemProperties();
        setVersionEffectiveDate(new VersionEffectiveDate());
        tidyUpTestData();
        setCore(new Core(this));
        getCore().resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        ConfigurationHandler.reset();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    protected void tearDown() {
		tidyUpTestData();
    }

    /**
     * Objects that contains instances of XMLString need special handling if the data in the XMLString
     * is to be represented 'in line' within the XML produced by toXML. This test checks that the 
     * handling is working.
     */
    public void testSimpleToXML() throws Exception {
        StyleDocumentCommand c=(StyleDocumentCommand)getCore().newCommand("StyleDocumentCommand");

        c.setMergedDataArg(new XMLString("<hello>plop</hello>"));
        System.out.println(getCore().toXML(c));
        
        assertTrue(getCore().toXML(c).toString().contains("<hello>plop</hello>"));
    }

    public void testSimpleFromXML() throws Exception {
        XMLString input=new XMLString(
                "<styleDocumentCommand xsi:type='java:com.ail.core.document.generatedocument.StyleDocumentCommand' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"+
                "<mergedDataArg><hello>plop</hello></mergedDataArg>"+
                "</styleDocumentCommand>");
        
        StyleDocumentCommand sdc=getCore().fromXML(StyleDocumentCommand.class, input);
        assertEquals("<hello>plop</hello>", sdc.getMergedDataArg().toString());
    }
}


