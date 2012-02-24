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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.rmi.RemoteException;

import javax.ejb.CreateException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.CoreUserBaseCase;
import com.ail.core.XMLString;
import com.ail.insurance.quotation.Quotation;
import com.ail.insurance.quotation.QuotationHome;

public class TestQuotationEjbXml extends CoreUserBaseCase {
    private static final long serialVersionUID = 1L;

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        resetConfigurations();
    }

    private Quotation getBean() throws NamingException, RemoteException, CreateException {
        setupSystemProperties();
        Context context=new InitialContext();
        QuotationHome home=(QuotationHome)context.lookup("Quotation");
        Quotation bean=(Quotation)PortableRemoteObject.narrow(home.create(), Quotation.class);

        return bean;
    }

    @Test
    public void testSimplAssessRisk() throws Exception {
        String arg=
            "<assessRisk xsi:type=\"java:com.ail.insurance.quotation.AssessRiskArgumentImpl\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
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
