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

import javax.annotation.security.RolesAllowed;
import javax.ejb.EJBException;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.jws.WebMethod;
import javax.jws.WebService;

import org.jboss.ejb3.annotation.SecurityDomain;
import org.jboss.ws.api.annotation.WebContext;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.CoreUser;
import com.ail.core.StatelessComponent;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.finder.GetClassListCommandImpl;

@Configurable
@Stateless
@Remote(Server.class)
@Local(ServerLocal.class)
@WebService
@WebContext(contextRoot="configure", urlPattern="server", authMethod = "BASIC")
@SecurityDomain("other")
@RolesAllowed({"Administrator"})
public class ServerBean extends StatelessComponent implements CoreUser {

    public ServerBean() {
        initialise("com.ail.core.configure.server.ServerBean");
    }

    /**
     * Reset the Core's configuration to its factory settings.
     */
    @WebMethod
    @RolesAllowed({"Administrator"})
    public void resetCoreConfiguration() {
        getCore().resetConfiguration();
        clearConfigurationCache();
        setVersionEffectiveDate(new VersionEffectiveDate());
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
    @WebMethod
    @RolesAllowed({"Administrator"})
    public void resetNamedConfiguration(String name) throws EJBException  {
        resetConfig(name);
        clearConfigurationCache();
        setVersionEffectiveDate(new VersionEffectiveDate());
    }

    /**
     * Reset a named configuration owner to its factory settings. The <code>name</code> argument
     * may be "all" in which case a predefined set of configurations (defined in this component's config)
     * are reset.
     * @param name The class name of the ConfigurationOwner, or "all" to reset the predefined list.
     * @throws EJBException if the configurationOwner class cannot be found/instantiated.
     */
    @WebMethod
    @RolesAllowed({"Administrator"})
    public void resetAllConfigurations() throws EJBException  {
        // reset the Core config first
        resetCoreConfiguration();
        
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
        
        clearConfigurationCache();
        setVersionEffectiveDate(new VersionEffectiveDate());
    }

    /**
     * Reset the server side cache used to hold configuration information.
     */
    @WebMethod
    @RolesAllowed({"Administrator"})
    public void clearConfigurationCache() throws EJBException  {
        ConfigurationHandler.resetCache();
    }

    /**
     * Reset the server side cache used to hold configuration information.
     * @param namespace The namespace to be cleared from the cache.
     */
    @WebMethod
    @RolesAllowed({"Administrator"})
    public void clearNamedConfigurationCache(String namespace) throws EJBException  {
        ConfigurationHandler.reset(namespace);
    }

    @WebMethod(exclude=true)
    public GetNamespacesCommandImpl getNamespaces(GetNamespacesCommandImpl arg) throws EJBException  {
        return invokeCommand("GetNamespaces", arg);
    }

    @WebMethod(exclude=true)
    public GetConfigurationCommandImpl getConfiguration(GetConfigurationCommandImpl arg) throws EJBException  {
        return invokeCommand("GetConfiguration", arg);
    }

    @WebMethod(exclude=true)
    public SetConfigurationCommandImpl setConfiguration(SetConfigurationCommandImpl arg) throws EJBException  {
        return invokeCommand("SetConfiguration", arg);
    }

    /**
     * Service wrapper business method for the GetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    @WebMethod(exclude=true)
    public GetCommandScriptCommandImpl getCommandScript(GetCommandScriptCommandImpl arg) throws EJBException  {
        return invokeCommand("GetCommandScript", arg);
    }

    /**
     * Service wrapper business method for the SetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    @WebMethod(exclude=true)
    public SetCommandScriptCommandImpl setCommandScript(SetCommandScriptCommandImpl arg) throws EJBException  {
        return invokeCommand("SetCommandScript", arg);
    }

    /**
     * Service wrapper business method for the GetClassList service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    @WebMethod(exclude=true)
    public GetClassListCommandImpl getClassList(GetClassListCommandImpl arg) throws EJBException  {
        return invokeCommand("GetClassList", arg);
    }
    
    /**
     * Service wrapper business method for the DeployCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    @WebMethod(exclude=true)
    public DeployCarCommandImpl deployCar(DeployCarCommandImpl arg) throws EJBException  {
        return invokeCommand("DeployCar", arg);
    }

    /**
     * Service wrapper business method for the PackageCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    @WebMethod(exclude=true)
    public PackageCarCommandImpl packageCar(PackageCarCommandImpl arg) throws EJBException  {
        return invokeCommand("PackageCar", arg);
    }

    /**
     * Service wrapper business method for the CatalogCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    @WebMethod(exclude=true)
    public CatalogCarCommandImpl catalogCar(CatalogCarCommandImpl arg) throws EJBException  {
        return invokeCommand("CatalogCar", arg);
    }
}
