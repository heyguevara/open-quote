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

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessRiskArgImp.java,v $
 * @stereotype argimp
 */
public class AssessRiskArgImp extends CommandArgImp implements AssessRiskArg {
    static final long serialVersionUID = 1199346453402049909L;
    private Policy policyArgRet;

    /** Default constructor */
    public AssessRiskArgImp() {
    }

    /**
     * {@inheritDoc}
     * @see #setPolicyArgRet
     * @return value of policy
     */
    public Policy getPolicyArgRet() {
        return this.policyArgRet;
    }

    /**
     * {@inheritDoc}
     * @see #getPolicyArgRet
     * @param policyArgRet New value for policy argument.
     */
    public void setPolicyArgRet(Policy policyArgRet) {
        this.policyArgRet = policyArgRet;
    }
}


