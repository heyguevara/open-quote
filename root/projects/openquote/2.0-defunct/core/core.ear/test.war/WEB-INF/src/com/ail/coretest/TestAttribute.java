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

package com.ail.coretest;

import static java.util.Locale.CANADA;
import static java.util.Locale.GERMANY;
import static java.util.Locale.UK;
import static java.util.Locale.US;
import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.core.Locale;
import com.ail.core.XMLString;

/**
 * Tests to exercise the facilities offered by he Core's Attribute class.
 */
public class TestAttribute extends TestCase {

    /** Constructs a test case with the given name. */
    public TestAttribute(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestAttribute.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

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

    public void testAttributeFormatting() {
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setThreadLocale(UK);

            Attribute attr;
    
            attr = new Attribute("amount5", "£12.234", "number;pattern=£#.00");
            
            assertEquals("£12.23", attr.getFormattedValue());
            assertEquals(12.234, attr.getObject());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }

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
    public void testChoiceOptionWithCommas() {
        Attribute a = new Attribute("refdata", "?", "choice,options=-1#?|1#Salon|2#House, Boat|3#Convertible, ship");
        assertEquals("-1#?|1#Salon|2#House, Boat|3#Convertible, ship", a.getFormatOption("options"));
    }

    /**
     * Test choice attribute formatting
     */
    public void testChoiceFormatting() {
        Attribute a = new Attribute("refdata", "Convertible, ship", "choice,options=-1#?|1#Salon|2#House, Boat|3#Convertible, ship");
        assertEquals("Convertible, ship", a.getFormattedValue());
        assertEquals("Convertible, ship", a.getValue());
        assertEquals(3.0, a.getObject());

    }

    public void testCurrencyAttribute() {
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setThreadLocale(UK);
    
            Attribute attr = new Attribute("q1", "1002.23", "currency", "GBP");
    
            assertEquals(java.lang.String.class, attr.getValue().getClass());
            assertEquals(java.lang.Double.class, attr.getObject().getClass());
    
            assertEquals("£1,002.23", attr.getFormattedValue());
            assertEquals("1002.23", attr.getValue());
            assertEquals(1002.23, attr.getObject());
        }
        finally {
            Locale.setDefault(runningLocale);
        }
    }
    
    /**
     * In versions of the core up to and including 2.2 the attribute value property was always mapped to an xml attribute. For 2.3
     * the value was moved to the text of the element to allow for markup within the value - particularly for attributes of type "note".
     * This test checks that the new approach works, and that backward compatibility has been maintained - i.e. that 2.3 will 
     * correctly read Xml generated from pre-2.3 systems.
     */
    public void testXmlTextMapping() throws Exception{
    	Attribute attr;
    	String pre23Sample="<attribute xsi:type='com.ail.core.Attribute' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' id='name' format='note' value='hello'/>";
    	String post23Sample="<attribute xsi:type='com.ail.core.Attribute' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' id='name' format='note'><value><![CDATA[My Hat <b>Is</b> Off]]></value></attribute>";
    	String subAttrPre23Sample="<attribute xsi:type='com.ail.core.Attribute' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' id='name' format='note' value='hello'>"+
    	                              "<attribute id='code' format='string' value='a value'/>"+
    	                          "</attribute>";
       	String subAttrPost23Sample="<attribute xsi:type='com.ail.core.Attribute' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance' id='name' format='note'>"+
       	                              "<attribute id='code' format='string' value='a value'/>"+
       	                              "<value><![CDATA[My Hat <b>Is</b> Off]]></value>"+
       	                           "</attribute>";

       	// check that the value for a note is mapped to an element
       	attr=new Attribute("test", "<b>text</b>", "note");
    	String xml=new CoreProxy().toXML(attr).toString();
        assertTrue(xml.indexOf("<value>")>0);
    	
        // check that the value for non-notes is mapped to an attribute
        attr=new Attribute("test", "hello", "string");
        String xml2=new CoreProxy().toXML(attr).toString();
        assertTrue(xml2.indexOf("<value>")==-1);
        
    	attr=new CoreProxy().fromXML(Attribute.class, new XMLString(pre23Sample));
    	assertEquals("name", attr.getId());
    	assertEquals("note", attr.getFormat());
    	assertEquals("hello", attr.getValue());

    	attr=new CoreProxy().fromXML(Attribute.class, new XMLString(post23Sample));
    	assertEquals("name", attr.getId());
    	assertEquals("note", attr.getFormat());
    	assertEquals("My Hat <b>Is</b> Off", attr.getValue());
    	
        attr=new CoreProxy().fromXML(Attribute.class, new XMLString(subAttrPre23Sample));
        assertEquals("name", attr.getId());
        assertEquals("note", attr.getFormat());
        assertEquals("hello", attr.getValue());
        assertEquals(1, attr.getAttributeCount());
        
        attr=new CoreProxy().fromXML(Attribute.class, new XMLString(subAttrPost23Sample));
        assertEquals("name", attr.getId());
        assertEquals("note", attr.getFormat());
        assertEquals("My Hat <b>Is</b> Off", attr.getValue());
        assertEquals(1, attr.getAttributeCount());

        xml=new CoreProxy().toXML(attr).toString();
    	assertTrue(xml.contains("<![CDATA[My Hat <b>Is</b> Off]]>"));
    	assertFalse(xml.contains("\"My Hat Is Off\""));
    }


    /**
     * The getFormattedValue() method on attribute should return values formatted
     * for the current locale. This test checks that functionality.
     */
    public void testLocaleSpecificCurrencyFormatting() throws Exception {
        
        // This test class may get run by developers anywhere in the world, 
        // so put the JVM into a known locale for testing - and be sure to 
        // switch back to the real one before returning.
        Locale runningLocale=Locale.getDefault();

        try {
            Locale.setThreadLocale(UK);
            Attribute gbp = new Attribute("q1", "1002.23", "currency", "GBP");
            
            // test that formatting works in the default locale
            assertEquals("£1,002.23", gbp.getFormattedValue());
    
            Attribute usd = new Attribute("q1", "1002.23", "currency", "USD");
    
            // USD formatted for Canada
            Locale.setThreadLocale(CANADA);
            assertEquals("USD1,002.23", usd.getFormattedValue());
            
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
}

