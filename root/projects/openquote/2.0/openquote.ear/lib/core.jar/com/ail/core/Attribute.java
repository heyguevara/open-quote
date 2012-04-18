/* Copyright Applied Industrial Logic Limited 2004. All rights Reserved */
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

package com.ail.core;

import static com.ail.core.Functions.hideNull;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Format;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.ail.annotation.TypeDefinition;

/**
 * An attribute is defined as "One of numerous aspects, as of a subject". Generally, another
 * type will own a (composite) collection of Attributes which help describe it. For example,
 * a Car type might have attributes including colour, engine size, number of seats. A Person 
 * type might have height, weight, and hair colour as attributes.<p>
 * 
 * Attribute supports very flexible types (or formats) of value information. The approach 
 * to formatting attributes is based on {@link java.text.MessageFormat MessageFormat} and 
 * includes extensions for handling currency, and common choice types. All of MessageFormat's 
 * <i>Format Type</i> and <i>Format Style</i> options are supported, so for example you 
 * specify the format as <code>"number;percent"</code> as follows:<p>
 * <code>new Attribute("id", "21", "number;percent");</code><br><br>
 * 
 * The following are all valid combinations of <i>Format Type</i>, <i>Format Style</i>, and unit:<p>
 * <code>Attribute a=new Attribute("id", "23.442", "number;pattern=#.##");</code><br>
 * <code>Attribute b=new Attribute("id", "&pound;21", "currency", "GBP");</code><br>
 * <code>Attribute c=new Attribute("id", "21", "currency", "EUR");</code><br>
 * <code>Attribute d=new Attribute("id", "YES", "yesorno");</code><br>
 * <code>Attribute e=new Attribute("id", "Female", "choice;options=1#Male|2#Female");</code><br>
 * <code>Attribute e=new Attribute("id", "Hello world", "string");</code><br>
 * <code>Attribute e=new Attribute("id", "I'm a long string of text that may wrap over many lines", "note");</code><br>
 * <p>
 * Note: The '<code>note</code>' and a '<code>string</code>' formats are handled identically. UI components 
 * or document renderers may choose to display them differently, but the implementation within this class is
 * identical.
 * <p>
 * The Attribute class offers four ways to access an attribute's value: {@link #getValue() getValue()}, 
 * {@link #getObject() getObject()} and {@link #getFormattedValue() getFormattedValue()}. The following
 * table outlines the differences between these methods based on the attributes listed above:
 * <small><table>
 * <tr><td>a.getValue()</td><td>returns "23.442" as a String</td></tr>
 * <tr><td>a.getObject()</td><td>returns Double(21.442)</td></tr>
 * <tr><td>a.getFormattedValue()</td><td>returns "23.44" as a String</td></tr>
 * <tr><td>b.getValue()</td><td>returns "&pound;21" as a String</td></tr>
 * <tr><td>b.getObject()</td><td>returns "&pound;21" as a String</td></tr>
 * <tr><td>b.getFormattedValue()</td><td>returns "&pound;21" as a String</td></tr>
 * <tr><td>c.getValue()</td><td>returns "21" as a String</td></tr>
 * <tr><td>c.getObject()</td><td>returns "21" as a String</td></tr>
 * <tr><td>c.getFormattedValue()</td><td>returns "&pound;21" as a String</td></tr>
 * <tr><td>d.getValue()</td><td>returns "YES" as a String</td></tr>
 * <tr><td>d.getObject()</td><td>returns Double(1.0)</td></tr>
 * <tr><td>d.getFormattedValue()</td><td>returns "YES" as a String</td></tr>
 * <tr><td>c.getValue()</td><td>returns "Female" as a String</td></tr>
 * <tr><td>c.getObject()</td><td>returns Double(2.0)</td></tr>
 * <tr><td>c.getFormattedValue()</td><td>returns "Female" as a String</td></tr>
 * </table></small><br>
 * 
 * <p>Values can only ever be set using {@link #setValue() setValue(String)}.</p>
 * 
 * <p>ThreadLocale sensitive attribute types (currency and number) are formatted and validated with respect to
 * the following (in descending order of priority):</p><ul>
 * <li>any pattern defined as part of the attribute (e.g. "number;pattern=#.##");</li>
 * <li>the pattern appropriate to the locale defined by {@link com.ail.core.ThreadLocale#getThreadLocale() ThreadLocale.getThreadLocale()}</li>
 * <li>the JVM's default locale (as defined by {@link java.util.Locale#getDefault() ThreadLocale.getDefault()}</li>
 * </ul>
 */
