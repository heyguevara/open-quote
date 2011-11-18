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

package com.ail.insurance.acceptance;

import com.ail.core.PreconditionException;
import com.ail.financial.CurrencyAmount;
import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;

public class CollectPremiumService extends com.ail.core.Service<CollectPremiumArg> {
    private static final long serialVersionUID = 1871676649916485145L;

    @Override
    public void invoke() throws PreconditionException {
		Policy policy = args.getPolicyArg();
		// check arguments
		if(policy==null){
			throw new PreconditionException("policy==null");
		}
		
		PolicyStatus status = policy.getStatus();
		if(status==null){
			throw new PreconditionException("status==null");
		}

		if(!status.equals(PolicyStatus.ON_RISK)){
			throw new PreconditionException("!status.equals(PolicyStatus.OnRisk)");
		}

		// prepayment must not be negative		
		CurrencyAmount prePayment = args.getPrePaymentArg();
		if(prePayment!=null &&  prePayment.getAmount().doubleValue()<0){
			throw new PreconditionException("prePayment < 0");
		}

		// if no prepayment, collect using payment details		
		if(prePayment==null || prePayment.getAmount().doubleValue()==0){
			PaymentSchedule paymentSchedule = policy.getPaymentDetails();
			if(paymentSchedule==null){
				throw new PreconditionException("paymentMethodDetails==null");
			}			
			core.logInfo("Collect payment using payment details from: " + policy.getPolicyNumber());
		}

		core.logInfo("Allocate premium for policy: " + policy.getPolicyNumber());
		core.logInfo("Audit trail for collect premium on policy: " + policy.getPolicyNumber());
		core.logInfo("Diary for collect premium on policy: " + policy.getPolicyNumber());
 
    }
}


