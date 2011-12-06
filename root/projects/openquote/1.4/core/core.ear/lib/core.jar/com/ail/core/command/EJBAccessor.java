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

package com.ail.core.command;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.util.Hashtable;
import java.util.Properties;

import javax.ejb.EJBHome;
import javax.ejb.EJBMetaData;
import javax.ejb.EJBObject;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.rmi.PortableRemoteObject;

import com.ail.core.BaseException;
import com.ail.core.BaseServerException;
import com.ail.core.CoreUser;
import com.ail.core.CoreUserImpl;
import com.ail.core.NotImplementedError;
import com.ail.core.configure.Configuration;

/**
 * Provide access to EJB entry points. The entry point is accessed by its remote
 * interface.
 * @version $Revision: 1.6 $
 * @state $State: Exp $
 * @date $Date: 2007/06/10 09:13:42 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/command/EJBAccessor.java,v $
 */
public class EJBAccessor extends Accessor {
    private static Hashtable<EJBAccessorSettings,CacheElement> cache=new Hashtable<EJBAccessorSettings,CacheElement>();
    private EJBAccessorSettings settings=new EJBAccessorSettings();
    private Argument args=null;

    /**
     * Get a class representing the remote interface of the EJB.
     * There are two ways we can do this: Use home.getEJBMetaData, or (if that
     * fails) create an instance of the bean (home.create) and take the class
     * of the object returned by the create().<p>
     * The latter is defo a bad way to go about things, but some app servers
     * (SunONE for one) don't respond well to getEJBMetaData. 
     * @param home
     * @param createMethod
     * @return A class representing the remote bean class
     * @throws RemoteException
     */
    @SuppressWarnings("unchecked")
    private Class<EJBObject> remoteInterface(EJBHome home, Method createMethod) throws RemoteException {
      try {
        // Method 1 - the smart approach:
        //  Get the home interface and get the meta data from it.
        //  Get the EJBs remote interface.
        EJBMetaData meta=home.getEJBMetaData();
  
        return (Class<EJBObject>)meta.getRemoteInterfaceClass();
      }
      catch(RemoteException e) {
        // Method 2 - The dumb approach
        //  Create an instance of the EJB using home.create().
        //  Get the class of whatever is returned, and use that.
        try {
          Object obj=createMethod.invoke(home);
          return (Class<EJBObject>)obj.getClass();
        }
        catch(Exception ignore) {
          throw e;
        }
      }
    }
    
    /**
     * Get hold of the EJBHome associated with this accessor
	 * Each instance of this class (EJBAccessor) has an EJB associated with it.
     * This method finds the EJBHome associated with this instance.
     * @return The home interface of the EJB associated with this accessor.
     */
	private CacheElement getEJBDetails(String methodName, Object[] args) {
        Method createMethod=null;
        EJBHome home=null;
        Class<EJBObject> remoteInterfaceClass=null;

        // get initial context
        Properties p=new Properties();

        if (getFactory()!=null) {
            p.put(Context.INITIAL_CONTEXT_FACTORY, getFactory());
        }

        if (settings.getUrl()!=null) {
            p.put(Context.PROVIDER_URL, settings.getUrl());
        }

        // Get the home interface
        try {
            InitialContext context=new InitialContext(p);
            home=(EJBHome)context.lookup(getJndiName());
        }
        catch(NamingException e) {
            throw new AccessorError("EJBAccessor: failed to find EJB: "+e);
        }

        // get the create and invoke methods details.
        try {
            int idx=0;

            // create an instance of the bean - experimentation showed that we have to
            // use reflection here :-(
            createMethod=home.getClass().getMethod("create");

            remoteInterfaceClass=remoteInterface(home, createMethod);

            // loop through the methods on the remote interface looking for the method by
            // name if one was specified, or returning the type of our argument (by convention
            // EJB components accept one arg and return an object of the same type).
            Method methods[]=remoteInterfaceClass.getDeclaredMethods();
            for(idx=0 ; idx<methods.length ; idx++) {
                if (methodName==null) {
                    if (methods[idx].getReturnType().isInstance(args[0])) {
                        break;
                    }
                }
                else {
                    if (methodName.equals(methods[idx].getName())) {
                        break;
                    }
                }
            }

            // if we dropped out of the above loop early, then we found a match,
            // create a cache element and add it to the cache.
            if (idx!=methods.length) {
                return new CacheElement(createMethod, methods[idx], home, remoteInterfaceClass);
            }
            else {
                throw new AccessorError("EJBAccessor: "+getJndiName()+"."+getRemoteMethod()+"() NoSuchMethodException");
            }
        }
        catch(NoSuchMethodException e) {
            throw new AccessorError("EJBAccessor: "+getJndiName()+"."+getRemoteMethod()+"() NoSuchMethodException");
        }
        catch(RemoteException e) {
            throw new AccessorError("EJBAccessor: failed to find EJB: "+e);
        }
    }

