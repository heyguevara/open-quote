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

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.castor.mapping.BindingType;
import org.castor.mapping.MappingUnmarshaller;
import org.exolab.castor.mapping.Mapping;
import org.exolab.castor.mapping.MappingException;
import org.exolab.castor.mapping.MappingLoader;
import org.exolab.castor.xml.ClassDescriptorResolverFactory;
import org.exolab.castor.xml.XMLClassDescriptorResolver;
import org.xml.sax.InputSource;

import com.ail.core.configure.XMLMapping;

/**
 * The methods defined in this class are shared by the Castor ToxML and FromXML services.
 */
public class CastorMappingLoader {
    
    /**
     * Fetch a class resolver based on the mappings supplied: by default in CastorBaseMapping.xml; and per caller as
     * specified in the argument. Building a resolver is a reasonably cycle consuming activity, so the method saves
     * the resolved back onto the XMLMapping argument provided. If the caller provides an XMLMapping which already
     * contains a resolver, that resolver is simply returned.<p/>
     * The expectation is that the caller will cache the instance of XMLMapping that it passes in and use it again
     * in future calls to this method.
     * @param argsMappingInOut
     * @return A resolver for use with castor's marshaller and unmarshaller.
     * @throws MappingException
     * @throws IOException
     */
    public static  XMLClassDescriptorResolver fetchClassResolver(XMLMapping argsMappingInOut) throws MappingException, IOException {
        XMLClassDescriptorResolver resolver;

        // If a mapping has been defined...        
        if (argsMappingInOut.getMarshaller()==null) {
            StringReader localMappingReader=null;
            InputStream defaultMappingStream=null;
            
            // ... create a new Mapping
            Mapping mapping=new Mapping(Thread.currentThread().getContextClassLoader());
            
            // add the default mapping to it
            defaultMappingStream=CastorMappingLoader.class.getResourceAsStream("CastorBaseMapping.xml");
            mapping.loadMapping(new InputSource(defaultMappingStream));

            // if a local mapping definition was supplied, add it
            if (argsMappingInOut.getDefinition()!=null) {
   
                // create a java.io.Reader around the mapping definition
                localMappingReader=new StringReader(argsMappingInOut.getDefinition());
   
                // turn the reader into an or.xml.sax.InputSource
                InputSource source=new InputSource(localMappingReader);
   
                // create a castor xml mapping around the input source
                mapping.loadMapping(source);
            }

            MappingUnmarshaller mum = new MappingUnmarshaller();

            MappingLoader loader = mum.getMappingLoader(mapping, BindingType.XML);
            
            resolver = (XMLClassDescriptorResolver) ClassDescriptorResolverFactory.createClassDescriptorResolver(BindingType.XML);
            
            resolver.setMappingLoader(loader);

            // save the mapping file for use later, we'll use it here too before we return.
            argsMappingInOut.setMarshaller(resolver);

            defaultMappingStream.close();

            if (localMappingReader!=null) {
                localMappingReader.close();
            }
        }
        else {
            // use the mapping that was loaded and cached last time around.
            resolver=(XMLClassDescriptorResolver)argsMappingInOut.getMarshaller();
        }

        return resolver;
    }
}
