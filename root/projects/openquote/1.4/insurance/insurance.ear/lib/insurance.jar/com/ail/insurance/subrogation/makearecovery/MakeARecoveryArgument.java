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

package com.ail.insurance.subrogation.makearecovery;

import java.util.Hashtable;

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.claim.Claim;
import com.ail.insurance.claim.PaymentType;
import com.ail.insurance.claim.RecoveryType;

@ArgumentDefinition
public interface MakeARecoveryArgument extends Argument {
    public Claim getClaim();

    public void setClaim(Claim claim);

    public RecoveryType getRecoveryType();

    public void setRecoveryType(RecoveryType recoveryType);

    public PaymentType getPaymentType();

    public void setPaymentType(PaymentType paymentType);

    public Hashtable<String,CurrencyAmount> getRecoveryMade();

    public void setRecoveryMade(Hashtable<String,CurrencyAmount> recoveryMade);

    public String getSource();

    public void setSource(String source);

    public String getReason();

    public void setReason(String reason);
}
