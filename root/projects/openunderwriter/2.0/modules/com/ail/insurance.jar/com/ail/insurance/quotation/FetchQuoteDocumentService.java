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

package com.ail.insurance.quotation;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.quotation.GenerateQuoteDocumentService.GenerateQuoteDocumentCommand;

@ServiceImplementation
public class FetchQuoteDocumentService extends Service<FetchQuoteDocumentService.FetchQuoteArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    @ServiceArgument
    public interface FetchQuoteArgument extends Argument {
        /**
         * The number of the quotation for which a document should be returned
         * @return quotation number.
         */
        String getQuotationNumberArg();
        
        /**
         * @see #getInvoiceArg()
         * @param policyArg
         */
        void setQuotationNumberArg(String quotationNumberArg);
        
        /**
         * Fetch the quotation which may have been modified by the process of document generation
         * @return Modified quotation
         */
        Policy getQuotationRet();
        
        /**
         * @see #getQuotationRet()
         * @param policyRet
         */
        void setQuotationRet(Policy policyRet);

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

    @ServiceCommand(defaultServiceClass=FetchQuoteDocumentService.class)
    public interface FetchQuoteCommand extends Command, FetchQuoteArgument {
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     */
    public void invoke() throws BaseException {
        if (args.getQuotationNumberArg()==null || args.getQuotationNumberArg().length()==0) {
            throw new PreconditionException("args.getQuotationNumberArg()==null || args.getQuotationNumberArg().length()==0");
        }

        // Fetch the saved quote from persistence
        SavedPolicy savedPolicy=(SavedPolicy)getCore().queryUnique("get.savedPolicy.by.quotationNumber", args.getQuotationNumberArg());
        
        if (savedPolicy==null) {
            throw new PreconditionException("core.queryUnique(get.savedPolicy.by.quotationNumber, "+args.getQuotationNumberArg()+")==null");
        }
        
        // We only generate quote docs on demand, so if there isn't one - generate it.
        if (savedPolicy.getQuotationDocument()==null) {
            GenerateQuoteDocumentCommand cmd=getCore().newCommand(GenerateQuoteDocumentCommand.class);
            cmd.setPolicyArg(savedPolicy.getPolicy());
            cmd.invoke();
            savedPolicy.setQuotationDocument(cmd.getDocumentRet());
            savedPolicy=getCore().update(savedPolicy);
            args.setQuotationRet(savedPolicy.getPolicy());
        }

        args.setDocumentRet(savedPolicy.getQuotationDocument());
    }
}
