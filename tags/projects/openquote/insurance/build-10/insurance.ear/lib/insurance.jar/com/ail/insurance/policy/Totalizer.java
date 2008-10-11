/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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

import java.util.StringTokenizer;

import com.ail.financial.CurrencyAmount;

/**
 * The totalizer calculation line adds any number of other named lines up to make a
 * total, and optionally contributes that total to another line.
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2007/02/18 16:50:43 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Totalizer.java,v $
 * @stereotype type
 */
public class Totalizer extends CalculationLine {
    private static final long serialVersionUID = 1048697316607294623L;
    private String dependsOn=null;

    public Totalizer() {
    }

    /**
     * @param id This line's Id
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param dependsOn A comma seperated list of the IDs of the lines that this one depends on (will sum).
     */
    public Totalizer(String id, String reason, Reference relatesTo, String contributesTo, String dependsOn) {
        super(id, reason, relatesTo, contributesTo, null);
        this.dependsOn=dependsOn;
    }

    /**
     * @param id This line's Id
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param dependsOn A comma seperated list of the IDs of the lines that this one depends on (will sum).
     * @param priority The priority of this line wrt other lines in the same sheet (lines with higher priority values are processed first)
     */
    public Totalizer(String id, String reason, Reference relatesTo, String contributesTo, String dependsOn, int priority) {
      super(id, reason, relatesTo, contributesTo, null, priority);
      this.dependsOn=dependsOn;
    }

    /**
     * Get the Ids of the lines that this totalizer depends on. The Ids
     * are returned as a comma seperated list.
     * @return List of the IDs of the lines that this line depends on.
     */
    public String getDependsOn() {
        return dependsOn;
    }

    /**
     * Set the Ids of the lines that this totalizer depends on. The Ids
     * are passed as a comma seperated list.
     * @param dependsOn List of the IDs of the lines that this line depends on.
     */
    public void setDependsOn(String dependsOn) {
        this.dependsOn = dependsOn;
    }

    public boolean calculate(AssessmentSheetList sheets, AssessmentSheet sheet) {
        CurrencyAmount total=new CurrencyAmount();
        CalculationLine cl=null;

        // loop through all the named dependsOn lines, and add their values to the total
        for(StringTokenizer st=new StringTokenizer(dependsOn, ",") ; st.hasMoreTokens() ; ) {
            cl=(CalculationLine)sheets.findAssessmentLine(st.nextToken(), sheet);

            if (cl==null || cl.getAmount()==null) {
                return false;
            }

            total.add(cl.getAmount());
        }

        // no baseCurrency means no lines found - could be an empty dependsOn?
        if (total.getCurrency()==null) {
            return false;
        }

        // set the amount base on the values just parsed
        setAmount(total);

        // if this line contributes to another...
        if (getContributesTo()!=null) {
            // try to get the line that this on contribites to.
            FixedSum conTo=(FixedSum)sheets.findAssessmentLine(getContributesTo(), sheet);

            // if it doesn't exist yet, create it.
            if (conTo==null) {
                conTo=new FixedSum(getContributesTo(), "calculated", null, null, new CurrencyAmount(total.getAmount(), total.getCurrency()));
                sheets.addAssessmentLine(conTo, sheet);
            }
            else {
                conTo.getAmount().add(getAmount());
            }
        }

        return true;
    }
}
