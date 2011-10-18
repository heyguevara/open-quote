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

import junit.framework.Test;
import junit.framework.TestCase;

import com.ail.party.Address;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/05/21 17:37:56 $
 * @source $Source: /home/bob/CVSRepository/projects/common/test.jar/com/ail/commercialtest/TestParty.java,v $
 */
public class TestParty extends TestCase {
    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestParty(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestParty.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test valid arguments for the Rate constructor
     * @throws Exception
     */
    public void testGoodFormats() throws Exception {
        Address addr=new Address();
        
        addr.setLine1("Little Owls");
        addr.setLine2("Huntington Road");
        addr.setTown("Crowborough");
        addr.setCounty("East Sussex");
        addr.setPostcode("TN6 2LJ");
        
        assertEquals("Little Owls, Huntington Road, Crowborough, East Sussex. TN6 2LJ", addr.toString());
    }

}
