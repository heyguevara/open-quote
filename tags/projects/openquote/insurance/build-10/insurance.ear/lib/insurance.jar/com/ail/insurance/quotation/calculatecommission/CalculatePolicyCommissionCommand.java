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

package com.ail.insurance.quotation.calculatecommission;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 23:19:08 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/calculatecommission/CalculatePolicyCommissionCommand.java,v $
 * @stereotype command
 */
public class CalculatePolicyCommissionCommand extends Command implements CalculatePolicyCommissionArg {
    private static final long serialVersionUID = -515840327199233860L;
    private CalculatePolicyCommissionArg args = null;

    public CalculatePolicyCommissionCommand() {
        super();
        args = new CalculatePolicyCommissionArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (CalculatePolicyCommissionArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArg() {
        return args.getPolicyArg();
    }

    /**
     * {@inheritDoc}
     * @param policyArg @{inheritDoc}
     */
    public void setPolicyArg(Policy policyArg) {
        args.setPolicyArg(policyArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public AssessmentSheet getAssessmentSheetArgRet() {
        return args.getAssessmentSheetArgRet();
    }

    /**
     * {@inheritDoc}
     * @param assessmentSheetArgRet @{inheritDoc}
     */
    public void setAssessmentSheetArgRet(AssessmentSheet assessmentSheetArgRet) {
        args.setAssessmentSheetArgRet(assessmentSheetArgRet);
    }
}
