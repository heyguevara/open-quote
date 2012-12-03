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

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ail.core.command.Command;
import com.ail.core.command.Argument;
import com.ail.core.command.CommandInvocationError;
import com.ail.core.configure.Builder;
import com.ail.core.configure.Builders;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.ConfigurationResetError;
import com.ail.core.configure.Configure;
import com.ail.core.configure.Group;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.Types;
import com.ail.core.configure.XMLMapping;
import com.ail.core.configure.finder.GetClassListService.GetClassListCommand;
import com.ail.core.document.Document;
import com.ail.core.document.GenerateDocumentService.GenerateDocumentCommand;
import com.ail.core.factory.Factory;
import com.ail.core.factory.FactoryHandler;
import com.ail.core.logging.BootLogger;
import com.ail.core.logging.Logging;
import com.ail.core.logging.Severity;
import com.ail.core.logging.LoggingService.LoggingCommand;
import com.ail.core.persistence.CloseSessionService.CloseSessionCommand;
import com.ail.core.persistence.CreateService.CreateCommand;
import com.ail.core.persistence.DeleteService.DeleteCommand;
import com.ail.core.persistence.LoadService.LoadCommand;
import com.ail.core.persistence.OpenSessionService.OpenSessionCommand;
import com.ail.core.persistence.Persistence;
import com.ail.core.persistence.QueryService.QueryCommand;
import com.ail.core.persistence.UpdateService.UpdateCommand;
import com.ail.core.product.DuplicateProductException;
import com.ail.core.product.Product;
import com.ail.core.product.ProductDetails;
import com.ail.core.product.UnknownProductException;
import com.ail.core.product.ListProductsService.ListProductsCommand;
import com.ail.core.product.NewProductTypeService.NewProductTypeCommand;
import com.ail.core.product.RegisterProductService.RegisterProductCommand;
import com.ail.core.product.RemoveProductService.RemoveProductCommand;
import com.ail.core.product.ResetAllProductsService.ResetAllProductsCommand;
import com.ail.core.product.ResetProductService.ResetProductCommand;
import com.ail.core.product.UpdateProductService.UpdateProductCommand;
import com.ail.core.validator.Validator;
import com.ail.core.validator.ValidatorService.ValidatorCommand;
import com.ail.core.validator.ValidatorResult;
import com.ail.core.xmlbinding.FromXMLService.FromXMLCommand;
import com.ail.core.xmlbinding.ToXMLService.ToXMLCommand;
import com.ail.core.xmlbinding.XMLBinding;

/**
 * This class is analogous to Java's java.lang.System class. It acts as a facade
 * for the services offered by the core. Its purpose it to simplify access to
 * the services, and so make using the services simpler.
 */
public class Core implements ConfigurationOwner, Configure, Factory, Logging, Persistence, XMLBinding, Validator, Product, Document, Serializable {
	public static final String CoreNamespace=Core.class.getName();
    private static final String MODIFIER_SEPARATOR = "/";
	private CoreUser coreUser=null;

    /**
	 * Clients are obliged to pass an instance of themselves in when they
     * create an instance of Core. This forces the contract between client
     * and core to be a tight well defined one.
     * @param coreUser The client wanting to use the core.
     */
    public Core(CoreUser coreUser) {
		this.coreUser=coreUser;
    }

    private void setCoreUser(CoreUser coreUser) {
        this.coreUser=coreUser;
	}

    public CoreUser getCoreUser() {
        return this.coreUser;
    }

	/**
     * Get the configuration owner for this instance of core.
     * The configuration owner will be either the core user (specified in the
     * constructor for this class), or this class itself. If the class that is
     * using this instance implements ConfigurationOwner then it owns its own
     * configuration, if it doesn't than the cores configuration is used instead.
     * @return The current config owner
     */
	private ConfigurationOwner getConfigurationOwner() {
		return (coreUser instanceof ConfigurationOwner) ? (ConfigurationOwner)coreUser : this;
    }

	/**
     * Get the core's version effective date. This date is used to determine
     * which versions of object and configurations to use when handling requests
     * made by the core. The client (coreUser) will have defines their own
     * version effective date, this one is only used as a fall-back.<p>
     * The core's version effective date is always based on the date/time that
     * this instance of the core was instantiated.
     * @return Date The core's version effective date
     */
	public VersionEffectiveDate getVersionEffectiveDate() {
        return coreUser.getVersionEffectiveDate();
    }

