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

import java.io.InputStream;
import java.io.Serializable;
import java.security.Principal;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import com.ail.core.command.AbstractCommand;
import com.ail.core.command.CommandArg;
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
import com.ail.core.configure.finder.GetClassListArg;
import com.ail.core.document.Document;
import com.ail.core.document.generatedocument.GenerateDocumentCommand;
import com.ail.core.factory.Factory;
import com.ail.core.factory.FactoryHandler;
import com.ail.core.logging.BootLogger;
import com.ail.core.logging.LoggerCommand;
import com.ail.core.logging.Logging;
import com.ail.core.logging.Severity;
import com.ail.core.persistence.CreateCommand;
import com.ail.core.persistence.DeleteCommand;
import com.ail.core.persistence.LoadCommand;
import com.ail.core.persistence.Persistence;
import com.ail.core.persistence.QueryCommand;
import com.ail.core.persistence.UpdateCommand;
import com.ail.core.product.DuplicateProductException;
import com.ail.core.product.Product;
import com.ail.core.product.ProductDetails;
import com.ail.core.product.UnknownProductException;
import com.ail.core.product.listproducts.ListProductsCommand;
import com.ail.core.product.newproducttype.NewProductTypeCommand;
import com.ail.core.product.registerproduct.RegisterProductCommand;
import com.ail.core.product.removeproduct.RemoveProductCommand;
import com.ail.core.product.resetallproducts.ResetAllProductsCommand;
import com.ail.core.product.resetproduct.ResetProductCommand;
import com.ail.core.product.updateproduct.UpdateProductCommand;
import com.ail.core.validator.Validator;
import com.ail.core.validator.ValidatorCommand;
import com.ail.core.validator.ValidatorResult;
import com.ail.core.xmlbinding.FromXMLCommand;
import com.ail.core.xmlbinding.ToXMLCommand;
import com.ail.core.xmlbinding.XMLBinding;

/**
 * This class is analogous to Java's java.lang.System class. It acts as a facade
 * for the services offered by the core. Its purpose it to simplify access to
 * the services, and so make using the services simpler.
 */
public class Core implements ConfigurationOwner, Configure, Factory, Logging, Persistence, XMLBinding, Validator, Product, Document, Serializable {
	public static final String CoreNamespace=Core.class.getName();
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

