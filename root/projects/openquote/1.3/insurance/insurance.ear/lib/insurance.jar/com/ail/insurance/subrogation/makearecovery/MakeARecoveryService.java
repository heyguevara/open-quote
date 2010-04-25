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

import java.util.Date;
import java.util.Enumeration;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.claim.ClaimSection;
import com.ail.insurance.claim.ClaimStatus;
import com.ail.insurance.claim.Recovery;
import com.ail.insurance.claim.SectionNotFoundException;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/subrogation/makearecovery/MakeARecoveryService.java,v $
 * @stereotype entry-point
 */
public class MakeARecoveryService extends Service {
    private static final long serialVersionUID = 3198893603833694389L;
    private MakeARecoveryArg args=null;
	private Core core=null;

	/**
     * Default constructor
     */
	public MakeARecoveryService() {
		core=new Core(this);
    }

	/**
     * Getter to fetch the entry point's code. This method is demanded by
     * the EntryPoint class.
     * @return This entry point's instance of Core.
     */
	public Core getCore() {
        return core;
    }

	/**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
	public Version getVersion() {
		Version v=(Version)core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2005/08/19 20:20:58 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/subrogation/makearecovery/MakeARecoveryService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.1 $");
        return v;
    }

	/**
     * Setter used to the set the entry points arguments. This method will be
     * called before <code>invoke()</code> is called.
     * @param Arguments for invoke
     */
    public void setArgs(CommandArg args){
        this.args = (MakeARecoveryArg)args;
    }

	/**
     * Getter returning the arguments used by this entry point. This entry point
     * doesn't modify the arguments.
     * @return An instance of LoggerArgs.
	 */
    public CommandArg getArgs() {
        return (CommandArg)args;
    }

	/**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     * @throws SectionNotFoundException If one of the sections identified in the
     * recoveryMade hashtable does not exist.
     */
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
