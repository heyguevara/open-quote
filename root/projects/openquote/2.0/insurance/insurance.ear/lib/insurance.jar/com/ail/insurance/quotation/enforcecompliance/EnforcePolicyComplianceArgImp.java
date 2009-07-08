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

package com.ail.insurance.quotation.enforcecompliance;

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 23:19:08 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/enforcecompliance/EnforcePolicyComplianceArgImp.java,v $
 * @stereotype argimp
 */
public class EnforcePolicyComplianceArgImp extends CommandArgImp implements EnforcePolicyComplianceArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** The policy that brokerage is being calculate for */
    private Policy policyArg;

    /** The assessment sheet to which brokerage calculations are to be added. */
    private AssessmentSheet assessmentSheetArgRet;

    /** Default constructor */
    public EnforcePolicyComplianceArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArg() {
        return policyArg;
    }

    /**
     * {@inheritDoc}
     * @param policyArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        this.policyArg = policyArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public AssessmentSheet getAssessmentSheetArgRet() {
        return assessmentSheetArgRet;
    }

    /**
     * {@inheritDoc}
     * @param assessmentSheetArgRet @{inheritDoc}
     */
    public void setAssessmentSheetArgRet(AssessmentSheet assessmentSheetArgRet) {
        this.assessmentSheetArgRet = assessmentSheetArgRet;
    }
}


