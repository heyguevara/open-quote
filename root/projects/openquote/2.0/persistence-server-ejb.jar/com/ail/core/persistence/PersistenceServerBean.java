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
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.StatelessComponent;
import com.ail.core.persistence.CreateService.CreateArgument;
import com.ail.core.persistence.DeleteService.DeleteArgument;
import com.ail.core.persistence.LoadService.LoadArgument;
import com.ail.core.persistence.QueryService.QueryArgument;
import com.ail.core.persistence.UpdateService.UpdateArgument;
/**
 * EJB Wrapper for the persistence server.
 */
@Configurable
@Stateless
@Remote(PersistenceServer.class)
public class PersistenceServerBean extends StatelessComponent implements PersistenceServerLocal {

    public PersistenceServerBean() {
        initialise("com.ail.core.persistence.PersistenceServerBean");
    }

    /**
     * Service wrapper method for the CreateCommand service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServiceException In response to exceptions thrown by the service.
     */
    public CreateArgument createCommand(CreateArgument arg) throws BaseServerException {
        return invokeCommand("Create", arg);
    }

	/**
	 * Service wrapper method for the UpdateCommand service.
	 * @param arg LoggingArgument to pass to the service
	 * @return Return value from the service
	 * @throws BaseServiceException In response to exceptions thrown by the service.
	 */
	public UpdateArgument updateCommand(UpdateArgument arg) throws BaseServerException {
		return invokeCommand("Update", arg);
	}

	/**
	 * Service wrapper method for the LoadCommand service.
	 * @param arg LoggingArgument to pass to the service
	 * @return Return value from the service
	 * @throws BaseServiceException In response to exceptions thrown by the service.
	 */
	public LoadArgument loadCommand(LoadArgument arg) throws BaseServerException {
		return invokeCommand("Load", arg);
	}

	/**
	 * Service wrapper method for the QueryCommand service.
	 * @param arg LoggingArgument to pass to the service
	 * @return Return value from the service
	 * @throws BaseServiceException In response to exceptions thrown by the service.
	 */
	public QueryArgument queryCommand(QueryArgument arg) throws BaseServerException {
		return invokeCommand("Query", arg);
	}

    /**
     * Service wrapper method for the DeleteCommand service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServiceException In response to exceptions thrown by the service.
     */
    public DeleteArgument deleteCommand(DeleteArgument arg) throws BaseServerException {
        return invokeCommand("Delete", arg);
    }
}


