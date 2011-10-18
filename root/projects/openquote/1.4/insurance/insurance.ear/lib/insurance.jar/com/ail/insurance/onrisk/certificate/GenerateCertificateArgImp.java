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

package com.ail.insurance.onrisk.certificate;

import com.ail.core.command.CommandArgImp;
import com.ail.insurance.policy.Policy;

/**
 * This class represents the value object arguments and returns used/generated
 * by the entry point.
 */
public class GenerateCertificateArgImp extends CommandArgImp implements GenerateCertificateArg {
    private static final long serialVersionUID = 3606702185555605240L;
    private Policy policyArg;
    private byte[] documentRet;
    
    /** @inheritDoc */
    public byte[] getDocumentRet() {
        return documentRet;
    }
    
    /** @inheritDoc */
    public void setDocumentRet(byte[] documentRet) {
        this.documentRet = documentRet;
    }
    
    /** @inheritDoc */
    public Policy getPolicyArg() {
        return policyArg;
    }
    
    /** @inheritDoc */
    public void setPolicyArg(Policy policyArg) {
        this.policyArg = policyArg;
    }
}
