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

package com.ail.core.persistence.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.classic.Session;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.CommandArg;
import com.ail.core.persistence.CreateArg;
import com.ail.core.persistence.CreateException;

/**
 * Implementation of the create service for Hibernate
 */
public class HibernateCreateService extends Service {
    private CreateArg args = null;
 
    /** Default constructor */
    public HibernateCreateService() {
        super();
    }

    /**
     * Getter to fetch the entry point's core. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return null;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (CreateArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of CreateArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, CreateException {

        // check arguments
	    if(args.getObjectArg()==null){
	        throw new PreconditionException("args.getObjectArg()==null");
	    }
        
        if (args.getObjectArg().getSerialVersion()!=0) {
            throw new PreconditionException("args.getObjectArg().getSerialVersion()!=0");
        }

        Session session=null;
        
        // create record
		try {
            session = HibernateFunctions.getSessionFactory().getCurrentSession(); 
            session.save(args.getObjectArg());
		} 
        catch (HibernateException e) {
			throw new CreateException("Hibernate could not save the object to the database",e);
		}
        catch(Throwable t) {
            t.printStackTrace();
        }
    }
}


