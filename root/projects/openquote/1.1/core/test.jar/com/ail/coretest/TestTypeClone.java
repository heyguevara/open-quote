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

package com.ail.coretest;

import java.util.Hashtable;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.Attribute;
import com.ail.core.History;
import com.ail.core.Type;
import com.ail.core.Version;
import com.ail.core.logging.LoggerArgImp;
import com.ail.core.logging.Severity;
import com.ail.coretest.service.TestArgImp;
import com.ail.coretest.service.TestCommand;
import com.ail.coretest.service.TypeWithHashtable;
import com.ail.coretest.service.TypeWithMap;
import com.ail.coretest.service.TypeWithSet;

/**
 * All classes in the type model must be cloneable - what's more the clone must
 * done in a deep fashion. The tests in the is class check that the base classes
 * manage this correctly.
 * 
 * @version $Revision: 1.8 $
 * @state $State: Exp $
 * @date $Date: 2007/05/05 13:25:36 $
 * @source $Source:
 *         /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestTypeClone.java,v $
 */
public class TestTypeClone extends TestCase {

    /** Constructs a test case with the given name. */
    public TestTypeClone(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestTypeClone.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }

    /**
     * Test to ensure that a sample of the Type classes in the core are
     * cloneable, and don't throw 'CloneNotSupported' exceptions when a clone is
     * attempted.
     * <ul>
     * <li>Create an instance of a few based types.</li>
     * <li>Clone each of the instances.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ul>
     */
    public void testCloneable() throws Exception {
        Type type = new Type();
        Version version = new Version();
        History history = new History();

        type.clone();
        version.clone();
        history.clone();
    }

    /**
     * Types must clone their own attributes, this test checks that some of the
     * Core's basic types handle this correctly.
     * <ul>
     * <li>For each type to be tested:</li>
     * <ul>
     * <li>Create an instance of the type</li>
     * <li>Populate some attributes on the instance</li>
     * <li>Clone the instance</li>
     * <li>Fail if the clone's attributes don't match the original's</li>
     * <li>Modify all the attributes on the original instance</li>
     * <li>Fail if any of the clone's attributes are modified</li>
     * </ul>
     * <li>Fail if any exceptions are thrown.</li>
     * </ul>
     */
    public void testDeepCloneType() throws Exception {
        // create the original type.
        Type type = new Type();
        type.setSerialVersion(21);
        type.setLock(true);

        // clone the instance, and make sure the clone's attributes are
        // identical.
        Type type2 = (Type) type.clone();
        assertEquals(type.getSerialVersion(), type2.getSerialVersion());
        assertEquals(type.hasLock(), type2.hasLock());

        // modify the original and make sure the clone's attributes don't get
        // modified.
        type.setSerialVersion(99);
        type.setLock(false);
        assertTrue(type.getSerialVersion() != type2.getSerialVersion());
        assertTrue(type.getLock() != type2.getLock());
    }

    public void testDeepCloneVersion() throws Exception {
        // create the original version
        Version version = new Version();
        version.setAuthor("Author");
        version.setComment("Comment");
        version.setCopyright("Copyright");
        version.setDate("Date");
        version.setSource("Source");
        version.setState("State");
        version.setVersion("Version");

        // clone the original and make sure the clone's attributes are identical
        Version version2 = (Version) version.clone();
        assertEquals(version.getAuthor(), version2.getAuthor());
        assertEquals(version.getComment(), version2.getComment());
        assertEquals(version.getCopyright(), version2.getCopyright());
        assertEquals(version.getDate(), version2.getDate());
        assertEquals(version.getSource(), version2.getSource());
        assertEquals(version.getState(), version2.getState());
        assertEquals(version.getVersion(), version2.getVersion());

        // modify the original and make sure the clone's attributes are not
        // changed
        version.setAuthor("Author1");
        version.setComment("Comment1");
        version.setCopyright("Copyright1");
        version.setDate("Date1");
        version.setSource("Source1");
        version.setState("State1");
        version.setVersion("Version1");
        assertTrue(!version.getAuthor().equals(version2.getAuthor()));
        assertTrue(!version.getComment().equals(version2.getComment()));
        assertTrue(!version.getCopyright().equals(version2.getCopyright()));
        assertTrue(!version.getDate().equals(version2.getDate()));
        assertTrue(!version.getSource().equals(version2.getSource()));
        assertTrue(!version.getState().equals(version2.getState()));
        assertTrue(!version.getVersion().equals(version2.getVersion()));
    }

    public void testDeepCloneHistory() throws Exception {
        // create the original history
        History history = new History();
        history.setSerialVersion(21);
        history.setLock(true);
        for (int i = 0; i < 10; i++) {
            Version v = new Version();
            v.setAuthor("author" + i);
            v.setComment("comment" + i);
            v.setCopyright("copyright" + i);
            v.setDate("date" + i);
            v.setSource("source" + i);
            v.setState("state" + i);
            v.setVersion("version" + i);
            history.addVersion(v);
        }

        // clone the original
        History history1 = (History) history.clone();
        assertEquals(history.getSerialVersion(), history1.getSerialVersion());
        assertEquals(history.getLock(), history1.getLock());
        assertEquals(history.getVersionCount(), history1.getVersionCount());
        for (int i = 0; i < 10; i++) {
            assertEquals(history.getVersion(i).getAuthor(), history1.getVersion(i).getAuthor());
            assertEquals(history.getVersion(i).getComment(), history1.getVersion(i).getComment());
            assertEquals(history.getVersion(i).getCopyright(), history1.getVersion(i).getCopyright());
            assertEquals(history.getVersion(i).getDate(), history1.getVersion(i).getDate());
            assertEquals(history.getVersion(i).getSource(), history1.getVersion(i).getSource());
            assertEquals(history.getVersion(i).getState(), history1.getVersion(i).getState());
            assertEquals(history.getVersion(i).getVersion(), history1.getVersion(i).getVersion());
        }
    }

