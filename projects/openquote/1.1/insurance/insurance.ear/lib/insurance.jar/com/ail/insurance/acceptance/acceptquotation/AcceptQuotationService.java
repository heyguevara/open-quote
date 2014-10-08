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

import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype service
 */
public class AcceptQuotationService extends com.ail.core.Service {
    private static final long serialVersionUID = 5492150960329684094L;
    private AcceptQuotationArg args = null;
    private com.ail.core.Core core = null;

    /** Default constructor */
    public AcceptQuotationService() {
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
        return Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());
    }
    
    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public com.ail.core.Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setAuthor("$Author$");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date$");
        v.setSource("$Source$");
        v.setState("$State$");
        v.setVersion("$Revision$");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(com.ail.core.command.CommandArg args) {
        this.args = (AcceptQuotationArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of AcceptQuotationArgs.
     */
    public com.ail.core.command.CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
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


