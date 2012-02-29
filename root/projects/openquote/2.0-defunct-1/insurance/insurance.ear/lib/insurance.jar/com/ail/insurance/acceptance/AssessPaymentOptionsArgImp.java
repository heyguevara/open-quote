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

package com.ail.insurance.acceptance;

import java.util.ArrayList;

import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/04/24 21:07:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/acceptance/AssessPaymentOptionsArgImp.java,v $
 * @stereotype argimp
 */
public class AssessPaymentOptionsArgImp extends com.ail.core.command.CommandArgImp implements AssessPaymentOptionsArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** Policy to assess payment options for */
    private Policy policyArg;

	/** The payment options suggested by the service */
	private ArrayList<PaymentSchedule> optionsRet;

    /** Default constructor */
    public AssessPaymentOptionsArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return {@inheritDoc}
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
     * @return {@inheritDoc}
     */
    public ArrayList<PaymentSchedule> getOptionsRet() {
        return optionsRet;
    }

    /**
     * {@inheritDoc
     * @param paymentOptionRet {@inheritDoc}
     */
    public void setOptionsRet(ArrayList<PaymentSchedule> optionsRet) {
        this.optionsRet = optionsRet;
    }
}


