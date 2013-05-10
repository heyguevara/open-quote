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
package com.ail.insurance.pageflow.portlet;

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
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.PortletURL;
import javax.portlet.ReadOnlyException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.ValidatorException;

import com.ail.core.ExceptionRecord;
import com.ail.insurance.pageflow.util.QuotationCommon;
import com.ail.insurance.pageflow.util.PageflowContext;
import com.ail.insurance.policy.Policy;

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
public class QuotationPortlet extends GenericPortlet {
    private String editJSP = null;
    private String configureJSP = null;

    @Override
    public void init() throws PortletException {
        editJSP = getInitParameter("edit-jsp");
        configureJSP = getInitParameter("configure-jsp");
    }

    @Override
    protected Collection<PortletMode> getNextPossiblePortletModes(RenderRequest request) {
        if (request.getPreferences().getValue("product", null) == null) {
            return Arrays.asList(PortletMode.EDIT);
        } else {
            return Arrays.asList(PortletMode.EDIT, PortletMode.VIEW);
        }
    }

    @Override
    public void processAction(ActionRequest request, ActionResponse response) throws ReadOnlyException, ValidatorException, IOException, PortletModeException {
        if (PortletMode.EDIT.equals(request.getPortletMode())) {
            processEditAction(request, response);
        }

        if (PortletMode.VIEW.equals(request.getPortletMode())) {
            processViewAction(request, response);
        }
    }

    private void processViewAction(ActionRequest request, ActionResponse response) {
        PageflowContext.initialise(request);

        try {
            QuotationCommon.processAction(request, response);
        } catch (Throwable t) {
            Policy policy = PageflowContext.getPolicy();

            if (policy == null) {
                t.printStackTrace();
            } else {
                policy.addException(new ExceptionRecord(t));
                QuotationCommon.persistQuotation(policy);
            }

            // TODO Forward to an error page
        }
    }

    private void processEditAction(ActionRequest request, ActionResponse response) throws ReadOnlyException, IOException, ValidatorException, PortletModeException {
        PortletPreferences prefs = request.getPreferences();
        prefs.setValue("product", request.getParameter("productName"));
        prefs.store();
        response.setPortletMode(PortletMode.VIEW);
    }

    @Override
    public void doEdit(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PageflowContext.initialise(request);
        response.setContentType("text/html");
        PortletURL addNameURL = response.createActionURL();
        addNameURL.setParameter("productName", "productName");
        request.setAttribute("productNameURL", addNameURL.toString());
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(editJSP);
        portletRequestDispatcher.include(request, response);
    }

    @Override
    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        if (request.getPreferences().getValue("product", null) == null) {
            doDisplayConfigureView(request, response);
        } else {
            doDisplayQuotationView(request, response);
        }
    }

    private void doDisplayConfigureView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        PortletRequestDispatcher portletRequestDispatcher = getPortletContext().getRequestDispatcher(configureJSP);
        portletRequestDispatcher.include(request, response);
    }

    private void doDisplayQuotationView(RenderRequest request, RenderResponse response) {
        PageflowContext.initialise(request);

        try {
            QuotationCommon.doView(request, response);
        } catch (Throwable t) {
            Policy policy = PageflowContext.getPolicy();

            if (policy == null) {
                t.printStackTrace();
            } else {
                policy.addException(new ExceptionRecord(t));
                QuotationCommon.persistQuotation(policy);
            }

            // TODO Forward to an error page
        }

    }
}
