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

package com.ail.insurance.quotation.generatedocument;

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;
import com.ail.insurance.policy.Policy;

@ArgumentDefinition
public interface GenerateDocumentArgument extends Argument {
    /**
     * The policy for which a quotation is to be generated. The policy's status must be
     * QUOTATION for the generation to be successful.
     * @return policy to generate for.
     */
    Policy getPolicyArg();
    
    /**
     * @see #getPolicyArg()
     * @param policyArg
     */
    void setPolicyArg(Policy policyArg);
    
    /**
     * The generated document.
     * @return document
     */
    byte[] getDocumentRet();

    /**
     * @see #getDocumentRet()
     * @param documentRet
     */
    void setDocumentRet(byte[] documentRet);
}
