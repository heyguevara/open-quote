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

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.XMLString;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.quotation.GenerateQuoteService.GenerateDocumentCommand;
import com.ail.openquote.Quotation;

public class TestMotorPlusDocumentRender extends CoreUserBaseCase {
    private static final long serialVersionUID = 2030295330203910171L;

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        super.setupSystemProperties();
        super.setCore(new Core(this));
    }
    
    @Test
    public void testGenDocLocal() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusDocumentRenderOne.xml"));

        Quotation quote=getCore().fromXML(Quotation.class, quoteXml);
        quote.setStatus(PolicyStatus.QUOTATION);
                        
        GenerateDocumentCommand cmd=getCore().newCommand(GenerateDocumentCommand.class);
        cmd.setPolicyArg(quote);
        cmd.invoke();

        PrintWriter out=new PrintWriter(new File("./target/test/TestGenerateDocument.pdf"));
        out.print(new String(cmd.getDocumentRet()));
        out.close();
    }
}
