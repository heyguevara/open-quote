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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.onrisk.wording.FetchWordingCommand;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * <p>Adds a "view wording" button to a page. When selected this button will open a new window containing
 * the current wording as a PDF.</p>
 * <p><img src="doc-files/ViewWordingButtonAction.png"/></p>
 * <p>If the PDF does not already exist, this button's action will create it and persist it before
 * returning to the client's browser.</p>
 * <p>The button forward to /quotation/DisplayWordingServlet to download the PDF to the client. This
 * URL is mapped to {@link com.ail.openquote.ui.DisplayWordingServlet}.
 */
public class ViewWordingButtonAction extends CommandButtonAction {
    private static final long serialVersionUID = 7575333161831400599L;
    
    public ViewWordingButtonAction() {
        setLabel("Document");
    }
    
    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        String op=Functions.getOperationParameters(request).getProperty("op");

        if ("view-wording".equals(op)) {
            try {
                CoreProxy proxy=new CoreProxy();

                String policyNumber=Functions.getOperationParameters(request).getProperty("id");

                // This will force the wording do to be generated if it hasn't been already, and will do nothing otherwise.
                FetchWordingCommand cmd=(FetchWordingCommand)proxy.newCommand("FetchWordingDocument");
                cmd.setPolicyNumberArg(policyNumber);
                cmd.invoke();
                
                // If we are processing a quotation (i.e. we're not on listing quotations, we're processing a quote)
                if (model instanceof Quotation) {
                	// ...assume that we have just updated the persisted quote and keep the session in step
	                SavedQuotation savedQuotation=(SavedQuotation)proxy.queryUnique("get.savedPolicy.by.policyNumber", policyNumber);
	                QuotationContext.setQuotation(savedQuotation.getQuotation());
                }
                
                response.sendRedirect("/quotation/DisplayWordingServlet?policyNumber="+policyNumber);
            }
            catch(Exception e) {
                throw new RenderingError("Failed to generate/display wording", e);
            }
        }
        
        return model;
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    	return executeTemplateCommand("ViewWordingButtonCommand", request, response, model);
    }
}
