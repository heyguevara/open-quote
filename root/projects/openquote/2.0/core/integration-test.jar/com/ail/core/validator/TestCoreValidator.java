/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.Validator;
import org.apache.commons.validator.ValidatorAction;
import org.apache.commons.validator.ValidatorResources;
import org.apache.commons.validator.ValidatorResourcesInitializer;
import org.apache.commons.validator.ValidatorResults;
import org.apache.commons.validator.ValidatorUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.PreconditionException;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;

@SuppressWarnings({"unchecked","deprecation"})
public class TestCoreValidator extends CoreUserBaseCase {

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() {
        ConfigurationHandler.resetCache();
        setVersionEffectiveDate(new VersionEffectiveDate());
        tidyUpTestData();
        setCore(new Core(this));
        getCore().resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        CommonsValidatorService service=new CommonsValidatorService();
        service.resetConfiguration();
        resetConfiguration();
        setVersionEffectiveDate(new VersionEffectiveDate());
        ConfigurationHandler.resetCache();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
    @After
    public void tearDown() {
		tidyUpTestData();
    }


	/**
	 * Test basic access to the validator service
	 * @throws Exception
	 */
    @Test
	public void testValidatorDirectAccess() throws Exception {
        ValidatorService.ValidatorCommand command = getCore().newCommand("TestValidatorCommand", ValidatorService.ValidatorCommand.class);

		try{
			command.invoke();
		}
		catch(PreconditionException e){
			// exception is what this text expects
			return;
		}

		fail("validate with null args should throw a PreconditionException");
    }

	/**
	 * Test direct access to the validator
	 * @throws Exception
	 */
    @Test
    @SuppressWarnings("rawtypes")
    public void testCommonsValidatorFail() throws Exception {
		// set up key & value
		String key = "TestForm";
		Object value = getCore().newType(Version.class);

		String commonsValidatorXml = getCore().getParameterValue("ValidationSpec","");

		ValidatorResources resources = new ValidatorResources();
		ByteArrayInputStream in = new ByteArrayInputStream(commonsValidatorXml.getBytes());
		ValidatorResourcesInitializer.initialize(resources, in);

		// pass xml to validator
		Validator validator = new Validator(resources, key);

		// Tell the validator which bean to validate against.
		validator.addResource(Validator.BEAN_KEY, value);

		// run validation
		ValidatorResults results = validator.validate();
		Iterator<String> propertyNames = results.get();

		boolean errorsFound = false;
		while (propertyNames.hasNext()) {

			String propertyName = (String) propertyNames.next();

			// Get the result of validating the property.
			org.apache.commons.validator.ValidatorResult result = results.getValidatorResult(propertyName);

			// Get all the actions run against the property, and iterate over their names.
			Map actionMap = result.getActionMap();
			Iterator keys = actionMap.keySet().iterator();

			while (keys.hasNext()) {
				String actName = (String) keys.next();

				assertFalse(propertyName + "[" + actName + "]", result.isValid(actName));

				if (!result.isValid(actName)) {
					errorsFound = true;
				}
			}
		}

		if(!errorsFound){
			fail("validate failed");
		}
	}


	/**
	 * Test direct access to the validator
	 * @throws Exception
	 */
    @Test
	@SuppressWarnings("rawtypes")
    public void testCommonsValidatorSuccess() throws Exception {
		// set up key & value
		String key = "TestForm";
		Version value = getCore().newType(Version.class);

		value.setAuthor("Me");
		value.setComment("Text Comment");
		value.setCopyright("(c)2003");
		value.setDate("02/02/2003");
		value.setSource("Here");
		value.setState("Done");
		value.setVersion("V1");

		String commonsValidatorXml = getCore().getParameterValue("ValidationSpec","");

		ValidatorResources resources = new ValidatorResources();
		ByteArrayInputStream in = new ByteArrayInputStream(commonsValidatorXml.getBytes());
		ValidatorResourcesInitializer.initialize(resources, in);

		// pass xml to validator
		Validator validator = new Validator(resources, key);

		// Tell the validator which bean to validate against.
		validator.addResource(Validator.BEAN_KEY, value);

		// run validation
		ValidatorResults results = validator.validate();
		Iterator propertyNames = results.get();

		boolean errorsFound = false;
		while (propertyNames.hasNext()) {

			String propertyName = (String) propertyNames.next();

			// Get the result of validating the property.
			org.apache.commons.validator.ValidatorResult result = results.getValidatorResult(propertyName);

			// Get all the actions run against the property, and iterate over their names.
			Map actionMap = result.getActionMap();
			Iterator keys = actionMap.keySet().iterator();

			while (keys.hasNext()) {
				String actName = (String) keys.next();
				// Get the Action for that name.
				ValidatorAction action = resources.getValidatorAction(actName);

				// If the result is valid, print PASSED, otherwise print FAILED
				System.out.println(propertyName + "[" + actName + "] (" + (result.isValid(actName) ? "PASSED" : "FAILED") + ") " + action.getMsg());

				//If the result failed, format the Action's message against the formatted field name

				if (!result.isValid(actName)) {
					errorsFound = true;
				}
			}
		}

		if(errorsFound){
			fail("validate failed");
		}
	}

	/**
	 * Checks if the field is required.
	 *
	 * @param 	bean 		The value validation is being performed on.
	 * @return	boolean		If the field isn't <code>null</code> and
	 *                           has a length greater than zero,
	 *                           <code>true</code> is returned.
	 *                           Otherwise <code>false</code>.
	*/

	public static boolean validateRequired(Object bean, Field field) {

	   String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
	   return !GenericValidator.isBlankOrNull(value);

	}


	/**
	 * Access to the validator service
	 * @param key formname
	 * @param value object to test
	 * @param validationXml validation to perform
	 * @return error messages from validation
	 * @throws Exception
	 */
	private String testValidatorServiceGeneric(String key, Object value, String validationXml) throws Exception {
				
	    ValidatorService.ValidatorCommand command = getCore().newCommand("TestValidatorCommand", ValidatorService.ValidatorCommand.class);
		command.setKeyArg(key);
		command.setValueArg(value);
		command.setValidationSpecArg(validationXml);

		try{
			command.invoke();
		}
		catch(PreconditionException e){
			// exception is what this text expects
			e.printStackTrace();
			fail("validate needs key,value & spec args");
			return "";
		}

		com.ail.core.validator.ValidatorResult results = command.getValidatorResultRet();
		int errorCount = results.getFailureDetailCount();

		String error = "";
		for(int i=0; i<errorCount; i++){
			ValidatorFailureDetail failureDetail = results.getFailureDetail(i);
			if(!error.equals("")){
				error = error + "\n";
			}
			error = error + failureDetail.getFieldName();
			error = error +" ("+ failureDetail.getValidationAction() +")";
			error = error +" : "+ failureDetail.getErrorMessage();
		}

		return error;
	}

	/**
	 * Test basic access to the validator service
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceSuccess() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		Version value = getCore().newType(Version.class);
		value.setAuthor("Me");
		value.setComment("Text Comment");
		value.setCopyright("(c)2003");
		value.setDate("02/02/2003");
		value.setSource("Here");
		value.setState("Done");
		value.setVersion("V1");

		String validationXml = getCore().getParameterValue("ServiceValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceSuccess");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test basic access to the validator service
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";

		Version value = getCore().newType(Version.class);
		value.setComment("Text Comment");
		value.setCopyright("(c)2003");
		value.setState("Done");
		value.setVersion("V1");

		String validationXml = getCore().getParameterValue("ServiceValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceFail");
		System.out.println(error);	
	}

	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceTypesFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setDateValue("10/10/03");
		value.setStringValue1("123456789012345");
		value.setStringValue2("1");
		value.setByteValue("AA");
		value.setShortValue("A");
		value.setIntValue("A");
		value.setLongValue("A");
		value.setFloatValue("A");
		value.setDoubleValue("A");
		value.setEmailValue("m.t.ail.co.uk");
		value.setCreditCardValue("1234567812345678");

		String validationXml = getCore().getParameterValue("ServiceTypesValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceTypesFail");
		System.out.println(error);	
	}


	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceTypesSuccess() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setDateValue("10/10/2003");
		value.setStringValue1("55555");
		value.setStringValue2("55555");
		value.setByteValue("1");
		value.setShortValue("1");
		value.setIntValue("1");
		value.setLongValue("1");
		value.setFloatValue("1");
		value.setDoubleValue("1");
		value.setEmailValue("m.t@ail.co.uk");
		value.setCreditCardValue("4111111111111111");

		String validationXml = getCore().getParameterValue("ServiceTypesValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceTypesSuccess");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceTypesNull() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceTypesValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceTypesNull");
		System.out.println(error);	

	}


	/**
	 * Test validator service value range validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceInRangeSuccess() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setShortValue("10");
		value.setIntValue("10");
		value.setFloatValue("10");
		value.setDoubleValue("10");

		String validationXml = getCore().getParameterValue("ServiceInRangeValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceInRangeSuccess");
		System.out.println(error);	

		fail(error);
	}

	/**
	 * Test validator service value range validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceInRangeFailBelow() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setShortValue("1");
		value.setIntValue("1");
		value.setFloatValue("1");
		value.setDoubleValue("1");

		String validationXml = getCore().getParameterValue("ServiceInRangeValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceInRangeFailBelow");
		System.out.println(error);	
	}

	/**
	 * Test validator service value range validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceInRangeFailAbove() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setShortValue("21");
		value.setIntValue("21");
		value.setFloatValue("21");
		value.setDoubleValue("21");

		String validationXml = getCore().getParameterValue("ServiceInRangeValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceInRangeFailAbove");
		System.out.println(error);	
	}



	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceRegExpSuccess() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("file.txt");

		String validationXml = getCore().getParameterValue("ServiceRegExpValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceInRangeSuccess");
		System.out.println(error);	

		fail(error);
	}

	/**
	 * Test validator service required if validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceRegExpFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("file.doc");

		String validationXml = getCore().getParameterValue("ServiceRegExpValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceInRangeFailBelow");
		System.out.println(error);	

		System.out.println(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfOrSuccess1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("value2");
		value.setIntValue("1");

		String validationXml = getCore().getParameterValue("ServiceReqIfOrValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfOrSuccess1");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service required if validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfOrSuccess2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setIntValue("1");

		String validationXml = getCore().getParameterValue("ServiceReqIfOrValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfOrSuccess2");
		System.out.println(error);	

		fail(error);
	}

	/**
	 * Test validator service required if validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfOrSuccess3() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("value2");

		String validationXml = getCore().getParameterValue("ServiceReqIfOrValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfOrSuccess3");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service required if validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfOrSuccess4() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceReqIfOrValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfOrSuccess4");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfOrFail1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue2("value2");
		value.setIntValue("1");

		String validationXml = getCore().getParameterValue("ServiceReqIfOrValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfOrFail1");
		System.out.println(error);	

		System.out.println(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfOrFail2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setIntValue("1");

		String validationXml = getCore().getParameterValue("ServiceReqIfOrValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfOrFail2");
		System.out.println(error);	

		System.out.println(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfAndSuccess1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("value2");
		value.setIntValue("1");

		String validationXml = getCore().getParameterValue("ServiceReqIfAndValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfAndSuccess1");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfAndSuccess2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceReqIfAndValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfAndSuccess2");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfAndFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue2("value2");
		value.setIntValue("1");

		String validationXml = getCore().getParameterValue("ServiceReqIfAndValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfAndFail");
		System.out.println(error);	

		System.out.println(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNotNullSuccess1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("value2");

		String validationXml = getCore().getParameterValue("ServiceReqIfNotNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNotNullSuccess1");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNotNullSuccess2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceReqIfNotNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNotNullSuccess2");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNotNullSuccess3() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");

		String validationXml = getCore().getParameterValue("ServiceReqIfNotNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNotNullSuccess3");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNotNullFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue2("value2");

		String validationXml = getCore().getParameterValue("ServiceReqIfNotNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNotNullFail");
		System.out.println(error);	

		System.out.println(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNullSuccess1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");

		String validationXml = getCore().getParameterValue("ServiceReqIfNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNullSuccess1");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNullSuccess2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue2("value2");

		String validationXml = getCore().getParameterValue("ServiceReqIfNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNullSuccess2");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNullSuccess3() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("value2");

		String validationXml = getCore().getParameterValue("ServiceReqIfNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNullSuccess3");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfNullFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceReqIfNullValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfNullFail");
		System.out.println(error);	

		System.out.println(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfEqualsSuccess1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("some value");

		String validationXml = getCore().getParameterValue("ServiceReqIfEqualsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfEqualsSuccess1");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfEqualsSuccess2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue2("some other value");

		String validationXml = getCore().getParameterValue("ServiceReqIfEqualsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfEqualsSuccess2");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfEqualsSuccess3() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue1("value1");
		value.setStringValue2("some other value");

		String validationXml = getCore().getParameterValue("ServiceReqIfEqualsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfEqualsSuccess3");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfEqualsSuccess4() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceReqIfEqualsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfEqualsSuccess");
		System.out.println(error);	

		fail(error);
	}


	/**
	 * Test validator service regular expression validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceReqIfEqualsFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();
		value.setStringValue2("some text");

		String validationXml = getCore().getParameterValue("ServiceReqIfEqualsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceReqIfEqualsFail");
		System.out.println(error);	

		System.out.println(error);
	}

	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceArraySubsFail1() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject subObject = new TestTypesObject();
		subObject.setIntValue("1");
		Collection<String> collection = new Vector<String>();
		collection.add("A");
		collection.add("B");
		TestTypesObject value = new TestTypesObject();
		value.setSubObjectValue(subObject);
		value.setCollectionValue(collection);
		
		String validationXml = getCore().getParameterValue("ServiceArraySubsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceArraySubsFail1");
		System.out.println(error);	
	}


	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceArraySubsFail2() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		String[] arrayValue = {"ONE","TWO"};
		Collection<String> collection = new Vector<String>();
		collection.add("A");
		collection.add("B");
		TestTypesObject value = new TestTypesObject();
		value.setArrayValue(arrayValue);
		value.setCollectionValue(collection);

		String validationXml = getCore().getParameterValue("ServiceArraySubsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceArraySubsFail2");
		System.out.println(error);	
	}


	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceArraySubsFail3() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject value = new TestTypesObject();

		String validationXml = getCore().getParameterValue("ServiceArraySubsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceArraySubsFail3");
		System.out.println(error);	
	}

	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceArraySubsFail4() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject subObject = new TestTypesObject();
		subObject.setIntValue("1");
		String[] arrayValue = {"ONE"};
		Collection<String> collection = new Vector<String>();
		collection.add("A");
		collection.add("B");
		TestTypesObject value = new TestTypesObject();
		value.setArrayValue(arrayValue);
		value.setSubObjectValue(subObject);
		value.setCollectionValue(collection);

		String validationXml = getCore().getParameterValue("ServiceArraySubsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceArraySubsFail4");
		System.out.println(error);	
	}


	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceArraySubsFail5() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject subObject = new TestTypesObject();
		subObject.setIntValue("1");
		String[] arrayValue = {"ONE","TWO"};
		Collection<String> collection = new Vector<String>();
		collection.add("A");
		TestTypesObject value = new TestTypesObject();
		value.setArrayValue(arrayValue);
		value.setSubObjectValue(subObject);
		value.setCollectionValue(collection);

		String validationXml = getCore().getParameterValue("ServiceArraySubsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceArraySubsFail5");
		System.out.println(error);	
	}


	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceArraySubsSuccess() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		TestTypesObject subObject = new TestTypesObject();
		subObject.setIntValue("1");
		String[] arrayValue = {"ONE","TWO"};
		Collection<String> collection = new Vector<String>();
		collection.add("A");
		collection.add("B");
		TestTypesObject value = new TestTypesObject();
		value.setArrayValue(arrayValue);
		value.setSubObjectValue(subObject);
		value.setCollectionValue(collection);

		String validationXml = getCore().getParameterValue("ServiceArraySubsValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);

		if(error.equals("")){
			// success - no errors
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceArraySubsSuccess");
		System.out.println(error);	

		fail(error);
	}



	/**
	 * Test validator service type validation
	 * @throws Exception
	 */
    @Test
	public void testValidatorServiceXMLFail() throws Exception {
		// set up key & value & validation
		String key = "TestForm";
		
		String value = "<a><string name=\"string1\">ONE</string></a>";

		String validationXml = getCore().getParameterValue("ServiceXMLValidationSpec","");
		
		String error = testValidatorServiceGeneric(key, value, validationXml);


		if(error.equals("")){
			// fail - no errors
			fail("validate should have found errors");
			return;
		}

		System.out.println();
		System.out.println("testValidatorServiceXMLFail");
		System.out.println(error);	
	}

	/**
	 * Test object
	 */
	public class TestTypesObject{
		
		String stringValue1;
		String stringValue2;
		String byteValue;
		String shortValue;
		String intValue;
		String longValue;
		String floatValue;
		String doubleValue;
		String emailValue;
		String creditCardValue;
		String dateValue;
		String[] arrayValue;
		Collection<String> collectionValue;
		TestTypesObject subObjectValue;		
				
		/**
		 * @return
		 */
		public Collection<String> getCollectionValue() {
			return collectionValue;
		}
				
		/**
		 * @return
		 */
		public String[] getArrayValue() {
			return arrayValue;
		}
				
		/**
		 * @return
		 */
		public TestTypesObject getSubObjectValue() {
			return subObjectValue;
		}

		/**
		 * @return
		 */
		public String getDateValue() {
			return dateValue;
		}

		/**
		 * @return
		 */
		public String getStringValue1() {
			return stringValue1;
		}
				
		/**
		 * @return
		 */
		public String getStringValue2() {
			return stringValue2;
		}

		/**
		 * @return
		 */
		public String getByteValue() {
			return byteValue;
		}

		/**
		 * @return
		 */
		public String getCreditCardValue() {
			return creditCardValue;
		}

		/**
		 * @return
		 */
		public String getDoubleValue() {
			return doubleValue;
		}

		/**
		 * @return
		 */
		public String getEmailValue() {
			return emailValue;
		}

		/**
		 * @return
		 */
		public String getFloatValue() {
			return floatValue;
		}

		/**
		 * @return
		 */
		public String getIntValue() {
			return intValue;
		}

		/**
		 * @return
		 */
		public String getLongValue() {
			return longValue;
		}

		/**
		 * @return
		 */
		public String getShortValue() {
			return shortValue;
		}

		/**
		 * @param collection
		 */
		public void setCollectionValue(Collection<String> collection) {
			collectionValue = collection;
		}
						
		/**
		 * @param stringArray
		 */
		public void setArrayValue(String[] stringArray) {
			arrayValue = stringArray;
		}
				
		/**
		 * @param testTypesObject
		 */
		public void setSubObjectValue(TestTypesObject testTypesObject) {
			subObjectValue = testTypesObject;
		}

		/**
		 * @param string
		 */
		public void setDateValue(String string) {
			dateValue = string;
		}

		/**
		 * @param string
		 */
		public void setStringValue1(String string) {
			stringValue1 = string;
		}

		/**
		 * @param string
		 */
		public void setStringValue2(String string) {
			stringValue2 = string;
		}

		/**
		 * @param string
		 */
		public void setByteValue(String string) {
			byteValue = string;
		}

		/**
		 * @param string
		 */
		public void setCreditCardValue(String string) {
			creditCardValue = string;
		}

		/**
		 * @param string
		 */
		public void setDoubleValue(String string) {
			doubleValue = string;
		}

		/**
		 * @param string
		 */
		public void setEmailValue(String string) {
			emailValue = string;
		}

		/**
		 * @param string
		 */
		public void setFloatValue(String string) {
			floatValue = string;
		}

		/**
		 * @param string
		 */
		public void setIntValue(String string) {
			intValue = string;
		}

		/**
		 * @param string
		 */
		public void setLongValue(String string) {
			longValue = string;
		}

		/**
		 * @param string
		 */
		public void setShortValue(String string) {
			shortValue = string;
		}

	}

}
