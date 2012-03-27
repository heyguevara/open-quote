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

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/ValidatorFailureDetail.java,v $
 * @stereotype type
 */
@TypeDefinition
public class ValidatorFailureDetail extends Type {
    /** Name of field where validation failed */
    private String fieldName;

    /** Name of validation performed */
    private String validationAction;

    /** Description of validation failure */
    private String errorMessage;

    /**
     * Getter returning the value of the fieldName property. Name of field where validation failed
     * @return Value of the fieldName property
     */
    public String getFieldName() {
        return fieldName;
    }

    /**
     * Setter to update the value of the fieldName property. Name of field where validation failed
     * @param fieldName New value for the fieldName property
     */
    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    /**
     * Getter returning the value of the validationAction property. Name of validation performed
     * @return Value of the validationAction property
     */
    public String getValidationAction() {
        return validationAction;
    }

    /**
     * Setter to update the value of the validationAction property. Name of validation performed
     * @param validationAction New value for the validationAction property
     */
    public void setValidationAction(String validationAction) {
        this.validationAction = validationAction;
    }

    /**
     * Getter returning the value of the errorMessage property. Description of validation failure
     * @return Value of the errorMessage property
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter to update the value of the errorMessage property. Description of validation failure
     * @param errorMessage New value for the errorMessage property
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
