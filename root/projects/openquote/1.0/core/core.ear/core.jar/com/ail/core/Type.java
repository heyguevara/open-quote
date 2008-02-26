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

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.JXPathException;

import com.ail.core.command.CommandArg;

/**
 * <i>Type</i> is the base of all 'type model' classes that are part of the domain
 * model. All model classes must either extend this class, or
 * another that itself extends this class.
 * @version $Revision: 1.22 $
 * @state $State: Exp $
 * @date $Date: 2007/05/05 13:25:37 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/Type.java,v $
 */
public class Type implements Serializable, Cloneable {
    static final long serialVersionUID = -7687502065734633603L;
    public static final long NOT_PERSISTED=-1;
    
	public transient JXPathContext jXPathContext;

	private boolean hasLock=false;
	private boolean lock=false;
	private boolean hasSerialVersion=false;
    private long serialVersion;
    private boolean hasSystemId=false;
    private long systemId=NOT_PERSISTED;
    private String foreignSystemId=null;
    
	/** A dynamic collection of attributes describing type */
	private List<Attribute> attribute = new ArrayList<Attribute>();
    
    public void deleteLock() {
        hasLock=false;
	}

    public boolean hasLock() {
        return hasLock;
    }

    public boolean getLock() {
        return lock;
    }

    public void setLock(boolean lock) {
		this.lock=lock;
		hasLock=true;
    }

    public void deleteSerialVersion() {
        hasSerialVersion=false;
    }

    public boolean hasSerialVersion() {
        return hasSerialVersion;
    }

    public long getSerialVersion() {
        return serialVersion;
    }

    public void setSerialVersion(long serialVersion) {
        this.serialVersion=serialVersion;
		hasSerialVersion=true;
    }

    public boolean hasSystemId() {
        return hasSystemId;
    }
    
    public long getSystemId() {
        return systemId;
    }
    
    public void setSystemId(long systemId) {
        this.systemId=systemId;
        hasSystemId=true;
    }
    
    /**
     * Return true if this object has been persisted.
     * @return true if persisted, false otherwise.
     */
    public boolean isPersisted() {
        return systemId!=NOT_PERSISTED;
    }
    
    /**
     * Disassociate this object with it's persisted counterpart.
     */
    public void markAsNotPersisted() {
        systemId=NOT_PERSISTED;
        serialVersion=0;
    }
    
    /**
     * Build a list of all the fields on the specified class that should
     * be considered for cloning in the {@link #clone() clone()} method below.
     * We'll only return methods from supertypes which implement Type.
     * @param clazz Class to collect fields for
     * @return An ArrayList of instances of {@link java.lang.reflect.Field Field} 
     */
    @SuppressWarnings("unchecked")
    private ArrayList<Field> getAllDeclaredFields(Class clazz) {
        ArrayList<Field> al=new ArrayList<Field>(20);
        ArrayList<String> names=new ArrayList<String>();

        // Go up the class tree as far as Type, but DONT include Type itself. Also, take care not to add the same field twice.
        for(Class c=clazz ; c!=Type.class ; c=c.getSuperclass()) {
            for(Field f: c.getDeclaredFields()) {
                if (!names.contains(f.getName())) {
                    al.add(f);
                    names.add(f.getName());
                }
            }
        }
        
        // The only field we want from Type is attribute, so if clazz is a sub-type of Type, then add the attribute field.
        if (Type.class.isAssignableFrom(clazz)) {
          try {
            al.add(Type.class.getDeclaredField("attribute"));
          }
          catch(NoSuchFieldException e) {
            e.printStackTrace();
            throw new NotImplementedError("Cannot find the attribute field on Type");
          }
        }
        
        return al;
    }

	public JXPathContext fetchJXPathContext() {
		if (jXPathContext==null) {
            jXPathContext=JXPathContext.newContext(this);
            jXPathContext.setFunctions(TypeXPathFunctionRegister.getInstance().getFunctionLibrary());
		}

		return jXPathContext;
	}
    
    /**
     * Execute the given xpath expression on <i>this</i> and return the single result. 
     * @param xpath Expression to evaluate
     * @exception TypeXPathException If evaluation of the expression fails.
     * @return Result of evaluation.
	 */
    public Object xpathGet(String xpath) {
        try {
            return fetchJXPathContext().getValue(xpath);
        } catch (JXPathException e) {
            throw new TypeXPathException(e);
        }
    }

