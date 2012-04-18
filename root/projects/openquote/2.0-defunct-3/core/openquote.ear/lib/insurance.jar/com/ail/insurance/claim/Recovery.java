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

package com.ail.insurance.claim;

import java.util.Date;

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;

@TypeDefinition
public class Recovery extends Type {
    private static final long serialVersionUID = -5789050829918007998L;

    private String reason;
    private String recoveredFrom;
    private RecoveryType recoveryType;
    private PaymentType paymentType;
    private Date recoveredDate;
    private CurrencyAmount amount;
    private String ID;

    public String getReason() { return reason; }

    public void setReason(String reason) { this.reason = reason; }

    public String getRecoveredFrom() { return recoveredFrom; }

    public void setRecoveredFrom(String recoveredFrom) { this.recoveredFrom = recoveredFrom; }

    public RecoveryType getRecoveryType() { return recoveryType; }

    public void setRecoveryType(RecoveryType recoveryType) { this.recoveryType = recoveryType; }

    public PaymentType getPaymentType() { return paymentType; }

    public void setPaymentType(PaymentType paymentType) { this.paymentType = paymentType; }

    public Date getRecoveredDate() { return recoveredDate; }

    public void setRecoveredDate(Date recoveredDate) { this.recoveredDate = recoveredDate; }

    public String getID() { return ID; }

    public void setID(String ID) { this.ID = ID; }

    public void setAmount(CurrencyAmount amount) { this.amount = amount; }

    public CurrencyAmount getAmount() { return this.amount; }
}
