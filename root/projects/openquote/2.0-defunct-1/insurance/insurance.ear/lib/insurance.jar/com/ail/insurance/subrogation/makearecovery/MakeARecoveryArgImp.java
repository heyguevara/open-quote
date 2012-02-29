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

import com.ail.core.command.CommandArgImp;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.claim.Claim;
import com.ail.insurance.claim.PaymentType;
import com.ail.insurance.claim.RecoveryType;

/**
 * This class represents the value object arguments and returns used/generated
 * by the MakeARecovery entry point.
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 23:19:08 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/subrogation/makearecovery/MakeARecoveryArgImp.java,v $
 */
public class MakeARecoveryArgImp extends CommandArgImp implements MakeARecoveryArg {
    private static final long serialVersionUID = 3606702185555605240L;
    private Claim claim;
    private RecoveryType recoveryType;
    private PaymentType paymentType;
    private Hashtable<String,CurrencyAmount> recoveryMade=new Hashtable<String,CurrencyAmount>();
    private String source;
    private String reason;

    public Claim getClaim(){ return claim; }

    public void setClaim(Claim claim){ this.claim = claim; }

    public RecoveryType getRecoveryType(){ return recoveryType; }

    public void setRecoveryType(RecoveryType recoveryType){ this.recoveryType = recoveryType; }

    public PaymentType getPaymentType(){ return paymentType; }

    public void setPaymentType(PaymentType paymentType){ this.paymentType = paymentType; }

    public Hashtable<String,CurrencyAmount> getRecoveryMade(){ return recoveryMade; }

    public void setRecoveryMade(Hashtable<String,CurrencyAmount> recoveryMade){ this.recoveryMade = recoveryMade; }

    public String getSource(){ return source; }

    public void setSource(String source){ this.source = source; }

    public String getReason(){ return reason; }

    public void setReason(String reason){ this.reason = reason; }
}
