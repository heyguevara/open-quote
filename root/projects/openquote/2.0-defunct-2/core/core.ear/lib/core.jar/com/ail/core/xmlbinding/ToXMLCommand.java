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
 * This command is responsible for converting objects into strings of XML that
 * represent them.
 * The arguments and returns used during this process are described in (and
 * encapsulated by an instance of) ToXMLArg.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/xmlbinding/ToXMLCommand.java,v $
 */
public class ToXMLCommand extends Command implements ToXMLArg {
	/** instance of arg to deligate to. */
    private ToXMLArg args=null;

	/**
     * Default constructor.
     */
    public ToXMLCommand() {
        super();
		args=new ToXMLArgImp();
    }

	/**
     * Accept the argument object to be passed into the entry point.
     * @param arg An instance of ToXMLArg
     */
    public void setArgs(CommandArg arg) {
		this.args=(ToXMLArg)arg;
    }

	/**
     * Return the arguments used (processed by) the entry point.
     * @return An instance of ToXMLArg
     */
    public CommandArg getArgs() {
		return args;
    }

	/**
     * Fetch the ObjectIn value. ObjectIn represents the object to be
     * converted into XML.
     * @return The object to be marshalled
     */
    public Object getObjectIn() {
        return args.getObjectIn();
    }

	/**
     * Set the value of the ObjectIn property.
     * @param objectIn The new value.
     */
    public void setObjectIn(Object objectIn) {
        args.setObjectIn(objectIn);
    }

	/**
     * Fetch the XmlOut value. XmlOut represents the result of the marshal
     * process: a string of XML built from <code>objectIn</code>
     * @return marshalled XML object.
     */
    public XMLString getXmlOut() {
		return args.getXmlOut();
    }

	/**
     * Set the value of the XmlOut property.
     * @param xmlOut The new value.
     */
    public void setXmlOut(XMLString xmlOut) {
		args.setXmlOut(xmlOut);
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