    /**
     * Invoke a method on an EJB. This can be quite an expensive process, i.e.<ol>
     * <li>Get the EJBHome for the EJB specified in our 'settings'.</li>
     * <li>Create an instance of the EJB.</li>
     * <li>Get the EJBs meta data, and from there get its remote interface</li>
     * <li>Loop through the methods on the remote interface looking for one to match
     * our settings.</li>
     * <li>Use the reflection API to invoke the method.</li></ol>
     * Pretty expensive, so we'll try to avoid the first four steps by using a cache.
     * @param methodName This is normally null, except when we're being called to invoke
     * getVersion or get/setConfiguration.
     * @param args Args to be passed to the method
     * @return Whatever the method returned
     * @throws AccessorError
     */
	private Object invokeRemoteMethod(String methodName, Object[] args) throws AccessorError, BaseException {
        CacheElement element=null;

        // args is optional (may be null)
        if (args!=null) {
            settings.setArgType(args[0].getClass().getName());
        }

        // invoke the method on the EJB.
        try {
            synchronized(cache) {
                // check the cache for a CacheElement that matches our settings
                element=(CacheElement)cache.get(settings);

                // if there wasn't one there, we'll have to create it.
                if (element==null || methodName!=null) {
                    element=getEJBDetails(methodName, args);
                    if (methodName==null) {
                        cache.put(settings, element);
                    }
                }
            }

		    return element.getInvokeMethod().invoke(element.create(), args);
        }
        catch(IllegalAccessException e) {
            throw new AccessorError("EJBAccessor: "+getJndiName()+"."+getRemoteMethod()+"() IllegalAccessException");
        }
        catch(InvocationTargetException e) {
        	Throwable exception = e;
        	while(exception.getCause()!=null){
				exception = exception.getCause();
        		if(exception instanceof BaseException){
        			throw (BaseException)exception;
        		}
        	}
        	e.printStackTrace();
            throw new AccessorError("EJBAccessor: "+getJndiName()+"."+getRemoteMethod()+"() threw :"+e.getTargetException());
        }
        catch(BaseServerException e) {
            throw e.getCauseException();
        }
    }

    /**
     * Invoke the service (via the EJB)
     * @throws BaseException
     */
    public void invoke() throws BaseException {
        super.logEntry();

        CoreUser cu=getArgs().getCallersCore();
        getArgs().setCallersCore(new CoreUserImpl(cu));
        Object[] argValues={getArgs()};
		setArgs((Argument)invokeRemoteMethod(getRemoteMethod(), argValues));
        
        super.logExit();
    }

    /**
     * Set the arguments to be passed to the EJB service
     * @param args
     */
    public void setArgs(Argument args) {
		this.args=args;
    }

    /**
     * Get the argument being passed into (or returned by) the EJB
     * service.
     * @return Arguments
     */
    public Argument getArgs() {
		return args;
    }

	public Configuration getConfiguration() {
        try {
    		return (Configuration)invokeRemoteMethod("getConfiguration", null);
        }
        catch(BaseException e) {
            throw new AccessorError(e);
        }
    }

	public void setConfiguration(Configuration properties) {
        throw new NotImplementedError("EJBCommand.setConfiguration");
    }

    public void setFactory(String factory) {
		settings.setFactory(factory);
    }

    public String getFactory() {
		return settings.getFactory();
    }

    public void setServer(String server) {
        settings.setServer(server);
    }

    public String getServer() {
		return settings.getServer();
    }

    public void setProtocol(String protocol) {
        settings.setProtocol(protocol);
    }

    public String getProtocol() {
        return settings.getProtocol();
    }

    public void setPortNumber(String portNumber) {
        settings.setPortNumber(portNumber);
    }

    public String getPortNumber() {
        return settings.getPortNumber();
    }

    public void setJndiName(String jndiName) {
        settings.setJndiName(jndiName);
    }

    public String getJndiName() {
        return settings.getJndiName();
    }

    public void setRemoteMethod(String remoteMethod) {
        settings.setRemoteMethod(remoteMethod);
    }

    public String getRemoteMethod() {
        return settings.getRemoteMethod();
    }

    public void setUrl(String url) {
        settings.setUrl(url);
    }

    public String getUrl() {
        return settings.getUrl();
    }

    public EJBAccessorSettings getSettings() {
        return this.settings;
    }

    public void setSettings(EJBAccessorSettings settings) {
        this.settings=settings;
    }

    public Object clone() throws CloneNotSupportedException {
        return this;
    }
}

/**
 * Private class to encapsulate the accessor's settings. We use these settings
 * as a key in a hashtable based cache, that's why they are split out here into
 * a seperate class.
 */
class EJBAccessorSettings {
    private String factory=null;
    private String server=null;
    private String protocol=null;
    private String portNumber=null;
    private String jndiName=null;
    private String remoteMethod=null;
    private String url=null;
    private String argType=null;
    private int[] hashParts=new int[8];
    private int hashValue;

