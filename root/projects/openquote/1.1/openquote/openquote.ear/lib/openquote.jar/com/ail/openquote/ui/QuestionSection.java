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
import java.util.ArrayList;
import java.util.Iterator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;

/**
 * <p>A QuestionSection renders itself as a section within a {@link Page Page} or other {@link PageContainer PageContainer}. 
 * The QuestionSection has a title, and itself contains any number of {@link Question Questions}.</p>
 * <p><img src="doc-files/QuestionSection.png"/></p>
 * There are two reason that you might consider using a QuestionSection: <ul>
 * <li>A page is too long and needs to be broken into parts - Breaking a long page of questions into sections,
 * and giving each of those sections a separate title helps usability.</li>
 * <li>Improved page layout - Each QuestionSection is rendered as it's own html table. Separating questions 
 * into separate sections therefore give the browser a better chance to optimize its use of space. This has
 * been used to good affect above. Had these two sections been rendered as one, the dropdowns would have all 
 * been aligned down the page making it look clumsy.</li></ul>
 * @see PageContainer
 * @see Page
 * @See Question
 */
public class QuestionSection extends PageElement {
	private static final long serialVersionUID = 6794522768423045427L;
    private ArrayList<? extends Question> question; 

    /** Title for this section on the page */
    private String title;
    
    public QuestionSection() {
        super();
        question=new ArrayList<Question>();
    }

    /**
     * Section title. This is rendered at the top of the section and is intended to help the
     * user identify the section.
     * @return Section's title string
     */
    public String getTitle() {
        return title;
    }

    /**
     * List of Questions which this section contains.
     * @return Question list of Questions
     */
    public ArrayList<? extends Question> getQuestion() {
        return question;
    }

    /**
     * @see #getQuestion()
     * @param question List of Questions
     */
    public void setQuestion(ArrayList<? extends Question> question) {
        this.question = question;
    }

    /**
     * @see #getTitle()
     * @param title Section's title string
     */
    public void setTitle(String title) {
        this.title = title;
    }
    
    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        boolean error=false;
        
        for (Question q : question) {
            error |= q.processValidations(request, response, model);
        }
        
        return error;
    }

    @Override
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        for (PageElement q : question) {
            q.processActions(request, response, model);
        }
    }

    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        for (Question q : question) {
            q.applyRequestValues(request, response, model);
        }
    }

    @Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w = response.getWriter();
        w.printf(" <table width='100%%' border='0' cols='4' cellpadding='4'>");

        // output the title row if a title was defined
        if (title!=null) {
            w.printf("  <tr class='portlet-section-subheader'><td colspan='4'>%s</td></tr>", getTitle());
        }

        Iterator<? extends Question> it=question.iterator();
        
        while(it.hasNext()) {
            w.printf("<tr>");
            it.next().renderResponse(request, response, model);
            w.printf("</tr>");
        }
        w.printf("</table>");
	}

    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        for(Question q: question) {
            q.renderPageHeader(request, response, model);
        }
    }
}