	/**
     * Save (create/update) the configuration for the current namespace.
	 * If the coreUser is a configuration owner (implements ConfigurationOwner),
     * then the configuration is save in the namespace associated with that owner.
     * Otherwise the core system's own configuration is updated.<p>
     * Note:Calling setConfiguration does not alter the configuration currently
     * being used. The configuration being used is determined by the callers
     * getVersionEffectiveDate(), this selects the version of configuration that
     * should be used by <code>getParameter()</code> for example.
     * @param config New configuration to be stored.
     * @see com.ail.core.configure.ConfigurationOwner
     * @see com.ail.core.CoreUser#getVersionEffectiveDate
	 * @see com.ail.core.Core#getParameter
     */
    public void setConfiguration(Configuration config) {
        ConfigurationHandler.getInstance().saveConfiguration(getConfigurationOwner(), config, this);
    }

	/**
     * Load the configuration for the current namespace.
	 * If the coreUser is a configuration owner (implements ConfigurationOwner),
     * then its configuration (the configuration associated with the owner) is
     * returned. Otherwise the core system's configuration is returned. The
     * configuration returned is version dependent - the callers getVersionEffectiveDate()
     * method will be invoked to determin which version of configuration to return.
     * @return The configuration associated with the current namespace.
     * @see com.ail.core.configure.ConfigurationOwner
     * @see com.ail.core.CoreUser#getVersionEffectiveDate
     */
    public Configuration getConfiguration() {
        return ConfigurationHandler.getInstance().loadConfiguration(getConfigurationOwner(), getCoreUser(), this);
    }

	/**
     * This method is used internally by the configure sub-system when an
     * instance of the core is being use by a non-ConfigurationOwner.
     * @return The Core systems configuration namespace.
     */
    public String getConfigurationNamespace() {
        return (coreUser.getConfigurationNamespace()!=null) ? coreUser.getConfigurationNamespace() : CoreNamespace;
    }

	/**
     * This method creates and saves a configuration containing just enough
     * information for the core (through resetConfiguration()) to load and save
     * the factory default configuration.
	 */
    private void saveBootstrapConfiguration() {
        Configuration config=new Configuration();
        config.setName("Bootstrap Core configuration");
        config.setVersion("1.0");
        config.setWho("boostrap");
        config.setNamespace("com.ail.core.Core");
        
        Builders blds=new Builders();
        config.setBuilders(blds);
        
        Builder b=new Builder();
        blds.addBuilder(b);
        b.setName("ClassBuilder");
        b.setFactory("com.ail.core.factory.ClassFactory");
        
        Types ts=new Types();
        config.setTypes(ts);
        com.ail.core.configure.Type t=new com.ail.core.configure.Type();
        ts.addType(t);
        t.setName("com.ail.core.xmlbinding.CastorFromXMLService");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.command.ClassAccessor");
        Parameter p=new Parameter();
        p.setName("ServiceClass");
        p.setValue("com.ail.core.xmlbinding.CastorFromXMLService");
        t.addParameter(p);
        
        t=new com.ail.core.configure.Type();
        ts.addType(t);
        p=new Parameter();
        t.setName("com.ail.core.xmlbinding.FromXMLService.FromXMLCommand");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.xmlbinding.FromXMLCommandImpl");
        p.setName("Accessor");
        p.setValue("com.ail.core.xmlbinding.CastorFromXMLService");
        t.addParameter(p);
        
        setConfiguration(config);
    }

	/**
     * Reset the core's configuration to its factory default.
     * The factory configuration that will be reset to is defined in the resource
     * file CoreDefaultConfig.xml.
     */
    public void resetConfiguration() {
		// Save the current core user, to be restored before we return.
		CoreUser savedCoreUser=getCoreUser();
		
		setCoreUser(new CoreUserImpl(CoreUserImpl.SELECT_LATEST_CONFIGURATIONS, null, null));
        
		try {
			// In order to 'fromXML' the contents of CoreDefaultConfig.xml, we'll
            // need to be able to access the fromXML service, hence we need some
            // minimal "bootstrap" configuration.
			saveBootstrapConfiguration();
			clearConfigurationCache();
			Thread.sleep(1);

			Configuration factoryConfig;
			
			factoryConfig=loadCoreDefaultConfig();
			factoryConfig=loadAndMergeAnnotatedConfig(factoryConfig);

			// reset the configuration
			setConfiguration(factoryConfig);
		}
        catch(Exception e) {
			throw new ConfigurationResetError("Failed to reset Core configuration", e);
        }
		finally {
		    // restore the saved core user
            setCoreUser(savedCoreUser);
        }
    }

