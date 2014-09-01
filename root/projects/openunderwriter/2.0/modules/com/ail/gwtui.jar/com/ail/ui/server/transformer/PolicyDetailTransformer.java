/* Copyright Applied Industrial Logic Limited 2014. All rights Reserved */
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
import java.util.List;

import com.ail.core.XMLException;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.party.Address;
import com.ail.ui.shared.model.PolicyDetailDTO;
import com.google.gwt.thirdparty.guava.common.collect.Lists;

public class PolicyDetailTransformer implements Transformer<SavedPolicy, PolicyDetailDTO>{

    @Override
    public PolicyDetailDTO apply(SavedPolicy input) throws Exception {

        return new PolicyDetailDTO(input.getPolicyNumber(),
                                    input.getQuotationNumber(),
                                    policyHolderName(input),
                                    policyHolderAddress(input),
                                    dateFormat(input.getQuotationDate()),
                                    policyExpiryDate(input),
                                    input.getProduct(),
                                    premium(input.getPremium()));
    }
    
    private String dateFormat(Date date) {
        if (date != null) {
            return DateFormat.getDateInstance().format(date);
        } else {
            return "";
        }
    }
    private String premium(CurrencyAmount premium) {
        if (premium != null) {
            return premium.toFormattedString();
        } else {
            return "";
        }
    }
    
    private String policyExpiryDate(SavedPolicy input) throws XMLException {
        if (input.getPolicy() != null
                && input.getPolicy().getExpiryDate() != null) {
            return dateFormat(input.getPolicy().getExpiryDate());
        } else {
            return "";
        }
    }
    
    private String policyHolderName(SavedPolicy input) throws XMLException {
        if (input.getPolicy() != null
                && input.getPolicy().getProposer() != null) {
            return input.getPolicy().getProposer().getLegalName();
        } else {
            return "";
        }
    }
    
    private List<String> policyHolderAddress(SavedPolicy input) throws XMLException {
        if (input.getPolicy() != null
                && input.getPolicy().getProposer() != null
                && input.getPolicy().getProposer().getAddress() != null) {
            Address address = input.getPolicy().getProposer().getAddress();
            return Lists.newArrayList(
                    address.getLine1(), address.getPostcode());
        } else {
            return Lists.newArrayList();
        }
    }

}
