/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.textui.TestRunner;

/**
 * Roll up test for everything in the project.
 */
public class TestAll extends TestCase {
   /**
    * Constructs a test case with the given name.
    */
   public TestAll(String name) {
       super(name);
   }

   public static Test suite() {
       TestSuite ts=new TestSuite();

       ts.addTest(TestCurrencyAmount.suite());
       ts.addTest(TestDateOfBirth.suite());
       ts.addTest(TestRate.suite());
       ts.addTest(TestParty.suite());
       return ts;
   }

   public static void main(String[] args) {
       TestRunner.run(suite());
   }

}
