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

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;
import com.ail.insurance.policy.Policy;

@ArgumentDefinition
public interface AssessRiskArgument extends Argument {
    /**
     * Fetch the value of the policy argument. Each of the sections in the policy is processed in turn, and appropriate
     * additions are made to their PremiumCalculateTables. The the policy's own AssessmentSheet is updated. All of the
     * is optional and under the control of the rules services defined for the product.
     * @see #setPolicyArgRet
     * @return value of policy
     */
    Policy getPolicyArgRet();

    /**
     * Set the value of the policy argument. Each of the sections in the policy is processed in turn, and appropriate
     * additions are made to their PremiumCalculateTables. The the policy's own AssessmentSheet is updated. All of the
     * is optional and under the control of the rules services defined for the product.
     * @see #getPolicyArgRet
     * @param policy New value for policy argument.
     */
    void setPolicyArgRet(Policy policy);
}


