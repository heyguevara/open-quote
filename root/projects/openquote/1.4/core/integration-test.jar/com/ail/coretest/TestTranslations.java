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

package com.ail.coretest;

import static java.util.Locale.US;
import static java.util.Locale.UK;
import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.Locale;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.language.Translation;
import com.ail.core.language.Translations;

/**
 */
public class TestTranslations extends CoreUserTestCase {
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
        Translations translations=new Translations(UK.toString());

        Translation translation=new Translation(UK.toString());
        translation.getKey().put("key1", "String number one");
        translation.getKey().put("key2", "String number two");
        translation.getKey().put("key3", "String number three");
        translation.getKey().put("key4", "String number four");
        translations.getTranslation().add(translation);
        
        translation=new Translation(US.toString(), UK.toString());
        translation.getKey().put("key1", "Second String number one");
        translation.getKey().put("key2", "Second String number two");
        translation.getKey().put("key3", "Second String number three");
        translations.getTranslation().add(translation);

        java.util.Locale saved=Locale.getThreadLocale();
        Locale.setThreadLocale(US);

        assertEquals("Second String number one", translations.translate("key1", "two"));
        assertEquals("String number four", translations.translate("key4", "two"));
        assertEquals("String number four", translations.translate("key4", "three"));
        assertEquals("three", translations.translate("key5", "three"));
        assertEquals("two", translations.translate("key5", "two"));
        
        Locale.setThreadLocale(saved);
    }
    
    @Test
    public void testFromXML() throws Exception {
        XMLString instanceXml = new XMLString(this.getClass().getResourceAsStream("TestTranslations.xml"));
        Translations instanceObj = (Translations) super.getCore().fromXML(Translations.class, instanceXml);
        assertNotNull(instanceObj);

        java.util.Locale saved=Locale.getThreadLocale();
        Locale.setThreadLocale(US);

        assertEquals("Second <b>String</b> number two", instanceObj.translate("key2", "two"));

        Locale.setThreadLocale(saved);
    }
}
