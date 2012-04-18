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

package com.ail.insurance.quotation;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.quotation.EnforcePolicyComplianceService.EnforcePolicyComplianceCommand;

@ServiceImplementation
public class EnforceComplianceService extends Service<EnforceComplianceService.EnforceComplianceArgument> {
    private static final long serialVersionUID = 5920061258083162375L;

    @ServiceArgument
    public interface EnforceComplianceArgument extends Argument {
        /**
         * Getter for the policyArgRet property. The policy to enforce compliance for
         * @return Value of policyArgRet, or null if it is unset
         */
        Policy getPolicyArgRet();

        /**
         * Setter for the policyArgRet property. * @see #getPolicyArgRet
         * @param policyArgRet new value for property.
         */
        void setPolicyArgRet(Policy policyArgRet);
    }

    @ServiceCommand(defaultServiceClass=EnforceComplianceService.class)
    public interface EnforceComplianceCommand extends Command, EnforceComplianceArgument {
    }

    /**
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    @Override
    public String getConfigurationNamespace() {
        return Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());
    }    
    
    @Override
    public void invoke() throws PreconditionException, BaseException {
        if (args.getPolicyArgRet()==null) {
            throw new PreconditionException("args.getPolicyArgRet()==null");
        }

        Policy policy=args.getPolicyArgRet();

        if (policy.getAssessmentSheet()==null) {
            throw new PreconditionException("policy.getAssessmentSheet()==null");
        }

        AssessmentSheet assessmentSheet=policy.getAssessmentSheet();

        if (policy.getProductTypeId()==null) {
            throw new PreconditionException("policy.getProductTypeId()==null");
        }

        // Remove any lines we added in the past
        assessmentSheet.removeLinesByOrigin("EnforceCompliance");

        // Lock the assessment sheet
        assessmentSheet.setLockingActor("EnforceCompliance");

        EnforcePolicyComplianceCommand command=core.newCommand("EnforcePolicyCompliance", EnforcePolicyComplianceCommand.class);
        command.setPolicyArg(policy);
        command.setAssessmentSheetArgRet(assessmentSheet);
        command.invoke();
        assessmentSheet=command.getAssessmentSheetArgRet();

        // unlock the assessment sheet
        assessmentSheet.clearLockingActor();

        // put the assessment sheet back into the policy
        policy.setAssessmentSheet(assessmentSheet);

        args.setPolicyArgRet(policy);
    }
}


