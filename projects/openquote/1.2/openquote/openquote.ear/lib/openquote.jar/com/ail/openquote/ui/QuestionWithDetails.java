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
import static com.ail.openquote.ui.util.Functions.convertCsvToList;
import static com.ail.openquote.ui.util.Functions.convertListToCsv;
import static com.ail.openquote.ui.util.Functions.xpathToId;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * <p>This element handles the common situation where selecting 'yes' in answer to a question
 * leads to the user having to answer a supplementary question (e.g. "If yes, please supply details").</p>
 * <p>This is a simplification of {@link QuestionWithSubSection QuestionWithSubSection} which 
 * isn't limited to a single supplementary question.</p>
 * <p><img src="doc-files/QuestionWithDetails.png"/></p>
 * <p>The main question must be a YesOrNo question, and must be phrased in such a way that "Yes" 
 * leads to more detail being required. As with the {@link Question Question} page element, the
 * detail question's answer field is rendered based on the properties of the {@link com.ail.core.Attribute Attribute}
 * that it is bound to.</p> 
 * <p>The detail field is only enabled if the question has been answered appropriately. By default this means that the 
 * answer is "Yes"; but this may be overridden by setting the detailsEnabledFor property to a semicolon separated list
 * of the answers that the details should be enabled for..</p>
 * <p>Validation is applied to the YesOrNo question, an answer is considered mandatory. Validation applied
 * to the detail question's answer is again defined by the properties of the {@link com.ail.core.Attribute Attribute}
 * that it is bound to.</p>
 * @version 1.1  
 */
public class QuestionWithDetails extends Question {
    private static final long serialVersionUID = 7118438575837087257L;
    private String detailsTitle;
    private String detailsBinding;
    /** Hints to the UI rendering engine specifying details of how the details field should be rendered. The values supported
     * are specific to the type of attribute being rendered. */ 
    private String detailsRenderHint=null;
	private List<String> detailsEnabledFor;
    
    public QuestionWithDetails() {
		super();
    	detailsEnabledFor=new ArrayList<String>();
    	detailsEnabledFor.add("Yes");
	}

    /**
     * @see #setDetailsEnabledFor(String)
     * @return List of answers for which the details field should be enabled.
     */
    public String getDetailsEnabledFor() {
    	return convertListToCsv(detailsEnabledFor);
	}

    /**
     * Define the answers for which the details field should be enabled.   
     * @param detailsEnabledFor A comma separated list of answers for which the details field should be enabled.
     */
    public void setDetailsEnabledFor(String detailsEnabledFor) {
		this.detailsEnabledFor = convertCsvToList(detailsEnabledFor);
	}

	public String getDetailsBinding() {
        return detailsBinding;
    }

    public void setDetailsBinding(String detailsBinding) {
        this.detailsBinding = detailsBinding;
    }

    /**
     * The details title to be displayed with the answer. This method returns the raw title without
     * expanding embedded variables (i.e. xpath references like ${person/firstname}).
     * @see #getExpendedDetailsTitle(Type)
     * @return value of title
     */
   public String getDetailsTitle() {
        return detailsTitle;
    }

   /**
    * @see #getDetailsTitle()
    * @param detailsTitle
    */
   public void setDetailsTitle(String detailsTitle) {
        this.detailsTitle = detailsTitle;
    }

   /** 
    * Hints to the UI rendering engine specifying details of how the details field should be rendered. The values supported
    * are specific to the type of attribute being rendered.  
    * @return the renderHint
    */
   public String getDetailsRenderHint() {
		return detailsRenderHint;
	}

	public void setDetailsRenderHint(String detailsRenderHint) {
		this.detailsRenderHint = detailsRenderHint;
	}

    /**
     * Get the title with all variable references expanded. References are expanded with 
     * reference to the models passed in. Relative xpaths (i.e. those starting ./) are
     * expanded with respect to <i>local</i>, all others are expanded with respect to
     * <i>root</i>. 
     * @param local Model to expand local references (xpaths starting ./) with respect to.
     * @return Title with embedded references expanded
     * @since 1.1
     */
    public String getExpandedDetailsTitle(Type local) {
		return expand(getDetailsTitle(), QuotationContext.getQuotation(), local);
    }
    
    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        return applyRequestValues(request, response, model, "");
    }

    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
        model=super.applyRequestValues(request, response, model, rowContext);
        model=applyAttributeValues(model, getDetailsBinding(), rowContext, request);
        return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    boolean error=false;

	    // validate the yes/no part of the question
        error|=super.processValidations(request, response, model);

        // if the question's answer is one for which details are enabled, validate the details
        if (detailsEnabledFor.contains(model.xpathGet(getBinding()+"/value"))) {
            error|=applyAttributeValidation(model, getDetailsBinding());
        }

        return error;
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
        String detailTitle = i18n(getExpandedDetailsTitle(model));
        String questionId = xpathToId(rowContext+binding);
        String detailId = xpathToId(rowContext+detailsBinding);
        
        String styleClass = getStyleClass();
        String ref = getRef();
        
        PrintWriter w = response.getWriter();
        
        QuotationContext.getRenderer().renderQuestionWithDetails(w, request, response, model, this, title, detailTitle, rowContext, questionId, detailId, styleClass, ref);

        return model;
    }
}
