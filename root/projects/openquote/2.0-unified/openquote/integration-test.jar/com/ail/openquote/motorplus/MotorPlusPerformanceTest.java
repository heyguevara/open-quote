/* Copyright Applied Industrial Logic Limited 2007. All rights reserved. */
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

import java.io.InputStream;
import java.util.HashMap;

import com.ail.core.CoreProxy;
import com.ail.core.XMLString;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.quotation.CalculatePremiumService.CalculatePremiumCommand;
import com.ail.insurance.policy.Section;
import com.ail.insurance.policy.Policy;

/**
 * This class contains a number of test methods used in performance testing with grinder.
 */
public class MotorPlusPerformanceTest {
    private static int issueCount=0;
    private static CoreProxy core=null;
    private static HashMap<Thread, TestQuotation> quoteByThread=new HashMap<Thread, TestQuotation>();
    
    static {
        System.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url", "jnp://localhost:1099");
        System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser");
        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
    }

    /**
     * Fetch the quotation assigned to this thread for testing.
     * @return
     */
    private TestQuotation getQuoteToProcess() {
        TestQuotation res=null;
        
        if (quoteByThread.containsKey(Thread.currentThread())) {
            res=quoteByThread.get(Thread.currentThread());
        }
        else {
            try {
                int idx=(int) (Math.random()*3.0)+1;
                core=new CoreProxy();
                InputStream is=MotorPlusPerformanceTest.class.getResourceAsStream("MotorPlusPerformanceTest0"+idx+".xml");
                XMLString quoteXml = new XMLString(is);
                res = new TestQuotation((Policy) core.fromXML(Policy.class, quoteXml));
                is.close();
                quoteByThread.put(Thread.currentThread(), res);
                res.quote.setQuotationNumber("T"+Thread.currentThread().getId());
                synchronized(quoteByThread) {
                    System.out.println("issues: "+issueCount++);
                }
            }
            catch(Throwable t) {
                t.printStackTrace();
                System.exit(0);
            }
        }
        
        return res;
    }
    
    private Policy cleanQuoteForTest(Policy quote) {
        quote.setStatus(PolicyStatus.APPLICATION);
        quote.setAssessmentSheet(null);
        for(Section s: quote.getSection()) {
            s.setAssessmentSheet(null);
        }
        return quote;
    }

    public void testCalculatePremium() throws Exception {
        // get the quote we're going to process, take a note of its premium and then clean it
        TestQuotation qt=getQuoteToProcess();
        Policy quote=cleanQuoteForTest(qt.quote);
        String qIn=quote.getQuotationNumber();

        // send the cleaned quote back into calc premium
        CalculatePremiumCommand cmd=core.newCommand(CalculatePremiumCommand.class);        
        cmd.setPolicyArgRet(quote);
        cmd.invoke();

        if (!cmd.getPolicyArgRet().getQuotationNumber().equals(qIn)) {
            System.err.println("Wrong quote. Sent: '"+qIn+"' in, but got: '"+cmd.getPolicyArgRet().getQuotationNumber()+"' back");
        }
        
        if (!cmd.getPolicyArgRet().getTotalPremium().equals(qt.premium)) {
            System.err.println("Premiums dont match. Expected: "+qt.premium+", got:"+cmd.getPolicyArgRet().getTotalPremium());
        }
        
        if (!qt.status.equals(cmd.getPolicyArgRet().getStatus())) {
            System.err.println("Status doesn't match");
        }
    }
    
    public static void main(String[] args) {
        MotorPlusPerformanceTest t=new MotorPlusPerformanceTest();
        try {
            t.testCalculatePremium();
        }
        catch(Throwable p) {
            p.printStackTrace();
        }
    }
}

class TestQuotation {
    Policy quote;
    CurrencyAmount premium;
    PolicyStatus status;

    public TestQuotation(Policy quote) {
        this.quote=quote;
        premium=quote.getTotalPremium();
        status=quote.getStatus();
    }
}
