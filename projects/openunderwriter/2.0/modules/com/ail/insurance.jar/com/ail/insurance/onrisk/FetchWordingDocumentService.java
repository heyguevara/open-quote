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
import com.ail.insurance.onrisk.GenerateWordingDocumentService.GenerateWordingDocumentCommand;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;

@ServiceImplementation
public class FetchWordingDocumentService extends Service<FetchWordingDocumentService.FetchWordingDocumentArgument> {
    private static final long serialVersionUID = 3198893603833694389L;
    
    @ServiceArgument
    public interface FetchWordingDocumentArgument extends Argument {
        /**
         * The number of the policy for which a document should be returned
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
         * @return Modified policy
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
    
    @ServiceCommand(defaultServiceClass=FetchWordingDocumentService.class)
    public interface FetchWordingDocumentCommand extends Command, FetchWordingDocumentArgument {}

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
            throw new PreconditionException("core.queryUnique(get.savedPolicy.by.policyNumbe, "+args.getPolicyNumberArg()+")==null");
        }
        
        // We only generate quote docs on demand, so if there isn't one - generate it.
        if (savedPolicy.getWordingDocument()==null) {
            GenerateWordingDocumentCommand cmd=getCore().newCommand(GenerateWordingDocumentCommand.class);
            cmd.setPolicyArg(savedPolicy.getPolicy());
            cmd.invoke();
            savedPolicy.setWordingDocument(cmd.getDocumentRet());
            savedPolicy=getCore().update(savedPolicy);
            args.setPolicyRet(savedPolicy.getPolicy());
        }

        args.setDocumentRet(savedPolicy.getWordingDocument());
    }
}
