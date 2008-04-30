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
package com.ail.openquote.portlet;

import java.io.IOException;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletSession;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLException;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;
import com.ail.openquote.ui.PageFlow;
import com.ail.openquote.ui.RowScroller;
import com.ail.openquote.ui.util.Functions;

/**
 * This Portlet acts as the controller (in MVC terms) for the quotation process. On initially being called, it
 * inspects the value of the 'product' init parameter to discover the name of the product it has been 
 * instantiated to quote for. The product name is used to create an instance of a quotation object (the Model)
 * - which is placed in the session, and an instance of the pageflow (View). As the pageflow holds no state, it 
 * can be instantiated by the core each time it is needed without significant overhead (the assumption being that
 * the pageflow type is marked 'singleInstance' in the product's configuration.
 * @author richarda
 */
public class QuotationPortlet extends GenericPortlet {
    
    public void processAction(ActionRequest request, ActionResponse response) {
        try {
            PortletSession session=request.getPortletSession();
            PortletPreferences prefs=request.getPreferences();
            Quotation quote=quotation(session, prefs);
            PageFlow pageFlow=pageFlow(quote, session, prefs);
            
            // apply values from the request back into the model
            pageFlow.applyRequestValues(request, response, quote);
    
            // if the request is immediate, or if the page passes validation...
            if (immediate(request) || !pageFlow.processValidations(request, response, quote)) {
                // ...process the actions in the request (e.g. move to next page)
                pageFlow.processActions(request, response, quote);

                // update the persisted quote
                persistSessionQuote(request);
            }
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
    }

    public void doView(RenderRequest request, RenderResponse response) throws IOException {
        try {
            PortletSession session=request.getPortletSession();
            PortletPreferences prefs=request.getPreferences();
            Quotation quote=quotation(session, prefs);
            PageFlow pageFlow=pageFlow(quote, session, prefs);

            pageFlow.renderResponse(request, response, quote);
        }
        catch(Throwable t) {
            t.printStackTrace();
        }
    }

    /**
     * Fetch the appropriate pageflow object for this session. The 'appropriate' pageflow
     * is the one associated with the product we're quoting for in this session. We get
     * the product's name from the quote.
     * @param quote Fetch the pageflow associated with this quote's product.
     * @param session The portlet session we're part of
     * @return Pageflow
     */
    private PageFlow pageFlow(Quotation quote, PortletSession session, PortletPreferences prefs) {
        PageFlow pageFlow=null;
        
        try {
            CoreProxy core=new CoreProxy();
            // If the quote has a quote date, use that as the ved - if it doesn't (as in the case of
            // a new quote), use the date now.
            Date ved=(quote.getQuotationDate() != null) ? quote.getQuotationDate() : new Date();
            core.setVersionEffectiveDate(new VersionEffectiveDate(ved));
            pageFlow=(PageFlow)core.newProductType(productName(session, prefs), "QuotationPageFlow");
        }
        catch(Throwable e) {
            e.printStackTrace();
        }

        return pageFlow;
    }

    /**
     * If the session has a quotation object in it already, fetch and return it. If it doesn't
     * then create one, add it to the session, and return it.
     * @param session
     * @return The Quotation object for this session.
     */
    private Quotation quotation(PortletSession session, PortletPreferences prefs) {
        try {
            // if there's no quotation in the session, create one.
            if (getCurrentQuotation(session) == null) {
                CoreProxy core = new CoreProxy();
                Quotation quote=(Quotation)core.newProductType(productName(session, prefs), "Quotation");
                quote.setProductTypeId(productName(session, prefs));
                setCurrentQuotation(session, quote);
            }
    
            return getCurrentQuotation(session);
        }
        catch(Error e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Get the name of the product we're quoting for. This comes from one of two places: in normal operation 
     * it is picked up from the portlet preference named 'product'. In development mode it is picked up from
     * the session.
     * @param session
     * @return the name of the product we're quoting for, or null if none is configured.
     */
    protected String productName(PortletSession session, PortletPreferences prefs) {
        return prefs.getValue("product", (String)session.getAttribute("product"));
    }

    /**
     * Return true if this request is 'immediate'. Immediate requests are used to indicate that the actions
     * associated with the request should be immediatly executed - even if the page would fail validation. This 
     * is useful, for example, when implementing a '&lt;&lt;Back' button on a page as it will allow the user
     * to move back even if the current page isn't fully filled in yet. It's also used by the {@link RowScroller}
     * to allow the 'Add' and 'Delete' buttons to work even when the contents of the scroller may not be valid. 
     * @return true if the request is immediate, false otherwise.
     */
    private boolean immediate(ActionRequest request) {
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
    private void persistSessionQuote(ActionRequest request) throws XMLException {
        PortletSession session=request.getPortletSession();
        Quotation quote=getCurrentQuotation(session);

        if (quote!=null) {
            SavedQuotation saved=new SavedQuotation(quote);
            
            // Update it if we've saved it before, otherwise create it.
            if (quote.isPersisted()) {
                saved=new CoreProxy().update(saved);
            }
            else {
                saved=new CoreProxy().create(saved);
            }

            // Copy the system id and serial version number back into the quote for next time.
            quote.setSystemId(saved.getSystemId());
            quote.setSerialVersion(saved.getSerialVersion());
    
            setCurrentQuotation(session, quote);
        }
    }

    protected Quotation getCurrentQuotation(PortletSession session) {
        return (Quotation)session.getAttribute("quotation");
    }

    protected void setCurrentQuotation(PortletSession session, Quotation quote) {
        session.setAttribute("quotation", quote);
    }
}
