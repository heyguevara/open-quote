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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Locale;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.CoreProxy;
import com.ail.core.ThreadLocale;
import com.ail.core.product.ListProductsService;
import com.ail.core.product.ResetAllProductsService;
import com.ail.insurance.policy.AssessmentNote;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.FixedSum;
import com.ail.insurance.policy.Marker;
import com.ail.insurance.policy.MarkerResolution;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.insurance.policy.Subjectivity;
import com.ail.insurance.policy.SumBehaviour;
import com.ail.insurance.policy.Totalizer;
import com.ail.insurance.quotation.CalculatePremiumService;
import com.ail.party.Person;
import com.ail.party.Title;
import com.ail.util.DateOfBirth;

@SuppressWarnings("deprecation")
public class TestPolicyPersistence  {
    private Locale savedLocale;
    private CoreProxy core;
    
    /**
     * Sets up the fixture (run before every test). 
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        new CalculatePremiumService().resetConfiguration();
        new ListProductsService().resetConfiguration();
        new ResetAllProductsService().resetConfiguration();

        core=new CoreProxy();
        savedLocale=ThreadLocale.getThreadLocale();
        ThreadLocale.setThreadLocale(Locale.UK);
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
        ThreadLocale.setThreadLocale(savedLocale);
    }

    /**
     * Test put on risk from quotation status
     * 
     * @throws Exception
     */
    @Test
    public void testPolicyPersistence() {
        {
            long start = System.currentTimeMillis();
            internalTestPolicyPersistence();
            long end = System.currentTimeMillis();
            System.out.printf("first took: %dms\n", end - start);
        }

        {
            long start = System.currentTimeMillis();
            internalTestPolicyPersistence();
            long end = System.currentTimeMillis();
            System.out.printf("second took: %dms\n", end - start);
        }
    }

