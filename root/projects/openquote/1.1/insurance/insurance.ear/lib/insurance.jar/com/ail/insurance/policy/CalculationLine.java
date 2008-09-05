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

package com.ail.insurance.policy;

import com.ail.financial.CurrencyAmount;

/**
 * A Calculation Line is an assessment line which contains a calculated element.
 */
public abstract class CalculationLine extends AssessmentLine {
    private static final long serialVersionUID = 8951155134050544922L;
    private String contributesTo;
    private CurrencyAmount amount;

    /**
     * Default constructor
     */
    public CalculationLine() {
    }

    /**
     * Constructor
     * @param id Id for this calculation line
     * @param reason Free text reason for the calculation line.
     * @param relatesTo Optional reference to the part of the policy that cause the calculation line.
     * @param contributesTo The id of the line that this one will contribute to.
     * @param amount The amount (value) of this line.
     */
    public CalculationLine(String id, String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount) {
        super(id, reason, relatesTo);
        this.contributesTo=contributesTo;
        this.amount=amount;
    }

    /**
     * Constructor
     * @param id Id for this calculation line
     * @param reason Free text reason for the calculation line.
     * @param relatesTo Optional reference to the part of the policy that cause the calculation line.
     * @param contributesTo The id of the line that this one will contribute to.
     * @param amount The amount (value) of this line.
     * @param priority The priority of this line wrt other lines in the same sheet (lines with higher priority values are processed first)
     */
    public CalculationLine(String id, String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount, int priority) {
        super(id, reason, relatesTo, priority);
        this.contributesTo=contributesTo;
        this.amount=amount;
    }

    public String getContributesTo() {
        return contributesTo;
    }

    public void setContributesTo(String contributesTo) {
        this.contributesTo = contributesTo;
    }

    /**
     * Process this line's calculation
     * @param sheets The list of assessment sheets currently being processed.
     * @param sheet The sheet this line is part of.
     * @return true if the line gets processed, false otherwise.
     */
    public abstract boolean calculate(AssessmentSheetList sheets, AssessmentSheet sheet);

    public CurrencyAmount getAmount() {
        return amount;
    }

    public void setAmount(CurrencyAmount amount) {
        this.amount = amount;
    }

    /**
     * Return this line's amount in String format (e.g."421.20 GBP")
     * @return Line's amount as a string, or "" if the line's amount is null.
     */
    public String getAmountAsString() {
        return (amount==null) ? "" : amount.getAmountAsString()+" "+amount.getCurrencyAsString();
    }
}
