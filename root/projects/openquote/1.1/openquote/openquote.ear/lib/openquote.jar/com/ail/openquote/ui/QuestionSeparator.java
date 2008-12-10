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

import static com.ail.openquote.ui.util.Functions.findErrors;
import static com.ail.openquote.ui.util.Functions.hasErrorMarkers;
import static com.ail.openquote.ui.messages.I18N.i18n;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.Functions;

/**
 * <p>The QuestionSeparator is used to break up long list of questions with either a title, or simple white space.</p>
 * <p><img src="doc-files/QuestionSeparator.png"/></p>
 * <p>In the example above, a page of conditions has been broken into three blocks to make it more readable. Three
 * QuestionSeparators are used, two with titles one without.</p>
 * @see com.ail.openquote.ui.Question
 */
public class QuestionSeparator extends Question {
    private static final long serialVersionUID = 7118438575837087257L;

    public QuestionSeparator() {
		super();
	}

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
    	return model;
    }

    @Override
    public void renderPageFooter(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException,
            IOException {
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
    	return model;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
    	return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return false;
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return renderResponse(request, response, model, null);
    }

	@Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        String title = getExpandedTitle(model);

        PrintWriter w=response.getWriter();
        
        
        if (title==null) {
            if (getBinding()!=null && hasErrorMarkers(model.xpathGet(getBinding(), Type.class))) {
        		w.printf("<td class='portlet-section-subheader' colspan='4'>%s</td>", findErrors(model.xpathGet(getBinding(), Type.class)));
        	}
        	else {
        		w.printf("<td class='portlet-section-subheader' colspan='4'>&nbsp;</td>");
        	}
        }
        else {
            w.printf("<td colspan='4'><table width='100%%''>"); 
            w.printf("<tr><td class='portlet-section-subheader' colspan='4'>%s</td></tr>", Functions.hideNull(i18n(title))); 
            if (getBinding()!=null && hasErrorMarkers(model.xpathGet(getBinding(), Type.class))) {
            	w.printf("<tr><td class='portlet-msg-error' colspan='4'>%s</td>", findErrors(model.xpathGet(getBinding(), Type.class)));
            }
            w.printf("</table></td>");
        }
        
        return model;
    }
}
