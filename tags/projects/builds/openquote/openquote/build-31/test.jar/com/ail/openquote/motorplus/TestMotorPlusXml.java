/* Copyright Applied Industrial Logic Limited 2006. All rights reserved. */
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

package com.ail.openquote.motorplus;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.ail.core.CoreProxy;
import com.ail.core.XMLString;
import com.ail.openquote.Quotation;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/08/28 17:36:36 $
 * @source $Source: /home/bob/CVSRepository/projects/multiquote/test.jar/com/ail/multiquote/motorplus/TestMotorPlusXml.java,v $
 */
public class TestMotorPlusXml extends TestCase {
    private static final long serialVersionUID = 2030295330203910171L;

    private CoreProxy core = null;

    /**
     * Constructs a test case with the given name.
     */
    public TestMotorPlusXml(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestMotorPlusXml.class);
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        System.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url","jnp://localhost:1099");
        System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser");
        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");

        System.setProperty("com.ail.core.configure.loader", "com.ail.core.configure.JDBCConfigurationLoader");
        System.setProperty("com.ail.core.configure.loaderParam.driver", "org.gjt.mm.mysql.Driver");
        System.setProperty("com.ail.core.configure.loaderParam.url", "jdbc:mysql://localhost:3306/openquote_1_0");
        System.setProperty("com.ail.core.configure.loaderParam.table", "config");
        System.setProperty("com.ail.core.configure.loaderParam.user", "openquote");
        System.setProperty("com.ail.core.configure.loaderParam.password", "openquote");
        core = new CoreProxy();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    protected void tearDown() {
    }

    
    /**
     * Test that a quotation with an assessment sheet marshals okay.
     * @throws Exception
     */
    public void testAssessmentSheetMarshal() throws Exception {
        System.out.println("test1");
        try {
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteSix.xml"));
            Quotation q=core.fromXML(Quotation.class, quoteXml);
            assertNotNull(q.getExpiryDate());
        }
        catch(Throwable t) {
            t.printStackTrace();
            fail("marshal failed");
        }
    }
}
