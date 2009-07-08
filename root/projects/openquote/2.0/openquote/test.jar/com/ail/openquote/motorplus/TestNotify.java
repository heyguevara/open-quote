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

import java.util.List;

import static com.ail.insurance.policy.PolicyStatus.SUBMITTED;

import junit.framework.Test;
import junit.framework.TestSuite;

import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.XMLString;
import com.ail.coretest.CoreUserTestCase;
import com.ail.insurance.quotation.notifyparty.NotifyPartyCommand;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class TestNotify extends CoreUserTestCase {
    private static final long serialVersionUID = 2030295330203910171L;

    /**
     * Constructs a test case with the given name.
     */
    public TestNotify(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestNotify.class);
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @SuppressWarnings("unchecked")
    protected void tearDown() throws Exception {
        // Emails are send async, so give the system a chance to process the emails before we delete 'em!
        Thread.sleep(10000);
        for(SavedQuotation sq: (List<SavedQuotation>)getCore().query("get.savedQuotation.by.quotationNumber", "TESTONE")) {
            getCore().delete(sq);
            System.out.println("Deleted quote systemId: "+sq.getSystemId());
        }

        for(SavedQuotation sq: (List<SavedQuotation>)getCore().query("get.savedQuotation.by.quotationNumber", "TESTTWO")) {
            getCore().delete(sq);
            System.out.println("Deleted quote systemId: "+sq.getSystemId());
        }
        
        for(SavedQuotation sq: (List<SavedQuotation>)getCore().query("get.savedQuotation.by.quotationNumber", "TESTTHREE")) {
            getCore().delete(sq);
            System.out.println("Deleted quote systemId: "+sq.getSystemId());
        }
    }
    
    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() throws Exception {
        super.setupSystemProperties();
        super.setCore(new Core(this));

        getCore().openPersistenceSession();

        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestNotifyQuotationOne.xml"));
        Quotation quote=getCore().fromXML(Quotation.class, quoteXml);
        SavedQuotation sq=getCore().create(new SavedQuotation(quote));
        System.out.println("Created quote systemId: "+sq.getSystemId());
        
        quoteXml = new XMLString(this.getClass().getResourceAsStream("TestNotifyQuotationTwo.xml"));
        quote=getCore().fromXML(Quotation.class, quoteXml);
        sq=getCore().create(new SavedQuotation(quote));
        System.out.println("Created quote systemId: "+sq.getSystemId());

        quoteXml = new XMLString(this.getClass().getResourceAsStream("TestNotifyQuotationOne.xml"));
        quote=getCore().fromXML(Quotation.class, quoteXml);
        quote.setQuotationNumber("TESTTHREE");
        quote.setStatus(SUBMITTED);
        sq=getCore().create(new SavedQuotation(quote));
        System.out.println("Created quote systemId: "+sq.getSystemId());

        getCore().closePersistenceSession();
        
        setConfigurationNamespace(Functions.productNameToConfigurationNamespace(quote.getProductTypeId()));
    }

    /**
     */
    public void testSendNotifyEmail() throws Exception {
        NotifyPartyCommand cmd=(NotifyPartyCommand)getCore().newCommand("SendNotification");
        cmd.setQuotationNumberArg("TESTONE");
        cmd.invoke();
   }

    public void testSendReferNotifyEmail() throws Exception {
        NotifyPartyCommand cmd=(NotifyPartyCommand)getCore().newCommand("SendNotification");
        cmd.setQuotationNumberArg("TESTTWO");
        cmd.invoke();
   }

    public void testSendSubmittedNotifyEmail() throws Exception {
        NotifyPartyCommand cmd=(NotifyPartyCommand)getCore().newCommand("SendNotification");
        cmd.setQuotationNumberArg("TESTTHREE");
        cmd.invoke();
   }
}
