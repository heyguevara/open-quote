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

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * An abstract UI element providing default handler methods common to its concrete sub-classes.
 */
public abstract class Page extends PageContainer {
	private static final long serialVersionUID = 297215265083279666L;
    
    public Page() {
        super();
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        response.setContentType(request.getResponseContentType());

        // Execute any page actions defined for this page
        for (Action a : getAction()) {
            model=a.renderResponse(request, response, model);
        }

        return QuotationContext.getRenderer().renderPage(response.getWriter(), request, response, model, this);
    }

    @Override
    public void renderPageFooter(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // Give all the elements a chance to output page level content.
        for (PageElement e : super.getPageElement()) {
            e.renderPageFooter(request, response, model);
        }
    }

    @Override
    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        // Give all the elements a chance to output page level content.
        for (PageElement e : super.getPageElement()) {
            e.renderPageHeader(request, response, model);
        }
    }
}
