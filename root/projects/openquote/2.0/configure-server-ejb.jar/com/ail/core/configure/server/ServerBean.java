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

package com.ail.core.configure.server;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.CoreUser;
import com.ail.core.EJBComponent;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.finder.GetClassListService.GetClassListCommand;
import com.ail.core.configure.server.CatalogCarService.CatalogCarCommand;
import com.ail.core.configure.server.DeployCarService.DeployCarCommand;
import com.ail.core.configure.server.GetCommandScriptService.GetCommandScriptCommand;
import com.ail.core.configure.server.GetConfigurationService.GetConfigurationCommand;
import com.ail.core.configure.server.GetNamespacesService.GetNamespacesCommand;
import com.ail.core.configure.server.PackageCarService.PackageCarCommand;
import com.ail.core.configure.server.SetCommandScriptService.SetCommandScriptCommand;
import com.ail.core.configure.server.SetConfigurationService.SetConfigurationCommand;

@Configurable
@Stateless
public class ServerBean extends EJBComponent implements Server, CoreUser {
    private static final String NAMESPACE="com.ail.core.configure.server.ServerBean";
    private SessionContext ctx=null;

    public ServerBean() {
        initialise(NAMESPACE);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbCreate() throws CreateException {
        initialise(NAMESPACE);
	}

    /**
     * Reset the Core's configuration to its factory settings.
     */
    public void resetCoreConfiguration() throws EJBException  {
        getCore().resetConfiguration();
        ConfigurationHandler.resetCache();
    }

    /**
     * Reset a single named config.
     * @param name Name of config to reset
     */
    private void resetConfig(String name) {
        try {
            getCore().logDebug("Requesting reset for:"+name);
            Class<?> clazz=Class.forName(name);
            ConfigurationOwner owner=(ConfigurationOwner)clazz.newInstance();
            owner.resetConfiguration();
            getCore().logInfo("Reset successful for:"+name);
        }
        catch(ClassNotFoundException e) {
            throw new EJBException("Could not find configuration owner class:"+name);
        }
        catch(InstantiationException e) {
            throw new EJBException("Could not instantiate configuration owner: "+e);
        }
        catch(IllegalAccessException e) {
            throw new EJBException("Could not instantiate configuration owner: "+e);
        }
    }

    /**
     * Reset a named configuration owner to its factory settings. The <code>name</code> argument
     * may be "all" in which case a predefined set of configurations (defined in this component's config)
     * are reset.
     * @param name The class name of the ConfigurationOwner, or "all" to reset the predefined list.
     * @throws EJBException if the configurationOwner class cannot be found/instantiated.
     */
    public void resetNamedConfiguration(String name) throws EJBException  {
        if (name.equalsIgnoreCase("all")) {

            // reset the Core config first
            getCore().resetConfiguration();
            ConfigurationHandler.resetCache();

            // loop through the AllNamespaceReset group, and reset all the configs named
            for(Parameter p: getCore().getGroup("NamespacesToResetOnResetAll").getParameter()) {
                try {
                    resetConfig(p.getName());
                }
                catch(Throwable t) {
                    System.err.println("Error resetting configuration for namespace: "+p.getName());
                    t.printStackTrace(System.err);
                }
            }
        }
        else {
            resetConfig(name);
        }
    }

    /**
     * Reset the server side cache used to hold configuration information.
     */
    public void clearConfigurationCache() throws EJBException  {
        ConfigurationHandler.resetCache();
    }

    /**
     * Reset the server side cache used to hold configuration information.
     * @param namespace The namespace to be cleared from the cache.
     */
    public void clearNamedConfigurationCache(String namespace) throws EJBException  {
        ConfigurationHandler.reset(namespace);
    }

    public GetNamespacesCommand getNamespaces(GetNamespacesCommand arg) throws EJBException  {
        return invokeCommand("GetNamespaces", arg);
    }

    public GetConfigurationCommand getConfiguration(GetConfigurationCommand arg) throws EJBException  {
        return invokeCommand("GetConfiguration", arg);
    }

    public SetConfigurationCommand setConfiguration(SetConfigurationCommand arg) throws EJBException  {
        return invokeCommand("SetConfiguration", arg);
    }

    /**
     * Service wrapper business method for the GetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GetCommandScriptCommand getCommandScript(GetCommandScriptCommand arg) throws EJBException  {
        return invokeCommand("GetCommandScript", arg);
    }

    /**
     * Service wrapper business method for the SetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public SetCommandScriptCommand setCommandScript(SetCommandScriptCommand arg) throws EJBException  {
        return invokeCommand("SetCommandScript", arg);
    }


    /**
     * Service wrapper business method for the GetClassList service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GetClassListCommand getClassList(GetClassListCommand arg) throws EJBException  {
        return invokeCommand("GetClassList", arg);
    }
    
    /**
     * Service wrapper business method for the DeployCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public DeployCarCommand deployCar(DeployCarCommand arg) throws EJBException  {
        return invokeCommand("DeployCar", arg);
    }

    /**
     * Service wrapper business method for the PackageCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public PackageCarCommand packageCar(PackageCarCommand arg) throws EJBException  {
        return invokeCommand("PackageCar", arg);
    }

    /**
     * Service wrapper business method for the CatalogCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public CatalogCarCommand catalogCar(CatalogCarCommand arg) throws EJBException  {
        return invokeCommand("CatalogCar", arg);
    }
}
