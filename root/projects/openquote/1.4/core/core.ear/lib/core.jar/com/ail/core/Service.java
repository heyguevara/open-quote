/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.ConfigurationResetError;

import java.io.InputStream;
import java.net.URL;
import java.security.Principal;

/**
 * The Service abstract class is implemented by all classes that may be exposed as
 * component entry points. <p>
 * In addition to the methods described here, an entry point implementation is also
 * expected to offer getter and setter methods which are used to pass arguments
 * to the entry point (setters called before invoke), and fetch the entry point's
 * results (getters, called after invoke).<p>
 * Each entry point has associated version and configuration information. The version
 * details are burned into the entry point's source code at build time. The entry point's
 * configuration can be modified at run-time using the setter provided.
 */
public abstract class Service<T extends CommandArg> extends Type implements CoreUser, ConfigurationOwner {
    protected T args;
    protected Core core;
    
    public Service() {
        core = new Core(this);
    }
    
    public void setCore(Core core) {
        this.core=core;
    }
    
    /**
     * Set the arguments to be used by this entry point's business logic.
     * @param args Arguments for the entry point to process.
     */
	public void setArgs(T args) {
	    this.args=args;
	}

	/**
     * Invoke the entry point's business logic. This is the core of the entry
     * point. Before this method is called, setArgs() will have been called
     * to supply the entry point with the arguments it will process. After
     * invoke() has been called, getArgs() will be called to retrieve the
     * results.
     */
    public abstract void invoke() throws BaseException;

	/**
     * A default entry point implementation returning the version effective date.
  	 * Entry points with a critical dependency on selecting the
     * version effective date should override this method. The default behaviour is
     * to use the version effective date from the CallersCore supplied in the Args
     * passed to the service. If that is null, then the date now is used.
     * @return Date date to use.
     */
	public VersionEffectiveDate getVersionEffectiveDate() {
        try {
            return getArgs().getCallersCore().getVersionEffectiveDate();
        }
        catch(NullPointerException e) {
            return new VersionEffectiveDate();
        }
    }

	/**
     * Get the arguments used by this entry point. "Arguments" in this context
     * are both the objects passed into the entry point, and the objects
     * returned by it. The entry point user will call getArgs() to retrieve
     * the entry points results.
     * @return The results of executing the entry point.
     */
    public T getArgs() {
        return args;
    }

	/**
     * Fetch the entry point's instance of the core.
     * @return The core being used by the entry points.
     */
	public Core getCore() {
	    return core;
	}

	/**
     * Default entry point implementation of the setConfiguration
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
     * Default entry point implementation of the getConfiguration
     * method. This method takes care of loading configurations
     * on the behalf of the entry point.
     * @return The loaded configuration.
     */
    public Configuration getConfiguration() {
		if (getCore()==null) {
            return null;
		}

		return getCore().getConfiguration();
    }

	/**
     * Default entry point implementation of the getConfigurationNamespace
     * method. The name of the entry point class is returned.
     * @return The classes namespace
     */
    public String getConfigurationNamespace() {
		return this.getClass().getName();
    }

    private void resetConfiguration(String name) {
	    if (getCore()==null) {
            return;
        }

        try {
            // load the <name>DefaultConfig resource into an XMLString
            URL inputUrl=this.getClass().getResource(name+"DefaultConfig.xml");

            if (inputUrl==null) {
                inputUrl=new URL("product://localhost:8080"+name.replace('.','/')+".xml");
            }
            
            InputStream inStream=inputUrl.openStream();

            if (inStream==null) {
                throw new ConfigurationResetError("Couldn't find class resource (name='"+name+"DefaultConfig.xml')");
            }

	        XMLString factoryConfigXML=new XMLString(inStream);

            // marshal the config XML into an instance of Configuration
	        Configuration factoryConfig=getCore().fromXML(Configuration.class, factoryConfigXML);

	        // write details of where we loaded the config from back into the config so that
            // anyone who uses this configuration in future can see where it came from.
            factoryConfig.setSource(inputUrl.toExternalForm());
            
            // reset the configuration
	        setConfiguration(factoryConfig);
	    }
        catch(XMLException e) {
            e.printStackTrace();
            throw new ConfigurationResetError("Default Configuration XML error for '"+name+"' sax error is:"+e.getMessage());
        }
        catch(Exception e) {
            e.printStackTrace();
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

        resetConfiguration("/"+cname.replace('.', '/'));
    }

	/**
     * Default entry point implementation of the resetConfiguration method.
     * This implementation loads the default config from the class resource
     * file named "<Entry Point Name>DefaultConfig.xml". For an entry point
     * named 'RedService', the resource 'RedDefaultConfig.xml' is loaded.
     */
    public void resetConfiguration() {
        String cname=this.getClass().getName();
        String name=cname.substring(cname.lastIndexOf('.')+1, cname.lastIndexOf("Service"));
        resetConfiguration(name);
    }

    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        try {
            return getArgs().getCallersCore().getSecurityPrincipal();
        }
        catch(NullPointerException e) {
            return null;
        }
    }
}
