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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/GetConfigurationCommand.java,v $
 */
public class GetConfigurationCommand extends Command implements GetConfigurationArg {
    private GetConfigurationArg args=null;

    /**
     * Default constructor.
     */
    public GetConfigurationCommand() {
        super();
		args=new GetConfigurationArgImp();
    }

    /**
     * {inheritDoc}
     */
    public void setArgs(CommandArg arg) {
		this.args=(GetConfigurationArg)arg;
    }

    /**
     * {inheritDoc}
     */
    public CommandArg getArgs() {
		return args;
    }

    /**
     * {inheritDoc}
     */
    public void setConfigurationRet(Configuration configurationRet) {
        args.setConfigurationRet(configurationRet);
    }

    /**
     * {inheritDoc}
     */
    public Configuration getConfigurationRet() {
        return args.getConfigurationRet();
    }

    /**
     * {inheritDoc}
     */
    public void setNamespaceArg(String namespace) {
        args.setNamespaceArg(namespace);
    }

    /**
     * {inheritDoc}
     */
    public String getNamespaceArg() {
        return args.getNamespaceArg();
    }
}
