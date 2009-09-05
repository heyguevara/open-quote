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
import com.ail.insurance.quotation.generatedocument.GenerateDocumentArg;
import com.ail.insurance.quotation.addpolicynumber.AddPolicyNumberArg;
import com.ail.insurance.quotation.addquotenumber.AddQuoteNumberArg;
import com.ail.insurance.quotation.assessrisk.AssessRiskArg;
import com.ail.insurance.quotation.calculatebrokerage.CalculateBrokerageArg;
import com.ail.insurance.quotation.calculatecommission.CalculateCommissionArg;
import com.ail.insurance.quotation.calculatemanagementcharge.CalculateManagementChargeArg;
import com.ail.insurance.quotation.calculatepremium.CalculatePremiumArg;
import com.ail.insurance.quotation.calculatetax.CalculateTaxArg;
import com.ail.insurance.quotation.enforcecompliance.EnforceComplianceArg;

public interface Quotation extends EJBObject {
    /** @link dependency */

    /*# QuotationBean lnkQuotationBean; */

    String invokeServiceXML(String xml) throws RemoteException;

    /**
     * Service wrapper business method for the AssessRisk service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    AssessRiskArg assessRisk(AssessRiskArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculatePremium service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculatePremiumArg calculatePremium(CalculatePremiumArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateTax service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateTaxArg calculateTax(CalculateTaxArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateCommission service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateCommissionArg calculateCommission(CalculateCommissionArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateBrokerage service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateBrokerageArg calculateBrokerage(CalculateBrokerageArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the CalculateManagementCharge service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    CalculateManagementChargeArg calculateManagementCharge(CalculateManagementChargeArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the AddQuoteNumber service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    AddQuoteNumberArg addQuoteNumber(AddQuoteNumberArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the AddPolicyNumber service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    AddPolicyNumberArg addPolicyNumber(AddPolicyNumberArg arg) throws RemoteException;
    
    /**
     * Service wrapper business method for the EnforceCompliance service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    EnforceComplianceArg enforceCompliance(EnforceComplianceArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the GenerateDocument service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateDocumentArg generateDocument(GenerateDocumentArg arg) throws RemoteException;
}



