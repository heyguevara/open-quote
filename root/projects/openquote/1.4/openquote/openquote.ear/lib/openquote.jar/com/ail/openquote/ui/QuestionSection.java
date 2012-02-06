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

import static com.ail.core.Functions.expand;
import static com.ail.openquote.ui.messages.I18N.i18n;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.QuotationContext;

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
     * Get the title with all variable references expanded. References are expanded with 
     * reference to the models passed in. Relative xpaths (i.e. those starting ./) are
     * expanded with respect to <i>local</i>, all others are expanded with respect to
     * <i>root</i>. 
     * @param root Model to expand references with respect to.
     * @param local Model to expand local references (xpaths starting ./) with respect to.
     * @return Title with embedded references expanded
     * @since 1.1
     */
    public String getExpandedTitle(Type root, Type local) {
    	if (getTitle()!=null) {
    		return expand(getTitle(), root, local);
    	}
    	else {
    		return null;
    	}
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
        Type localModel = (getBinding()==null) ? model : model.xpathGet(getBinding(), Type.class);
        
        for (Question q : question) {
            error |= q.processValidations(request, response, localModel);
        }
        
        return error;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        Type localModel = (getBinding()==null) ? model : model.xpathGet(getBinding(), Type.class);

        for (PageElement q : question) {
            q.processActions(request, response, localModel);
        }
        
        return model;
    }

    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        Type localModel = (getBinding()==null) ? model : model.xpathGet(getBinding(), Type.class);
        
        for (Question q : question) {
            q.applyRequestValues(request, response, localModel);
        }
        
        return model;
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    	if (!conditionIsMet(model)) {
    		return model;
    	}

        PrintWriter w = response.getWriter();
        Type localModel = (getBinding()==null) ? model : model.xpathGet(getBinding(), Type.class);
        String title = getExpandedTitle(QuotationContext.getQuotation(), model);

        title = i18n(title);
        
        String styleClass = getStyleClass();
        String ref = getRef();
        
        QuotationContext.getRenderer().renderQuestionSection(w, request, response, localModel, this, title, styleClass, ref);
        
        return model;
	}

    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        Type localModel = (getBinding()==null) ? model : model.xpathGet(getBinding(), Type.class);

        for(Question q: question) {
            q.renderPageHeader(request, response, localModel);
        }
    }

    @Override
    public void applyElementId(String basedId) {
    	int idx=0;
    	for(PageElement e: question) {
    		e.applyElementId(basedId+ID_SEPARATOR+(idx++));
    	}
    	super.applyElementId(basedId);
   	}
}
