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
import com.ail.util.Rate;

/**
 * An assessment line which applies a rate behaviour - i.e. it applies a rate to the value of another line and 
 * optionally contributes the result to another line.
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2007/02/18 16:50:43 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/RateBehaviour.java,v $
 * @stereotype type
 */
public class RateBehaviour extends Behaviour {
    private static final long serialVersionUID = -1646835026437040884L;
    private String dependsOn=null;
    private Rate rate=null;

    /**
     * Default constructor
     */
    public RateBehaviour() {
    }

    /**
     * Constructor
     * @param id The Id to use for this line
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one cntributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param type Behaviour type (Load or Discount)
     * @param rate The rate to be used in the calculation.
     */
    public RateBehaviour(String id, String reason, Reference relatesTo, String contributesTo, String dependsOn, BehaviourType type, Rate rate) {
        super(id, reason, relatesTo, contributesTo, type);
        this.dependsOn=dependsOn;
        this.rate=rate;
    }

    /**
     * Constructor
     * @param id The Id to use for this line
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one cntributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param type Behaviour type (Load or Discount)
     * @param rate The rate to be used in the calculation.
     * @param priority The priority of this line wrt other lines in the same sheet (lines with higher priority values are processed first)
     */
    public RateBehaviour(String id, String reason, Reference relatesTo, String contributesTo, String dependsOn, BehaviourType type, Rate rate, int priority) {
        super(id, reason, relatesTo, contributesTo, type, priority);
        this.dependsOn=dependsOn;
        this.rate=rate;
    }

    /**
     * Get the name of the assessment sheet line that this line depends on.
     * @return Name of an assessment sheet line
     */
    public String getDependsOn() {
        return dependsOn;
    }

    /**
     * Set the name of the assessment sheet line that this line depends on.
     * @param dependsOn Name of an assessment sheet line
     */
    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    /**
     * Get the Rate for this line. This is the rate that this line will apply to the
     * value of the 'dependsOn' line to derive its own value.
     * @return This line's rate
     */
    public Rate getRate() {
        return rate;
    }

    /**
     * Set the Rate for this line. This is the rate that this line will apply to the
     * value of the 'dependsOn' line to derive its own value.
     * @param rate This line's rate
     */
    public void setRate(Rate rate) {
        this.rate = rate;
    }

    /**
     * Perform this lines calculation.
     * @param sheets The sheets being evaluated
     * @param sheet The sheet that this line is part of.
     * @return true if the line is successfully evaluated, false otherwise.
     */
    public boolean calculate(AssessmentSheetList sheets, AssessmentSheet sheet) {
        // try to get the line that this one depends on
        FixedSum depOn=(FixedSum)sheets.findAssessmentLine(getDependsOn(), sheet);

        // If the line doesn't exist (yet) give up
        if (depOn==null) {
            return false;
        }

        // Copy the currency amount from the line, and apply the rate to it
        CurrencyAmount amnt=new CurrencyAmount(depOn.getAmount().getAmount(), depOn.getAmount().getCurrency());
        amnt.apply(rate);

        // A line may not contribute to anything. For example, tax may have been included in the
        // base rates - but the tax line still needs to appear in the assessment sheet. In this case
        // the contributesTo will be null.
        if (getContributesTo()!=null) {
            // try to get the line that this one contribites to.
            FixedSum conTo=(FixedSum)sheets.findAssessmentLine(getContributesTo(), sheet);

            // if it doesn't exist, create it.
            if (conTo==null) {
                conTo=new FixedSum(getContributesTo(), "calculated", null, null, new CurrencyAmount(0, amnt.getCurrency()));
                sheets.addAssessmentLine(conTo, sheet);
            }

            // If we're loading add our calculated amount to contributed to,
            // if we are a discount subtract it.
            if (getType().equals(BehaviourType.LOAD)) {
                conTo.getAmount().add(amnt);
            }
            else if (getType().equals(BehaviourType.TAX)) {
                conTo.getAmount().add(amnt);
            }
            else if (getType().equals(BehaviourType.COMMISSION)) {
                conTo.getAmount().add(amnt);
            }
            else if (getType().equals(BehaviourType.BROKERAGE)) {
                conTo.getAmount().add(amnt);
            }
            else if (getType().equals(BehaviourType.MANAGEMENT_CHARGE)) {
                conTo.getAmount().add(amnt);
            }
            else if (getType().equals(BehaviourType.DISCOUNT)) {
                conTo.getAmount().subtract(amnt);
            }
        }

        // set this lines amount now we have it.
        setAmount(amnt);

        return true;
    }
}
