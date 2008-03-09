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

package com.ail.core.configure.server;

import java.util.Collection;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/09/03 18:07:56 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/PackageCarArg.java,v $
 * @stereotype arg
 */
public interface PackageCarArg extends CommandArg {
    /**
     * Getter for the namespacesArg property. A collection of strings providing the names (namespaces) of the
     * configurations to be packaged
     * @return Value of namespacesArg, or null if it is unset
     */
    Collection<String> getNamespacesArg();

    /**
     * Setter for the namespacesArg property. * @see #getNamespacesArg
     * @param namespacesArg new value for property.
     */
    void setNamespacesArg(Collection<String> namespacesArg);

    /**
     * Getter for the carRet property. The packaged car as a byte array.
     * @return Value of carRet, or null if it is unset
     */
    byte[] getCarRet();

    /**
     * Setter for the parRet property.
     * @see #getCarRet
     * @param carRet new value for property.
     */
    void setCarRet(byte[] carRet);
}


