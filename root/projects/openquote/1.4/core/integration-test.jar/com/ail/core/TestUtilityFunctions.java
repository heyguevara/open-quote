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

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.CoreUserTestCase;

public class TestUtilityFunctions extends CoreUserTestCase {
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
     * Always select the latest configurations
     * @return
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
		tidyUpTestData();
    }

    
    /**
     * Test that the Doc Gen service fails if no arguments are supplied.
     * @throws Exception
     */
    @Test
    public void testStringExpand() throws Exception {
        Version v=(Version)getCore().newType("Version");
        
        v.setAuthor("H.G. Wells");
        v.setComment("Nice House, good tea");
        v.setCopyright("(c) me");

        String st=Functions.expand("Author: ${/author}, '${/comment}'. ${/copyright}", v);
        assertEquals("Author: H.G. Wells, 'Nice House, good tea'. (c) me", st);
    }
}
