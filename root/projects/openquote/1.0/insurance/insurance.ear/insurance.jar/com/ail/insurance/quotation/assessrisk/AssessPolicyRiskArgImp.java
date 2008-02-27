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
import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/12/13 21:15:41 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessPolicyRiskArgImp.java,v $
 * @stereotype argimp
 */
public class AssessPolicyRiskArgImp extends CommandArgImp implements AssessPolicyRiskArg {
    static final long serialVersionUID = 1199346453402049909L;
    private Policy policyArg;
    private AssessmentSheet assessmentSheetArgRet;

    /** Assess risk core for logging/lookups etc. */
    private Core coreArg;

    /** Default constructor */
    public AssessPolicyRiskArgImp() {
    }

    /**
     * {@inheritDoc}
     * @see #setPolicy
     * @return value of policy
     */
    public Policy getPolicyArg() {
        return this.policyArg;
    }

    /**
     * {@inheritDoc}
     * @see #getPolicy
     * @param policy New value for policy argument.
     */
    public void setPolicyArg(Policy policyArg) {
        this.policyArg = policyArg;
    }

    /**
     * {@inheritDoc}
     * @see #setAssessmentSheet
     * @return value of assessmentsheet
     */
    public AssessmentSheet getAssessmentSheetArgRet() {
        return this.assessmentSheetArgRet;
    }

    /**
     * {@inheritDoc}
     * @see #getAssessmentSheet
     * @param assessmentSheet New value for assessmentsheet argument.
     */
    public void setAssessmentSheetArgRet(AssessmentSheet assessmentSheetArgRet) {
        this.assessmentSheetArgRet = assessmentSheetArgRet;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Core getCoreArg() {
        return coreArg;
    }

    /**
     * {@inheritDoc}
     * @param coreArg @{inheritDoc}
     */
    public void setCoreArg(Core coreArg) {
        this.coreArg = coreArg;
    }
}


