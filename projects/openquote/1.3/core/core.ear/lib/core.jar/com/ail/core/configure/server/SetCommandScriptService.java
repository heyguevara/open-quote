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

import com.ail.core.Service;
import com.ail.core.Core;
import com.ail.core.Version;
import com.ail.core.PreconditionException;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.Type;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/SetCommandScriptService.java,v $
 */
public class SetCommandScriptService extends Service {
    private SetCommandScriptArg args = null;
    private Core core = null;

    /** Default constructor */
    public SetCommandScriptService() {
        core = new Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2005/07/16 10:23:27 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/SetCommandScriptService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.2 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (SetCommandScriptArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return The arguments object.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Return the namespace of the configuration we're updating
     * @return The classes namespace
     */
    public String getConfigurationNamespace() {
        return (args.getNamespaceArg()!=null) ? args.getNamespaceArg() : args.getCommandScriptArg().getNamespace();
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException {
        if (args.getCommandScriptArg()==null) {
            throw new PreconditionException("commandScriptArg==null");
        }

        // For this sevice the args can either be specified seperatly in the args (as is normal)
        // or within the CommandScript arg. The args ones have priority
        String namespaceArg   = (args.getNamespaceArg()!=null) ? args.getNamespaceArg() : args.getCommandScriptArg().getNamespace();
        String commandNameArg = (args.getCommandNameArg()!=null) ? args.getCommandNameArg() : args.getCommandScriptArg().getCommandName();
        String commandTypeArg = (args.getCommandTypeArg()!=null) ? args.getCommandTypeArg() : args.getCommandScriptArg().getType();

        if (namespaceArg==null || namespaceArg.length()==0) {
            throw new PreconditionException("namespace==null || namespace==''");
        }

        if (commandNameArg==null || commandNameArg.length()==0) {
            throw new PreconditionException("typeNameArg==null || typeNameArg==''");
        }

        if (commandTypeArg==null || commandTypeArg.length()==0) {
            throw new PreconditionException("commandTypeArg==null || commandTypeArg==''");
        }

        try {
            // Get configuration for specified namespace
            Configuration config=core.getConfiguration();

            // First, find the type and its accessor. Then param's name should be Service, but accept "Accessor"
            // too for backwards compatibility reasons.
            String accessorTypeName;
            try {
              accessorTypeName=config.findType(args.getCommandNameArg()).findParameter("Service").getValue();
            } catch(NullPointerException e) {
              accessorTypeName=config.findType(args.getCommandNameArg()).findParameter("Accessor").getValue();
            }

            Type accessorType=config.findType(accessorTypeName);

            accessorType.setKey("com.ail.core.command."+commandTypeArg+"Accessor");
            accessorType.findParameter("Script").setValue(args.getCommandScriptArg().getScript());

            // Set configuration for namespace
            core.setConfiguration(config);
        }
        catch(NullPointerException e) {
            throw new PreconditionException("Script or accessor not defined for command:"+commandNameArg+" in namespace:"+namespaceArg);
        }
    }
}


