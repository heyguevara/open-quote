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

import static com.ail.openquote.ui.messages.I18N.i18n;
import static com.ail.openquote.ui.util.Functions.xpathToId;

import java.io.IOException;
import java.io.PrintWriter;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;

/**
 * <p>If the answer to a given question is yes, a number of subsequent questions are asked. This
 * page elements presents the user with a YesOrNo question. If Yes is selected, and panel of
 * subsequent questions is revealed.</p>
 * <p><img src="doc-files/QuestionWithSubSection-1.png"/></p>
 * <p>If the question is unanswered, or is answered "No" then the sub section remains hidden as
 * shown above. If "Yes" is selected, the sub section is revealed as below.</p>
 * <p><img src="doc-files/QuestionWithSubSection-2.png"/></p>
 * QuestionWithSubSection provides a more flexible alternative to {@see QuestionWithDetails QuestionWithDetails}.
 * It is more flexible in that any number of additional questions can be asked the sub section (including
 * {@see RowScroller} and {@see SectionScroller} elements); whereas QuestionWithDetails only supports a single
 * question.</p>
 * @see QuestionWithDetails
 * @see RowScroller
 * @see SectionScroller
 */
public class QuestionWithSubSection extends Question {
    private static final long serialVersionUID = 7118438575837087257L;
    
    /** PageElement to be rendered/filled if the Question is answered "Yes". */
    private PageElement subSection=null;
    
    public QuestionWithSubSection() {
        super();
        subSection=new PageSection();
    }

    /**
     * PageElement to be rendered/filled if the Question is answered "Yes".
     * @return sub section page element
     */
    public PageElement getSubSection() {
        return subSection;
    }

    /**
     * @see #getSubSection()
     * @param subSection sub section page element
     */
    public void setSubSection(PageElement subSection) {
        this.subSection = subSection;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        return applyRequestValues(request, response, model, "");
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
		model=super.processActions(request, response, model);
		model=subSection.processActions(request, response, model);
		return model;
    }

    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
        model=super.applyRequestValues(request, response, model, rowContext);
        model=subSection.applyRequestValues(request, response, model);
        return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    boolean error=false;

	    // validate the yes/no part of the question
        error|=super.processValidations(request, response, model);

        // If 'Yes' is selected, validate the subsection
        com.ail.core.Attribute attr=(com.ail.core.Attribute)model.xpathGet(getBinding());
        if (attr.isYesornoType() && "Yes".equals(attr.getValue())) {
            error|=subSection.processValidations(request, response, model);
        }
        
        return error;
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    renderResponse(request, response, model, "");
	    return model;
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
    	String onChange=null;
    	String aTitle = getExpandedTitle(model);
        PrintWriter w=response.getWriter();
        String questionId=xpathToId(rowContext+binding);

        if ("radio".equals(getRenderHint())) {
        	onChange="showHideDivDisplay(this.checked && this.value==\"Yes\", this.checked && this.value==\"No\", \""+id+"\")";
        }
        else if ("checkbox".equals(getRenderHint())) {
        	onChange="showHideDivDisplay(this.checked, !this.checked, \""+id+"\")";
        }
        else {
        	onChange="showHideDivDisplay(this.options[this.selectedIndex].text==\"Yes\", this.value!=\"Yes\", \""+id+"\")";
        }
        
        w.printf("<td>%s</td>", i18n(aTitle));
        w.printf("<td colspan='3'>%s</td>", renderAttribute(model, getBinding(), rowContext, onChange, getOnLoad()));
        w.printf("</tr>");
        w.printf("<tr><td colspan='4'>");
        w.printf(" <div id='%s' style='visibility:hidden;display:none'>", id);
        w.print("   <table width='90%'><tr><td width='5%'/><td>");
        model=subSection.renderResponse(request, response, model);
        w.printf("  </td></tr></table>");
        w.printf(" </div>");
        w.printf("</td>");

        // Disable the 'detail' area unless the question's answer is 'Yes'
        if ("radio".equals(getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
                    "radio=findElementsByName(\"%1$s\")[1];" +
                    "showHideDivDisplay(radio.checked, !radio.checked, \"%2$s\");"+
                    "</script>", questionId, id);
        }
        else if ("checkbox".equals(getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
                    "cbox=findElementsByName(\"%1$s\")[0];" +
                    "showHideDivDisplay(" +
                      "cbox.checked,"+
                      "!cbox.checked, "+
                      "\"%2$s\")</script>", questionId, id);
        }
        else {
            w.printf("<script type='text/javascript'>"+
                    "opt=findElementsByName(\"%1$s\")[0];" +
                    "showHideDivDisplay(" +
                      "opt.options[opt.selectedIndex].text==\"Yes\", "+
                      "opt.options[opt.selectedIndex].text==\"No\", "+
                      "\"%2$s\")</script>", questionId, id);
        }

        return model;
    }
}
