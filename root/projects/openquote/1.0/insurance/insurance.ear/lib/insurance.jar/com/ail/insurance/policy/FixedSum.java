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
 * A type of assessment line representing a fixed amount (as opposed to one that is calculated by applying a rate to
 * another line).
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/02/18 16:50:43 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/FixedSum.java,v $
 * @stereotype type
 */
public class FixedSum extends CalculationLine {
    private static final long serialVersionUID = 399954132621176151L;

    /**
     * Default constructor
     */
    public FixedSum() {
    }

    /**
     * Constructor
     * @param id This line's Id
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param amount The amount to be contributed.
     */
    public FixedSum(String id, String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount) {
        super(id, reason, relatesTo, contributesTo, amount);
    }

    /**
     * Constructor
     * @param id This line's Id
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param amount The amount to be contributed.
     * @param priority The priority of this line wrt other lines in the same sheet (lines with higher priority values are processed first)
     */
    public FixedSum(String id, String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount, int priority) {
      super(id, reason, relatesTo, contributesTo, amount, priority);
    }

    /**
     * A FixedSum can only either contribute to another FixedSum, or do nothing.
     * @param sheets All the sheets in the current process.
     * @param sheet The sheet that this line is part of.
     * @return true - always
     */
    public boolean calculate(AssessmentSheetList sheets, AssessmentSheet sheet) {
        // Do we contribute to anything?
        if (getContributesTo()!=null) {
            // yes, then does the line we contribute to exist?
            FixedSum conTo=(FixedSum)sheets.findAssessmentLine(getContributesTo(), sheet);

            // if not, create it.
            if (conTo==null) {
                conTo=new FixedSum(getContributesTo(), "calculated", null, null, new CurrencyAmount(0, getAmount().getCurrency()));
                sheets.addAssessmentLine(conTo, sheet);
            }

            // add to the 'contributeTo' line.
            conTo.getAmount().add(getAmount());
        }

        return true;
    }
}
