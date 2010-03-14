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
import com.ail.core.configure.ConfigurationSummary;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/07/31 18:04:03 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/GetNamespacesHistoryCommand.java,v $
 */
public class GetNamespacesHistoryCommand extends Command implements GetNamespacesHistoryArg {
    private GetNamespacesHistoryArg args=null;

    /**
     * Default constructor.
     */
    public GetNamespacesHistoryCommand() {
        super();
		args=new GetNamespacesHistoryArgImp();
    }

    /**
     * {inheritDoc}
     */
    public void setArgs(CommandArg arg) {
		this.args=(GetNamespacesHistoryArg)arg;
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
    public void setNamespacesRet(Collection<ConfigurationSummary> namespaces) {
        args.setNamespacesRet(namespaces);
    }

    /**
     * {@inheritDoc}
     */
    public Collection<ConfigurationSummary> getNamespacesRet() {
        return args.getNamespacesRet();
    }

    /**
     * {@inheritDoc}
     */
    public String getNamespaceArg() {
        return args.getNamespaceArg();
    }

    /**
     * {@inheritDoc}
     */
    public void setNamespaceArg(String namespaceArg) {
        args.setNamespaceArg(namespaceArg);
    }
}
