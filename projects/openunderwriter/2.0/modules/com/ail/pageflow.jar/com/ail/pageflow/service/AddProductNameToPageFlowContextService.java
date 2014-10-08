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

package com.ail.pageflow.service;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.pageflow.ExecutePageActionService;
import com.ail.pageflow.PageFlowContext;

/**
 * Get the name of the product we're working for. This comes from one of three
 * places: in normal operation it is picked up from the portlet preference named
 * 'product', or from the request property "openquote.product". In development
 * mode (i.e. in the sandpit) it is picked up from the session.
 */
@ServiceImplementation
public class AddProductNameToPageFlowContextService extends Service<ExecutePageActionService.ExecutePageActionArgument> {
    private static final long serialVersionUID = 3198813603833694389L;
    public static final String PRODUCT_PORTLET_PREFERENCE_NAME = "product";
    public static final String PRODUCT_PORTLET_REQUEST_PARAMETER_NAME = "openquote.product";

    @Override
    public void invoke() throws BaseException {
        if (args.getPortletRequestArg() == null) {
            throw new PreconditionException("args.getPortletRequestArg()==null");
        }

        if (args.getPortletSessionArg() == null) {
            throw new PreconditionException("(args.getPortletSessionArg()");
        }

        if (args.getPortletPreferencesArg() == null) {
            throw new PreconditionException("args.getPortletPreferencesArg() == null");
        }

        PortletPreferences preferences = args.getPortletPreferencesArg();
        PortletRequest request = args.getPortletRequestArg();

        String productName;

        // The request property takes precedence over everything
        productName = request.getProperty(PRODUCT_PORTLET_REQUEST_PARAMETER_NAME);

        // If the request property is null, try the portlet preference
        if (productName == null) {
            productName = preferences.getValue(PRODUCT_PORTLET_PREFERENCE_NAME, null);
        }

        // If the portlet reference was also null, try the session attribute
        if (productName == null) {
            productName = getProductNameFromPageFlowContext();
        }

        // Regardless of what pageflow name we have, even if it is null, pass it
        // into the context.
        setProductNameToPageFlowContext(productName);

        if (productName!=null) {
            String namespace = productNameToConfigurationNamespace(getProductNameFromPageFlowContext());
            PageFlowContext.setCoreProxy(new CoreProxy(namespace));
        }
        else {
            PageFlowContext.setCoreProxy(new CoreProxy());
        }
    }
    
     // Wrap call to PageFlowContext static to help testability.
    protected String getProductNameFromPageFlowContext() {
        return PageFlowContext.getProductName();
    }
    
    // Wrap call to PageFlowContext static to help testability.
    protected void setProductNameToPageFlowContext(String productNameArg) {
        PageFlowContext.setProductName(productNameArg);
    }
}