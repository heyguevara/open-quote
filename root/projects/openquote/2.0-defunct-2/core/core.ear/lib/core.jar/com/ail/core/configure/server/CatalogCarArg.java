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
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/CatalogCarArg.java,v $
 * @stereotype arg
 */
public interface CatalogCarArg extends CommandArg {
    /**
     * Getter for the namespacesRet property. A list of the configuration namespaces found
     * in the car.
     * @return Value of namespacesRet, or null if it is unset
     */
    Collection<String> getNamespacesRet();

    /**
     * Setter for the namespacesArg property. 
     * @see #getNamespacesRet
     * @param namespacesRet new value for property.
     */
    void setNamespacesRet(Collection<String> namespacesRet);

    /**
     * Getter for the carArg property. The par to import configurations from
     * @return Value of carArg, or null if it is unset
     */
    byte[] getCarArg();

    /**
     * Setter for the carArg property. * @see #getCarArg
     * @param carArg new value for property.
     */
    void setCarArg(byte[] carArg);
}
