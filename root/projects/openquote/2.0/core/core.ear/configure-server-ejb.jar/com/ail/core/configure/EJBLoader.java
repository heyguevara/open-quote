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

package com.ail.core.configure;

import com.ail.core.VersionEffectiveDate;

import javax.ejb.EJBObject;
import java.rmi.RemoteException;
import java.util.Collection;
import javax.ejb.EJBException;

/**
 * Remote interface for the EJBLoader bean.
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2005/07/31 18:04:03 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/configure-server-ejb.jar/com/ail/core/configure/EJBLoader.java,v $
 */
public interface EJBLoader extends EJBObject {
    /** @link dependency */
    /*# ServerBean lnkEJBLoaderBean; */

    public Configuration loadConfiguration(String namespace, VersionEffectiveDate date) throws RemoteException, EJBConfigurationException;

    public void saveConfiguration(String namespace, Configuration config) throws RemoteException, EJBConfigurationException, EJBException;

    public Collection<String> getNamespaces() throws RemoteException, EJBConfigurationException, EJBException;

    public Collection<ConfigurationSummary> getNamespacesHistoryDetail(String namespace) throws RemoteException, EJBConfigurationException, EJBException;

    public Collection<ConfigurationSummary> getNamespacesDetail() throws RemoteException, EJBConfigurationException, EJBException;

    public byte[] loadConfigurationAsByteArray(String namespace, VersionEffectiveDate date) throws EJBException, RemoteException;

    public int saveConfiguration(String namespace, byte[] config) throws EJBException, RemoteException;

    public int reset() throws RemoteException;

    public int purgeAllConfigurations() throws RemoteException;

    public int deleteConfigurationRepository() throws RemoteException;
}
