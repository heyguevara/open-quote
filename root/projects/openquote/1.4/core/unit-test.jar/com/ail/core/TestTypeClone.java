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

package com.ail.core;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.ail.core.Attribute;
import com.ail.core.History;
import com.ail.core.Type;
import com.ail.core.Version;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.core.command.CommandArgImp;
import com.ail.core.logging.LoggerArgImp;
import com.ail.core.logging.Severity;

/**
 * All classes in the type model must be cloneable - what's more the clone must
 * done in a deep fashion. The tests in the is class check that the base classes
 * manage this correctly.
 */
public class TestTypeClone {

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
    @Test
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
    @Test
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

    @Test
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

    @Test
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

    @Test
    public void testCommandClone() throws Exception {
        MyTestCommand c = new MyTestCommand();
        MyTestArgImp a = new MyTestArgImp();
        c.setArgs(a);
        MyTestCommand c1 = (MyTestCommand) c.clone();
        assertTrue(c.hashCode() != c1.hashCode());
        assertTrue(c.getArgs().hashCode() != c1.getArgs().hashCode());
    }

    @Test
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
    @Test
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
    @Test
    public void testEnumCloning() throws Exception {
        LoggerArgImp limp=new LoggerArgImp();
        
        limp.setSeverity(Severity.DEBUG);
        
        LoggerArgImp clon=(LoggerArgImp)limp.clone();
        
        assertEquals(clon.getSeverity(), limp.getSeverity());
    }
    
    @Test
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

    @Test
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

/**
 * Type class with a Hashtable property, to help TestTypeClone.
 */
class TypeWithHashtable extends Type {
    private Hashtable<String,Object> table;
    private String name;

    public Hashtable<String,Object> getTable() {
        return table;
    }

