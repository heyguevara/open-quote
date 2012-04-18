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

package com.ail.insurance.quotation;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;

/**
 * Service to notify a party of the existence of a quote. Typical implementations of this service include
 * notifying proposers, brokers and/or carriers of the creation of a new quotation. The implementation might
 * send notification by a number of channels including emails and web service calls.</p>
 * The caller may specify the quotation either by quote number or by the instance of the quote itself.
 */
@ServiceImplementation
public class NotifyPartyService extends Service<NotifyPartyService.NotifyPartyArgument> {

    @ServiceArgument
    public interface NotifyPartyArgument extends Argument {
        String getQuotationNumberArg();
        
        void setQuotationNumberArg(String quotationNumberArg);
        
        Policy getPolicyArg();
        
        void setPolicyArg(Policy policyArg);
    }
    
    @ServiceCommand
    public interface NotifyPartyCommand extends Command, NotifyPartyArgument {
    }

    private static final long serialVersionUID = -4915889686192216902L;
    private String configurationNamespace = null;
    
    /**
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    public String getConfigurationNamespace() {
        return configurationNamespace;
    }
        
    private void setConfigurationNamespace(String configurationNamespace) {
        this.configurationNamespace=configurationNamespace;
    }
    
    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, BaseException {
        Policy policy=null;
        
        // Fail if we've been given neither a quote number or a policy
        if (args.getQuotationNumberArg()==null && args.getPolicyArg()==null) {
            throw new PreconditionException("args.getQuotationNumberArg()==null && args.getPolicyArg()==null");
        }

        // Fail if we've been given both a quote number or a policy
        if (args.getQuotationNumberArg()!=null && args.getPolicyArg()!=null) {
            throw new PreconditionException("args.getQuotationNumberArg()!=null && args.getPolicyArg()!=null)");
        }

        if (args.getQuotationNumberArg()!=null) {
            if (args.getQuotationNumberArg().length()==0) {
                throw new PreconditionException("args.getQuotationNumberArg().length()==0");
            }

            SavedPolicy savedPolicy=(SavedPolicy)getCore().query("get.savedPolicy.by.quotationNumber", args.getQuotationNumberArg());

            policy=savedPolicy.getPolicy();
        }
        else {
            policy=(Policy)args.getPolicyArg();
        }
        
        String namespace=Functions.productNameToConfigurationNamespace(policy.getProductTypeId());
        setConfigurationNamespace(namespace);
        
        NotifyPartyCommand command=(NotifyPartyCommand)core.newCommand(NotifyPartyCommand.class);
        
        command.setPolicyArg(policy);

        command.invoke();
    }
}
