/* Copyright Applied Industrial Logic Limited 2007. All rights Reserved */
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

package com.ail.insurance.acceptance.acceptquotation;

import static com.ail.insurance.policy.PolicyStatus.QUOTATION;
import static com.ail.insurance.policy.PolicyStatus.SUBMITTED;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;

@ServiceImplementation
public class AcceptQuotationService extends Service<AcceptQuotationArgument> {
    private static final long serialVersionUID = 5492150960329684094L;

    /**
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    @Override
    public String getConfigurationNamespace() {
        return Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());
    }
    
    @Override
    public void invoke() throws PreconditionException, BaseException {
        // check preconditions
		if (args.getPolicyArgRet()==null){
			throw new PreconditionException("args.getPolicyArgRet()==null");
		}

        if (args.getPolicyArgRet().getStatus()==null){
            throw new PreconditionException("args.getPolicyArgRet().getStatus()==null");
        }

        if (!QUOTATION.equals(args.getPolicyArgRet().getStatus())){
            throw new PreconditionException("!QUOTATION.equals(args.getPolicyArgRet().getStatus())");
        }
		
        if (args.getPolicyArgRet().getPaymentDetails()==null) {
            throw new PreconditionException("args.getPolicyArgRet().getPaymentDetails()==null");
        }
        		
		// put on risk
		args.getPolicyArgRet().setStatus(SUBMITTED);
    }
}


