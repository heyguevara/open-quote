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

import com.ail.core.Type;

import java.math.BigDecimal;
import java.util.Date;

/**
 * MoneyProvision objects represents an amount of money that is payable or due from one party to another party.
 * It may be a single payment, or a number of payments of an identical amount at a frequency.<p/> 
 * For example it may represent:-<ol>
 * <li>a single payment of &pound;30 by direct debit from account number xxxxxxx, sort code yyyyyy; or</li>
 * <li>10 payments of &pound;30 each paid monthly by Master Card with card number xxxxx, card holder yyy.</li></ol>
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2006/11/04 11:20:23 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/financial/MoneyProvision.java,v $
 * @stereotype type
 */
public class MoneyProvision extends Type {
    private static final long serialVersionUID = 1L;

    /** The method by which the payment will or has been made. */
    private PaymentMethod paymentMethod;
    
    /** The amount of money. */
    private CurrencyAmount amount;

    /** The date at which payments start. */
    private Date paymentsStartDate;

    /** The date at which payments end */
    private Date paymentsEndDate;

    /** The number of payments to be made */
    private int number;
    
    /** A textual description of the reason for the payment. */
    private String description;

    /** Payment frequency */
    private FinancialFrequency frequency;

    /**
     * Default constructor
     */
    public MoneyProvision() {
        super();
    }

    /**
     * @param number The number of payments to be made.
     * @param amount The amount of each payment (all are equal).
     * @param frequency The frequency of payments (Monthly, Weekly, etc).
     * @param paymentMethod The method e.g. payment card, direct debit, etc.
     * @param paymentsStartDate The date when payments start.
     * @param paymentsEndDate The date when payments end.
     * @param description Any textual description.
     */
    public MoneyProvision(int number, CurrencyAmount amount, FinancialFrequency frequency, PaymentMethod paymentMethod, Date paymentsStartDate, Date paymentsEndDate, String description) {
        super();
        this.paymentMethod = paymentMethod;
        this.number = number;
        this.amount = amount;
        this.paymentsStartDate = paymentsStartDate;
        this.paymentsEndDate = paymentsEndDate;
        this.frequency = frequency;
        this.description = description;
    }

    /**
     * Getter returning the value of the amount property. The amount of money.
     * @return Value of the amount property
     */
    public CurrencyAmount getAmount() {
        return amount;
    }

    /**
     * Setter to update the value of the amount property. The amount of money.
     * @param amount New value for the amount property
     */
    public void setAmount(CurrencyAmount amount) {
        this.amount = amount;
    }

    /**
     * Getter returning the value of the paymentsStartDate property. The date at which payments start.
     * @return Value of the paymentsStartDate property
     */
    public Date getPaymentsStartDate() {
        return paymentsStartDate;
    }

    /**
     * Setter to update the value of the paymentsStartDate property. The date at which payments start.
     * @param paymentsStartDate New value for the paymentsStartDate property
     */
    public void setPaymentsStartDate(Date paymentsStartDate) {
        this.paymentsStartDate = paymentsStartDate;
    }

    /**
     * Getter returning the value of the paymentsEndDate property. The date at which payments end
     * @return Value of the paymentsEndDate property
     */
    public Date getPaymentsEndDate() {
        return paymentsEndDate;
    }

    /**
     * Setter to update the value of the paymentsEndDate property. The date at which payments end
     * @param paymentsEndDate New value for the paymentsEndDate property
     */
    public void setPaymentsEndDate(Date paymentsEndDate) {
        this.paymentsEndDate = paymentsEndDate;
    }

    /**
     * Getter returning the value of the description property. A textual description of the reason for the payment.
     * @return Value of the description property
     */
    public String getDescription() {
        return description;
    }

    /**
     * Setter to update the value of the description property. A textual description of the reason for the payment.
     * @param description New value for the description property
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Getter returning the value of the frequency property. Payment frequency
     * @return Value of the frequency property
     */
    public FinancialFrequency getFrequency() {
        return frequency;
    }

    /**
     * Setter to update the value of the frequency property. Payment frequency
     * @param frequency New value for the frequency property
     */
    public void setFrequency(FinancialFrequency frequency) {
        this.frequency = frequency;
    }

    /**
     * Get the value of the frequency property as a string (as opposed to an instance of FinancialFrequency).
     * @return String representation of the frequency, or null if the property has not been set.
     */
    public String getFrequencyAsString() {
        if (frequency != null) {
            return frequency.name();
        }
        return null;
    }

    /**
     * Set the frequency property as a String. The String must represents a valid
     * FinancialFrequency. i.e. it must be suitable for a call to FinancialFrequency.forName().
     * @param frequency New value for property.
     * @throws IndexOutOfBoundsException If frequency is not a valid FinancialFrequency.
     */
    public void setFrequencyAsString(String frequency) throws IndexOutOfBoundsException {
        this.frequency = FinancialFrequency.valueOf(frequency);
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    /**
     * Calculate the total value of this provision assuming that all payments were made.
     * @return amount * number
     */
    public CurrencyAmount calculateTotal() {
        BigDecimal tot=amount.getAmount().multiply(new BigDecimal(number));
        return new CurrencyAmount(tot, amount.getCurrency());
    }
}