    private CoreUser getCoreUser() {
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
     * Get the core's version effective date. This date is used to determin
     * which versions of object and configurations to use when handling requests
     * made by the core. The client (coreUser) will have defines their own
     * version effective date, this one is only used as a fallback.<p>
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
        config.setVersion("$Revision: 1.16 $");
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
        t.setName("FromXMLService");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.command.ClassAccessor");
        Parameter p=new Parameter();
        p.setName("ServiceClass");
        p.setValue("com.ail.core.xmlbinding.CastorFromXMLService");
        t.addParameter(p);
        
        t=new com.ail.core.configure.Type();
        ts.addType(t);
        p=new Parameter();
        t.setName("FromXML");
        t.setBuilder("ClassBuilder");
        t.setKey("com.ail.core.xmlbinding.FromXMLCommand");
        p.setName("Accessor");
        p.setValue("FromXMLService");
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
		
		setCoreUser(new CoreUserImpl(CoreUserImpl.SelectLatestConfigurations, null, null));
        
		try {
			// In order to 'fromXML' the contents of CoreDefaultConfig.xml, we'll
            // need to be able to access the fromXML service, hence we need some
            // minimal configuration.
			saveBootstrapConfiguration();
			Thread.sleep(1);

			// load the CoreDefaultConfig resource into an XMLString
			InputStream in=this.getClass().getResourceAsStream("CoreDefaultConfig.xml");
			XMLString factoryConfigXML=new XMLString(in);

			// marshal the config XML into an instance of Configuration
            Configuration factoryConfig=fromXML(Configuration.class, factoryConfigXML);

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

    /**
     * Fetch the named group the from current configuration.
     * The "current configuration" is defined by the configuration namespace
     * and the version effective date. The namespace is taken either from the
     * core user if they implement ConfigurationOwner, or from the core itself.
     * The versionEffectiveDate comes from the core user.<p>
     * The group's name may be dot seperated indicating
     * that the group is nested within other groups.
     * @param name The name of the group to be returned.
     * @return The configuration group, or null if one is not defined for this namespace and version effective date.
     */
    public Group getGroup(String name) {
        return ConfigurationHandler.getInstance().getGroup(name, getConfigurationOwner(), getCoreUser(), this);
    }

    /**
     * Return the source of the configuration being used by this instance of core. As configurations optionally
     * have "parent" configuration that they iinherit from, this method returns a collection of sources with
     * one element for each configuration in the hierarchy.
     * @param owner The configuration's owner
     * @param user The user asking for the source
     * @param core The core being used - and who's source will be returend
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
     */
    public CommandArg invokeService(String commandName, CommandArg arguments) throws BaseException {
        AbstractCommand command=newCommand(commandName);
        arguments.setCallersCore(new CoreUserImpl(getCoreUser()));
        command.setArgs(arguments);
        command.invoke();
        return command.getArgs();
    }

	/**
     * Create a instance of the named command object.
	 * The named command is looked up in the current configuration, and
     * created from the specification held.
     * @param commandName The name of the command to create.
     * @return The command object ready for use.
     */
    public AbstractCommand newCommand(String commandName) {
        AbstractCommand command=FactoryHandler.getInstance().newCommand(commandName, getConfigurationOwner(), this);
        command.getArgs().setCallersCore(getCoreUser());
        return command;
    }

	/**
     * Create a instance of the named type object.
	 * The named type is looked up in the current configuration, and
     * created from the specification held.
     * @param typeName The name of the type to create.
     * @return The type object ready for use.
     */
    public Type newType(String typeName) {
		return FactoryHandler.getInstance().newType(typeName, getConfigurationOwner(), this);
    }

	/**
     * Create a instance of the named object.
	 * The named object is looked up in the current configuration, and
     * created from the specification held.
     * @param objectName The name of the object to create.
     * @return The object ready for use.
     */
    public Object newObject(String objectName) {
        throw new NotImplementedError("Core.newObject");
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
            LoggerCommand cmd=(LoggerCommand)newCommand("DebugLogger");
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.DEBUG);
            cmd.invoke();
		}
        catch(Throwable e) {
            System.err.println("debug logger failed ("+e+") message was: "+message);
            throw new CommandInvocationError(e.toString());
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
            LoggerCommand cmd=(LoggerCommand)newCommand("InfoLogger");
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.INFO);
            cmd.invoke();
		}
        catch(Throwable e) {
            System.err.println("info logger failed ("+e+") message was: "+message);
            throw new CommandInvocationError(e.toString(), e);
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
     * occured, but that it was dealt with and is not thought (by the developer)
     * to be if great importence.
     * @param message The text of the message to be output.
     */
    public void logWarning(String message, Throwable cause) {
		try {
            LoggerCommand cmd=(LoggerCommand)newCommand("WarningLogger");
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.WARNING);
            cmd.invoke();
		}
        catch(Throwable e) {
            System.err.println("warning logger failed ("+e+") message was: "+message);
            throw new CommandInvocationError(e.toString(), e);
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
            LoggerCommand cmd=(LoggerCommand)newCommand("ErrorLogger");
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.ERROR);
            cmd.invoke();
		}
        catch(Throwable e) {
            System.err.println("error logger failed ("+e+") message was: "+message);
            throw new CommandInvocationError(e.toString(), e);
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
            LoggerCommand cmd=(LoggerCommand)newCommand("FatalLogger");
            cmd.setMessage(message);
            cmd.setCause(cause);
            cmd.setDate(new Date());
            cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
            cmd.setSeverity(Severity.FATAL);
            cmd.invoke();
		}
        catch(Throwable e) {
            System.err.println("fatal logger failed ("+e+") message was: "+message);
            throw new CommandInvocationError(e.toString(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    public void logFatal(String message) {
        logFatal(message, null);
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T extends Type> T update(T object) {
        try {
            UpdateCommand command=(UpdateCommand)newCommand("Update");
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
            QueryCommand command=(QueryCommand)newCommand("Query");
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
            LoadCommand command=(LoadCommand)newCommand("Load");
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
            DeleteCommand command=(DeleteCommand)newCommand("Delete");
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
            QueryCommand command=(QueryCommand)newCommand("Query");
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
            CreateCommand command=(CreateCommand)newCommand("Create");
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
            newCommand("OpenPersistenceSession").invoke();
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
            newCommand("ClosePersistenceSession").invoke();
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
        FromXMLCommand cmd=(FromXMLCommand)newCommand("FromXML");
        XMLMapping mapping=ConfigurationHandler.getInstance().getXMLMapping(getConfigurationOwner(), getCoreUser(), this);

        cmd.setCallersCore(new CoreUserImpl(getCoreUser()));
        cmd.setClassIn(clazz);
        cmd.setXmlMappingInOut(mapping);
        cmd.setXmlIn(xml);

		try {
			cmd.invoke();
		}
		catch(XMLException e) {
            throw e;
        }
        catch(BaseException e) {
            throw new CommandInvocationError(e);
        }
		return (T)cmd.getObjectOut();
    }

	/**
     * Transform an object into its XML representation.
     * @param obj The object to be transformed.
     * @return An XMLString representing <code>obj</code>
     */
    public XMLString toXML(Object obj) {
        ToXMLCommand cmd=(ToXMLCommand)newCommand("ToXML");
        XMLMapping mapping=ConfigurationHandler.getInstance().getXMLMapping(getConfigurationOwner(), getCoreUser(), this);

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
        ValidatorCommand command = (ValidatorCommand) this.newCommand("Validator");
        
        // add arguments
        command.setKeyArg(key);
        command.setValueArg(value);
        command.setCallersCore(getCoreUser());
        command.setValidationSpecArg(getParameterValue("ValidationSpec"));
        
        // invoke
        command.invoke();
        
        // get result
        ValidatorResult result = command.getValidatorResultRet();
        
        return result;
	}

    public GetClassListArg getClassList(GetClassListArg arg) {
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
     * Fetch a collection of the products know to the system.
     * @return A collection of product names (as instances of java.lang.String).
     * @since 2.0
     */
    public Collection<ProductDetails> listProducts() {
        try {
            ListProductsCommand cmd=(ListProductsCommand)this.newCommand("ListProducts");
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
     * use during its lifecycle; this method is used to instantantiate specific types by name.<p>
     * For example, a complex insurance product may define several different types to describe
     * the assets the product covers. A commercial combined product might define a stock asset, a
     * vehicle asset, a safe asset, etc. Each of these is described within the product as a seperate
     * named type. A client would use this method to instantiate these different types as and when 
     * they needed to be added to an instance of a commercial combined policy.
     * @param productName The product "owning" the type.
     * @param typeName The name of type to be instantiated.
     * @return The instantiated type.
     * @since 2.0
     */
    public Type newProductType(String productName, String typeName) {
        try {
            NewProductTypeCommand cmd=(NewProductTypeCommand)this.newCommand("NewProductType");
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
     * type. This is the type (generally) instantiated at the beginning of the lifecycle. 
     * @param productName The name of the product to instantiate for.
     * @return The instantiated type.
     * @since 2.0
     */
    public Type newProductType(String productName) {
        try {
            NewProductTypeCommand cmd=(NewProductTypeCommand)this.newCommand("NewProductType");
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
            ResetAllProductsCommand cmd=(ResetAllProductsCommand)this.newCommand("ResetAllProducts");
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
            ResetProductCommand cmd=(ResetProductCommand)this.newCommand("ResetProduct");
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
            RegisterProductCommand cmd=(RegisterProductCommand)this.newCommand("RegisterProduct");
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
            RemoveProductCommand cmd=(RemoveProductCommand)this.newCommand("RemoveProduct");
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
            UpdateProductCommand cmd=(UpdateProductCommand)this.newCommand("UpdateProduct");
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
            GenerateDocumentCommand cmd=(GenerateDocumentCommand)this.newCommand("GenerateDocument");
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
     * Output a message to the Error boot logging channel. This should only be used when the 
     * normal logging services are not available - e.g. during startup when the normal service
     * have yet to be loaded.
     * An error is fatal if it stops the operation being processed. For example,
     * if the systems configuration information is defined in an inconsistent way
     * a fatal error is generated.
     * @param message The text of the message to be output.
     */
    public void logBootError(String message, Throwable cause) {
        BootLogger.log(coreUser.getClass(), coreUser.getVersionEffectiveDate(), Severity.ERROR, message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void logBootError(String message) {
        logBootError(message, null);
    }
    
    /**
     * Output a message to the Fatal boot logging channel. This should only be used when the 
     * normal logging services are not available - e.g. during startup when the normal service
     * have yet to be loaded.
     * An error is fatal if it stops the operation being processed. For example,
     * if the systems configuration information is defined in an inconsistent way
     * a fatal error is generated.
     * @param message The text of the message to be output.
     */
    public void logBootFatal(String message, Throwable cause) {
        BootLogger.log(coreUser.getClass(), coreUser.getVersionEffectiveDate(), Severity.FATAL, message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void logBootFatal(String message) {
        logBootFatal(message, null);
    }
    
    /**
     * Output a message to the Info boot logging channel. This should only be used when the 
     * normal logging services are not available - e.g. during startup when the normal service
     * have yet to be loaded.
     * An error is fatal if it stops the operation being processed. For example,
     * if the systems configuration information is defined in an inconsistent way
     * a fatal error is generated.
     * @param message The text of the message to be output.
     */
    public void logBootInfo(String message, Throwable cause) {
        BootLogger.log(coreUser.getClass(), coreUser.getVersionEffectiveDate(), Severity.INFO, message, cause);
    }

    /**
     * {@inheritDoc}
     */
    public void logBootInfo(String message) {
        logBootInfo(message, null);
    }

    /**
     * Output a message to the Warning boot logging channel. This should only be used when the 
     * normal logging services are not available - e.g. during startup when the normal service
     * have yet to be loaded.
     * An error is fatal if it stops the operation being processed. For example,
     * if the systems configuration information is defined in an inconsistent way
     * a fatal error is generated.
     * @param message The text of the message to be output.
     */
    public void logBootWarning(String message, Throwable cause) {
        BootLogger.log(coreUser.getClass(), coreUser.getVersionEffectiveDate(), Severity.WARNING, message, cause);
    }
    
    /**
     * {@inheritDoc}
     */
    public void logBootWarning(String message) {
        logBootWarning(message, null);
    }
}
