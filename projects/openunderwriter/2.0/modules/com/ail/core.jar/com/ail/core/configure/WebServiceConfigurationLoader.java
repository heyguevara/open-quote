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
package com.ail.core.configure;

import com.ail.core.NotImplementedError;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.BaseError;

import java.util.Collection;
import java.net.URL;
import java.net.MalformedURLException;
import java.rmi.RemoteException;
import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import org.apache.axis.client.Service;
import org.apache.axis.client.Call;
import org.apache.axis.Constants;

import javax.xml.rpc.ParameterMode;
import javax.xml.rpc.ServiceException;
import javax.xml.namespace.QName;

/**
 * This configuration loader acts as a web services client.
 */
public class WebServiceConfigurationLoader extends AbstractConfigurationLoader {
    /**
     * Fetch the configuration associated with a given namespace, and return it.
     * @param namespace The namespace to load for.
     * @param date The effective date to load the configuration for.
     * @return loaded configuration.
     */
    public Configuration loadConfiguration(String namespace, VersionEffectiveDate date) {
        String url=null;

        try {
            url=getLoaderParams().getProperty("url");
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new URL(url));
            call.setOperationName("loadConfigurationAsByteArray");

            QName vedQn = new QName( "urn:EJBLoaderRemote", "VersionEffectiveDate" );
            call.registerTypeMapping(VersionEffectiveDate.class, vedQn,
                          new org.apache.axis.encoding.ser.BeanSerializerFactory(VersionEffectiveDate.class, vedQn),
                          new org.apache.axis.encoding.ser.BeanDeserializerFactory(VersionEffectiveDate.class, vedQn));

            // Call to addParameter/setReturnType as described in user-guide.html
            call.addParameter("namespace", Constants.XSD_STRING, ParameterMode.IN);
            call.addParameter("date", vedQn, ParameterMode.IN);
            call.setReturnType(Constants.XSD_BASE64);

            byte[] config=(byte[])call.invoke(new Object[]{namespace, date});

            InputStream is = new ByteArrayInputStream(config);
            ObjectInputStream objis = new ObjectInputStream(is);

            return (Configuration)objis.readObject();
        }
        catch(ServiceException e) {
            throw new BootstrapConfigurationError("Failed to create web service "+e);
        }
        catch(MalformedURLException e) {
            throw new BootstrapConfigurationError("Webservice URL is badly formed: "+url);
        }
        catch(RemoteException e) {
            for(Throwable t=e ; t!=null ; t=t.getCause()) {
                if (t instanceof BaseError) {
                    throw (BaseError)t;
                }
            }
            // todo "suspected" isn't good enough!
            throw new UnknownNamespaceError("Suspected namespace error (check the server log)");
        }
        catch(Exception e) {
            throw new BootstrapConfigurationError("Failed to deserialize configuration:"+e);
        }
    }

    /**
     * Save (and update) the configuration associated with the specificed namespace.
     * @param namespace The namespace to save to.
     * @param config The configuration to save.
     * @throws ConfigurationUpdateCollisionError If the configuration being written is stale (i.e. has be updated in persistent store since it was read).
     */
    public void saveConfiguration(String namespace, Configuration config) throws ConfigurationUpdateCollisionError {
        String url=null;

        try {
            url=getLoaderParams().getProperty("url");
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new URL(url));
            call.setOperationName("saveConfiguration");

            // Call to addParameter/setReturnType as described in user-guide.html
            call.addParameter("namespace", Constants.XSD_STRING, ParameterMode.IN);
            call.addParameter("config", Constants.XSD_BASE64, ParameterMode.IN);
            call.setReturnType(Constants.XSD_INT);

            // Convert 'config' into a byte array
            ByteArrayOutputStream baos=new ByteArrayOutputStream();
            ObjectOutputStream oos=new ObjectOutputStream(baos);
            oos.writeObject(config);
            oos.flush();

            call.invoke(new Object[]{namespace, baos.toByteArray()});
        }
        catch(ServiceException e) {
            throw new BootstrapConfigurationError("Failed to create web service "+e);
        }
        catch(MalformedURLException e) {
            throw new BootstrapConfigurationError("Webservice URL is badly formed: "+url);
        }
        catch(RemoteException e) {
            for(Throwable t=e ; t!=null ; t=t.getCause()) {
                if (t instanceof BaseError) {
                    throw (BaseError)t;
                }
            }
            throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
        }
        catch(Exception e) {
            // todo better error handling needed here
            e.printStackTrace();
        }
    }

    /**
     * Build and return a list of the namespaces currently being used. More specifically
     * this includes only those that are stored and are current.
     * @return A collection of namespace names as Strings.
     */
    public Collection<String> getNamespaces() {
        throw new NotImplementedError("WebServiceConfigurationLoader.getNamespaces");
    }

    /**
     * When the configuration handler is asked to "reset", it passes that request onto
     * the loader currently in user. The loader should reset any internal state in this
     * method.
     */
    public void reset() {
        String url=null;

        try {
            url=getLoaderParams().getProperty("url");
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new URL(url));
            call.setOperationName("reset");

            // Call to addParameter/setReturnType as described in user-guide.html
            call.setReturnType(Constants.XSD_INT);

            call.invoke(new Object[]{});
        }
        catch(ServiceException e) {
            throw new BootstrapConfigurationError("Failed to create web service "+e);
        }
        catch(MalformedURLException e) {
            throw new BootstrapConfigurationError("Webservice URL is badly formed: "+url);
        }
        catch(RemoteException e) {
            for(Throwable t=e ; t!=null ; t=t.getCause()) {
                if (t instanceof BaseError) {
                    throw (BaseError)t;
                }
            }
            throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
        }
        catch(Exception e) {
            // todo better error handling needed here
            e.printStackTrace();
        }
    }

    /**
     * Delete ALL configuration information. This will include all historical configuration
     * information. <p>
     * <b>NOTE: ALL CONFIGURATION INFORMATION WILL BE LOST!</b>
     */
    public void purgeAllConfigurations() {
        String url=null;

        try {
            url=getLoaderParams().getProperty("url");
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new URL(url));
            call.setOperationName("purgeAllConfigurations");

            // Call to addParameter/setReturnType as described in user-guide.html
            call.setReturnType(Constants.XSD_INT);

            call.invoke(new Object[]{});
        }
        catch(ServiceException e) {
            throw new BootstrapConfigurationError("Failed to create web service "+e);
        }
        catch(MalformedURLException e) {
            throw new BootstrapConfigurationError("Webservice URL is badly formed: "+url);
        }
        catch(RemoteException e) {
            for(Throwable t=e ; t!=null ; t=t.getCause()) {
                if (t instanceof BaseError) {
                    throw (BaseError)t;
                }
            }
            throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
        }
        catch(Exception e) {
            // todo better error handling needed here
            e.printStackTrace();
        }
    }

    /**
     * Delete the repository holding configuration information. This not only removes all
     * configuration information {@link #purgeAllConfigurations} but also removes the
     * repository itself.<p>
     * <b>NOTE: ALL CONFIGURATION INFORMATION WILL BE LOST!</b>
     */
    public void deleteConfigurationRepository() {
        String url=null;

        try {
            url=getLoaderParams().getProperty("url");
            Service service = new Service();
            Call call = (Call) service.createCall();

            call.setTargetEndpointAddress(new URL(url));
            call.setOperationName("deleteConfigurationRepository");

            // Call to addParameter/setReturnType as described in user-guide.html
            call.setReturnType(Constants.XSD_INT);

            call.invoke(new Object[]{});
        }
        catch(ServiceException e) {
            throw new BootstrapConfigurationError("Failed to create web service "+e);
        }
        catch(MalformedURLException e) {
            throw new BootstrapConfigurationError("Webservice URL is badly formed: "+url);
        }
        catch(RemoteException e) {
            for(Throwable t=e ; t!=null ; t=t.getCause()) {
                if (t instanceof BaseError) {
                    throw (BaseError)t;
                }
            }
            throw new BootstrapConfigurationError("Failed to contact configuration server:"+e);
        }
        catch(Exception e) {
            // todo better error handling needed here
            e.printStackTrace();
        }
    }

    /**
     * Fetch the summary details for all the configurations that are current. In this context "current"
     * means the latest version (i.e. with a validTo of zero). The collection returned is made up of
     * instances of {@link ConfigurationSummary ConfigurationSummary}.
     * @return A collection of {@link ConfigurationSummary ConfigurationSummary}
     */
    public Collection<ConfigurationSummary> getNamespacesSummary() {
        throw new NotImplementedError("FileConfigurationLoader.getNamespacesDetail");
    }

    /**
     * Fetch the details of a specific configurations history. 
     * @param namespace Namespace to get history for.
     * @return A collection of {@link ConfigurationSummary ConfigurationSummary}
     */
    public Collection<ConfigurationSummary> getNamespacesHistorySummary(String namespace) {
        throw new NotImplementedError("FileConfigurationLoader.getNamespacesHistorySummary");
    }

    @Override
    public boolean isConfigurationRepositoryCreated() {
        throw new NotImplementedError("FileConfigurationLoader.isConfigurationRepositoryCreated");
    }
}
