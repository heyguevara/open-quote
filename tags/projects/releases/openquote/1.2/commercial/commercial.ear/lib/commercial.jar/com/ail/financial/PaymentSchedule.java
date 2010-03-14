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

import java.util.ArrayList;
import java.util.List;

import com.ail.core.Type;

/**
 * A payment schedule simply groups together a collection of {@link com.ail.financial.MoneyProvision MoneyProvisions}.</p>
 * For example, a schedule might define:-<ol>
 * <li>A single payment of &pound;30 to be made by direct debit; or</li>
 * <li>A payment of &pound;30 to be made by Master Card, followed by 10 monthly payments of &pound;43 to be made by direct debit.</li>
 * </ol>
 */
public class PaymentSchedule extends Type {
    private static final long serialVersionUID = 1L;

    private List<MoneyProvision> moneyProvision;
    
    /** A textual description of the reason for the payment. */
    private String description;

    /**
     * @param moneyProvision List of money provisions
     * @param description Textual description
     */
    public PaymentSchedule(List<MoneyProvision> moneyProvision, String description) {
        super();
        this.moneyProvision = moneyProvision;
        this.description = description;
    }

    /**
     * Constructor 
     */
    public PaymentSchedule() {
        moneyProvision=new ArrayList<MoneyProvision>();
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
     * Fetch the list of money provisions associated with this schedule. This list will never
     * be null.
     * @return List of money provisions.
     */
    public List<MoneyProvision> getMoneyProvision() {
        return moneyProvision;
    }

    /**
     * Set the list of money provisions associated with this schedule.
     * @param moneyProvision
     */
    public void setMoneyProvision(ArrayList<MoneyProvision> moneyProvision) {
        if (moneyProvision==null) {
            moneyProvision=new ArrayList<MoneyProvision>();
        }
        else {
            this.moneyProvision = moneyProvision;
        }
    }
    
    public CurrencyAmount calculateTotal() {
        CurrencyAmount total=new CurrencyAmount();
        
        for(MoneyProvision prov: getMoneyProvision()) {
            total.add(prov.calculateTotal());
        }
        
        return total;
    }
}
