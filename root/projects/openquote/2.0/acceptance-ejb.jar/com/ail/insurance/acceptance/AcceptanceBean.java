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

import javax.ejb.Remote;
import javax.ejb.Local;
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.StatelessComponent;
import com.ail.insurance.acceptance.AcceptQuotationService.AcceptQuotationArgument;
import com.ail.insurance.acceptance.CollectPremiumService.CollectPremiumArgument;
import com.ail.insurance.acceptance.ProduceDocumentationService.ProduceDocumentationArgument;
import com.ail.insurance.acceptance.PutOnRiskService.PutOnRiskArgument;

@Configurable
@Stateless
@Remote(AcceptanceRemote.class)
@Local(AcceptanceLocal.class)
public class AcceptanceBean extends StatelessComponent {

    public AcceptanceBean() {
        initialise("com.ail.insurance.acceptance.AcceptanceBean");
    }

    /**
     * Service wrapper method for the ProduceDocumentation service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ProduceDocumentationArgument produceDocumentation(ProduceDocumentationArgument argument) {
        return invokeCommand("ProduceDocumentation", argument);
    }

    /**
     * Service wrapper method for the PutOnRisk service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public PutOnRiskArgument putOnRisk(PutOnRiskArgument argument) {
        return invokeCommand("PutOnRisk", argument);
    }

    /**
     * Service wrapper method for the CollectPremium service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public CollectPremiumArgument collectPremium(CollectPremiumArgument argument) {
        return invokeCommand("CollectPremium", argument);
    }

    /**
     * Service wrapper method for the AcceptQuotation service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public AcceptQuotationArgument acceptQuotation(AcceptQuotationArgument argument) {
        return invokeCommand("AcceptQuotation", argument);
    }
}


