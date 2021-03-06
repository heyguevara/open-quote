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

package com.ail.core.document;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.TypeXPathFunctionRegister;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.Configuration;
import com.ail.core.document.GenerateDocumentService.GenerateDocumentCommand;
import com.ail.core.document.StyleDocumentArgumentImpl;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.core.document.model.RenderContext;

public class TestGenerateDocument extends CoreUserBaseCase {
    static boolean setup=false;
    
    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        tidyUpTestData();
        setupSystemProperties();
        setCore(new Core(this));
        if (!setup) {
            resetConfigurations();
            TypeXPathFunctionRegister.getInstance().reloadFunctionLibrary();
            setup=true;
        }
    }
    
    /**
     * Always select the latest configurations
     * @return
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
		tidyUpTestData();
    }

    /**
     * Test that the Doc Gen service fails if no arguments are supplied.
     * @throws Exception
     */
    @Test(expected=com.ail.core.PreconditionException.class)
    public void testDocGenServiceExists() throws Exception {
        GenerateDocumentCommand com=getCore().newCommand(GenerateDocumentCommand.class);
        com.invoke();
        fail("command worked even without giving it any args!");
     }

    /**
     * Test that a simple document definition can be marshalled/unmarshalled.
     * @throws Exception
     */
    @Test
    public void testDocRenderModel() throws Exception {
        XMLString docDefXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentModelOne.xml"));
        DocumentDefinition def=getCore().fromXML(DocumentDefinition.class, docDefXml);
        getCore().toXML(def);
    }

    @Test
    public void testDocRenderData() throws Exception {
        // Get an object to render data from
        XMLString configXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentDefaultConfig.xml"));
        Configuration config=getCore().fromXML(Configuration.class, configXml);

        // Get the definition of how/what to render
        XMLString docDefXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentModelTwo.xml"));
        DocumentDefinition def=getCore().fromXML(DocumentDefinition.class, docDefXml);

        StringWriter sw=new StringWriter();
        RenderContext context=new RenderContext(new PrintWriter(sw), config);
        def.getDocumentData().render(context);

        // check that variable expansion is working in the urlData handler
        assertTrue(sw.toString().contains("please contact us on TestProduct default"));

        // check the the various flavours of model binding and defaulting have worked
        assertTrue(sw.toString().contains("<itemData id=\"nm1\" title=\"Timeout\" class=\"integer\">600000</itemData>"));
        assertTrue(sw.toString().contains("<itemData id=\"nm1\" title=\"Who\" class=\"string\">factory</itemData>"));
        assertTrue(sw.toString().contains("<itemData id=\"nm1\" title=\"Version\" class=\"string\">"));
        assertTrue(sw.toString().contains("<itemData id=\"nm2\" title=\"Not configuration Name\" class=\"string\">Default</itemData>"));
        assertTrue(sw.toString().contains("<itemData id=\"nm3\" title=\"Default configuration Name\" class=\"string\">Default</itemData>"));
        assertTrue(sw.toString().contains("<itemData id=\"nm4\" title=\"Bad configuration Name\" class=\"string\">undefined:/niim</itemData>"));
        
        // check that the repeating data section has all the rows it should have
        assertTrue(sw.toString().contains("Thing One"));
        assertTrue(sw.toString().contains("2000"));
        assertTrue(sw.toString().contains("Thing Two"));
        assertTrue(sw.toString().contains("House"));
        assertTrue(sw.toString().contains("Thing Three"));
        assertTrue(sw.toString().contains("A long windy road"));
        assertTrue(sw.toString().contains("Thing Three and a half"));
        assertTrue(sw.toString().contains("Toad in the hole"));
    }
    
    @Test
    public void testTypeConversion() throws Exception {
        String tin= "<?xml version='1.0' encoding='UTF-8'?>\n"+
                    "<styleDocumentArgumentImpl xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:type='java:com.ail.core.document.StyleDocumentArgumentImpl'>"+
                      "<styledDocumentRet>"+
                        "<something with='an attribute'>but not much more.</something>"+
                      "</styledDocumentRet>"+
                    "</styleDocumentArgumentImpl>";
        
        getCore().fromXML(StyleDocumentArgumentImpl.class, new XMLString(tin));
    }
    
    @Test
    public void testFOPDocGenService() throws Exception {
        // we'll use an instance of Configuration to source some data from.
        XMLString configXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentDefaultConfig.xml"));
        Configuration config=getCore().fromXML(Configuration.class, configXml);

        GenerateDocumentCommand com=getCore().newCommand(GenerateDocumentCommand.class);
        com.setModelArg(config);
        com.setProductNameArg("com.ail.core.product.TestProduct01");
        com.setDocumentDefinitionArg("MyTestFOPDocument");
        com.invoke();
    }

    @Test
    public void testGenerateDocumentFromCore() throws Exception {
        XMLString configXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentDefaultConfig.xml"));
        Configuration model=getCore().fromXML(Configuration.class, configXml);

        byte[] doc=getCore().generateDocument("com.ail.core.product.TestProduct01", "MyTestFOPDocument", model);
        
        // uncomment to dump the pdf to a file
        // PrintWriter out=new PrintWriter(new File("./target/test/testGenerateDocumentFromCore.pdf"));
        // out.print(new String(doc));
        // out.close();

        assertNotNull(doc);
        assertTrue("Size of generated document doesn't look right (was: "+doc.length+", expected > 4700)", doc.length>4700);
    }
    
    @Test
    public void testITextDocGenService() throws Exception {
        // we'll use an instance of Configuration to source some data from.
        XMLString configXml = new XMLString(getClass().getResourceAsStream("TestGenerateDocumentDefaultConfig.xml"));
        Configuration config=getCore().fromXML(Configuration.class, configXml);

        GenerateDocumentCommand com=getCore().newCommand(GenerateDocumentCommand.class);
        com.setModelArg(config);
        com.setProductNameArg("com.ail.core.product.TestProduct01");
        com.setDocumentDefinitionArg("MyTestITextDocument");
        com.invoke();

        byte[] doc=com.getRenderedDocumentRet();
        
        // uncomment to dump the pdf to a file
        PrintWriter out=new PrintWriter(new File("./target/test/output.pdf"));
        out.print(new String(doc));
        out.close();

        assertNotNull(doc);
        assertTrue("Size of generated document doesn't look right (was: "+doc.length+", expected > 4700)", doc.length>4700);
    }
}
