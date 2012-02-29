/* Copyright Applied Industrial Logic Limited 2005. All rights reserved. */
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
package com.ail.core.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ail.core.CoreProxy;
import com.ail.core.configure.server.ServerDeligate;

/**
 * This servlet performs common operations on the core configure system in response to user requests.
 * Operations are specified using the "op" parameter, with additional parameters being passed as 
 * appropriate.
 * <p>
 * <u>Operations</u><br/>
 * <ul><li>Reset product: op=resetProduct&productName=&lt;path of product to reset&gt;<br/>
 * Resets the specified product and clears it's configuration cache.</li>
 * <li>Clear a product cache: op=clearProductCache&productName=&lt;path of product to reset&gt;<br/>
 * Clears the specified product cache.</li>
 * <li>Reset a configuration namespace: op=resetNamedConfiguration&namespace=&lt;namespace&gt;<br/>
 * Reset the named configuration namespace to the factory default settings.</li>
 * </ul>
 * </p>
 * @since 2.0
 */
public class ConfigureOperationServlet extends HttpServlet {

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String op=request.getParameter("op");
        response.setContentType("text/html");
        try {
            CoreProxy core=new CoreProxy();

            if ("resetProduct".equals(op)) {
                String productName=request.getParameter("productName");
                core.resetProduct(productName);
                core.clearProductCache(productName);
                core.logInfo("Registry reset for product: "+productName);
                response.getWriter().append("<html><body>Registry reset for product: "+productName+"</body></html>");
            }
            else if ("clearProductCache".equals(op)) {
                String productName=request.getParameter("productName");
                core.clearProductCache(productName);
                core.logInfo("Cache cleared for product: "+productName);
                response.getWriter().append("<html><body>Cache cleared for product: "+productName+"</body></html>");
            }
            else if ("resetNamedConfiguration".equals(op)) {
                String namespace=request.getParameter("namespace");
                new ServerDeligate(request.getUserPrincipal()).resetNamedConfiguration(namespace);
                core.setVersionEffectiveDateToNow();
                core.logInfo("Configuration reset for namespace: "+namespace);
                response.getWriter().append("<html><body>Reset configuration namespace: "+namespace+"</body></html>");
            }
        }
        catch(Exception e) {
            throw new ServletException("Configuration operation "+op+" failed with exception", e);
        }
    }
}
