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
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.Section;
/**
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2007/03/30 22:43:09 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessSectionRiskCommand.java,v $
 * @stereotype command
 */
public class AssessSectionRiskCommand extends Command implements AssessSectionRiskArg {
    private static final long serialVersionUID = -5438138646757920100L;
    private transient AssessSectionRiskArg args = null;

    public AssessSectionRiskCommand() {
        super();
        args = new AssessSectionRiskArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (AssessSectionRiskArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @see #setPolicy
     * @return value of policy
     */
    public Policy getPolicyArg() {
        return args.getPolicyArg();
    }

    /**
     * {@inheritDoc}
     * @see #getPolicyArgRet
     * @param policy New value for policy argument.
     */
    public void setPolicyArg(Policy policy) {
        args.setPolicyArg(policy);
    }

    /**
     * {@inheritDoc}
     * @see #setSection
     * @return value of section
     */
    public Section getSectionArg() {
        return args.getSectionArg();
    }

    /**
     * {@inheritDoc}
     * @see #getSection
     * @param section New value for section argument.
     */
    public void setSectionArg(Section section) {
        args.setSectionArg(section);
    }

    /**
     * {@inheritDoc}
     * @see #setAssessmentSheet
     * @return value of assessmentsheet
     */
    public AssessmentSheet getAssessmentSheetArgRet() {
        return args.getAssessmentSheetArgRet();
    }

    /**
     * {@inheritDoc}
     * @see #getAssessmentSheet
     * @param assessmentsheet New value for assessmentsheet argument.
     */
    public void setAssessmentSheetArgRet(AssessmentSheet assessmentsheet) {
        args.setAssessmentSheetArgRet(assessmentsheet);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Core getCoreArg() {
        return args.getCoreArg();
    }

    /**
     * {@inheritDoc}
     * @param coreArg @{inheritDoc}
     */
    public void setCoreArg(Core coreArg) {
        args.setCoreArg(coreArg);
    }
}
