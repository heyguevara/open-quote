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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * A blank page element outputs nothing. Specifically it renders a non-breaking space.
 */
public class Blank extends PageElement {
	private static final long serialVersionUID = -1320299722728499324L;

	public Blank() {
		super();
	}

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
		// nothing to do here - Blank doesn't have a value
		return model;
	}

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
		// nothing to do here - Blank doesn't have validations
        return false;
	}

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
		return QuotationContext.getRenderer().renderBlank(response.getWriter(), request, response, model, this);
	}
}
