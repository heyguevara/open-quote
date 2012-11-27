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

package com.ail.core.urlhandler.product;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import com.ail.core.Functions;
import com.ail.core.ThreadLocale;

/**
 * The handler deals with URLs of the form: "product://". All this class does is to transparently transform a "product://" URL into 
 * a reference to a resource in the product repository. The reference is always language sensitive in that the 
 * language associated with the current thread's {@link com.ail.core.ThreadLocale} is always passed as a parameter in the URL. The repository
 * therefore has the option of returning language specific variants if it supports them.
 * <p/>
 * This handler is specifically implemented to work with alfresco. It also assumes that access to Product resources
 * is restricted and requires authentication, and will authenticate requests automatically by first retrieving an alfresco ticket.
 * <p/>
 * For example, a URL like: <code>product://localhost:8080/Demo/Demo/Welcome.html</code> is translated into a call
 * to <code>http://localhost:8080/alfresco/download/direct?path=/Company%20Home/Product/Demo/Demo/Welcome.html?language=en?ticket=<i>ticket</i></code>.
 * <p/>
 */
public class Handler extends URLStreamHandler {
    private static final long TICKET_TIMEOUT=1000*60*50; // timeout tickets every 50 mins - alfresco's session timeout is 60 mins (see alfreso's WEB-INF/web.xml session-timeout).
    
    private static StringBuffer ticket=new StringBuffer();
    private static long ticketExpiryTime=0;
    
    private String getTicket(URL url) throws IOException {
        if (ticket.length()==0 || System.currentTimeMillis() > ticketExpiryTime) {
            synchronized (ticket) {
                // TODO user and password _have_ to be moved out of here!!
                URL loginUrl=new URL("http", url.getHost(), url.getPort(), "/alfresco/service/api/login?u=productreader&pw=preader");
                String rawTicketResponse=Functions.loadUrlContentAsString(loginUrl);        

                ticket.delete(0, ticket.length());
                ticket.append(rawTicketResponse.substring(rawTicketResponse.indexOf("<ticket>")+8, rawTicketResponse.lastIndexOf("<")));

                ticketExpiryTime=System.currentTimeMillis()+TICKET_TIMEOUT;

                // Workaround for a bug in Alfresco 2.9.0 (C_dev 816) schema 124. In this version it appears that
                // authentication of any content request depends on the alfresco web-client having a fully initialized
                // JSF context, which it will only have when the /alfresco page has been opened. Attempting to make
                // content requests otherwise results in an NPE in alfresco, and an IOException to the client
                // We workaround that here by reading from /alfresco here, once, during ticket initialization. 
                new URL("http", url.getHost(), url.getPort(), "/alfresco").openConnection().getContent();
            } 
        }
        
        return ticket.toString();
    }
    
    protected URLConnection openConnection(URL u) throws IOException {
        String language="";
        
        // Respect the language associated with the current thread if the URL doesn't already include one.
        if (!u.getPath().contains("&language=")) {
            language="&language="+ThreadLocale.getThreadLocale().getLanguage();
        }

        URL actualURL = new URL("http", u.getHost(), u.getPort(), "/alfresco/download/direct?path=/Company%20Home/Product"+u.getPath()+"&ticket="+getTicket(u)+language);
        
        return actualURL.openConnection();
    }
}
