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

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;
import javax.sql.DataSource;

import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.EJBComponent;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

/**
 * EJB Wrapper for the persistence server.
 * @version $Revision: 1.7 $
 * @state $State: Exp $
 * @date $Date: 2006/09/20 20:15:43 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/persistence-server-ejb.jar/com/ail/core/persistence/PersistenceServerBean.java,v $
 */
public class PersistenceServerBean extends EJBComponent implements SessionBean {
    private VersionEffectiveDate versionEffectiveDate = null;
    private Core core = null;
    private SessionContext ctx = null;
	private DataSource ds = null;

    public PersistenceServerBean() {
        versionEffectiveDate = new VersionEffectiveDate();
        core = new Core(this);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }
    
    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void ejbCreate() throws CreateException {
        versionEffectiveDate = new VersionEffectiveDate();
        core = new com.ail.core.Core(this);
    }

    /**
     * Expose services of this EJB via XML. This method unmarshals the XML argument string into
     * an object, finds a method on the EJB to accept that object type as an argument
     * and invokes it. The result returned from the method is marshalled back into XM and returned.<p>
     * The methods are invoked on the context's local interface if possible (if one
     * exists). If no local interface is found then the remote interface is used instead.
     * Invoking methods via the local/remote interface means that the deployment settings
     * for security and transacts will be honoured.
     * @param xml XML argument to be passed to the service.
     * @return XML returned from the service.
     */
    public String invokeServiceXML(String xml) {
        return super.invokeServiceXML(xml, ctx);
    }

    public Core getCore() {
        return core;
    }

    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    public void setConfiguration(Configuration config) {
        try {
            super.setConfiguration(config);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public Configuration getConfiguration() {
        try {
            return super.getConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public String getConfigurationNamespace() {
        return super.getConfigurationNamespace();
    }

    public void resetConfiguration() {
        try {
            super.resetConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public Version getVersion() {
        try {
            Version v = (com.ail.core.Version) core.newType("Version");
            v.setCopyright("Copyright Applied Industrial Logic Limited 2006. All rights reserved.");
            v.setDate("$Date: 2006/09/20 20:15:43 $");
            v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/persistence-server-ejb.jar/com/ail/core/persistence/PersistenceServerBean.java,v $");
            v.setState("$State: Exp $");
            v.setVersion("$Revision: 1.7 $");
            return v;
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }


    /**
     * Service wrapper method for the CreateCommand service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServiceException In response to exceptions thrown by the service.
     */
    public CreateArg createCommand(CreateArg arg) throws BaseServerException {
        return invokeCommand(core, "Create", arg);
    }

	/**
	 * Service wrapper method for the UpdateCommand service.
	 * @param arg Argument to pass to the service
	 * @return Return value from the service
	 * @throws BaseServiceException In response to exceptions thrown by the service.
	 */
	public UpdateArg updateCommand(UpdateArg arg) throws BaseServerException {
		return invokeCommand(core, "Update", arg);
	}

	/**
	 * Service wrapper method for the LoadCommand service.
	 * @param arg Argument to pass to the service
	 * @return Return value from the service
	 * @throws BaseServiceException In response to exceptions thrown by the service.
	 */
	public LoadArg loadCommand(LoadArg arg) throws BaseServerException {
		return invokeCommand(core, "Load", arg);
	}

	/**
	 * Service wrapper method for the QueryCommand service.
	 * @param arg Argument to pass to the service
	 * @return Return value from the service
	 * @throws BaseServiceException In response to exceptions thrown by the service.
	 */
	public QueryArg queryCommand(QueryArg arg) throws BaseServerException {
		return invokeCommand(core, "Query", arg);
	}

    /**
     * Service wrapper method for the DeleteCommand service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServiceException In response to exceptions thrown by the service.
     */
    public DeleteArg deleteCommand(DeleteArg arg) throws BaseServerException {
        return invokeCommand(core, "Delete", arg);
    }
}


