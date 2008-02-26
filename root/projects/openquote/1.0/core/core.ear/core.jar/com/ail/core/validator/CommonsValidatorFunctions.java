/* Copyright Applied Industrial Logic Limited 2003. All rights reserved. */
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

import java.util.Locale;

import org.apache.commons.validator.Field;
import org.apache.commons.validator.GenericValidator;
import org.apache.commons.validator.GenericTypeValidator;
import org.apache.commons.validator.ValidatorUtil;
import org.apache.commons.validator.Validator;
import org.exolab.castor.xml.ValidationException;

/**
 * Commons validator functions
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/CommonsValidatorFunctions.java,v $
 **/
public class CommonsValidatorFunctions {

	/**
	 * Checks if the field is required.
	 *	<field property="field" depends="required"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field isn't <code>null</code> and 
	 *                           has a length greater than zero, 
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationError
	 */
	public static boolean validateRequired(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return !GenericValidator.isBlankOrNull(value);
	}

	/**
	 * Checks if the field is a minimum length.
	 *	<field    property="firstName"  depends="minimumLength">
	 *		<var>								
	 *			<var-name>minimumLength</var-name>	// minimum length - Default = no minimum length
	 *			<var-value>n</var-value>		
	 *		</var>
	 * </field>    
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field isn't <code>null</code> and 
	 *                           has a length greater than the minimum length, 
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException if Minimum length is not a valid integer
	 */
	public static boolean validateMinimumLength(Object bean, Field field) throws ValidationException{
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		String minimumLength = field.getVarValue("minimumLength");
		if(GenericValidator.isBlankOrNull(minimumLength)){
			return true;
		}
		int minLen;
		try{
			minLen = Integer.parseInt(minimumLength, 10);
		}
		catch(NumberFormatException e){
			throw new ValidationException("NumberFormatException: "+field.getKey()+".minimumLength="+minimumLength);
		}
		if(value==null){
			return minLen==0;
		}
		return GenericValidator.minLength(value, minLen);
	}

	/**
	 * Checks if the field is a maximum length.
	 *	<field    property="firstName"  depends="maximumLength">
	 *		<var>								
	 *			<var-name>maximumLength</var-name>	// maximum length - Default = no maximum length
	 *			<var-value>n</var-value>		
	 *		</var>
	 * </field>    
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field isn't <code>null</code> and 
	 *                           has a length less than the maximum length, 
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException if Maximum length is not a valid integer
	 */
	public static boolean validateMaximumLength(Object bean, Field field) throws ValidationException{
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		String maximumLength = field.getVarValue("maximumLength");
		if(GenericValidator.isBlankOrNull(maximumLength)){
			return true;
		}
		int maxLen;
		try{
			maxLen = Integer.parseInt(maximumLength, 10);
		}
		catch(NumberFormatException e){
			throw new ValidationException("NumberFormatException: "+field.getKey()+".maximumLength="+maximumLength);
		}
		if(value==null){
			return true;
		}
		return GenericValidator.maxLength(value, maxLen);
	}

	/**
	 * Checks if the field can be successfully converted to a <code>byte</code>.
	 *	<field property="field" depends="byte"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field can be successfully converted 
	 *                           to a <code>byte</code> <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateByte(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isByte(value);
	}

	/**
	 * Checks if the field can be successfully converted to a <code>short</code>.
	 *	<field property="field" depends="short"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field can be successfully converted 
	 *                           to a <code>short</code> <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateShort(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isShort(value);
	}

	/**
	 * Checks if the field can be successfully converted to a <code>int</code>.
	 *	<field property="field" depends="integer"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field can be successfully converted 
	 *                           to a <code>int</code> <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
 	 */
	public static boolean validateInt(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isInt(value);
	}

