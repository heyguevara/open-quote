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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/diary/makeentry/MakeEntryCommand.java,v $
 */
public class MakeEntryCommand extends Command implements MakeEntryArg {
    private static final long serialVersionUID = -4887196665132323576L;
    private MakeEntryArg args = null;

    public MakeEntryCommand() {
        super();
        args = new MakeEntryArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (MakeEntryArg)arg;
    }

    public CommandArg getArgs() {
        return (com.ail.core.command.CommandArg) args;
    }
}
