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
package com.ail.core.xmlbinding.castor;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import javax.xml.transform.Result;

import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.XMLContext;
import org.xml.sax.InputSource;

import com.ail.core.configure.ConfigurationError;

/**
 * The methods defined in this class are shared by the Castor ToxML and FromXML
 * services.
 */
public class CastorMappingContext {
    private  XMLContext xmlContext=new XMLContext();

    /**
     * Initializing Castor is a relatively time consuming process which we don't want to repeat
     * for every marshal/unmarshal request. Fortunately, Castor provides an XMLContext class which,
     * once initialized, can be used to quickly create marshallers and unmarshallers. This method 
     * takes takes care of setting up an XMLContext.
     */
    public CastorMappingContext() {
        try {
            StringReader localMappingReader = null;
            InputStream defaultMappingStream = null;

            
            // set xml style "mixed" or "lower". With "mixed" you get
            // "elementName", with "lower" you get "element-name"
            xmlContext.setProperty("org.exolab.castor.xml.naming", "mixed");

            // create a new Mapping
            Mapping mapping = new Mapping();

            // add the default mapping to it
            defaultMappingStream = CastorMappingContext.class.getResourceAsStream("CastorBaseMapping.xml");
            mapping.loadMapping(new InputSource(defaultMappingStream));

            xmlContext.addMapping(mapping);

            defaultMappingStream.close();

            if (localMappingReader != null) {
                localMappingReader.close();
            }
        } catch (IOException e) {
            throw new ConfigurationError("Failed to read mapping resource file (CastorBaseMapping.xml).", e);
        } catch (MappingException e) {
            throw new ConfigurationError("Mapping file invalid (CastorBaseMapping.xml).", e);
        }
    }

    public CastorMappingContext(String additionalMapping) {
        this();

        if (additionalMapping!=null && additionalMapping.length()>0) {
            try {
                StringReader localMappingReader=null;
        
                // create a java.io.Reader around the mapping definition
                localMappingReader=new StringReader(additionalMapping);
        
                // create a castor xml mapping around the input source
                Mapping mapping = new Mapping();
                mapping.loadMapping(new InputSource(localMappingReader));
        
                xmlContext.addMapping(mapping);
            } catch (MappingException e) {
                throw new ConfigurationError("Mapping additional invalid.", e);
            }
        }
    }
    
    public Marshaller createMarshaller() {
        Marshaller marshaller=xmlContext.createMarshaller();

        marshaller.setProperty("org.exolab.castor.xml.proxyInterfaces", "org.hibernate.proxy.HibernateProxy");
        
        // Have the marshaller include 'xsi:type' attributes in the output
        marshaller.setMarshalExtendedType(true);

        // Turn validation off to avoid URL hits
        marshaller.setValidation(false);

        // Stop the conversion of < to &lt; etc.
        marshaller.addProcessingInstruction(Result.PI_DISABLE_OUTPUT_ESCAPING, "");

        return marshaller;
    }

    public Unmarshaller createUnmarshaller() {
        Unmarshaller unmarshaller=xmlContext.createUnmarshaller();
        
        // Many configs (etc) contain embedded text and we don't want to lose the formatting.
        unmarshaller.setWhitespacePreserve(true);

        unmarshaller.setClassLoader(Thread.currentThread().getContextClassLoader());

        // Enable xinclude
        unmarshaller.setProperty("org.exolab.castor.sax.features", "http://apache.org/xml/features/xinclude,http://xml.org/sax/features/namespaces");
        
        return unmarshaller;
    }
}
