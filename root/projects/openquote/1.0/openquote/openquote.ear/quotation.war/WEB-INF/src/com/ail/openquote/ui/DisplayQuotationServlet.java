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
package com.ail.openquote.ui;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DisplayQuotationServlet extends HttpServlet {
    
    private static final long serialVersionUID = 6984589565187737714L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=\"quotation.pdf\"");
        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private");

        byte[] doc=(byte[])request.getSession().getAttribute("javax.portlet.p.MotorPlusQuotationPortletInstance?quotedocument");
        response.getOutputStream().write(doc);
        response.getOutputStream().flush();
    }
}
