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

package com.ail.core.configure;

import com.ail.core.Type;

/**
 * This class holds XML data binding information for a configuration. It holds
 * the mapping description - which is generally a string of XML, and transient
 * instances of a bindingContext object.
 */
public class XMLMapping extends Type {
    static final long serialVersionUID = 5193477041052835669L;
    private String definition = null;
    private transient Object bindingContext = null;

    /**
     * Default constructor.
     */
    public XMLMapping() {
    }

    /**
     * Get the XML mapping definition. The actual format of the information
     * returned depends upon the data binding mechanism being used, but it
     * is generally an XML string that describes how XML nodes are mapped into
     * object instances.
     * @return String describing the mapping.
     */
    public String getDefinition() {
        return definition;
    }

    public String getDefinitionCDATA() {
        return "<![CDATA["+definition+"]]>";
    }

    /**
     * Set the XML mapping string
     * @see #getDefinition
     * @param definition The mapping string.
     */
    public void setDefinition(String definition) {
        this.definition = definition;
    }

    /**
     * Get the XML bindingContext. The XML data binding handlers may use this
     * property to cache an instance of the bindingContext. The configuration
     * handler will ensure that this is handled in a version safe fashion.
     * @return An instance of a bindingContext.
     */
    public Object getBindingContext() {
        return bindingContext;
    }

    /**
     * Set the bindingContext for this mapping.
     * @see #getMarshaller
     * @param bindingContext The bindingContext instance
     */
    public void setBindingContext(Object bindingContext) {
        this.bindingContext = bindingContext;
    }
}
