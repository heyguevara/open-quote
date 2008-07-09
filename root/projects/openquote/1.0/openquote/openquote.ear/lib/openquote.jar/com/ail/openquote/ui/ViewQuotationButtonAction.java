/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotationSummary;
import com.ail.openquote.ui.util.Functions;

/**
 * <p>Adds a "view quotation" button to a page. When selected this button will open a new window containing
 * the current quotation as a PDF.</p>
 * <p><img src="doc-files/ViewQuotationButtonAction.png"/></p>
 * <p>If the PDF does not already exist, this button's action will create it and persist it before
 * returning to the client's browser.</p>
 * <p>The button forward to /quotation/DisplayQuotationServlet to download the PDF to the client. This
 * URL is mapped to {@link com.ail.openquote.ui.DisplayQuotationServlet}.
 */
public class ViewQuotationButtonAction extends CommandButtonAction {
    private static final long serialVersionUID = 7575333161831400599L;
    
    public ViewQuotationButtonAction() {
        setLabel("View");
    }
    
    @Override
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        String op=Functions.getOperationParameters(request).getProperty("op");

        if (op!=null && op.equals(getLabel())) {
            try {
                String quoteNumber=Functions.getOperationParameters(request).getProperty("id");
                response.sendRedirect("/quotation/DisplayQuotationServlet?quoteNumber="+quoteNumber);
            }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();

        // Not too nice this: we want to use this button from the search results screen and from the quote summary screen,
        // but they have different 'models'. The search result screen has SavedQuotationSummary and the quote summary
        // screen has a Quotation. We either have two different implementations of this button (and the mappings/classes/etc)
        // to match, or we have the nasty 'instanceof' below.
        String quoteNumber=(model instanceof SavedQuotationSummary) ? ((SavedQuotationSummary)model).getQuotationNumber() 
                                                                    : ((Quotation)model).getQuotationNumber();

        w.printf("<input type='submit' name='op=%1$s:id=%2$s' value='%1$s' class='portlet-form-input-field'/>", getLabel(), quoteNumber);
    }
}
