/* Copyright Applied Industrial Logic Limited 2003. All rights reserved. */
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

import java.net.URL;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Hibernate functions
 * 
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source:
 *         /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/hibernate/HibernateFunctions.java,v $
 */
public class HibernateFunctions {
    private static final SessionFactory sessionFactory;
    static {
        try {
            // Create the SessionFactory from hibernate.cfg.xml
            URL config=HibernateFunctions.class.getResource("HibernateConfiguration.xml");
            sessionFactory = new Configuration().configure(config).buildSessionFactory();
        }
        catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
    }

    public static SessionFactory getSessionFactory() {
        return sessionFactory;
    }
}
