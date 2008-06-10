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

import java.io.File;
import java.io.PrintWriter;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ail.core.Core;
import com.ail.core.XMLString;
import com.ail.coretest.CoreUserTestCase;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.quotation.generatedocument.GenerateDocumentCommand;
import com.ail.openquote.Quotation;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class TestMotorPlusDocumentRender extends CoreUserTestCase {
    private static final long serialVersionUID = 2030295330203910171L;

    /**
     * Constructs a test case with the given name.
     */
    public TestMotorPlusDocumentRender(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestMotorPlusDocumentRender.class);
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        super.setupSystemProperties();
        super.setCore(new Core(this));
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    protected void tearDown() {
    }
    
    /**
     */
    public void testGenDocLocal() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusDocumentRenderOne.xml"));

        Quotation quote=getCore().fromXML(Quotation.class, quoteXml);
        quote.setStatus(PolicyStatus.QUOTATION);
                        
        GenerateDocumentCommand cmd=(GenerateDocumentCommand)getCore().newCommand("GenerateQuoteDocument");
        cmd.setPolicyArg(quote);
        cmd.invoke();

        PrintWriter out=new PrintWriter(new File("./deploy/test/TestGenerateDocument.pdf"));
        out.print(new String(cmd.getDocumentRet()));
        out.close();
    }
}
