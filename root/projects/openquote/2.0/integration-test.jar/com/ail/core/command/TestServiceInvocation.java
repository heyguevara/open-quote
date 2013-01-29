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

package com.ail.core.command;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreProxy;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.Timer;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.server.ServerBean;
import com.ail.core.dummyservice.DummyService.DummyArgument;
import com.ail.core.dummyservice.DummyService.DummyCommand;
import com.ail.core.dummyservice.DummyService;
import com.ail.core.logging.Severity;
import com.ail.core.logging.LoggingService.LoggingCommand;
import com.ail.core.product.ListProductsService.ListProductsCommand;

/**
 * Test that basic service invocation works as specified. Note: These tests
 * assume that the JDBCConfigurationLoader is being used.
 */
public class TestServiceInvocation extends CoreUserBaseCase {
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
            System.setProperty("java.naming.factory.url.pkgs", "org.jboss.ejb.client.naming");
            System.setProperty("java.naming.factory.initial","org.jboss.naming.remote.client.InitialContextFactory");
            System.setProperty("java.naming.provider.url","remote://localhost:4447");
            System.setProperty("jboss.naming.client.ejb.context", "true");

    
            tidyUpTestData();
    
            ConfigurationHandler.resetCache();
    
            new ServerBean().resetCoreConfiguration();
            new ServerBean().resetAllConfigurations();
            new DummyService().resetConfiguration();
            resetConfiguration();
    
