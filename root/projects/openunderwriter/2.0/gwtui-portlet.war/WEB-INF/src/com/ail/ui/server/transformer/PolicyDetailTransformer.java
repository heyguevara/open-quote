/* Copyright Applied Industrial Logic Limited 20014. All rights Reserved */
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
package com.ail.ui.server.transformer;



import java.text.DateFormat;
import java.util.Date;

import com.ail.insurance.policy.SavedPolicySummary;
import com.ail.ui.shared.model.PolicyDetailDTO;

public class PolicyDetailTransformer implements Transformer<SavedPolicySummary, PolicyDetailDTO>{

    @Override
    public PolicyDetailDTO apply(SavedPolicySummary input) {
        return new PolicyDetailDTO(input.getPolicyNumber(),
                                    input.getQuotationNumber(),
                                    dateFormat(input.getQuotationDate()),
                                    dateFormat(input.getQuotationExpiryDate()),
                                    input.getProduct(),
                                    input.getPremium().toFormattedString());
    }
    
    private String dateFormat(Date date) {
        return DateFormat.getDateInstance().format(date);
    }

}
