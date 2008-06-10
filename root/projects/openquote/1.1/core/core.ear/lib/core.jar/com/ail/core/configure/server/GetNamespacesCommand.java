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

package com.ail.core.configure.server;

import java.util.Collection;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * Fetch a list of all the configuration namespaces known to the system. A collection of Strings is returned, one String for each namespace.
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2005/07/19 21:18:15 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/GetNamespacesCommand.java,v $
 */
public class GetNamespacesCommand extends Command implements GetNamespacesArg {
    private GetNamespacesArg args=null;

    /**
     * Default constructor.
     */
    public GetNamespacesCommand() {
        super();
		args=new GetNamespacesArgImp();
    }

    /**
     * {inheritDoc}
     */
    public void setArgs(CommandArg arg) {
		this.args=(GetNamespacesArg)arg;
    }

    /**
     * {inheritDoc}
     */
    public CommandArg getArgs() {
		return args;
    }

    /**
     * {@inheritDoc}
     */
    public void setNamespaces(Collection<String> namespaces) {
        args.setNamespaces(namespaces);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<String> getNamespaces() {
        return args.getNamespaces();
    }
}
