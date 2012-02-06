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

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.CoreProxy;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.TypeXPathFunctionRegister;
import com.ail.core.XMLString;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Asset;
import com.ail.insurance.quotation.assessrisk.AssessRiskCommand;
import com.ail.insurance.quotation.calculatepremium.CalculatePremiumCommand;
import com.ail.openquote.Fact;
import com.ail.openquote.Functions;
import com.ail.openquote.Quotation;

public class TestMotorPlusQuotation extends CoreUserBaseCase {
    private static final long serialVersionUID = 2030295330203910171L;

    private CoreProxy core = null;

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() { 
        setupSystemProperties();
        core = new CoreProxy();
    }

    /**
    /**
     * DON'T PUT ANY TESTS BEFORE THIS ONE!! Perform two risk assessments and compare their performance. As this is the first test
     * in this TestCase, the system will have to load the decision table and compile the rules. The results of this should be cached
     * for reuse on the next call. This test checks that the performance difference between the first and second invocations is
     * significant enough to suggest that caching is working.
     */
    @Test
    public void testPerformanceTest() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        long start1, end1, start2, end2;

        start1 = System.currentTimeMillis();
        {
            AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
            command.setPolicyArgRet(quoteIn);
            command.invoke();
        }
        end1 = System.currentTimeMillis();

        start2 = System.currentTimeMillis();
        {
            AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
            command.setPolicyArgRet(quoteIn);
            command.invoke();
            command.getPolicyArgRet();
        }
        end2 = System.currentTimeMillis();
        System.out.printf("first took: %d, second took: %d\n", (end1 - start1), (end2 - start2));

