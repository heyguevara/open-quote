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

import static com.ail.insurance.policy.PolicyStatus.SUBMITTED;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.Functions;
import com.ail.core.XMLString;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.quotation.NotifyPartyService.NotifyPartyCommand;
import com.ail.insurance.policy.Policy;

public class TestNotify extends CoreUserBaseCase {
    private static final long serialVersionUID = 2030295330203910171L;
    String configurationNamespace;
    
    
    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    @SuppressWarnings("unchecked")
    public void tearDown() throws Exception {
        // Emails are send async, so give the system a chance to process the emails before we delete 'em!
        Thread.sleep(10000);
        for(SavedPolicy sq: (List<SavedPolicy>)getCore().query("get.savedPolicy.by.quotationNumber", "TESTONE")) {
            getCore().delete(sq);
            System.out.println("Deleted quote systemId: "+sq.getSystemId());
        }

        for(SavedPolicy sq: (List<SavedPolicy>)getCore().query("get.savedPolicy.by.quotationNumber", "TESTTWO")) {
            getCore().delete(sq);
            System.out.println("Deleted quote systemId: "+sq.getSystemId());
        }
        
        for(SavedPolicy sq: (List<SavedPolicy>)getCore().query("get.savedPolicy.by.quotationNumber", "TESTTHREE")) {
            getCore().delete(sq);
            System.out.println("Deleted quote systemId: "+sq.getSystemId());
        }
    }
    
    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() throws Exception {
        super.setupSystemProperties();
        super.setCore(new Core(this));

        getCore().openPersistenceSession();

        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestNotifyQuotationOne.xml"));
        Policy quote=getCore().fromXML(Policy.class, quoteXml);
        SavedPolicy sq=getCore().create(new SavedPolicy(quote));
        System.out.println("Created quote systemId: "+sq.getSystemId());
        
        quoteXml = new XMLString(this.getClass().getResourceAsStream("TestNotifyQuotationTwo.xml"));
        quote=getCore().fromXML(Policy.class, quoteXml);
        sq=getCore().create(new SavedPolicy(quote));
        System.out.println("Created quote systemId: "+sq.getSystemId());

        quoteXml = new XMLString(this.getClass().getResourceAsStream("TestNotifyQuotationOne.xml"));
        quote=getCore().fromXML(Policy.class, quoteXml);
        quote.setQuotationNumber("TESTTHREE");
        quote.setStatus(SUBMITTED);
        sq=getCore().create(new SavedPolicy(quote));
        System.out.println("Created quote systemId: "+sq.getSystemId());

        getCore().closePersistenceSession();

        setConfigurationNamespace(Functions.productNameToConfigurationNamespace(quote.getProductTypeId()));
    }

    @Test
    public void testSendNotifyEmail() throws Exception {
        NotifyPartyCommand cmd=getCore().newCommand(NotifyPartyCommand.class);
        cmd.setQuotationNumberArg("TESTONE");
        cmd.invoke();
   }

    @Test
    public void testSendReferNotifyEmail() throws Exception {
        NotifyPartyCommand cmd=getCore().newCommand(NotifyPartyCommand.class);
        cmd.setQuotationNumberArg("TESTTWO");
        cmd.invoke();
   }

    @Test
    public void testSendSubmittedNotifyEmail() throws Exception {
        NotifyPartyCommand cmd=getCore().newCommand(NotifyPartyCommand.class);
        cmd.setQuotationNumberArg("TESTTHREE");
        cmd.invoke();
   }

    public String getConfigurationNamespace() {
        return configurationNamespace;
    }

    public void setConfigurationNamespace(String configurationNamespace) {
        this.configurationNamespace = configurationNamespace;
    }
}
