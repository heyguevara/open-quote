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

package com.ail.insurancetest;

import static org.junit.Assert.*;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.XMLString;
import com.ail.core.product.listproducts.ListProductsService;
import com.ail.core.product.resetallproducts.ResetAllProductsService;
import com.ail.insurance.quotation.Quotation;
import com.ail.insurance.quotation.QuotationBean;
import com.ail.insurance.quotation.QuotationHome;
import com.ail.insurance.quotation.calculatepremium.CalculatePremiumService;

public class TestQuotationEjbXml {

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        new QuotationBean().resetConfiguration();
        new ListProductsService().resetConfiguration();
        new ResetAllProductsService().resetConfiguration();
        new CalculatePremiumService().resetConfiguration();
    }

    private Quotation getBean() throws NamingException, RemoteException, CreateException {
        Context context=new InitialContext();
        QuotationHome home=(QuotationHome)context.lookup("Quotation");
        Quotation bean=(Quotation)PortableRemoteObject.narrow(home.create(), Quotation.class);

        return bean;
    }

    @Test
    public void testSimplAssessRisk() throws Exception {
        String arg=
            "<assessRisk xsi:type=\"java:com.ail.insurance.quotation.assessrisk.AssessRiskArgumentImpl\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
                "<policyArgRet xsi:type=\"java:com.ail.insurance.policy.Policy\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
                    "<status>APPLICATION</status>"+
                    "<productTypeId>com.ail.core.product.TestProduct1</productTypeId>"+
                    "<section sectionTypeId='TestSectionID' id='sec1'/>"+
                "</policyArgRet>"+
                "<callersCore xsi:type='java:com.ail.core.CoreUserImpl' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'>"+
                    "<configurationNamespace>TESTNAMESPACE</configurationNamespace>"+
                    "<versionEffectiveDate time='%EFFECTIVEDATE%'/>"+
                "</callersCore>"+
            "</assessRisk>";

        arg=arg.replaceAll("%EFFECTIVEDATE%", Long.toString(System.currentTimeMillis()));
        
        String ret=getBean().invokeServiceXML(arg);

        assertNotNull(ret);

        XMLString xml=new XMLString(ret);
        System.out.println(xml);

        // check a few key fields in the result
        assertEquals("1", xml.evalCommand("count(/assessRiskArgumentImpl/policyArgRet/assessmentSheet)"));
        assertEquals("LOAD", xml.eval("/assessRiskArgumentImpl/policyArgRet/section[1]/assessmentSheet/assessmentList[1]/value/type/text()"));
        assertEquals("50%", xml.eval("/assessRiskArgumentImpl/policyArgRet/section[1]/assessmentSheet/assessmentList[1]/value/rate/rate/text()"));
    }

}
