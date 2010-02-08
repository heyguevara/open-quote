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

import java.sql.Connection;
import java.sql.Statement;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.hibernate.ObjectNotFoundException;

import com.ail.core.Attribute;
import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandInvocationError;
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

public class TestCoreHibernatePersistence extends CoreUserTestCase {
    /**
     * Constructs a test case with the given name.
     */
    public TestCoreHibernatePersistence(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestCoreHibernatePersistence.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() throws ClassNotFoundException {

        setCore(new Core(this));

        setupSystemProperties();
        tidyUpTestData();

        new Core(this).resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());

        new HibernateCreateService().resetConfiguration();
        new PersistenceServerBean().resetConfiguration();
        resetConfiguration();

        ConfigurationHandler.reset();

        setVersionEffectiveDate(new VersionEffectiveDate());
        try {
            Context ctx = new InitialContext();
            DataSource dataSource = (DataSource) ctx.lookup("java:/PersistenceDS");

            Connection con = dataSource.getConnection();
            Statement st = con.createStatement();
            st.execute("delete from core_Attribute");
            st.execute("delete from core_Type");
            st.execute("delete from core_configure_Component");
            st.execute("delete from core_configure_Parameter");
            st.execute("delete from core_configure_Group");
            st.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace(System.err);
        }
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * @throws Exception
     */
    public void testHibernateCreateDirectAccess() throws Exception {
        getCore().openPersistenceSession();

        CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");

        try {
            command.invoke();
        }
        catch (PreconditionException e) {
            // exception is what this test expects
            return;
        }
        finally {
            getCore().closePersistenceSession();
        }
        
        fail("create with null args should throw a PreconditionException");
    }

    /**
     * @throws Exception
     */
    public void testHibernateCreateFailPredifnedSerialVersion() throws Exception {
        try {
            getCore().openPersistenceSession();

            // set up object
            Attribute attr = new Attribute();
            attr.setId("ATTR1");
            attr.setFormat("int");
            attr.setUnit("feet");
            attr.setValue("21");
            attr.setSerialVersion(1);

            CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");
            command.setObjectArg(attr);

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
            getCore().closePersistenceSession();
        }        

        fail("create should fail due to pre-defined serial version");
    }

    /**
     * @throws Exception
     */
    public void testHibernateCreateSuccess() throws Exception {
        try {
            getCore().openPersistenceSession();

            // set up object
            Attribute attr = new Attribute();
            attr.setId("ATTR1");
            attr.setFormat("int");
            attr.setUnit("feet");
            attr.setValue("21");

            CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");
            command.setObjectArg(attr);

            command.invoke();
        }
        catch (Exception e) {
            e.printStackTrace();
            fail("create failed");
        }
        finally {
            getCore().closePersistenceSession();
        }        
    }

    /**
     * @throws Exception
     */
    public void testHibernateCreateFailMissingObject() throws Exception {
        try {
            getCore().openPersistenceSession();

            CreateCommand command = (CreateCommand) getCore().newCommand("TestCreateCommand");

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
            getCore().closePersistenceSession();
        }        

        fail("create should fail due to missing object in arg failed");
    }

    /**
     * @throws Exception
     */
    public void testHibernateUpdateSuccess() throws Exception {
        try {
            getCore().openPersistenceSession();

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

            upate.invoke();
        }
        catch (BaseException e) {
            e.printStackTrace();
            fail("update failed");
        }
        finally {
            getCore().closePersistenceSession();
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
    public void testHibernateUpdateFailWrongSerialVersion() throws Exception {
        long id;
        Attribute originalAttr=null;

        try {
            getCore().openPersistenceSession();

            Attribute attr = new Attribute();
            attr.setId("ATTR2");
            attr.setFormat("int");
            attr.setUnit("feet");
            attr.setValue("1");
    
            CreateCommand create = (CreateCommand) getCore().newCommand("TestCreateCommand");
            create.setObjectArg(attr);
            create.invoke();
            attr=(Attribute)create.getObjectArg();

            id=attr.getSystemId();
            
            originalAttr=attr;
        }
        finally {
            getCore().closePersistenceSession();
        }

        try  {
            getCore().openPersistenceSession();

            LoadCommand load = (LoadCommand) getCore().newCommand("TestLoadCommand");
            load.setTypeArg(Attribute.class);
            load.setSystemIdArg(id);
            load.invoke();
            Attribute attr=(Attribute)load.getObjectRet();
            
            attr.setValue("2");

            UpdateCommand update = (UpdateCommand) getCore().newCommand("TestUpdateCommand");
            update.setObjectArg(attr);
            update.invoke();
        }
        finally {
            getCore().closePersistenceSession();
        }
        
        getCore().openPersistenceSession();

        originalAttr.setValue("3");

        UpdateCommand update = (UpdateCommand) getCore().newCommand("TestUpdateCommand");
        update.setObjectArg(originalAttr);
        
        try {
            update.invoke();
            getCore().closePersistenceSession();
            fail("update should failed due to wrong serial version");
        }
        catch (CommandInvocationError e) {
            // This is what we want.
        }
        catch (ConnectionError e) {
            fail("Test couldn't connect.");
        }
    }

    /**
     * Attempt to invoke the update command without passing an object to store.
     */
    public void testHibernateUpdateFailMissingObject() throws Exception {
        try {
            getCore().openPersistenceSession();

            UpdateCommand command = (UpdateCommand) getCore().newCommand("TestUpdateCommand");

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
            getCore().closePersistenceSession();
        }        

        fail("update should fail due to missing object in arg failed");
    }

    /**
     * Test that a persisted object can be retrieved using the Load service.
     * <ol>
     * <li>Create an instance of Attribute (id=ATTR1, value="21")</li>
     * <li>Persist the instance (Using the TestCreateService).</li>
     * <li>Create an instance of the TestLoadService command.</li>
     * <li>Set the command to locate an instance of TestCoreHibernatePersistenceTestObject with an id of ATTR1.</li>
     * <li>Invoke the command.
     * <li>
     * <li>Check that the result returned is an instance of TestCoreHibernatePersistenceTestObject</li>
     * <li>Check that the result has an id of ATTR1</li>
     * <li>Check that the result has a value of "Test Object"</li>
     * <li>Fail if any checks fail</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ol>
     */
    public void testHibernateLoadSuccess() throws Exception {
        try {
            getCore().openPersistenceSession();

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
        }
        finally {
            getCore().closePersistenceSession();
        }        
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
    public void testHibernateQuerySuccess() throws Exception {
        try {
            getCore().openPersistenceSession();

            Attribute attr = new Attribute();
            attr.setId("ATTR1");
            attr.setFormat("int");
            attr.setUnit("feet");
            attr.setValue("21");
    
            CreateCommand command1 = (CreateCommand) getCore().newCommand("TestCreateCommand");
            command1.setObjectArg(attr);
            command1.invoke();
        }
        finally {
            getCore().closePersistenceSession();
        }        

        try {
            getCore().openPersistenceSession();
            
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
        }
        finally {
            getCore().closePersistenceSession();
        }        
    }

    /**
     * The Core exposes persistence methods in an easy-to-use form. This test
     * runs through some simple scenarios using those methods.
     * @throws Exception
     */
    public void testCorePersistenceMethods() throws Exception {
        Attribute attr1;
        Attribute attr2;
        
        try {
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
        }
        finally {
            getCore().closePersistenceSession();
        }        
        
        try {
            getCore().openPersistenceSession();

            attr2.setValue("22");
            attr2.setId("NEWATTR");
            
            getCore().update(attr2);
            
            assertEquals("NEWATTR", attr2.getId());
            assertEquals("22", attr2.getValue());
            assertEquals("feet", attr2.getUnit());
            assertEquals("int", attr2.getFormat());
        }
        finally {
            getCore().closePersistenceSession();
        }        
        
        try {
            Attribute attr;

            getCore().openPersistenceSession();

            attr=(Attribute)getCore().queryUnique("get.attribute.by.unit", "feet");
            assertEquals("NEWATTR", attr.getId());
            assertEquals("22", attr.getValue());
            assertEquals("feet", attr.getUnit());
            assertEquals("int", attr.getFormat());
        }
        finally {
            getCore().closePersistenceSession();
        }        
        
        try {
            getCore().openPersistenceSession();

            for(int i=0 ; i<100 ; i++) {
                getCore().create(new Attribute("ATTR", "Val "+i, "string", "inch"));
            }

            List<?> l=getCore().query("get.attribute.by.unit", "inch");
            
            assertEquals(100, l.size());
        }
        finally {
            getCore().closePersistenceSession();
        }        
    }

    public void testAttributesWithinAttributes() {
        Attribute attr=new Attribute("PARENT", "JOE", "string");

        for(int i=0 ; i<10 ; i++) {
            attr.addAttribute(new Attribute("CHILD"+i, "JIM", "string"));
        }

        try {
            getCore().openPersistenceSession();

            // This should cascade and create all the child attributes too.
            getCore().create(attr);
        }
        finally {
            getCore().closePersistenceSession();
        }

        try {
            getCore().openPersistenceSession();

            Attribute attr2=getCore().load(Attribute.class, attr.getSystemId());

            System.out.println(getCore().toXML(attr2));
        }
        finally {
            getCore().closePersistenceSession();
        }
    }

    /**
     * Test the persistence of a deep hierarchy. Actually not all that deep, but deep enough to prove the point.
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
    public void testDeepTypeHierarchy() {
        long groupId;
        
        Group g=new Group();
        g.setName("my group");
        
        Parameter p=new Parameter();
        p.setName("myParameter");
        p.setValue("parameter value");
        
        g.addParameter(p);
        
         try {
            getCore().openPersistenceSession();

            g=getCore().create(g);
            
            groupId=g.getSystemId();
        }
        finally {
            getCore().closePersistenceSession();
        }

        try {
            getCore().openPersistenceSession();
            
            Group g1=getCore().load(Group.class, groupId);
            
            assertNotNull(g1);
            assertNotNull(g1.findParameter("myParameter"));
            assertEquals("parameter value", g1.findParameterValue("myParameter"));
        }
        finally {
            getCore().closePersistenceSession();
        }

        try {
            getCore().openPersistenceSession();
            
            Group g1=getCore().load(Group.class, groupId);

            getCore().delete(g1);
        }
        finally {
            getCore().closePersistenceSession();
        }

        try {
            getCore().openPersistenceSession();
            
            Group g1=getCore().load(Group.class, groupId);
 
            assertNotNull(g1);
            assertNotNull(g1.findParameter("myParameter"));
            assertEquals("parameter value", g1.findParameterValue("myParameter"));
            
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
