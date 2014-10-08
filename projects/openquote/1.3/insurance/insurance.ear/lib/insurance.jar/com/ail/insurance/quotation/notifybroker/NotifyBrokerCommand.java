/* Copyright Applied Industrial Logic Limited 2010. All rights Reserved */
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
package com.ail.insurance.quotation.notifybroker;

import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.quotation.notifyparty.NotifyPartyCommand;

public class NotifyBrokerCommand extends NotifyPartyCommand {
    private static final long serialVersionUID = -3654246992587485810L;
    private NotifyBrokerArg args = null;

    public NotifyBrokerCommand() {
        super();
        args = new NotifyBrokerArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (NotifyBrokerArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getQuotationNumberArg() {
        return args.getQuotationNumberArg();
    }

    /**
     * {@inheritDoc}
     * @param quotationNumberArg @{inheritDoc}
     */
    public void setQuotationNumberArg(String quotationNumberArg) {
        args.setQuotationNumberArg(quotationNumberArg);
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
     * @param quotationArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        args.setPolicyArg(policyArg);
    }
}
