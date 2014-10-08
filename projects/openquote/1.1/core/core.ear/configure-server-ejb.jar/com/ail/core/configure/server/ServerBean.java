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

import com.ail.core.BaseError;
import com.ail.core.BaseException;
import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.EJBComponent;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.AbstractCommand;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.finder.GetClassListArg;

/**
 * @version $Revision: 1.8 $
 * @state $State: Exp $
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/configure-server-ejb.jar/com/ail/core/configure/server/ServerBean.java,v $
 * @ejbHome <{ServerHome}>
 * @ejbRemote <{Server}>
 * @undefined
 * @displayName
 * @ejbLocal <{ServerLocal}>
 * @ejbLocalHome <{ServerLocalHome}>
 */
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

    public Version getVersion() throws EJBException  {
        Version v=(Version)core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2007/10/05 22:47:50 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/configure-server-ejb.jar/com/ail/core/configure/server/ServerBean.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.8 $");
        return v;
    }

    public Core getCore() throws EJBException  {
        return core;
    }

    /**
     * Reset the Core's configuration to its factory settings.
     */
    public void resetCoreConfiguration() throws EJBException  {
        core.resetConfiguration();
        ConfigurationHandler.reset();
    }

    /**
     * Reset a single named config.
     * @param name Name of config to reset
     */
    @SuppressWarnings("unchecked")
    private void resetConfig(String name) {
        try {
            core.logDebug("Requesting reset for:"+name);
            Class clazz=Class.forName(name);
            ConfigurationOwner owner=(ConfigurationOwner)clazz.newInstance();
            owner.resetConfiguration();
            core.logDebug("Reset successful for:"+name);
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
            ConfigurationHandler.reset();
            core.logDebug("Reset successful for:com.ail.core.Core");

            // loop through the AllNamespaceReset group, and reset all the configs named
            for(Parameter p: core.getGroup("NamespacesToResetOnResetAll").getParameter()) {
                resetConfig(p.getName());
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
        ConfigurationHandler.reset();
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
     * for security and transacts will be honoured.
     * @param xml XML argument to be passed to the service.
     * @return XML returned from the service.
     */
    public String invokeServiceXML(String xml) throws EJBException  {
        return super.invokeServiceXML(xml, ctx);
    }

    public Element[] invokeServiceSoap(Element[] xml) throws EJBException {
        return xml;
    }

    public GetNamespacesArg getNamespaces(GetNamespacesArg arg) throws EJBException  {
        try {
            AbstractCommand command=core.newCommand("GetNamespaces");
            command.setArgs(arg);
            command.invoke();
            return (GetNamespacesArg)command.getArgs();
        }
        catch(BaseException e) {
            throw new EJBException(e);
        }
        catch(BaseError e) {
            throw new EJBException(e.toString());
        }
    }

    public GetConfigurationArg getConfiguration(GetConfigurationArg arg) throws EJBException  {
        try {
            AbstractCommand command=core.newCommand("GetConfiguration");
            command.setArgs(arg);
            command.invoke();
            return (GetConfigurationArg)command.getArgs();
        }
        catch(BaseException e) {
            throw new EJBException(e);
        }
        catch(BaseError e) {
            throw new EJBException(e.toString());
        }
    }

    public SetConfigurationArg setConfiguration(SetConfigurationArg arg) throws EJBException  {
        try {
            AbstractCommand command=core.newCommand("SetConfiguration");
            command.setArgs(arg);
            command.invoke();
            return (SetConfigurationArg)command.getArgs();
        }
        catch(BaseException e) {
            throw new EJBException(e);
        }
        catch(BaseError e) {
            throw new EJBException(e.toString());
        }
    }

    /**
     * Service wrapper business method for the GetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GetCommandScriptArg getCommandScript(GetCommandScriptArg arg) throws EJBException  {
            try {
                com.ail.core.command.AbstractCommand command = core.newCommand("GetCommandScript");
                command.setArgs(arg);
                command.invoke();
                return (com.ail.core.configure.server.GetCommandScriptArg) command.getArgs();
            }
            catch (com.ail.core.BaseException e) {
                throw new com.ail.core.BaseServerException(e);
            }
            catch (com.ail.core.BaseError e) {
                throw new com.ail.core.BaseServerException(e);
            }
    }

    /**
     * Service wrapper business method for the SetCommandScript service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public SetCommandScriptArg setCommandScript(com.ail.core.configure.server.SetCommandScriptArg arg) throws EJBException  {
            try {
                com.ail.core.command.AbstractCommand command = core.newCommand("SetCommandScript");
                command.setArgs(arg);
                command.invoke();
                return (com.ail.core.configure.server.SetCommandScriptArg) command.getArgs();
            }
            catch (com.ail.core.BaseException e) {
                throw new com.ail.core.BaseServerException(e);
            }
            catch (com.ail.core.BaseError e) {
                throw new com.ail.core.BaseServerException(e);
            }
    }


    /**
     * Service wrapper business method for the GetClassList service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GetClassListArg getClassList(GetClassListArg arg) throws EJBException  {
            try {
                AbstractCommand command = core.newCommand("GetClassList");
                command.setArgs(arg);
                command.invoke();
                return (GetClassListArg) command.getArgs();
            }
            catch (com.ail.core.BaseException e) {
                throw new com.ail.core.BaseServerException(e);
            }
            catch (com.ail.core.BaseError e) {
                throw new com.ail.core.BaseServerException(e);
            }
    }
    
    /**
     * Service wrapper business method for the DeployCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public DeployCarArg deployCar(DeployCarArg arg) throws EJBException  {
            try {
                AbstractCommand command = core.newCommand("DeployCar");
                command.setArgs(arg);
                command.invoke();
                return (DeployCarArg) command.getArgs();
            }
            catch (com.ail.core.BaseException e) {
                throw new com.ail.core.BaseServerException(e);
            }
            catch (com.ail.core.BaseError e) {
                throw new com.ail.core.BaseServerException(e);
            }
    }

    /**
     * Service wrapper business method for the PackageCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public PackageCarArg packageCar(PackageCarArg arg) throws EJBException  {
            try {
                AbstractCommand command = core.newCommand("PackageCar");
                command.setArgs(arg);
                command.invoke();
                return (PackageCarArg) command.getArgs();
            }
            catch (com.ail.core.BaseException e) {
                throw new com.ail.core.BaseServerException(e);
            }
            catch (com.ail.core.BaseError e) {
                throw new com.ail.core.BaseServerException(e);
            }
    }

    /**
     * Service wrapper business method for the CatalogCar service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public CatalogCarArg catalogCar(CatalogCarArg arg) throws EJBException  {
            try {
                AbstractCommand command = core.newCommand("CatalogCar");
                command.setArgs(arg);
                command.invoke();
                return (CatalogCarArg) command.getArgs();
            }
            catch (com.ail.core.BaseException e) {
                throw new com.ail.core.BaseServerException(e);
            }
            catch (com.ail.core.BaseError e) {
                throw new com.ail.core.BaseServerException(e);
            }
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
