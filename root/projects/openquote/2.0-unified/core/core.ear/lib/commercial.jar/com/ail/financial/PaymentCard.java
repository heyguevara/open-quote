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

package com.ail.financial;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ail.annotation.TypeDefinition;

/**
 * Represents the details of a payment card. Payment card encompasses both credit and debit cards.
 */
@TypeDefinition
public class PaymentCard extends PaymentMethod {
    private static final long serialVersionUID = 1L;
    private DateFormat dateFormatter=new SimpleDateFormat("MM/yy");

    /** The type of card (issuer) */
    private CardIssuer issuer;
    
    /** The credit card's number */
    private String cardNumber;

    /** The card holder's name as it appears on the card */
    private String cardHoldersName;

    /** The card's issues number (optional based on card type) */
    private String issueNumber;

    /** Date (month & year only) when the card expires */
    private Date expiryDate;
    
    /** Date (month & year only) when the card starts */
    private Date startDate;
    
    /** The security code which appears on the back of the card (optional based on card type) */
    private String securityCode;
    
    public PaymentCard() {
        super();
    }

    public PaymentCard(CardIssuer issuer, String cardNumber, String cardHoldersName, String issueNumber, String securityCode) {
        super();
        this.issuer = issuer;
        this.cardNumber = cardNumber;
        this.cardHoldersName = cardHoldersName;
        this.issueNumber = issueNumber;
        this.securityCode = securityCode;
    }

    public String getCardHoldersName() {
        return cardHoldersName;
    }

    public void setCardHoldersName(String cardHoldersName) {
        this.cardHoldersName = cardHoldersName;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }
    
    /**
     * Get the card number with the first part masked for security. This
     * will return a string in the form '**** **** **** 1234"
     * @return masked card number
     */
    public String getMaskedCardNumber() {
        if (cardNumber!=null && cardNumber.length() > 4) {
            return "**** **** **** "+cardNumber.substring(cardNumber.length()-4);
        }
        else {
            return "**** **** **** ****";            
        }
    }

    public String getIssueNumber() {
        return issueNumber;
    }

    public void setIssueNumber(String issueNumber) {
        this.issueNumber = issueNumber;
    }

    public String getSecurityCode() {
        return securityCode;
    }

    public void setSecurityCode(String securityCode) {
        this.securityCode = securityCode;
    }

    public CardIssuer getIssuer() {
        return issuer;
    }

    public void setIssuer(CardIssuer issuer) {
        this.issuer = issuer;
    }

    public String getIssuerAsString() {
        if (issuer!=null) {
            return issuer.toString();
        }
        else {
            return null;
        }
    }
    
    public void setIssuerAsString(String issuer) throws IndexOutOfBoundsException {
        this.issuer=CardIssuer.valueOf(issuer);
    }

    public Date getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    /**
     * Fetch a formatted string representation of the expiry date. This method
     * will return a string in the format "MM/yy"
     * @return formatted date, or an empty string if expiryDate is null
     */
    public String getFormattedExpiryDate() {
        if (expiryDate==null) {
            return "";
        }
        
        return dateFormatter.format(expiryDate);
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Fetch a formatted string representation of the start date. This method
     * will return a string in the format "MM/yy"
     * @return formatted date, or an empty string if expiryDate is null
     */
    public String getFormattedStartDate() {
        if (startDate==null) {
            return "";
        }

        return dateFormatter.format(startDate);
    }
}
