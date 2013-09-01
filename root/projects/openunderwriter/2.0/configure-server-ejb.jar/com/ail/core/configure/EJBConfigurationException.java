/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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
package com.ail.core.configure;

import com.ail.core.BaseException;
import com.ail.core.BaseError;

import javax.ejb.EJBException;

/**
 * This exception is throw by the EJBLoader in reponse to any problems that it encounters.
 * Its purpose is simply to wrap the exception/error such that it cause roll backs, and
 * give the client side code access to the actual exception/error.
 */
public class EJBConfigurationException extends EJBException {
    private Throwable serverSideCause=null;

    /**
     * Constructor to create an EJBConfigurationException to wrap a
     * BaseError.
     * @param e BaseError
     */
    public EJBConfigurationException(BaseError e) {
        super(e.toString());
        serverSideCause=e;
    }

    /**
     * Consstructor to create an EJBConfigurationException to wrap a
     * BaseException.
     * @param e BaseException
     */
    public EJBConfigurationException(BaseException e) {
        super(e);
        serverSideCause=e;
    }

    /**
     * Constructor.
     * @param message Message
     * @param cause causing exception
     */
    public EJBConfigurationException(String message, Throwable cause) {
        super(message);
        this.serverSideCause=cause;
    }

    /**
     * Get the exception/error that cause the problem.
     * @return
     */
    public Throwable getServerSideCause() {
        return serverSideCause;
    }

    /**
     * Return the cause if it was a BaseError, otherwise return null.
     * @return Cause, or null.
     */
    public BaseError getBaseErrorCause() {
        if (serverSideCause instanceof BaseError) {
            return (BaseError)serverSideCause;
        }
        return null;
    }

    /**
     * Return the cause if it was a BaseException, otherwise return null.
     * @return Cause, or null.
     */
    public BaseException getBaseExceptionCause() {
        if (serverSideCause instanceof BaseException) {
            return (BaseException)serverSideCause;
        }
        return null;
    }

    public String toString() {
        return serverSideCause.toString();
    }
}
