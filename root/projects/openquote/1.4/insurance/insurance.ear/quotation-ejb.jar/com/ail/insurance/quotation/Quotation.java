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

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.ail.core.BaseServerException;
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

public interface Quotation extends EJBObject {
    String invokeServiceXML(String xml) throws RemoteException;

    /**
     * Service wrapper business method for the AssessRisk service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    AssessRiskArgument assessRisk(AssessRiskArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculatePremium service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculatePremiumArgument calculatePremium(CalculatePremiumArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateTax service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateTaxArgument calculateTax(CalculateTaxArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateCommission service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateCommissionArgument calculateCommission(CalculateCommissionArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateBrokerage service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateBrokerageArgument calculateBrokerage(CalculateBrokerageArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateManagementCharge service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateManagementChargeArgument calculateManagementCharge(CalculateManagementChargeArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the AddQuoteNumber service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    AddQuoteNumberArgument addQuoteNumber(AddQuoteNumberArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the AddPolicyNumber service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    AddPolicyNumberArgument addPolicyNumber(AddPolicyNumberArgument arg) throws RemoteException;
    
    /**
     * Service wrapper business method for the EnforceCompliance service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    EnforceComplianceArgument enforceCompliance(EnforceComplianceArgument arg) throws RemoteException;

    /**
     * Service wrapper business method for the GenerateDocument service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateDocumentArgument generateDocument(GenerateDocumentArgument arg) throws RemoteException;
}



