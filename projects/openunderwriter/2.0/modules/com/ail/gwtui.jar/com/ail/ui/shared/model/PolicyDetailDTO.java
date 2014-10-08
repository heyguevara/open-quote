/* Copyright Applied Industrial Logic Limited 2014. All rights Reserved */
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
package com.ail.ui.shared.model;

import java.io.Serializable;
import java.util.List;

public class PolicyDetailDTO implements Serializable {

    private static final long serialVersionUID = 1L;
    
    private String policyNumber;
    
    private String quotationNumber;
    
    private String policyHolderName;
    
    private List<String> policyHolderAddress;
    
    private String quoteDate;
    
    private String expiryDate;
    
    private String product;
    
    private String premium;
    
    private boolean initialised;
    
    public PolicyDetailDTO() {
    }

    public PolicyDetailDTO(String policyNumber, 
                            String quotationNumber, 
                            String policyHolderName,
                            List<String> policyHolderAddress,
                            String quoteDate, 
                            String expiryDate, 
                            String product, 
                            String premium) {
        
        this.policyNumber = policyNumber;
        this.quotationNumber = quotationNumber;
        this.policyHolderName = policyHolderName;
        this.policyHolderAddress = policyHolderAddress;
        this.quoteDate = quoteDate;
        this.expiryDate = expiryDate;
        this.product = product;
        this.premium = premium;
        this.initialised = true;
    }

    public String getPolicyNumber() {
        return policyNumber;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public String getPolicyHolderName() {
        return policyHolderName;
    }

    public List<String> getPolicyHolderAddress() {
        return policyHolderAddress;
    }

    public String getQuoteDate() {
        return quoteDate;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getProduct() {
        return product;
    }

    public String getPremium() {
        return premium;
    }

    public boolean isInitialised() {
        return initialised;
    }
    
    
}
