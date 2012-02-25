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

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.insurance.quotation.FetchQuoteService.FetchDocumentArgument;
import com.ail.insurance.quotation.GenerateQuoteService.GenerateDocumentCommand;
import com.ail.openquote.SavedQuotation;

/**
 * This service fetches the quotation document associated with a quotation. If the document hasn't been generated
 * in the past, the service will generate it and persist it with the quotation for reuse later.
 */
@ServiceImplementation
public class FetchDocumentService extends Service<FetchDocumentArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     */
	public void invoke() throws BaseException {
        if (args.getQuotationNumberArg()==null || args.getQuotationNumberArg().length()==0) {
            throw new PreconditionException("args.getQuotationNumberArg()==null || args.getQuotationNumberArg().length()==0");
        }

        // Fetch the saved quote from persistence
        SavedQuotation savedQuotation=(SavedQuotation)getCore().queryUnique("get.savedQuotation.by.quotationNumber", args.getQuotationNumberArg());
        
        if (savedQuotation==null) {
            throw new PreconditionException("core.queryUnique(get.savedQuotation.by.quotationNumber, "+args.getQuotationNumberArg()+")==null");
        }
        
        // We only generate quote docs on demand, so if there isn't one - generate it.
        if (savedQuotation.getQuotationDocument()==null) {
            GenerateDocumentCommand cmd=getCore().newCommand(GenerateDocumentCommand.class);
            cmd.setPolicyArg(savedQuotation.getQuotation());
            cmd.invoke();
            savedQuotation.setQuotationDocument(cmd.getDocumentRet());
            savedQuotation=getCore().update(savedQuotation);
            args.setQuotationRet(savedQuotation.getQuotation());
        }

        args.setDocumentRet(savedQuotation.getQuotationDocument());
    }
}
