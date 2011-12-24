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

import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

public interface PersistenceServer extends EJBObject {
    /** @link dependency */

    /*# PersistenceServerBean lnkPersistenceServerBean; */

    String invokeServiceXML(String xml) throws RemoteException;

    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException;

    void setConfiguration(Configuration config) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    CreateArgument createCommand(CreateArgument arg) throws RemoteException;

	UpdateArgument updateCommand(UpdateArgument arg) throws RemoteException;

	LoadArgument loadCommand(LoadArgument arg) throws RemoteException;

	QueryArgument queryCommand(QueryArgument arg) throws RemoteException;

	DeleteArgument deleteCommand(DeleteArgument arg) throws RemoteException;
}


