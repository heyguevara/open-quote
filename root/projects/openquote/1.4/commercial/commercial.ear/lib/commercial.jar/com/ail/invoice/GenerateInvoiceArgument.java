/* Copyright Applied Industrial Logic Limited 2011. All rights Reserved */
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

package com.ail.invoice;

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;
import com.ail.financial.Invoice;

/**
 * Interface defining the arguments and returns associated with the invoice document generation service.
 */
@ArgumentDefinition
public interface GenerateInvoiceArgument extends Argument {
    /**
     * The invoice to generate a document for.
     * @return
     */
    Invoice getInvoiceArg();
    
    /**
     * @see #setInvoiceArg(Invoice)
     * @param invoiceArg
     */
    void setInvoiceArg(Invoice invoiceArg);
    
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
