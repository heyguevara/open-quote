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

package com.ail.insurance.onrisk;

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

import com.ail.core.BaseServerException;
import com.ail.insurance.onrisk.certificate.GenerateCertificateArg;
import com.ail.insurance.onrisk.invoice.GenerateInvoiceArg;
import com.ail.insurance.onrisk.wording.GenerateWordingArg;

public interface OnRisk extends EJBObject {
    String invokeServiceXML(String xml) throws RemoteException;

    /**
     * Service wrapper business method for the GenerateCertificateArg service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateCertificateArg generateCertificate(GenerateCertificateArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the GenerateInvoiceArg service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateInvoiceArg generateInvoice(GenerateInvoiceArg arg) throws RemoteException;

    /**
     * Service wrapper business method for the GenerateWordingArg service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateWordingArg generateWording(GenerateWordingArg arg) throws RemoteException;
}



