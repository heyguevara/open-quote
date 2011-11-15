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

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.List;
import java.util.Properties;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Attribute;
import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.Group;
import com.ail.core.configure.Parameter;
import com.ail.core.persistence.ConnectionError;
import com.ail.core.persistence.CreateCommand;
import com.ail.core.persistence.LoadCommand;
import com.ail.core.persistence.PersistenceServerBean;
import com.ail.core.persistence.QueryCommand;
import com.ail.core.persistence.UpdateCommand;
import com.ail.core.persistence.hibernate.HibernateCreateService;
import com.ail.core.CoreUserTestCase;

/**
 */
public class TestCoreHibernatePersistence extends CoreUserTestCase {
    private static boolean onetimeSetupDone = false;
    private static Properties props;
    
    private AbstractConfigurationLoader loader = null;

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() throws ClassNotFoundException {

        setCore(new Core(this));

        if (!onetimeSetupDone) {
            System.out.println("Resetting configs");
            setupSystemProperties();
            tidyUpTestData();

            new Core(this).resetConfiguration();
            setVersionEffectiveDate(new VersionEffectiveDate());

            new HibernateCreateService().resetConfiguration();
            new PersistenceServerBean().resetConfiguration();
            resetConfiguration();

            ConfigurationHandler.reset();
            
            loader = AbstractConfigurationLoader.loadLoader();
            props = loader.getLoaderParams();
            
            Class.forName(props.getProperty("driver"));
            
            onetimeSetupDone=true;
        }
        
        setVersionEffectiveDate(new VersionEffectiveDate());
        
        try {
            Connection con = DriverManager.getConnection(props.getProperty("url"), props);
            Statement st = con.createStatement();
            st.execute("delete from core_Attribute");
            st.execute("delete from core_Type");
            st.execute("delete from core_configure_Component");
            st.execute("delete from core_configure_Parameter");            
            st.execute("delete from core_configure_Group");            
            st.close();
            con.close();
        }
        catch (Exception e) {
            System.err.println("Failed to create test tables.");
            e.printStackTrace(System.err);
            System.exit(0);
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testHibernateCreateDirectAccess() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();
        
        CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");

        try {
            command.invoke();
        }
        catch (PreconditionException e) {
            // exception is what this test expects
            return;
        }
        finally {
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }
        
        fail("create with null args should throw a PreconditionException");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testHibernateCreateFailPredifnedSerialVersion() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();

        // set up object
        Attribute attr = new Attribute();
        attr.setId("ATTR1");
        attr.setFormat("int");
        attr.setUnit("feet");
        attr.setValue("21");
        attr.setSerialVersion(1);

        CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");
        command.setObjectArg(attr);

        try {
            command.invoke();
        }
        catch (BaseException e) {
            return;
        }
        catch (ConnectionError e) {
            e.printStackTrace();
            fail("Test couldn't connect.");
            return;
        }
        finally {
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }
        fail("create should fail due to pre-defined serial version");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testHibernateCreateSuccess() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();

        // set up object
        Attribute attr = new Attribute();
        attr.setId("ATTR1");
        attr.setFormat("int");
        attr.setUnit("feet");
        attr.setValue("21");

        CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");
        command.setObjectArg(attr);

        try {
            command.invoke();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail("create failed");
        }
        finally {
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }
    }

    /**
     * @throws Exception
     */
    @Test
    public void testHibernateCreateFailMissingObject() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();

        CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");

        try {
            command.invoke();
        }
        catch (BaseException e) {
            return;
        }
        catch (ConnectionError e) {
            fail("Test couldn't connect.");
            return;
        }
        finally {
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }

        fail("create should fail due to missing object in arg failed");
    }

    /**
     * @throws Exception
     */
    @Test
    public void testHibernateUpdateSuccess() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();

        Attribute attr = new Attribute();
        attr.setId("ATTR1");
        attr.setFormat("int");
        attr.setUnit("feet");
        attr.setValue("21");

        CreateCommand create = (CreateCommand) getCore().newCommand("TestCreateCommand");
        create.setObjectArg(attr);
        create.invoke();

