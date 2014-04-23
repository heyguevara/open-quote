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
package com.ail.pageflow.portlet;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletException;
import javax.portlet.PortletMode;
import javax.portlet.PortletModeException;
import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import com.ail.core.BaseException;
import com.ail.core.ExceptionRecord;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.util.PageFlowContext;

import static com.ail.pageflow.service.AddPageFlowNameToPageFlowContextService.PAGEFLOW_PORTLET_PREFERENCE_NAME;
import static com.ail.pageflow.service.AddProductNameToPageFlowContextService.PRODUCT_PORTLET_PREFERENCE_NAME;

/**
 * This Portlet acts as the controller (in MVC terms) for the quotation process.
 * On initially being called, it inspects the value of the 'product' init
 * parameter to discover the name of the product it has been instantiated to
 * quote for. The product name is used to create an instance of a quotation
 * object (the Model) - which is placed in the session, and an instance of the
 * PageFlow (View). As the PageFlow holds no state, it can be instantiated by
 * the core each time it is needed without significant overhead (the assumption
 * being that the PageFlow type is marked 'singleInstance' in the product's
 * configuration.
 */
public class PageFlowPortlet extends GenericPortlet {
    private String editJSP = null;
    private String configureJSP = null;
    private QuotationCommon quotationCommon = null;

    public PageFlowPortlet() {
        quotationCommon=new QuotationCommon();
    }
    
    @Override
    public void init() throws PortletException {
        editJSP = getInitParameter("edit-jsp");
        configureJSP = getInitParameter("configure-jsp");
    }

    @Override
    protected Collection<PortletMode> getNextPossiblePortletModes(RenderRequest request) {
        if (isProductSelected(request)) {
            return Arrays.asList(PortletMode.EDIT);
        } else {
            return Arrays.asList(PortletMode.EDIT, PortletMode.VIEW);
        }
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws ReadOnlyException, ValidatorException, IOException, PortletModeException {
        if (PortletMode.EDIT.equals(request.getPortletMode())) {
            doProcessEditAction(request, response);
        }

        if (PortletMode.VIEW.equals(request.getPortletMode())) {
            doProcessQuotationAction(request, response);
        }
    }

    @Override
    public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PageFlowContext.initialise(request, response);
        response.setContentType("text/html");

        PortletURL addNameURL = response.createActionURL();
        
        String selectedProduct = request.getPreferences().getValue(PRODUCT_PORTLET_PREFERENCE_NAME, null);
        String selectedPageFlow = request.getPreferences().getValue(PAGEFLOW_PORTLET_PREFERENCE_NAME, null);        

        request.setAttribute("productName", selectedProduct);
        request.setAttribute("pageFlowName", selectedPageFlow);
        request.setAttribute("productNameURL", addNameURL.toString());
        
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(editJSP);
        
        portletRequestDispatcher.include(request, response);
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        if (isProductSelected(request)) {
            doDisplayQuotationView(request, response);
        } else {
            doDisplayConfigureView(request, response);
        }
    }

    private void doProcessEditAction(ActionRequest request, ActionResponse response) throws ReadOnlyException, IOException, ValidatorException, PortletModeException {
        PortletPreferences prefs = request.getPreferences();
        
        if (!"?".equals(request.getParameter("pageFlowName"))) {
            prefs.setValue(PAGEFLOW_PORTLET_PREFERENCE_NAME, request.getParameter("pageFlowName"));
        }
        
        if (!"?".equals(request.getParameter("productName"))) {
            String oldValue = prefs.getValue(PRODUCT_PORTLET_PREFERENCE_NAME, "?");
            String newValue = request.getParameter("productName");
            if (oldValue==null || !oldValue.equals(newValue)) {
                prefs.setValue(PRODUCT_PORTLET_PREFERENCE_NAME, newValue);
                prefs.setValue(PAGEFLOW_PORTLET_PREFERENCE_NAME, null);
            }
        }
        
        prefs.store();
        
        if (isProductSelected(request)) {
            response.setPortletMode(PortletMode.VIEW);
        }
    }

    private void doDisplayConfigureView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(configureJSP);
        portletRequestDispatcher.include(request, response);
    }

    private void doProcessQuotationAction(ActionRequest request, ActionResponse response) {
        PageFlowContext.initialise(request, response);

        try {
            quotationCommon.processAction(request, response);
        } catch (Throwable t) {
            Policy policy = PageFlowContext.getPolicy();

            if (policy == null) {
                t.printStackTrace();
            } else {
                policy.addException(new ExceptionRecord(t));
                try {
                    quotationCommon.persistQuotation(policy);
                } catch (BaseException e) {
                    // TODO Forward to an error page
                    e.printStackTrace();
                }
            }

            // TODO Forward to an error page
        }
    }

    private void doDisplayQuotationView(RenderRequest request, RenderResponse response) {
        PageFlowContext.initialise(request, response);

        try {
            quotationCommon.doView(request, response);
        } catch (Throwable t) {
            Policy policy = PageFlowContext.getPolicy();

            if (policy == null) {
                t.printStackTrace();
            } else {
                policy.addException(new ExceptionRecord(t));
                try {
                    quotationCommon.persistQuotation(policy);
                } catch (BaseException e) {
                    // TODO Forward to an error page
                    e.printStackTrace();
                }
            }

            // TODO Forward to an error page
        }
    }

    private boolean isProductSelected(PortletRequest request) {
        String selectedProduct = request.getPreferences().getValue(PRODUCT_PORTLET_PREFERENCE_NAME, null);
        String selectedPageFlow = request.getPreferences().getValue(PAGEFLOW_PORTLET_PREFERENCE_NAME, null);

        if (selectedProduct == null || "".equals(selectedProduct) || "?".equals(selectedProduct) 
        ||  selectedPageFlow == null || "".equals(selectedPageFlow) || "?".equals(selectedPageFlow)) {
            return false;
        } else {
            return true;
        }
    }
}
