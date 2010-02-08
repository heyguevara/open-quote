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
import com.ail.core.command.CommandArg;
import com.ail.core.configure.XMLMapping;

/**
 * This interface defines the Bean used to encapsulate the argument and return
 * values used by the ToXml entry point.
 */
public interface ToXMLArg extends CommandArg {
	/**
     * Fetch the ObjectIn value. ObjectIn represents the object to be
     * converted into XML.
     * @return The object to be marshalled
     */
    Object getObjectIn();

	/**
     * Set the value of the ObjectIn property.
     * @param objectIn The new value.
     */
    void setObjectIn(Object objectIn);

    /**
     * Get the value of XmlMapping. This describes how to map from xml into
     * java objects (and the reverse), and map be used by the service to
     * cache marshallers and unmarshallers.
     * @return The mapping, or null if it is not defined.
     */
    XMLMapping getXmlMappingInOut();

    /**
     * Set the XML mapping property.
     * @see #getXmlMappingIn
     * @param xmlMapping The mapping to use.
     */
    void setXmlMappingInOut(XMLMapping xmlMapping);

	/**
     * Fetch the XmlOut value. XmlOut represents the result of the marshal
     * process: a string of XML built from <code>objectIn</code>
     * @return marshalled XML object.
     */
    XMLString getXmlOut();

	/**
     * Set the value of the XmlOut property.
     * @param xmlOut The new value.
     */
    void setXmlOut(XMLString xmlOut);
}
