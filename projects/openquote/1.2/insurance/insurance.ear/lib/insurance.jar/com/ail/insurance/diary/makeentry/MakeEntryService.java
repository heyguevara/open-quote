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

package com.ail.insurance.diary.makeentry;

import com.ail.core.Core;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 23:19:08 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/diary/makeentry/MakeEntryService.java,v $
 */
public class MakeEntryService extends Service {
    private static final long serialVersionUID = -5414824114546710596L;
    private MakeEntryArg args = null;
    private Core core = null;

    /** Default constructor */
    public MakeEntryService() {
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
        v.setDate("$Date: 2006/01/15 23:19:08 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/diary/makeentry/MakeEntryService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.2 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param Arguments for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (MakeEntryArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of $Name:  $Args.
     */
    public CommandArg getArgs() {
        return (com.ail.core.command.CommandArg) args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() {
        // Your business logic here!
    }
}