    /**
     * Execute the given xpath expression on <i>this</i>. The expectation is that the xpath expression
     * evaluates to more than one node. This method returns the matching nodes as an iteration.
     * @param xpath Expression to evaluate
     * @exception TypeXPathException If evaluation of the expression fails.
     * @return Result of evaluation.
     */
    @SuppressWarnings("unchecked")
    public Iterator xpathIterate(String xpath) {
        try {
            return fetchJXPathContext().iterate(xpath);
        } catch (JXPathException e) {
            throw new TypeXPathException(e);
        }
    }

    /**
     * Evaluate the given xpath expression on <i>this</i> and return the result as an instance
     * of the class <i>clazz</i>.
     * @param xpath Expression to evaluate
     * @param clazz Class to return an instance of
     * @return Result.
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T xpathGet(String xpath, Class<T> clazz) {
        try {
            return (T)fetchJXPathContext().getValue(xpath, clazz);
        } catch (JXPathException e) {
            throw new TypeXPathException(e);
        }
    }

    /**
     * Set the value of a property within <i>this</i> identified by an xpath expression to a the value <i>obj</i>.
     * @param xpath Expression identifying the property to set.
     * @param obj value to set the property to.
     */
    public void xpathSet(String xpath, Object obj) {
        try {
            fetchJXPathContext().setValue(xpath, obj);
        } catch (JXPathException e) {
            throw new TypeXPathException(e);
        }
    }

