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

import com.ail.core.command.CommandArgImp;
import com.ail.core.XMLString;
import com.ail.core.configure.XMLMapping;

/**
 * This is the implementation of FromXMLArg used to pass arguments to and from the FromXML entry point(s).
 */
public class FromXMLArgImp extends CommandArgImp implements FromXMLArg {
    private Object objectOut = null;
    private XMLString xmlIn = null;
    private Class<?> classIn = null;
    private XMLMapping xmlMappingInOut = null;

    /**
     * Fetch the value of ObjectOut. This is the value retured from the
     * FromXML entry point, and represents the object created when <code>XmlIn</code> was unmarshalled.
     * @return Unmarshalled object
     */
    public Object getObjectOut() {
        return objectOut;
    }

    /**
     * Set the value of the ObjectOut property.
     * @param objectOut The value to be set.
     */
    public void setObjectOut(Object objectOut) {
        this.objectOut = objectOut;
    }

    /**
     * Fetch the XMLString that will be unmarshalled by the entry point.
     * This is the input to the unmarshal process, and should represent an
     * instance of the class specificed in <code>ClassIn</code>.
     * @return The XMLString to be unmarshalled.
     */
    public XMLString getXmlIn() {
        return xmlIn;
    }

    /**
     * Set the value of the XmlIn property.
     * @param xmlIn The value to be set.
     */
    public void setXmlIn(XMLString xmlIn) {
        this.xmlIn = xmlIn;
    }

    /**
     * Get the value of the ClassIn argument. This argument specifies the class which <code>XmlIn</code>
     * represents an instance of.
     * @return The class to be unmarshalled into.
     */
    public Class<?> getClassIn() {
        return classIn;
    }

    /**
     * Set the value of the ClassIn property.
     * @param classIn The value to be set.
     */
    public void setClassIn(Class<?> classIn) {
        this.classIn = classIn;
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
    public void setXmlMappingInOut(XMLMapping xmlMappingInOut) {
        this.xmlMappingInOut = xmlMappingInOut;
    }
}
