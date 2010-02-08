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
package com.ail.coretest;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.framework.TestCase;
import junit.textui.TestRunner;
import com.ail.coretest.configuration.TestEJBConfigurationLoader;
import com.ail.coretest.configuration.TestJDBCConfigurationLoader;

public class TestAll extends TestCase {
   /**
    * Constructs a test case with the given name.
    */
   public TestAll(String name) {
       super(name);
   }

   public static Test suite() {
       TestSuite ts=new TestSuite();

       ts.addTest(TestJDBCConfigurationLoader.suite());
       ts.addTest(TestEJBConfigurationLoader.suite());
       ts.addTest(TestConfigurationServices.suite());
       ts.addTest(TestCoreConfigReset.suite());
       ts.addTest(TestCoreConfiguration.suite());
       ts.addTest(TestCoreFactory.suite());
       ts.addTest(TestCoreHibernatePersistence.suite());
       ts.addTest(TestCoreLogging.suite());
       ts.addTest(TestCoreValidator.suite());
       ts.addTest(TestCoreXMLBinding.suite());
       ts.addTest(TestCoreXMLMapping.suite());
       ts.addTest(TestProductServices.suite());
       ts.addTest(TestServiceInvocation.suite());
       ts.addTest(TestTypeClone.suite());
       ts.addTest(TestTypeXpath.suite());
       ts.addTest(TestUrlHandlers.suite());
       ts.addTest(TestXMLString.suite());
       ts.addTest(TestFactoryTypeMerging.suite());
       ts.addTest(TestAttribute.suite());
       ts.addTest(TestUtilityFunctions.suite());
       ts.addTest(TestCoreXMLStringMapping.suite());
       ts.addTest(TestGenerateDocument.suite());
       ts.addTest(TestConfigurationServices.suite());
       ts.addTest(TestExceptionRecord.suite());
       ts.addTest(TestLocale.suite());
       ts.addTest(TestTranslations.suite());
       ts.addTest(TestExceptionRecord.suite());
       
       return ts;
   }

   public static void main(String[] args) {
       TestRunner.run(suite());
   }
}
