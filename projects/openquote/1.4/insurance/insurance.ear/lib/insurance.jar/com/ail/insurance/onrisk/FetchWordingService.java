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
import com.ail.annotation.ServiceInterface;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.policy.Policy;

@ServiceInterface
public interface FetchWordingService {

    @ServiceArgument
    public interface FetchWordingArgument extends Argument {
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
    
    @ServiceCommand
    public interface FetchWordingCommand extends Command, FetchWordingArgument {}
}
