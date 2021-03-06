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

import com.ail.core.Core;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import java.util.TimeZone;

/**
 * This test is used to ensure that the VersionEffectiveDate mechenism works across timezones. The
 * test is <b>not run like a normal junit test</b>. Instead it is run from an ant script (TimezoneTest.xml).
 * This is necessary in order to ensure that the test switches between timezones cleanly.<p>
 * In summary, this is what the test does:<ol>
 * <li>In the GMT timezone:<ol>
 *  <li>Reset a test class's configuration.</li>
 *  <li>Update the configure by setting the paramater "TestParameter" to the value "2"</li>
 * </ol></li>
 * <li>In the EST timezone:<ol>
 * <li>Load the configuration and fetch the value of "TestParameter"</li>
 * <li>Fail if the test parameter's value is anything other than 2</li>
 * </ol></li>
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:24 $
 * @source $Source: /home/bob/CVSRepository/projects/core/test.jar/com/ail/coretest/TimezoneTest.java,v $
 */
public class TimezoneTest extends CoreUserTestCase {

    /**
     * Constructs a test case with the given name.
     */
    public TimezoneTest(String name) {
        super(name);
    }

    public static Test suite() {
		TestSuite tests=new TestSuite(TimezoneTest.class);
        return tests;
    }


	public static void main(String[] args) {
		TestRunner.run(suite());
	}

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() {
        System.out.println("In oneTimeSetup with user.timezone="+TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT));
        ConfigurationHandler.reset();

        if (TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).equals("GMT")) {
            setVersionEffectiveDate(new VersionEffectiveDate());
            tidyUpTestData();
            setCore(new Core(this));
            getCore().resetConfiguration();
            setVersionEffectiveDate(new VersionEffectiveDate());
            resetConfiguration();
        }
        else {
            setVersionEffectiveDate(new VersionEffectiveDate());
            setCore(new Core(this));
            getCore().resetConfiguration();
        }

        setVersionEffectiveDate(new VersionEffectiveDate());
        ConfigurationHandler.reset();
        System.out.println("VersionEffectiveDate:"+getVersionEffectiveDate().getTime());
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    protected void tearDown() {
//		tidyUpTestData();
    }

    /**
     * If the timezone in EST:
     * <ol>
     * <li>Get the value of TestParameter</li>
     * <li>Fail if the value is not 2</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ol>
     * @throws Exception
     */
    public void testQueryConfig() throws Exception {
        if (TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).equals("EST")) {
            String param=getCore().getParameterValue("TestParameter");
            assertNotNull(param);
            assertEquals("2", param);
        }
    }

    /**
     * If the timezone is GMT:
     * <ol>
     * <li>Get the class' configuration</li>
     * <li>Set the value of TestParameter to 2</li>
     * <li>Save the configuration</li>
     * </ol>
     * @throws Exception
     */
    public void testUpdateConfig() throws Exception {
        if (TimeZone.getDefault().getDisplayName(false, TimeZone.SHORT).equals("GMT")) {
            Configuration config=getConfiguration();
            config.findParameter("TestParameter").setValue("2");
            getCore().setConfiguration(config);
        }
    }

}