    public void setTable(Hashtable<String,Object> table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

/**
 * Sample Type with a Set to help in testing the core's cloning
 */
class TypeWithMap extends Type {
    private Map<String,Version> myMap=new HashMap<String,Version>();
    
    public Map<String,Version> getMyMap() {
        return myMap;
    }
    
    public void setMyMap(Map<String,Version> myMap) {
        this.myMap=myMap;
    }
}

/**
 * Sample Type with a Set to help in testing the core's cloning
 */
class TypeWithSet extends Type {
    private Set<Version> mySet=new HashSet<Version>();
    
    public Set<Version> getMySet() {
        return mySet;
    }
    
    public void setMySet(Set<Version> mySet) {
        this.mySet=mySet;
    }
}

/**
 * Sample Command to help in testing the core's cloning
 */
class MyTestCommand extends Command implements MyTestArg {
    private MyTestArg args = null;

    public MyTestCommand() {
        super();
        args = new MyTestArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (MyTestArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @see #setX
     * @return value of x
     */
    public int getX() {
        return args.getX();
    }

    /**
     * {@inheritDoc}
     * @see #getX
     * @param x New value for x argument.
     */
    public void setX(int x) {
        args.setX(x);
    }

    /**
     * {@inheritDoc}
     * @see #setY
     * @return value of y
     */
    public int getY() {
        return args.getY();
    }

    /**
     * {@inheritDoc}
     * @see #getY
     * @param y New value for y argument.
     */
    public void setY(int y) {
        args.setY(y);
    }

    /**
     * {@inheritDoc}
     * @see #setR
     * @return value of r
     */
    public int getR() {
        return args.getR();
    }

    /**
     * {@inheritDoc}
     * @see #getR
     * @param r New value for r argument.
     */
    public void setR(int r) {
        args.setR(r);
    }

    public void setStringRet(String string) {
        args.setStringRet(string);
    }

    public String getStringRet() {
        return args.getStringRet();
    }

    /**
     * {@inheritDoc}
     * @see #setPreConditionFlag
     * @return value of preconditionflag
     */
    public boolean getPreConditionFlag() {
        return args.getPreConditionFlag();
    }

    /**
     * {@inheritDoc}
     * @see #getPreConditionFlag
     * @param preconditionflag New value for preconditionflag argument.
     */
    public void setPreConditionFlag(boolean preconditionflag) {
        args.setPreConditionFlag(preconditionflag);
    }

    /**
     * {@inheritDoc}
     * @see #setPostConditionFlag
     * @return value of postconditionflag
     */
    public boolean getPostConditionFlag() {
        return args.getPostConditionFlag();
    }

    /**
     * {@inheritDoc}
     * @see #getPostConditionFlag
     * @param postconditionflag New value for postconditionflag argument.
     */
    public void setPostConditionFlag(boolean postconditionflag) {
        args.setPostConditionFlag(postconditionflag);
    }

    public Attribute getDetailAttribute() {
        return args.getDetailAttribute();
    }

    public void setDetailAttribute(Attribute attribute) {
        args.setDetailAttribute(attribute);
    }

    public Version getVersionArgRet() {
        return args.getVersionArgRet();
    }

    public void setVersionArgRet(Version versionArgRet) {
        args.setVersionArgRet(versionArgRet);
    }
}

/**
 * Sample Arg interface to help in testing the core's cloning
 */
interface MyTestArg extends CommandArg {
    /**
     * Fetch the value of the version argument. 
     * @see #setVersion
     * @return value of version
     */
    Version getVersionArgRet();
    
    /**
     * Set the value of the version argument.
     * @see #getVersion
     * @param versionArgRet New value for version argument.
     */
    void setVersionArgRet(Version versionArgRet);
    
    /**
     * Fetch the value of the x argument. First value for adder
     * @see #setX
     * @return value of x
     */
    int getX();

    /**
     * Set the value of the x argument. First value for adder
     * @see #getX
     * @param x New value for x argument.
     */
    void setX(int x);

    /**
     * Fetch the value of the y argument. Second value for adder
     * @see #setY
     * @return value of y
     */
    int getY();

    /**
     * Set the value of the y argument. Second value for adder
     * @see #getY
     * @param y New value for y argument.
     */
    void setY(int y);

    /**
     * Fetch the value of the r argument. Result from addition
     * @see #setR
     * @return value of r
     */
    int getR();

    /**
     * Set the value of the r argument. Result from addition
     * @see #getR
     * @param r New value for r argument.
     */
    void setR(int r);

    void setStringRet(String string);
    
    String getStringRet();
    
    /**
     * Fetch the value of the postconditionflag argument. Flag set by the pre-condition service to show that it has been run.
     * @see #setPostConditionFlag
     * @return value of postconditionflag
     */
    boolean getPostConditionFlag();

    /**
     * Set the value of the postconditionflag argument. Flag set by the pre-condition service to show that it has been run.
     * @see #getPostConditionFlag
     * @param postconditionflag New value for postconditionflag argument.
     */
    void setPostConditionFlag(boolean postconditionflag);

    /**
     * Fetch the value of the preconditionflag argument. Flag set by the pre condition service to show that it has been run.
     * @see #setPreConditionFlag
     * @return value of preconditionflag
     */
    boolean getPreConditionFlag();

    /**
     * Set the value of the preconditionflag argument. Flag set by the pre condition service to show that it has been run.
     * @see #getPreConditionFlag
     * @param preconditionflag New value for preconditionflag argument.
     */
    void setPreConditionFlag(boolean preconditionflag);

    void setDetailAttribute(Attribute attribute);
    
    Attribute getDetailAttribute();
}

/**
 * Sample ArgImp interface to help in testing the core's cloning
 */
class MyTestArgImp extends CommandArgImp implements MyTestArg {
    static final long serialVersionUID = 1199346453402049909L;
    private int x;
    private int y;
    private int r;
    private String string;
    private Version versionArgRet;
    private boolean preconditionflag;
    private boolean postconditionflag;
    private Attribute detailAttribute;

    /** Default constructor */
    public MyTestArgImp() {
    }

    /**
     * Argument priming constructor.
     * @param x Value for X argument.
     * @param y Value for Y argument.
     */
    public MyTestArgImp(int x, int y) {
        this.x=x;
        this.y=y;
    }

    /**
     * {@inheritDoc}
     * @see #setX
     * @return value of x
     */
    public int getX() {
        return this.x;
    }

    /**
     * {@inheritDoc}
     * @see #getX
     * @param x New value for x argument.
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * {@inheritDoc}
     * @see #setY
     * @return value of y
     */
    public int getY() {
        return this.y;
    }

    /**
     * {@inheritDoc}
     * @see #getY
     * @param y New value for y argument.
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * {@inheritDoc}
     * @see #setR
     * @return value of r
     */
    public int getR() {
        return this.r;
    }

    /**
     * {@inheritDoc}
     * @see #getR
     * @param r New value for r argument.
     */
    public void setR(int r) {
        this.r = r;
    }

    public void setStringRet(String string) {
        this.string=string;
    }

    public String getStringRet() {
        return this.string;
    }

    /**
     * {@inheritDoc}
     * @see #setPreConditionFlag
     * @return value of preconditionflag
     */
    public boolean getPreConditionFlag() {
        return this.preconditionflag;
    }

    /**
     * {@inheritDoc}
     * @see #getPreConditionFlag
     * @param preconditionflag New value for preconditionflag argument.
     */
    public void setPreConditionFlag(boolean preconditionflag) {
        this.preconditionflag = preconditionflag;
    }

    /**
     * {@inheritDoc}
     * @see #setPostConditionFlag
     * @return value of postconditionflag
     */
    public boolean getPostConditionFlag() {
        return this.postconditionflag;
    }

    /**
     * {@inheritDoc}
     * @see #getPostConditionFlag
     * @param postconditionflag New value for postconditionflag argument.
     */
    public void setPostConditionFlag(boolean postconditionflag) {
        this.postconditionflag = postconditionflag;
    }

    public Attribute getDetailAttribute() {
        return detailAttribute;
    }

    public void setDetailAttribute(Attribute detailAttribute) {
        this.detailAttribute = detailAttribute;
    }

    public Version getVersionArgRet() {
        return versionArgRet;
    }

    public void setVersionArgRet(Version versionArgRet) {
        this.versionArgRet=versionArgRet;
    }
}


