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

package com.ail.insurance.subrogation;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.insurance.subrogation.makearecovery.MakeARecoveryArgument;

public interface Subrogation extends EJBObject {
    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException;

    void setConfiguration(Configuration config) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    MakeARecoveryArgument makeARecovery(MakeARecoveryArgument command) throws RemoteException;
}
