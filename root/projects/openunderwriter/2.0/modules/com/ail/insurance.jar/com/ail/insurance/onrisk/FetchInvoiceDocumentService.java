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

package com.ail.insurance.onrisk;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.onrisk.GenerateInvoiceDocumentService.GenerateInvoiceDocumentCommand;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;

@ServiceImplementation
public class FetchInvoiceDocumentService extends Service<FetchInvoiceDocumentService.FetchInvoiceDocumentArgument> {
    private static final long serialVersionUID = 3198893603833694389L;
    
    @ServiceArgument
    public interface FetchInvoiceDocumentArgument extends Argument {
        /**
         * The number of the invoice for which a document should be returned
         * @return policy number.
         */
        String getPolicyNumberArg();
        
        /**
         * @see #getInvoiceArg()
         * @param policyArg
         */
        void setPolicyNumberArg(String policyNumberArg);
        
        /**
         * Fetch the policy which may have been modified by the process of document generation
         * @return Modified quotation
         */
        Policy getPolicyRet();
        
        /**
         * @see #getPolicyRet()
         * @param policyRet
         */
        void setPolicyRet(Policy policyRet);

        /**
         * The generated document.
         * @return document
         */
        byte[] getDocumentRet();

        /**
         * @see #getDocumentRet()
         * @param documentRet
         */
        void setDocumentRet(byte[] documentRet);
    }

    @ServiceCommand(defaultServiceClass=FetchInvoiceDocumentService.class)
    public interface FetchInvoiceDocumentCommand extends Command, FetchInvoiceDocumentArgument {
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     */
    public void invoke() throws BaseException {
        if (args.getPolicyNumberArg()==null || args.getPolicyNumberArg().length()==0) {
            throw new PreconditionException("args.getPolicyNumberArg()==null || args.getPolicyNumberArg().length()==0");
        }

        // Fetch the saved quote from persistence
        SavedPolicy savedPolicy=(SavedPolicy)getCore().queryUnique("get.savedPolicy.by.policyNumber", args.getPolicyNumberArg());
        
        if (savedPolicy==null) {
            throw new PreconditionException("core.queryUnique(get.savedPolicy.by.policyNumber, "+args.getPolicyNumberArg()+")==null");
        }
        
        if (savedPolicy.getPolicy()==null || savedPolicy.getPolicy().getPaymentDetails()==null) {
            throw new PreconditionException("savedQuotation.getQuotation()==null || savedQuotation.getQuotation().getPaymentDetails()==null");
        }
        
        // We only generate quote docs on demand, so if there isn't one - generate it.
        if (savedPolicy.getInvoiceDocument()==null) {
            GenerateInvoiceDocumentCommand cmd=getCore().newCommand(GenerateInvoiceDocumentCommand.class);
            cmd.setPolicyArg(savedPolicy.getPolicy());
            cmd.invoke();
            savedPolicy.setInvoiceDocument(cmd.getDocumentRet());
            savedPolicy=getCore().update(savedPolicy);
            args.setPolicyRet(savedPolicy.getPolicy());
        }

        args.setDocumentRet(savedPolicy.getInvoiceDocument());
    }
}