@TypeDefinition
public class Attribute extends Type implements Identified {
    private static final Pattern formatOptionsPattern=Pattern.compile("(size=([0-9]*))|"+
                                                                      "(min=[0-9.]*)|"+
                                                                      "(max=[0-9.]*)|"+
                                                                      "(options=.*)|" +
                                                                      "(pattern=.*)|"+
                                                                      "(type=[A-Za-z0-9_.]*)|"+
                                                                      "(master=[A-Za-z0-9_.]*)|"+
                                                                      "(required=(no|yes))|"+
                                                                      "(slave=[A-Za-z0-9_.]*)|" +
                                                                      "(ref=[/A-Za-z0-9_():]*)|"+
                                                                      "(percent)");
    
    private static Map<Thread,Type> referenceContext=Collections.synchronizedMap(new HashMap<Thread,Type>());
    transient private String localFormat;
    public static String YES_OR_NO_FORMAT="choice,options=-1#?|0#No|1#Yes";

    /** The name of the facet - generally unique in a collection */
    private String id;

    /**
     * The value of the facet. For a colour facet, this might be "green". For a "height" facet this might be a Double
     * instance with a value of 6.1.
     */
    private String value;

    /** The format of object represented (or accepted). e.g. "string", etc. */
    private String format;

    /**
     * An optional attribute describing the unit of the facet. This can be any standard unit understood by
     * Unit.valueOf(String). E.g. "kg", "m", etc.
     */
    private String unit;

    /** Default constructor
     */
    public Attribute() {
    }

    /**
     * Constructor 
     * @param id Value for id property
     * @param value Value for val property
     * @param format Value for type property
     * @param unit Value for unit property
     */
    public Attribute(String id, String value, String format) {
        setId(id);
        setFormat(format);
        setValue(value);
    }

    /**
     * Constructor 
     * @param id Value for id property
     * @param value Value for val property
     * @param format Value for type property
     * @param unit Value for unit property
     */
    public Attribute(String id, String value, String format, String unit) {
        setId(id);
        setFormat(format);
        setUnit(unit);
        setValue(value);
    }

    /**
     * Getter returning the value of the id property. The id of the facet - generally unique in a collection
     * @return Value of the name property
     */
    public String getId() {
        return id;
    }

    /**
     * Setter to update the value of the id property. The id of the facet - generally unique in a collection
     * @param name New value for the name property
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter returning the value of the value property. The value of the facet. For a colour facet, this might be "green".
     * For a "height" facet this might be a Double instance with a value of 6.1.
     * @return Value of the value property
     */
    public String getValue() {
        return value;
    }

    /**
     * <p>Setter to update the value of the value property. The value of the facet. For a colour facet, this might be "green".
     * For a "height" facet this might be a Double instance with a value of 6.1.</p>
     * <p>The value supplied here may or may not be valid with respect to the Attributes format. In either case, the value will
     * be accepted. The {@link Attribute#isInvalid() isInvalid()} method can be used later to determine the values validity.
     * Some attempts are made to convert the value supplied into the system's internal representation (see below), but these attempts
     * will fail silently if the value passed in cannot be converted - again, the value will be accepted regardless.</p>
     * <b>Number attributes</b>
     * <p>For number attributes (attributes with format==number) an attempt is made to convert the value to the internal
     * representation if possible. If it isn't possible, then the value is left as supplied. We have to do this now (in setValue)
     * because it is only now that we know the user's locale and therefore have a fighting chance to understand what was meant.</p>
     * We will make a number of attempts to get a conversion, and return immediately should one succeed:<ol>
     * <li>If a pattern is defined for this attribute, use it with locale specific symbols.</li>
     * <li>If a pattern is defined for this attribute, use it with the JVM's default locale's symbols</li>
     * <li>Use the number format appropriate for the locale.</li>
     * <li>Use the number format appropriate for the JVM's default locale.</li>
     * </ol>
     * @param value New value for the value property
     */
    public void setValue(String val) {
        this.value = val;
        
        if (isNumberType()) {
            value = interpretNumberValue(val);
        }
        else if (isCurrencyType()) {
            value = interpretCurrencyValue(val);
        }
        else {
            value = val;
        }
    }

