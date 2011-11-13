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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.financial.Invoice;

public class GenerateInvoiceNumberRuleCommand extends Command implements GenerateInvoiceNumberRuleArg {
    private static final long serialVersionUID = 5485595422924140879L;

    private GenerateInvoiceNumberRuleArg args = null;

    public GenerateInvoiceNumberRuleCommand() {
        super();
        args = new GenerateInvoiceNumberRuleArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (GenerateInvoiceNumberRuleArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Invoice getInvoiceArg() {
        return args.getInvoiceArg();
    }

    /**
     * {@inheritDoc}
     * @param invoiceArg @{inheritDoc}
     */
    public void setInvoiceArg(Invoice invoiceArg) {
        args.setInvoiceArg(invoiceArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public int getUniqueNumberArg() {
        return args.getUniqueNumberArg();
    }

    /**
     * {@inheritDoc}
     * @param uniqueNumberArg @{inheritDoc}
     */
    public void setUniqueNumberArg(int uniqueNumberArg) {
        args.setUniqueNumberArg(uniqueNumberArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getInvoiceNumberRet() {
        return args.getInvoiceNumberRet();
    }

    /**
     * {@inheritDoc}
     * @param invoiceNumberRet @{inheritDoc}
     */
    public void setInvoiceNumberRet(String invoiceNumberRet) {
        args.setInvoiceNumberRet(invoiceNumberRet);
    }
}
