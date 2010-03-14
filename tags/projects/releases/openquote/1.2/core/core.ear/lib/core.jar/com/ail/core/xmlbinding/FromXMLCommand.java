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

package com.ail.core.xmlbinding;

import com.ail.core.XMLString;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.XMLMapping;

/**
 * This command is responsible for converting strings of XML into instances
 * of the objects they represent.
 * The arguments and returns used during this process are described in (and
 * encapsulated by an instance of) FromXMLArg.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/xmlbinding/FromXMLCommand.java,v $
 */
@SuppressWarnings("unchecked")
public class FromXMLCommand extends Command implements FromXMLArg {
	/** instance of arg to delegate to. */
    private FromXMLArg args=null;

	/**
     * Default constructor
     */
	public FromXMLCommand() {
        args=new FromXMLArgImp();
    }

	/**
     * Accept the argument object to be passed into the entry point.
     * @param arg An instance of FromXMLArg
     */
    public void setArgs(CommandArg arg) {
		this.args=(FromXMLArg)arg;
    }

	/**
     * Return the arguments used (processed by) the entry point.
     * @return An instance of FromXMLArg
     */
    public CommandArg getArgs() {
		return args;
    }


	/**
     * Fetch the value of ObjectOut. This is the value retured from the
     * FromXML entry point, and represents the object created when
     * <code>XmlIn</code> was unmarshalled.
     * @return Unmarshalled object
     */
    public Object getObjectOut() {
		return args.getObjectOut();
    }

	/**
     * Set the value of the ObjectOut property.
     * @param objectOut The value to be set.
     */
    public void setObjectOut(Object objectOut) {
		args.setObjectOut(objectOut);
    }

	/**
     * Fetch the XMLString that will be unmarshalled by the entry point.
     * This is the input to the unmarshal process, and should represent an
     * instance of the class specificed in <code>ClassIn</code>.
     * @return The XMLString to be unmarshalled.
     */
    public XMLString getXmlIn() {
		return args.getXmlIn();
    }

	/**
     * Set the value of the XmlIn property.
     * @param xmlIn The value to be set.
     */
    public void setXmlIn(XMLString xmlIn) {
		args.setXmlIn(xmlIn);
    }

	/**
     * Get the value of the ClassIn argument.
     * This argument specifies the class which <code>XmlIn</code> represents
     * an instance of.
     * @return The class to be unmarshalled into.
     */
    public Class getClassIn(){
        return args.getClassIn();
    }

	/**
     * Set the value of the ClassIn property.
     * @param classIn The value to be set.
     */
    public void setClassIn(Class classIn){
        args.setClassIn(classIn);
    }

    /**
     * {@inheritDoc}
     */
    public XMLMapping getXmlMappingInOut() {
        return args.getXmlMappingInOut();
    }

    /**
     * {@inheritDoc}
     */
    public void setXmlMappingInOut(XMLMapping xmlMapping) {
        args.setXmlMappingInOut(xmlMapping);
    }
}
