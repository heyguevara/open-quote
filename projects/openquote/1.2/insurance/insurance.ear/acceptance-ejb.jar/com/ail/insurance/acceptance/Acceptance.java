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
import com.ail.insurance.acceptance.acceptquotation.AcceptQuotationArg;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/03/03 09:31:07 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/acceptance-ejb.jar/com/ail/insurance/acceptance/Acceptance.java,v $
 */
public interface Acceptance extends javax.ejb.EJBObject {
    /** @link dependency */

    /*# AcceptanceBean lnkAcceptanceBean; */

    String invokeServiceXML(String xml) throws RemoteException;

    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException;

    void setConfiguration(com.ail.core.configure.Configuration config) throws RemoteException;

    com.ail.core.configure.Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    com.ail.core.Version getVersion() throws RemoteException;

    ProduceDocumentationArg produceDocumentation(ProduceDocumentationArg arg) throws RemoteException;

    PutOnRiskArg putOnRisk(PutOnRiskArg arg) throws RemoteException;

    CollectPremiumArg collectPremium(CollectPremiumArg arg) throws RemoteException;

    AcceptQuotationArg acceptQuotation(AcceptQuotationArg arg) throws RemoteException;
}


