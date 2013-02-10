/* Copyright Applied Industrial Logic Limited 2013. All rights Reserved */
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

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;

import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.ConfigurationResetError;

class ConfigurationResetHandler {
    private String name;
    private Core core;
    private ConfigurationOwner owner;

    ConfigurationResetHandler(Core core, ConfigurationOwner owner, String name) {
        this.name = name;
        this.core = core;
        this.owner = owner;
    }

    void invoke() {
        if (core == null) {
            return;
        }

        String resourcePath = null;
        String productPath = null;

        try {
            // load the <name>DefaultConfig resource into an XMLString
            resourcePath = name + "DefaultConfig.xml";
            URL inputUrl = owner.getClass().getResource(resourcePath);

            // if it didn't load as a class resource, try it as a product URL
            if (inputUrl == null) {
                String host = core.getParameterValue("ProductURLHandler.Host");
                String port = core.getParameterValue("ProductURLHandler.Port");
                productPath = "product://" + host + ":" + port + "/" + name.replace('.', '/') + ".xml";
                inputUrl = new URL(productPath);
            }

            InputStream inStream = inputUrl.openStream();

            if (inStream == null) {
                throw new ConfigurationResetError("Couldn't find class resource (name='" + name + "DefaultConfig.xml')");
            }

            XMLString factoryConfigXML = new XMLString(inStream);

            // marshal the config XML into an instance of Configuration
            Configuration factoryConfig = core.fromXML(Configuration.class, factoryConfigXML);

            // Add details of where we loaded the config from back into the
            // config so that anyone who uses this configuration in future can
            // see where it came from.
            factoryConfig.setSource(inputUrl.toExternalForm());

            // reset the configuration
            owner.setConfiguration(factoryConfig);
        } catch (FileNotFoundException e) {
            throw new ConfigurationResetError("Could not load configuration from either class resource: " + resourcePath + ", or url: " + productPath);
        } catch (XMLException e) {
            throw new ConfigurationResetError("Default Configuration XML error for '" + name + "' sax error", e);
        } catch (Exception e) {
            throw new ConfigurationResetError("Failed to reset " + name + " configuration.", e);
        }
    }
}
