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

import javax.ejb.Local;

import com.ail.core.BaseServerException;
import com.ail.insurance.onrisk.GenerateCertificateService.GenerateCertificateArgument;
import com.ail.insurance.onrisk.GenerateInvoiceService.GenerateInvoiceArgument;
import com.ail.insurance.onrisk.GenerateWordingService.GenerateWordingArgument;

@Local
public interface OnRiskLocal {
    /**
     * Service wrapper business method for the GenerateCertificateCommand service.
     * @param arg The command to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateCertificateArgument generateCertificate(GenerateCertificateArgument argument);

    /**
     * Service wrapper business method for the GenerateInvoiceCommand service.
     * @param arg The Argument to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateInvoiceArgument generateInvoice(GenerateInvoiceArgument argument);

    /**
     * Service wrapper business method for the GenerateWordingCommand service.
     * @param arg The command to pass to the service.
     * @return The objects returned from the service.
     * @throws BaseServerException In response to any exception thrown by the service.
     */
    GenerateWordingArgument generateWording(GenerateWordingArgument argument);
}



