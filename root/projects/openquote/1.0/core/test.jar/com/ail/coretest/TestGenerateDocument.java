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

import java.io.File;
import java.io.PrintWriter;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.server.ServerBean;
import com.ail.core.document.generatedocument.GenerateDocumentCommand;
import com.ail.core.document.generatedocument.StyleDocumentArgImp;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.core.document.model.RenderContext;

/**
 * @version $Revision: 1.11 $
 * @state $State: Exp $
 * @date $Date: 2007/04/15 22:18:33 $
 * @source $Source: /home/bob/CVSRepository/projects/core/test.jar/com/ail/coretest/TestGenerateDocument.java,v $
 */
public class TestGenerateDocument extends CoreUserTestCase {
    static boolean setup=false;
    
    /**
     * Constructs a test case with the given name.
     */
    public TestGenerateDocument(String name) {
        super(name);
    }

    public static Test suite() {
		return new TestSuite(TestGenerateDocument.class);
    }

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        tidyUpTestData();
        setupSystemProperties();
        setCore(new Core(this));
        if (!setup) {
            System.out.println("setup");
            ConfigurationHandler.reset();
            getCore().resetConfiguration();
            new ServerBean().resetNamedConfiguration("all");
            resetConfiguration();
            ConfigurationHandler.reset();
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
    protected void tearDown() {
		tidyUpTestData();
    }

    
    /**
     * Test that the Doc Gen service fails if no arguments are supplied.
     * @throws Exception
     */
    public void testDocGenServiceExists() throws Exception {
        try {
            GenerateDocumentCommand com=(GenerateDocumentCommand)getCore().newCommand("GenerateQuoteDocument");
            com.invoke();
            fail("command worked even without giving it any args!");
        }
        catch(PreconditionException  e) {
            // this is ok
        }
     }

    /**
     * Test that a simple document definition can be marshalled/unmarshalled.
     * @throws Exception
     */
    public void testDocRenderModel() throws Exception {
        XMLString docDefXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentModelOne.xml"));
        DocumentDefinition def=getCore().fromXML(DocumentDefinition.class, docDefXml);
        getCore().toXML(def);
    }

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
        System.out.println(sw.toString());
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
    
    public void testTypeConversion() throws Exception {
        String tin= "<?xml version='1.0' encoding='UTF-8'?>\n"+
                    "<styleDocumentArgImp xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' xsi:type='java:com.ail.core.document.generatedocument.StyleDocumentArgImp'>"+
                      "<styledDocumentRet>"+
                        "<something with='an attribute'>but not much more.</something>"+
                      "</styledDocumentRet>"+
                    "</styleDocumentArgImp>";
        
        getCore().fromXML(StyleDocumentArgImp.class, new XMLString(tin));
        
    }
    
    public void testDocGenService() throws Exception {
        PrintWriter out;

        try {
            // we'll use an instance of Configuration to source some data from.
            XMLString configXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentDefaultConfig.xml"));
            Configuration config=getCore().fromXML(Configuration.class, configXml);
    
            for(int i=0 ; i<10 ; i++) {
                Timer.start("gen test "+i);        
                String doc = generateDocument(config);
                Timer.stop("gen test "+i);
            
                out=new PrintWriter(new File("./deploy/test/TestGenerateDocument"+i+".pdf"));
                out.print(doc);
                out.close();
            }
        }
        catch(Throwable e) {
            e.printStackTrace();
            fail("Exception thrown during doc gen.");
        }
    }

    private String generateDocument(Configuration config) throws BaseException {
        GenerateDocumentCommand com=(GenerateDocumentCommand)getCore().newCommand("GenerateQuoteDocument");
        com.setModelArg(config);
        com.setProductNameArg("com.ail.core.product.TestProduct1");
        com.setDocumentDefinitionArg("MyTestDocument");
        com.invoke();
        String doc=new String(com.getRenderedDocumentRet());
        return doc;
    }

    public void testGenerateDocumentFromCore() throws Exception {
        XMLString configXml = new XMLString(this.getClass().getResourceAsStream("TestGenerateDocumentDefaultConfig.xml"));
        Configuration model=getCore().fromXML(Configuration.class, configXml);

        byte[] doc=getCore().generateDocument("com.ail.core.product.TestProduct1", "MyTestDocument", model);
        
//        PrintWriter out=new PrintWriter(new File("./deploy/test/testGenerateDocumentFromCore.pdf"));
//        out.print(new String(doc));
//        out.close();

        assertNotNull(doc);
        assertEquals(4736, doc.length);
        
    }
}
