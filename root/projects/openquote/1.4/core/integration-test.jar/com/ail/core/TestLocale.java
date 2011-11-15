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

package com.ail.core;

import static java.util.Locale.CANADA;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.Locale;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.CoreUserTestCase;

public class TestLocale extends CoreUserTestCase {
    static boolean setup=false;
    
    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        tidyUpTestData();
        setupSystemProperties();
        setCore(new Core(this));
        if (!setup) {
            System.out.println("setup");
            ConfigurationHandler.reset();
            getCore().resetConfiguration();
            ConfigurationHandler.reset();
            setup=true;
        }
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
        tidyUpTestData();
    }

    /**
     * Always select the latest configurations
     * @return
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    /**
     * @throws Exception
     */
    @Test
    public void testLocaleConstruction() throws Exception {
        try {
            new Locale().getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        }
        catch(IllegalStateException e) {
            // Ignore this - it's what ww want to see
        }
        catch(Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }

    
        try {
            new Locale(null, null, null).getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        }
        catch(IllegalStateException e) {
            // Ignore this - it's what ww want to see
        }
        catch(Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }

        try {
            new Locale(null, null, "p").getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        }
        catch(IllegalStateException e) {
            // Ignore this - it's what ww want to see
        }
        catch(Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }

        try {
            new Locale(null, java.util.Locale.CANADA.getCountry(), "p").getInstance();
            fail("You should not be able to get an instance from a badly initialised locale");
        }
        catch(IllegalStateException e) {
            // Ignore this - it's what ww want to see
        }
        catch(Throwable e) {
            fail("Wrong exception thrown when getting an instance from a badly initialised locale");
        }

        new Locale(CANADA.getLanguage(), null, null).getInstance();
        new Locale(CANADA.getLanguage(), CANADA.getCountry(), null).getInstance();
        new Locale(CANADA.getLanguage(), CANADA.getCountry(), "p").getInstance();
    }
    
    @Test
    public void testLocaleToXML() throws Exception {
        Locale locale;
        
        locale=new Locale(CANADA.getLanguage(), CANADA.getCountry(), "p");
        
        String xml=getCore().toXML(locale).toString();
        
        assertTrue(xml.indexOf("language=\"en\"")>0);
        assertTrue(xml.indexOf("country=\"CA\"")>0);
        assertTrue(xml.indexOf("variant=\"p\"")>0);
    }

    @Test
    public void testLocaleFromXML() throws Exception {
        Locale locale;
        
        String xml="<locale language='de' country='DE' variant='p' xsi:type='java:com.ail.core.Locale' xmlns:xsi='http://www.w3.org/2001/XMLSchema-instance'/>";

        locale=getCore().fromXML(Locale.class, new XMLString(xml));
        
        assertEquals("DE", locale.getCountry());
        assertEquals("de", locale.getLanguage());
        assertEquals("p", locale.getVariant());
    }
}
