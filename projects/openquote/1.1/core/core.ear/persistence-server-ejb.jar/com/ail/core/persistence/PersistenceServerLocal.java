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

package com.ail.core.persistence;

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;

import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

public interface PersistenceServerLocal extends EJBLocalObject {
    /** @link dependency */
    /*# ServerBean lnkServerBean; */

    String invokeServiceXML(String xml) throws EJBException;

    VersionEffectiveDate getVersionEffectiveDate() throws EJBException;

    void setConfiguration(Configuration config) throws EJBException;

    Configuration getConfiguration() throws EJBException;

    String getConfigurationNamespace() throws EJBException;

    void resetConfiguration() throws EJBException;

    Version getVersion() throws EJBException;

    CreateArg createCommand(CreateArg arg) throws EJBException;

    UpdateArg updateCommand(UpdateArg arg) throws EJBException;

    LoadArg loadCommand(LoadArg arg) throws EJBException;

    QueryArg queryCommand(QueryArg arg) throws EJBException;

    DeleteArg deleteCommand(DeleteArg arg) throws EJBException;
}
