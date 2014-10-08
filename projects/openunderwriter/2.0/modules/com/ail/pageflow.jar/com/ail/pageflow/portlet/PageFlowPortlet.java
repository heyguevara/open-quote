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

import static com.ail.pageflow.service.AddPageFlowNameToPageFlowContextService.PAGEFLOW_PORTLET_PREFERENCE_NAME;
import static com.ail.pageflow.service.AddProductNameToPageFlowContextService.PRODUCT_PORTLET_PREFERENCE_NAME;

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

import com.ail.core.ExceptionRecord;
import com.ail.pageflow.PageFlowContext;

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
    private String errorJSP = null;
    private PageFlowCommon pageFlowCommon = null;

    public PageFlowPortlet() {
        pageFlowCommon=new PageFlowCommon();
    }
    
    @Override
    public void init() throws PortletException {
        editJSP = getInitParameter("edit-jsp");
        configureJSP = getInitParameter("configure-jsp");
        errorJSP = getInitParameter("error-jsp");
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
            doProcessPageFlowAction(request, response);
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
        
        PageFlowContext.destroy();
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        if (isProductSelected(request)) {
            doDisplayPageFlowView(request, response);
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

    private void doProcessPageFlowAction(ActionRequest request, ActionResponse response) {

        try {
            PageFlowContext.initialise(request, response);

            pageFlowCommon.processAction(request, response);
        } catch (Throwable t) {
            handleError(t);
        }
        finally {
            PageFlowContext.destroy();
        }
    }

    private void doDisplayPageFlowView(RenderRequest request, RenderResponse response) {

        try {
            PageFlowContext.initialise(request, response);

            pageFlowCommon.doView(request, response);
        } catch (Throwable t) {
            handleError(t);
        }
        finally {
            PageFlowContext.destroy();
        }
    }

    private void handleError(Throwable t) {
        try {
            PageFlowContext.getPolicy().addException(new ExceptionRecord(t));

            pageFlowCommon.persistQuotation(PageFlowContext.getPolicy());

            String errorMessage = t.getClass() + " thrown while processing policy (systemId=" + PageFlowContext.getPolicySystemId() + "): [" + t.getMessage()+"]. Full details have been persisited with the policy.";

            PageFlowContext.getCoreProxy().logError(errorMessage);
        } catch (Throwable x) {
            PageFlowContext.getCoreProxy().logError(t.toString(), t);
        }
        finally {
            PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(errorJSP);
            try {
                portletRequestDispatcher.forward(PageFlowContext.getRequest(), PageFlowContext.getResponse());
            } catch (Exception e) {
                // We've done everything we can!
            }
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
