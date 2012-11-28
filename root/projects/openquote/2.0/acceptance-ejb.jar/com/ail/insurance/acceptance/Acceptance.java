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

import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.acceptance.AcceptQuotationService.AcceptQuotationArgument;
import com.ail.insurance.acceptance.CollectPremiumService.CollectPremiumArgument;
import com.ail.insurance.acceptance.ProduceDocumentationService.ProduceDocumentationArgument;
import com.ail.insurance.acceptance.PutOnRiskService.PutOnRiskArgument;

@Remote
public interface Acceptance {
    String invokeServiceXML(String xml);

    VersionEffectiveDate getVersionEffectiveDate();

    void setConfiguration(com.ail.core.configure.Configuration config);

    com.ail.core.configure.Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    ProduceDocumentationArgument produceDocumentation(ProduceDocumentationArgument argument);

    PutOnRiskArgument putOnRisk(PutOnRiskArgument argument);

    CollectPremiumArgument collectPremium(CollectPremiumArgument argument);

    AcceptQuotationArgument acceptQuotation(AcceptQuotationArgument argument);
}

