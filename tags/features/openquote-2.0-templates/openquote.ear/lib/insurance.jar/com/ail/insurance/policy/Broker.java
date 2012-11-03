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
package com.ail.insurance.policy;

import com.ail.annotation.TypeDefinition;
import com.ail.party.Organisation;

/**
 * Type represents a broker who owns products in the system. It acts as a repository for
 * all the contact information (etc) which is needed to identify the organisation, and
 * defines values for various fields on the UI.
 */
@TypeDefinition
public class Broker extends Organisation {
	private static final long serialVersionUID = -4521508279619758949L;
	/** Email address to which clients (proposers) can send emails */
    private String emailAddress;
    /** General phone number to be used where a more specific one isn't known */
    private String telephoneNumber;
    /** Trading name if different from legal name. Leave as null if the two are the same */
    private String tradingName;
    /** Direct debit ID number if direct debit payments are to be received */
    private String directDebitIdentificationNumber;
    /** Queries about quotes should be directed here. If null, telephoneNumber is used. */
    private String quoteTelephoneNumber;
    /** Queries about payments should be directed here. If null, telephoneNumber is used. */
    private String paymentTelephoneNumber;
    /** The system sends quotation emails to this address (for system use, not for general emails). */
    private String quoteEmailAddress;
    /** The name of an individual in the organisation */
    private String contactName;
    /** Number to call in the event of a claim If null, telephoneNumber is used. */
    private String claimTelephoneNumber;
    
	public Broker() {
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getDirectDebitIdentificationNumber() {
        return directDebitIdentificationNumber;
    }

    public void setDirectDebitIdentificationNumber(String directDebitIdentificationNumber) {
        this.directDebitIdentificationNumber = directDebitIdentificationNumber;
    }

    public String getPaymentTelephoneNumber() {
        return paymentTelephoneNumber!=null ? paymentTelephoneNumber : telephoneNumber;
    }

    public void setPaymentTelephoneNumber(String paymentTelephoneNumber) {
        this.paymentTelephoneNumber = paymentTelephoneNumber;
    }

    public String getQuoteEmailAddress() {
        return quoteEmailAddress;
    }

    public void setQuoteEmailAddress(String quoteEmailAddress) {
        this.quoteEmailAddress = quoteEmailAddress;
    }

    public String getQuoteTelephoneNumber() {
        return quoteTelephoneNumber !=null ? quoteTelephoneNumber : telephoneNumber;
    }

    public void setQuoteTelephoneNumber(String quoteTelephoneNumber) {
        this.quoteTelephoneNumber = quoteTelephoneNumber;
    }

    public String getTradingName() {
        if (tradingName==null) {
            return getLegalName();
        }

        return tradingName;
    }

    public void setTradingName(String tradingName) {
        this.tradingName = tradingName;
    }

    public String getClaimTelephoneNumber() {
        return claimTelephoneNumber!=null ? claimTelephoneNumber : telephoneNumber;
    }

    public void setClaimTelephoneNumber(String claimTelephoneNumber) {
        this.claimTelephoneNumber = claimTelephoneNumber;
    }
}
