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

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

/**
 * This class represents the value object arguments and returns used/generated
 * by the MakeARecovery entry point.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class FetchDocumentArgImp extends CommandArgImp implements FetchDocumentArg {
    private static final long serialVersionUID = 3606702185555605240L;
    private String quotationNumberArg;
    private byte[] documentRet;
    private Policy quotationRet;
    
    /** @inheritDoc */
    public Policy getQuotationRet() {
        return quotationRet;
    }

    /** @inheritDoc */
    public void setQuotationRet(Policy quotationRet) {
        this.quotationRet = quotationRet;
    }

    /** @inheritDoc */
    public byte[] getDocumentRet() {
        return documentRet;
    }
    
    /** @inheritDoc */
    public void setDocumentRet(byte[] documentRet) {
        this.documentRet = documentRet;
    }
    
    /** @inheritDoc */
    public String getQuotationNumberArg() {
        return quotationNumberArg;
    }
    
    /** @inheritDoc */
    public void setQuotationNumberArg(String quotationNumberArg) {
        this.quotationNumberArg = quotationNumberArg;
    }
}
