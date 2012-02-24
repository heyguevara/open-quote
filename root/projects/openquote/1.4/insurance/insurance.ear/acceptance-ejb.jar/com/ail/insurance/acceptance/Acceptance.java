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

import java.rmi.RemoteException;

import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.acceptance.AcceptQuotationService.AcceptQuotationArgument;
import com.ail.insurance.acceptance.CollectPremiumService.CollectPremiumArgument;
import com.ail.insurance.acceptance.ProduceDocumentationService.ProduceDocumentationArgument;
import com.ail.insurance.acceptance.PutOnRiskService.PutOnRiskArgument;

public interface Acceptance extends javax.ejb.EJBObject {
    String invokeServiceXML(String xml) throws RemoteException;

    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException;

    void setConfiguration(com.ail.core.configure.Configuration config) throws RemoteException;

    com.ail.core.configure.Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    ProduceDocumentationArgument produceDocumentation(ProduceDocumentationArgument argument) throws RemoteException;

    PutOnRiskArgument putOnRisk(PutOnRiskArgument argument) throws RemoteException;

    CollectPremiumArgument collectPremium(CollectPremiumArgument argument) throws RemoteException;

    AcceptQuotationArgument acceptQuotation(AcceptQuotationArgument argument) throws RemoteException;
}


