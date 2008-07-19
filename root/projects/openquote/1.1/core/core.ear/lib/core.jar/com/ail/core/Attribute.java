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

import java.text.MessageFormat;
import java.text.ParsePosition;
import java.util.Collections;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * specify the format as <code>"number,percent"</code> as follows:<p>
 * <code>new Attribute("id", "21", "number,percent");</code><br><br>
 * 
 * The following are all valid combinations of <i>Format Type</i>, <i>Format Style</i>, and unit:<p>
 * <code>Attribute a=new Attribute("id", "23.442", "number,#.##");</code><br>
 * <code>Attribute b=new Attribute("id", "&pound;21", "currency", "GBP");</code><br>
 * <code>Attribute c=new Attribute("id", "21", "currency", "EUR");</code><br>
 * <code>Attribute d=new Attribute("id", "YES", "yesorno");</code><br>
 * <code>Attribute e=new Attribute("id", "Female", "choice,1#Male|2#Female");</code><br>
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
 * <tr><td>c.getFormattedValue()</td><td>returns "€21" as a String</td></tr>
 * <tr><td>d.getValue()</td><td>returns "YES" as a String</td></tr>
 * <tr><td>d.getObject()</td><td>returns Double(1.0)</td></tr>
 * <tr><td>d.getFormattedValue()</td><td>returns "YES" as a String</td></tr>
 * <tr><td>c.getValue()</td><td>returns "Female" as a String</td></tr>
 * <tr><td>c.getObject()</td><td>returns Double(2.0)</td></tr>
 * <tr><td>c.getFormattedValue()</td><td>returns "Female" as a String</td></tr>
 * </table></small><br>
 * 
 * Values can only ever be set using {@link #setValue() setValue(String)}.
 *
 * 
 * @version $Revision: 1.17 $
 * @state $State: Exp $
 * @date $Date: 2007/12/22 10:40:49 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/Attribute.java,v $
 * @stereotype type
 */
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
                                                                      "(ref=[/A-Za-z0-9_():]*)");
    
    private static Map<Thread,Type> referenceContext=Collections.synchronizedMap(new HashMap<Thread,Type>());
    transient private MessageFormat formatter;
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
        this(id, value, format);
        setUnit(unit);
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
     * Setter to update the value of the value property. The value of the facet. For a colour facet, this might be "green".
     * For a "height" facet this might be a Double instance with a value of 6.1.
     * @param value New value for the value property
     */
    public void setValue(String val) {
        this.value = val;
    }

    /**
     * Method to help castor maintain backward compatibility with pre-core 2.3 XML documents. The value returned from this
     * method will be mapped to the value attribute. As it is always null the value attribute is never marshalled.
     * @return always null
     */
    public String getValueFromXmlAttribute() {
    	return null;
    }
    
    /**
     * Castor will use this method when it finds a pre-2.3 style value (in an XML attribute).
     * @see #getValueFromXmlAttribute() 
     * @param val
     */
    public void setValueFromXmlAttribute(String val) {
    	this.value=val;
    }
    
    /**
     * Return the Attribute's value formatted 
     * @return The formatted attributes value.
     */
    public String getFormattedValue() {
        if (isStringType() || isNoteType() || isFreeChoiceType()) {
            return value;
        }
        else {
            Object o = getObject();
            String s = formatter().format(new Object[] { o });
            return s;
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
    }

    /**
     * return the actual format to be used - dereferenced if necessary.
     * @return dereferenced format
     */
    private String getLocalFormat() {
        if (localFormat==null) {
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

    public String getFormatOption(String optionName) {
        for(Matcher m=formatOptionsPattern.matcher(getLocalFormat()) ; m.find() ; ) {
            if (m.group().startsWith(optionName+"=")) {
                return m.group().substring(optionName.length()+1);
            }
        }

        return null;
    }
    
    public Object getObject() {
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
                if (!value.startsWith(Currency.getInstance(unit).getSymbol())) {
                    String tval = value.replaceFirst("^[^0-9+-.]*", "");
                    String symbol = Currency.getInstance(unit).getSymbol();
    
                    if (symbol.length() == 1) {
                        tval = symbol + tval;
                    }
                    else {
                        tval = tval + " " + symbol;
                    }
    
                    return formatter().parse(tval, new ParsePosition(0))[0];
                }
                else {
                    return formatter().parse(value, new ParsePosition(0))[0];
                }
            }
            else {
                return formatter().parse(value, new ParsePosition(0))[0];
            }
        }
        catch(NullPointerException e) {
            throw new IllegalStateException("Cannot determine format for attribute (id='"+id+"') format defined as '"+getLocalFormat()+"'");
        }
    }

    private MessageFormat formatter() {
        if (formatter != null) {
            return formatter;
        }

        // handle special cases of format - those in addition to what you get from
        // the default MessageFormat.
        if (isStringType() || isNoteType()) {
            // "string" doesn't need a formatter. getObject handles it.
            return formatter;
        }
        else if (isYesornoType()) {
            // "yesorno" boils down to a choice: YES, NO, or '?' (neither)
            formatter = new MessageFormat("{0,choice,-1#?|0#No|1#Yes}");
        }
        else if (isCurrencyType()) {
            if (unit != null && unit.matches("[A-Z]{3}?")) {
                try {
                    // this will throw an exception if getUnit() doesn't return a currency code.
                    Currency c = Currency.getInstance(unit);
                    StringBuffer f = new StringBuffer();

                    if (c.getSymbol().length() == 1) {
                        f.append(c.getSymbol());
                    }

                    if (c.getDefaultFractionDigits() == 0) {
                        f.append("#,##0");
                    }
                    else {
                        f.append("#,##0.");

                        for (int i = 0; i < c.getDefaultFractionDigits(); i++) {
                            f.append('0');
                        }
                    }

                    if (c.getSymbol().length() != 1) {
                        f.append(" " + c.getSymbol());
                    }

                    formatter = new MessageFormat("{0,number," + f + "}");
                }
                catch (Exception e) {
                    // ignore - the if (formatter==null) will handle this below.
                }
            }
            else {
                formatter = new MessageFormat("{0,number}");
            }
        }
        else if (isNumberType()) {
            String pattern = getFormatOption("pattern");
            formatter = new MessageFormat("{0, number"+(pattern==null ? "" : ","+pattern)+"}");
        }
        else if (isDateType()) {
            String pattern = getFormatOption("pattern");
            formatter = new MessageFormat("{0, date"+(pattern==null ? "" : ","+pattern)+"}");
        }
        else if (isChoiceType()) {
            String options = getFormatOption("options");
            formatter = new MessageFormat("{0, choice"+(options==null ? "" : ","+options)+"}");
        }
        // If all else fails, assume the format is one MessageFormat can handle.
        if (formatter == null) {
            formatter = new MessageFormat("{0," + getLocalFormat() + "}");
        }

        return formatter;
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
        else {
            return value.indexOf('?')>=0;
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return ("yesorno".equals(getLocalFormat()));
    }

    /**
     * Return true if the type (format) of this attribute is note.
     * @return true if this is a note, false otherwise.
     */
    public boolean isNoteType() {
        if (getLocalFormat()==null) {
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
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
            new CoreProxy().logError("Attribute: "+id+" has no defined format");
            return false;
        }
        
        return (getLocalFormat().startsWith("date"));
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
