/* Copyright Applied Industrial Logic Limited 2008. All rights reserved. */
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

package com.ail.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

/**
 * Record the details of an individual exception/error. This class provides a means to record exceptions and
 * errors rather than simply dumping them to a log file. 
 * @since 2.3
 */
public class ExceptionRecord extends Type {
    static final int MAX_FRAMES_TO_CAPTURE=50;
    
    /** The date/time at which the exception occurred */
    private Date date = null;
    
    /** The reason property taken from the exception */
    private String reason;
    
    /** Lines taken from the stack */
    private Collection<String> stack; 
    
    /** The name of the class which caught the exception */
    private String catchingClassName;
    
    /**
     * Default constructor
     */
    public ExceptionRecord() {
    }
    
    /**
     * Unnest exception to get to base cause of the problem
     * @param t Exception to be un-nested
     * @return base reason
     */
    private Throwable unwindNested(Throwable t) {
        if (t instanceof BaseException) {
            BaseException c=(BaseException)t;
            return c.getTarget()!=null ? unwindNested(c.getTarget()) : c;
        }
        else if (t instanceof BaseError) {
            BaseError c=(BaseError)t;
            return c.getTarget()!=null ? unwindNested(c.getTarget()) : c;
        }
        else {
            return t;
        }
    }
    
    /**
     * Create a ExceptionRecord from a Throwable. Specifying a truncation point class allows
     * will cause the stack to be recorded only up to the last reference to a specified class, 
     * this is of particular use in JEE environments where the container's classes are of
     * little use in diagnosing a problem.
     * @param th Throwable to be represented by this record
     */
    public ExceptionRecord(Throwable th) {
        date = new Date();
        reason = th.toString();
        stack = new ArrayList<String>();
        
        // Set catching class to whoever called us
        StackTraceElement[] here=Thread.currentThread().getStackTrace();
        if (here!=null && here.length >= 1) {
            catchingClassName = here[2].getClassName();
        }
        
        StackTraceElement[] stackToDump=null;

        // We want the stack trace we record to be as relevant as possible, so when the
        // exception we've been given has a cause of some kind - i.e. wraps another
        // exception, we use that rather than the wrapping exception. 
        Throwable cause=unwindNested(th);
        stack.add(cause.toString());
        stackToDump=cause.getStackTrace();

        if (stackToDump==null) {
            stackToDump=th.getStackTrace();
        }

        for(StackTraceElement ste: stackToDump) {
            stack.add(ste.getClassName()+"."+ste.getMethodName()+"("+ste.getFileName()+":"+ste.getLineNumber()+")");
            if (stack.size() > MAX_FRAMES_TO_CAPTURE) {
                break;
            }
        }
    }

    /**
     * @return the exceptionDate
     */
    public Date getDate() {
        return date;
    }

    /**
     * @param exceptionDate the exceptionDate to set
     */
    public void setDate(Date date) {
        this.date = date;
    }

    /**
     * @return the reason
     */
    public String getReason() {
        return reason;
    }

    /**
     * @param reason the reason to set
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * @return the stack
     */
    public Collection<String> getStack() {
        return stack;
    }

    /**
     * @param stack the stack to set
     */
    public void setStack(Collection<String> stack) {
        this.stack = stack;
    }

    /**
     * @return the catchingClassName
     */
    public String getCatchingClassName() {
        return catchingClassName;
    }

    /**
     * @param catchingClassName the catchingClassName to set
     */
    public void setCatchingClassName(String catchingClassName) {
        this.catchingClassName = catchingClassName;
    }
}
