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

package com.ail.core.validator;

import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/ValidatorArg.java,v $
 * @stereotype arg
 */
public interface ValidatorArg extends CommandArg {
    /**
     * Getter for the valueArg property. Value to be validated
     * @return Value of valueArg, or null if it is unset
     */
    Object getValueArg();

    /**
     * Setter for the valueArg property.
     * @see #getValueArg
     * @param valueArg new value for property.
     */
    void setValueArg(Object valueArg);

    /**
     * Getter for the keyArg property. Identifies validation to perform
     * @return Value of keyArg, or null if it is unset
     */
    String getKeyArg();

    /**
     * Setter for the keyArg property.
     * @see #getKeyArg
     * @param keyArg new value for property.
     */
    void setKeyArg(String keyArg);

    /**
     * Getter for the validatorResultRet property. Results of validation
     * @return Value of validatorResultRet, or null if it is unset
     */
    ValidatorResult getValidatorResultRet();

    /**
     * Setter for the validatorResultRet property.
     * @see #getValidatorResultRet
     * @param validatorResultRet new value for property.
     */
    void setValidatorResultRet(ValidatorResult validatorResultRet);

    /**
     * Getter for the ValidationSpecArg property. The ValidationSpec includes any details that the service will need in order
     * to perform validations.
     * @return Value of ValidationSpecArg, or null if it is unset
     */
    String getValidationSpecArg();

    /**
     * Setter for the ValidationSpecArg property.
     * @see #getValidationSpecArg
     * @param validationSpecArg new value for property.
     */
    void setValidationSpecArg(String validationSpecArg);
}


