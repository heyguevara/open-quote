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
package com.ail.insurance.pageflow;

import static com.ail.insurance.pageflow.util.I18N.i18n;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.pageflow.util.QuotationContext;

/**
 * <p>The Question page element is probably one of the more commonly used elements in pageflows. It renders as a single line 
 * within its container which presents a question on the left, and prompts for an answer on the right. Validation errors
 * are rendered to the right of the answer.</p>
 * <p><img src="doc-files/Question.png"/></p>
 * <p>The screenshot above shows 12 questions; the first showing the locations of a question's parts.</p>
 * <p>The way in which the 'value binding' section is rendered depends entirely on the nature of the
 * {@link com.ail.core.Attribute Attribute} which the question is bound to (via it's {@link #getBinding() binding} property).
 * The screenshot shows how String Attributes are rendered (Name, Freeze brand and Passport are all examples 
 * of string Attributes). It also show Choice Attributes (Age, Gender, Colour, etc); and Currency Attributes (Purchase price and Sum insured).</p>
 * <p>The validations applied to each answer are also defined by the properties of the Attribute that the question is bound to.</p>
 * @see com.ail.core.Attribute
 */
public class Question extends AttributeField {
    private static final long serialVersionUID = 7118438575837087257L;

    public Question() {
		super();
	}

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    return renderResponse(request, response, model, "");
    }

	@Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
    	if (!conditionIsMet(model)) {
    		return model;
    	}

    	String title = i18n(getExpandedTitle(model));
    	
    	String styleClass = getStyleClass();
    	
    	String ref = getRef();

        PrintWriter w=response.getWriter();
        
        return QuotationContext.getRenderer().renderQuestion(w, request, response, model, this, title, rowContext, styleClass, ref);
    }
}