    Configuration loadAndMergeAnnotatedConfig(Configuration factoryConfig) throws IOException, XMLException {
        // load the merged annotation config
        InputStream in=null;
        XMLString factoryConfigXML=null;;
        
        try {
            in = this.getClass().getResourceAsStream("AnnotatedTypeConfig.xml");
            factoryConfigXML = new XMLString(in);
        }
        finally {
            in.close();
        }

        // marshal the config XML into an instance of Configuration.
        Configuration annotatedConfig = fromXML(Configuration.class, factoryConfigXML);
        
        // merge the value from the annotated config into the factory's config.
        factoryConfig.mergeWithDataFrom(annotatedConfig, this);

        return factoryConfig;
    }

    /**
     * Load config from CoreDefaultConfig.xml and return
     */
    private Configuration loadCoreDefaultConfig() throws IOException, XMLException {
        // load the CoreDefaultConfig resource into an XMLString
        InputStream in=this.getClass().getResourceAsStream("CoreDefaultConfig.xml");
        XMLString factoryConfigXML=new XMLString(in);

        // marshal the config XML into an instance of Configuration
        return fromXML(Configuration.class, factoryConfigXML);
    }

    /**
     * Fetch the named group the from current configuration.
     * The "current configuration" is defined by the configuration namespace
     * and the version effective date. The namespace is taken either from the
     * core user if they implement ConfigurationOwner, or from the core itself.
     * The versionEffectiveDate comes from the core user.<p>
     * The group's name may be dot separated indicating
     * that the group is nested within other groups.
     * @param name The name of the group to be returned.
     * @return The configuration group, or null if one is not defined for this namespace and version effective date.
     */
    public Group getGroup(String name) {
        return ConfigurationHandler.getInstance().getGroup(name, getConfigurationOwner(), getCoreUser(), this);
    }

    /**
     * Return the source of the configuration being used by this instance of core. As configurations optionally
     * have "parent" configuration that they inherit from, this method returns a collection of sources with
     * one element for each configuration in the hierarchy.
     * @param owner The configuration's owner
     * @param user The user asking for the source
     * @param core The core being used - and who's source will be returned
     * @return The sources from which the configuration was loaded.
     */
    public Collection<String> getConfigurationSource() {
        return ConfigurationHandler.getInstance().getConfigurationSources(getConfigurationOwner(), getCoreUser(), this);
    }
    
    public Collection<String> getConfigurationNamespaceParent() {
        return ConfigurationHandler.getInstance().getConfigurationNamespaceParent(getConfigurationOwner(), getCoreUser(), this);
    }
    
    /**
     * Fetch the named parameter from current configuration.
     * The "current configuration" is defined by the configuration namespace
     * and the version effective date. The namespace is taken either from the
     * core user if they implement ConfigurationOwner, or from the core itself.
     * The versionEffectiveDate comes from the core user.<p>
     * The parameter's name may be dot seperated indicating
     * that the parameter is nested within one of more groups.
     * @param name The name of the parameter to be returned.
     * @return The parameter, or null if one is not defined for this namespace and version effective date.
     */
    public Parameter getParameter(String name) {
        return ConfigurationHandler.getInstance().getParameter(name, getConfigurationOwner(), getCoreUser(), this);
    }

    /**
     * Fetch the value of the named parameter from current configuration.
     * The "current configuration" is defined by the configuration namespace
     * and the version effective date. Note: The following two pieces of
     * code are exactly equivalent:<p><ol>
     * <li><code>String value=null;<br>
     * Parameter p=core.getParameter("paramName");<br>
     * if (p!=null) {<br>
     * &nbsp;&nbsp;&nbsp;&nbsp;value=p.getValue();<br>
     * }</code></li>
     * <li><code>String value=core.getParameterValue("paramName");</code></li></ul>
     * </p>
     * @param name The name of the parameter to return a value for.
     * @return The parameter's value, or null if it is not defined for this namespace and version effective date.
     */
    public String getParameterValue(String name) {
        return ConfigurationHandler.getInstance().getParameterValue(name, getConfigurationOwner(), getCoreUser(), this);
    }

