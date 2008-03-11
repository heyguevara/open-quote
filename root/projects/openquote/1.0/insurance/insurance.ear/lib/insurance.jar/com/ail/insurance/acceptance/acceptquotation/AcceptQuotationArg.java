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

package com.ail.insurance.acceptance.acceptquotation;

import com.ail.insurance.policy.Policy;
/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype arg
 */
public interface AcceptQuotationArg extends com.ail.core.command.CommandArg {
    /**
     * Getter for the policyArgRet property. Quoted policy to be put on risk
     * @return Value of policyArgRet, or null if it is unset
     */
    Policy getPolicyArgRet();

    /**
     * Setter for the policyArgRet property. * @see #getPolicyArgRet
     * @param policyArgRet new value for property.
     */
    void setPolicyArgRet(Policy policyArgRet);
}


