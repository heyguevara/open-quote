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

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/04/24 21:07:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/CollectPremiumService.java,v $
 * @stereotype service
 */
public class CollectPremiumService extends com.ail.core.Service {
    private static final long serialVersionUID = 1871676649916485145L;
    private CollectPremiumArg args = null;
    private com.ail.core.Core core = null;

    /** Default constructor */
    public CollectPremiumService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public com.ail.core.Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public com.ail.core.Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2006/04/24 21:07:59 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/CollectPremiumService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.3 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(com.ail.core.command.CommandArg args) {
        this.args = (CollectPremiumArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of CollectPremiumArgs.
     */
    public com.ail.core.command.CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
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


