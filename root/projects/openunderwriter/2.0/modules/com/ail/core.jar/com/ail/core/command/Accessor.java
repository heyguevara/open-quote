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

package com.ail.core.command;

import java.lang.management.ManagementFactory;
import java.lang.management.ThreadMXBean;
import java.util.HashMap;
import java.util.Map;

import com.ail.core.Core;

/**
 * This class is extended by all accessor classes. Its function is simply to
 * group the accessors as a sub-type of AbstractCommand, and to differentiate
 * them from other CommandImpl types.
 */
abstract public class Accessor extends AbstractCommand {
    private transient Map<Argument, Long> cpuTime=new HashMap<Argument, Long>();
    private transient Map<Argument, Long> userTime=new HashMap<Argument, Long>();
    
    /** The logging indicator controls the level of logging
     * generated by a service invocation.
     */
    private AccessorLoggingIndicator loggingIndicator=AccessorLoggingIndicator.NONE;

    /**
     * Get the logging indicator as a String. The value returned will be
     * one of the {@link AccessorLoggingIndicator AccessorLoggingIndicator} values
     * as a String.
     * @return Value of indicator
     */
    public String getLoggingIndicatorAsString() {
       if (loggingIndicator!=null) {
            return loggingIndicator.toString();
        }
        return null;
    }

    /**
     * Set the logging indicator from a string value. The String must be
     * valid for a call to {@link AccessorLoggingIndicator AccessorLoggingIndicator.forName}
     * @param logging Logging level
     */
    public void setLoggingIndicatorAsString(String logging) {
        this.loggingIndicator = AccessorLoggingIndicator.valueOf(logging);
    }

    /**
     * Get the logging level
     * @return The current logging level
     */
    public AccessorLoggingIndicator getLoggingIndicator() {
        return loggingIndicator;
    }

    /**
     * Set the logging level.
     * @param loggingIndicator
     */
    public void setLoggingIndicator(AccessorLoggingIndicator loggingIndicator) {
        this.loggingIndicator=loggingIndicator;
    }

    /**
     * Implementing classes invoke this method to output a message (if
     * the logging indicator demands it) when a service is invoked.
     */
    protected void logEntry() {
        if (!AccessorLoggingIndicator.NONE.equals(loggingIndicator)) {

            ThreadMXBean t=ManagementFactory.getThreadMXBean();
            cpuTime.put(getArgs(), t.getCurrentThreadCpuTime());
            userTime.put(getArgs(), t.getCurrentThreadUserTime());

            Core core=new Core(getArgs().getCallersCore());

            if (AccessorLoggingIndicator.CALL.equals(loggingIndicator)) {
                core.logDebug(this.getClass().getName()+":"+getArgs().getClass().getName() +" invoked.");
            }
            else if (AccessorLoggingIndicator.FULL.equals(loggingIndicator)) {
                String xmlArgs=core.toXML(getArgs()).toString();
                core.logDebug(this.getClass().getName()+" invoked with arguments: "+xmlArgs);
            }
        }
    }

    /**
     * Implementing classes invoke this method to output a message (if
     * the logging indicator demands it) when a service returns.
     */
    protected void logExit() {
        if (!AccessorLoggingIndicator.NONE.equals(loggingIndicator)) {

            ThreadMXBean t=ManagementFactory.getThreadMXBean();
            String timing;
            
            try {
                long cpu=t.getCurrentThreadCpuTime()-cpuTime.remove(getArgs());
                long user=t.getCurrentThreadUserTime()-userTime.remove(getArgs());
                timing="(in "+cpu+"ns cpu time, "+user+"ns user time)";
            }
            catch(NullPointerException e) {
                timing="";
            }
            
            Core core=new Core(getArgs().getCallersCore());

            if (AccessorLoggingIndicator.CALL.equals(loggingIndicator)) {
                core.logDebug(this.getClass().getName()+":"+getArgs().getClass().getName() +" returned "+timing);
            }
            else if (AccessorLoggingIndicator.FULL.equals(loggingIndicator)) {
                String xmlArgs=core.toXML(getArgs()).toString();
                core.logDebug(core.getClass().getName()+" returned "+timing+" with results: "+xmlArgs);
            }
        }
    }
}
