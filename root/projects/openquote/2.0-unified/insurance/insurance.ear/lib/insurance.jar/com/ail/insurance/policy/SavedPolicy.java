/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.insurance.policy;

import com.ail.annotation.TypeDefinition;
import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.core.XMLString;

@TypeDefinition
public class SavedPolicy extends SavedPolicySummary {
    private static final long serialVersionUID = 8924615006523668514L;

    private XMLString policyXML;
    private byte[] quotationDocument;
    private byte[] invoiceDocument;
    private byte[] wordingDocument;
    private byte[] certificateDocument;
    
    public SavedPolicy() {
    }

    public SavedPolicy(Policy policy) {
        setPolicy(policy);
    }
    
    public void setPolicy(Policy policy) {
        this.policyXML=new CoreProxy().toXML(policy);
        setQuotationNumber(policy.getQuotationNumber());
        setPolicyNumber(policy.getPolicyNumber());
        setQuotationDate(policy.getQuotationDate());
        setQuotationExpiryDate(policy.getQuotationExpiryDate());
        setStatus(policy.getStatus());
        setPage(policy.getPage());
        setProduct(policy.getProductTypeId());
        setSystemId(policy.getSystemId());
        setSerialVersion(policy.getSerialVersion());
        setUsername(policy.getUsername());
        setUserSaved(policy.isUserSaved());
        setTestCase(policy.isTestCase());
        
        try {
            setPremium(policy.getTotalPremium());
        }
        catch(IllegalStateException e) {
            // ignore this - we'll get it if the policy doesn't have a premium; as  
            // we're saving applications, it shouldn't come as a surprise that they don't
            // always have premiums.
        }
    }
    
    public Policy getPolicy() throws XMLException {
        Policy policy=new CoreProxy().fromXML(Policy.class, policyXML);
        policy.setSerialVersion(super.getSerialVersion());
        policy.setSystemId(super.getSystemId());
        return policy;
    }
    
    public String getPolicyAsString() {
        return policyXML.toString();
    }

    public void setPolicyAsString(String policy) throws XMLException {
        this.policyXML=new XMLString(policy);
    }

    public byte[] getQuotationDocument() {
        return quotationDocument;
    }

    public void setQuotationDocument(byte[] quotationDocument) {
        this.quotationDocument = quotationDocument;
    }

    public byte[] getInvoiceDocument() {
        return invoiceDocument;
    }

    public void setInvoiceDocument(byte[] invoiceDocument) {
        this.invoiceDocument = invoiceDocument;
    }

    public byte[] getWordingDocument() {
        return wordingDocument;
    }

    public void setWordingDocument(byte[] wordingDocument) {
        this.wordingDocument = wordingDocument;
    }

    public byte[] getCertificateDocument() {
        return certificateDocument;
    }

    public void setCertificateDocument(byte[] certificateDocument) {
        this.certificateDocument = certificateDocument;
    }
}
