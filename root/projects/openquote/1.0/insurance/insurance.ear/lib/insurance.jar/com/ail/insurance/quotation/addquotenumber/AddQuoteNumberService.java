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

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationError;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;

/**
 * This service generates quotation numbers and adds them to policy objects.
 * @version $Revision: 1.7 $
 * @state $State: Exp $
 * @date $Date: 2007/06/10 11:05:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addquotenumber/AddQuoteNumberService.java,v $
 * @stereotype service
 */
public class AddQuoteNumberService extends Service {
    private static final long serialVersionUID = 6143065395162584693L;
    private AddQuoteNumberArg args = null;
    private Core core = null;
    private UniqueNumberHandler uniqueNumberHandler;
    private String configurationNamespace="com.ail.insurance.quotation.addquotenumber.AddQuoteNumberService";
    
    /** Default constructor */
    public AddQuoteNumberService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2007/06/10 11:05:59 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addquotenumber/AddQuoteNumberService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.7 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (AddQuoteNumberArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of AddQuoteNumberArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    public String getConfigurationNamespace() {
        return configurationNamespace;
    }
    
    /** The 'business logic' of the entry point. */
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

        int uniqueNumber=getUniqueNumberHander().getNextNumber(policy, this);
        
        // Switch to the product's configuration so we pick up the product specific rules.
        configurationNamespace=Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());

        
        // Invoke the product specific quote number gen command 
        GenerateQuoteNumberRuleCommand command=(GenerateQuoteNumberRuleCommand)core.newCommand("GenerateQuoteNumberRule");
        command.setPolicyArg(policy);
        command.setUniqueNumberArg(uniqueNumber);
        command.invoke();
        String quoteNumber=command.getQuoteNumberRet();
        core.logDebug("Quote number: "+quoteNumber+" generated");
        args.getPolicyArgRet().setQuotationNumber(quoteNumber);
    }

    private UniqueNumberHandler getUniqueNumberHander() {
        if (uniqueNumberHandler==null) {
            // get the name of the handler defined in configuration.
            String handlerName=core.getParameter("UniqueQuotationNumberHandler").getValue();

            if ("ConfigurePropertyUniqueNumberHandler".equals(handlerName)) {
                uniqueNumberHandler=new ConfigurePropertyUniqueNumberHandler();
            }
            else if ("PolicyIdUniqueNumberGeneratorHandler".equals(handlerName)) {
                uniqueNumberHandler=new PolicyIdUniqueNumberGeneratorHandler();
            }
            else {
                if (handlerName==null || handlerName.length()==0) {
                    throw new ConfigurationError("The parameter UniqueQuotationNumberHandler is undefined.");
                }
                else {
                    throw new ConfigurationError("The UniqueQuotationNumberHandler '"+handlerName+"' is not recognized.");
                }
            }
        }
        return uniqueNumberHandler;
    }
}

interface UniqueNumberHandler {
    public int getNextNumber(Policy policy, ConfigurationOwner owner);
}

/**
 * Private class used to track the usage of unique numbers.
 */
class ConfigurePropertyUniqueNumberHandler implements UniqueNumberHandler {
    private int blockEnd=0;
    private int nextNumber=0;

    private boolean isBlockEmpty() {
        return blockEnd==nextNumber;
    }

    private void setNextNumber(int next) {
        nextNumber=next;
    }

    private void setBlockEnd(int blockEnd) {
        this.blockEnd=blockEnd;
    }

    public int getNextNumber(Policy policy, ConfigurationOwner owner) {
        // get the next unique number block if we've run out of numbers
        synchronized(this) {
            if (isBlockEmpty()) {
                Configuration config=owner.getConfiguration();
                int nextNumber=Integer.parseInt(config.findParameter("NextNumber").getValue());
                int blockSize=Integer.parseInt(config.findParameter("BlockSize").getValue());

                setNextNumber(nextNumber);
                setBlockEnd(nextNumber+blockSize);

                config.findParameter("NextNumber").setValue(Integer.toString(nextNumber+blockSize+1));

                owner.setConfiguration(config);
            }
        }

        return nextNumber++;
    }
}

/**
 */
class PolicyIdUniqueNumberGeneratorHandler implements UniqueNumberHandler {
    public int getNextNumber(Policy policy, ConfigurationOwner owner) {
        return (int)policy.getSystemId();
    }
}