    /**
     * Return the value of a parameter or a default if it is null.
     * @see #getParameterValue
     * @param name The name of the parameter
     * @param defaultValue The default value
     * @return The parameter's value, or the value of default if it is undefined.
     */
    public String getParameterValue(String name, String defaultValue) {
        return ConfigurationHandler.getInstance().getParameterValue(name, defaultValue, getConfigurationOwner(), getCoreUser(), this);
    }

	/**
     * Fetch all the Parameters in a group and return them as a java.util.Properties.
     * This is a utility method returns the parameters in a named group
     * and creates a Properties object with them. Each Parameter's name and value
     * attributes are mapped into a property in the Properties object.<p>
     * If the group specified does not exist, null is returned. If the group does
     * exist but contains no Parameters, an empty Properties object is returned.
     * @param name The name of the group whose parameters will be used.
     * @return The parameters as properties, or null.
     */
	public Properties getParametersAsProperties(String name) {
        return ConfigurationHandler.getInstance().getParametersAsProperties(name, getConfigurationOwner(), getCoreUser(), this);
    }

    /**
     * Create an instance of the specified command, and invoke it with the
     * argument provided. The results returned by the command are returned.
     * @param commandName The name of the command to invoke.
     * @param arguments The arguments to pass to the service.
     * @return The arguments as returned from the service.
     * @throws BaseException Any exception thrown by the service.
     * @deprecated Use {@link #invokeService(Class, LoggingArgument)} instead
     */
    public Argument invokeService(String commandName, Argument arguments) throws BaseException {
        Command command=newCommand(commandName, Command.class);
        arguments.setCallersCore(new CoreUserImpl(getCoreUser()));
        command.setArgs(arguments);
        command.invoke();
        return command.getArgs();
    }

    /**
     * Create an instance of the specified command, and invoke it with the
     * argument provided. The results returned by the command are returned.
     * @param commandClass The command class to invoke.
     * @param arguments The arguments to pass to the service.
     * @return The arguments as returned from the service.
     * @throws BaseException Any exception thrown by the service.
     */
    @SuppressWarnings("unchecked")
    public <T extends Argument> T invokeService(Class<? extends Command> commandClass, T arguments) throws BaseException {
        Command command=newCommand(commandClass);
        arguments.setCallersCore(new CoreUserImpl(getCoreUser()));
        command.setArgs(arguments);
        command.invoke();
        return (T)command.getArgs();
    }

    /**
     * Create an instance of the specified command, and invoke it with the
     * argument provided. The results returned by the command are returned.
     * @param commandName The name of the command to invoke.
     * @param commandClass The class of the command to invoke.
     * @param arguments The arguments to pass to the service.
     * @return The arguments as returned from the service.
     * @throws BaseException Any exception thrown by the service.
     */
    @SuppressWarnings("unchecked")
    public <T extends Argument> T invokeService(String commandName, Class<? extends Command> commandClass, T arguments) throws BaseException {
        Command command=newCommand(commandName, commandClass);
        arguments.setCallersCore(new CoreUserImpl(getCoreUser()));
        command.setArgs(arguments);
        command.invoke();
        return (T)command.getArgs();
    }

