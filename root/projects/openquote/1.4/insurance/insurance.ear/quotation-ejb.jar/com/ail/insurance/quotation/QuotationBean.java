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

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.EJBComponent;
import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberArgument;
import com.ail.insurance.quotation.addquotenumber.AddQuoteNumberArgument;
import com.ail.insurance.quotation.assessrisk.AssessRiskArgument;
import com.ail.insurance.quotation.calculatebrokerage.CalculateBrokerageArgument;
import com.ail.insurance.quotation.calculatecommission.CalculateCommissionArgument;
import com.ail.insurance.quotation.calculatemanagementcharge.CalculateManagementChargeArgument;
import com.ail.insurance.quotation.calculatepremium.CalculatePremiumArgument;
import com.ail.insurance.quotation.calculatetax.CalculateTaxArgument;
import com.ail.insurance.quotation.enforcecompliance.EnforceComplianceArgument;
import com.ail.insurance.quotation.generatedocument.GenerateDocumentArgument;

@Configurable
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
    public AssessRiskArgument assessRisk(AssessRiskArgument arg) {
        return invokeCommand(core, "AssessRisk", arg);
    }

    /**
     * Service wrapper business method for the CalculatePremium service.
     * @param arg The Argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public CalculatePremiumArgument calculatePremium(CalculatePremiumArgument arg) {
        return invokeCommand(core, "CalculatePremium", arg);
    }

    /**
     * Service wrapper method for the CalculateTax service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateTaxArgument calculateTax(CalculateTaxArgument arg) {
        return invokeCommand(core, "CalculateTax", arg);
    }

    /**
     * Service wrapper method for the CalculateCommission service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateCommissionArgument calculateCommission(CalculateCommissionArgument arg) {
        return invokeCommand(core, "CalculateCommission", arg);
    }

    /**
     * Service wrapper method for the CalculateBrokerage service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateBrokerageArgument calculateBrokerage(CalculateBrokerageArgument arg) {
        return invokeCommand(core, "CalculateBrokerage", arg);
    }

    /**
     * Service wrapper method for the CalculateManagementCharge service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateManagementChargeArgument calculateManagementCharge(CalculateManagementChargeArgument arg) {
        return invokeCommand(core, "CalculateManagement", arg);
    }

    /**
     * Service wrapper method for the AddQuoteNumber service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AddQuoteNumberArgument addQuoteNumber(AddQuoteNumberArgument arg) {
        return invokeCommand(core, "AddQuoteNumber", arg);
    }

    /**
     * Service wrapper method for the AddPolicyNumber service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AddPolicyNumberArgument addPolicyNumber(AddPolicyNumberArgument arg) {
        return invokeCommand(core, "AddPolicyNumber", arg);
    }

    /**
     * Service wrapper method for the EnforceCompliance service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public EnforceComplianceArgument enforceCompliance(EnforceComplianceArgument arg) {
        return invokeCommand(core, "EnforceCompliance", arg);
    }

    /**
     * Service wrapper method for the GenerateDocument service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public GenerateDocumentArgument generateDocument(GenerateDocumentArgument arg) {
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


