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

package com.ail.insurance.onrisk.certificate;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.insurance.onrisk.FetchCertificateService.FetchCertificateArgument;
import com.ail.insurance.onrisk.GenerateCertificateService.GenerateCertificateCommand;
import com.ail.openquote.SavedQuotation;

/**
 * This service fetches the certificate document associated with a quotation. If the document hasn't been generated
 * in the past, the service will generate it and persist it with the quotation for reuse later.
 */
@ServiceImplementation
public class FetchCertificateService extends Service<FetchCertificateArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     */
	public void invoke() throws BaseException {
        if (args.getPolicyNumberArg()==null || args.getPolicyNumberArg().length()==0) {
            throw new PreconditionException("args.getPolicyNumberArg()==null || args.getPolicyNumberArg().length()==0");
        }

        // Fetch the saved quote from persistence
        SavedQuotation savedQuotation=(SavedQuotation)getCore().queryUnique("get.savedPolicy.by.policyNumber", args.getPolicyNumberArg());
        
        if (savedQuotation==null) {
            throw new PreconditionException("core.queryUnique(get.savedPolicy.by.policyNumber, "+args.getPolicyNumberArg()+")==null");
        }
        
        // We only generate quote docs on demand, so if there isn't one - generate it.
        if (savedQuotation.getCertificateDocument()==null) {
        	GenerateCertificateCommand cmd=getCore().newCommand(GenerateCertificateCommand.class);
            cmd.setPolicyArg(savedQuotation.getQuotation());
            cmd.invoke();
            savedQuotation.setCertificateDocument(cmd.getDocumentRet());
            savedQuotation=getCore().update(savedQuotation);
            args.setPolicyRet(savedQuotation.getQuotation());
        }

        args.setDocumentRet(savedQuotation.getCertificateDocument());
    }
}