	/**
	 * Checks if the field can be successfully converted to a <code>long</code>.
	 *	<field property="field" depends="long"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field can be successfully converted 
	 *                           to a <code>long</code> <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateLong(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isLong(value);
	}

	/**
	 * Checks if the field can be successfully converted to a <code>float</code>.
	 *	<field property="field" depends="float"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field can be successfully converted 
	 *                           to a <code>float</code> <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateFloat(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isFloat(value);
	}

	/**
	 * Checks if the field can be successfully converted to a <code>double</code>.
	 *	<field property="field" depends="double"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field can be successfully converted 
	 *                           to a <code>double</code> <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateDouble(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isDouble(value);
	}

	/**
	 * Checks if the field is an e-mail address.
	 *	<field property="field" depends="email"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field is an e-mail address
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateEmail(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		return GenericValidator.isEmail(value);
	}

	/**
	 * Checks if the field matches a regular expresion.
	 *	<field    property="firstName"  depends="regularExpression">
	 *		<var>								
	 *			<var-name>regularExpression</var-name>	// expression to match
	 *			<var-value>*.txt</var-value>		
	 *		</var>
	 * </field>    
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field matches a regular expresion
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateRegexp(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		String regep = field.getVarValue("regularExpression");
		return GenericValidator.matchRegexp(value, regep);
	}

	/**
	 * Checks if the field is a credit card.
	 *	<field property="field" depends="creditCard"/>
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field is a credit card
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateCreditCard(Object bean, Field field) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		if(value==null){
			return false;
		}
		return GenericValidator.isCreditCard(value);
	}


	/**
	 * Checks if the field is a date.
	 *	<field    property="dob"  depends="date">
	 *		<var>								
	 *			<var-name>datePattern</var-name>	// pattern to match to match
	 *			<var-value>dd/MM/yyyy</var-value>	// default = locale		
	 *		</var>
	 *		<var>								
	 *			<var-name>strictDate</var-name>		// default = true
	 *			<var-value>true</var-value>			// true = validate date length aswell as format
	 *		</var>
	 * </field>    
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @param 	locale 		Date locale format if pattern not being tested
	 * @return	boolean		If the field is a Date
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateDate(Object bean, Field field, Locale locale) throws ValidationException {
		String value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		if (!GenericValidator.isBlankOrNull(field.getVarValue("datePattern"))) {
			// validate with date Pattern
			String pattern = field.getVarValue("datePattern");
			boolean strict = true;
			if (!GenericValidator.isBlankOrNull(field.getVarValue("strictDate"))) {
				// strict validation?
				strict = !"false".equalsIgnoreCase(field.getVarValue("strictDate"));
			}
			return GenericValidator.isDate(value, pattern, strict);			
		}
		else{
			return GenericValidator.isDate(value, locale);
		}
	}

	// Validate in range constants
	public final static String FIELD_TEST_DOUBLE = "DOUBLE";
	public final static String FIELD_TEST_INTEGER = "INTEGER";
	public final static String FIELD_TEST_FLOAT = "FLOAT";
	public final static String FIELD_TEST_SHORT = "SHORT";

	/**
	 * Checks if the field is in range.
	 *	<field    property="number"  depends="inRange">
	 *		<var>								
	 *			<var-name>dataType</var-name>	// Data type - DOUBLE, INTEGER, FLOAT, SHORT
	 *			<var-value>DOUBLE</var-value>	// default = locale		
	 *		</var>
	 *		<var>								
	 *			<var-name>minimumValue</var-name>	// Minimum value
	 *			<var-value>5</var-value>	
	 *		</var>
	 *		<var>								
	 *			<var-name>maximumValue</var-name>	// Maximum value
	 *			<var-value>15</var-value>	
	 *		</var>
	 * </field>    
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @return	boolean		If the field is in range
	 *                           <code>true</code> is returned.  
	 *                           Otherwise <code>false</code>.
	 * throws ValidationException
	 */
	public static boolean validateInRange(Object bean, Field field) throws ValidationException {
		String valueString = ValidatorUtil.getValueAsString(bean, field.getProperty());
		String minString = field.getVarValue("minimumValue");
		String maxString = field.getVarValue("maximumValue");

		String type = FIELD_TEST_DOUBLE;
		if (!GenericValidator.isBlankOrNull(field.getVarValue("dataType"))) {
			// validate with data type
			type = field.getVarValue("dataType").toUpperCase();
		}
		
		if(type.equals(FIELD_TEST_DOUBLE)){
			Double minDouble = GenericTypeValidator.formatDouble(minString);
			Double maxDouble = GenericTypeValidator.formatDouble(maxString);
			Double valueDouble = GenericTypeValidator.formatDouble(valueString);
	
			// check for error
			if(minDouble==null || maxDouble==null || valueDouble==null){
				String error = "ERROR: ";
				if(minDouble==null){
					minString = minString==null ? "NULL" : minString;
					error = error+field.getKey()+".minimumValue="+minString;
				}
				if(minDouble==null && maxDouble==null){
					error = error+",";
				}
				if(maxDouble==null){
					maxString = maxString==null ? "NULL" : maxString;
					error = error+field.getKey()+".maximumValue="+maxString;
				}
				if((minDouble==null || maxDouble==null) && valueDouble==null){
					error = error+",";
				}
				if(valueDouble==null){
					valueString = valueString==null ? "NULL" : valueString;
					error = error+field.getKey()+"="+valueString;
				}
				throw new ValidationException(error);		
			}				

			return GenericValidator.isInRange(valueDouble.doubleValue(), minDouble.doubleValue(), maxDouble.doubleValue());
		}
		
		else if(type.equals(FIELD_TEST_INTEGER)){
			Integer minInt = Integer.valueOf(minString,10);
			Integer maxInt = Integer.valueOf(maxString,10);
			Integer valueInt = Integer.valueOf(valueString,10);
	
			// check for error
			if(minInt==null || maxInt==null || valueInt==null){
				String error = "ERROR: ";
				if(minInt==null){
					minString = minString==null ? "NULL" : minString;
					error = error+field.getKey()+".minimumValue="+minString;
				}
				if(minInt==null && maxInt==null){
					error = error+",";
				}
				if(maxInt==null){
					maxString = maxString==null ? "NULL" : maxString;
					error = error+field.getKey()+".maximumValue="+maxString;
				}
				if((minInt==null || maxInt==null) && valueInt==null){
					error = error+",";
				}
				if(valueInt==null){
					valueString = valueString==null ? "NULL" : valueString;
					error = error+field.getKey()+"="+valueString;
				}
				throw new ValidationException(error);		
			}				

			return GenericValidator.isInRange(valueInt.intValue(), minInt.intValue(), maxInt.intValue());
		}
		
		else if(type.equals(FIELD_TEST_FLOAT)){
			Float minFloat = GenericTypeValidator.formatFloat(minString);
			Float maxFloat = GenericTypeValidator.formatFloat(maxString);
			Float valueFloat = GenericTypeValidator.formatFloat(valueString);
	
			// check for error
			if(minFloat==null || maxFloat==null || valueFloat==null){
				String error = "ERROR: ";
				if(minFloat==null){
					minString = minString==null ? "NULL" : minString;
					error = error+field.getKey()+".minimumValue="+minString;
				}
				if(minFloat==null && maxFloat==null){
					error = error+",";
				}
				if(maxFloat==null){
					maxString = maxString==null ? "NULL" : maxString;
					error = error+field.getKey()+".maximumValue="+maxString;
				}
				if((minFloat==null || maxFloat==null) && valueFloat==null){
					error = error+",";
				}
				if(valueFloat==null){
					valueString = valueString==null ? "NULL" : valueString;
					error = error+field.getKey()+"="+valueString;
				}
				throw new ValidationException(error);		
			}				

			return GenericValidator.isInRange(valueFloat.floatValue(), minFloat.floatValue(), maxFloat.floatValue());
		}
		
		else if(type.equals(FIELD_TEST_SHORT)){
			Short minShort = Short.valueOf(minString,10);
			Short maxShort = Short.valueOf(maxString,10);
			Short valueShort = Short.valueOf(valueString,10);
	
			// check for error
			if(minShort==null || maxShort==null || valueShort==null){
				String error = "ERROR: ";
				if(minShort==null){
					minString = minString==null ? "NULL" : minString;
					error = error+field.getKey()+".minimumValue="+minString;
				}
				if(minShort==null && maxShort==null){
					error = error+",";
				}
				if(maxShort==null){
					maxString = maxString==null ? "NULL" : maxString;
					error = error+field.getKey()+".maximumValue="+maxString;
				}
				if((minShort==null || maxShort==null) && valueShort==null){
					error = error+",";
				}
				if(valueShort==null){
					valueString = valueString==null ? "NULL" : valueString;
					error = error+field.getKey()+"="+valueString;
				}
				throw new ValidationException(error);		
			}				

			return GenericValidator.isInRange(valueShort.shortValue(), minShort.shortValue(), maxShort.shortValue());
		}
		
		else{
			throw new ValidationException("INVALID TYPE: "+field.getKey()+".dataType="+type);
		}
		
	}