    private void internalTestPolicyPersistence() {

        long policySystemId;

        {
            Policy policy = (Policy) core.newProductType("com.ail.core.product.TestProduct4");

            long start = System.currentTimeMillis();

            core.openPersistenceSession();

            policy = (Policy) core.create(policy);

            policySystemId = policy.getSystemId();

            core.closePersistenceSession();

            long end = System.currentTimeMillis();
            System.out.printf("save took: %dms\n", end - start);

        }

        {
            long start = System.currentTimeMillis();

            core.openPersistenceSession();

            Policy policy = (Policy) core.load(Policy.class, policySystemId);

            assertNotNull(policy);
            assertEquals("0101010", policy.getId());
            assertEquals("com.ail.core.product.TestProduct4", policy.getProductTypeId());
            assertEquals("POL1234", policy.getPolicyNumber());
            assertEquals("QUO4321", policy.getQuotationNumber());
            assertNotNull(policy.getInceptionDate());
            assertNotNull(policy.getExcess());
            assertTrue(PolicyStatus.APPLICATION.equals(policy.getStatus()));

            Person ph = (Person) policy.getPolicyHolder();

            assertTrue(Title.MR.equals(ph.getTitle()));
            assertEquals("Lord", ph.getOtherTitle());
            assertEquals("Jimbo", ph.getFirstName());
            assertEquals("Clucknasty", ph.getSurname());
            assertEquals(new DateOfBirth(1964, 11, 8).getDateAsString(), ph.getDateOfBirth().getDateAsString());
            assertEquals("Mr. Jimbo Clucknasty", ph.getLegalName());
            assertEquals("The Top Flat", ph.getAddress().getLine1());
            assertEquals("East Wing", ph.getAddress().getLine2());
            assertEquals("Plugsmear House", ph.getAddress().getLine3());
            assertEquals("Snorberry Lane", ph.getAddress().getLine4());
            assertEquals("Frobmarshington", ph.getAddress().getLine5());
            assertEquals("Upper Wallingham", ph.getAddress().getTown());
            assertEquals("Kunt", ph.getAddress().getCounty());
            assertEquals("UK", ph.getAddress().getCountry());
            assertEquals("KU19 3FS", ph.getAddress().getPostcode());

            assertNotNull(policy.getAssetById("a11"));
            assertEquals(19, policy.getAssetById("a11").getAttributeCount());

            assertNotNull(policy.getSectionById("s11"));

            assertNotNull(policy.getCoverageById("c1"));
            assertEquals("East river tea bag cover", policy.getCoverageById("c1").getDescription());
            assertTrue(policy.getCoverageById("c1").isEnabled());
            assertTrue(!policy.getCoverageById("c1").isOptional());
            assertEquals("\u00A3"+"1,000.00", policy.getCoverageById("c1").getLimit().toString());
            assertEquals("\u00A3"+"500.00", policy.getCoverageById("c1").getDeductible().toString());

            AssessmentSheet sheet = policy.getAssessmentSheet();

            AssessmentNote note1 = (AssessmentNote) sheet.findLineById("note1");
            assertEquals("note1", note1.getId());
            assertEquals("because i want to", note1.getReason());
            assertNull(note1.getRelatesTo());

            MarkerResolution res1 = (MarkerResolution) sheet.findLineById("res1");
            assertEquals("res1", res1.getId());
            assertEquals("because I've resolved it", res1.getReason());
            assertNull(res1.getRelatesTo());
            assertEquals("note1", res1.getBehaviourId());

            Marker mkr1 = (Marker) sheet.findLineById("mkr1");
            assertEquals("mkr1", mkr1.getId());
            assertEquals("declines are good", mkr1.getReason());
            assertEquals("DECLINE", mkr1.getTypeAsString());
            assertEquals("ASSET", mkr1.getRelatesTo().getTypeAsString());
            assertEquals("as1", mkr1.getRelatesTo().getId());

            Subjectivity sub1 = (Subjectivity) sheet.findLineById("sub1");
            assertEquals("sub1", sub1.getId());
            assertEquals("subjectivities are good", sub1.getReason());
            assertEquals("SUBJECTIVITY", sub1.getTypeAsString());
            assertEquals("ASSET", sub1.getRelatesTo().getTypeAsString());
            assertEquals("as1", sub1.getRelatesTo().getId());
            assertEquals("Proof of fish eaten", sub1.getText());
            assertTrue(!sub1.isManuscript());

            FixedSum fix1 = (FixedSum) sheet.findLineById("fix1");
            assertEquals("fix1", fix1.getId());
            assertEquals("fixed sums are good", fix1.getReason());
            assertEquals("ASSET", fix1.getRelatesTo().getTypeAsString());
            assertEquals("as1", fix1.getRelatesTo().getId());
            assertEquals("final premium", fix1.getContributesTo());
            assertEquals("\u00A3"+"123.00", fix1.getAmount().toString());

            Totalizer tot = (Totalizer) sheet.findLineById("tot");
            assertEquals("tot", tot.getId());
            assertEquals("totals are nice", tot.getReason());
            assertEquals("ASSET", tot.getRelatesTo().getTypeAsString());
            assertEquals("as1", tot.getRelatesTo().getId());
            assertEquals("final premium", tot.getContributesTo().toString());
            assertEquals("one,two,tree", tot.getDependsOn());

            RateBehaviour rat = (RateBehaviour) sheet.findLineById("rat");
            assertEquals("rat", rat.getId());
            assertEquals("I rate rates", rat.getReason());
            assertEquals("ASSET", rat.getRelatesTo().getTypeAsString());
            assertEquals("as2", rat.getRelatesTo().getId());
            assertEquals("total premium", rat.getContributesTo().toString());
            assertTrue(BehaviourType.LOAD.equals(rat.getType()));
            assertEquals("2%", rat.getRate().getRate());
            assertEquals("one", rat.getDependsOn());

            SumBehaviour sum = (SumBehaviour) sheet.findLineById("sum");
            assertEquals("sum", sum.getId());
            assertEquals("Sum do sum dont", sum.getReason());
            assertEquals("ASSET", sum.getRelatesTo().getTypeAsString());
            assertEquals("as2", sum.getRelatesTo().getId());
            assertEquals("final premium", sum.getContributesTo().toString());
            assertTrue(BehaviourType.LOAD.equals(sum.getType()));
            assertEquals("\u00A3"+"321.00", sum.getAmount().toString());

            core.closePersistenceSession();

            long end = System.currentTimeMillis();
            System.out.printf("load took: %dms\n", end - start);
        }
    }
}
