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

package com.ail.core.persistence;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/08/20 15:03:54 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/persistence-server-ejb.jar/com/ail/core/persistence/PersistenceServer.java,v $
 */
public interface PersistenceServer extends EJBObject {
    /** @link dependency */

    /*# PersistenceServerBean lnkPersistenceServerBean; */

    String invokeServiceXML(String xml) throws RemoteException;

    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException;

    void setConfiguration(Configuration config) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    Version getVersion() throws RemoteException;

    CreateArg createCommand(CreateArg arg) throws RemoteException;

	UpdateArg updateCommand(UpdateArg arg) throws RemoteException;

	LoadArg loadCommand(LoadArg arg) throws RemoteException;

	QueryArg queryCommand(QueryArg arg) throws RemoteException;

	DeleteArg deleteCommand(DeleteArg arg) throws RemoteException;
}


