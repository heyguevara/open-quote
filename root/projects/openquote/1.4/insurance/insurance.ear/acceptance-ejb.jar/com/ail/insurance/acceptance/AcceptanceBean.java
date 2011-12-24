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

package com.ail.insurance.acceptance;

import javax.ejb.SessionContext;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.acceptance.acceptquotation.AcceptQuotationArgument;

@Configurable
public class AcceptanceBean extends com.ail.core.EJBComponent implements javax.ejb.SessionBean {
    private static final long serialVersionUID = -1235854950858098351L;

    private VersionEffectiveDate versionEffectiveDate = null;
    private com.ail.core.Core core = null;
    private javax.ejb.SessionContext ctx = null;

    public AcceptanceBean() {
        core = new Core(this);
        versionEffectiveDate=new VersionEffectiveDate();
    }

    public void setSessionContext(javax.ejb.SessionContext context) {
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

    public void ejbCreate() throws javax.ejb.CreateException {
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

    @Override
    public com.ail.core.Core getCore() {
        return core;
    }


    @Override
    public void setConfiguration(com.ail.core.configure.Configuration config) {
        try {
            super.setConfiguration(config);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    @Override
    public com.ail.core.configure.Configuration getConfiguration() {
        try {
            return super.getConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    @Override
    public String getConfigurationNamespace() {
        return super.getConfigurationNamespace();
    }

    @Override
	public VersionEffectiveDate getVersionEffectiveDate() {
		return versionEffectiveDate;
	}

	@Override
	public void resetConfiguration() {
        try {
            super.resetConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    /**
     * Service wrapper method for the ProduceDocumentation service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ProduceDocumentationArgument produceDocumentation(ProduceDocumentationArgument argument) {
        return invokeCommand(getCore(), "ProduceDocumentation", argument);
    }

    /**
     * Service wrapper method for the PutOnRisk service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public PutOnRiskArgument putOnRisk(PutOnRiskArgument argument) {
        return invokeCommand(getCore(), "PutOnRisk", argument);
    }

    /**
     * Service wrapper method for the CollectPremium service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CollectPremiumArgument collectPremium(CollectPremiumArgument argument) {
        return invokeCommand(getCore(), "CollectPremium", argument);
    }

    /**
     * Service wrapper method for the AcceptQuotation service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AcceptQuotationArgument acceptQuotation(AcceptQuotationArgument argument) {
        return invokeCommand(getCore(), "AcceptQuotation", argument);
    }
}


