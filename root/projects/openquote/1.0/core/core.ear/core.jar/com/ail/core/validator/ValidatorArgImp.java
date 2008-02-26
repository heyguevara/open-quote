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

import com.ail.core.command.CommandArgImp;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/ValidatorArgImp.java,v $
 * @stereotype argimp
 */
public class ValidatorArgImp extends CommandArgImp implements ValidatorArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** Value to be validated */
    private Object valueArg;

    /** Identifies validation to perform */
    private String keyArg;

    /** Results of validation */
    private ValidatorResult validatorResultRet;

    /** The ValidationSpec includes any details that the service will need in order to perform validations. */
    private String validationSpecArg;

    /** Default constructor */
    public ValidatorArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Object getValueArg() {
        return valueArg;
    }

    /**
     * {@inheritDoc}
     * @param valueArg @{inheritDoc}
     */
    public void setValueArg(Object valueArg) {
        this.valueArg = valueArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getKeyArg() {
        return keyArg;
    }

    /**
     * {@inheritDoc}
     * @param keyArg @{inheritDoc}
     */
    public void setKeyArg(String keyArg) {
        this.keyArg = keyArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public ValidatorResult getValidatorResultRet() {
        return validatorResultRet;
    }

    /**
     * {@inheritDoc}
     * @param validatorResultRet @{inheritDoc}
     */
    public void setValidatorResultRet(ValidatorResult validatorResultRet) {
        this.validatorResultRet = validatorResultRet;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getValidationSpecArg() {
        return validationSpecArg;
    }

    /**
     * {@inheritDoc}
     * @param validationSpecArg @{inheritDoc}
     */
    public void setValidationSpecArg(String validationSpecArg) {
        this.validationSpecArg = validationSpecArg;
    }
}


