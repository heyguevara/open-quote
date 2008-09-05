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

package com.ail.insurance.quotation.fetchdocument;

import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;

/**
 * Interface defining the arguments and returns associated with the fetch document service.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public interface FetchDocumentArg extends CommandArg {
    /**
     * The number of the quotation for which a document should be returned
     * @return quotation number.
     */
    String getQuotationNumberArg();
    
    /**
     * @see #getPolicyArg()
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
