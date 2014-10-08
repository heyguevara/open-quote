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

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;

/**
 * This class holds XML databinding information for a configuration. It holds
 * the mapping description - which is generally a string of XML, and transient
 * instances of a marshaller and unmarshaller object.
 */
@TypeDefinition
public class XMLMapping extends Type {
    static final long serialVersionUID = 5193477041052835669L;
    private String definition = null;
    private transient Object marshaller = null;
    private transient Object unmarshaller = null;

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
     * Get the XML marshaller. The XML databinding handlers may use this
     * property to cache an instance of the marshaller. The configuration
     * handler will ensure that this is handled in a version safe fashion.
     * @return An instance of a marshaller.
     */
    public Object getMarshaller() {
        return marshaller;
    }

    /**
     * Set the marshaller for this mapping.
     * @see #getMarshaller
     * @param marshaller The marshaller instance
     */
    public void setMarshaller(Object marshaller) {
        this.marshaller = marshaller;
    }

    /**
     * Get the unmarshaller.
     * @see #getMarshaller
     * @return An instance of the marshaller.
     */
    public Object getUnmarshaller() {
        return unmarshaller;
    }

    /**
     * Set the unmarshaller.
     * @see #getMarshaller
     * @param unmarshaller The XML Unmarshaller.
     */
    public void setUnmarshaller(Object unmarshaller) {
        this.unmarshaller = unmarshaller;
    }
}
