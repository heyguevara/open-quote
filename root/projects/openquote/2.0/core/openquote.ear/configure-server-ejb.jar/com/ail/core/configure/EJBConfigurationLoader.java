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

import java.rmi.RemoteException;
import java.util.Collection;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.rmi.PortableRemoteObject;

import com.ail.core.BaseError;
import com.ail.core.VersionEffectiveDate;

/**
 * This configuration loader acts as a facade to the the configuration server services.
 * It is intended to be used by remote clients (e.g. web start apps) which don't have direct
 * access to loader being used by the server side. In effect the remote client's requests for
 * configuration information are relayed to the server side configuration server, which in
 * turn relays them to whatever mechanism the server is using (e.g. JDBC).
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2005/07/31 18:04:03 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/configure-server-ejb.jar/com/ail/core/configure/EJBConfigurationLoader.java,v $
 */
public class EJBConfigurationLoader extends AbstractConfigurationLoader {
    EJBLoader server=null;

    private void handleException(RemoteException e) {
        for(Throwable t=e ; t!=null ; t=t.getCause()) {
            if (t instanceof BaseError) {
                throw (BaseError)t;
            }
            else if (t instanceof EJBConfigurationException) {
                throw (BaseError)((EJBConfigurationException)t).getServerSideCause();
            }
        }
        throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
    }

    private void handleException(EJBConfigurationException e) {
        throw e.getBaseErrorCause();
    }

    /**
     * Fetch an instance of the remote facade that will perform the configuration
     * operations for us.
     * @return Instance of the facade EJB.
     */
    public EJBLoader getLoaderEJB() {
        try {
            Context context=new InitialContext();
            EJBLoaderHome home=(EJBLoaderHome)context.lookup("EJBLoader");
            server=(EJBLoader)PortableRemoteObject.narrow(home.create(), EJBLoader.class);
            return server;
        } catch(Throwable e) {
            throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
        }
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
            config=getLoaderEJB().loadConfiguration(namespace, date);
        } catch(RemoteException e) {
            handleException(e);
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
            getLoaderEJB().saveConfiguration(namespace, config);
        }  catch(RemoteException e) {
            handleException(e);
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
            ret=getLoaderEJB().getNamespaces();
        }
        catch(RemoteException e) {
            handleException(e);
        }
        catch(EJBConfigurationException e) {
            handleException(e);
        }

        return ret;
    }

    public Collection<ConfigurationSummary> getNamespacesSummary() {
        Collection<ConfigurationSummary> ret=null;

        try {
            ret=getLoaderEJB().getNamespacesDetail();
        } 
        catch(RemoteException e) {
            handleException(e);
        } 
        catch(EJBConfigurationException e) {
            handleException(e);
        }

        return ret;
    }

    public Collection<ConfigurationSummary> getNamespacesHistorySummary(String namespace) {
        Collection<ConfigurationSummary> ret=null;

        try {
            ret=getLoaderEJB().getNamespacesHistoryDetail(namespace);
        } 
        catch(RemoteException e) {
            handleException(e);
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
            getLoaderEJB().reset();
        } catch(RemoteException e) {
            handleException(e);
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
            getLoaderEJB().purgeAllConfigurations();
        } catch(RemoteException e) {
            handleException(e);
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
            getLoaderEJB().deleteConfigurationRepository();
        } catch(RemoteException e) {
            handleException(e);
        } catch(EJBConfigurationException e) {
            handleException(e);
        }
    }
}









