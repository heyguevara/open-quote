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
package com.ail.openquote.ui.util;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;
import com.ail.openquote.ui.PageFlow;
import com.ail.openquote.ui.RowScroller;

/**
 * This class defines methods which are common to both the quotation and sandpit Portlets.
 */
public class QuotationCommon {
    
    public static void processAction(ActionRequest request, ActionResponse response) throws XMLException {
    	PageFlow pageFlow=QuotationContext.getPageFlow();
    	Quotation quotation=QuotationContext.getQuotation();
        
        // apply values from the request back into the model
        pageFlow.applyRequestValues(request, response, quotation);

        // if the request is immediate, or if the page passes validation...
        if (immediate(request) || !pageFlow.processValidations(request, response, quotation)) {
            // ...process the actions in the request (e.g. move to next page)
            pageFlow.processActions(request, response, quotation);

            // update the persisted quote
            persistQuote();
        }
    }

    public static void doView(RenderRequest request, RenderResponse response) throws IOException {
    	PageFlow pageFlow=QuotationContext.getPageFlow();
    	Quotation quotation=QuotationContext.getQuotation();
        
        pageFlow.renderResponse(request, response, quotation);
    }

    /**
     * Get the name of the product we're quoting for. This comes from one of two places: in normal operation 
     * it is picked up from the portlet preference named 'product'. In development mode it is picked up from
     * the session.
     * @param session
     * @return the name of the product we're quoting for, or null if none is configured.
     */
    public static String productName(PortletSession session, PortletPreferences prefs) {
        return prefs.getValue("product", (String)session.getAttribute("product"));
    }

    /**
     * Return true if this request is 'immediate'. Immediate requests are used to indicate that the actions
     * associated with the request should be immediately executed - even if the page would fail validation. This 
     * is useful, for example, when implementing a '&lt;&lt;Back' button on a page as it will allow the user
     * to move back even if the current page isn't fully filled in yet. It's also used by the {@link RowScroller}
     * to allow the 'Add' and 'Delete' buttons to work even when the contents of the scroller may not be valid. 
     * @return true if the request is immediate, false otherwise.
     */
    private static boolean immediate(ActionRequest request) {
        String im=Functions.getOperationParameters(request).getProperty("immediate");
        
        return "true".equals(im);
    }
    
    /**
     * Persist the current quotation. Take the quotation from the session, and persist
     * it to the database. If a record does not already exist, one is created; if one 
     * does exist, it is updated. Because persisting a object may modify it (a systemId
     * may be added, or a serial version incremented), the session's quotation object
     * is replaced. 
     * @param session
     * @throws XMLException
     */
    private static void persistQuote() throws XMLException {
        Quotation quote=QuotationContext.getQuotation();

        if (quote!=null) {
        	quote=persistQuotation(quote);
        	QuotationContext.setQuotation(quote);
        }
    }

    public static Quotation persistQuotation(Quotation quote) {
        if (quote!=null) {
        	SavedQuotation saved=null;
        	CoreProxy proxy=new CoreProxy();
        	
            // Update it if we've saved it before, otherwise create it.
        	if (quote.isPersisted()) {
        		saved=(SavedQuotation)proxy.queryUnique("get.savedQuotation.by.systemId", quote.getSystemId());
        		saved.setQuotation(quote);
        		saved=proxy.update(saved);
            }
            else {
            	saved=new SavedQuotation(quote);
                saved=proxy.create(saved);
            }

            // Copy the system id and serial version number back into the quote for next time.
            quote.setSystemId(saved.getSystemId());
            quote.setSerialVersion(saved.getSerialVersion());
        }
        
        return quote;
    }
}
