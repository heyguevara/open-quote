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

package com.ail.insurance.quotation.assessrisk;

import com.ail.core.Core;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/12/13 21:15:41 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessPolicyRiskArg.java,v $
 * @stereotype arg
 */
public interface AssessPolicyRiskArg extends CommandArg {
    /**
     * Fetch the value of the policy argument. $Description$
     * @see #setPolicy
     * @return value of policy
     */
    Policy getPolicyArg();

    /**
     * Set the value of the policy argument. $Description$
     * @see #getPolicy
     * @param policy New value for policy argument.
     */
    void setPolicyArg(Policy policy);

    /**
     * Fetch the value of the assessmentsheet argument. The table to which the service should add behaviours.
     * @see #setAssessmentSheet
     * @return value of assessmentsheet
     */
    AssessmentSheet getAssessmentSheetArgRet();

    /**
     * Set the value of the assessmentsheet argument. The table to which the service should add behaviours.
     * @see #getAssessmentSheet
     * @param assessmentSheet New value for assessmentsheet argument.
     */
    void setAssessmentSheetArgRet(AssessmentSheet assessmentSheet);

    /**
     * Getter for the coreArg property. Assess risk core for logging/lookups etc.
     * @return Value of coreArg, or null if it is unset
     */
    Core getCoreArg();

    /**
     * Setter for the coreArg property. * @see #getCoreArg
     * @param coreArg new value for property.
     */
    void setCoreArg(Core coreArg);
}