    /**
     * Set (update) the hash value that hashCode() will return. Each setter calls this
     * method, and it updates the hashValue. This value is simply the sum of the
     * characted values of each property (factory, server, etc), added together.
     * @param indx Unique value for each property
     * @param s The string the setter was called with
     */
    private void setHashValue(int indx, String s) {
        hashParts[indx]=0;
        for(int i=s.length() ; i>0 ; hashParts[indx]+=s.charAt(--i));

        hashValue=0;
        for(int i=0 ; i<hashParts.length ; hashValue+=hashParts[i++]);
    }

    /**
     * Get the factory class' name. For JBoss this would be
     * <code>org.jnp.interfaces.NamingContextFactory</code> for example.
     * @return The factory classes name.
     */
    public String getFactory() {
        return factory;
    }

    /**
     * Set the value of the factory property.
     * @see #getFactory
     * @param factory New factory setting
     */
    public void setFactory(String factory) {
        this.factory = factory;
        setHashValue(0, factory);
    }

    /**
     * Get the server property. This property holds the server name.
     * @return
     */
    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
        setHashValue(1, server);
    }

    public String getProtocol() {
        return protocol;
    }

    public void setProtocol(String protocol) {
        this.protocol = protocol;
        setHashValue(2, protocol);
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
        setHashValue(3, portNumber);
    }

    public String getJndiName() {
        return jndiName;
    }

    public void setJndiName(String jndiName) {
        this.jndiName = jndiName;
        setHashValue(4, jndiName);
    }

    public String getRemoteMethod() {
        return remoteMethod;
    }

    public void setRemoteMethod(String remoteMethod) {
        this.remoteMethod = remoteMethod;
        setHashValue(5, remoteMethod);
    }

    public String getArgType() {
        return argType;
    }

    public void setArgType(String argType) {
        this.argType=argType;
        setHashValue(6, argType);
    }

    public String getUrl() {
        if (url!=null) {
            return url;
        }
        else if (getProtocol()!=null && getServer()!=null && getPortNumber()!=null) {
            return getProtocol()+"://"+getServer()+":"+getPortNumber();
        }
        else {
            return null;
        }
    }

    public void setUrl(String url) {
        this.url = url;
        setHashValue(7, url);
    }

    public int hashCode() {
        return hashValue;
    }

    public boolean equals(Object that) {
        return that instanceof EJBAccessorSettings && that.hashCode() == hashValue;
    }
}

/**
 * This class encapsulates the information cached per EJBAccessor.
 * The intention is to hold onto as much information as possible to
 * speed up the creation of the required EJB, and the method the service
 * is wrapped in.<p>
 * Instances of this class are cached to speed up EJB service access.
 */
class CacheElement {
    private Method createMethod = null;
    private Method invokeMethod = null;
    private EJBHome home = null;
    private Class<EJBObject> remoteInterfaceClass = null;

    /**
     * Constructor
     * @param createMethod The method to use when creating an instance of the EJB.
     * @param invokeMethod The method to use when invoking the service on the EJB.
     * @param home The EJBs home interface.
     */
    public CacheElement(Method createMethod, Method invokeMethod, EJBHome home, Class<EJBObject> remoteInterfaceClass) {
        setCreateMethod(createMethod);
        setInvokeMethod(invokeMethod);
        setHome(home);
        setRemoteInterfaceClass(remoteInterfaceClass);
    }

    /**
     * Get the create method. The create method is used on the home interface to create
     * a new instance of the bean
     * @return create method.
     */
    public Method getCreateMethod() {
        return createMethod;
    }

    /**
     * Set the create method.
     * @param method The create method.
     */
    public void setCreateMethod(Method method) {
        this.createMethod = method;
    }

    /**
     * Getter to fetch the EJB's home interface.
     * @return The EJB's home interface.
     */
    public EJBHome getHome() {
        return home;
    }

    /**
     * Setter to set the EJB's home interface.
     * @param home Home interface.
     */
    public void setHome(EJBHome home) {
        this.home = home;
    }

    /**
     * Getter to fetch the method used to access the service method on the
     * EJB's remote interface.
     * @return Service's invoke method
     */
    public Method getInvokeMethod() {
        return invokeMethod;
    }

    /**
     * Setter to set the service's EJB remote method.
     * @param invokeMethod The remote method
     */
    public void setInvokeMethod(Method invokeMethod) {
        this.invokeMethod = invokeMethod;
    }

    /**
     * Getter returning the remote interface class supplied by the EJB.
     * @return Remote interface
     */
    public Class<EJBObject> getRemoteInterfaceClass() {
        return remoteInterfaceClass;
    }

    /**
     * Setter to set the remote interface class offered by the EJB
     * @param remoteInterfaceClass
     */
    public void setRemoteInterfaceClass(Class<EJBObject> remoteInterfaceClass) {
        this.remoteInterfaceClass = remoteInterfaceClass;
    }

    public EJBObject create() throws IllegalAccessException, InvocationTargetException {
        Object obj=createMethod.invoke(home);
        return (EJBObject)PortableRemoteObject.narrow(obj, remoteInterfaceClass);
    }

}
