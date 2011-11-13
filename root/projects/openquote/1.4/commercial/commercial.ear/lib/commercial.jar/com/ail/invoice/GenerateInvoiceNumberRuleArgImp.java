/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

import com.ail.core.command.CommandArgImp;
import com.ail.financial.Invoice;

public class GenerateInvoiceNumberRuleArgImp extends CommandArgImp implements GenerateInvoiceNumberRuleArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** The policy to generate a number for */
    private Invoice invoiceArg;

    /**
     * A unique number that may be used by the number generation service. This number is guaranteed to be unique for each
     * invocation of the service.
     */
    private int uniqueNumberArg;

    /** The generated policy number. */
    private String invoiceNumberRet;

    /** Default constructor */
    public GenerateInvoiceNumberRuleArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Invoice getInvoiceArg() {
        return invoiceArg;
    }

    /**
     * {@inheritDoc}
     * @param policyArg @{inheritDoc}
     */
    public void setInvoiceArg(Invoice invoiceArg) {
        this.invoiceArg = invoiceArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public int getUniqueNumberArg() {
        return uniqueNumberArg;
    }

    /**
     * {@inheritDoc}
     * @param uniqueNumberArg @{inheritDoc}
     */
    public void setUniqueNumberArg(int uniqueNumberArg) {
        this.uniqueNumberArg = uniqueNumberArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getInvoiceNumberRet() {
        return invoiceNumberRet;
    }

    /**
     * {@inheritDoc}
     * @param invoiceNumberRet @{inheritDoc}
     */
    public void setInvoiceNumberRet(String policyNumberRet) {
        this.invoiceNumberRet = policyNumberRet;
    }
}