    /**
     * Make an attempt to interpret the string passed in as a number and store the result in {@link #value} having
     * resolved locale specific group and decimal fraction separators.
     * @param val
     * @return interpreted value
     * @see Attribute#setValue(String)
     */
    private String interpretNumberValue(String val) {
        Locale locale=com.ail.core.ThreadLocale.getThreadLocale();
        String pattern=getFormatOption("pattern");

        // If we have a pattern to work from, try that first...
        if (pattern!=null) {
            try {
                // ATTEMPT 1) use the locale's symbols
                DecimalFormat format=new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
                return format.parse(val).toString();
            } catch (ParseException e) {
                try {
                    // ATTEMPT 2) use the system's default symbols
                    DecimalFormat format=new DecimalFormat(pattern, new DecimalFormatSymbols());
                    return format.parse(val).toString();
                } catch (ParseException pe) {
                    // ignore this, lets try the system's format.
                }
            }
        }
   
        try {
            // ATTEMPT 3) there's no pattern, so use the locale
            NumberFormat format=NumberFormat.getInstance(locale);
            return format.parse(val).toString();
        } catch (ParseException e) {
            // ignore this, lets try the system's format.
        }

        try {
            // ATTEMPT 4) use the JVM's locale
            NumberFormat format=NumberFormat.getInstance();
            return format.parse(val).toString();
        } catch (ParseException e) {
            // ignore this, this was the last try - so the value isn't valid and we'll leave it as is.
        }
        
        // All attempts have failed, return the string as is.
        return val;
    }

    private String interpretCurrencyValue(String val) {
        Locale locale = com.ail.core.ThreadLocale.getThreadLocale();
        String pattern = getFormatOption("pattern");
        
        if (unit==null) {
            throw new IllegalStateException("Attribute '"+id+"' must define a currency in its unit property.");
        }
        
        Currency currency = Currency.getInstance(unit);

        if (pattern!=null) {
            try {
                // ATTEMPT 1) use the locale's symbols
                DecimalFormat format=new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
                format.setCurrency(currency);
                return format.parse(val).toString();
            } catch (ParseException e) {
                try {
                    // ATTEMPT 2) use the system's default symbols
                    DecimalFormat format=new DecimalFormat(pattern, new DecimalFormatSymbols());
                    format.setCurrency(currency);
                    return format.parse(val).toString();
                } catch (ParseException pe) {
                    // ignore this, lets try the system's format.
                }
            }
        }
        
        try {
            // ATTEMPT 3) there's no pattern, so use the locale
            NumberFormat format=NumberFormat.getCurrencyInstance(locale);
            format.setCurrency(Currency.getInstance(unit));
            return format.parse(val).toString();
        } catch (ParseException e) {
            // ignore this, lets try the system's format.
        }

        try {
            // ATTEMPT 4) use the JVM's locale
            NumberFormat format=NumberFormat.getCurrencyInstance();
            format.setCurrency(Currency.getInstance(unit));
            return format.parse(val).toString();
        } catch (ParseException e) {
            // ignore this, this was the last try - so the value isn't valid and we'll leave it as is.
        }
        
        // All attempts have failed, return the string as is.
        return interpretNumberValue(val);
    }
    
