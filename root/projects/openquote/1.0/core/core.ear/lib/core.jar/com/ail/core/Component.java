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

package com.ail.core;

import java.io.InputStream;
import java.net.URL;

import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.ConfigurationResetError;

/**
 * This class is the abstract supclass of all Components. A component is
 * simply a way of grouping entry points together. It isn't required
 * that all components extend this class, but it does save implementation
 * effort.
 * @version $Revision: 1.6 $
 * @state $State: Exp $
 * @date $Date: 2007/06/04 11:56:23 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/Component.java,v $
 */
public abstract class Component implements CoreUser, ConfigurationOwner {

	/**
     * Return the version details of this component.
     * @return version details
     */
    public abstract Version getVersion();

    /**
     * A default component implemention returning the time now.
	 * Components with a critical dependency on selecting the
     * version effective date should override this method.
     * @return VersionEffectiveDate now.
     */
	public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }

    /**
     * Fetch the entry point's instance of the core.
     * @return The core being used by the entry points.
     */
	public abstract Core getCore();

    /**
     * Default component implementation of the setConfiguration
     * method. This method takes care of saving configrations
     * on the behalf of the entry point.
     * @param configuration The configuration to save.
     */
    public void setConfiguration(Configuration configuration) {
	    if (getCore()!=null) {
            getCore().setConfiguration(configuration);
	    }
    }

    /**
     * Default component implementation of the getConfiguration
     * method. This method takes care of loading configurations
     * on the behalf of the component.
     * @return The loaded configuration.
     */
    public Configuration getConfiguration() {
	    if (getCore()==null) {
            return null;
	    }

        return getCore().getConfiguration();
    }

    /**
     * Default component implementation of the getConfigurationNamespace
     * method. The name of the component class is returned.
     * @return The classes namespace
     */
    public String getConfigurationNamespace() {
	    return this.getClass().getName();
    }

    private void resetConfiguration(String name) {
	    if (getCore()==null) {
            return;
        }

		XMLString factoryConfigXML=null;
		
        try {
            // load the <name>DefaultConfig resource into an XMLString
            URL inputUrl=this.getClass().getResource(name+"DefaultConfig.xml");

            if (inputUrl==null) {
                inputUrl=new URL("http://localhost:8080/alfresco/cms/Product"+name.replace('.','/')+".xml");
            }
            
            InputStream inStream=inputUrl.openStream();

            if (inStream==null) {
                throw new ConfigurationResetError("Couldn't find class resource (name='"+name+"DefaultConfig.xml')");
            }

	        factoryConfigXML=new XMLString(inStream);

            // marshal the config XML into an instance of Configuration
	        Configuration factoryConfig=getCore().fromXML(Configuration.class, factoryConfigXML);

            // write details of where we loaded the config from back into the config so that
            // anyone who uses this configuraiton in future can see where it came from.
            factoryConfig.setSource(inputUrl.toExternalForm());
            
            // reset the configuration
	        setConfiguration(factoryConfig);
	    }
        catch(XMLException e) {
            e.printStackTrace(System.err);
            System.err.println(factoryConfigXML);
            throw new ConfigurationResetError("Default Configuration XML error for '"+name+"' sax error is:"+e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            System.err.println(factoryConfigXML);
	        throw new ConfigurationResetError("Failed to reset "+name+" configuration: "+e);
        }
    }

    /**
     * This method may be used by configuration owners who "share" a
     * configuration. It reset the configuration based on their
     * namespace rather than their classname.<br>
     * To use it they would overload the resetConfiguration method
     * as follows:<br>
     * <pre>
     * public void resetConfiguration() {
     *    super.resetConfigurationByNamespace();
     * }
     * </pre>
     */
    protected void resetConfigurationByNamespace() {
        // Derive the configuration's name from the namespace
        String cname=getConfigurationNamespace();
        String name=cname.substring(cname.lastIndexOf('.')+1);

        resetConfiguration(name);
    }

    /**
     * Default component implementation of the resetConfiguration method.
     * This implementation loads the default config from the class resource
     * file named "<Component name>DefaultConfig.xml". For an component
     * named 'Red', the resource 'RedDefaultConfig.xml' is loaded.<p>
     * Note: If the component's name ends with 'Bean' - as EJB based ones
     * do, the 'Bean' postfix is removed (RedBean -> Red).
     */
    public void resetConfiguration() {
        // Derive the configuration's name from the class name
        String cname=getConfigurationNamespace();
        String name=cname.substring(cname.lastIndexOf('.')+1);
        if (name.endsWith("Bean")) {
            name=name.substring(0, name.length()-4);
        }

        resetConfiguration(name);
    }
}
