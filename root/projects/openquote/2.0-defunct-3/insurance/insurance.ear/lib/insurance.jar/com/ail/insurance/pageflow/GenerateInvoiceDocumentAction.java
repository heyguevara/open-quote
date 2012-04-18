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
package com.ail.insurance.pageflow;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.onrisk.FetchInvoiceService.FetchInvoiceCommand;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.pageflow.util.QuotationContext;

/**
 * PageFlow action to generate the invoice document for the current quotation.
 * The document will be generated and persisted with the quotation.
 */
public class GenerateInvoiceDocumentAction extends Action {
    private static final long serialVersionUID = 7575333161831400599L;

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        // do nothing
    	return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return false;
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // do nothing
    	return model;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        if (conditionIsMet(model)) {
            try {
                CoreProxy proxy=new CoreProxy();
                
                Policy quote=(Policy)model;
                
                // This will force the quote do to be generated if it hasn't been already, and will do nothing otherwise.
                FetchInvoiceCommand cmd=proxy.newCommand(FetchInvoiceCommand.class);
                cmd.setPolicyNumberArg(quote.getPolicyNumber());
                cmd.invoke();
                
                // If we are processing a quotation (i.e. we're not on listing quotations, we're processing a quote)
                if (model instanceof Policy) {
                	// ...assume that we have just updated the persisted quote and keep the session in step
	                SavedPolicy savedPolicy=(SavedPolicy)proxy.queryUnique("get.savedPolicy.by.quotationNumber", quote.getQuotationNumber());
	                QuotationContext.setPolicy(savedPolicy.getPolicy());
                }
            }
            catch(Exception e) {
                throw new RenderingError("Failed to generate/display quote", e);
            }
        }
        
        return model;
    }
}
