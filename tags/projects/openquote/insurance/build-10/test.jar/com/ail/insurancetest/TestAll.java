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
package com.ail.insurancetest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/10/07 11:58:19 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/test.jar/com/ail/insurancetest/TestAll.java,v $
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

       ts.addTest(TestAcceptance.suite());
       ts.addTest(TestAssessmentSheet.suite());
       ts.addTest(TestPolicyStatus.suite());
       ts.addTest(TestQuotation.suite());
       ts.addTest(TestQuotationEjbXml.suite());
       ts.addTest(TestSubrogation.suite());
       return ts;
   }

   public static void main(String[] args) {
       TestRunner.run(suite());
   }

}
