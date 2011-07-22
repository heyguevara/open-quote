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

import javax.ejb.Remote;

import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

@Remote
public interface PersistenceServer {

    String invokeServiceXML(String xml);

    VersionEffectiveDate getVersionEffectiveDate();

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    Version getVersion();

    CreateArg createCommand(CreateArg arg);

	UpdateArg updateCommand(UpdateArg arg);

	LoadArg loadCommand(LoadArg arg);

	QueryArg queryCommand(QueryArg arg);

	DeleteArg deleteCommand(DeleteArg arg);
}


