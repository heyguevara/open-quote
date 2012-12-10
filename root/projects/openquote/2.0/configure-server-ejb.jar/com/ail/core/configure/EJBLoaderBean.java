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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Collection;

import javax.annotation.Resource;
import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.core.BaseError;
import com.ail.core.VersionEffectiveDate;

/**
 * This class acts as a facade to the configuration loader being used on the
 * server side. It is use by the EJBConfigurationLoader (client) to provide
 * access to server side configuration for remote clients (e.g. web start apps)
 * which need to access the server's configuration information.
 */
@Stateless
@Remote(EJBLoader.class)
public class EJBLoaderBean implements EJBLoaderLocal {
    SessionContext ctx=null;
    AbstractConfigurationLoader loader=null;

    @Resource
    public void setSessionContext(SessionContext sessionContext) {
        ctx=sessionContext;
    }

    /**
     * Get an instance of the configuration loader being used
     * @return The loader currently in use.
     */
    private AbstractConfigurationLoader getLoader() {
        if (loader==null) {
            loader=AbstractConfigurationLoader.loadLoader();
        }

        return loader;
    }


    /**
     * Delegate to the local configuration loader's loadConfiguration method.
     * @param namespace Namespace to load.
     * @param date Date to load the namespace for (version effective date).
     * @return The loaded configuration.
     * @throws EJBException
     */
    public Configuration loadConfiguration(String namespace, VersionEffectiveDate date) throws EJBConfigurationException {
        try {
            return getLoader().loadConfiguration(namespace, date);
        }
        catch(BaseError error) {
            throw new EJBConfigurationException(error);
        }
    }

    /**
     * Performs the same function as loadConfiguration, but returns a byte array. This method
     * is used by the web service based config loader.
     * @param namespace Namespace to load
     * @param date Date to load configuration for.
     * @return The loaded configuration
     */
    public byte[] loadConfigurationAsByteArray(String namespace, VersionEffectiveDate date) throws EJBConfigurationException {
        Configuration config=loadConfiguration(namespace, date);

        try {
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(config);
            oos.flush();

            return baos.toByteArray();
        }
        catch(EJBConfigurationException e) {
            throw e;
        }
        catch(Exception e) {
            throw new EJBConfigurationException("Failed to convert Configuration to byte[]", e);
        }
    }

    /**
     * Delegate to the local configuration loader's saveConfiguration method.
     * @param namespace Namespace to load.
     * @param config The configuration to save.
     * @throws EJBException
     */
    public void saveConfiguration(String namespace, Configuration config) throws EJBConfigurationException {
        try {
            getLoader().saveConfiguration(namespace, config);
        }
        catch(BaseError error) {
            throw new EJBConfigurationException(error);
        }
    }

    /**
     * Delegate to the local configuration loader's saveConfiguration method.
     * @param namespace Namespace to load.
     * @param config The configuration to save.
     * @throws EJBException
     */
    public int saveConfiguration(String namespace, byte[] config) throws EJBConfigurationException {

        try {
            InputStream is = new ByteArrayInputStream(config);
            ObjectInputStream objis = new ObjectInputStream(is);
            Configuration con = (Configuration)objis.readObject();

            saveConfiguration(namespace, con);
        }
        catch(EJBConfigurationException e) {
            throw e;
        }
        catch(Exception e) {
            throw new EJBConfigurationException("Failed to convert byte[] into Configuration", e);
        }

        // web services must have a return!
        return 0;
    }

    /**
     * Delegate to the local configuration loader's getNamespaces method.
     * @return A collection of namespaces (instances of String).
     * @throws EJBException
     */
    public Collection<String> getNamespaces() throws EJBConfigurationException {
        try {
            return getLoader().getNamespaces();
        }
        catch(BaseError error) {
            throw new EJBConfigurationException(error);
        }
    }

    /**
     * Delegate to the local configuration loader's getNamespacesDetail method.
     * @return A collection of namespaces (instances of {@link com.ail.core.configuration.ConfigurationSummary ConfigurationSummary} ).
     * @throws EJBException
     */
    public Collection<ConfigurationSummary> getNamespacesDetail() throws EJBConfigurationException {
        try {
            return getLoader().getNamespacesSummary();
        }
        catch(BaseError error) {
            throw new EJBConfigurationException(error);
        }
    }

    /**
     * Delegate to the local configuration loader's getNamespacesHistoryDetail method.
     * @return A collection of namespaces (instances of {@link com.ail.core.configuration.ConfigurationSummary ConfigurationSummary} ).
     * @throws EJBException
     */
    public Collection<ConfigurationSummary> getNamespacesHistoryDetail(String namespace) throws EJBConfigurationException {
        try {
            return getLoader().getNamespacesHistorySummary(namespace);
        }
        catch(BaseError error) {
            throw new EJBConfigurationException(error);
        }
    }

    /**
     * Pass reset requests onto the loader.
     */
    public int reset() {
        getLoader().reset();
        return 0;
    }

    /**
     * Pass purgeAllConfigurations requests onto the loader.
     */
    public int purgeAllConfigurations() {
        getLoader().purgeAllConfigurations();
        return 0;
    }

    /**
     * Pass deleteConfigurationRepository requests onto the loader.
     */
    public int deleteConfigurationRepository() {
        getLoader().deleteConfigurationRepository();
        return 0;
    }
}
