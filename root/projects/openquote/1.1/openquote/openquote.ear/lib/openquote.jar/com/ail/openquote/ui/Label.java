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
import java.util.ArrayList;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.Binding;

/**
 * <p>A Label renders as fixed text on the page. It may be built up, in part or entirely, from dynamic information collected
 * from the {@link com.ail.openquote.Quotation Quotation} being processed.</p>
 * <p>The screenshot below shows a section title inside a {@link SectionScroller}. A {@link SectionScroller#getSectionTitle() section title}
 * is itself an instance of Label.</p>
 * <p><img src="doc-files/Label.png"/></p>
 * <p>A Label has a {@link #getFormat() format} and zero or more {@link #getParameter() parameters}. The format is taken to be
 * a printf style format as described <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Formatter.html#syntax">here</a>
 * The parameters supply data for the arguments referenced in the format string. Each parameter is an XPath expression to be 
 * evaluated against the current binding context</p>
 * <p>In the example above, format is set to "Details for %s %s %s", as this is a SectionScroller bound to 'asset[assetTypeId='Vehicle']',
 * the context used to evaluate the parameters is an asset; three parameters are supplied: "attribute[id='make']/value", 
 * "attribute[id='model']/value" and "attribute[id='registration']/value".</p>
 */
public class Label extends PageElement {
    private static final long serialVersionUID = 1L;

    /** Printf style format string. */
    private String format;

    /** XPath bindings to supply data for the arguments referenced in {@link #format} */
    private ArrayList<Binding> parameter;

    public Label() {
        super();
        parameter = new ArrayList<Binding>();
    }

    /**
     * Get the list of parameters supplied to support the arguments referenced in {@link #getFormat() format}. Each
     * parameter takes the form of an XPath expression which will be evaluated against the current context (the
     * binding of the PageElement which contains the Label).
     * @return Parameter bindings.
     */
    public ArrayList<Binding> getParameter() {
        return parameter;
    }

    /**
     * @see #getParameter()
     * @param parameter Parameter bindings
     */
    public void setParameter(ArrayList<Binding> parameter) {
        this.parameter = parameter;
    }

    /**
     * A format string in the printf style. See <a href="http://java.sun.com/j2se/1.5.0/docs/api/java/util/Formatter.html#syntax">here</a>
     * for a description of this format.
     * @return Format string
     */
    public String getFormat() {
        return format;
    }

    /**
     * @see #getFormat()
     * @param format Format string
     */
    public void setFormat(String format) {
        this.format = format;
    }

    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        // nothing to do here.
    }

    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        // nothing to do here.
        return false;
    }

    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException,
            IOException {
        Object[] params = new Object[parameter.size()];
        int i = 0;

        for (Binding expr : parameter) {
            params[i++] = model.xpathGet(expr.getXpath());
        }

        response.getWriter().printf(format, params);
    }
}
