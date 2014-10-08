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

package com.ail.core.logging;

import com.ail.core.command.Accessor;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

import java.util.Date;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/06/17 21:49:36 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/logging/LoggerCommand.java,v $
 */
public class LoggerCommand extends Command implements LoggerArg {
	private Accessor accessor=null;
    private LoggerArg args=null;

    public LoggerCommand() {
        super();
		args=new LoggerArgImp();
    }

	public LoggerCommand(Accessor accessor, LoggerArgImp args) {
        setAccessor(accessor);
        setArgs(args);
    }

	public Accessor getAccessor() {
        return accessor;
    }

	public void setAccessor(Accessor accessor) {
        this.accessor=accessor;
    }

	public void setService(Accessor accessor) {
	    this.accessor=accessor;
	}

    public void setArgs(CommandArg arg) {
		this.args=(LoggerArg)arg;
    }

    public CommandArg getArgs() {
		return args;
    }

    public void setMessage(String message) {
		args.setMessage(message);
    }

    public Date getDate() {
		return args.getDate();
    }

    public void setDate(Date date) {
		args.setDate(date);
    }

    public Severity getSeverity() {
		return args.getSeverity();
    }

    public void setSeverity(Severity severity) {
		args.setSeverity(severity);
    }

    public String getMessage() {
		return args.getMessage();
    }

    public Throwable getCause() { 
        return args.getCause();
    }
    
    public void setCause(Throwable cause) { 
        args.setCause(cause);
    }
}
