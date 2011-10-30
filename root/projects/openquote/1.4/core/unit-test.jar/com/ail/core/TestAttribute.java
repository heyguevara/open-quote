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

package com.ail.core;

import static java.util.Locale.CANADA;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;
import static java.util.Locale.US;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Attribute;
import com.ail.core.Locale;

/**
 * Tests to exercise the facilities offered by he Core's Attribute class.
 */
public class TestAttribute {

    @Before
    public void setUp() throws Exception {
        Locale.setThreadLocale(UK);
    }
    
    @After
    public void teadDown() throws Exception {
        Locale.setThreadLocale(UK);
    }
    
    @Test
    public void testAttributeValidation() throws Exception {
        Attribute attr;

        attr = new Attribute("q1", "1", "number;min=0;max=21");
        attr.setValue("-1");
        assertTrue(attr.isInvalid());
        attr.setValue("22");
        assertTrue(attr.isInvalid());
        attr.setValue("10");
        assertTrue(!attr.isInvalid());

        attr = new Attribute("q1", "1", "currency;min=0.20;max=21", "GBP");
        attr.setValue("0.12");
        assertTrue(attr.isInvalid());
        attr.setValue("22.54");
        assertTrue(attr.isInvalid());
        attr.setValue("10");
        assertTrue(!attr.isInvalid());

        attr = new Attribute("q1", "hello", "string;min=4;max=20");
        attr.setValue("ooo");
        assertTrue(attr.isInvalid());
        attr.setValue("ooooooooooooooooooooo");
        assertTrue(attr.isInvalid());
        attr.setValue("ooooooooooooo");
        assertTrue(!attr.isInvalid());

        attr = new Attribute("q1", "hello", "string;min=0;max=20");
        attr.setValue("");
        assertTrue(!attr.isInvalid());
        attr.setValue("oooooooooo");
        assertTrue(!attr.isInvalid());
        attr.setValue("ooooooooooooooooooooo");
        assertTrue(attr.isInvalid());

        attr = new Attribute("q1", "hello", "string;pattern=[0-9a-z]*");
        attr.setValue("abc123");
        assertTrue(!attr.isInvalid());
        attr.setValue("Aabc123");
        assertTrue(attr.isInvalid());

        attr = new Attribute("q1", "30/11/1978", "date;pattern=dd/MM/yyyy");
        attr.getObject();
    }

    @Test
    public void testAttributeFormatting() {
        Attribute attr;

        attr = new Attribute("amount5", "£12.234", "number;pattern=£#.00");
        assertEquals("£12.23", attr.getFormattedValue());
        assertEquals(12.234, attr.getObject());
    }

    @Test
    public void testReference() {
        Attribute attr;

        Attribute.setReferenceContext(new Attribute("refdata", "?", "choice,options=-1#?|1#Salon|2#Coupe|3#Convertible"));

        attr = new Attribute("id123", "?", "ref=/format");
        assertFalse(attr.isInvalid());

        attr.setValue("Salon");
        assertFalse(attr.isInvalid());

        attr.setValue("Hello");
        assertTrue(attr.isInvalid());
    }

    /**
     * Test that commas in a format option list are seen as part of the option text itself, and not a format separator.
     */
    @Test
    public void testChoiceOptionWithCommas() {
        Attribute a = new Attribute("refdata", "?", "choice,options=-1#?|1#Salon|2#House, Boat|3#Convertible, ship");
        assertEquals("-1#?|1#Salon|2#House, Boat|3#Convertible, ship", a.getFormatOption("options"));
    }

    /**
     * Test choice attribute formatting
     */
    @Test
    public void testChoiceFormatting() {
        Attribute a = new Attribute("refdata", "Convertible, ship", "choice,options=-1#?|1#Salon|2#House, Boat|3#Convertible, ship");
        assertEquals("Convertible, ship", a.getFormattedValue());
        assertEquals("Convertible, ship", a.getValue());
        assertEquals(3.0, a.getObject());

    }

    @Test
    public void testCurrencyAttribute() {
        Attribute attr = new Attribute("q1", "1002.23", "currency", "GBP");

        assertEquals(java.lang.String.class, attr.getValue().getClass());
        assertEquals(java.lang.Double.class, attr.getObject().getClass());

        assertEquals("£1,002.23", attr.getFormattedValue());
        assertEquals("1002.23", attr.getValue());
        assertEquals(1002.23, attr.getObject());
    }
    
    /**
     * The getFormattedValue() method on attribute should return values formatted
     * for the current locale. This test checks that functionality.
     */
    @Test
    public void testLocaleSpecificCurrencyFormatting() throws Exception {
        
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setDefault(new Locale(UK));
            Attribute gbp = new Attribute("q1", "1002.23", "currency", "GBP");
            
            // test that formatting works in the default locale
            assertEquals("£1,002.23", gbp.getFormattedValue());
    
            Attribute usd = new Attribute("q1", "1002.23", "currency", "USD");
    
            // USD formatted for Canada
            Locale.setThreadLocale(CANADA);
            assertEquals("US$1,002.23", usd.getFormattedValue());
            
            // USD formatted for USA
            Locale.setThreadLocale(US);
            assertEquals("$1,002.23", usd.getFormattedValue());
            
            // USD formatted for Germany
            Locale.setThreadLocale(GERMANY);
            assertEquals("1.002,23 USD", usd.getFormattedValue());
            
            Locale.setThreadLocale(US);
            usd.setFormat("currency;pattern=\u00A4 #,##0");
            assertEquals("$ 1,002", usd.getFormattedValue());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }

