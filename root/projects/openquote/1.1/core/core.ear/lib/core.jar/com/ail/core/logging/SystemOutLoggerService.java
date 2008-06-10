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

import com.ail.core.Service;
import com.ail.core.command.CommandArg;
import com.ail.core.Version;
import java.io.PrintStream;
import java.text.SimpleDateFormat;
import com.ail.core.Core;

/**
 * This logging entry point directs log messages to the System print streams 'out'
 * and 'err'.
 * Messages are output to a 'out' and 'err' depending on their severity. DEBUG and
 * INFO messages are written to 'out', WARNING, ERROR, and FATAL messages are
 * written to 'err'.<p>
 * Messages are written in the following form:<p>
 * &nbsp;<i>owner</i>:<i>date</i>:<i>severity</i>:<i>effective date</i>:<i>message</i>
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2007/06/17 21:49:36 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/logging/SystemOutLoggerService.java,v $
 */
public class SystemOutLoggerService extends Service {
    private LoggerArgImp args=null;
	private static SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy hh:mm:ss");

	/**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
	public Version getVersion() {
		Version v=new Version();
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2007/06/17 21:49:36 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/logging/SystemOutLoggerService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.4 $");
        return v;
    }

	/**
     * This entry point has no Core requirements, so simply return null.
     * @return null
     */
	public Core getCore() {
        return null;
    }

	/**
     * Setter used to the set the arguments that <code>invoke()</code> will
     * use when it is called.
     * @param args for invoke
     */
    public void setArgs(CommandArg args){
        this.args = (LoggerArgImp)args;
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
     * The 'business logic' of the entry point.
     */
	public void invoke() {
		// default to writting to error stream.
		PrintStream logTo=System.err;

		// direct DEBUG and INFO messages to System.out
		if (Severity.DEBUG.equals(args.getSeverity()) || Severity.INFO.equals(args.getSeverity())) {
			logTo=System.out;
        }

		// Write the log message
		synchronized(format) {
		    if (args.getCause()==null) {
		        logTo.println(args.getCallersCore().getClass().getName()+":"+format.format(args.getDate())+":"+args.getSeverity()+":"+format.format(args.getCallersCore().getVersionEffectiveDate().getDate())+":"+args.getMessage());
            }
            else {
                logTo.println(args.getCallersCore().getClass().getName()+":"+format.format(args.getDate())+":"+args.getSeverity()+":"+format.format(args.getCallersCore().getVersionEffectiveDate().getDate())+":"+args.getMessage()+" Cause follows:");
                args.getCause().printStackTrace(logTo);
            }
		}
    }
}
