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

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.EJBComponent;
import com.ail.insurance.onrisk.GenerateCertificateService.GenerateCertificateArgument;
import com.ail.insurance.onrisk.GenerateInvoiceService.GenerateInvoiceArgument;
import com.ail.insurance.onrisk.GenerateWordingService.GenerateWordingArgument;

@Configurable
@Stateless
public class OnRiskBean extends EJBComponent implements OnRisk {
    private static final String namespace="com.ail.insurance.onrisk.OnRiskBean";
    private SessionContext ctx = null;

    public OnRiskBean() {
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
     * Service wrapper business method for the GenerateCertificateArgument service.
     * @param arg The argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GenerateCertificateArgument generateCertificate(GenerateCertificateArgument argument) {
        return invokeCommand("GenerateCertificate", argument);
    }
    
    /**
     * Service wrapper business method for the GenerateInvoiceCommand service.
     * @param arg The command to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GenerateInvoiceArgument generateInvoice(GenerateInvoiceArgument argument) {
        return invokeCommand("GenerateInvoice", argument);
    }
    
    /**
     * Service wrapper business method for the GenerateWordingCommand service.
     * @param arg The command to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    public GenerateWordingArgument generateWording(GenerateWordingArgument argument) {
        return invokeCommand("GenerateWording", argument);
    }
}


