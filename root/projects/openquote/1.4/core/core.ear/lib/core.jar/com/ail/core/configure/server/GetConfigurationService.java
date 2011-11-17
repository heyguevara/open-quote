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

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.ConfigurationHandler;

public class GetConfigurationService extends Service {
    private GetConfigurationArg args=null;
	private Core core=null;

	/**
     * Default constructor
     */
	public GetConfigurationService() {
		core=new Core(this);
    }

	/**
     * Getter to fetch the entry point's code. This method is demanded by
     * the Service class.
     * @return This entry point's instance of Core.
     */
	public Core getCore() {
        return core;
    }

	/**
     * Setter used to the set the entry points arguments. This method will be
     * called before <code>invoke()</code> is called.
     * @param args for invoke
     */
    public void setArgs(CommandArg args){
        this.args = (GetConfigurationArg)args;
    }

	/**
     * Getter returning the arguments used by this entry point. This entry point
     * doesn't modify the arguments.
     * @return An instance of LoggerArgs.
	 */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Override and return the namespace we've been asked to fetch.
     * @return The namespace we've been invoked to fetch.
     */
    public String getConfigurationNamespace() {
        return args.getNamespaceArg();
    }

	/**
     * The 'business logic' of the entry point.
     */
	public void invoke() throws PreconditionException {
        if (args.getNamespaceArg()==null || args.getNamespaceArg().length()==0) {
            throw new PreconditionException("namespace!=null && namespace!=\"\"");
        }

        args.setConfigurationRet(ConfigurationHandler.getInstance().loadConfiguration(this, this, core));
    }

    /**
     * Stub this out to prevent the Service parent class for
     * running any pre-service before invoke is called.
     */
    public void invokePre() throws BaseException {
    }

    /**
     * Stub this out to prevent the Service parent class for
     * running any pre-service before invoke is called.
     */
    public void invokePost() throws BaseException {
    }
}



