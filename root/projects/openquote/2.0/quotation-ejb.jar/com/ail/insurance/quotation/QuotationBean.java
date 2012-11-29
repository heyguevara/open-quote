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
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.EJBComponent;
import com.ail.insurance.quotation.AddPolicyNumberService.AddPolicyNumberArgument;
import com.ail.insurance.quotation.AddQuoteNumberService.AddQuoteNumberArgument;
import com.ail.insurance.quotation.AssessRiskService.AssessRiskArgument;
import com.ail.insurance.quotation.CalculateBrokerageService.CalculateBrokerageArgument;
import com.ail.insurance.quotation.CalculateCommissionService.CalculateCommissionArgument;
import com.ail.insurance.quotation.CalculateManagementChargeService.CalculateManagementChargeArgument;
import com.ail.insurance.quotation.CalculatePremiumService.CalculatePremiumArgument;
import com.ail.insurance.quotation.CalculateTaxService.CalculateTaxArgument;
import com.ail.insurance.quotation.EnforceComplianceService.EnforceComplianceArgument;
import com.ail.insurance.quotation.GenerateQuoteService.GenerateDocumentArgument;

@Configurable
@Stateless
public class QuotationBean extends EJBComponent implements Quotation {
    private static final String namespace="com.ail.insurance.quotation.QuotationBean";
    private SessionContext ctx = null;
    
    public QuotationBean() {
        initialise(namespace);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbCreate() throws CreateException {
        initialise(namespace);
    }

    /**
     * Service wrapper business method for the AssessRisk service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public AssessRiskArgument assessRisk(AssessRiskArgument arg) {
        return invokeCommand("AssessRisk", arg);
    }

    /**
     * Service wrapper business method for the CalculatePremium service.
     * @param arg The Argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public CalculatePremiumArgument calculatePremium(CalculatePremiumArgument arg) {
        return invokeCommand("CalculatePremium", arg);
    }

    /**
     * Service wrapper method for the CalculateTax service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateTaxArgument calculateTax(CalculateTaxArgument arg) {
        return invokeCommand("CalculateTax", arg);
    }

    /**
     * Service wrapper method for the CalculateCommission service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateCommissionArgument calculateCommission(CalculateCommissionArgument arg) {
        return invokeCommand("CalculateCommission", arg);
    }

    /**
     * Service wrapper method for the CalculateBrokerage service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateBrokerageArgument calculateBrokerage(CalculateBrokerageArgument arg) {
        return invokeCommand("CalculateBrokerage", arg);
    }

    /**
     * Service wrapper method for the CalculateManagementCharge service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CalculateManagementChargeArgument calculateManagementCharge(CalculateManagementChargeArgument arg) {
        return invokeCommand("CalculateManagementCharge", arg);
    }

    /**
     * Service wrapper method for the AddQuoteNumber service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AddQuoteNumberArgument addQuoteNumber(AddQuoteNumberArgument arg) {
        return invokeCommand("AddQuoteNumber", arg);
    }

    /**
     * Service wrapper method for the AddPolicyNumber service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AddPolicyNumberArgument addPolicyNumber(AddPolicyNumberArgument arg) {
        return invokeCommand("AddPolicyNumber", arg);
    }

    /**
     * Service wrapper method for the EnforceCompliance service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public EnforceComplianceArgument enforceCompliance(EnforceComplianceArgument arg) {
        return invokeCommand("EnforceCompliance", arg);
    }

    /**
     * Service wrapper method for the GenerateDocument service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public GenerateDocumentArgument generateDocument(GenerateDocumentArgument arg) {
        return invokeCommand("GenerateDocument", arg);
    }
}


