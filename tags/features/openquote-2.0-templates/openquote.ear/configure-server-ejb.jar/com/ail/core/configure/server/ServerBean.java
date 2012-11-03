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
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import org.w3c.dom.Element;

import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.EJBComponent;
import com.ail.core.VersionEffectiveDate;
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
import com.ail.annotation.Configurable;

@Configurable
public class ServerBean extends EJBComponent implements SessionBean, CoreUser, ConfigurationOwner {
	private Core core=null;
    private SessionContext ctx=null;

    public ServerBean() {
        core=new Core(this);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void ejbCreate() throws CreateException {
		core=new Core(this);
	}

    /**
     * The version effective date for the bean is always 'now'.
     * @return version effective date.
     */
    public VersionEffectiveDate getVersionEffectiveDate() throws EJBException  {
        return new VersionEffectiveDate();
    }

    public Core getCore() throws EJBException  {
        return core;
    }

    /**
     * Reset the Core's configuration to its factory settings.
     */
    public void resetCoreConfiguration() throws EJBException  {
        core.resetConfiguration();
        ConfigurationHandler.resetCache();
    }

    /**
     * Reset a single named config.
     * @param name Name of config to reset
     */
    private void resetConfig(String name) {
        try {
            core.logDebug("Requesting reset for:"+name);
            Class<?> clazz=Class.forName(name);
            ConfigurationOwner owner=(ConfigurationOwner)clazz.newInstance();
            owner.resetConfiguration();
            core.logInfo("Reset successful for:"+name);
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
            core.resetConfiguration();
            ConfigurationHandler.resetCache();

            // loop through the AllNamespaceReset group, and reset all the configs named
            for(Parameter p: core.getGroup("NamespacesToResetOnResetAll").getParameter()) {
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

    /**
     * Expose services via XML. This method unmarshals the XML argument string into
     * an object, finds a method on the EJB to accept that object type as an argument
     * and invokes it. The result returned from the method is marshalled back into XM
     * and returned.<p>
     * The methods are invoked on the context's local interface if possible (if one
     * exists). If no local interface is found then the remote interface is used instead.
     * Invoking methods via the local/remote interface means that the deployment settings
     * for security and transacts will be honored.
     * @param xml XML argument to be passed to the service.
     * @return XML returned from the service.
     */
    public String invokeServiceXML(String xml) throws EJBException  {
        return super.invokeServiceXML(xml, ctx);
    }

    public Element[] invokeServiceSoap(Element[] xml) throws EJBException {
        return xml;
    }

    public GetNamespacesCommand getNamespaces(GetNamespacesCommand arg) throws EJBException  {
        return invokeCommand(core, "GetNamespaces", arg);
    }

    public GetConfigurationCommand getConfiguration(GetConfigurationCommand arg) throws EJBException  {
        return invokeCommand(core, "GetConfiguration", arg);
    }

    public SetConfigurationCommand setConfiguration(SetConfigurationCommand arg) throws EJBException  {
        return invokeCommand(core, "SetConfiguration", arg);
    }

    /**
     * Service wrapper business method for the GetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GetCommandScriptCommand getCommandScript(GetCommandScriptCommand arg) throws EJBException  {
        return invokeCommand(core, "GetCommandScript", arg);
    }

    /**
     * Service wrapper business method for the SetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public SetCommandScriptCommand setCommandScript(SetCommandScriptCommand arg) throws EJBException  {
        return invokeCommand(core, "SetCommandScript", arg);
    }


    /**
     * Service wrapper business method for the GetClassList service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GetClassListCommand getClassList(GetClassListCommand arg) throws EJBException  {
        return invokeCommand(core, "GetClassList", arg);
    }
    
    /**
     * Service wrapper business method for the DeployCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public DeployCarCommand deployCar(DeployCarCommand arg) throws EJBException  {
        return invokeCommand(core, "DeployCar", arg);
    }

    /**
     * Service wrapper business method for the PackageCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public PackageCarCommand packageCar(PackageCarCommand arg) throws EJBException  {
        return invokeCommand(core, "PackageCar", arg);
    }

    /**
     * Service wrapper business method for the CatalogCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public CatalogCarCommand catalogCar(CatalogCarCommand arg) throws EJBException  {
        return invokeCommand(core, "CatalogCar", arg);
    }

    /**
     * Hard code the namespace to "com.ail.core.configure.server.ServerBean". Generally,
     * the super class will automatically provide a namespace based on the class name, 
     * but for EJBs this can be a problem. Some app server generated containers effect
     * the name of the class causing the configuration to fail. Weblogic is one such.
     * @return The namespace of the configuration.
     */
    public String getConfigurationNamespace() {
        return "com.ail.core.configure.server.ServerBean";
    }
}
