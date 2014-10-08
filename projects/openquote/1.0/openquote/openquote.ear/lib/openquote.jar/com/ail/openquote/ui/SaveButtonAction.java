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
package com.ail.openquote.ui;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;
import com.ail.openquote.ui.util.Functions;

/**
 * <p>Adds a save button to a page. By default this button saves the quote and returns
 * the user to the current product's home page when selected.</p>
 * <p><img src="doc-files/SaveButtonAction.png"</></p>
 * <p>This page element cooperates with any {@link LoginSection LoginSection} page
 * element on the same page and will enable the LoginSection if the user is not
 * currently logged in.</p>
 * <p>The "Save" label may be overridden using the element's {@link #setLabel(String) label}
 * property.</p>
 */
public class SaveButtonAction extends CommandButtonAction {
    private static final long serialVersionUID = 7575333161831400599L;
    
    public SaveButtonAction() {
        setLabel("Save");
    }
    
    @Override
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        String op=Functions.getOperationParameters(request).getProperty("op");
        if (op!=null && op.equals(getLabel())) {
            Quotation quote=(Quotation)model;
            quote.setUserSaved(true);
            if (quote.getUsername()==null) {
                quote.setUsername(request.getRemoteUser());
            }
            SavedQuotation sq=new SavedQuotation(quote);
            new CoreProxy().update(sq);
            super.processActions(request, response, model);
            request.getPortletSession().invalidate();
        }
    }

    @Override
    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();

        if (request.getRemoteUser()!=null) {
            w.printf("<input type='submit' name='op=%1$s' value='%1$s' class='portlet-form-input-field'/>", getLabel());
        }
        else {
            w.printf("<input type='button' onClick='%s' name='op=%2$s' value='%2$s' class='portlet-form-input-field'/>", LoginSection.reset, getLabel());
        }
    }
}
