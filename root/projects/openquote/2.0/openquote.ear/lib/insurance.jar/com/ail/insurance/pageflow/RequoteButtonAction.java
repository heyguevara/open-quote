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
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.pageflow.util.Functions;
import com.ail.insurance.pageflow.util.QuotationContext;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.policy.Section;

/**
 * <p>Adds a requote button to a page. By default this button will redirect the user
 * to the "ProposerDetails" screen, and is labeled as "Requote". On selection the quotation's
 * status is set to @{link com.ail.insurance.policy.PolicyStatus.APPLICATION}; 
 * it's quote number is removed; and it is marked as not having been saved. This 
 * has the effect of making this a new quote without any association to the original
 * quote.</p> 
 * <p><img src="doc-files/RequoteButtonAction.png"/></p>
 * <p>Both the label and destination page settings can be overridden by setting the 
 * {@link #getDestinationPageId() destinationPageId} and {@link #getLabel() label} properties.</p>
 */
public class RequoteButtonAction extends CommandButtonAction {
    private static final long serialVersionUID = 7575333161831400599L;
    
    /**
     * Default constructor
     */
    public RequoteButtonAction() {
        setLabel("i18n_requote_button_label");
        setDestinationPageId("ProposerDetails");
    }
    
    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        String op=Functions.getOperationParameters(request).getProperty("op");
        if ("requote".equals(op)) {
            Policy quote=(Policy)model;
            quote.setPage(getDestinationPageId());
            quote.setStatus(PolicyStatus.APPLICATION);
            quote.setQuotationNumber(null);
            quote.getAssessmentSheet().getAssessmentLine().clear();
            for(Section section: quote.getSection()) {
            	section.getAssessmentSheet().getAssessmentLine().clear();
            }
            quote.setUserSaved(false);
            quote.markAsNotPersisted();
            super.processActions(request, response, model);
        }
        return model;
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        return QuotationContext.getRenderer().renderRequoteButtonAction(w, request, response, model, this, i18n(getLabel()));
    }
}
