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
import com.ail.core.command.CommandArgImp;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/09/03 18:07:56 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/PackageCarArgImp.java,v $
 */
public class PackageCarArgImp extends CommandArgImp implements PackageCarArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** A collection of strings providing the names (namespaces) of the configurations to be packaged */
    private Collection<String> namespacesArg;

    /** The packaged car as a byte array. */
    private byte[] carRet;

    /** Default constructor */
    public PackageCarArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Collection<String> getNamespacesArg() {
        return namespacesArg;
    }

    /**
     * {@inheritDoc}
     * @param namespacesArg @{inheritDoc}
     */
    public void setNamespacesArg(Collection<String> namespacesArg) {
        this.namespacesArg = namespacesArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public byte[] getCarRet() {
        return carRet;
    }

    /**
     * {@inheritDoc}
     * @param carRet @{inheritDoc}
     */
    public void setCarRet(byte[] carRet) {
        this.carRet = carRet;
    }
}


