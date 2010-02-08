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

import java.rmi.RemoteException;

import javax.ejb.EJBException;
import javax.ejb.EJBObject;

import org.w3c.dom.Element;

import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

public interface Server extends EJBObject {
    /** @link dependency */
    /*# ServerBean lnkServerBean; */

    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException, EJBException;

    void setConfiguration(Configuration config) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    void resetCoreConfiguration() throws RemoteException, EJBException;

    void resetNamedConfiguration(String name) throws RemoteException, EJBException;

    void clearConfigurationCache() throws RemoteException, EJBException;

    void clearNamedConfigurationCache(String namespace) throws RemoteException, EJBException;

	Version getVersion() throws RemoteException, EJBException;

    String invokeServiceXML(String xml) throws RemoteException, EJBException;

    Element[] invokeServiceSoap(Element[] xml) throws RemoteException, EJBException;

    GetNamespacesArg getNamespaces(GetNamespacesArg arg) throws RemoteException, EJBException;

    GetConfigurationArg getConfiguration(GetConfigurationArg arg) throws RemoteException, EJBException;

    SetConfigurationArg setConfiguration(SetConfigurationArg arg) throws RemoteException, EJBException;

    GetCommandScriptArg getCommandScript(GetCommandScriptArg arg) throws RemoteException, EJBException;

    SetCommandScriptArg setCommandScript(SetCommandScriptArg arg) throws RemoteException, EJBException;

    DeployCarArg deployCar(DeployCarArg arg) throws RemoteException, EJBException;
    
    PackageCarArg packageCar(PackageCarArg arg) throws RemoteException, EJBException;

    CatalogCarArg catalogCar(CatalogCarArg arg) throws RemoteException, EJBException;
}
