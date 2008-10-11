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
package com.ail.openquote;

import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.core.XMLString;

public class SavedQuotation extends SavedQuotationSummary {
    private static final long serialVersionUID = 8924615006523668514L;

    private XMLString quotationXML;
    private byte[] quotationDocument;
    
    public SavedQuotation() {
    }

    public SavedQuotation(Quotation quotation) {
        setQuotation(quotation);
    }
    
    public void setQuotation(Quotation quotation) {
        this.quotationXML=new CoreProxy().toXML(quotation);
        setQuotationNumber(quotation.getQuotationNumber());
        setQuotationDate(quotation.getQuotationDate());
        setQuotationExpiryDate(quotation.getQuotationExpiryDate());
        setStatus(quotation.getStatus());
        setPage(quotation.getPage());
        setProduct(quotation.getProductTypeId());
        setSystemId(quotation.getSystemId());
        setSerialVersion(quotation.getSerialVersion());
        setUsername(quotation.getUsername());
        setUserSaved(quotation.isUserSaved());
        setTestCase(quotation.isTestCase());
        
        try {
            setPremium(quotation.getTotalPremium());
        }
        catch(IllegalStateException e) {
            // ignore this - we'll get it if the quotation doesn't have a premium; as  
            // we're saving applications, it shouldn't come as a surprise that they don't
            // always have premiums.
        }
    }
    
    public Quotation getQuotation() throws XMLException {
        Quotation quotation=new CoreProxy().fromXML(Quotation.class, quotationXML);
        quotation.setSerialVersion(super.getSerialVersion());
        quotation.setSystemId(super.getSystemId());
        return quotation;
    }
    
    public String getQuotationAsString() {
        return quotationXML.toString();
    }

    public void setQuotationAsString(String quotation) throws XMLException {
        this.quotationXML=new XMLString(quotation);
    }

    public byte[] getQuotationDocument() {
        return quotationDocument;
    }

    public void setQuotationDocument(byte[] quotationDocument) {
        this.quotationDocument = quotationDocument;
    }
}
