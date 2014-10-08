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

import java.security.Principal;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Vector;

import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.VersionEffectiveDate;
import com.ail.financial.Currency;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.claim.Claim;
import com.ail.insurance.claim.ClaimSection;
import com.ail.insurance.claim.ClaimStatus;
import com.ail.insurance.claim.PaymentType;
import com.ail.insurance.claim.Recovery;
import com.ail.insurance.claim.RecoveryType;
import com.ail.insurance.claim.SectionNotFoundException;
import com.ail.insurance.subrogation.makearecovery.MakeARecoveryCommand;

/**
 * This proxy is provided to simplify JSP access to the subrogation component's
 * MakeARecovery service.
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2007/06/04 13:10:16 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/subrogation-ejb.jar/com/ail/insurance/subrogation/SubrogationBeanProxy.java,v $
 */
public class SubrogationBeanProxy implements CoreUser {
    private static final long serialVersionUID = -1502116276816641324L;
    private Claim claim = null;
    private MakeARecoveryCommand command = null;
    private Core core = null;
    private VersionEffectiveDate versionEffectiveDate = null;
    private Hashtable<String,CurrencyAmount> recoveries = new Hashtable<String,CurrencyAmount>();

    public void setRecoveryType(String recoveryType) {
        command.setRecoveryType(RecoveryType.valueOf(recoveryType));
        core.logDebug("set recovery type to: " + command.getRecoveryType().name());
    }

    public void setPaymentType(String paymentType) {
        command.setPaymentType(PaymentType.valueOf(paymentType));
        core.logDebug("set payment type to: " + command.getPaymentType().name());
    }

    public void setRecoveryAmount1(String recoveryAmount) {
        recoveries.put("SECTION1", new CurrencyAmount(Double.parseDouble(recoveryAmount), Currency.USD));
        core.logDebug("set recovery 1 to: " + recoveryAmount);
    }

    public void setRecoveryAmount2(String recoveryAmount) {
        recoveries.put("SECTION2", new CurrencyAmount(Double.parseDouble(recoveryAmount), Currency.USD));
        core.logDebug("set recovery 2 to: " + recoveryAmount);
    }

    public void setRecoveryAmount3(String recoveryAmount) {
        recoveries.put("SECTION3", new CurrencyAmount(Double.parseDouble(recoveryAmount), Currency.USD));
        core.logDebug("set recovery 3 to: " + recoveryAmount);
    }

    public void invokeClaim() {
        if (!recoveries.isEmpty()) {
            try {
                command.setRecoveryMade(recoveries);
                command.invoke();
                claim = command.getClaim();
                core.logDebug("claim invoked..");
            } catch (com.ail.core.BaseException be) {
                exceptionHandler(be);
            }
        }
    }

    public boolean getClaimInitialized() {
        return (claim != null);
    }

    public String getOutstandingTotal() {
        return command.getClaim().getOutstandingTotal().toString();
    }

    public String getTotalRecovered() {
        return command.getClaim().getTotalRecovered().toString();
    }

    public Vector<Recovery> getRecoveries() {
        try {
            Collection<Recovery> col=command.getClaim().getClaimSectionByID("SECTION1").getRecoveries();
            return new Vector<Recovery>(col);
        } catch (SectionNotFoundException snfe) {
            exceptionHandler(snfe);
            return null;
        }
    }

    public String getEstimatedReserve(String sectionId) {
        try {
            return command.getClaim().getClaimSectionByID(sectionId).getEstimatedReserve().toString();
        } catch (com.ail.insurance.claim.SectionNotFoundException snfe) {
            exceptionHandler(snfe);
            return "?";
        }
    }

    public String getTotalRecovered(String sectionId) {
        try {
            return command.getClaim().getClaimSectionByID(sectionId).getTotalRecovered().toString();
        } catch (com.ail.insurance.claim.SectionNotFoundException snfe) {
            exceptionHandler(snfe);
            return "?";
        }
    }

    public String getOutstandingClaim(String sectionId) {
        try {
            return command.getClaim().getClaimSectionByID(sectionId).getOutstandingClaim().toString();
        } catch (com.ail.insurance.claim.SectionNotFoundException snfe) {
            exceptionHandler(snfe);
            return "?";
        }
    }

    /**
     * Create an sample claim to work with.
     */
    public void initClaim() {
        if (claim == null) {
            setUp();
            command = (MakeARecoveryCommand)core.newCommand("MakeARecovery");
            claim = (Claim)core.newType("Claim");
            claim.setSubrogationPotential(true);
            claim.setClaimStatus(ClaimStatus.OPEN);
            claim.setOutstandingTotal(new CurrencyAmount(1000.00, Currency.USD));
            claim.setTotalRecovered(new CurrencyAmount(0.00, Currency.USD));

            ClaimSection section1 = (ClaimSection)core.newType("ClaimSection");
            section1.setID("SECTION1");
            section1.setEstimatedReserve(new CurrencyAmount(100.0, Currency.USD));
            section1.setTotalRecovered(new CurrencyAmount(0.0, Currency.USD));
            section1.setOutstandingClaim(new CurrencyAmount(500.0, Currency.USD));
            claim.getClaimSections().add(section1);

            ClaimSection section2 = (ClaimSection)core.newType("ClaimSection");
            section2.setID("SECTION2");
            section2.setEstimatedReserve(new CurrencyAmount(100.0, Currency.USD));
            section2.setTotalRecovered(new CurrencyAmount(0.0, Currency.USD));
            section2.setOutstandingClaim(new CurrencyAmount(300.0, Currency.USD));
            claim.getClaimSections().add(section2);

            ClaimSection section3 = (ClaimSection)core.newType("ClaimSection");
            section3.setID("SECTION3");
            section3.setEstimatedReserve(new CurrencyAmount(50.0, Currency.USD));
            section3.setTotalRecovered(new CurrencyAmount(0.0, Currency.USD));
            section3.setOutstandingClaim(new CurrencyAmount(200.0, Currency.USD));
            claim.getClaimSections().add(section3);

            command.setSource("From the insured");
            command.setReason("Following a demand");
            command.setClaim(claim);

            core.logDebug("claim initialized..");
        }
    }

    protected void setUp() {
        core = new Core(this);
        versionEffectiveDate = new VersionEffectiveDate();
    }

    /**
     * Method demanded by the CoreUser interface.
     * @return A date to use to selecte the corrent version of config info.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    public Principal getSecurityPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }

    private void exceptionHandler(Exception exception) {
        exception.printStackTrace();
    }

    public String getConfigurationNamespace() {
        // TODO Auto-generated method stub
        return null;
    }
}



