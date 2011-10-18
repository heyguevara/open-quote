/* Copyright Applied Industrial Logic Limited 2007. All rights Reserved */
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

package com.ail.insurance.onrisk.certificate;

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

public class FetchCertificateArgImp extends CommandArgImp implements FetchCertificateArg {
    private static final long serialVersionUID = 3606702185555605240L;
    private String policyNumberArg;
    private byte[] documentRet;
    private Policy policyRet;
    
    /** @inheritDoc */
    public Policy getPolicyRet() {
        return policyRet;
    }

    /** @inheritDoc */
    public void setPolicyRet(Policy policyRet) {
        this.policyRet = policyRet;
    }

    /** @inheritDoc */
    public byte[] getDocumentRet() {
        return documentRet;
    }
    
    /** @inheritDoc */
    public void setDocumentRet(byte[] documentRet) {
        this.documentRet = documentRet;
    }
    
    /** @inheritDoc */
    public String getPolicyNumberArg() {
        return policyNumberArg;
    }
    
    /** @inheritDoc */
    public void setPolicyNumberArg(String policyNumberArg) {
        this.policyNumberArg = policyNumberArg;
    }
}
