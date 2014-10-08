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
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Attribute;
import com.ail.core.Type;

/**
 * <p>An Answer simply displays the answer given to a previous asked question. An {@link AnswerSection} is used to 
 * group a number of Answers together. These elements are generally used as part of a summary screen.</p>
 * <p><img src="doc-files/Answer.png"/></p>
 * <p>The screenshot shows an Answer in the context of an {@link AnswerSection} which contains five Answers in all.</p>
 * <p>An Answer displays a title an answer. The title may be defined statically, using the {@link #getTitle() title} 
 * property; or dynamically using the {@link #getTitleBinding() titleBinding} property. The XPath expression defined 
 * by the {@link #getTitleBinding() titleBinding} property is evaluated against the quotation object at page render 
 * time. The answer itself is the result of evaluating the XPath expression defined by {@link PageElement#getBinding()
 * binding} against the quotation object.</p>
 * @see AnswerScroller
 */
public class Answer extends PageElement {
    private static final long serialVersionUID = -1048535311696230109L;
    private static SimpleDateFormat dateFormat=new SimpleDateFormat("d MMMMM, yyyy");
    
    /** The fixed title to be displayed with the answer */
    private String title;

    /** 
     * An optional XPath value which can be evaluated against the quotation to fetch a dynamic title.
     * If defined this is used in preference to the fixed title: @{link #title} 
     */
    private String titleBinding;
    
    public Answer() {
    }
    
    /**
     * The fixed title to be displayed with the answer.
     * @return value of title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @see #getTitle()
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * An optional XPath value which can be evaluated against the quotation to fetch a dynamic title.
     * If defined this is used in preference to the fixed title: @{link #title} 
     * @return title binding XPath
     */
    public String getTitleBinding() {
        return titleBinding;
    }

    /**
     * @see #getTitleBinding()
     * @param titleBinding
     */
    public void setTitleBinding(String titleBinding) {
        this.titleBinding = titleBinding;
    }

    private String formattedAnswer(Object answer) {
        if (answer instanceof String) {
            return (String)answer;
        }
        else if (answer instanceof Date) {
            return dateFormat.format((Date)answer);
        }
        else if (answer instanceof com.ail.core.Attribute) {
            return ((Attribute)answer).getFormattedValue();
        }

        return "Don't know how to format a '"+(answer==null ? "null" : answer.getClass().getName())+"'";
    }

    @Override
    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w = response.getWriter();
        
        String aTitle=(titleBinding!=null) ? (String)model.xpathGet(titleBinding) : title;

        w.printf("<tr><td>%s</td><td>%s</td></tr>", aTitle, formattedAnswer(model.xpathGet(binding)));
    }

}