            oneTimeSetupDone=true;
        }

        ConfigurationHandler.resetCache();

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
     * Test that services written in BeanShell work correctly. BeanShell services
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
        
        DummyCommand command;
        
        command = getCore().newCommand("TestBeanShellService", DummyCommand.class);
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());

        Timer.split("testBeanShellService");

        command = getCore().newCommand("TestBeanShellService", DummyCommand.class);
        command.setX(101);
        command.setY(10);
        command.invoke();
        assertEquals(212, command.getR());

        Timer.split("testBeanShellService");
        
        for(int i=0 ; i<50 ; i++) {
            command = getCore().newCommand("TestBeanShellService", DummyCommand.class);
            command.setX(101);
            command.setY(10);
            command.invoke();
            assertEquals(212, command.getR());
        }

        Timer.stop("testBeanShellService");
    }
    
    @Test
    public void testXSLT() throws Exception {
        ListProductsCommand command = getCore().newCommand("TestXSLTService", ListProductsCommand.class);
        command.invoke();
        assertEquals(2, command.getProductsRet().size());
    }

    @Test
    public void testXSLTPerformance() throws Exception {
        {
            Timer.start("testXSLTPerformance first");
    
            ListProductsCommand command = getCore().newCommand("TestXSLTService", ListProductsCommand.class);
            command.invoke();
            assertEquals(2, command.getProductsRet().size());
            
            Timer.stop("testXSLTPerformance first");
        }

        {
            Timer.start("testXSLTPerformance second");
    
            ListProductsCommand command = getCore().newCommand("TestXSLTService", ListProductsCommand.class);
            command.invoke();
            assertEquals(2, command.getProductsRet().size());
            
            Timer.stop("testXSLTPerformance second");
        }

        {
            Timer.start("testXSLTPerformance 10");

            for(int i=0 ; i<10 ; i++)
            {
                ListProductsCommand command = getCore().newCommand("TestXSLTService", ListProductsCommand.class);
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
        DummyCommand command=getCore().newCommand("TestDroolsDecisionTable", DummyCommand.class);
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
        DummyCommand command=getCore().newCommand("TestDroolsDecisionTableInheritance", DummyCommand.class);
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
        DummyCommand command=getCore().newCommand("TestDroolsInheritance", DummyCommand.class);
        
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
        DummyCommand command = getCore().newCommand("TestDroolsService", DummyCommand.class);
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
        DummyCommand command = getCore().newCommand("TestDroolsUrlLoader", DummyCommand.class);
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
    public void testDroolsServiceStringMatchingRule() throws Exception {
        DummyCommand command = getCore().newCommand("TestDroolsUrlLoader", DummyCommand.class);
        command.setX(100);
        command.setName("1234");
        command.setR(0);
        command.invoke();
        assertEquals(999, command.getR());
 
        command.setName("A1234");
        command.setR(0);
        command.invoke();
        assertEquals(0, command.getR());
 }
    
    @Test
    public void testDroolsXMLService() throws Exception {
        DummyCommand command = getCore().newCommand("TestDroolsXMLService", DummyCommand.class);
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
     * <li>Get the DummyService (which adds two ints together)
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
        DummyCommand command = getCore().newCommand("TestService", DummyCommand.class);
        command.setX(1);
        command.setY(1);
        command.invoke();
        assertEquals(2, command.getR());
    }

    /**
     * Test the single line service invocation facility.
     * <ol>
     * <li>Invoke the DummyService passing it arguments of 1 and 1.</li>
     * <li>Fail if the result isn't 2.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * <ol>
     */
    @Test
    public void testSingleLineInvoke() throws Exception {
        DummyArgument tai=getCore().newType(DummyArgument.class);
        tai.setX(1);
        tai.setY(1);

        DummyArgument r = (DummyArgument) getCore().invokeService(DummyCommand.class, tai);
        assertEquals(2, r.getR());
    }

    @Test
    public void testServiceLogging() throws Exception {
        DummyArgument tai=getCore().newType(DummyArgument.class);
        tai.setX(1);
        tai.setY(1);
        
        getCore().invokeService("TestServiceNoLogging", DummyCommand.class, tai);
        getCore().invokeService("TestServiceCallLogging", DummyCommand.class, tai);
        getCore().invokeService("TestServiceFullLogging", DummyCommand.class, tai);
    }

    /**
     * Test that the JMS based command queue is working.
     * <ul>
     * <li>Create a command which appends a message to the system log (JBoss server log)</li>
     * <li>Send the command to the command queue</li>
     * <li>Wait for 10 seconds</li>
     * <li>Check that the command has executed by reading the server log</li>
     * <li>Fail if the test message is not found in the log</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ul>
     * @throws Exception
     */
    @Test
    public void testJMSService() throws Exception {
        String testMessage="A test message from the TestJMSCommand";
        
        LoggingCommand loggingCommand = getCore().newCommand("TestJMSCommand", LoggingCommand.class);
        loggingCommand.setMessage(testMessage);
        loggingCommand.setSeverity(Severity.INFO);
        loggingCommand.setCallersCore(new CoreProxy());
        loggingCommand.setDate(new Date());
        loggingCommand.invoke();
        
        // wait 10 seconds, then check the log for the message that the command server should
        // have output as a response to being sent a TestJMSCommand
        Thread.sleep(1000*10);
        
        File logFile=new File("./target/liferay-portal-6.1.1-ce-ga2/jboss-7.1.1/standalone/log/server.log");
        BufferedReader logReader=new BufferedReader(new InputStreamReader(new FileInputStream(logFile)));
        boolean foundLogEntry=false;
        for(String line=logReader.readLine() ; line!=null ; line=logReader.readLine()) {
            foundLogEntry |= line.contains(testMessage);
        }
        logReader.close();
        
        assertTrue("JMS Service does not seems to have executed", foundLogEntry);
    }
    
    @Test
    public void testDroolsAccessorCache() throws Exception {
        long startAt, endAt;

        DummyCommand command = getCore().newCommand("TestDroolsService2", DummyCommand.class);
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
        DummyCommand command=getCore().newCommand("TestBeanShellInheritance",DummyCommand.class);
        
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
        DummyCommand command=getCore().newCommand("TestBeanShellUrlLoader",DummyCommand.class);
        
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
                DummyCommand command = getCore().newCommand("TestDroolsService", DummyCommand.class);
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
        
        DummyCommand command;
        
        command = getCore().newCommand("TestJaninoService", DummyCommand.class);
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());

        Timer.split("testSimpleJaninoService");

        command = getCore().newCommand("TestJaninoService", DummyCommand.class);
        command.setX(21);
        command.setY(3);
        command.invoke();
        assertEquals(24, command.getR());

        Timer.split("testSimpleJaninoService");

        for(int i=0 ; i<50 ; i++) {
            command = getCore().newCommand("TestJaninoService", DummyCommand.class);
            command.setX(101);
            command.setY(10);
            command.invoke();
            assertEquals(212, command.getR());
        }

        Timer.stop("testSimpleJaninoService");

        command = getCore().newCommand("TestJaninoUrlService", DummyCommand.class);
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
        DummyCommand command=getCore().newCommand("TestJaninoInheritanceService", DummyCommand.class);
        
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
    
    @Test(expected=JaninoServiceException.class)
    public void testJaninoInvokeBadArguments() throws Exception {
        DummyCommand command=getCore().newCommand("TestJaninoUrlServiceBadArgument", DummyCommand.class);
        command.invoke();
    }
    
    @Test(expected=JaninoServiceException.class)
    public void testJaninoInvokeNoArguments() throws Exception {
        DummyCommand command=getCore().newCommand("TestJaninoUrlServiceNoArgument", DummyCommand.class);
        command.invoke();
    }
    
    @Test(expected=JaninoServiceException.class)
    public void testJaninoInvokeNoMethod() throws Exception {
        DummyCommand command=getCore().newCommand("TestJaninoUrlServiceNoMethod", DummyCommand.class);
        command.invoke();
    }
    
    @Test
    public void testVelocity() throws Exception {
        StringWriter output=new StringWriter();
        
        DummyCommand command = getCore().newCommand("TestVelocityService", DummyCommand.class);
        command.setX(21);
        command.setY(34);
        command.setWriterArg(output);
        
        Timer.start("testVelocity");
        command.invoke();
        Timer.split("testVelocity");
        command.invoke();
        Timer.stop("testVelocity");
        
        String result=output.toString();
        
        assertTrue("Result does not contain expected string: 'The value of X is: 21'", result.contains("The value of X is: 21"));
        assertTrue("Result does not contain expected string: 'The value of Y is: 34'", result.contains("The value of Y is: 34"));
        assertTrue("Result does not contain expected string: 'Total is: 55'", result.contains("Total is: 55"));
    }
    
    @Test
    public void testEJBService() throws Exception {
        ListProductsCommand lpc=getCore().newCommand("TestEJBService", ListProductsCommand.class);
        lpc.invoke();
        assertNotNull(lpc.getProductsRet());
        assertEquals(12, lpc.getProductsRet().size());
    }
}
