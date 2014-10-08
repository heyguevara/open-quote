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

import junit.framework.TestCase;
import junit.framework.Test;

import java.util.Calendar;
import java.util.Locale;

import com.ail.util.DateOfBirth;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/16 21:08:54 $
 * @source $Source: /home/bob/CVSRepository/projects/common/test.jar/com/ail/commercialtest/TestDateOfBirth.java,v $
 */
public class TestDateOfBirth extends TestCase {
    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestDateOfBirth(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestDateOfBirth.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test DOB calculations. The Dob class includes functions to
     * calculate age today and age on a date.
     */
    public void testAgeCalculations() throws Exception {
        Calendar c=Calendar.getInstance();
        c.set(1964, 11, 8);
        DateOfBirth dob=new DateOfBirth(c.getTime());

        c.set(1980, 10, 9);
        assertEquals(16, dob.ageAtDate(c.getTime()));
    }

    /**
     * Test constructor that takes year month day values.
     */
    public void testYMDConstructor() {
        DateOfBirth dob=new DateOfBirth(1964, 11, 8);
        Calendar c=Calendar.getInstance();
        c.set(1980, 10, 9);
        assertEquals(16, dob.ageAtDate(c.getTime()));
    }

    /**
     * Test that the locale specific text formatting of dates
     * works.
     * @throws Exception
     */
    public void testStringDobMethods() throws Exception {
        // Put the JVM into UK locale mode
        Locale.setDefault(new Locale("en", "GB"));

        // Create a date and check the string format works
        DateOfBirth dob=new DateOfBirth(1964, 11, 8);
        assertEquals("08/12/64", dob.getDateAsString());

        // Set the date as a String, and check that when fetched it is the same.
        dob.setDateAsString("13/12/1964");
        assertEquals("13/12/64", dob.getDateAsString());
    }

	/**
	 * Test that the locale specific text formatting of dates
	 * works.
	 * @throws Exception
	 */
	public void testStringDobMethodsSpecificLocale() throws Exception {
		// Create a date and check the string format works
		DateOfBirth dob=new DateOfBirth(1964, 11, 8);
		assertEquals("08/12/1964", dob.getDateAsString("dd/MM/yyyy"));

		// Set the date as a String, and check that when fetched it is the same.
		dob.setDateAsString("13/12/1964", "dd/MM/yyyy");
		assertEquals("13/12/64", dob.getDateAsString("dd/MM/yy"));
	}
	
	public void testSetAsStringFormat() throws Exception {
		DateOfBirth dob=new DateOfBirth();
		dob.setDateAsString("01/01/00");
		assertEquals("01/01/00", dob.getDateAsString("dd/MM/yy"));
		
	}
}
