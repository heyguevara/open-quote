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
import org.hibernate.SessionFactory;
import org.hibernate.StaleObjectStateException;
import org.hibernate.classic.Session;
import org.hibernate.context.ManagedSessionContext;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.core.persistence.CloseSessionArg;
import com.ail.core.persistence.UpdateException;

/**
 * Implementation of the close session service for Hibernate
 */
public class HibernateCloseSessionService extends Service {
    private CloseSessionArg args = null;

    private Core core;

    /** Default constructor */
    public HibernateCloseSessionService() {
        super();
        core = new Core(this);
    }

    /**
     * Getter to fetch the entry point's core. This method is demanded by the EntryPoint class.
     * 
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * 
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) getCore().newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2006/08/20 15:03:54 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/hibernate/HibernateCloseSessionService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.2 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * 
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (CloseSessionArg) args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * 
     * @return An instance of CloseSessionArg.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** 
     * The 'business logic' of the entry point. Note that we do not actually
     * close close the hibernate session here - which is somewhat counterintuitive.
     * Instead we are making the assumption that Hibernate is configured to use
     * either "thread" or "jta" as it's "current_session_context_class", and as 
     * such the session will be closed for us.
     * */
    public void invoke() throws PreconditionException, UpdateException {
        SessionFactory factory=null;
        Session session=null;
        
        try {
            factory=HibernateFunctions.getSessionFactory();
            session=factory.getCurrentSession();
            session.getTransaction().commit();
        }
        catch (StaleObjectStateException e) {
            throw new UpdateException(e.toString(), e);
        }
        catch (HibernateException e) {
            throw new UpdateException(e.toString(), e);
        }
        finally {
            try {
                session.close();
            }
            catch (StaleObjectStateException e) {
                throw new UpdateException(e.toString(), e);
            }
            finally {
                ManagedSessionContext.unbind(factory);
            }
        }
    }
}
