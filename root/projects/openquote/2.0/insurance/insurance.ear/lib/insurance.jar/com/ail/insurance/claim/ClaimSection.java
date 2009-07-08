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

/**
 * @stereotype type
 */
public class ClaimSection extends Type {
    private static final long serialVersionUID = -209668224910237416L;

    private String ID;

    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @clientCardinality 1 
     */

    private Vector<Recovery> recoveries=null;
    private String policySectionID;
    private CurrencyAmount estimatedReserve;
    private CurrencyAmount totalRecovered;
    private CurrencyAmount outstandingClaim;

    /**
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 1..* 
     */
    /*private Recovery lnkRecovery;*/

    /**
     * Default constructor
     */
    public ClaimSection() {
        recoveries=new Vector<Recovery>();
    }

    public String getID(){ return ID; }

    public void setID(String ID){ this.ID = ID; }

    public String getPolicySectionID(){ return policySectionID; }

    public void setPolicySectionID(String policySectionID){ this.policySectionID = policySectionID; }

    public CurrencyAmount getEstimatedReserve(){ return estimatedReserve; }

    public void setEstimatedReserve(CurrencyAmount estimatedReserve){ this.estimatedReserve = estimatedReserve; }

    public CurrencyAmount getTotalRecovered(){ return totalRecovered; }

    public void setTotalRecovered(CurrencyAmount totalRecovered){ this.totalRecovered = totalRecovered; }

    public CurrencyAmount getOutstandingClaim(){ return outstandingClaim; }

    public void setOutstandingClaim(CurrencyAmount outstandingClaim){ this.outstandingClaim = outstandingClaim; }

    public Collection<Recovery> getRecoveries() {
        return recoveries;
    }

    public void setRecoveries(Collection<Recovery> recoveries) {
        this.recoveries=new Vector<Recovery>(recoveries);
    }

    public void addRecovery(Recovery recovery) {
        recoveries.add(recovery);
    }
}