    /**
     * 
     * @return
     */
    public String getValueForXmlText() {
    	return isNoteType() ? getValue() : null;
    }
    
    public void setValueForXmlText(String value) {
    	this.value = value;
    }
    
    /**
     */
    public String getValueForXmlAttribute() {
    	return isNoteType() ? null : getValue();
    }
    
    /**
     * Return the Attribute's value formatted
     * @param locale The locale to format the result for 
     * @return The formatted attributes value.
     */
    public String getFormattedValue() {
        if (isStringType() || isNoteType() || isFreeChoiceType()) {
            return value;
        }
        else {
            Format format=formatter(ThreadLocale.getThreadLocale());
            
            if (format instanceof MessageFormat) {
                return format.format(new Object[]{getObject()});
            }
            else {
                return format.format(getObject());
            }
        }
    }

    /**
     * Getter returning the value of the type property. The java type of object represented (or accepted).
     * e.g. java.lang.String, etc.
     * @return Value of the type property
     */
    public String getFormat() {
        return format;
    }

    /**
     * Setter to update the value of the type property. The java type of object represented (or accepted).
     * e.g. java.lang.String, etc.
     * @param type New value for the type property
     */
    public void setFormat(String format) {
        this.format = format;
        localFormat = null;
    }

    /**
     * return the actual format to be used - dereferenced if necessary.
     * @return dereferenced format
     */
    private String getLocalFormat() {
        if (localFormat==null && format!=null) {
            if (format.startsWith("ref=")) {
                localFormat=getReferenceContext().xpathGet(format.substring(4), String.class);
            }
            else {
                localFormat=format;
            }
        }
        
        return localFormat;
    }
    
    /**
     * Getter returning the value of the unit property. An optional attribute describing the unit of the facet. This can be any
     * standard unit understood by Unit.valueOf(String). E.g. "kg", "m", etc.
     * @return Value of the unit property
     */
    public String getUnit() {
        return unit;
    }

    /**
     * Setter to update the value of the unit property. An optional attribute describing the unit of the facet. This can be any
     * standard unit understood by Unit.valueOf(String). E.g. "kg", "m", etc.
     * @param unit New value for the unit property
     */
    public void setUnit(String unit) {
        this.unit = unit;
    }

    /**
     * An attributes format is frequently made up of a number of optional values. For example, the format string may specify
     * a number with minimum and maximum values: format="number;min=10;max=2000". This method returns the value of a named 
     * option. In the example above, calling it with optionName set to "min" would return "10".
     * @param optionName The name of the option to return the value of
     * @return The value of the option, or an empty string if the option has no value, or null if the option is not present in the attribute's format.
     */
    public String getFormatOption(String optionName) {
        for(Matcher m=formatOptionsPattern.matcher(getLocalFormat()) ; m.find() ; ) {
            if (m.group().startsWith(optionName+"=")) {
                return m.group().substring(optionName.length()+1);
            }
            else if (m.group().equals(optionName)) {
                return "";
            }
        }

        return null;
    }
    
