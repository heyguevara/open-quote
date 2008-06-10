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

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.Attribute;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.History;
import com.ail.core.TypeXPathException;
import com.ail.core.TypeXPathFunctionRegister;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;

/**
 * @version $Revision: 1.9 $
 * @state $State: Exp $
 * @date $Date: 2007/01/07 17:38:31 $
 * @source $Source:
 *         /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestTypeXpath.java,v $
 */
public class TestTypeXpath extends CoreUserTestCase {
    boolean initialised=false;

    /** Constructs a test case with the given name. */
    public TestTypeXpath(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestTypeXpath.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    protected void setUp() {
        if (!initialised) {
            super.setupSystemProperties();
            ConfigurationHandler.reset();
            setVersionEffectiveDate(new VersionEffectiveDate());
            setCore(new Core(this));
            getCore().resetConfiguration();
            setVersionEffectiveDate(new VersionEffectiveDate());
            ConfigurationHandler.reset();
            initialised=true;
        }
    }
    /**
     * Test some simple gets and sets using Type's xpath methods. Type offers
     * support for processing XPATH expressions against a Type's instance. This
     * test performs some basic checks to ensure the support is working.
     * <ol>
     * <li>Create an instance of Version (Author="J.R.Hartley", Version="2").
     * </li>
     * <li>Fail if the xpath "author" doesn't return "J.R.Hartley".</li>
     * <li>Fail if the xpath "version" doesn't return "2".</li>
     * <li>Fail if the xpath "version" specifying Integer as the return doesn't
     * return Integer(2).</li>
     * <li>Use xpathSet to set "version" to "45"
     * <li>Fail if the xpath "version" specifying Integer as the return doesn't
     * return Integer(45).</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ol>
     */
    public void testSimpleXpathGetAndSet() throws Exception {
        Version version = new Version();
        version.setAuthor("J.R.Hartley");
        version.setVersion("2");

        assertEquals("J.R.Hartley", version.xpathGet("author"));
        assertEquals("2", (String) version.xpathGet("version"));
        assertEquals(new Integer(2), (Integer) version.xpathGet("version", Integer.class));

        version.xpathSet("version", "45");
        assertEquals(new Integer(45), (Integer) version.xpathGet("version", Integer.class));

        try {
            assertNull(version.xpathGet("this/path/does/not/exist"));
            fail("Expected exception not thrown");
        }
        catch(TypeXPathException e) {
            // ignore this - it's what we want.
        }
        catch(Throwable t) {
            fail("wrong exception thrown ("+t+")");
        }
    }

    /**
     * Test some more complex gets and sets using Type's xpath methods. Type
     * offers support for processing XPATH expressions against a Type's
     * instance. This test performs some basic checks to ensure the support is
     * working.
     * <ol>
     * </ol>
     */
    public void testComplexXpathGetAndSet() throws Exception {
        History history = new History();
        history.setSerialVersion(21);
        history.setLock(true);
        for (int i = 0; i < 10; i++) {
            Version v = new Version();
            v.setAuthor("author" + i);
            v.setComment("comment" + i);
            v.setCopyright("copyright" + i);
            v.setDate("date" + i);
            v.setSource("source" + i);
            v.setState("state" + i);
            v.setVersion("version" + i);
            history.addVersion(v);
        }

        assertEquals(new Integer(21), history.xpathGet("serialVersion", Integer.class));
        assertEquals("author0", history.xpathGet("version[1]/author"));
        assertEquals("author0", history.xpathGet("version[1]/author", String.class));
    }

    /**
     * Test some attribute xpaths
     */
    public void testAttributeXpathGetAndSet() throws Exception {
        Version v = new Version();
        v.setAuthor("author");
        v.setComment("comment");
        v.setCopyright("copyright");
        v.setDate("date");
        v.setSource("source");
        v.setState("state");
        v.setVersion("version");

        v.addAttribute(new Attribute("attrib1", "hello", "string"));
        v.addAttribute(new Attribute("attrib2", "30/11/1978", "date,pattern=dd/MM/yyyy"));
        v.addAttribute(new Attribute("gender1", "Male", "choice,options=-1#?|1#Male|2#Female"));
        v.addAttribute(new Attribute("gender2", "?", "choice,options=-1#?|1#Male|2#Female"));
        v.addAttribute(new Attribute("amount1", "£12", "currency", "GBP"));
        v.addAttribute(new Attribute("amount2", "12", "currency", "EUR"));
        v.addAttribute(new Attribute("amount3", "12%", "number,percent"));
        v.addAttribute(new Attribute("amount4", "12", "currency"));
        v.addAttribute(new Attribute("amount5", "£12.234", "number,pattern=£#.##"));
        v.addAttribute(new Attribute("amount6", "12,000", "currency"));
        v.addAttribute(new Attribute("answer", "Yes", "yesorno"));
        
        Calendar c = Calendar.getInstance();
        c.set(1978, 10, 30, 0, 0, 0);

        assertEquals("hello", v.xpathGet("attribute[id='attrib1']/value"));
        assertEquals("30/11/1978", v.xpathGet("attribute[id='attrib2']/value"));
        assertEquals(c.getTime().toString(), v.xpathGet("attribute[id='attrib2']/object", Date.class).toString());
        assertEquals(new Double(1.0), (Double) v.xpathGet("attribute[id='gender1']/object"));
        assertEquals("£12", v.xpathGet("attribute[id='amount1']/value"));
        assertEquals("12.00 GBP", v.xpathGet("attribute[id='amount1']/formattedValue"));
        assertEquals("12", v.xpathGet("attribute[id='amount1']/object", String.class));
        assertEquals("12", v.xpathGet("attribute[id='amount2']/object", String.class));
        assertEquals("12", v.xpathGet("attribute[id='amount2']/value"));
        assertEquals("Male", v.xpathGet("attribute[id='gender1']/formattedValue"));
        
        assertEquals(new Long(12), (Long)v.xpathGet("attribute[id='amount3']/object"));
        assertEquals("12%", (String)v.xpathGet("attribute[id='amount3']/value"));
        assertEquals(new Long(12), (Long)v.xpathGet("attribute[id='amount4']/object"));
        assertEquals("12", (String)v.xpathGet("attribute[id='amount4']/value")); 
        assertEquals(new Double(12.234), (Double)v.xpathGet("attribute[id='amount5']/object"));
        assertEquals("£12.234", (String)v.xpathGet("attribute[id='amount5']/value")); 
        assertEquals("£12.23", (String)v.xpathGet("attribute[id='amount5']/formattedValue"));
        assertEquals("12.00 EUR", (String)v.xpathGet("attribute[id='amount2']/formattedValue"));
        assertEquals(new Long(12000), (Long)v.xpathGet("attribute[id='amount6']/object"));
        assertEquals("12,000", v.xpathGet("attribute[id='amount6']/value"));
        assertEquals("12,000", (String)v.xpathGet("attribute[id='amount6']/formattedValue"));
        assertEquals("Male", (String)v.xpathGet("attribute[id='gender1']/value"));
        assertEquals(new Double(1.0), (Double)v.xpathGet("attribute[id='gender1']/object"));
        assertEquals("Male", (String)v.xpathGet("attribute[id='gender1']/formattedValue"));
        assertEquals("?", (String)v.xpathGet("attribute[id='gender2']/value"));
        assertEquals(new Double(-1.0), (Double)v.xpathGet("attribute[id='gender2']/object"));
        assertEquals("?", (String)v.xpathGet("attribute[id='gender2']/formattedValue"));
    }
    
    public void testFunctions() {
        TypeXPathFunctionRegister.getInstance().registerFunctionLibrary("test", Functions.class);
        
        Version v = new Version();
        
        v.addAttribute(new Attribute("attrib1", "hello", "string"));
        v.addAttribute(new Attribute("attrib2", "30/11/1978", "date,pattern=dd/MM/yyyy"));
        v.addAttribute(new Attribute("gender1", "Male", "choice,options=-1#?|1#Male|2#Female"));
        v.addAttribute(new Attribute("gender2", "?", "choice,options=-1#?|1#Male|2#Female"));

        // 'standard' xpath function
        assertEquals(4.0, v.xpathGet("count(/attribute)", Double.class));

        // One of ours from com.ail.core.Function
        assertEquals("30 November, 1978", v.xpathGet("test:format(attribute[id='attrib2']/object, 'dd MMMM, yyyy')"));
    }

    public void testXpathFunctionRegistration() {
        Version v = new Version();
        
        v.addAttribute(new Attribute("dob1", "30/11/1978", "date,pattern=dd/MM/yyyy"));
        v.addAttribute(new Attribute("dob2", "30/11/1986", "date,pattern=dd/MM/yyyy"));
        v.addAttribute(new Attribute("dob3", "30/11/1964", "date,pattern=dd/MM/yyyy"));

        // register some new functions
        TypeXPathFunctionRegister.getInstance().registerFunctionLibrary("test", TestFunctions.class);
        
        // invoke the newly registered function
        assertEquals(29, v.xpathGet("test:age(attribute[id='dob1'])"));
        assertEquals(21, v.xpathGet("test:age(test:youngest(attribute))"));
    }

    public void testCollection() {
        Version v = new Version();
        
        v.addAttribute(new Attribute("attrib1", "hello", "string"));
        v.addAttribute(new Attribute("attrib2", "30/11/1978", "date,pattern=dd/MM/yyyy"));
        v.addAttribute(new Attribute("gender1", "Male", "choice,options=-1#?|1#Male|2#Female"));
        v.addAttribute(new Attribute("gender2", "?", "choice,options=-1#?|1#Male|2#Female"));

        Object o=v.xpathGet("attribute");

        assertTrue(o instanceof List);
    }

    /**
     * Define a few simple functions for use from JXPath expressions
     *
     */
    public static class TestFunctions {
        public static Integer yearsSince(Date date, Date when) {
            // create a calendar to represent the date of birth (or whatever)
            Calendar then=Calendar.getInstance();
            then.setTime(date);
            
            // create a calendar for now
            Calendar now=Calendar.getInstance();
            now.setTime(when);
            
            // age is the difference in years between then and now
            int age=now.get(Calendar.YEAR) - then.get(Calendar.YEAR);
            
            // if the anniversay hasn't yet passed this year, subtract 1
            then.add(Calendar.YEAR, age);        
            if (now.before(then)) {
                age--;
            }
            
            return age;
        }

        public static Integer age(Attribute attrib) {
            if (attrib.isDateType()) {
                return yearsSince((Date)attrib.getObject(), new Date());                
            }
            return 0;
        }

        /**
         * Returns the youngest of a collection of date attributes. The collection is assumed to 
         * be made up attributes (and only attributes) and only attributes of type date will be
         * taken into account - any other attributes will be ignored.
         * @param attribs Collection of date attributes
         * @return The youngest date in the collection.
         */
        @SuppressWarnings("unchecked")
        public static Attribute youngest(Collection attribs) {
            Attribute youngest=null;

            for(Attribute at: (Collection<Attribute>)attribs) {
                if (at.isDateType()) {
                    if (youngest==null || ((Date)youngest.getObject()).before((Date)at.getObject())) {
                        youngest=at;
                    }
                }
            }
            
            return youngest;
        }
    }
}
