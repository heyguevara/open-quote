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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorException;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResourcesInitializer;
import org.apache.commons.validator.ValidatorResults;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.CommandArg;

/**
 * Implementation of the Validator service using the <a href="http://jakarta.apache.org/commons/validator">
 * jakarta commons validator</a> project.<p>
 * configuration.
 */
@SuppressWarnings("deprecation")
public class CommonsValidatorService extends Service {
    private ValidatorArg args = null;
    private Core core = null;

    /** Default constructor */
    public CommonsValidatorService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (ValidatorArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of ValidatorArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    @SuppressWarnings("unchecked")
    public void invoke() throws PreconditionException, ValidationError {
        // get arguments
		String key = args.getKeyArg();
		Object value = args.getValueArg();
        String validationSpec = args.getValidationSpecArg();

		// check arguments
        if(key==null || key.trim().equals("")){
        	throw new PreconditionException("key==null || key.trim().equals(\"\")");
        }
		if(value==null){
			throw new PreconditionException("value==null");
		}
        if (validationSpec==null || validationSpec.length()==0) {
            throw new PreconditionException("validationSpec==null || validationSpec.length()==0");
        }

		// get validation method configuration		
		String validationGlobals = getConfiguration().findParameterValue("CommonsGlobalXml", "").trim();

		// merge validation xml
		String validationXml = "<form-validation>"+validationGlobals+validationSpec+"</form-validation>"				;

		// create validator resource from xml
		ValidatorResources resources = new ValidatorResources();
		ByteArrayInputStream in = new ByteArrayInputStream(validationXml.getBytes());
		try {
			ValidatorResourcesInitializer.initialize(resources, in);
		} catch (IOException e) {
			throw new ValidationError(e.getMessage(),e);
		}

		// pass xml to validator
		Validator validator = new Validator(resources, key);

		// Tell the validator which bean to validate against.
		validator.addResource(Validator.BEAN_KEY, value);

		// run validation
		ValidatorResults results = null;
		try {
			results = validator.validate();
		} catch (ValidatorException e) {
			throw new ValidationError(e.getMessage(),e);
		}
		
		// loop through fields & find errors
		Iterator<String> propertyNames = (Iterator<String>)results.get();

		ValidatorResult validatorResult = new ValidatorResult();
		while (propertyNames.hasNext()) {

			// get field name
			String propertyName = propertyNames.next();

			// Get the result of validating the property.
			org.apache.commons.validator.ValidatorResult result = (org.apache.commons.validator.ValidatorResult) results.getValidatorResult(propertyName);

			// Get all the actions run against the property, and iterate over their names.
			Map<String,Object> actionMap = result.getActionMap();
			Iterator<String> keys = actionMap.keySet().iterator();

			// loop through the dependencies
			while (keys.hasNext()) {
				String actName = keys.next();
				// Get the Action for that name.
				ValidatorAction action = resources.getValidatorAction(actName);

				//If the result failed, format the Action's message against the formatted field name
				if (!result.isValid(actName)) {
					ValidatorFailureDetail detail = new ValidatorFailureDetail();
					detail.setFieldName(propertyName);
					detail.setValidationAction(actName);
					detail.setErrorMessage(action.getMsg());
					validatorResult.addFailureDetail(detail);
				}
			}
		}
		
		args.setValidatorResultRet(validatorResult);					
    }
}


