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

package com.ail.insurance.quotation.refreshassessmentsheets;

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/refreshassessmentsheets/RefreshAssessmentSheetsArgImp.java,v $
 * @stereotype argimp
 */
public class RefreshAssessmentSheetsArgImp extends CommandArgImp implements RefreshAssessmentSheetsArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** The policy to refresh assessment sheets on. */
    private Policy policyArgRet;

    /** Used to tie assessment sheet entries to the service (owner) which originated them. */
    private String originArg;

    /** Default constructor */
    public RefreshAssessmentSheetsArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Policy getPolicyArgRet() {
        return policyArgRet;
    }

    /**
     * {@inheritDoc}
     * @param policyArgRet @{inheritDoc}
     */
    public void setPolicyArgRet(Policy policyArgRet) {
        this.policyArgRet = policyArgRet;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getOriginArg() {
        return originArg;
    }

    /**
     * {@inheritDoc}
     * @param originArg @{inheritDoc}
     */
    public void setOriginArg(String originArg) {
        this.originArg = originArg;
    }
}


