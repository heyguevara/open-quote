/* Copyright Applied Industrial Logic Limited 2005. All rights Reserved */
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
package com.ail.core.configure.server;

import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

/**
 * Web service endpoint for the configuration server. This endpoint simply
 * deligates to an instance of {@link com.ail.core.configure.server.ServerDeligate ServerDeligate} passing
 * on the XML String argument is was given along with the security principal that the caller supplied.
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/configure-server-ejb.jar/com/ail/core/configure/server/ServerEndpointBean.java,v $
 */
public class ServerEndpointBean implements SessionBean {
    private SessionContext sessionContext=null;
    private ServerDeligate serverDeligate=null;
    
    public void ejbCreate() {
    }
    
    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
        sessionContext=ctx;
    }

    private ServerDeligate getServerDeligate() {
        if (serverDeligate==null) {
            serverDeligate=new ServerDeligate(sessionContext.getCallerPrincipal());
        }
        
        return serverDeligate;
    }
    
    public String invokeServiceXML(String xml) {
        try {
            return getServerDeligate().invokeServiceXML(xml);
        }
        catch(Exception e) {
            // TODO better handling needed here!
            e.printStackTrace();
            return null;
        }
    }
}
