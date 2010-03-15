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

import com.ail.core.Locale;
import com.ail.core.Type;
import com.ail.util.Rate;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Instances of this class represent amounts of money.
 * todo This currently assumes a BigDecimal scale of 2 for all currencies.
 */
public class CurrencyAmount extends Type {
    static final long serialVersionUID = -7610940646036255844L;
    private static DecimalFormat format=new DecimalFormat("#0.00");
    private BigDecimal amount=null;
    private Currency currency=null;

    /**
     * Default constructor. This constructor is provided to use by frameworks
     * (like castor) that demand a default constructor. Its use from code is
     * discouraged as it creates an instance that is unusable until the
     * setters for amount and currency have been invoked.
     */
    public CurrencyAmount() {
    }

    /**
     * Constructor
     * @param amount The amount
     * @param currency The currency
     */
    public CurrencyAmount(BigDecimal amount, Currency currency) {
        amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        setAmount(amount);
        setCurrency(currency);
    }

    /**
     * Constructor
     * @param amount The amount
     * @param currency The currency
     */
    public CurrencyAmount(String amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    /**
     * Constructor
     * @param amount The amount
     * @param currency The currency.
     */
    public CurrencyAmount(double amount, Currency currency) {
        this(new BigDecimal(amount), currency);
    }

    /**
     * Initialise ourself based on another currency instance
     * @param that The instance to base ourself on.
     */
    private void autoCreate(CurrencyAmount that) {
        if (currency==null) {
            currency=that.currency;
            amount=new BigDecimal(0);
        }
    }

    /**
     * Get the amount represented
     * @return the amount.
     */
    public BigDecimal getAmount() {
        return amount;
    }

    /**
     * Set the amount property.
     * @param amount
     */
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    /**
     * Set the amount property from a string.
     * @throws NumberFormatException If string does not represent a valid decimal amount.
     * @param amount
     */
    public void setAmountAsString(String amount) throws NumberFormatException {
        this.amount=new BigDecimal(amount);
        this.amount.setScale(2);
    }

    public String getAmountAsString() {
        return format.format(amount.doubleValue());
    }

    /**
     * Subtract another currency amount from this one, saving the result in this one.
     * @throws IllegalArgumentException If the currency types does not match.
     * @param that Currency amount to subtract
     */
    public void subtract(CurrencyAmount that) throws IllegalArgumentException {
        autoCreate(that);

        if (this.currency.equals(that.currency)) {
            setAmount(amount.subtract(that.amount));
        }
        else {
            throw new IllegalArgumentException("Currencies do not match");
        }
    }

    /**
     * Add another currency amount to this one, saving the result in this one.
     * @throws IllegalArgumentException If the curerncies do not match.
     * @param that Currenct amount to add
     */
    public void add(CurrencyAmount that) throws IllegalArgumentException {
        autoCreate(that);

        if (this.currency.equals(that.currency)) {
            setAmount(amount.add(that.amount));
        }
        else {
            throw new IllegalArgumentException("Currencies do not match");
        }
    }

    /**
     * Compare the value of 'that' currency amount with this. The two amounts
     * must represent the same currency.
     * @param that Currency amount to compare with.
     * @throws IllegalArgumentException if the currencies do not match.
     * @return true if 'that' is less than this.
     */
    public boolean lessThan(CurrencyAmount that) {
        return lessThan(that.getAmount().doubleValue(), that.getCurrency());
    }

    /**
     * Compare the value of 'that' currency amount with this. The two amounts
     * must represent the same currency.
     * @param that Currency amount to compare with.
     * @throws IllegalArgumentException if the currencies do not match.
     * @return true if 'that' is greater than than this.
     */
    public boolean greaterThan(CurrencyAmount that) {
        return greaterThan(that.getAmount().doubleValue(), that.getCurrency());
    }

    /**
     * Compare the value of <code>amount</code> in <code>currency</code> with the amount represented by
     * <code>this</code>. The two currencies must be the same.
     * @param amount Amount to compare against.
     * @param currency The currency <code>amount</code> is in.
     * @throws IllegalArgumentException if the currencies do not match.
     * @return true if the amount represented by <code>amount</code> and <code>currency</code> is less than than this.
     */
    public boolean lessThan(double amount, Currency currency) {
        if (this.currency.equals(currency)) {
            return (this.amount.doubleValue() < amount);
        }
        else {
            throw new IllegalArgumentException("Currency type missmatch: "+currency.toString()+" and "+this.currency.toString());
        }
    }

    /**
     * Compare the value of <code>amount</code> in <code>currency</code> with the amount represented by
     * <code>this</code>. The two currencies must be the same.
     * @param amount Amount to compare against.
     * @param currency The currency <code>amount</code> is in.
     * @throws IllegalArgumentException if the currencies do not match.
     * @return true if the amount represented by <code>amount</code> and <code>currency</code> is greater than than this.
     */
    public boolean greaterThan(double amount, Currency currency) {
        if (this.currency.equals(currency)) {
            return (this.amount.doubleValue() > amount);
        }
        else {
            throw new IllegalArgumentException("Currency type missmatch: "+currency.toString()+" and "+this.currency.toString());
        }
    }

    /**
     * Set the currency - private because it doesn't make sense for a currency amount to have
     * its currency change, unless some calculation is involved. This calculation is beyone the
     * scope of this value object.
     * @param currency
     */
    private void setCurrency(Currency currency) {
        this.currency = currency;
    }

    /**
     * Set the currency based on a String representation of a currency.
     * @param currency
     * @throws IndexOutOfBoundsException
     */
    public void setCurrencyAsString(String currency) throws IndexOutOfBoundsException {
        this.currency=Currency.valueOf(currency);
    }

    /**
     * Get the amount's currency
     * @return currency
     */
    public Currency getCurrency() {
        return currency;
    }

    /**
     * Get the currency as a string.
     * @return Currency as a string
     */
    public String getCurrencyAsString() {
        if (currency!=null) {
            return currency.toString();
        }
        else {
            return null;
        }
    }

    /**
     * Get the String representation of this amount.
     * @return
     */
    public String toString() {
        return getAmountAsString()+" "+currency;
    }
    
    /**
     * Get the value as a string in the format best suited for the current thread's locale.
     * The actual currency symbol used and the number format depend on the locale.
     * @return Formatted string
     */
    public String toFormattedString() {
        NumberFormat fmt=NumberFormat.getCurrencyInstance(Locale.getThreadLocale());
        fmt.setCurrency(java.util.Currency.getInstance(getCurrencyAsString()));
        return fmt.format(amount);
    }

    /**
     * Apply a percentage to this currency amount.
     * @param rate Percentage to apply.
     */
    public void apply(Rate rate) {
        amount=rate.applyTo(amount, 2, BigDecimal.ROUND_HALF_UP);
        amount.setScale(2, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * Clone this. Most types can rely on Type's clone method to handle this,
     * but because we want to keep setCurrency() private, we have to handle 
     * the method here.
     * @return Exact copy of this.
     * @throws CloneNotSupportedException If the clone fails
     */
    public Object clone() throws CloneNotSupportedException {
        CurrencyAmount clone=new CurrencyAmount(this.amount, this.currency);

        return clone;
    }

    /**
     * Equality checker
     * @param that Amount to check equality with
     * @return true if this has the same currency and value as that.
     */
    public boolean equals(Object obj) {
        if (obj instanceof CurrencyAmount) {
            CurrencyAmount that=(CurrencyAmount)obj;
            return this.currency.equals(that.currency) && this.amount.equals(that.amount);
        }
        else {
            return false;
        }            
    }
}
