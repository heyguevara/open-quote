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

/**
 * <p>
 * The QuestionPage element is the most commonly used page within
 * {@link PageFlow PageFlows}. As the name suggests a QuestionPage is designed
 * to hold elements that ask questions.
 * </p>
 * <p>
 * A QuestionPage is only really visible on the UI in terms of the Questions
 * (etc.) that it contains. It may optionally have a {@link #getTitle() title}
 * which will be rendered as a banner across the top of the page if it is
 * defined, and it should define an {@link #getId() id} as pages can only be
 * navigated to by their IDs, so a page without an ID will not ever be
 * displayed.
 * </p>
 * 
 * @see InformationPage
 * @see PageFlow
 */
public class QuestionPage extends Page {
    private static final long serialVersionUID = -58317419505726271L;

    public QuestionPage() {
        super();
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        model = super.renderResponse(request, response, model);

        model = super.renderPageHeader(request, response, model);

        model = executeTemplateCommand("QuestionPage", request, response, model);

        model = super.renderPageFooter(request, response, model);

        return model;
    }
}
