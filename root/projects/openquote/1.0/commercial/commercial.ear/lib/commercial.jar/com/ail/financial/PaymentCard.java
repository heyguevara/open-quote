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

import java.util.Date;

/**
 * Represents the details of a payment card. Payment card encompasses both credit and debit cards.
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/05/21 12:41:18 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/financial/PaymentCard.java,v $
 * @stereotype type
 */
public class PaymentCard extends PaymentMethod {
    private static final long serialVersionUID = 1L;

    /** The type of card (issuer) */
    private CardIssuer issuer;
    
    /** The credit card's number */
    private String cardNumber;

    /** The card holder's name as it appears on the card */
    private String cardHoldersName;

    /** The card's issues number (optional based on card type) */
    private String issueNumber;

    /** Date (month & year only) when the card expirs */
    private Date expiryDate;
    
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

    /** The security code which appears on the back of the card (optional based on card type) */
    private String securityCode;

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
}
