/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

package com.ail.pageflow.service;

import static com.ail.pageflow.PageFlowContext.getPageFlow;
import static com.ail.pageflow.PageFlowContext.getPolicy;
import static com.ail.pageflow.PageFlowContext.getProductName;
import static com.ail.pageflow.PageFlowContext.setPolicy;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.ThreadLocale;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.ExecutePageActionService;

/**
 * Fetch the policy associated with this PageFlow if there is one. If there is a
 * policy associated with the PageFlow, this service will fetch it and put it
 * into the PageFlow's context. If there is no policy associated, the service
 * has no effect.
 */
@ServiceImplementation
public class FetchPolicyForPageFlowService extends Service<ExecutePageActionService.ExecutePageActionArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    @Override
    public void invoke() throws BaseException {

        if (args.getPortletSessionArg() == null) {
            throw new PreconditionException("args.getPortletSessionArg() == null");
        }

        if (args.getPortletRequestArg() == null) {
            throw new PreconditionException("args.getPortletRequestArg() == null");
        }

        // If the session already has a policy object in it, then use it.
        if (getPolicy() != null) {
            Policy policy = getPolicy();

            // The request's ThreadLocale could change from one request to the
            // next,if the user switches their browser settings for example, so
            // always use the current settings.
            policy.setLocale(new ThreadLocale(args.getPortletRequestArg().getLocale()));

            // If we have a policy, then having a product name becomes a
            // precondition
            if (getProductName() == null || getProductName().length() == 0) {
                throw new PreconditionException("getProductName() == null || getProductName().length() == 0");
            }

            // Make sure that the product name is correctly populated in the
            // policy as we can't necessarily rely on the policy's type
            // definition to do this.
            policy.setProductTypeId(getProductName());

            // Add the policy to the PageFlowContext
            setPolicy(policy);
            
            getPageFlow().setCurrentPage(policy.getPage());
            
            // Add the policy to the command argument
            args.setModelArgRet(policy);
        }
    }
}