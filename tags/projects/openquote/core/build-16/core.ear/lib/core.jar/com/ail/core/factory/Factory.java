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

import com.ail.core.command.AbstractCommand;

/**
 * This interface describes the contract between the Core class and the Factory
 * sub-system. It defines the methods that Core is expected to expose on behalf
 * of the Factory, and for which the Factory provides Entry Points based
 * implementations.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:29 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/factory/Factory.java,v $
 */
public interface Factory {

	/**
     * Create a new instance of the named command. The details of the type
     * to be created are loaded from the callers configuration.
     * @param version The version of the command to create.
     * @param commandName The name to be used to locate the commands details.
	 * @return An instance of the command.
     */
	AbstractCommand newCommand(String commandName);

	/**
     * Create a new instance of the named type. The typeName argument
     * relates to a type in the callers configuration which defines the
     * specifics of the type to be created.
     * @param version The version of the type to create
     * @param typeName The name use to loafe the type's details.
     * @return An instance of a type.
     */
	com.ail.core.Type newType(String typeName);

	/**
     * Create an instance of the specific object. The objectName argument
     * relates to an object described in the callers configuration. This
     * in turn defines the specifics of the object to be created.
	 * @param version The version of the object to create.
     * @param objectName The name of the object in the callers configuration.
     * @return The instantiated object.
     */
	Object newObject(String objectName);
}
