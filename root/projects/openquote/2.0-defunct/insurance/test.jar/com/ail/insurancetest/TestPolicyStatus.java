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

package com.ail.insurancetest;

import junit.framework.Test;
import junit.framework.TestCase;

import com.ail.insurance.policy.PolicyStatus;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 23:19:08 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/test.jar/com/ail/insurancetest/TestPolicyStatus.java,v $
 */
public class TestPolicyStatus extends TestCase {
    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestPolicyStatus(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestPolicyStatus.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     */
    public void testGettingStatusByName() {
        PolicyStatus ps=PolicyStatus.valueOf("APPLICATION");
        assertNotNull(ps);
        assertEquals(PolicyStatus.APPLICATION, ps);

        try {
            ps=PolicyStatus.valueOf("AStatusThatDoesntExist");
            fail("found a nonexistant status type!");
        }
        catch(IllegalArgumentException e) {
            // ignore this - its what we want to see.
        }
    }
}
