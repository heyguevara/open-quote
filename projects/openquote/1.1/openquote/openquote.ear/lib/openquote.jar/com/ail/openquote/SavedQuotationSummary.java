/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.openquote;

import java.util.Date;

import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.PolicyStatus;

/**
 * This class is mapped to the database to represent a summary of each persisted quotation. In
 * effect it defines the fields broken out of the quotation object graph to allow for searching
 * and reporting.
 */
public class SavedQuotationSummary extends Type {
    private static final long serialVersionUID = 8924615006523668514L;

    private String quotationNumber;
    private PolicyStatus status;
    private String page; // the name of the page (in the page flow) that the user was on when this quote was saved
    private Date quotationDate;
    private Date quotationExpiryDate;
    private String username;
    private String product;
    private CurrencyAmount premium;
    private boolean userSaved; // true if the user requested that this quote be saved
    private boolean testCase; 
    
    public SavedQuotationSummary() {
    }

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public Date getQuotationDate() {
        return quotationDate;
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    public Date getQuotationExpiryDate() {
        return quotationExpiryDate;
    }

    public void setQuotationExpiryDate(Date quotationExpiryDate) {
        this.quotationExpiryDate = quotationExpiryDate;
    }

    public String getQuotationNumber() {
        return quotationNumber;
    }

    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    public PolicyStatus getStatus() {
        return status;
    }

    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    public String getStatusAsString() {
        return status.toString();
    }
    
    public void setStatusAsString(String status) {
        this.status=PolicyStatus.forName(status);
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public CurrencyAmount getPremium() {
        return premium;
    }

    public void setPremium(CurrencyAmount premium) {
        this.premium = premium;
    }

    public boolean isUserSaved() {
        return userSaved;
    }

    public void setUserSaved(boolean userSaved) {
        this.userSaved = userSaved;
    }

    public boolean isTestCase() {
        return testCase;
    }

    public void setTestCase(boolean testCase) {
        this.testCase = testCase;
    }
}
