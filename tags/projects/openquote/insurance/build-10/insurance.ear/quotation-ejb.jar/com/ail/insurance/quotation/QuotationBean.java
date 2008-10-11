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

package com.ail.insurance.quotation;

import javax.ejb.CreateException;
import javax.ejb.EJBException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.EJBComponent;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberArg;
import com.ail.insurance.quotation.addquotenumber.AddQuoteNumberArg;
import com.ail.insurance.quotation.assessrisk.AssessRiskArg;
import com.ail.insurance.quotation.calculatebrokerage.CalculateBrokerageArg;
import com.ail.insurance.quotation.calculatecommission.CalculateCommissionArg;
import com.ail.insurance.quotation.calculatemanagementcharge.CalculateManagementChargeArg;
import com.ail.insurance.quotation.calculatepremium.CalculatePremiumArg;
import com.ail.insurance.quotation.calculatetax.CalculateTaxArg;
import com.ail.insurance.quotation.enforcecompliance.EnforceComplianceArg;
import com.ail.insurance.quotation.generatedocument.GenerateDocumentArg;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/09/20 20:40:57 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/quotation-ejb.jar/com/ail/insurance/quotation/QuotationBean.java,v $
 * @undefined
 * @displayName
 * @ejbHome <{QuotationHome}>
 * @ejbRemote <{Quotation}>
 */
public class QuotationBean extends EJBComponent implements SessionBean {
    private static final long serialVersionUID = 6789993103676049055L;
    private VersionEffectiveDate versionEffectiveDate = null;
    private Core core = null;
    private SessionContext ctx = null;

    public QuotationBean() {
        core=new Core(this);
        versionEffectiveDate=new VersionEffectiveDate();
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
        core = new Core(this);
    }

    /**
     * Getter to return the core this component is using.
     * @return Core instance.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Return the component's version effective date. This date will determine
     * the effective date that the component will run as - and hence the version
     * of configuration information it uses.
     * @return version effective date
     * @throws EJBException
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    /**
     * Return this component's version information
     * @return version details
     * @throws EJBException
     */
    public Version getVersion() {
        try {
            Version v = (com.ail.core.Version) core.newType("Version");
            v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
            v.setDate("$Date: 2006/09/20 20:40:57 $");
            v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/quotation-ejb.jar/com/ail/insurance/quotation/QuotationBean.java,v $");
            v.setState("$State: Exp $");
            v.setVersion("$Revision: 1.3 $");
            return v;
        }
        catch (Exception e) {
            throw new javax.ejb.EJBException(e);
        }
    }

    /**
     * Expose services via XML. This method unmarshals the XML argument string into
     * an object, finds a method on the EJB to accept that object type as an argument
     * and invokes it. The result returned from the method is marshalled back into XM
     * and returned.<p>
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

    /**
     * Service wrapper business method for the AssessRisk service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public AssessRiskArg assessRisk(AssessRiskArg arg) {
        return invokeCommand(core, "AssessRisk", arg);
    }

    /**
     * Service wrapper business method for the CalculatePremium service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public CalculatePremiumArg calculatePremium(CalculatePremiumArg arg) {
        return invokeCommand(core, "CalculatePremium", arg);
    }

    /**
     * Service wrapper method for the CalculateTax service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateTaxArg calculateTax(CalculateTaxArg arg) {
        return invokeCommand(core, "CalculateTax", arg);
    }

    /**
     * Service wrapper method for the CalculateCommission service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateCommissionArg calculateCommission(CalculateCommissionArg arg) {
        return invokeCommand(core, "CalculateCommission", arg);
    }

    /**
     * Service wrapper method for the CalculateBrokerage service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateBrokerageArg calculateBrokerage(CalculateBrokerageArg arg) {
        return invokeCommand(core, "CalculateBrokerage", arg);
    }

    /**
     * Service wrapper method for the CalculateManagementCharge service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateManagementChargeArg calculateManagementCharge(CalculateManagementChargeArg arg) {
        return invokeCommand(core, "CalculateManagementCharge", arg);
    }

    /**
     * Service wrapper method for the AddQuoteNumber service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AddQuoteNumberArg addQuoteNumber(AddQuoteNumberArg arg) {
        return invokeCommand(core, "AddQuoteNumber", arg);
    }

    /**
     * Service wrapper method for the AddPolicyNumber service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AddPolicyNumberArg addPolicyNumber(AddPolicyNumberArg arg) {
        return invokeCommand(core, "AddPolicyNumber", arg);
    }

    /**
     * Service wrapper method for the EnforceCompliance service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public EnforceComplianceArg enforceCompliance(EnforceComplianceArg arg) {
        return invokeCommand(core, "EnforceCompliance", arg);
    }

    /**
     * Service wrapper method for the GenerateDocument service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public GenerateDocumentArg generateDocument(GenerateDocumentArg arg) {
        return invokeCommand(core, "GenerateDocument", arg);
    }

	/**
	 * Hard code the namespace to "com.ail.insurance.quotation.QuotationBean". Generally,
	 * the super class will automatically provide a namespace based on the class name, 
	 * but for EJBs this can be a problem. Some app server generated containers effect
	 * the name of the class causing the configuration to fail. Weblogic is one such.
	 * @return The namespace of the configuration.
	 */
	public String getConfigurationNamespace() {
		return "com.ail.insurance.quotation.QuotationBean";
	}
}


