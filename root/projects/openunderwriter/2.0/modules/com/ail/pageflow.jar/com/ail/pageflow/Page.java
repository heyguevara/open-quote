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
package com.ail.pageflow;

import java.io.IOException;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.pageflow.util.PageFlowContext;

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
        if (PageFlowContext.getPageFlow().isAdvancingPage()) {
            for(Action a: getAction()) {
                a.executeAction(request, model, ActionType.ON_PAGE_ENTRY);
            }
        }
        
        response.setContentType(request.getResponseContentType());

        return executeTemplateCommand("Page", request, response, model);
    }

    @Override
    public Type renderPageFooter(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand("PageFooter", request, response, model);
    }

    @Override
    public Type renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand("PageHeader", request, response, model);
    }
}