    /**
     * Create a new instance of the command specified. The details of the type
     * to be created are loaded from the callers configuration. This method is
     * distinct from {@link #newCommand(Class, String)}. This method simple looks
     * for a configured command with the name <i>commandName</i>, whereas method 
     * looks for a command named after the fully qualified name of the Class, and
     * modified by the value of String (e.g. <i>"com.ail.core.LoggerCommand/stdout"</i>)
     * @param commandName The name of the command to create an instance of
     * @param clazz The expected type of the resulting command 
     * @return An instance of the command.
     */
    @SuppressWarnings("unchecked")
    public <T extends Command> T newCommand(String commandName, Class<T> clazz) {
        return (T)FactoryHandler.getInstance().newCommand(commandName, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the command specified with a modifier. The details of the type
     * to be created are loaded from the callers configuration.
     * @param commandName The name of the command to create an instance of
     * @param modifier A modifier to apply to the command name.
     * @param clazz The expected type of the resulting command 
     * @return An instance of the command.
     */
    @SuppressWarnings("unchecked")
    public <T extends Command> T newCommand(String commandName, String modifier, Class<T> clazz) {
        return (T)FactoryHandler.getInstance().newCommand(commandName+MODIFIER_SEPARATOR+modifier, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the command specified. The details of the type
     * to be created are loaded from the callers configuration.
     * @param clazz The class of the type to be created.
     * @return An instance of the command.
     */
    public <T extends Command> T newCommand(Class<T> clazz) {
        return FactoryHandler.getInstance().newCommand(clazz, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the command specified. The details of the type
     * to be created are loaded from the callers configuration based on the 
     * class' name and the additional modifier. This method is distinct from 
     * {@link #newCommand(String, Class)}. This method looks in configuration
     * for a command named after the fully qualified name of the Class, and
     * modified by the value of String (e.g. <i>"com.ail.core.LoggerCommand/stdout"</i>);
     * whereas, that method simple looks for a configured command with the 
     * name <i>commandName</i>
     * @param clazz The class of the type to be created.
     * @param modifier select the specific configuration required. 
     * @return An instance of the command.
     */
    public <T extends Command> T newCommand(Class<T> clazz, String modifier) {
        return FactoryHandler.getInstance().newCommand(clazz, modifier, getConfigurationOwner(), this);
    }

	/**
     * Create a instance of the named type object.
	 * The named type is looked up in the current configuration, and
     * created from the specification held.
     * @param typeName The name of the type to create.
     * @return The type object ready for use.
     */
    public Object newType(String typeName) {
		return FactoryHandler.getInstance().newType(typeName, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the named type. The typeName argument
     * relates to a type in the callers configuration which defines the
     * specifics of the type to be created. 
     * the name of the type required.
     * @param typeName The name use to load the type's details.
     * @param clazz The expected type of the resulting command 
     * @return An instance of a type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T newType(String typeName, Class<T> clazz) {
        return (T)FactoryHandler.getInstance().newType(typeName, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the named type with a modifier. The typeName argument
     * relates to a type in the callers configuration which defines the specifics of 
     * the type to be created. The modifier is applied to the typeName to arrive at
     * the name of the type required.
     * @param typeName The name use to load the type's details.
     * @param modifer to be applied
     * @param clazz The expected type of the resulting command 
     * @return An instance of a type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T newType(String typeName, String modifier, Class<T> clazz) {
        return (T)FactoryHandler.getInstance().newType(typeName+MODIFIER_SEPARATOR+modifier, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the specified type. The clazz argument
     * relates to a type in the callers configuration which defines the
     * specifics of the type to be created.
     * @param clazz The class to return an instance for.
     * @return An instance of a type.
     */
	public <T extends Object> T newType(Class<T> clazz) {
	    return FactoryHandler.getInstance().newType(clazz, getConfigurationOwner(), this);
    }

    /**
     * Create a new instance of the specified type. The clazz and modifier arguments
     * relate to a type in the callers configuration which defines the
     * specifics of the type to be created.
     * @param clazz The class to return an instance for.
     * @return An instance of a type.
     */
    public <T extends Object> T newType(Class<T> clazz, String modifier) {
        return FactoryHandler.getInstance().newType(clazz, modifier, getConfigurationOwner(), this);
    }

    /**
     * Output a message to the Debug logging channel.
     * Messages written to this channel are of interest to developers, and to
     * anyone trying to debug a system problem. The channel would generally
     * only be turned on when a problem is being investigated.
     * @param message The text of the message to be output.
     */
    public void logDebug(String message, Throwable cause) {
		try {
            LoggingCommand cmd=newCommand("DebugLogger", LoggingCommand.class);
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.DEBUG);
            cmd.invoke();
		}
        catch(Throwable e) {
            logUnloggable(Severity.DEBUG, message, cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logDebug(String message) {
        logDebug(message, null);
    }

    /**
     * Output a message to the Info logging channel.
	 * This channel is designed to take messages that are of interest during
     * normal operations. For example, "System ready", "Configuration reloaded".
     * @param message The text of the message to be output.
     */
    public void logInfo(String message, Throwable cause) {
		try {
            LoggingCommand cmd=newCommand("InfoLogger", LoggingCommand.class);
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.INFO);
            cmd.invoke();
		}
        catch(Throwable e) {
            logUnloggable(Severity.INFO, message, cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logInfo(String message) {
        logInfo(message, null);
    }

    /**
     * Output a message to the Warning logging channel.
	 * Messages written to this channel indicate that something unexpected
     * occurred, but that it was dealt with and is not thought (by the developer)
     * to be if great importance.
     * @param message The text of the message to be output.
     */
    public void logWarning(String message, Throwable cause) {
		try {
            LoggingCommand cmd=newCommand("WarningLogger", LoggingCommand.class);
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.WARNING);
            cmd.invoke();
		}
        catch(Throwable e) {
            logUnloggable(Severity.WARNING, message, cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logWarning(String message) {
        logWarning(message, null);
    }

    /**
     * Output a message to the Error logging channel.
	 * The error channel is reserved for messages that describe serious
     * system problems. The problem didn't stop processing, but is significant
     * enough to require investigation.
     * @param message The text of the message to be output.
     */
    public void logError(String message, Throwable cause) {
		try {
		    LoggingCommand cmd=newCommand("ErrorLogger", LoggingCommand.class);
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.ERROR);
            cmd.invoke();
		}
        catch(Throwable e) {
            logUnloggable(Severity.ERROR, message, cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logError(String message) {
        logError(message, null);
    }

    /**
     * Output a message to the Fatal logging channel.
	 * An error is fatal if it stops the operation being processed. For example,
     * if the systems configuration information is defined in an inconsistent way
     * a fatal error is generated.
     * @param message The text of the message to be output.
     */
    public void logFatal(String message, Throwable cause) {
		try {
		    LoggingCommand cmd=newCommand("FatalLogger", LoggingCommand.class);
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.FATAL);
            cmd.invoke();
		}
        catch(Throwable e) {
            logUnloggable(Severity.FATAL, message, cause);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logFatal(String message) {
        logFatal(message, null);
    }

    public void logUnloggable(Severity severity, String message, Throwable cause) {
        BootLogger.log(coreUser.getClass(), coreUser.getVersionEffectiveDate(), severity, message, cause);
    }
    
    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T extends Type> T update(T object) {
        try {
            UpdateCommand command=newCommand("Update", UpdateCommand.class);
            command.setObjectArg(object);
            command.invoke();
            return (T)command.getObjectArg();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * @inheritDoc
     */
    public List<?> query(String queryName, Object... queryArgs) {
        try {
            QueryCommand command=newCommand("Query", QueryCommand.class);
            command.setQueryNameArg(queryName);
            command.setQueryArgumentsArg(queryArgs);
            command.invoke();
            return command.getResultsListRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T extends Type> T load(Class<T> type, long systemId) {
        try {
            LoadCommand command=newCommand("Load", LoadCommand.class);
            command.setTypeArg(type);
            command.setSystemIdArg(systemId);
            command.invoke();
            return (T)command.getObjectRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * @inheritDoc
     */
    public void delete(Type type) {
        try {
            DeleteCommand command=newCommand("Delete", DeleteCommand.class);
            command.setObjectArg(type);
            command.invoke();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * @inheritDoc
     */
    public Type queryUnique(String queryName, Object... queryArgs) {
        try {
            QueryCommand command=newCommand("Query", QueryCommand.class);
            command.setQueryNameArg(queryName);
            command.setQueryArgumentsArg(queryArgs);
            command.invoke();
            return command.getUniqueResultRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T extends Type> T create(T object) {
        try {
            CreateCommand command=newCommand("Create", CreateCommand.class);
            command.setObjectArg(object);
            command.invoke();
            return (T)command.getObjectArg();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * @inheritDoc
     */
    public void openPersistenceSession() {
        try {
            newCommand(OpenSessionCommand.class).invoke();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }
    
    /**
     * @inheritDoc
     */
    public void closePersistenceSession() {
        try {
            newCommand(CloseSessionCommand.class).invoke();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e.toString(), e);
        }
    }
    
    /**
     * Transform XML into an object. The XMLString is taken to be an xml
     * representation of an instance of the specified class. This method
     * translates the XML into an instance of the class and returns that
     * instance.
     * @param clazz The Class represented by <code>xml</code>
     * @param xml The XML representing and instance of <code>clazz</code>
     * @return an instance of <code>clazz</code> built up from <code>xml</code>
     */
    @SuppressWarnings("unchecked")
    public <T extends Object> T fromXML(Class<T> clazz, XMLString xml) throws XMLException {
        FromXMLCommand cmd = newCommand(FromXMLCommand.class);
        XMLMapping mapping = ConfigurationHandler.getInstance().getXMLMapping(getConfigurationOwner(), getCoreUser(), this);

        cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
        cmd.setClassIn(clazz);
        cmd.setXmlMappingInOut(mapping);
        cmd.setXmlIn(xml);

        try {
            cmd.invoke();
        } catch (XMLException e) {
            throw e;
        } catch (BaseException e) {
            throw new CommandInvocationError(e);
        }
        return (T) cmd.getObjectOut();
    }

	/**
     * Transform an object into its XML representation.
     * @param obj The object to be transformed.
     * @return An XMLString representing <code>obj</code>
     */
    public XMLString toXML(Object obj) {
        ToXMLCommand cmd = newCommand(ToXMLCommand.class);
        XMLMapping mapping = ConfigurationHandler.getInstance().getXMLMapping(getConfigurationOwner(), getCoreUser(), this);

        cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
        cmd.setObjectIn(obj);
        cmd.setXmlMappingInOut(mapping);

		try {
			cmd.invoke();
		}
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }

		return cmd.getXmlOut();
    }

	/**
	 * Validate a value
	 * @param key Key to identify validation required
	 * @param value Value to validate
	 * @return Validation results
	 */
	public ValidatorResult validate(String key, Object value) throws BaseException {
        
        // create command
        ValidatorCommand command = this.newCommand(ValidatorCommand.class);
        
        // add arguments
        command.setKeyArg(key);
        command.setValueArg(value);
        command.setCallersCore(getCoreUser());
        command.setValidationSpecArg(getParameterValue("ValidationSpec"));
        
        // invoke
        command.invoke();
        
        // get result
        return command.getValidatorResultRet();
	}

    public GetClassListCommand getClassList(GetClassListCommand arg) {
        throw new NotImplementedError("Core.getClassList");
    }

    /**
     * Return the security principal associated with this core's core user.
     * @return The core user's security principal, or null if none is defined.
     */
    public Principal getSecurityPrincipal() {  
        return coreUser.getSecurityPrincipal();
    }

    /**
     * Clear the configure cache associated with a product. 
     * @param productName Product to clear the cache for.
	 * @since 2.0
     */
    public void clearProductCache(String productName) {
        String configurationNamespace=Functions.productNameToConfigurationNamespace(productName);
        clearConfigurationCache(configurationNamespace);
    }
    
    /**
     * Fetch a collection of the products know to the system.
     * @return A collection of product names (as instances of java.lang.String).
     * @since 2.0
     */
    public Collection<ProductDetails> listProducts() {
        try {
            ListProductsCommand cmd=this.newCommand(ListProductsCommand.class);
            cmd.invoke();
            return cmd.getProductsRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }

    /**
     * Instantiate a type associated with a product. A product must define at least one type 
     * (see {@link #newProductType(String)}), but may define any number of additional types for 
     * use during its life-cycle; this method is used to instantiate specific types by name.<p>
     * For example, a complex insurance product may define several different types to describe
     * the assets the product covers. A commercial combined product might define a stock asset, a
     * vehicle asset, a safe asset, etc. Each of these is described within the product as a separate
     * named type. A client would use this method to instantiate these different types as and when 
     * they needed to be added to an instance of a commercial combined policy.
     * @param productName The product "owning" the type.
     * @param typeName The name of type to be instantiated.
     * @return The instantiated type.
     * @since 2.0
     */
    public Type newProductType(String productName, String typeName) {
        try {
            NewProductTypeCommand cmd=this.newCommand(NewProductTypeCommand.class);
            cmd.setProductNameArg(productName);
            cmd.setTypeNameArg(typeName);
            cmd.invoke();
            return cmd.getTypeRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }

    /**
     * Instantiate the default type associated with a product. Each product must define a default
     * type. This is the type (generally) instantiated at the beginning of the life-cycle. 
     * @param productName The name of the product to instantiate for.
     * @return The instantiated type.
     * @since 2.0
     */
    public Type newProductType(String productName) {
        try {
            NewProductTypeCommand cmd=this.newCommand(NewProductTypeCommand.class);
            cmd.setProductNameArg(productName);
            cmd.invoke();
            return cmd.getTypeRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }

    /**
     * Reset all the products know to the product manager. The product manager's configuration
     * contains a list of products. This method performs a system reset on each of the products
     * in that list. This amounts to performing a configuration reset using the products name
     * as the configuration namespace.
     * @since 2.0
     */
    public void resetAllProducts() {
        try {
            ResetAllProductsCommand cmd=this.newCommand(ResetAllProductsCommand.class);
            cmd.invoke();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }

    /**
     * Reset a specific product. Perform a system reset on the named product returning it
     * to it's factory state.
     * @param productName The name of the product to reset
     * @since 2.0
     */
    public void resetProduct(String productName) {
        try {
            ResetProductCommand cmd=this.newCommand(ResetProductCommand.class);
            cmd.setProductNameArg(productName);
            cmd.invoke();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }

    /**
     * Register a new product with the catalog.
     * @param details Details of the product to register
     * @throws DuplicateProduct if a product by the name specified (details.name) is already defined.
     * @since 2.0
     */
    public void registerProduct(ProductDetails productDetails) throws DuplicateProductException {
        try {
            RegisterProductCommand cmd=this.newCommand(RegisterProductCommand.class);
            cmd.setProductDetailsArg(productDetails);
            cmd.invoke();
        }
        catch(DuplicateProductException e) {
            throw e;
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }
    
    /**
     * Remove a product from the registry.
     * @param name Name of the product to remove.
     * @throws UnknownProduct if the named product is not defined.
     * @since 2.0
     */
    public void removeProduct(ProductDetails productDetails) throws UnknownProductException {
        try {
            RemoveProductCommand cmd=this.newCommand(RemoveProductCommand.class);
            cmd.setProductDetailsArg(productDetails);
            cmd.invoke();
        }
        catch(UnknownProductException e) {
            throw e;
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }
    
    /**
     * Update the product registries details wrt the details passed in.
     * @param name The name of the product to be updated.
     * @param details Details to store
     * @throws UnknownProduct if the product (name) isn't defined.
     * @since 2.0
     */
    public void updateProduct(String productName, ProductDetails productDetails) throws UnknownProductException {
        try {
            UpdateProductCommand cmd=this.newCommand(UpdateProductCommand.class);
            cmd.setProductNameArg(productName);
            cmd.setProductDetailsArg(productDetails);
            cmd.invoke();
        }
        catch(UnknownProductException e) {
            throw e;
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }
    
    /**
     * Generate a document and return it as a byte[]. The product named "<i>productName</i>" is assumed to include a 
     * document definition by the name "<i>documentDefinition</i>". Document definitions define all of the information
     * that the generator needs in order to render a document except for dynamic data - this is provided my <i>model</i>.
     * <p/>
     * The type of document returned (pdf, rtf, word, etc) is determined by the document definition.
     * <p/>
     * This method should be assumed to work entirely in memory - i.e. it does not persist the document at all.
     * <p/>
     * A loose contract exist between the document definition and the <i>model</i>. A definition is written with the
     * expectation that it can take whatever dynamic data it needs from the model it is given. Generation will fail
     * if the model passed in does not match the definition's expectations.
     * @param productName The name of product which 'owns' the document definition.
     * @param documentDefinitionName the name of the definition to use.
     * @param model The dynamic data satisfying references in the document definition.
     * @return The rendered document.
     */
    public byte[] generateDocument(String productName, String documentDefinitionName, Type model) {
        try {
            GenerateDocumentCommand cmd=this.newCommand(GenerateDocumentCommand.class);
            cmd.setProductNameArg(productName);
            cmd.setDocumentDefinitionArg(documentDefinitionName);
            cmd.setModelArg(model);
            cmd.invoke();
            return cmd.getRenderedDocumentRet();
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
    }

    /**
     * Reset the server side cache used to hold configuration information.
     */
    public void clearConfigurationCache() {
        ConfigurationHandler.resetCache();
    }

    /**
     * Reset the server side cache used to hold configuration information.
     * @param namespace The namespace to be cleared from the cache.
     */
    public void clearConfigurationCache(String namespace) {
        ConfigurationHandler.reset(namespace);
    }
}
