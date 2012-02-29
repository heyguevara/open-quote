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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.claim.Claim;
import com.ail.insurance.claim.PaymentType;
import com.ail.insurance.claim.RecoveryType;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/12/19 22:28:31 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/subrogation/makearecovery/MakeARecoveryCommand.java,v $
 */
public class MakeARecoveryCommand extends Command implements MakeARecoveryArg {
    private static final long serialVersionUID = -4556506348834606687L;
    private MakeARecoveryArg args=null;

    public MakeARecoveryCommand() {
        super();
		args=new MakeARecoveryArgImp();
    }

    public void setArgs(CommandArg arg) {
		this.args=(MakeARecoveryArg)arg;
    }

    public CommandArg getArgs() {
		return (CommandArg)args;
    }

    public Claim getClaim() {
		return args.getClaim();
    }

    public void setClaim(Claim claim) {
		args.setClaim(claim);
    }

    public RecoveryType getRecoveryType() {
		return args.getRecoveryType();
    }

    public void setRecoveryType(RecoveryType recoveryType) {
		args.setRecoveryType(recoveryType);
    }

    public PaymentType getPaymentType() {
		return args.getPaymentType();
    }

    public void setPaymentType(PaymentType paymentType) {
		args.setPaymentType(paymentType);
    }

    public Hashtable<String,CurrencyAmount> getRecoveryMade() {
		return args.getRecoveryMade();
    }

    public void setRecoveryMade(Hashtable<String,CurrencyAmount> recoveryMade) {
		args.setRecoveryMade(recoveryMade);
    }

    public String getSource() {
		return args.getSource();
    }

    public void setSource(String source) {
		args.setSource(source);
    }

    public String getReason() {
		return args.getReason();
    }

    public void setReason(String reason) {
		args.setReason(reason);
    }
}
