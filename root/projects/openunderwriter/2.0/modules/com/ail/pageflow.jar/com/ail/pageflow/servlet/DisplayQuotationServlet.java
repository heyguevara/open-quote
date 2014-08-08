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
package com.ail.pageflow.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.insurance.quotation.FetchQuoteDocumentService.FetchQuoteCommand;

public class DisplayQuotationServlet extends HttpServlet {
    
    private static final long serialVersionUID = 6984589565187737714L;

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String quoteNumber=request.getParameter("quoteNumber");
        
        response.setContentType("application/x-download");
        response.setHeader("Content-Disposition", "attachment;filename=\"Quotation-"+quoteNumber+".pdf\"");
        response.setHeader("Pragma", "private");
        response.setHeader("Cache-Control", "private");

        CoreProxy proxy=new CoreProxy();
        FetchQuoteCommand cmd=proxy.newCommand(FetchQuoteCommand.class);
        cmd.setQuotationNumberArg(quoteNumber);

        try {
            cmd.invoke();
        }
        catch(BaseException e) {
            e.printStackTrace();
            throw new ServletException("Failed to fetch quote (number:"+quoteNumber+") for display.");
        }
                        
        byte[] doc=cmd.getDocumentRet();
        response.getOutputStream().write(doc);
        response.getOutputStream().flush();
    }
}