	// Validate required if constants
	public final static String FIELD_TEST_NULL = "NULL";
	public final static String FIELD_TEST_NOTNULL = "NOTNULL";
	public final static String FIELD_TEST_EQUAL = "EQUAL";
	public final static String FIELD_JOIN_AND = "AND";
//	public final static String FIELD_INDEX_TRUE = "true";
//	public final static String FIELD_INDEX_FALSE = "false";

	/**
	 * Validate only if a test is successfull on other fields
	 *	<field    property="firstName"  depends="requiredIf">
	 *		<var>								// Optional - default = AND
	 *			<var-name>fieldJoin</var-name>	// AND = all field tests required to succeed before this field is required
	 *			<var-value>AND</var-value>		// otherwise treat as OR = one field test required to succeed before this field is required
	 *		</var>
	 *		<var>
	 *			<var-name>field[i]</var-name>	// name of field i (i=0 to n)
	 *			<var-value>lastName</var-value>
	 *		</var>
	 *		<var>
	 *			<var-name>fieldTest[i]</var-name>	// NULL or NOTNULL or EQUAL
	 *			<var-value>EQUAL</var-value>	 	// NULL = field i must be null
	 *		</var>									// NOTNULL = field i must not be null
	 *												// EQUALS = field i must be equal to the value specified fieldValue[i]
	 *		<var>
	 *			<var-name>fieldValue[i]</var-name>	// only required if fieldTest[i] = EQUAL
	 *			<var-value>Mr</var-value>			// value field[i] must equal
	 *		</var>
	 * </field>    
	 *
	 * @param 	bean		Object with field to validate
	 * @param 	field 		Field to validate
	 * @param 	validator 	Validation configuration
	 * @return	boolean			If all specified field tests succeed then:
	 *	 							If the field isn't <code>null</code> and 
	 *  	                         has a length greater than zero, 
	 *      	                     <code>true</code> is returned.  
	 *          	                 Otherwise <code>false</code>.
	 * 							Otherwise <code>true</code> is returned.
	 * throws ValidationException
	 */
	public static boolean validateRequiredIf(Object bean, Field field, Validator validator) throws ValidationException{
		Object form = validator.getResource(Validator.BEAN_KEY);

		// get value to test
		String value = null;
		if (isString(bean)) {
			value = (String) bean;
		} else {
			value = ValidatorUtil.getValueAsString(bean, field.getProperty());
		}

		// requireif field counter
		int i = 0;

		// if it is an AND test then required = true (default = AND)
		boolean required = false;
		String fieldJoin = FIELD_JOIN_AND;
		if (!GenericValidator.isBlankOrNull(field.getVarValue("fieldJoin"))) {
			fieldJoin = field.getVarValue("fieldJoin");
		}
		if (fieldJoin.equalsIgnoreCase(FIELD_JOIN_AND)) {
			required = true;
		}

		// while value found for requiredif field i
		while (!GenericValidator.isBlankOrNull(field.getVarValue("field[" + i + "]"))) {

			// name of field i
			String dependProp = field.getVarValue("field[" + i + "]");
			// test for field i- NULL or NOTNULL or EQUALS
			String dependTest = field.getVarValue("fieldTest[" + i + "]");
			//
			String dependTestValue = field.getVarValue("fieldValue[" + i + "]");

/*			// if field i name is indexed (i.e. addressLine1)
			String dependIndexed = field.getVarValue("fieldIndexed[" + i + "]");

			if (dependIndexed == null){
				dependIndexed = FIELD_INDEX_FALSE;
			}

			// tidy indexed field i name (remove brackets)
			if (field.isIndexed() && dependIndexed.equalsIgnoreCase(FIELD_INDEX_TRUE)) {
				String key = field.getKey();
				if ((key.indexOf("[") > -1) && (key.indexOf("]") > -1)) {
					String ind = key.substring(0, key.indexOf(".") + 1);
					dependProp = ind + dependProp;
				}
			}
*/

			boolean this_required = false;

			// get value of field i
			String dependVal = ValidatorUtil.getValueAsString(form, dependProp);

			// NULL field i test - field i value required if not null && not empty
			if (dependTest.equals(FIELD_TEST_NULL)) {
				if ((dependVal != null) && (dependVal.length() > 0)) {
					this_required = false; 
				} else {
					this_required = true;
				}
			}

			// NOTNULL field i test - field i value required if null or empty
			if (dependTest.equals(FIELD_TEST_NOTNULL)) {
				if ((dependVal != null) && (dependVal.length() > 0)) {
					this_required = true;
				} else {
					this_required = false;
				}
			}

			// EQUAL field i test - field i value required equal to fieldValue[i]
			if (dependTest.equals(FIELD_TEST_EQUAL)) {
				this_required = dependTestValue.equalsIgnoreCase(dependVal);
			}

			// if field join type is an AND, only required if all other tests meet previously
			if (fieldJoin.equalsIgnoreCase(FIELD_JOIN_AND)) {
				required = required && this_required;
			} else {
				required = required || this_required;
			}

			i++;
		}

		if (required) {
			if ((value != null) && (value.length() > 0)) {
				return true;
			} else {
				return false;
			}
		}

		return true;
	}

	/**
	 * Check object is String
	 * @param o Object to test
	 * @return true if String
	 */
	private static boolean isString(Object o) {
		if (o == null){
			return true;
		}
		return (o instanceof String);
	}

}
