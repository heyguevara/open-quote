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

package com.ail.pageflow.util;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.claim.SectionNotFoundException;

/**
 * Get the name of the product we're working for. This comes from one of three
 * places: in normal operation it is picked up from the portlet preference named
 * 'product', or from the request property "openquote.product". In development
 * mode (i.e. in the sandpit) it is picked up from the session.
 */
@ServiceImplementation
public class AddProductNameToPageflowContextService extends Service<AddProductNameToPageflowContextService.AddProductNameToPageflowContextArgument> {
    private static final long serialVersionUID = 3198813603833694389L;
    public static final String PRODUCT_SESSION_ATTRIBUTE_NAME = "product";
    public static final String PRODUCT_PORTLET_PREFERENCE_NAME = "product";
    public static final String PRODUCT_PORTLET_REQUEST_PARAMETER_NAME = "openquote.product";

    @ServiceArgument
    public interface AddProductNameToPageflowContextArgument extends Argument {
        PortletRequest getPortletRequestArg();

        void setPortletRequestArg(PortletRequest arg);
    }

    @ServiceCommand(defaultServiceClass = AddProductNameToPageflowContextService.class)
    public interface AddProductNameToPageflowContextCommand extends Command, AddProductNameToPageflowContextArgument {
    }

    /**
     * The 'business logic' of the entry point.
     * 
     * @throws PreconditionException
     *             If one of the preconditions is not met
     * @throws SectionNotFoundException
     *             If one of the sections identified in the
     */
    @Override
    public void invoke() throws BaseException {
        if (args.getPortletRequestArg() == null) {
            throw new PreconditionException("args.getPortletRequestArg()==null");
        }

        if (args.getPortletRequestArg().getPortletSession() == null) {
            throw new PreconditionException("args.getPortletRequestArg().getPortletSession()==null");
        }

        if (args.getPortletRequestArg().getPreferences() == null) {
            throw new PreconditionException("args.getPortletRequestArg().getPreferences()==null");
        }

        String pageflowName;

        PortletSession session = args.getPortletRequestArg().getPortletSession();
        PortletPreferences prefs = args.getPortletRequestArg().getPreferences();

        // The request property takes precedence over everything
        pageflowName = args.getPortletRequestArg().getProperty(PRODUCT_PORTLET_REQUEST_PARAMETER_NAME);

        // If the request property is null, try the portlet preference
        if (pageflowName == null) {
            pageflowName = prefs.getValue(PRODUCT_PORTLET_PREFERENCE_NAME, null);
        }

        // If the portlet reference was also null, try the session attribute
        if (pageflowName == null) {
            pageflowName = (String) session.getAttribute(PRODUCT_SESSION_ATTRIBUTE_NAME);
        }

        // Regardless of what pageflow name we have, even if it is null, pass it
        // into the context.
        PageflowContext.setProductName(pageflowName);
    }
}