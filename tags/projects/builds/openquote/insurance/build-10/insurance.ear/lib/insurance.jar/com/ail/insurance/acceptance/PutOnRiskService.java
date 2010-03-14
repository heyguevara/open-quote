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

import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberCommand;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 19:49:05 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/PutOnRiskService.java,v $
 * @stereotype service
 */
public class PutOnRiskService extends com.ail.core.Service {
    private static final long serialVersionUID = 5492150960329684094L;
    private PutOnRiskArg args = null;
    private com.ail.core.Core core = null;

    /** Default constructor */
    public PutOnRiskService() {
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
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    public String getConfigurationNamespace() {
        return args.getPolicyArgRet().getProductTypeId();
    }
    
    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public com.ail.core.Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2006/01/15 19:49:05 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/PutOnRiskService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.3 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(com.ail.core.command.CommandArg args) {
        this.args = (PutOnRiskArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of PutOnRiskArgs.
     */
    public com.ail.core.command.CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, BaseException {
		Policy policy = args.getPolicyArgRet();
		// check arguments
		if(policy==null){
			throw new PreconditionException("policy==null");
		}
		PolicyStatus status = policy.getStatus();
		if(status==null){
			throw new PreconditionException("status==null");
		}
		if(!status.equals(PolicyStatus.QUOTATION)){
			throw new PreconditionException("!status.equals(PolicyStatus.Quotation)");
		}
		
		// clear out any existing number
		policy.setPolicyNumber(null);

		// run command to add new number
		AddPolicyNumberCommand command = (AddPolicyNumberCommand) getCore().newCommand("AddPolicyNumber");
		command.setPolicyArgRet(policy);
		command.invoke();
		policy = command.getPolicyArgRet();

		// confirm payment details
		if(policy.getPaymentDetails()!=null){
			core.logInfo("Found Payment Details for: " + policy.getPolicyNumber());
		}
		else{
			core.logInfo("No Payment Details for: " + policy.getPolicyNumber());
		}
		
		// Customer ledger
		core.logInfo("If required, create customer ledger for: " + policy.getPolicyNumber());
		
		// payment schedule
		core.logInfo("Create payment schedule for: " + policy.getPolicyNumber());
		
		// put on risk
		policy.setStatus(PolicyStatus.ON_RISK);
		
		// post to account
		core.logInfo("Create post to account for: " + policy.getPolicyNumber());
    }
}


