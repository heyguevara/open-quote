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

import com.ail.core.command.CommandArgImp;
import com.ail.core.CommandScript;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/SetCommandScriptArgImp.java,v $
 */
public class SetCommandScriptArgImp extends CommandArgImp implements SetCommandScriptArg {
    static final long serialVersionUID = 1199346453402049909L;
    private String namespaceArg;
    private String commandNameArg;
    private String commandTypeArg;
    private CommandScript commandScriptArg;

    /** Default constructor */
    public SetCommandScriptArgImp() {
    }

    /**
     * {@inheritDoc}
     * @see #setNamespaceArg
     * @return value of namespaceArg
     */
    public String getNamespaceArg() {
        return this.namespaceArg;
    }

    /**
     * {@inheritDoc}
     * @see #getNamespaceArg
     * @param namespaceArg New value for namespaceArg argument.
     */
    public void setNamespaceArg(String namespaceArg) {
        this.namespaceArg = namespaceArg;
    }

    /**
     * {@inheritDoc}
     * @see #setCommandNameArg
     * @return value of commandNameArg
     */
    public String getCommandNameArg() {
        return this.commandNameArg;
    }

    /**
     * {@inheritDoc}
     * @see #getCommandNameArg
     * @param commandNameArg New value for commandNameArg argument.
     */
    public void setCommandNameArg(String commandNameArg) {
        this.commandNameArg = commandNameArg;
    }

    /**
     * {@inheritDoc}
     * @see #setCommandScriptArg
     * @return value of commandScriptArg
     */
    public CommandScript getCommandScriptArg() {
        return this.commandScriptArg;
    }

    /**
     * {@inheritDoc}
     * @see #getCommandScriptArg
     * @param commandScriptArg New value for commandScriptArg argument.
     */
    public void setCommandScriptArg(CommandScript commandScriptArg) {
        this.commandScriptArg = commandScriptArg;
    }

    /**
     * {@inheritDoc}
     * @return The script type.
     */
    public String getCommandTypeArg() {
        return this.commandTypeArg;
    }

    /**
     * {@inheritDoc}
     * @see #getCommandTypeArg
     * @param commandTypeArg
     */
    public void setCommandTypeArg(String commandTypeArg) {
        this.commandTypeArg=commandTypeArg;
    }
}


