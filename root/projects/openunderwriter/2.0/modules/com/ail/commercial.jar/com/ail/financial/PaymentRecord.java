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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.ail.core.Type;

/**
 * A PaymentRecord represents a single event in a payment history. This type is
 * immutable. Events record the processing of payments in terms of the placement
 * of the original payment request (with PayPal for example), the approval or
 * cancellation of that request. Each element details the amount involved, the
 * date of the event, the type of event.
 */
public class PaymentRecord extends Type {
    private static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSSZ";
    CurrencyAmount amount;
    Date date;
    PaymentRecordType type;
    String methodIdentifier;
    String transactionReference;
    String description;

    public PaymentRecord(String amountAsString, String currencyAsString, String transactionReference, String methodIdentifer, String type, String dateAsString) throws ParseException {
        this.amount = new CurrencyAmount(amountAsString, currencyAsString);
        this.transactionReference = transactionReference;
        this.methodIdentifier = methodIdentifer;
        this.type = PaymentRecordType.valueOf(type);
        this.date = new SimpleDateFormat(DATE_FORMAT).parse(dateAsString);
    }

    public PaymentRecord(CurrencyAmount currencyAmount, String transactionReference, String methodIdentifer, PaymentRecordType type, Date date) {
        this.transactionReference = transactionReference;
        this.amount = currencyAmount;
        this.methodIdentifier = methodIdentifer;
        this.type = type;
        this.date = date;
    }

    public PaymentRecord(CurrencyAmount currencyAmount, String transactionReference, PaymentMethod paymentMethod, PaymentRecordType type, String description) {
        this(currencyAmount, transactionReference, paymentMethod.getId(), type, new Date());
        this.description=description;
    }

    public PaymentRecord(CurrencyAmount currencyAmount, String transactionReference, PaymentMethod paymentMethod, PaymentRecordType type) {
        this(currencyAmount, transactionReference, paymentMethod.getId(), type, new Date());
    }

    public CurrencyAmount getAmount() {
        return amount;
    }

    public Date getDate() {
        return date;
    }

    public PaymentRecordType getType() {
        return type;
    }

    public String getMethodIdentifier() {
        return methodIdentifier;
    }

    public String getTransactionReference() {
        return transactionReference;
    }

    public String getAmountAsString() {
        return amount.getAmountAsString();
    }

    public String getCurrencyAsString() {
        return amount.getCurrencyAsString();
    }

    public String getDateAsString() {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    public String getTypeAsString() {
        return type.getName();
    }
    
    public String getDescription() {
        return description;
    }
}
