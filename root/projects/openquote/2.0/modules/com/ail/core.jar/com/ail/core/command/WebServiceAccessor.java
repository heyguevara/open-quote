/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.Principal;

import javax.xml.namespace.QName;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;
import javax.xml.rpc.ServiceFactory;

import com.ail.core.BaseError;
import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.BootstrapConfigurationError;
import com.ail.core.configure.Configuration;

/**
 * Accessor providing access to web service based commands. The assumption here is that the 
 * web-service we're invoking is one of our own - i.e. it takes a single command object as
 * an argument and returns a command object of the same type. It also assumes that the web
 * service is based on our EJB wrapper pattern, and as such it has a "invokeServiceXML"
 * method.<p>
 * In effect we're using soap as a transport mechanism here rather than providing a general
 * accessor that might provide access to any web service.
 */
public class WebServiceAccessor extends Accessor implements CoreUser {
    private String url=null;
    private String namespace=null;
    private String localPart=null;
    private String operationName="invokeServiceXML";
    private String endpointClass=null;
    private Argument args=null;
    private Core core=null;

    public WebServiceAccessor() {
        core=new Core(this);
    }

    /**
     * Update this CommandImpl object's arguments with those taken from the command
     * arg passed in. This is in effect a bulk setter. This interface will be
     * implemented by beans that have many more getters/setters to support
     * their properties. The implementations of this method take 'that', and
     * pull all the relevant properties into this.
     * @param that Source for arguments
     */
    public void setArgs(Argument that) {
        this.args=that;
    }

    /**
     * Get the argument set associated with this command.
     * @return This objects arguments.
     */
    public Argument getArgs() {
        return args;
    }

    /**
     * Invoke the command associated with this command object.
     * Before invoke is called, the command object must be populated with
     * all the information (parameters) needed to invoke the service via
     * the command object's setters. Once invoke has been called, the
     * object's getters are used to retrieve the results.
     */
    public void invoke() throws BaseException {
        super.logEntry();

        try {
            URL url=new URL(getUrl());
            
            QName qname = new QName(getNamespace(), getLocalPart());

            ServiceFactory factory=ServiceFactory.newInstance();
            Service service=factory.createService(url, qname);
            
            Class<?> endpointClass=Class.forName(getEndpointClass());
            Remote endpoint=service.getPort(endpointClass);

            Method method=endpoint.getClass().getDeclaredMethod(getOperationName(), new Class[]{String.class});
                        
            // clear out the stuff we can't send
            args.setCallersCore(null);

            String ret=(String)method.invoke(endpoint, new Object[]{core.toXML(args).toString()});

            args=core.fromXML(args.getClass(), new XMLString(ret));
        }
        catch(InvocationTargetException e) {
            try {
                throw e.getTargetException();
            }
            catch(RemoteException re) {
                for(Throwable t=re ; t!=null ; t=t.getCause()) {
                    if (t instanceof BaseError) {
                        throw (BaseError)t;
                    }
                }
                throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
            }
            catch(Throwable t) {
                // TODO better error handling needed here
                t.printStackTrace();
            }
        }
        catch(ServiceException e) {
            throw new BootstrapConfigurationError("Failed to create web service "+e);
        }
        catch(MalformedURLException e) {
            throw new BootstrapConfigurationError("Webservice URL is badly formed: "+getUrl());
        }
        catch(NoSuchMethodException e) {
            throw new BootstrapConfigurationError("Webservice operation name (method) not found: "+getOperationName());
        }
        catch(ClassNotFoundException e) {
            throw new BootstrapConfigurationError("Webservice endpoint class not found: "+getEndpointClass());
        }
        catch(IllegalAccessException e) {
            throw new BootstrapConfigurationError("Webservice operation could not be invoked (IllegalAccessException): "+getEndpointClass()+"."+getOperationName());
        }
        
        super.logExit();
    }

    public Configuration getConfiguration() {
        throw new CommandInvocationError("Get configuration cannot be invoked on a Webservice service");
    }

    public void setConfiguration(Configuration properties) {
        throw new CommandInvocationError("Set configuration cannot be invoked on a Webservice service");
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getLocalPart() {
        return localPart;
    }

    public void setLocalPart(String localPart) {
        this.localPart = localPart;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public String getEndpointClass() {
        return endpointClass;
    }

    public void setEndpointClass(String endpointClass) {
        this.endpointClass = endpointClass;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }

    /**
     * The Core uses this callback to determin which versions of artefacts it
     * should use on the CoreUser's behalf.
     * @return The version date that the CoreUser is working at.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return args.getCallersCore().getVersionEffectiveDate();
    }

    /**
     * Return the caller's configuration namespace.
     * @return Caller's namespace
     */
    public String getConfigurationNamespace() {
        return args.getCallersCore().getConfigurationNamespace();
    }
    
    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        return args.getCallersCore().getSecurityPrincipal();
    }

    public Object clone() throws CloneNotSupportedException {
        WebServiceAccessor clone=(WebServiceAccessor)super.clone();
        clone.args=args;
        clone.core=core;
        return clone;
    }
}
