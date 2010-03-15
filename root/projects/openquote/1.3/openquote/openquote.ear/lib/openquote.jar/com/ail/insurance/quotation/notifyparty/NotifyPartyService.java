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
package com.ail.insurance.quotation.notifyparty;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;

/**
 * This service simply delegates to the service specified by the quotation's product.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype service
 */
public class NotifyPartyService extends Service {
    private static final long serialVersionUID = -4915889686192216902L;
    private NotifyPartyArg args = null;
    private String configurationNamespace = null;
    private Core core = null;

    /** Default constructor */
    public NotifyPartyService() {
        core = new Core(this);
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
    public void setArgs(CommandArg args) {
        this.args = (NotifyPartyArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of CalculateTaxArgs.
     */
    public CommandArg getArgs() {
        return args;
    }
    
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
        Quotation quote=null;
        
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

            SavedQuotation savedQuotation=(SavedQuotation)getCore().query("get.savedQuotation.by.quotationNumber", args.getQuotationNumberArg());

            quote=savedQuotation.getQuotation();
        }
        else {
            quote=(Quotation)args.getPolicyArg();
        }
        
        String namespace=Functions.productNameToConfigurationNamespace(quote.getProductTypeId());
        setConfigurationNamespace(namespace);
        
        NotifyPartyCommand command=(NotifyPartyCommand)core.newCommand("NotifyParty");
        
        command.setPolicyArg(quote);

        command.invoke();
    }
}


