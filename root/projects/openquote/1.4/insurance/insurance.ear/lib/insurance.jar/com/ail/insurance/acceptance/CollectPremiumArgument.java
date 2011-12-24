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

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.Policy;

@ArgumentDefinition
public interface CollectPremiumArgument extends Argument {
    /**
     * Getter for the policyArg property. Policy to collect premium for
     * @return Value of policyArg, or null if it is unset
     */
    Policy getPolicyArg();

    /**
     * Setter for the policyArg property. * @see #getPolicyArg
     * @param policyArg new value for property.
     */
    void setPolicyArg(Policy policyArg);

	/**
	 * Getter for the prePaymentArg property. Amount if premium already collected & just to be posted
	 * @return Value of prePaymentArg, or null if it is premium to be collected from using policy payment details
	 */
	CurrencyAmount getPrePaymentArg();

	/**
	 * Setter for the prePaymentArg property. * @see #getPrePaymentArg
	 * @param prePaymentArg new value for property.
	 */
	void setPrePaymentArg(CurrencyAmount prePaymentArg);
}


