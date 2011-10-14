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

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

public class NotifyBrokerArgImp extends CommandArgImp implements NotifyBrokerArg {
    private static final long serialVersionUID = 4473189523716866043L;

    /** The policy to generate a quote number for, and to which the number is added. */
    private String quotationNumberArg;
    
    private Policy policyArg;

    /** Default constructor */
    public NotifyBrokerArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getQuotationNumberArg() {
        return quotationNumberArg;
    }

    /**
     * {@inheritDoc}
     * @param quotationNumberArg @{inheritDoc}
     */
    public void setQuotationNumberArg(String quotationNumberArg) {
        this.quotationNumberArg = quotationNumberArg;
    }

    /**
     * {@inheritDoc}
     */
    public Policy getPolicyArg() {
        return policyArg;
    }

    /**
     * {@inheritDoc}
     * @param quotationArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        this.policyArg = policyArg;
    }
}