    /**
     * Get the object representation of this attribute. The actual Object returned is implied by the
     * attribute's format: <ul>
     * <li>if the value is null, null is returned.</li>
     * <li>If format is string or note, a String is returned.</li>
     * <li>
     * @param locale ThreadLocale to prepare the object for.
     * @return
     */
    public Object getObject() {
        Object ret=null;
        
        if (getLocalFormat() == null) {
            throw new IllegalStateException("Cannot determine type for attribute (id='"+id+"')");
        }

        if (value == null) {
            return null;
        }

        try {
            if (isStringType() || isNoteType()) {
                return value;
            }
            else if (isCurrencyType() && unit != null && unit.matches("[A-Z]{3}?")) {
                // The Attribute's value _should_ be in a format appropriate to the currency in the unit
                // property, but it may also be in the format appropriate to the locale. So, $1,200.23 may 
                // appear in value as: "USD1,200.23", "1200.23 USD", "1.200,00 USD" depending on the locale.
                // Here, we want to turn that string into a Double.

                // Strip out any character that isn't valid in a number.
                StringBuffer tval = new StringBuffer(value.replaceFirst("[^0-9+-.',()]*", ""));
                
                // To parseObject() for the specified locale below, we need to make sure that the decimal
                // separator is right for the local we're parsing into. But, at this point, tval may or
                // may not have a separator ("1200" is a perfectly valid double after all).
                
                // Find out where the decimal separator should be for this currency if there is one.
                int dsFractionDigits=Currency.getInstance(unit).getDefaultFractionDigits();

                // Translate the fractional digits into a string position
                int dsStringPos=tval.length()-dsFractionDigits-1;
                
                // If the char at that position is not a number...
                if (dsStringPos > 0 && !Character.isDigit(tval.charAt(dsStringPos))) {
                    // ... replace if with the right symbol for the currency we're parsing into
                    char dsChar=new DecimalFormatSymbols(com.ail.core.ThreadLocale.getThreadLocale()).getDecimalSeparator();
                    tval.setCharAt(dsStringPos, dsChar);
                }

                ret=NumberFormat.getInstance(com.ail.core.ThreadLocale.getThreadLocale()).parseObject(tval.toString());
            }
            else if (isNumberType()) {
                if (getFormatOption("percent")!=null) {
                    Number val=NumberFormat.getInstance().parse(value);
                    ret=val.doubleValue() / 100;
                }
                else {
                    ret=NumberFormat.getInstance().parseObject(value);
                }
            }
            else {
                ret=formatter(com.ail.core.ThreadLocale.getThreadLocale()).parseObject(value);
            }
        }
        catch(NullPointerException e) {
            throw new IllegalStateException("Cannot determine format for attribute (id='"+id+"') format defined as '"+getLocalFormat()+"'");
        }
        catch(ParseException e) {
            throw new IllegalStateException("Could not parse the value of attribute (id='"+id+"') format defined as '"+getLocalFormat()+"'");
        }
        
        // At this point ret may either be an Object, or an Array as calls to parseObject can return both. However, 
        // as Attribute's can only have one value, so we always want to return just one Object.
        return (ret!=null && ret.getClass().isArray() ? ((Object[])ret)[0] : ret);
    }

    /**
     * Build a message format to handle the formatting of value for this attribute. The specifics of the
     * format used will depend on the attribute's format, and the locale specified. 
     * @param locale
     * @return A message format suitable for formatting this attribute.
     */
    private Format formatter(Locale locale) {
        // handle special cases of format - those in addition to what you get from
        // the default MessageFormat.
        if (isStringType() || isNoteType()) {
            // "string" doesn't need a formatter. getObject handles it.
            return null;
        }
        else if (isYesornoType()) {
            // "yesorno" boils down to a choice: YES, NO, or '?' (neither)
            return new MessageFormat("{0,choice,-1#?|0#No|1#Yes}");
        }
        else if (isCurrencyType()) {
            String pattern = getFormatOption("pattern");

            if (unit != null && unit.matches("[A-Z]{3}?")) {
                try {
                    NumberFormat format=null;

                    if (pattern!=null) {
                        format=new DecimalFormat(pattern, new DecimalFormatSymbols(locale));
                    }
                    else {
                        format=NumberFormat.getCurrencyInstance(locale);
                    }
                    format.setCurrency(Currency.getInstance(unit));
                    
                    return format;
                }
                catch (Exception e) {
                    // ignore - the if (formatter==null) will handle this below.
                }
            }
            else {
                return new MessageFormat("{0,number}");
            }
        }
        else if (isNumberType()) {
            String pattern = getFormatOption("pattern");
            if (pattern!=null) {
                DecimalFormat df=new DecimalFormat(pattern);
                df.setDecimalFormatSymbols(new DecimalFormatSymbols(locale));
                return df;
            }
            else {
                if (getFormatOption("percent")!=null) {
                    return NumberFormat.getPercentInstance(locale);
                }
                else {
                    return NumberFormat.getInstance(locale);
                }
            }
        }
        else if (isDateType()) {
            String pattern = getFormatOption("pattern");
            return new MessageFormat("{0, date"+(pattern==null ? "" : ","+pattern)+"}");
        }
        else if (isChoiceType()) {
            String options = getFormatOption("options");
            return new MessageFormat("{0, choice"+(options==null ? "" : ","+options)+"}");
        }

        // If all else fails, assume the format is one MessageFormat can handle.
        return new MessageFormat("{0," + getLocalFormat() + "}");
    }

