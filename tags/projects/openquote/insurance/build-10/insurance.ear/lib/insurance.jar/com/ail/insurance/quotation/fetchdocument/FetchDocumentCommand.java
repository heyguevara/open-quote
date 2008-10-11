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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class FetchDocumentCommand extends Command implements FetchDocumentArg {
    private static final long serialVersionUID = -4556506348834606687L;
    private FetchDocumentArg args;
    
    public FetchDocumentCommand() {
        super();
		args=new FetchDocumentArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args=(FetchDocumentArg)arg;
    }

    public CommandArg getArgs() {
        return (CommandArg)args;
    }

    /** @inheritDoc */
    public byte[] getDocumentRet() {
        return args.getDocumentRet();
    }
    
    /** @inheritDoc */
    public void setDocumentRet(byte[] documentRet) {
        args.setDocumentRet(documentRet);
    }
    
    /** @inheritDoc */
    public String getQuotationNumberArg() {
        return args.getQuotationNumberArg();
    }
    
    /** @inheritDoc */
    public void setQuotationNumberArg(String quotationNumberArg) {
        args.setQuotationNumberArg(quotationNumberArg);
    }

    public Policy getQuotationRet() {
        return args.getQuotationRet();
    }

    public void setQuotationRet(Policy policyRet) {
        args.setQuotationRet(policyRet);
    }
}
