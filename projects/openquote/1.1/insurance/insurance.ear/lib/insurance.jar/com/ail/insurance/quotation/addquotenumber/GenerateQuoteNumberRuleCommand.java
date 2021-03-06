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

package com.ail.insurance.quotation.addquotenumber;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addquotenumber/GenerateQuoteNumberRuleCommand.java,v $
 * @stereotype command
 */
public class GenerateQuoteNumberRuleCommand extends Command implements GenerateQuoteNumberRuleArg {
    private static final long serialVersionUID = 7864522245187447353L;
    private GenerateQuoteNumberRuleArg args = null;

    public GenerateQuoteNumberRuleCommand() {
        super();
        args = new GenerateQuoteNumberRuleArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (GenerateQuoteNumberRuleArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArg() {
        return args.getPolicyArg();
    }

    /**
     * {@inheritDoc}
     * @param policyArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        args.setPolicyArg(policyArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getQuoteNumberRet() {
        return args.getQuoteNumberRet();
    }

    /**
     * {@inheritDoc}
     * @param quoteNumberRet @{inheritDoc}
     */
    public void setQuoteNumberRet(String quoteNumberRet) {
        args.setQuoteNumberRet(quoteNumberRet);
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
}
