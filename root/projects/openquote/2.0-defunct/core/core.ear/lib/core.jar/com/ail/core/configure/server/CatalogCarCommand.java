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
import com.ail.core.command.Command;
/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/CatalogCarCommand.java,v $
 * @stereotype command
 */
public class CatalogCarCommand extends Command implements CatalogCarArg {
    
    private CatalogCarArg args = null;

    public CatalogCarCommand() {
        super();
        args = new CatalogCarArgImp();
    }

    public void setArgs(com.ail.core.command.CommandArg arg) {
        this.args = (CatalogCarArg)arg;
    }

    public com.ail.core.command.CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Collection<String> getNamespacesRet() {
        return args.getNamespacesRet();
    }

    /**
     * {@inheritDoc}
     * @param namespacesRet @{inheritDoc}
     */
    public void setNamespacesRet(Collection<String> namespacesRet) {
        args.setNamespacesRet(namespacesRet);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public byte[] getCarArg() {
        return args.getCarArg();
    }

    /**
     * {@inheritDoc}
     * @param carArg @{inheritDoc}
     */
    public void setCarArg(byte[] carArg) {
        args.setCarArg(carArg);
    }
}