        attr = (Attribute) create.getObjectArg();
        attr.setId("ATTR1");
        attr.setFormat("int");
        attr.setUnit("feet");
        attr.setValue("22");

        UpdateCommand upate = (UpdateCommand) getCore().newCommand("TestUpdateCommand");
        upate.setObjectArg(attr);

        try {
            upate.invoke();
        }
        catch (BaseException e) {
            e.printStackTrace();
            fail("update failed");
        }
        finally {
            getCore().newCommand("TestCloseSessionCommand").invoke();
       }
    }

    /**
     * <ol>
     * <li>Create a record</li>
     * <li>Update the record</li>
     * <li>Update the record using the same serialVersionId - this update should fail</li>
     * </ol>
     * @throws Exception
     */
    @Test
    public void testHibernateUpdateFailWrongSerialVersion() throws Exception {
        Attribute attr; 
        
        {
            getCore().newCommand("TestOpenSessionCommand").invoke();
    
            attr = new Attribute();
            attr.setId("ATTR2");
            attr.setFormat("int");
            attr.setUnit("feet");
            attr.setValue("21");
    
            CreateCommand create = (CreateCommand) getCore().newCommand("TestCreateCommand");
            create.setObjectArg(attr);
            create.invoke();
    
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }

        long svid=attr.getSerialVersion();
        
        {
            getCore().newCommand("TestOpenSessionCommand").invoke();
    
            attr.setId("PLOP");
            UpdateCommand upate = (UpdateCommand) getCore().newCommand("TestUpdateCommand");
            upate.setObjectArg(attr);
            upate.invoke();

            getCore().newCommand("TestCloseSessionCommand").invoke();
        }
        
        attr.setSerialVersion(svid);
        
        {
            getCore().newCommand("TestOpenSessionCommand").invoke();

            UpdateCommand command = (UpdateCommand) getCore().newCommand("TestUpdateCommand");
            command.setObjectArg(attr);
            
            try {
                command.invoke();
                getCore().newCommand("TestCloseSessionCommand").invoke();

                fail("update should failed due to wrong serial version");
            }
            catch (BaseException e) {
                // This is what we want.
            }
            catch (ConnectionError e) {
                fail("Test couldn't connect.");
            }

        }
    }

    /**
     * Test direct access to the validator
     * 
     * @throws Exception
     */
    @Test
    public void testHibernateUpdateFailMissingObject() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();

        UpdateCommand command = (UpdateCommand) getCore().newCommand("TestUpdateCommand");

        try {
            command.invoke();
        }
        catch (BaseException e) {
            return;
        }
        catch (ConnectionError e) {
            fail("Test couldn't connect.");
            return;
        }
        finally {
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }

        fail("update should fail due to missing object in arg failed");
    }

    /**
     * Test that a persisted object can be retrieved using the Load service.
     * <ol>
     * <li>Create an instance of TestCoreHibernatePersistenceTestObject (id=1, value="Test Object")</li>
     * <li>Persist the instance (Using the TestCreateService).</li>
     * <li>Create an instance of the TestLoadService command.</li>
     * <li>Set the command to locate an instance of TestCoreHibernatePersistenceTestObject with an id of 32.</li>
     * <li>Invoke the command.
     * <li>
     * <li>Check that the result returned is an instance of TestCoreHibernatePersistenceTestObject</li>
     * <li>Check that the result has an id of 32</li>
     * <li>Check that the result has a value of "Test Object"</li>
     * <li>Fail if any checks fail</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ol>
     */
    @Test
    public void testHibernateLoadSuccess() throws Exception {
        getCore().newCommand("TestOpenSessionCommand").invoke();

        Attribute attr = new Attribute();
        attr.setId("ATTR1");
        attr.setFormat("int");
        attr.setUnit("feet");
        attr.setValue("21");

        CreateCommand command1 = (CreateCommand) getCore().newCommand("TestCreateCommand");
        command1.setObjectArg(attr);
        command1.invoke();

        LoadCommand command = (LoadCommand) getCore().newCommand("TestLoadCommand");
        command.setTypeArg(Attribute.class);
        command.setSystemIdArg(command1.getObjectArg().getSystemId());
        command.invoke();

        assertNotNull("Object returned was null", command.getObjectRet());
        assertTrue("Object returned is of wrong type", command.getObjectRet() instanceof Attribute);
        Attribute res = (Attribute) command.getObjectRet();
        assertEquals("Objet returned has wrong ID", "ATTR1", res.getId());
        assertEquals("Object returned has wrong value", "21", res.getValue());

        getCore().newCommand("TestCloseSessionCommand").invoke();
    }

    /**
     * Test that a persisted object can be retrieved using the Query service.
     * <ol>
     * <li>Create an instance of TestCoreHibernatePersistenceTestObject (id=1, value="Test Object")</li>
     * <li>Persist the instance (Using the TestCreateService).</li>
     * <li>Create an instance of the TestLoadService command.</li>
     * <li>Set the command to locate an instance of TestCoreHibernatePersistenceTestObject with an id of 32.</li>
     * <li>Invoke the command.
     * <li>
     * <li>Check that the result returned is an instance of TestCoreHibernatePersistenceTestObject</li>
     * <li>Check that the result has an id of 32</li>
     * <li>Check that the result has a value of "Test Object"</li>
     * <li>Fail if any checks fail</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ol>
     */
    @Test
    public void testHibernateQuerySuccess() throws Exception {
        {
            getCore().newCommand("TestOpenSessionCommand").invoke();
    
            Attribute attr = new Attribute();
            attr.setId("ATTR1");
            attr.setFormat("int");
            attr.setUnit("feet");
            attr.setValue("21");
    
            CreateCommand command1 = (CreateCommand) getCore().newCommand("TestCreateCommand");
            command1.setObjectArg(attr);
            command1.invoke();

            getCore().newCommand("TestCloseSessionCommand").invoke();
        }

        {
            getCore().newCommand("TestOpenSessionCommand").invoke();
            
            QueryCommand command = (QueryCommand) getCore().newCommand("TestQueryCommand");
            command.setQueryNameArg("get.attribute.by.unit");
            command.setQueryArgumentsArg("feet");
    
            command.invoke();
    
            assertNotNull("Object returned was null", command.getResultsListRet());
            assertTrue("Query returned no objects", command.getResultsListRet().size()>0);
            assertTrue("Object returned is of wrong type", command.getUniqueResultRet() instanceof Attribute);
            Attribute res = (Attribute) command.getUniqueResultRet();
            assertEquals("Objet returned has wrong ID", "ATTR1", res.getId());
            assertEquals("Object returned has wrong value", "21", res.getValue());
    
            getCore().newCommand("TestCloseSessionCommand").invoke();
        }
    }

    /**
     * The Core exposes persistence methods in an easy-to-use form. This test
     * runs through some simple scenarios using those methods.
     * @throws Exception
     */
    @Test
    public void testCorePersistenceMethods() throws Exception {
        Attribute attr1;
        Attribute attr2;
        
        {
            getCore().openPersistenceSession();
            
            attr1=new Attribute("ATTR1", "12", "int", "feet");
            
            attr1=(Attribute)getCore().create(attr1);
            attr2=getCore().load(Attribute.class, attr1.getSystemId());

            assertEquals("ATTR1", attr2.getId());
            assertEquals("12", attr2.getValue());
            assertEquals("feet", attr2.getUnit());
            assertEquals("int", attr2.getFormat());
            assertEquals(attr1.getSystemId(), attr2.getSystemId());
            assertEquals(attr1.getSerialVersion(), attr2.getSerialVersion());
            
            getCore().closePersistenceSession();
        }
        
        {
            getCore().openPersistenceSession();

            attr2.setValue("22");
            attr2.setId("NEWATTR");
            
            getCore().update(attr2);
            
            assertEquals("NEWATTR", attr2.getId());
            assertEquals("22", attr2.getValue());
            assertEquals("feet", attr2.getUnit());
            assertEquals("int", attr2.getFormat());

            getCore().closePersistenceSession();
        }
        
        {
            Attribute attr;

            getCore().openPersistenceSession();

            attr=(Attribute)getCore().queryUnique("get.attribute.by.unit", "feet");
            assertEquals("NEWATTR", attr.getId());
            assertEquals("22", attr.getValue());
            assertEquals("feet", attr.getUnit());
            assertEquals("int", attr.getFormat());

            getCore().closePersistenceSession();
        }
        
        {
            getCore().openPersistenceSession();
            
            for(int i=0 ; i<100 ; i++) {
                getCore().create(new Attribute("ATTR", "Val "+i, "string", "inch"));
            }

            List<?> l=getCore().query("get.attribute.by.unit", "inch");
            
            assertEquals(100, l.size());
            
            getCore().closePersistenceSession();
        }
    }

    @Test
    public void testAttributesWithinAttributes() {
        Attribute attr=new Attribute("PARENT", "JOE", "string");

        for(int i=0 ; i<10 ; i++) {
            attr.addAttribute(new Attribute("CHILD"+i, "JIM", "string"));
        }

        {
            getCore().openPersistenceSession();

            // This should cascade and create all the child attributes too.
            getCore().create(attr);
            
            getCore().closePersistenceSession();
        }

        {
            getCore().openPersistenceSession();

            Attribute attr2=getCore().load(Attribute.class, attr.getSystemId());

            System.out.println(getCore().toXML(attr2));
            
            getCore().closePersistenceSession();
        }
    }

    /**
     * Test the persistence of a deep hierarchy. Actually not all that deep, but deep enough to proove the point.
     * The configure system defines a class hierarchy which is used to hold configurations. It's more or less
     * a copy-book implementation of the composite pattern. We'll play with two types here: Parameter and Group;
     * Both extend Component, and Component extends Type in common with all other model types in the system. Group
     * contains a list of Parameters, and a list of Groups.
     * 
     * <ol>
     * <li>Create an in memory instance of a Group, with on Parameter in it.</li>
     * <li>Create a persistence session, store the instance, get it's id, close the session.</li>
     * <li>Create a new session, load the object using it's id, check the content of the instance, close the session.</li>
     * <li>Create another session, load the object again, and delete it, then close the session</li>
     * <li>Create a session, try to load the object using it's id again and try to check it's content. close the session. Fail if this works!</li>
     * <li>Fail if any exceptions are thrown from the above steps (except the last one).</li>
     * </ol>
     */
    @Test
    public void testDeepTypeHierarchy() {
        long groupId;
        
        Group g=new Group();
        g.setName("my group");
        
        Parameter p=new Parameter();
        p.setName("myParameter");
        p.setValue("parameter value");
        
        g.addParameter(p);
        
        {
            getCore().openPersistenceSession();

            g=getCore().create(g);
            
            groupId=g.getSystemId();
            
            getCore().closePersistenceSession();
        }

        {
            getCore().openPersistenceSession();
            
            Group g1=getCore().load(Group.class, groupId);
            
            assertNotNull(g1);
            assertNotNull(g1.findParameter("myParameter"));
            assertEquals("parameter value", g1.findParameterValue("myParameter"));
            
            getCore().closePersistenceSession();
        }

        {
            getCore().openPersistenceSession();
            
            Group g1=getCore().load(Group.class, groupId);

            getCore().delete(g1);
            
            getCore().closePersistenceSession();
        }

        try {
            getCore().openPersistenceSession();
            
            Group g1=getCore().load(Group.class, groupId);
 
            assertNotNull(g1);
            assertNotNull(g1.findParameter("myParameter"));
            assertEquals("parameter value", g1.findParameterValue("myParameter"));
            
            getCore().closePersistenceSession();
            
            fail("Loaded and object that doesn't exist, and read values from it? That just isn't right.");
        }
        catch(ObjectNotFoundException e) {
            //this is okay
        }
        catch(Throwable e) {
            fail("Should have got an ObjectNotFound, but got a "+e+" instead");
        }
        finally {
            getCore().closePersistenceSession();
        }
    }
}   
