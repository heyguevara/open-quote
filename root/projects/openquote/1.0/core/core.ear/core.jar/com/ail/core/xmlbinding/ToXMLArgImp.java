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
import com.ail.core.command.CommandArgImp;
import com.ail.core.configure.XMLMapping;

/**
 * This is a concrete implementation of the CommandArg for the ToXML entry point.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/xmlbinding/ToXMLArgImp.java,v $
 */
public class ToXMLArgImp extends CommandArgImp implements ToXMLArg {
    private Object objectIn = null;
    private XMLString xmlOut = null;
    private XMLMapping xmlMappingInOut = null;

	/**
     * Fetch the ObjectIn value. ObjectIn represents the object to be
     * converted into XML.
     * @return The object to be marshalled
     */
    public Object getObjectIn() {
        return objectIn;
    }

	/**
     * Set the value of the ObjectIn property.
     * @param objectIn The new value.
     */
    public void setObjectIn(Object objectIn) {
        this.objectIn = objectIn;
    }

	/**
     * Fetch the XmlOut value. XmlOut represents the result of the marshal
     * process: a string of XML built from <code>objectIn</code>
     * @return marshalled XML object.
     */
    public XMLString getXmlOut() {
        return xmlOut;
    }

	/**
     * Set the value of the XmlOut property.
     * @param xmlOut The new value.
     */
    public void setXmlOut(XMLString xmlOut) {
        this.xmlOut = xmlOut;
    }

    /**
     * {@inheritDoc}
     */
    public XMLMapping getXmlMappingInOut() {
        return xmlMappingInOut;
    }

    /**
     * {@inheritDoc}
     */
    public void setXmlMappingInOut(XMLMapping xmlMapping) {
        this.xmlMappingInOut=xmlMapping;
    }
}
