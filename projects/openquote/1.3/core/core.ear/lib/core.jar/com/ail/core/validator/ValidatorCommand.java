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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/ValidatorCommand.java,v $
 * @stereotype command
 */
public class ValidatorCommand extends Command implements ValidatorArg {
    private ValidatorArg args = null;

    public ValidatorCommand() {
        super();
        args = new ValidatorArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (ValidatorArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Object getValueArg() {
        return args.getValueArg();
    }

    /**
     * {@inheritDoc}
     * @param valueArg @{inheritDoc}
     */
    public void setValueArg(Object valueArg) {
        args.setValueArg(valueArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getKeyArg() {
        return args.getKeyArg();
    }

    /**
     * {@inheritDoc}
     * @param keyArg @{inheritDoc}
     */
    public void setKeyArg(String keyArg) {
        args.setKeyArg(keyArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public ValidatorResult getValidatorResultRet() {
        return args.getValidatorResultRet();
    }

    /**
     * {@inheritDoc}
     * @param validatorResultRet @{inheritDoc}
     */
    public void setValidatorResultRet(ValidatorResult validatorResultRet) {
        args.setValidatorResultRet(validatorResultRet);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getValidationSpecArg() {
        return args.getValidationSpecArg();
    }

    /**
     * {@inheritDoc}
     * @param validationSpecArg @{inheritDoc}
     */
    public void setValidationSpecArg(String validationSpecArg) {
        args.setValidationSpecArg(validationSpecArg);
    }
}