        assertTrue("second call should be 2 times quicker than the first. First:" + (end1 - start1) + " second:" + (end2 - start2),
                (end1 - start1) / 2 > (end2 - start2));
    }

    /**
     * Test to try out various expressions that are used in the rules - really more of a scatch pad for trying things out than an
     * actual unit test.
     * 
     * @throws Exception
     */
    @Test
    public void testVariousExpressions() throws Exception {
        {
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
            Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
            Object o;

            o = quoteIn.xpathGet("count(/asset/attribute[id='securitydevice' and value='No'])");
            assertTrue(o instanceof Double);
            assertEquals(new Double(0.0), (Double) o);

            o = quoteIn.xpathGet("count(/asset/attribute[id='securitydevice' and value='Yes'])");
            assertTrue(o instanceof Double);
            assertEquals(new Double(1.0), (Double) o);

            assertTrue("TN6 2LJ".matches("^TN.*"));

            assertNotNull(quoteIn.xpathIterate("asset[assetTypeId='AccidentHistoryAsset']/attribute[id='date']/object"));
        }

        {
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteTwo.xml"));
            Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);

            assertNotNull(quoteIn.xpathGet("m:ageInYears(asset[id='driver1']/attribute[id='dob'])"));

            try {
                quoteIn.xpathSet("/excess[id='youngDriver2']/enabled", Boolean.TRUE);
            }
            catch (Throwable e) {
                fail(e.toString());
            }
        }

        {
            TypeXPathFunctionRegister.getInstance().registerFunctionLibrary("m", Functions.class);
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteThree.xml"));
            Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);

            Fact f = new Fact("conviction", quoteIn.xpathGet("asset[assetTypeId='ConvictionHistoryAsset']"));
            assertEquals(0, f.xpathInt("m:ageInYears(attribute[id='date'])"));
            assertEquals("Minor offence", f.xpathString("attribute[id='type']"));
        }

        {
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteThree.xml"));
            Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
            assertNotNull(quoteIn.xpathGet("asset[assetTypeId='AccidentHistoryAsset']"));
        }
    }
    

    @Test
    public void testBasicAssessRisk() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        Quotation quoteOut;

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();

        // check the lines we're expecting, and only the lines we're expecting, are present.
        assertNotNull(sheet.xpathGet("line[reason='Comprehensive cover for a 43 year old driver of a 2340cc car worth 19,000.00 GBP']"));
        assertNotNull(sheet.xpathGet("line[reason='Group D Occupation']"));
        assertNotNull(sheet.xpathGet("line[reason='Security devices fitted to all vehicles']"));
        assertNotNull(sheet.xpathGet("line[reason='Four years no claims discount']"));
        assertNotNull(sheet.xpathGet("line[reason='High risk postcode']"));
        assertEquals(5, sheet.getLineCount());
    }

    @Test
    public void testAssessRiskFemaleDriver() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);

        quoteIn.xpathSet("asset[id='driver1']/attribute[id='sex']/value", "Female");
        quoteIn.xpathSet("asset[id='driver1']/attribute[id='dob']/value", "01/08/1980");

        Quotation quoteOut;

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();

        // check the lines we're expecting, and only the lines we're expecting, are present.
        assertNotNull(sheet.xpathGet("line[reason='Main driver is female and over 25 years of age.']"));
    }

    @Test
    public void testAssessRiskDeclineYoung() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        Quotation quoteOut;

        quoteIn.xpathSet("asset[id='driver1']/attribute[id='dob']/value", "01/08/1994");

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();

        // check the lines we're expecting, and only the lines we're expecting, are present.
        assertNotNull(sheet.xpathGet("line[reason='Driver below 17 years or age']"));
    }

    public void testAssessRiskDeclineOld() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        Quotation quoteOut;

        quoteIn.xpathSet("asset[id='driver1']/attribute[id='dob']/value", "01/08/1925");

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();

        // check the lines we're expecting, and only the lines we're expecting, are present.
        assertNotNull(sheet.xpathGet("line[reason='Driver over 75 years or age']"));
    }

    public void testFourDriversAssessRisk() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteTwo.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        Quotation quoteOut;

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();

        // check the lines we're expecting, and only the lines we're expecting, are present.
        assertNotNull(sheet.xpathGet("line[reason='Comprehensive cover for a 43 year old driver of a 2340cc car worth 19,000.00 GBP']"));
        assertNotNull(sheet.xpathGet("line[reason='Young additional driver']"));
        assertNotNull(sheet.xpathGet("line[reason='Left hand drive conversion']"));
        assertNotNull(sheet.xpathGet("line[reason='Group D Occupation']"));
        assertNotNull(sheet.xpathGet("line[reason='Four years no claims discount']"));
        assertNotNull(sheet.xpathGet("line[reason='Security devices fitted to all vehicles']"));
        assertNotNull(sheet.xpathGet("line[reason='Other vehicle modification']"));
        assertNotNull(sheet.xpathGet("line[reason='Applied: Young or inexperianced driver excess (22-24 years of age)']"));
        assertNotNull(sheet.xpathGet("line[reason='High risk postcode']"));
        assertEquals(9, sheet.getLineCount());
    }

    public void testVehicleAgeDiscountsAndReferrals() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteTwo.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        Quotation quoteOut;

        quoteIn.xpathSet("asset[id='vehicle1']/attribute[id='year']/value", "1990");

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();

        assertNotNull(sheet.xpathGet("line[reason='Vehicle over 10 years old']"));
    }

    /**
     * Test to check that a conviction asset results in a loading.
     * 
     * @throws Exception
     */
    public void testAssessRiskConviction() throws Exception {
        XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteThree.xml"));
        Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);

        Quotation quoteOut;

        AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
        command.setPolicyArgRet(quoteIn);
        command.invoke();
        quoteOut = (Quotation) command.getPolicyArgRet();

        AssessmentSheet sheet = quoteOut.getAssessmentSheet();
        assertNotNull(sheet.xpathGet("line[reason='Minor offence in the last year']"));
    }

    /**
     * Test to check that more than one conviction asset results in more than one loading.
     * 
     * @throws Exception
     */
    public void testAssessRiskTwoConviction() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteThree.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
         Quotation quoteOut;
        
         Asset c2=(Asset)core.newProductType(quoteIn.getProductTypeId(), "ConvictionHistoryAsset");
         c2.xpathSet("attribute[id='date']/value", "01/01/2006");
         c2.xpathSet("attribute[id='type']/value", "Serious offence resulting in disqualification");
                
         quoteIn.addAsset(c2);
        
         AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
         command.setPolicyArgRet(quoteIn);
         command.invoke();
         quoteOut = (Quotation) command.getPolicyArgRet();
        
         AssessmentSheet sheet = quoteOut.getAssessmentSheet();
         assertNotNull(sheet.xpathGet("line[reason='Minor offence in the last year']"));
         assertNotNull(sheet.xpathGet("line[reason='Disqualification 2-3 years ago']"));
    }

    /**
     * Test to check that more accident history asset results in referrals and loadings.
     * 
     * @throws Exception
     */
    public void testAssessRiskOneLoadableAccident() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteThree.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
         Quotation quoteOut;
        
         // Test 1: that accidents when a driver was over 22 years of age cause a loading
         quoteIn.xpathSet("asset[id='driversHistory']/attribute[id='claimAccidentOrLoss']/value", "Yes");
         quoteIn.xpathSet("asset[id='accidentHistory1']/attribute[id='date']/value", "01/01/2005");
         quoteIn.xpathSet("asset[id='accidentHistory1']/attribute[id='atFault']/value", "No");
        
         AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
         command.setPolicyArgRet(quoteIn);
         command.invoke();
         quoteOut = (Quotation) command.getPolicyArgRet();
        
         AssessmentSheet sheet = quoteOut.getAssessmentSheet();
        
         assertNotNull(sheet.xpathGet("line[reason='No fault accident when over 22 years of age']"));
         assertEquals(7, sheet.getLineCount());
        
         // Test 2: that accidents when a driver was under 22 years of age cause a refer
         quoteIn.xpathSet("asset[id='driver1']/attribute[id='dob']/value", "01/01/1987");
         quoteIn.xpathSet("asset[id='accidentHistory1']/attribute[id='atFault']/value", "Yes");
        
         command.setPolicyArgRet(quoteIn);
         command.invoke();
         quoteOut = (Quotation) command.getPolicyArgRet();
        
         sheet = quoteOut.getAssessmentSheet();
         assertNotNull(sheet.xpathGet("line[reason='At fault accident when less than 22 years of age']"));
        
         // Test 4: that accidents with a claim > £1500 refers
         quoteIn.xpathSet("asset[id='accidentHistory1']/attribute[id='value']/value", "1501");
        
         command.setPolicyArgRet(quoteIn);
         command.invoke();
         quoteOut = (Quotation) command.getPolicyArgRet();
        
         sheet = quoteOut.getAssessmentSheet();
         assertNotNull(sheet.xpathGet("line[reason='At fault accident when less than 22 years of age']"));
    }

    public void testAccidentsWithinAYear() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        
         Asset accident=(Asset)core.newProductType(quoteIn.getProductTypeId(), "AccidentHistoryAsset");
         accident.xpathSet("attribute[id='date']/value", "01/01/2004");
         accident.xpathSet("attribute[id='value']/value", "0");
         quoteIn.addAsset(accident);
        
         accident=(Asset)core.newProductType(quoteIn.getProductTypeId(), "AccidentHistoryAsset");
         accident.xpathSet("attribute[id='date']/value", "01/05/2004");
         accident.xpathSet("attribute[id='value']/value", "0");
         quoteIn.addAsset(accident);
        
         accident=(Asset)core.newProductType(quoteIn.getProductTypeId(), "AccidentHistoryAsset");
         accident.xpathSet("attribute[id='date']/value", "01/08/2004");
         accident.xpathSet("attribute[id='value']/value", "0");
         quoteIn.addAsset(accident);
        
         accident=(Asset)core.newProductType(quoteIn.getProductTypeId(), "AccidentHistoryAsset");
         accident.xpathSet("attribute[id='date']/value", "01/08/2006");
         accident.xpathSet("attribute[id='value']/value", "0");
         quoteIn.addAsset(accident);
        
         AssessRiskCommand command = core.newCommand(AssessRiskCommand.class);
         command.setPolicyArgRet(quoteIn);
         command.invoke();
         Quotation quoteOut = (Quotation) command.getPolicyArgRet();
        
         AssessmentSheet sheet = quoteOut.getAssessmentSheet();
         assertNotNull(sheet.xpathGet("line[reason='Too many accidents in a single year']"));
    }

    /**
     */
    public void testBasicPremiumCalculation() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
         Quotation quoteOut;
        
         quoteIn.xpathSet("/asset[id='driver1']/attribute[id='occupation']/value", "Doctor");
        
         CalculatePremiumCommand ccp = core.newCommand(CalculatePremiumCommand.class);
         ccp.setPolicyArgRet(quoteIn);
         ccp.invoke();
         quoteOut = (Quotation) ccp.getPolicyArgRet();
        
         AssessmentSheet sheet = quoteOut.getAssessmentSheet();
         assertTrue(323.54==((CurrencyAmount)sheet.xpathGet("line[id='total premium']/amount")).getAmount().doubleValue());
    }

    /**
     * The basic product includes one accident and one conviction asset. These should be ignored by rules unless the proposer
     * answers 'Yes' to the "Have you had any X's in the last 5 years" questions. This test checks that this is the case. If the
     * rules did try to look at these assets, we'd get an exception because the assets don't contain any valid information - in fact
     * they have blank fields.
     * 
     * @throws Exception
     */
    public void testPremiumCalculationNoAccidentsOrConvictions() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteFour.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
         Quotation quoteOut;
        
         AssessRiskCommand arc = core.newCommand(AssessRiskCommand.class);
         arc.setPolicyArgRet(quoteIn);
         arc.invoke();
        
         CalculatePremiumCommand ccp = core.newCommand(CalculatePremiumCommand.class);
         ccp.setPolicyArgRet(arc.getPolicyArgRet());
         ccp.invoke();
         quoteOut = (Quotation) ccp.getPolicyArgRet();
        
         AssessmentSheet sheet = quoteOut.getAssessmentSheet();
        
         assertTrue(203.28==((CurrencyAmount)sheet.xpathGet("line[id='total premium']/amount")).getAmount().doubleValue());
    }

    /**
     * The conditions applied to actions use xpath notation to test the contents of the quotation. This test runs a few typical
     * tests just to check they return the expected results.
     * 
     * @throws Exception
     */
    public void testXpathConditionsTest() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteOne.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
                
         assertTrue((Boolean)quoteIn.xpathGet("m:test(page[.='Quotation'])"));
         assertFalse((Boolean)quoteIn.xpathGet("m:test(page[.='NotReallyThere'])"));
    }

    /**
     * Each product's rules are responsible for checking that the assessment lines they generate are sufficient
     * for a premium to be calculated. Where a base premium is looked up from a table, the product's rules should
     * add a referral to the sheet if no match was found in the table. 
     * This test checks that MotorPlus' risk assessment rules do that check.
     * @throws Exception
     */
    public void testPremiumCalculationNoBasePremium() throws Exception {
         XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteFour.xml"));
         Quotation quoteIn = (Quotation) core.fromXML(Quotation.class, quoteXml);
        
         // The base rate lookup only covers vehicles up to 3000cc, so we won't find a base premium for 9999.
         quoteIn.xpathSet("/asset[id='vehicle1']/attribute[id='cc']/value", "9999");

         AssessRiskCommand arc = core.newCommand(AssessRiskCommand.class);
         arc.setPolicyArgRet(quoteIn);
         arc.invoke();
                
         AssessmentSheet sheet = arc.getPolicyArgRet().getAssessmentSheet();
        
         assertTrue(sheet.isMarkedForRefer());
         assertNotNull(sheet.xpathGet("line[id='no base']/reason"));
    }
}
