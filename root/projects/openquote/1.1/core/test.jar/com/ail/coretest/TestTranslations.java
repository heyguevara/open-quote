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

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.Core;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.language.Translation;
import com.ail.core.language.Translations;

/**
 */
public class TestTranslations extends CoreUserTestCase {
    static boolean setup=false;
    
    /**
     * Constructs a test case with the given name.
     */
    public TestTranslations(String name) {
        super(name);
    }

    public static Test suite() {
		return new TestSuite(TestTranslations.class);
    }

	public static void main(String[] args) {
		TestRunner.run(suite());
	}

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
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
    protected void tearDown() {
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
    public void testLocaleConstruction() throws Exception {
        Translations translations=new Translations("one");

        Translation translation=new Translation("one");
        translation.getKey().put("key1", "String number one");
        translation.getKey().put("key2", "String number two");
        translation.getKey().put("key3", "String number three");
        translation.getKey().put("key4", "String number four");
        translations.getTranslation().add(translation);
        
        translation=new Translation("two", "one");
        translation.getKey().put("key1", "Second String number one");
        translation.getKey().put("key2", "Second String number two");
        translation.getKey().put("key3", "Second String number three");
        translations.getTranslation().add(translation);

        assertEquals("String number one", translations.translate("one", "key1"));
        assertEquals("Second String number one", translations.translate("two", "key1"));
        assertEquals("String number four", translations.translate("two", "key4"));
        assertEquals("String number four", translations.translate("three", "key4"));
        assertEquals("$$key5$$", translations.translate("three", "key5"));
        assertEquals("$$key5$$", translations.translate("two", "key5"));
        assertEquals("$$key5$$", translations.translate("one", "key5"));
        
        System.out.println(getCore().toXML(translations));
    }
}
