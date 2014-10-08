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

import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.acceptance.acceptquotation.AcceptQuotationArg;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/03/03 09:31:07 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/acceptance-ejb.jar/com/ail/insurance/acceptance/AcceptanceBean.java,v $
 * @undefined
 * @displayName
 * @ejbHome <{AcceptanceHome}>
 * @ejbRemote <{Acceptance}>
 */
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

    private com.ail.core.command.CommandArg invokeCommand(String name, com.ail.core.command.CommandArg arg) {
        try {
            com.ail.core.command.AbstractCommand command = core.newCommand(name);
            command.setArgs(arg);
            command.invoke();
            return command.getArgs();
        }
        catch (com.ail.core.BaseException e) {
            throw new com.ail.core.BaseServerException(e);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
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

    public com.ail.core.Core getCore() {
        return core;
    }


    public void setConfiguration(com.ail.core.configure.Configuration config) {
        try {
            super.setConfiguration(config);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public com.ail.core.configure.Configuration getConfiguration() {
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

	public VersionEffectiveDate getVersionEffectiveDate() {
		return versionEffectiveDate;
	}

    public void resetConfiguration() {
        try {
            super.resetConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public com.ail.core.Version getVersion() {
        try {
            Version v = (com.ail.core.Version) core.newType("Version");
            v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
            v.setDate("$Date: 2007/03/03 09:31:07 $");
            v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/acceptance-ejb.jar/com/ail/insurance/acceptance/AcceptanceBean.java,v $");
            v.setState("$State: Exp $");
            v.setVersion("$Revision: 1.3 $");
            return v;
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
    public ProduceDocumentationArg produceDocumentation(ProduceDocumentationArg arg) {
        return (ProduceDocumentationArg)invokeCommand("ProduceDocumentationService", arg);
    }

    /**
     * Service wrapper method for the PutOnRisk service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public PutOnRiskArg putOnRisk(PutOnRiskArg arg) {
        return (PutOnRiskArg)invokeCommand("PutOnRiskService", arg);
    }

    /**
     * Service wrapper method for the CollectPremium service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CollectPremiumArg collectPremium(CollectPremiumArg arg) {
        return (CollectPremiumArg)invokeCommand("CollectPremiumService", arg);
    }

    /**
     * Service wrapper method for the AcceptQuotation service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AcceptQuotationArg acceptQuotation(AcceptQuotationArg arg) {
        return (AcceptQuotationArg)invokeCommand("AcceptQuotationService", arg);
    }
}


