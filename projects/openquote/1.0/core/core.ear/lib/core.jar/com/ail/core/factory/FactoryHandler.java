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

package com.ail.core.factory;

import com.ail.core.Core;
import com.ail.core.NotImplementedError;
import com.ail.core.Type;
import com.ail.core.command.AbstractCommand;
import com.ail.core.configure.Builder;
import com.ail.core.configure.ConfigurationOwner;

public class FactoryHandler {
    /**
     * @link
     * @shapeType PatternLink
     * @pattern Singleton
     * @supplierRole Singleton factory
     */

    /*# private FactoryHandler _factoryHandler; */

    private static FactoryHandler instance = null;

    protected FactoryHandler() {
    }

	/**
     * Fetch the builder associated with a type. This method
     * operates a cache to speed type creation. The hashtable
     * 'builders' maps builder names to instances of the appropriate
     * sub-class of AbstractFactory.
     * @param builderName The name of the builder.
     * @param core The core to use to get configs.
     * @return The Factory to use to create instances of type 't'.
     */
	@SuppressWarnings("unchecked")
    private AbstractFactory fetchBuilder(String builderName, Core core) {
		// Fetch the Builder to use to create instance of this type.
		Builder builder=(Builder)core.getGroup("_Builders."+builderName);

        if (builder.getInstance()==null) {
			try {
				Class clazz=Class.forName(builder.getFactory());
				builder.setInstance((AbstractFactory)clazz.newInstance());
			}
            catch(ClassNotFoundException e) {
                throw new FactoryConfigurationError("Builder factory class not found: "+builder.getFactory());
            }
            catch(InstantiationException e) {
                throw new FactoryConfigurationError("Builder factory failed to instantiate: "+builder.getFactory());
            }
            catch(IllegalAccessException e) {
                throw new FactoryConfigurationError("Builder factory failed to instantiate - Illegal access: "+builder.getFactory());
			}
        }

		return builder.getInstance();
    }

	/**
     * Create an instance of a type and cache it as a prototype if required to.
     * @param typeSpec The configuration details of the type.
     * @return An instance of the type.
     */
	private Object createInstance(com.ail.core.configure.Type typeSpec, Core core) {
	    // If there's a prototype already, try using it.
	    if (typeSpec.getPrototype()!=null) {
	        try {
	            if (typeSpec.isSingleInstance()) {
	                return typeSpec.getPrototype();
                }
                else {
                    return typeSpec.getPrototype().clone();
                }
	        }
	        catch(CloneNotSupportedException e) {
	            // Tell the world that the clone failed, but not to worry: we'll fall 
	            // through and create the instance the old fashoned method.
	            core.logWarning("Deep clone not supported by type '"+typeSpec.getName()+"'. Attempt to clone threw:"+e);
	        }
	    }
    
        // get hold of the Factory for this type
        AbstractFactory factory=fetchBuilder(typeSpec.getBuilder(), core);

        Object newInstance=null;
        
        if (factory.cachePrototype()) {
            synchronized(typeSpec) {
                if (typeSpec.getPrototype()==null) {
                    // Use the factory to create the instance
                    newInstance=factory.createType(typeSpec, core);
                
                    try {
                        if (typeSpec.isSingleInstance()) {
                            typeSpec.setPrototype((Type)newInstance);
                        }
                        else {
                            typeSpec.setPrototype((Type)((Type)newInstance).clone());
                        }
                    }
                    catch(CloneNotSupportedException e) {
                        // Tell the world about the clone failure, but don't worry about it. Performace may
                        // suffer as a result, but that may not be an issue.
                        core.logWarning("Clone for prototype failed for '"+typeSpec.getName()+"'. Attempt to clone threw:"+e);
                    }
                }
                else {
                    newInstance=createInstance(typeSpec, core);
                }
            }
        }
        else {
            // Use the factory to create the instance
            newInstance=factory.createType(typeSpec, core);        
        }
        
        return newInstance;
        
//        synchronized(typeSpec) {
//            if (typeSpec.getPrototype()==null) {
//                // get hold of the Factory for this type
//        	    AbstractFactory factory=fetchBuilder(typeSpec.getBuilder(), core);
//        
//        	    // Use the factory to create the instance
//        	    Object instance=factory.createType(typeSpec, core);
//            
//        	    // Cache the instance as a prototype if the factory says we should.
//        	    if (factory.cachePrototype()) {
//        	        try {
//                        if (typeSpec.isSingleInstance()) {
//                            typeSpec.setPrototype((Type)instance);
//                        }
//                        else {
//                            typeSpec.setPrototype((Type)((Type)instance).clone());
//                        }
//                    }
//                    catch(CloneNotSupportedException e) {
//                        // Tell the world about the clone failure, but don't worry about it. Performace may
//                        // suffer as a result, but that may not be an issue.
//                        core.logWarning("Clone for prototype failed for '"+typeSpec.getName()+"'. Attempt to clone threw:"+e);
//                    }
//        		}
//        	    
//        	    return instance;
//            }
//            else {
//                return createInstance(typeSpec, core);
//            }
//        }
	}

    public static FactoryHandler getInstance() {
        if (instance == null) {
            instance = new FactoryHandler();
        }

        return instance;
    }

    public Type newType(String name, ConfigurationOwner owner, Core core) {
        com.ail.core.configure.Type type=null;

		// fetch the type's details from configuration
        try {
            type=(com.ail.core.configure.Type)core.getGroup("_Types."+name);
        }
        catch(ClassCastException e) {
            throw new UndefinedTypeError("Type: '"+name+"' is not based on Type.class");
        }

		if (type==null) {
            throw new UndefinedTypeError("Type: '"+name+"' is undefined");
        }

        return (Type)createInstance(type, core);
    }

    public AbstractCommand newCommand(String name, ConfigurationOwner owner, Core core) {
        return (AbstractCommand)newType(name, owner, core);
    }

    public Object newObject(String name, ConfigurationOwner owner, Core core) {
        throw new NotImplementedError("FactoryHandler.newObject");
    }
}
