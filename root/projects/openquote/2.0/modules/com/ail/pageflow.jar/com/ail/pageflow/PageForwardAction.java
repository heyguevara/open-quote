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
package com.ail.pageflow;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.policy.Policy;

import static com.ail.pageflow.ActionType.ON_PROCESS_ACTIONS;
/**
 * A page action than conditionally moves the context to a specified page. PageForwardActions may be nested inside
 * {@link CommandButtonAction CommandButtonActions} in order to override the CommandButtonAction's default
 * page destination if some condition is met.</br>
 * For exampled, it is common for the last page in a quotation page flow (immediately before the quote or
 * referral summary is shown) to include a CommandButtonAction directing the pageflow to the referral page, but
 * nest a PageForwardAction which will forward to the quote summary page if the quote is successful.
 * @see CommandButtonAction
 */
public class PageForwardAction extends Action {
    private static final long serialVersionUID = 7575333161831400599L;
    private String destinationPageId;

    public String getDestinationPageId() {
        return destinationPageId;
    }

    public void setDestinationPageId(String destinationPageId) {
        this.destinationPageId = destinationPageId;
    }

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
        return model;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        if (ON_PROCESS_ACTIONS.equals(getWhen()) && conditionIsMet(model)) {
            ((Policy)model).setPage(getDestinationPageId());
        }
        return model;
    }
}
