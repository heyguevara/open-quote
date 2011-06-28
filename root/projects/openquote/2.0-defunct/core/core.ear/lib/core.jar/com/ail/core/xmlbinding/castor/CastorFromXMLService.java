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

import java.io.StringReader;

import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;

import com.ail.core.Core;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.XMLException;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.XMLMapping;
import com.ail.core.xmlbinding.FromXMLArgImp;

/**
 * This entry point uses the castor framework to unmarshal an XMLString into
 * the object it represents.<p>
 * Two arguments are accepted:
 * <ul>
 * <li>classIn - The Class object to be used. The unmarshal will create an
 * instance of this class and populate it as the root element of the XMLString.</li>
 * <li>XmlIn - The XMLString representing the XML to be unmarshalled.</li>
 * </ul>
 * One return object is generated:
 * <ul>
 * <li>ObjectOut - The object resulting from the unmarshal.</li>
 * </ul>
 * These arguments and returns are encapsulated in an instance of FromXMLCommandArg.
 */
public class CastorFromXMLService extends Service {

	/** The argument object used by this entry point. */
    private FromXMLArgImp args=null;

	/**
     * This entry point has no Core requirements, so simply return null.
     * @return null
     */
	public Core getCore() {
        return null;
    }

	/**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
	public Version getVersion() {
		Version v=new Version();
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2007/04/15 22:18:34 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/xmlbinding/CastorFromXMLService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.3 $");
        return v;
    }

	/**
     * Setter used to the set the arguments that <code>invoke()</code> will
     * use when it is called.
     * @param args for invoke
     */
    public void setArgs(CommandArg args){
        this.args = (FromXMLArgImp)args;
    }

	/**
     * Getter returning the arguments used by this entry point.
     * @return An instance of FromXMLArgImp.
	 */
    public CommandArg getArgs() {
        return args;
    }

	/**
     * The 'business logic' of the entry point.
     */
	public void invoke() throws XMLException {
		StringReader reader=new StringReader(args.getXmlIn().toString());

		try {
            CastorMappingContext bindingContext;

            // If there's no marshaller yet...
            if (args.getXmlMappingInOut() == null) {
                args.setXmlMappingInOut(new XMLMapping());
            }

            if (args.getXmlMappingInOut().getBindingContext()==null) {
                String additionalMapping=args.getXmlMappingInOut().getDefinition();
                bindingContext=new CastorMappingContext(additionalMapping);
                args.getXmlMappingInOut().setBindingContext(bindingContext);
            }
            else {
                bindingContext=(CastorMappingContext)args.getXmlMappingInOut().getBindingContext();
            }
            
            Unmarshaller unmarshaller=bindingContext.createUnmarshaller();
            unmarshaller.setClass(args.getClassIn());
            unmarshaller.setClassLoader(this.getClass().getClassLoader());

            args.setObjectOut(unmarshaller.unmarshal(reader));
		}
        catch(MarshalException e) {
			throw new XMLException("Marshal exception ", e);
        }
        catch(ValidationException e) {
			throw new XMLException("Validation exception ", e);
        }
    }
}
