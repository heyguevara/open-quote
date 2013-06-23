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

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.ExecutePageActionService;
import com.ail.pageflow.util.PageFlowContext;

@ServiceImplementation
public class CreateNewBusinessQuotationService extends Service<ExecutePageActionService.ExecutePageActionArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    @Override
    public void invoke() throws BaseException {

        if (args.getPortletRequestArg() == null) {
            throw new PreconditionException("args.getPortletRequestArg() == null");
        }

        if (PageFlowContext.getProductName() == null || PageFlowContext.getProductName().length() == 0) {
            throw new PreconditionException("PageFlowContext.getProductName() == null || PageFlowContext.getProductName().length() == 0");
        }

        if (PageFlowContext.getPageFlow() == null) {
            throw new PreconditionException("PageFlowContext.getPageFlow() == null");
        }

        if (getPolicyFromPageFlowContext() != null) {
            throw new PreconditionException("getPolicyFromPageFlowContext() != null");
        }

        // Create a new policy
        Policy policy = (Policy) PageFlowContext.getCoreProxy().newProductType(PageFlowContext.getProductName(), "Policy");

        // The productTypeID in the policy must match the product we're working for.
        policy.setProductTypeId(getProductNameFromPageFlowContext());
        
        // Put the policy into the PageFlowContext
        setPolicyToPageFlowContext(policy);
        
        // Put the policy into the command args
        args.setModelArgRet(policy);

        if (PageFlowContext.getPolicy() == null) {
            throw new PostconditionException("getPolicy() == null");
        }
    }
    
    // Wrapper to static PageFlowContext method call to help testability.
    protected Policy getPolicyFromPageFlowContext() {
        return PageFlowContext.getPolicy();
    }
    
    // Wrapper to static PageFlowContext method call to help testability.
    protected void setPolicyToPageFlowContext(Policy policyArg) {
        PageFlowContext.setPolicy(policyArg);
    }
    
    // Wrapper to static PageFlowContext method call to help testability.
    protected String getProductNameFromPageFlowContext() {
        return PageFlowContext.getProductName();
    }
}