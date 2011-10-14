/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

package com.ail.commercialtest;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import junit.framework.Test;
import junit.framework.TestCase;

import com.ail.financial.PaymentCard;

/**
 */
public class TestPaymentCard extends TestCase {
    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestPaymentCard(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestPaymentCard.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * The PaymentCard class provides methods to format the start and end dates
     * of the card. This test checks that they are working correctly.
     * <ol>
     * <li>Create a PaymentCard instance with start date='10/11/2010' and end date="10/12/2012"</li>
     * <li>Test that getFormattedStartDate returns '11/10'</li>
     * <li>Test that getFormattedEndDate returns '12/12/</li>
     * </ol>
     */
    public void testStartAndExpiryDateFormatting() throws Exception {
        PaymentCard sut=new PaymentCard();

        DateFormat df=new SimpleDateFormat("dd/MM/yyyy");
        
        sut.setStartDate(df.parse("10/11/2010"));
        sut.setExpiryDate(df.parse("10/12/2012"));
        
        assertEquals("11/10", sut.getFormattedStartDate());
        assertEquals("12/12", sut.getFormattedExpiryDate());
    }

    /**
     * The PaymentCard class offers a method to return a masked version of
     * the card number. This test checks that this is working correctly.
     * <ol>
     * <li>Create an instance of PaymentCard and set the card number to '1234 2345 3456 4567'</li>
     * <li>Test that getMaskedCardNumber returns '**** **** **** 4567'</li>
     * </ol>
     */
    public void testCardNumberMasking() {
        PaymentCard sut=new PaymentCard();
        
        sut.setCardNumber("1234 2345 3456 4567");
        
        assertEquals("**** **** **** 4567", sut.getMaskedCardNumber());
        
    }

    public void testNullDates() {
        PaymentCard sut=new PaymentCard();
        
        try {
            sut.getFormattedExpiryDate();
        }
        catch(NullPointerException e) {
            fail("NPE should not be thrown");
        }

        try {
            sut.getFormattedStartDate();
        }
        catch(NullPointerException e) {
            fail("NPE should not be thrown");
        }
    }
}