    /** 
     * If this is a choice master ({@see #isChoiceMasterType()}) then return the name of
     * the slave. 
     * @return Return the slave's name
     * @throws IllegalStateException if this is not a choice master.
     */
    public String getChoiceSlave() throws IllegalStateException {
        if (isChoiceMasterType()) {
            return hideNull(getFormatOption("slave"));
        }
        else {
            throw new IllegalStateException("This attribute ('"+id+"') is not a choice master");
        }
    }
    
    /** 
     * If this is a choice slave ({@see #isChoiceMasterType()}) then return the name of
     * the master. 
     * @return Return the master's name
     * @throws IllegalStateException if this is not a choice master.
     */
    public String getChoiceMaster() throws IllegalStateException {
        if (isChoiceSlaveType()) {
            return hideNull(getFormatOption("master"));
        }
        else {
            throw new IllegalStateException("This attribute ('"+id+"') is not a choice slave");
        }
    }
    
    /**
     * If this is a choice which derives it's list of options from a type, return the name of the type.
     * @return Name of the type defining options.
     * @throws IllegalStateException if this is not a choice, or is but does not define a 'type=' option.
     */
    public String getChoiceTypeName() throws IllegalStateException {
        
        if (isChoiceType()) {
            String typeName=hideNull(getFormatOption("type"));
            if (typeName!=null) {
                return typeName;
            }
            
            throw new IllegalStateException("This attribute ('"+id+"') is does not specify a 'type=' format option");
        }

        throw new IllegalStateException("This attribute ('"+id+"') is not a choice");
    }
    
    /**
     * Return true if the value of this attribute is 'undefined'. For 'note', 'string', 'number' and 'currency'
     * types undefined means "is null or of zero length". In the case of 'choice' undefined means value is '?'.
     * @return
     */
    public boolean isUndefined() {
        if (isNumberType() || isStringType() || isNoteType() || isCurrencyType() || isDateType()) {
            return value==null || value.length()==0;
        }
        else if (isChoiceType() && !isFreeChoiceType()){
            return (Double)getObject() < 0;
        }
        else if (isYesornoType()) {
            return (Double)getObject() < 0;
        }
        else {
            return true;
        }
    }

    /**
     * Test if the value of this attribute is invalid. Almost any value can be passed into an 
     * attribute's setValue() method and the method will not complain (and if getValue() is called
     * the same thing will be returned). However, the format may specify validation criteria. This
     * method will test if the value currently held by the attribute is invalid against those criteria. 
     * @return true if value is invalid, false otherwise.
     */
    public boolean isInvalid() {

        // if we can't get a formatted value, the attribute cannot be valid
        try {
            getFormattedValue();
        }
        catch(Throwable t) {
            return true;
        }
        
        if (isCurrencyType() || isNumberType()) {
            String min=getFormatOption("min");
            String max=getFormatOption("max");
            
            if (getFormat().contains("percent")) {
                min="0";
                max="100";
            }

            return ((min!=null && new Double(min) > new Double(value)) |
                    (max!=null && new Double(max) < new Double(value)));
        }

        if (isStringType() | isNoteType()) {
            String min=getFormatOption("min");
            String max=getFormatOption("max");
            String regex=getFormatOption("pattern");
            
            return ((min!=null && value.length() < new Integer(min)) |
                    (max!=null && value.length() > new Integer(max)) |
                    (regex!=null && !value.matches(regex)));
        }
        
        if (isChoiceType()) {
            getFormatOption("options");
        }
        
        return false;
    }
    