    /**
     * The getFormattedValue() method on attribute should return values formatted
     * for the current locale. This test checks that functionality.
     */
    @Test
    public void testLocaleSpecificNumberFormatting() throws Exception {
        
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setThreadLocale(UK);
            Attribute number = new Attribute("q1", "1002.23", "number");
    
            Locale.setThreadLocale(GERMANY);
            assertEquals("1.002,23", number.getFormattedValue());

            Locale.setThreadLocale(US);
            assertEquals("1,002.23", number.getFormattedValue());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }

    /**
     * Values sent to an Attribute's set method may be in a format appropriate 
     * to the current locale, or in the Attribute's own defined format (pattern), or
     * in a format suitable for the system's default locale. This test checks these
     * operations.
     */
    @Test
    public void testSetOperationsForLocaleSpecificdNumberValues() throws Exception {
        
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setDefault(new Locale(UK));
            Attribute number = new Attribute("q1", "1002.23", "number");
            
            Locale.setThreadLocale(UK);
            number.setValue("2,004.32");
            assertEquals("2004.32", number.getValue());
            assertEquals(2004.32, number.getObject());  
            
            Locale.setThreadLocale(GERMANY);
            number.setValue("1.002,64");
            assertEquals("1002.64", number.getValue());
            assertEquals(1002.64, number.getObject());  
            number.setValue("9.021,131");
            assertEquals("9021.131", number.getValue());
            assertEquals(9021.131, number.getObject());  

             Locale.setThreadLocale(UK);
            number.setValue("10,921.441");

            Locale.setThreadLocale(GERMANY);
            assertEquals("10921.441", number.getValue());
            assertEquals("10.921,441", number.getFormattedValue());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }

    @Test
    public void testSetOperationsForLocaleSpecificdCurrencyValues() throws Exception {
        
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setDefault(new Locale(UK));
            Locale.setThreadLocale(UK);

            Attribute money = new Attribute("q1", "1002.23", "currency", "GBP");
            money.setValue("1004.80");
            assertEquals("1004.8", money.getValue());
            money.setValue("2,001.90");
            assertEquals("2001.9", money.getValue());
            money.setValue("£921.30");
            assertEquals("921.3", money.getValue());

            Locale.setThreadLocale(GERMANY);
            money.setValue("1004,80");
            assertEquals("1004.8", money.getValue());
            money.setValue("2.001,90");
            assertEquals("2001.9", money.getValue());
            money.setValue("921,30 GBP");
            assertEquals("921.3", money.getValue());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }

    @Test
    public void testGetAndSetOperationsForPercentValues() throws Exception {
        
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setDefault(new Locale(UK));
            Locale.setThreadLocale(UK);

            Attribute percent;
            
            percent = new Attribute("q1", "12%", "number,percent");
            assertEquals("12", percent.getValue());
            assertEquals(0.12, percent.getObject());
            assertEquals("12%", percent.getFormattedValue());
            
            percent = new Attribute("q1", "12", "number,percent");
            assertEquals("12", percent.getValue());
            assertEquals(.12, percent.getObject());
            assertEquals("12%", percent.getFormattedValue());

            percent.setValue("15");
            assertEquals("15", percent.getValue());
            assertEquals(.15, percent.getObject());
            assertEquals("15%", percent.getFormattedValue());

            percent.setValue("90%");
            assertEquals("90", percent.getValue());
            assertEquals(.9, percent.getObject());
            assertEquals("90%", percent.getFormattedValue());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }
    
    @Test
    public void testNumberLocaleFormatting() throws Exception {
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setDefault(new Locale(GERMANY));
            Locale.setThreadLocale(GERMANY);

            Attribute percent;
            
            percent = new Attribute("q1", "1000", "number,patter=#,###");
            assertEquals("1000", percent.getValue());
            assertEquals("1.000", percent.getFormattedValue());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }
    
    @Test
    public void testYesOrNoWithRequiredEqualsNo() throws Exception {
        Attribute attr;
        attr=new Attribute("id", "Yes", "yesorno");
        assertTrue(attr.isYesornoType());
        assertFalse(attr.isUndefined());

        attr=new Attribute("id", "No", "yesorno");
        assertTrue(attr.isYesornoType());
        assertFalse(attr.isUndefined());

        attr=new Attribute("id", "?", "yesorno");
        assertTrue(attr.isYesornoType());
        assertTrue(attr.isUndefined());

        attr=new Attribute("id", "?", "yesorno;required=no");
        assertTrue(attr.isYesornoType());
        assertTrue(attr.isUndefined());
    }

    @Test
    public void testChoiceWithNonstandardBlank() throws Exception {
        Attribute attr;

        attr=new Attribute("id", "-", "choice,options=-1#-|1#Salon|2#Coupe|3#Convertible");
        assertTrue(attr.isUndefined());
        
        attr.setValue("Salon");
        assertFalse(attr.isUndefined());
    }
    
    @Test
    public void testRequiredOption() {
        Attribute attr;

        attr=new Attribute("id", "-", "string");
        assertTrue(attr.isRequired());

        attr=new Attribute("id", "-", "string,required=yes");
        assertTrue(attr.isRequired());

        attr=new Attribute("id", "-", "string,required=no");
        assertFalse(attr.isRequired());
    }
}

