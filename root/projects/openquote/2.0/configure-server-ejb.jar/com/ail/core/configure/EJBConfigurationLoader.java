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
package com.ail.core.configure;

import java.util.Collection;

import javax.ejb.EJB;

import com.ail.core.VersionEffectiveDate;

/**
 * This configuration loader acts as a facade to the the configuration server services.
 * It is intended to be used by remote clients (e.g. web start apps) which don't have direct
 * access to loader being used by the server side. In effect the remote client's requests for
 * configuration information are relayed to the server side configuration server, which in
 * turn relays them to whatever mechanism the server is using (e.g. JDBC).
 */
public class EJBConfigurationLoader extends AbstractConfigurationLoader {
    @EJB
    static EJBLoader loader;
    
    private void handleException(EJBConfigurationException e) {
        throw e.getBaseErrorCause();
    }

	/**
     * Load a namespace's configuration.
     * @param namespace The namespace to load.
     * @param date The date to load the namespace for.
	 * @throws UnknownNamespaceError if the namespace does not exist, or is not valid for the date specificed.
     * @return The configuration.
     */
	public Configuration loadConfiguration(String namespace, VersionEffectiveDate date) {
        Configuration config=null;

        try {
            config=loader.loadConfiguration(namespace, date);
        } catch(EJBConfigurationException e) {
            handleException(e);
        }

        return config;
    }

	/**
     * Save a configuration into the database.
	 * All namespace records have a validfrom and a validto date stamp. A record
     * with validto set to zero indicates the latest config. This method will
     * update the current latest record, setting its validto to now. It will then
     * insert a new record with validFrom set to now+1ms, and validTo set to zero.<p>
     * If either the database or table specified in loader.properties does not
     * exist on the first attempt to save a configuration, this method will
     * attempt to create both. For this to work the db user specified in loader.properties
     * must have the necessary 'GRANTS' for table and database creation.
     * @param namespace The namespace the configuration is associated with.
     * @param config The configuration to be saved.
     * @throws BootstrapConfigurationError if the configuration cannot be serialized, or the database access fails.
     */
    public void saveConfiguration(String namespace, Configuration config) {
        try {
            loader.saveConfiguration(namespace, config);
        } catch(EJBConfigurationException e) {
            handleException(e);
        }
    }

    /**
     * Return a list of all the namespaces in the database which have a validTo
     * date of 0.
     * @return A list of currently defined namespaces as Strings
     */
    public Collection<String> getNamespaces() {
        Collection<String> ret=null;
        try {
            ret=loader.getNamespaces();
        }
        catch(EJBConfigurationException e) {
            handleException(e);
        }

        return ret;
    }

    public Collection<ConfigurationSummary> getNamespacesSummary() {
        Collection<ConfigurationSummary> ret=null;

        try {
            ret=loader.getNamespacesDetail();
        } 
        catch(EJBConfigurationException e) {
            handleException(e);
        }

        return ret;
    }

    public Collection<ConfigurationSummary> getNamespacesHistorySummary(String namespace) {
        Collection<ConfigurationSummary> ret=null;

        try {
            ret=loader.getNamespacesHistoryDetail(namespace);
        } 
        catch(EJBConfigurationException e) {
            handleException(e);
        }

        return ret;
    }

    /**
     * When the configuration handler is asked to "reset", it passes that request onto
     * the loader currently in user. The loader should reset any internal state in this
     * method.
     */
    public void reset() {
        try {
            loader.reset();
        } catch(EJBConfigurationException e) {
            handleException(e);
        }
    }

    /**
     * Delete ALL configuration information. This will include all historical configuration
     * information. <p>
     * <b>NOTE: ALL CONFIGURATION INFORMATION WILL BE LOST!</b>
     */
    public void purgeAllConfigurations() {
        try {
            loader.purgeAllConfigurations();
        } catch(EJBConfigurationException e) {
            handleException(e);
        }
    }

    /**
     * Delete the repository holding configuration information. This not only removes all
     * configuration information {@link #purgeAllConfigurations} but also removes the
     * repository itself.<p>
     * <b>NOTE: ALL CONFIGURATION INFORMATION WILL BE LOST!</b>
     */
    public void deleteConfigurationRepository() {
        try {
            loader.deleteConfigurationRepository();
        } catch(EJBConfigurationException e) {
            handleException(e);
        }
    }

    @Override
    public boolean isConfigurationRepositoryCreated() {
        return loader.isConfigurationRepositoryCreated();
    }
}









