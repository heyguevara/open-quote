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

package com.ail.insurance.quotation.addquotenumber;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.key.GenerateUniqueKeyCommand;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;

/**
 * This service generates quotation numbers and adds them to policy objects.
 */
@ServiceImplementation
public class AddQuoteNumberService extends Service<AddQuoteNumberArgument> {
    private static final long serialVersionUID = 6143065395162584693L;
    private String configurationNamespace="com.ail.insurance.quotation.addquotenumber.AddQuoteNumberService";
    
    @Override
    public String getConfigurationNamespace() {
        return configurationNamespace;
    }
    
    @Override
    public void invoke() throws PreconditionException, BaseException {
        
        // Select this service's configuration.
        configurationNamespace="com.ail.insurance.quotation.addquotenumber.AddQuoteNumberService";
        
        if (args.getPolicyArgRet()==null) {
            throw new PreconditionException("args.getPolicyArgRet()==null");
        }

        Policy policy=args.getPolicyArgRet();

        if (policy.getStatus()==null) {
            throw new PreconditionException("policy.getStatus()==null");
        }

        if (!policy.getStatus().equals(PolicyStatus.APPLICATION)) {
            throw new PreconditionException("policy.getStatus()!=PolicyStatus.Application");
        }

        if (policy.getQuotationNumber()!=null && policy.getQuotationNumber().trim().length()!=0) {
            throw new PreconditionException("policy.getQuotationNumber()!=null && policy.getQuotationNumber().trim().length()!=0");
        }

        if (policy.getProductTypeId()==null) {
            throw new PreconditionException("policy.getProductTypeId()==null");
        }

        // Switch to the product's configuration so we pick up the product specific rules.
        configurationNamespace=Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());

        GenerateUniqueKeyCommand gukc=getCore().newCommand(GenerateUniqueKeyCommand.class);
        gukc.setKeyIdArg("QuoteNumber");
        gukc.setProductTypeIdArg(policy.getProductTypeId());
        gukc.invoke();
        
        // Invoke the product specific quote number gen command 
        GenerateQuoteNumberRuleCommand command=core.newCommand("GenerateQuoteNumberRule", GenerateQuoteNumberRuleCommand.class);
        command.setPolicyArg(policy);
        command.setUniqueNumberArg(gukc.getKeyRet());
        command.invoke();
        String quoteNumber=command.getQuoteNumberRet();
        core.logDebug("Quote number: "+quoteNumber+" generated");
        args.getPolicyArgRet().setQuotationNumber(quoteNumber);
    }
}
