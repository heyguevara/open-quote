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

package com.ail.insurance.subrogation;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.EJBComponent;
import com.ail.insurance.subrogation.MakeARecoveryService.MakeARecoveryArgument;

@Configurable
@Stateless
@Remote(Subrogation.class)
public class SubrogationBean extends EJBComponent implements SubrogationLocal {
    private static final long serialVersionUID = 6506879017396687519L;
    private static final String NAMESPACE="com.ail.insurance.subrogation.SubrogationBean";
    private SessionContext ctx=null;
    
    public SubrogationBean() {
        initialise(NAMESPACE);
    }

    @Resource
    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    @PostConstruct
    public void postConstruct() {
        initialise(NAMESPACE);
	}

    public MakeARecoveryArgument makeARecovery(MakeARecoveryArgument arg) {
        return invokeCommand("MakeARecovery", arg);
    }
}