    /**
     * Return true if the type (format) of this attribute is number.
     * @return true if this is a number, false otherwise.
     */
    public boolean isNumberType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("number"));
    }
    
    /**
     * Return true if the type (format) of this attribute is choice. This method returns true
     * for both choice and free choice attributes.
     * @see #isFreeChoiceType()
     * @return true if this is a choice, false otherwise.
     */
    public boolean isChoiceType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("choice"));
    }
    
    /**
     * A 'free choice' attribute is one which is of choice type (format), but whose format does
     * not define the valid option list. This is typically used where a choice format is required,
     * but the actual values are determined outside of the attribute's definition - possibly from 
     * a lookup table, by the UI, of by parsing some other part of the model.
     * @return true if this is a choice, false otherwise
     */
    public boolean isFreeChoiceType() {
        return (isChoiceType() && getFormatOption("options")==null);
    }
    
    /**
     * Return true if this is a master choice type. A master is one whose value dictates the value
     * of a slave choice. For example, if two attributes representing the make and model of a vehicle
     * are used, the make would be the master, and the model the slage. When the master's value is
     * changed, the options available in the slave model list are updated.
     * @return true if this is a master choice attribute, false otherwise.
     */
    public boolean isChoiceMasterType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("choice") && getFormatOption("slave")!=null);
    }
    
    /**
     * See {@link #isChoiceMasterType()}
     * @return true if this is a slave choice attribute, false otherwuse.
     */
    public boolean isChoiceSlaveType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("choice") && getFormatOption("master")!=null);
    }
    
    /**
     * Return true if the type (format) of this attribute is currency.
     * @return true if this is a currency, false otherwise.
     */
    public boolean isCurrencyType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("currency"));
    }

    /**
     * Return true if the type (format) of this attribute is yesorno.
     * @return true if this is a yesorno, false otherwise.
     */
    public boolean isYesornoType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("yesorno"));
    }

    /**
     * Return true if the type (format) of this attribute is note.
     * @return true if this is a note, false otherwise.
     */
    public boolean isNoteType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("note"));
    }

    /**
     * Return true if the type (format) of this attribute is string.
     * @return true if this is a string, false otherwise.
     */
    public boolean isStringType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("string"));
    }

    /**
     * Return true if the type (format) of this attribute is date.
     * @return true if this is a date, false otherwise.
     */
    public boolean isDateType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("date"));
    }
    
    /**
     * Return true if the attribute is "required" - i.e. it is mandatory. This will
     * be the case if the attribute's format contains the "required" option and that 
     * option has the value of "yes".
     * @return true if the attribute is required, false otherwise.
     */
    public boolean isRequired() {
        if (getLocalFormat()==null) {
            new CoreProxy().logInfo("Attribute: "+id+" has no defined format");
            return false;
        }
        
        String required=getFormatOption("required");

        if (required==null || required.length()==0 || "yes".equalsIgnoreCase(required)) {
            return true;
        }

        return false;
    }

    /**
     * If <i>that</i> is an Attribute, and has the same Id as <i>this</i> they are considered equal.
     * @return true if <i>this</i> and <i>that</i> have the same Id.
     */
    public boolean compareById(Object that) {
        if (that instanceof Attribute) {
            return (this.getId()!=null && this.getId().equals(((Attribute)that).getId()));
        }
        else {
            return false;
        }
    }

    /**
     * Set the context against which references within Attributes in this thread will be evaluated.
     * Certain properties of an Attribute may be defined outside the Attribute itself, i.e. by 
     * reference to another object. Where such a reference is used, the object which those 
     * references are evaluated against is taken to be the reference context associated with the
     * current thread - i.e. the object passed into this method.
     * @param ctx Reference context
     */
    public static void setReferenceContext(Type ctx) {
        referenceContext.put(Thread.currentThread(), ctx);
    }
    
    public static Type getReferenceContext() {
        return referenceContext.get(Thread.currentThread());
    }
}
