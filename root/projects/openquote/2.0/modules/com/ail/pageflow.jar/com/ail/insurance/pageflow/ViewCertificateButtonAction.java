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
import com.ail.insurance.onrisk.FetchCertificateService.FetchCertificateCommand;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.pageflow.util.Functions;
import com.ail.insurance.pageflow.util.PageflowContext;

/**
 * <p>Adds a "view certificate" button to a page. When selected this button will open a new window containing
 * the current certificate as a PDF.</p>
 * <p><img src="doc-files/ViewCertificateButtonAction.png"/></p>
 * <p>If the PDF does not already exist, this button's action will create it and persist it before
 * returning to the client's browser.</p>
 * <p>The button forward to /quotation/DisplayCertificateServlet to download the PDF to the client. This
 * URL is mapped to {@link com.ail.insurance.pageflow.DisplayCertificateServlet}.
 */
public class ViewCertificateButtonAction extends CommandButtonAction {
    private static final long serialVersionUID = 7575333161831400599L;
    
    public ViewCertificateButtonAction() {
        setLabel("Document");
    }
    
    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        String op=Functions.getOperationParameters(request).getProperty("op");

        if ("view-certificate".equals(op)) {
            try {
                CoreProxy proxy=new CoreProxy();

                String policyNumber=Functions.getOperationParameters(request).getProperty("id");

                // This will force the certificate do to be generated if it hasn't been already, and will do nothing otherwise.
                FetchCertificateCommand cmd=proxy.newCommand(FetchCertificateCommand.class);
                cmd.setPolicyNumberArg(policyNumber);
                cmd.invoke();
                
                // If we are processing a quotation (i.e. we're not on listing quotations, we're processing a quote)
                if (model instanceof Policy) {
                	// ...assume that we have just updated the persisted quote and keep the session in step
	                SavedPolicy savedPolicy=(SavedPolicy)proxy.queryUnique("get.savedPolicy.by.policyNumber", policyNumber);
	                PageflowContext.setPolicy(savedPolicy.getPolicy());
                }
                
                response.sendRedirect("/quotation-portlet/DisplayCertificateServlet?policyNumber="+policyNumber);
            }
            catch(Exception e) {
                throw new RenderingError("Failed to generate/display certificate", e);
            }
        }
        
        return model;
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    	return executeTemplateCommand("ViewCertificateButtonCommand", request, response, model);
    }
}
