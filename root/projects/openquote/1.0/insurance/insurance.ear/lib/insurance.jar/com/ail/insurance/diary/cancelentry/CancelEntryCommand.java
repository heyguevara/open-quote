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

package com.ail.insurance.diary.cancelentry;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/diary/cancelentry/CancelEntryCommand.java,v $
 */
public class CancelEntryCommand extends Command implements CancelEntryArg {
    private static final long serialVersionUID = -5489816808892198373L;
    private CancelEntryArg args = null;

    public CancelEntryCommand() {
        super();
        args = new CancelEntryArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (CancelEntryArg)arg;
    }

    public CommandArg getArgs() {
        return (com.ail.core.command.CommandArg) args;
    }
}