    /**
     * Clone this object. This clone method is used by all Type subclasses to handle deep cloning.
     * For the factory to operate correctly it is essential that Types can be deep cloned, as it
     * hangs onto prototyped instances by name and simply clones them when a request is made for
     * an instance of a named type.
     * @throws CloneNotSupportedException If the type cannot be deep cloned.
     */
	@SuppressWarnings("unchecked")
    public Object clone() throws CloneNotSupportedException {
            Object cloneObject=super.clone();

            ArrayList<Field> fields=getAllDeclaredFields(this.getClass());

            Field field=null;
            String fieldName=null;
            Class fieldType=null;
            Method method=null;
            String methodBaseName=null;

            for(int i=fields.size()-1 ; i>=0 ; i--) {

                try {
                    // the field we're cloning
                    field=fields.get(i);

                    // the class (or type) of the field we're cloning
                    fieldType=field.getType();
                    
                    // JXPath's context includes a cache, if we don't clear it the clone's
                    // JXPath will point into the thing we cloned!
                    ((Type)cloneObject).jXPathContext=null;
                    
                    // If the field is is a primitive, a String, a static, transient, or a core...
                    if (fieldType.isPrimitive()
                    ||  fieldType==String.class
                    ||  fieldType==Object.class
                    ||  Modifier.isStatic(field.getModifiers())
                    ||  Modifier.isTransient(field.getModifiers())
                    ||  Core.class.isAssignableFrom(fieldType)) {
                        // ...ignore it - super.clone() will have handled primitives, and
                        // cores and statics can be ignored altogether.
                        continue;
                    }

                    // the name of the field we're cloning
                    fieldName=field.getName();

                    // If the field name was myValue, methodBaseName will be MyValue (used in setMyValue, getMyValue).
                    methodBaseName=Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);

                    // If the field is a List, or implements List, then deep clone it.
                    if (List.class.isAssignableFrom(fieldType)) {
                        method=null;

                        // Get the List we're going to clone
                        method=this.getClass().getMethod("get"+methodBaseName);
                        List list=(List)method.invoke(this);

                        // We'll clone into this List
                        List<Object> clonedList=(List<Object>)list.getClass().newInstance();

                        Object type=null;

                        // Loop through the ArrayList cloning each element into cloneList.
                        // Note: ArrayLists of Strings are quite common, so we'll handle them too,
                        // but generally only classes based on Type can be cloned in this way.
                        for(int j=0 ; j<list.size() ; j++) {
                            type=list.get(j);
                            if (type instanceof String) {
                                clonedList.add(type);
                            }
                            else {
                                clonedList.add(((Type)type).clone());
                            }
                        }

                        // Set the cloned ArrayList into the clone. The assumption here is that the setter
                        // we're going to call takes only one argument, and that argument is the same
                        // as that returned by the getter. Safe enough.
                        method=this.getClass().getMethod("set"+methodBaseName, method.getReturnType());
                        method.invoke(cloneObject, clonedList);
                    }
                    // if the field is a Map, or implements Map, then deep clone it
                    else if (Map.class.isAssignableFrom(fieldType)) {
                        method=null;

                        // Get the Map we're going to clone
                        method=this.getClass().getMethod("get"+methodBaseName);
                        Map map=(Map)method.invoke(this);

                        // We'll clone into this clonedMap
                        Map clonedMap=(Map)map.getClass().newInstance();

                        Type value=null;

                        // Loop through the map cloning each element into cloneMap.
                        for(Object key: map.keySet() ) {
                            value=(Type)map.get(key);
                            clonedMap.put(key, (Type)value.clone());;
                        }

                        // Set the cloned map into the clone
                        method=this.getClass().getMethod("set"+methodBaseName, method.getReturnType());
                        method.invoke(cloneObject, clonedMap);
                    }
                    // if the field is a Set, or implements Set, then deep clone it
                    else if (Set.class.isAssignableFrom(fieldType)) {
                        method=null;

                        // Get the Set we're going to clone
                        method=this.getClass().getMethod("get"+methodBaseName);
                        Set<Type> set=(Set<Type>)method.invoke(this);

                        // We'll clone into this clonedSet
                        Set clonedSet=(Set)set.getClass().newInstance();

                        // Loop through the set cloning each element into cloneSet.
                        for(Type value: set ) {
                            clonedSet.add(value.clone());;
                        }

                        // Set the cloned set into the clone
                        method=this.getClass().getMethod("set"+methodBaseName, method.getReturnType());
                        method.invoke(cloneObject, clonedSet);
                    }
                    // If the field is a Type, clone it.
                    else if (Type.class.isAssignableFrom(fieldType) || CommandArg.class.isAssignableFrom(fieldType)) {
                        method=null;

                        method=this.getClass().getMethod("get"+methodBaseName);
                        Type fieldValue=(Type)method.invoke(this);

                        Object arg=(fieldValue==null) ? null : fieldValue.clone();

                        method=this.getClass().getMethod("set"+methodBaseName, method.getReturnType());
                        method.invoke(cloneObject, arg);
                    }
                    // If the field is a Date...
                    else if (Date.class.isAssignableFrom(fieldType)) {
                        method=null;

                        method=this.getClass().getMethod("get"+methodBaseName);
                        Date fieldValue=(Date)method.invoke(this);

                        Object arg=(fieldValue==null) ? null : fieldValue.clone();

                        method=this.getClass().getMethod("set"+methodBaseName, fieldType);
                        method.invoke(cloneObject, arg);
                    }
                    else if(fieldType.isEnum()) {
                        method=this.getClass().getMethod("get"+methodBaseName);
                        Enum fieldValue=(Enum)method.invoke(this);
                        
                        method=this.getClass().getMethod("set"+methodBaseName, fieldType);
                        method.invoke(cloneObject, fieldValue);
                    }
                    // If all else fails, set the clone field's value to null.
                    else {
                        method=this.getClass().getMethod("set"+methodBaseName, fieldType);
                        method.invoke(cloneObject, new Object[]{null});
                    }
                }
                catch(InvocationTargetException e) {
                    throw new CloneNotSupportedException("Attempt to clone "+this.getClass().getName()+"."+fieldName+" threw (InvocationTargetException) "+e.getTargetException());
                }
                catch(Exception e) {
                    throw new CloneNotSupportedException("Attempt to clone "+this.getClass().getName()+"."+fieldName+" threw "+e);
                }
            }
            return cloneObject;
    }
    
	/**
     * Invoke the getter for a named field on a given object and return whatever comes back. Chances are
     * the method will be get&lt;fieldName&gt;, but in the case of a boolean it may be is&lt;fieldName&gt;;
     * if the 'get' fails, we'll try the 'is'. 
     * @param fieldName field to call the method for.
     * @param subject Object to invoke the method on.
     * @return Whatever the getter returns
     * @throws SecurityException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
	 */
    private Object callGetter(String fieldName, Object subject) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        // if fieldname is 'forename', methodTail will be 'Forname'.
        String methodTail=Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);

        try {
            String methodName="get"+methodTail;
            Method method=subject.getClass().getMethod(methodName);
            return method.invoke(subject);
        }
        catch(NoSuchMethodException e) {
            try {
                String methodName="is"+methodTail;
                Method method=subject.getClass().getMethod(methodName);
                return method.invoke(subject);
            }
            catch(NoSuchMethodException x) {
                throw new NoSuchMethodError(subject.getClass().getName()+".[is|get]"+methodTail);
            }
        }
    }
    
	@SuppressWarnings("unchecked")
    private void callSetter(String fieldName, Class fieldType, Object on, Object value) throws SecurityException, NoSuchMethodException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String methodName="set"+Character.toUpperCase(fieldName.charAt(0))+fieldName.substring(1);
        Method method=on.getClass().getMethod(methodName, fieldType);
        method.invoke(on, value);
    }
    
    /**
     * Merge properties from 'donor' into 'subject'. The rules go like this:
     * <pre>
     *  For each property on the donor that isn't transient
     *    If the subject has the same property (or Field in reflection terminology)
     *      If the property is a primitive, a Date or an Enum
     *        If the subject field has <i>no value</i>
     *          Copy the value from donor into subject.
     *      If the property is based on List, Map, Set, or Collection
     *        If the subject's property is null
     *          Create a new List, Map, Set as appropriate
     *          Add the new collection to the subject
     *        For each element in the donor instance which extends Type
     *          If the element has an 'id' field and the subject doesn't have a matching element
     *            Clone the donor's element and add it to the subject
     *          If the subject doesn't contain (!x.contain(y)) the element 
     *            Clone the donor's element and add it to the subject
     *        For each element in the donor instance which doesn't extend Type
     *          If the subject doesn't contain (!x.contain(y)) the element
     *            Recurse
     *      If the property is an Object
     *        If it extends Type and subject has <i>no value</i> 
     *          Clone the donor's element and add it to the subject
     *        If it doesn't extend Type and the subject has no value
     *          Ignore it - we only merge Types or primitives
     *          
     * Where <i>no value</i> means:
     *   boolean: false
     *   int: 0
     *   double: 0
     *   float: 0
     *   char: 0
     *   bute: 0
     *   long: 0
     *   String null or ""
     *   Object: null
     *   Date: null
     *   Enum: null
     *   
     * </pre>
     * @param donor
     * @param subject
     * @param core
	 */
    @SuppressWarnings("unchecked")
    private void mergeDonorIntoSubject(Type donor, Type subject, Core core) {
        // If there's nothing to merge (from or into) then give up right away.
        if (donor==null || subject==null) {
            return;
        }

        // get a list of all the fields that the donor has to offer
        ArrayList<Field> donorFields=getAllDeclaredFields(donor.getClass());
        ArrayList<Field> subjectFields=getAllDeclaredFields(subject.getClass());
        
        Class fieldType=null;
        String fieldName=null;

        for(Field field: donorFields) {
            fieldType=field.getType();
            fieldName=field.getName();
            
            try {
                if (subjectFields.contains(field)) {
                    if (Modifier.isStatic(field.getModifiers())
                    ||  Modifier.isTransient(field.getModifiers())
                    ||  Core.class.isAssignableFrom(fieldType)) {
                        continue;
                    }
                    
                    if (fieldType.isPrimitive()) {
                        // get the subject's value for this field
                        Object sv=callGetter(fieldName, subject);

                        // if the field is a number...
                        if (sv instanceof Number) {
                            // ...and the subject has the value of zero, then override it.
                            if (Number.class.cast(sv).doubleValue()==0) {
                                callSetter(fieldName, fieldType, subject, callGetter(fieldName, donor));
                            }
                        }
                        // else if the field is a boolean and the subject's value is 'false' then override it.
                        else if (Boolean.class.cast(sv).booleanValue()==false){
                            callSetter(fieldName, fieldType, subject, callGetter(fieldName, donor));
                        }
                    }
                    else if (fieldType==Date.class || fieldType==String.class || fieldType.isEnum()) {
                        // if the subject's value for this field is null, override it with the donor's value
                        if (callGetter(fieldName, subject)==null) {
                            callSetter(fieldName, fieldType, subject, callGetter(fieldName, donor));
                        }
                    }
                    else if (Collection.class.isAssignableFrom(fieldType)) {
                        // if the donor has a collection, but the subject's corresponding collection is null...
                        if (callGetter(fieldName, donor)!=null && callGetter(fieldName, subject)==null) {
                            // output q warning. Well behaved classes should always initialise collections.
                            core.logWarning("Subject class "+subject.getClass().getName()+" has null collection for field: "+fieldName+". Class constructors should initialise collections. Collection not merged.");
                            continue;
                        }
                        
                        Collection c=(Collection)callGetter(fieldName, donor);
                        
                        // if the collection is null, ignore it - we cant't merge nothing!
                        if (c!=null) {
                            // for each object in the donor's collection
                            for(Object dObj: c) {
                                boolean merged=false;
                                // if the subject's collection has an object with the same id
                                for(Object sObj: (Collection)callGetter(fieldName, subject)) {
                                    // merge the objects if: 
                                    //  dObj and sObj both implement Identified and are equal by their identifier, or
                                    //  neither dObj or sObj implement Identifier but they are equal by 'equals()'.
                                    if ((dObj instanceof Identified && sObj instanceof Identified && ((Identified)sObj).compareById((Identified)dObj))
                                    ||  (!(dObj instanceof Identified || sObj instanceof Identified) && dObj.equals(sObj))) {
                                        // merge them
                                        mergeDonorIntoSubject((Type)dObj, (Type)sObj, core);
                                        merged=true;
                                        break;
                                    }
                                }
    
                                // if a match wasn't found in the subject, then add a clone of the donor's
                                if (!merged) {
                                    ((Collection)callGetter(fieldName, subject)).add(((Type)dObj).clone());
                                }
                            }
                        }
                    }
                    else if (Type.class.isAssignableFrom(fieldType)) {
                        // if the subject doesn't have a matching value, clone the donor's and us it.
                        if (callGetter(fieldName, subject)==null) {
                            Type t=(Type)callGetter(fieldName, donor);
                            if (t!=null) {
                                callSetter(fieldName, fieldType, this, t.clone());
                            }
                        }
                        // if the subject does have an instance, then merge the donor's values into it.
                        else {
                            mergeDonorIntoSubject((Type)callGetter(fieldName, donor), (Type)callGetter(fieldName, subject), core);
                        }
                    }
                    else {
                        core.logWarning("field: "+fieldName+" was ignored during merge");
                    }
                }
            }
            catch(Exception e) {
                e.printStackTrace();
                core.logWarning("Failed to merge: "+fieldName);
            }                
        }
    }

    /**
     * Merge data from a specified type into <i>this</i>. This method does not demand that the donor and 'this' (the subject) have
     * to be of the same or even compatible types; we will simply copy whatever we can based on the property names and types.
     * @param donor Take values from here into <i>this</i>
	 */
    public void mergeWithDataFrom(Type donor, Core core) {
        mergeDonorIntoSubject(donor, this, core);
    }

	/**
	 * Get the collection of instances of Attribute associated with this object.
	 * @return attribute A collection of instances of Excess
	 * @see #setAttribute
	 */
	public List<Attribute> getAttribute() {
		return attribute;
	}    

	/**
	 * Set the collection of instances of Attribute associated with this object.
	 * @param attribute A collection of instances of Excess
	 * @see #getAttribute
	 */
	public void setAttribute(List<Attribute> attribute) {
		this.attribute = new ArrayList<Attribute>(attribute);
	}

	/**
	 * Get a count of the number of Attribute instances associated with this object
	 * @return Number of instances
	 */
	public int getAttributeCount() {
		return this.attribute.size();
	}

	/**
	 * Remove the element specified from the list.
	 * @param i Index of element to remove
	 */
	public void removeAttribute(int i) {
		this.attribute.remove(i);
	}

	/**
	 * Remove the specified instance of Attribute from the list.
	 * @param attribute Instance to be removed
	 */
	public void removeAttribute(Attribute attribute) {
		this.attribute.remove(attribute);
	}

	/**
	 * Add an instance of Attribute to the list associated with this object.
	 * @param attribute Instance to add to list
	 */
	public void addAttribute(Attribute attribute) {
		this.attribute.add(attribute);
	}

    /**
     * Fetch a spacific Attribute from the collection by index number.
     * @param i Index of element to return
     * @retun The instance of Attribute at the specified index
     */
    public Attribute getAttribute(int i) {
        return this.attribute.get(i);
    }

    /**
     * The foreign system id is provided in order to make mapping to/from external system somewhat easier. The
     * expectation is that information extracted from foreign systems will have IDs of some kind which those
     * system uses to identify the data. 
     * @return The ID
     */
    public String getForeignSystemId() {
        return foreignSystemId;
    }

    /**
     * @see #getForeignSystemId()
     * @param foreignSystemId
     */
    public void setForeignSystemId(String foreignSystemId) {
        this.foreignSystemId = foreignSystemId;
    }
}
