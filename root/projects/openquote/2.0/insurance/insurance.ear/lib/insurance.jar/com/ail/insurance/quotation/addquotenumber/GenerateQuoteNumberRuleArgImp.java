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

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/addquotenumber/GenerateQuoteNumberRuleArgImp.java,v $
 * @stereotype argimp
 */
public class GenerateQuoteNumberRuleArgImp extends CommandArgImp implements GenerateQuoteNumberRuleArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** The policy to generate a number for */
    private Policy policyArg;

    /** The generated number */
    private String quoteNumberRet;

    /**
     * A unique number generated for each call to the rule service. This number may be used as part of the quote
     * generation rule if required.
     */
    private int uniqueNumberArg;

    /** Default constructor */
    public GenerateQuoteNumberRuleArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArg() {
        return policyArg;
    }

    /**
     * {@inheritDoc}
     * @param policyArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        this.policyArg = policyArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getQuoteNumberRet() {
        return quoteNumberRet;
    }

    /**
     * {@inheritDoc}
     * @param quoteNumberRet @{inheritDoc}
     */
    public void setQuoteNumberRet(String quoteNumberRet) {
        this.quoteNumberRet = quoteNumberRet;
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
}


