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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreProxy;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.server.ServerBean;
import com.ail.core.logging.LoggerCommand;
import com.ail.core.logging.Severity;
import com.ail.core.product.listproducts.ListProductsCommand;
import com.ail.coretest.service.TestArgImp;
import com.ail.coretest.service.TestCommand;
import com.ail.coretest.service.TestService;

/**
 * Test that basic service invocation works as specified. Note: These tests
 * assume that the JDBCConfigurationLoader is being used.
 */
public class TestServiceInvocation extends CoreUserTestCase {
    private static boolean oneTimeSetupDone=false;
    

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and
     * delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        setCore(new Core(this));

        if (!oneTimeSetupDone) {
            System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser"); 
            System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
            System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
            System.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
            System.setProperty("java.naming.provider.url","jnp://localhost:1099");
    
            tidyUpTestData();
    
            ConfigurationHandler.reset();
    
            new ServerBean().resetNamedConfiguration("all");
            new TestService().resetConfiguration();
            resetConfiguration();
    
            oneTimeSetupDone=true;
        }

        ConfigurationHandler.reset();

    }

    /**
     * Always select the latest configuration.
     * 
     * @return
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    /**
     * Test that services writen in BeanShell work correctly. BeanShell services
     * allow a service's logic to be defined as a script in the services
     * configuration. This is similar to using rules in a lot of ways as the
     * logic can be changed without the need to recompile anything. This test
     * The TestBeanShellService simply adds two numbers (X & Y) together to get
     * R, if X>100 it is multiplied by 2 before being added to Y.
     * <ol>
     * <li>Create an instance of the TestBeanShellService.</li>
     * <li>Invoke the service with X=21 and Y=3</li>
     * <li>Fail if the result (R) doesn't equal 24</li>
     * <li>Invoke the service again with X=101 and Y=10</li>
     * <li>Fail if the result (R) doesn't equal 212</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    @Test
    public void testBeanShellService() throws Exception {
        Timer.start("testBeanShellService");
        
        TestCommand command;
        
        command = (TestCommand) getCore().newCommand("TestBeanShellService");
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());

        Timer.split("testBeanShellService");

        command = (TestCommand) getCore().newCommand("TestBeanShellService");
        command.setX(101);
        command.setY(10);
        command.invoke();
        assertEquals(212, command.getR());

        Timer.split("testBeanShellService");
        
        for(int i=0 ; i<50 ; i++) {
            command = (TestCommand) getCore().newCommand("TestBeanShellService");
            command.setX(101);
            command.setY(10);
            command.invoke();
            assertEquals(212, command.getR());
        }

        Timer.stop("testBeanShellService");
    }
    
    @Test
    public void testXSLT() throws Exception {
        ListProductsCommand command = (ListProductsCommand) getCore().newCommand("TestXSLTService");
        command.invoke();
        assertEquals(2, command.getProductsRet().size());
    }

    @Test
    public void testXSLTPerformance() throws Exception {
        {
            Timer.start("testXSLTPerformance first");
    
            ListProductsCommand command = (ListProductsCommand) getCore().newCommand("TestXSLTService");
            command.invoke();
            assertEquals(2, command.getProductsRet().size());
            
            Timer.stop("testXSLTPerformance first");
        }

        {
            Timer.start("testXSLTPerformance second");
    
            ListProductsCommand command = (ListProductsCommand) getCore().newCommand("TestXSLTService");
            command.invoke();
            assertEquals(2, command.getProductsRet().size());
            
            Timer.stop("testXSLTPerformance second");
        }

        {
            Timer.start("testXSLTPerformance 10");

            for(int i=0 ; i<10 ; i++)
            {
                ListProductsCommand command = (ListProductsCommand) getCore().newCommand("TestXSLTService");
                command.invoke();
                assertEquals(2, command.getProductsRet().size());
            }

            Timer.stop("testXSLTPerformance 10");
        }
    }

    /**
     * The DroolsDecisionTableAccessor provides access to drools decision table
     * based services. This test ensures that 'happy path' access to this type
     * of service works okay.
     * @throws Exception
     */
    @Test
    public void testDroolsDecisionTable() throws Exception {
        TestCommand command=(TestCommand)getCore().newCommand("TestDroolsDecisionTable");
        command.setVersionArgRet(new Version());
        
        long start1=System.currentTimeMillis();
        command.getVersionArgRet().setAuthor("T.S.Elliot");
        command.getVersionArgRet().setComment("Fish Cakes");
        command.invoke();
        assertEquals("The Times", command.getVersionArgRet().getSource());
        long end1=System.currentTimeMillis();

        command.getVersionArgRet().setAuthor("H.G.Wells");
        command.getVersionArgRet().setComment("Jaffa Cakes");
        command.invoke();
        assertEquals("Daily Mail", command.getVersionArgRet().getSource());

        long start2=System.currentTimeMillis();
        command.getVersionArgRet().setAuthor("E.Blyton");
        command.getVersionArgRet().setComment("Indoor Cakes");
        command.invoke();
        assertEquals("Indoor Bakers Weekly", command.getVersionArgRet().getSource());
        long end2=System.currentTimeMillis();
        
        // Decision tables are so quick these days that this test is almost not necessary. I've put
        // a cut-off of 2ms on the second call, if it was faster than that we'll assume a success.
        assertTrue("caching isn't working - initial call should be 100s of times slower than later calls. (initial: "+(end1-start1)+", subsequent:"+(end2-start2)+")", (end2-start2<2) || (end1-start1)/10 > (end2-start2));
    }

    /**
     * The DroolsDecisionTableAccessor provides support for "inheritance" which allows the rules
     * defined in one rule spreadsheet to inherit rules from another. This test checks that this
     * feature is working.
     * @throws Exception
     */
    @Test
    public void testDroolsDecisionTableInheritance() throws Exception {
        TestCommand command=(TestCommand)getCore().newCommand("TestDroolsDecisionTableInheritance");
        command.setVersionArgRet(new Version());
        
        Timer.start("Initial decision table");
        command.getVersionArgRet().setAuthor("T.S.Elliot");
        command.getVersionArgRet().setComment("Fish Cakes");
        command.invoke();
        assertEquals("The Times", command.getVersionArgRet().getSource());
        Timer.stop("Initial decision table");
        
        command.getVersionArgRet().setAuthor("H.G.Wells");
        command.getVersionArgRet().setComment("Jaffa Cakes");
        command.invoke();
        assertEquals("Evening Post", command.getVersionArgRet().getSource());

        command.getVersionArgRet().setAuthor("E.Blyton");
        command.getVersionArgRet().setComment("Indoor Cakes");
        command.invoke();
        assertEquals("Indoor Bakers Weekly", command.getVersionArgRet().getSource());

        Timer.start("Later decision table");
        command.getVersionArgRet().setAuthor("P.K.Dick");
        command.getVersionArgRet().setComment("Christmas Cake");
        command.invoke();
        assertEquals("Daily Star", command.getVersionArgRet().getSource());
        Timer.stop("Later decision table");
    }

    /**
     * The DroolsDecisionTableAccessor provides access to drools decision table
     * based services. This test ensures that 'happy path' access to this type
     * of service works okay.
     * @throws Exception
     */
    @Test
    public void testDroolsInheritance() throws Exception {
        TestCommand command=(TestCommand)getCore().newCommand("TestDroolsInheritance");
        
        command.setX(21);
        command.setY(34);
        command.invoke();
        assertEquals(55, command.getR());

        command.setX(101);
        command.setY(10);
        command.invoke();
        assertEquals(313, command.getR());

        command.setX(100);
        command.setY(107);
        command.invoke();
        assertEquals(100, command.getR());
    }

    @Test
    public void testDroolsService() throws Exception {
        TestCommand command = (TestCommand) getCore().newCommand("TestDroolsService");
        command.setX(21);
        command.setY(34);
        command.invoke();
        assertEquals(55, command.getR());

        command.setX(101);
        command.setY(10);
        command.invoke();
        assertEquals(212, command.getR());
    }

    @Test
    public void testDroolsServiceUrlScript() throws Exception {
        TestCommand command = (TestCommand) getCore().newCommand("TestDroolsUrlLoader");
        command.setX(21);
        command.setY(34);
        command.invoke();
        assertEquals(55, command.getR());

        command.setX(101);
        command.setY(10);
        command.invoke();
        assertEquals(212, command.getR());
    }

    @Test
    public void testDroolsXMLService() throws Exception {
        TestCommand command = (TestCommand) getCore().newCommand("TestDroolsXMLService");
        command.setX(21);
        command.setY(34);
        command.invoke();
        assertEquals(55, command.getR());

        command.setX(101);
        command.setY(10);
        command.invoke();
        assertEquals(212, command.getR());
    }

    /**
     * Test the "normal" multi line service invocation.
     * <ol>
     * <li>Get the TestService (which adds two ints together)
     * <li>
     * <li>Set the first int to 1 using the setter.</li>
     * <li>Set the second int to 1 using the setter.</li>
     * <li>invoke the service.</li>
     * <li>Fail if the result returned isn't 2.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    @Test
    public void testMultiLineInvoke() throws Exception {
        TestCommand command = (TestCommand) getCore().newCommand("TestService");
        command.setX(1);
        command.setY(1);
        command.invoke();
        assertEquals(2, command.getR());
    }

    /**
     * Test the single line service invocation facility.
     * <ol>
     * <li>Invoke the TestService passing it arguments of 1 and 1.</li>
     * <li>Fail if the result isn't 2.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * <ol>
     */
    @Test
    public void testSingleLineInvoke() throws Exception {
        TestArgImp r = (TestArgImp) getCore().invokeService("TestService", new TestArgImp(1, 1));
        assertEquals(2, r.getR());
    }

    @Test
    public void testServiceLogging() throws Exception {
        getCore().invokeService("TestServiceNoLogging", new TestArgImp(1, 1));
        getCore().invokeService("TestServiceCallLogging", new TestArgImp(1, 1));
        getCore().invokeService("TestServiceFullLogging", new TestArgImp(1, 1));
    }

    @Test
    public void testJMSService() throws Exception {
        LoggerCommand command = (LoggerCommand) getCore().newCommand("TestJMSCommand");
        command.setMessage("A test message");
        command.setSeverity(Severity.INFO);
        command.setCallersCore(new CoreProxy());
        command.setDate(new Date());
        command.invoke();
    }
    
    @Test
    public void testDroolsAccessorCache() throws Exception {
        long startAt, endAt;

        TestCommand command = (TestCommand) getCore().newCommand("TestDroolsService2");
        command.setX(21);
        command.setY(34);

        startAt = System.currentTimeMillis();
        command.invoke();
        assertEquals(55, command.getR());
        endAt = System.currentTimeMillis();
        long firstCall = endAt - startAt;

        startAt = System.currentTimeMillis();
        command.setR(0);
        command.invoke();
        assertEquals(55, command.getR());
        endAt = System.currentTimeMillis();
        long secondCall = endAt - startAt;

        System.out.println("first took:" + firstCall + " second took:" + secondCall);
    }

    /**
     * Test that the BeanShell accessor's inheritance support is working. The BeanShell acceessor's
     * implementation of inheritance (using the "extends" parameter) is really more akin to chaining
     * two or more accessors together. The command named in the extends property is executed, and
     * the results of that execution are passed into the extending script for it to do with as it
     * pleases. The extended command need not be another BeanShell based command, it could be any
     * type of command.<p>
     * The test configuration (TestServiceInvocationDefaultConfig.xml) defines the BeanShell command
     * "TestBeanshellInheritanceService" which extends "TestBeanShellService". "TestBeanShellService" 
     * applies a simple rule: <code>if x&lt;100 r=x+y; else r=2*x+y;)</code>. The inheriting service 
     * adds it's own rule: <code>if (x&gt;1000) r=x-(2*y);</code>
     */
    @Test
    public void testBeanShellInheritance() throws Exception {
        TestCommand command=(TestCommand)getCore().newCommand("TestBeanShellInheritance");
        
        // Test the first rule in the base class (x<100 so r=x+y)
        command.setX(9);
        command.setY(10);
        command.invoke();
        assertEquals(19, command.getR());
    }
    
    /**
     * The Beanshell accessor supports the loading of scripts from URLs. This test executes
     * a command configured that way.
     * @throws Exception
     */
    @Test
    public void testBeanshellUrlLoader() throws Exception {
        TestCommand command=(TestCommand)getCore().newCommand("TestBeanShellUrlLoader");
        
        // Test the first rule in the base class (x<100 so r=x+y)
        command.setX(9);
        command.setY(10);
        command.invoke();

        assertEquals(19, command.getR());
    }
    
    @Test
    public void testDroolsMemoryLeak() throws Exception {
        int i = 0;

        try {
            for (i = 0; i <= 100000; i++) {
                TestCommand command = (TestCommand) getCore().newCommand("TestDroolsService");
                command.setX(21);
                command.setY(34);
                command.invoke();
                assertEquals(55, command.getR());
            }
        } catch (OutOfMemoryError e) {
            fail("out of memory at:" + i);
        }
    }

    /**
     * The Janino Accessor provides access to services whose logic is defined in a java like syntax.
     * This tests ensures that the accessor works in its simplest form and performs some rudimentary performance 
     * tests.
     * @throws Exception
     */
    @Test
    public void testSimpleJaninoService() throws Exception {
        Timer.start("testSimpleJaninoService");
        
        TestCommand command;
        
        command = (TestCommand) getCore().newCommand("TestJaninoService");
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());

        Timer.split("testSimpleJaninoService");

        command = (TestCommand) getCore().newCommand("TestJaninoService");
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());

        Timer.split("testSimpleJaninoService");

        for(int i=0 ; i<50 ; i++) {
            command = (TestCommand) getCore().newCommand("TestJaninoService");
            command.setX(101);
            command.setY(10);
            command.invoke();
            assertEquals(212, command.getR());
        }

        Timer.stop("testSimpleJaninoService");

        command = (TestCommand) getCore().newCommand("TestJaninoUrlService");
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());
    }

    /**
     * The Janino Accessor supports inheritance by allowing Janino services to be 'chained'. If one janino
     * service may "extends" another, the logic of the extended service is executed before the logic of the
     * extending service. This test checks that this extension mechanism is working.
     * @throws Exception
     */
    @Test
    public void testJaninoInheritance() throws Exception {
        TestCommand command=(TestCommand)getCore().newCommand("TestJaninoInheritanceService");
        
        // Test the logic in the TestJaninoInheritanceService itself (x>1000 so r=x-(2*y))
        command.setX(1001);
        command.setY(10);
        command.invoke();
        assertEquals(981, command.getR());

        // Now test the logic in the service which TestJaninoInheritanceService extends (x>100 so r=2*x+y)
        command.setX(110);
        command.setY(3);
        command.invoke();
        assertEquals(223, command.getR());

        // Now test the other logic in the service which TestJaninoInheritanceService extends (x<100 so r=x+y)
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());
    }
    
    @Test
    public void testVelocity() throws Exception {
        TestCommand command = (TestCommand) getCore().newCommand("TestVelocityService");
        command.setX(21);
        command.setY(34);

        Timer.start("testVelocity");
        command.invoke();
        Timer.split("testVelocity");
        command.invoke();
        Timer.stop("testVelocity");
        
        assertTrue("Result does not contain expected string: 'The value of X is: 21'", command.getStringRet().contains("The value of X is: 21"));
        assertTrue("Result does not contain expected string: 'The value of Y is: 34'", command.getStringRet().contains("The value of Y is: 34"));
        assertTrue("Result does not contain expected string: 'Total is: 55'", command.getStringRet().contains("Total is: 55"));
    }
}