    public void testCommandClone() throws Exception {
        TestCommand c = new TestCommand();
        TestArgImp a = new TestArgImp();
        c.setArgs(a);
        TestCommand c1 = (TestCommand) c.clone();
        assertTrue(c.hashCode() != c1.hashCode());
        assertTrue(c.getArgs().hashCode() != c1.getArgs().hashCode());
    }

    public void testTypeWithHashtable() throws CloneNotSupportedException {

        // create a TypeWithHashtable and populate it
        TypeWithHashtable table = new TypeWithHashtable();
        table.setName("fred");
        Hashtable<String,Object> ht = new Hashtable<String,Object>();
        ht.put("object1", new Version());
        ht.put("object2", new Version());
        table.setTable(ht);

        // clone the table
        TypeWithHashtable cloned = (TypeWithHashtable) table.clone();

        // make sure the clone and it's component are different from the
        // original.
        assertTrue(cloned.hashCode() != table.hashCode());
        assertTrue(cloned.getTable().hashCode() != table.getTable().hashCode());
        assertTrue(cloned.getTable().get("object1").hashCode() != table.getTable().get("object1").hashCode());
        assertTrue(cloned.getTable().get("object2").hashCode() != table.getTable().get("object2").hashCode());
    }

    /**
     * An issue was raised with regards to the cloning of types (specifically
     * Asset in the insurance model) that used Attributes. The Attributes were
     * not being correctly cloned which meant that each instance of the type
     * ended up sharing a collection of attributes so a change to an attribute
     * on one asset showed up on all the other instances too. This test ensures
     * that this bug is fixed.
     * <ul>
     * <li>Create an instance of a type (com.ail.core.Version).</li>
     * <li>Add two attributes to the instance.</li>
     * <li>Clone the instance.</li>
     * <li>Compare the references in the attribute collection on the original
     * instance with those on the clone. Fail if any are the same.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ul>
     */
    public void testDeepCloneWithAttributes() throws Exception {
        Version original = new Version();
        original.addAttribute(new Attribute("one", "1", "number,#.##"));

        Version clon = (Version) original.clone();

        assertTrue("Attributes share a reference!!", clon.getAttribute(0) != original.getAttribute(0));

        clon.getAttribute(0).setValue("2");
        
        assertEquals("1", original.getAttribute(0).getValue());
        assertEquals("2", clon.getAttribute(0).getValue());
    }
    
    /**
     * Since JDK 1.5 java supports enumerated types. This test checks that our cloning code in
     * the Type class correctly supports them.
     * @throws Exception
     */
    public void testEnumCloning() throws Exception {
        LoggerArgImp limp=new LoggerArgImp();
        
        limp.setSeverity(Severity.DEBUG);
        
        LoggerArgImp clon=(LoggerArgImp)limp.clone();
        
        assertEquals(clon.getSeverity(), limp.getSeverity());
    }
    
    public void testMapCloning() throws Exception {
        Version v;

        TypeWithMap src=new TypeWithMap();
        
        v=new Version();
        v.setAuthor("ONE");
        src.getMyMap().put("one", v);

        v=new Version();
        v.setAuthor("TWO");
        src.getMyMap().put("two", v);

        v=new Version();
        v.setAuthor("THREE");
        src.getMyMap().put("three", v);

        TypeWithMap cln=(TypeWithMap)src.clone();
    
        assertTrue(cln!=src);
        assertNotNull(cln.getMyMap().get("one"));
        assertNotNull(cln.getMyMap().get("two"));
        assertNotNull(cln.getMyMap().get("three"));
        assertTrue(cln.getMyMap().get("one")!=src.getMyMap().get("one"));
        assertTrue(cln.getMyMap().get("two")!=src.getMyMap().get("two"));
        assertTrue(cln.getMyMap().get("three")!=src.getMyMap().get("three"));
    }

    public void testSetCloning() throws Exception {
        Version v;

        TypeWithSet src=new TypeWithSet();
        
        v=new Version();
        v.setAuthor("ONE");
        src.getMySet().add(v);

        v=new Version();
        v.setAuthor("TWO");
        src.getMySet().add(v);

        v=new Version();
        v.setAuthor("THREE");
        src.getMySet().add(v);

        TypeWithSet cln=(TypeWithSet)src.clone();

        assertEquals(cln.getMySet().size(), src.getMySet().size());
        
        for(Version vsrc: src.getMySet()) {
            if (cln.getMySet().contains(vsrc)) {
                fail("clone set contained same element as src");
            }
        }
        
        assertTrue(cln!=src);
    }
}
