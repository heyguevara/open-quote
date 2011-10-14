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

import java.util.Collection;
import java.util.Vector;

import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;

/** @stereotype type */
public class Claim extends Type {
    private static final long serialVersionUID = -3990863191815395871L;

    private String ID;
    private ClaimStatus claimStatus;

    /**
     *@link aggregationByValue
     *   @associates <{ClaimSection}>
     * @supplierCardinality 0..*
     * @clientCardinality 1
     */
    private Vector<ClaimSection> claimSections;
    private RecoveryStatus recoveryStatus;
    private boolean subrogationPotential;
    private boolean subrogationWaiver;
    private CurrencyAmount totalEstimatedRecoveryValue;
    private CurrencyAmount totalRecovered;
    private CurrencyAmount outstandingReserve;
    private CurrencyAmount estimatedReserve;
    private CurrencyAmount subrogationReserve;
    private CurrencyAmount salvageReserve;
    private CurrencyAmount deductableReserve;
    private CurrencyAmount contributionReserve;
    private boolean paid;

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..1
     * @clientCardinality 1
     */
    /* private Litigation lnkLitigation; */

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..1
     * @clientCardinality 1
     */
    /* private ADR lnkADR; */
    private boolean litigationFlag;
    private boolean adrFlag;
    private String policyID;
    private CurrencyAmount outstandingTotal;

    public Claim() {
        claimSections = new Vector<ClaimSection>();
    }

    public ClaimStatus getClaimStatus() {
        return claimStatus;
    }

    public void setClaimStatus(ClaimStatus claimStatus) {
        this.claimStatus = claimStatus;
    }

    public Collection<ClaimSection> getClaimSections() {
        return claimSections;
    }

    public void setClaimSections(Collection<ClaimSection> claimSections) {
        this.claimSections=new Vector<ClaimSection>(claimSections);
    }

    /**
     * Find a specific section in the claim by its ID.
     * @param sectionID The ID of the section to return
     * @return The section with ID==sectionID
     * @throws SectionNotFoundException If the section cannot be found.
     */
    public ClaimSection getClaimSectionByID(String sectionID) throws SectionNotFoundException {
        for(ClaimSection section: getClaimSections()) {
            if (section.getID().equals(sectionID)) {
                return section;
            }
        }
        
        throw new SectionNotFoundException("Section ID="+sectionID+" not found in claim");
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public RecoveryStatus getRecoveryStatus() {
        return recoveryStatus;
    }

    public void setRecoveryStatus(RecoveryStatus recoveryStatus) {
        this.recoveryStatus = recoveryStatus;
    }

    public boolean isSubrogationPotential() {
        return subrogationPotential;
    }

    public void setSubrogationPotential(boolean subrogationPotential) {
        this.subrogationPotential = subrogationPotential;
    }

    public boolean isSubrogationWaiver() {
        return subrogationWaiver;
    }

    public void setSubrogationWaiver(boolean subrogationWaiver) {
        this.subrogationWaiver = subrogationWaiver;
    }

    public CurrencyAmount getTotalEstimatedRecoveryValue() {
        return totalEstimatedRecoveryValue;
    }

    public void setTotalEstimatedRecoveryValue(CurrencyAmount totalEstimatedRecoveryValue) {

        this.totalEstimatedRecoveryValue = totalEstimatedRecoveryValue;
    }

    public CurrencyAmount getTotalRecovered() {
        return totalRecovered;
    }

    public void setTotalRecovered(CurrencyAmount totalRecovered) {
        this.totalRecovered = totalRecovered;
    }

    public CurrencyAmount getOutstandingReserve() {
        return outstandingReserve;
    }

    public void setOutstandingReserve(CurrencyAmount outstandingReserve) {
        this.outstandingReserve = outstandingReserve;
    }

    public CurrencyAmount getEstimatedReserve() {
        return estimatedReserve;
    }

    public void setEstimatedReserve(CurrencyAmount estimatedReserve) {
        this.estimatedReserve = estimatedReserve;
    }

    public CurrencyAmount getSubrogationReserve() {
        return subrogationReserve;
    }

    public void setSubrogationReserve(CurrencyAmount subrogationReserve) {
        this.subrogationReserve = subrogationReserve;
    }

    public CurrencyAmount getSalvageReserve() {
        return salvageReserve;
    }

    public void setSalvageReserve(CurrencyAmount salvageReserve) {
        this.salvageReserve = salvageReserve;
    }

    public CurrencyAmount getDeductableReserve() {
        return deductableReserve;
    }

    public void setDeductableReserve(CurrencyAmount deductableReserve) {
        this.deductableReserve = deductableReserve;
    }

    public CurrencyAmount getContributionReserve() {
        return contributionReserve;
    }

    public void setContributionReserve(CurrencyAmount contributionReserve) {
        this.contributionReserve = contributionReserve;
    }

    public boolean isPaid() {
        return paid;
    }

    public void setPaid(boolean paid) {
        this.paid = paid;
    }

    public boolean isLitigationFlag() {
        return litigationFlag;
    }

    public void setLitigationFlag(boolean litigationFlag) {
        this.litigationFlag = litigationFlag;
    }

    public boolean isAdrFlag() {
        return adrFlag;
    }

    public void setAdrFlag(boolean adrFlag) {
        this.adrFlag = adrFlag;
    }

    public String getPolicyID() {
        return policyID;
    }

    public void setPolicyID(String policyID) {
        this.policyID = policyID;
    }

    public CurrencyAmount getOutstandingTotal() {
        return outstandingTotal;
    }

    public void setOutstandingTotal(CurrencyAmount outstandingTotal) {
        this.outstandingTotal = outstandingTotal;
    }
}
