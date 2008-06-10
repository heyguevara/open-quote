/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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
import java.io.PrintWriter;
import java.io.PrintStream;

/**
 * This Exception class represents the base of all bob exceptions; which
 * indicate conditions within bob that applications might want to
 * catch.
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2007/03/20 22:18:39 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/BaseException.java,v $
 **/
public abstract class BaseException extends Exception {
    private ArrayList<String> errors = null;
    private Throwable target = null;

    /**
     * Constructor
     * @param description A description of the error.
     **/
    public BaseException(String description) {
        super(description);
    }

    /**
     * Constructor
     * @param description A description of the error.
     * @param target The exception that cause this exception to be thrown.
     **/
    public BaseException(String description, Throwable target) {
        super(description);
        this.target = target;
    }

    /**
     * Constructor
     * Build a BaseException from a BaseError.
     * @param e BaseError to be converted.
     **/
    public BaseException(BaseError e) {
        super(e.getDescription());
        this.target = e;
    }

    /**
     * Get the error description. This description is a textual
     * description of the error.
     * @return description
     **/
    public String getDescription() {
        return super.getMessage();
    }

    /**
     * Add an error to the error list.
     * A BaseException may list more that one 'sub-error'.
     * @param s Error message
     **/
    public void addError(String s) {
        if (errors == null) {
            errors = new ArrayList<String>();
        }

        errors.add(s);
    }

    /**
     * Convert this exception into a string
     * @return String representation of the exception.
     **/
    public String toString() {
        String ret = super.toString();

        if (errors != null) {
            for(String s: errors) {
                ret += "\n" + s;
            }
        }

        return ret;
    }

    public void printStackTrace() {
        if (target != null) {
            target.printStackTrace();
        }
        else {
            super.printStackTrace();
        }
    }

    public void printStackTrace(PrintStream stream) {
        if (target != null) {
            target.printStackTrace(stream);
        }
        else {
            super.printStackTrace(stream);
        }
    }

    public void printStackTrace(PrintWriter writer) {
        if (target != null) {
            target.printStackTrace(writer);
        }
        else {
            super.printStackTrace(writer);
        }
    }
}
