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

package com.ail.insurance.subrogation;

import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;

import com.ail.annotation.Configurable;
import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.claim.Claim;
import com.ail.insurance.claim.ClaimSection;
import com.ail.insurance.claim.ClaimStatus;
import com.ail.insurance.claim.PaymentType;
import com.ail.insurance.claim.Recovery;
import com.ail.insurance.claim.RecoveryType;
import com.ail.insurance.claim.SectionNotFoundException;

@Configurable
@ServiceImplementation
public class MakeARecoveryService extends Service<MakeARecoveryService.MakeARecoveryArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    @ServiceArgument
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

    @ServiceCommand(defaultServiceClass=MakeARecoveryService.class)
    public interface MakeARecoveryCommand extends Command, MakeARecoveryArgument {
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     * @throws SectionNotFoundException If one of the sections identified in the
     * recoveryMade hashtable does not exist.
     */
    @Override
	public void invoke() throws PreconditionException, SectionNotFoundException {
		if (args.getClaim()==null) {
            throw new PreconditionException("claim==null");
        }

		if (!ClaimStatus.OPEN.equals(args.getClaim().getClaimStatus())
        &&  !ClaimStatus.REOPEN.equals(args.getClaim().getClaimStatus())) {
            throw new PreconditionException("claim.getClaimStatus() != ClaimStatus.OPEN || ClaimStatus.REOPEN");
        }

		if (args.getClaim().isSubrogationWaiver()) {
            throw new PreconditionException("claim.isSubrogationWaiver()==true");
        }

		if (!args.getClaim().isSubrogationPotential()) {
            throw new PreconditionException("claim.isSubrogationPotential()==false");
		}

        if (args.getReason()==null || args.getReason().length()==0) {
            throw new PreconditionException("reason==null || reason==\"\"");
        }

        if (args.getSource()==null || args.getSource().length()==0) {
            throw new PreconditionException("source==null || source==\"\"");
		}

        if (args.getRecoveryMade()==null) {
            throw new PreconditionException("recoveryMade==null");
        }

        if (args.getPaymentType()==null) {
            throw new PreconditionException("paymentType==null");
        }

        if (args.getRecoveryType()==null) {
            throw new PreconditionException("recoveryType==null");
        }

        ClaimSection section=null;
        Recovery recoveryRec=null;

        for(Enumeration<String> en=args.getRecoveryMade().keys() ; en.hasMoreElements() ; ) {
            String sectionID=en.nextElement();
            CurrencyAmount recovery=(CurrencyAmount)args.getRecoveryMade().get(sectionID);

            // ignore recoveries with zero amounts
            if (recovery.getAmount().doubleValue() != 0) {
                core.logDebug("payment against section: "+sectionID+" amount:"+recovery.getAmount());
    
                section=args.getClaim().getClaimSectionByID(sectionID);
    
                section.setEstimatedReserve(section.getEstimatedReserve().subtract(recovery));
                section.setTotalRecovered(section.getTotalRecovered().add(recovery));
                section.setOutstandingClaim(section.getOutstandingClaim().subtract(recovery));
                args.getClaim().setOutstandingTotal(args.getClaim().getOutstandingTotal().subtract(recovery));
                args.getClaim().setTotalRecovered(args.getClaim().getTotalRecovered().add(recovery));
    
                recoveryRec=(Recovery)core.newType("Recovery");
                recoveryRec.setRecoveredDate(new Date());
                recoveryRec.setReason(args.getReason());
                recoveryRec.setRecoveredFrom(args.getSource());
                recoveryRec.setPaymentType(args.getPaymentType());
                recoveryRec.setRecoveryType(args.getRecoveryType());
                recoveryRec.setAmount(recovery);
                section.addRecovery(recoveryRec);
            }
        }
    }
}
