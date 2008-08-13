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

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.Functions;

/**
 * <p>An AttributeField represents an individual column within a {@link RowScroller}. The RowScroller itself
 * iterates over a collection of results rendering one row on the screen for each row of data in the 
 * collection. The columns in each row are represented by AttributeFields which are bound to 
 * {@link com.ail.core.Attribute Attributes} in the quotation.</p>
 * <p><img src="doc-files/AttributeField.png"/></p>
 * <p>In the example above the RowScroller is bound to a collection of vehicle assets. The current quote contains
 * two assets matching the criteria - an Alfa Romeo and a Bentley. The RowScroller contains nine AttributeFields
 * each of which is bound to a different Attribute in the assets. The AttributeField bindings are taken to be relative to
 * the binding of the RowScroller. In this case the RowScroller is bound to <code>asset[assetTypeId='Vehicle']</code>,
 * and the individual AttributeFields are bound to <code>attribute[id='Make']</code>, <code>attribute[id='Model']</code>,
 * etc</p>
 * <p>The Attribute which the AttributeField is bound to dictates the HTML form elements used to represent it, and
 * the validations that will be applied to it. For example, a "choice" attribute is rendered as a drop down list; a
 * "string" is rendered as a right justified text field.</p>
 * <p>Each AttributeField defines it's column {@link #getTitle() title}, and an optional {@link #getSubTitle() subTitle}
 * which can be used when the titles are fixed. Dynamic titles - the text of which is picked up from another part of
 * the quote - can be defined using {@link #getTitleBinding() titleBinding} and {@link #getSubTitleBinding() subTitleBinding}. 
 * It may also define javascript to be executed either onLoad (when the page is loaded); or onChange (when a fields 
 * value is changed).
 * @see RowScroller
 * @see com.ail.core.Attribute
 */
public class AttributeField extends PageElement {
    private static final long serialVersionUID = 7118438575837087257L;

    /** The fixed title to be displayed with the answer */
    private String title;

    /** The fixed subtitle to be displayed with the answer */
    private String subTitle;

    /** A dynamic title taken from some other part of the quote instance to be displayed with the answer */
    private String titleBinding;

    /** The dynamic subtitle taken from some other part of the quote instance to be displayed with the answer */
    private String subTitleBinding;

    /** Javascript to be executed when the page loads */
    private String onLoad;

    /** Javascript to be executed when a field's value is changed */
    private String onChange;

    public AttributeField() {
		super();
	}

    /** The fixed text sub title to be displayed with the answer.  
     * @return sub title text if defined, or null otherwise.
     */
    public String getSubTitle() {
        return subTitle;
    }

    /**
     * @see #getSubTitle()
     * @param subTitle
     */
    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    /** The fixed text title to be displayed with the answer.  
     * @return title text if defined.
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
     * Javascript to be executed when a field's value is changed
     * @return java script
     */
    public String getOnChange() {
        return onChange;
    }

    /**
     * @see #getOnChange()
     * @param onChange
     */
    public void setOnChange(String onChange) {
        this.onChange = onChange;
    }

    /**
     * Javascript to be executed when the page loads. 
     * @return java script
     */
    public String getOnLoad() {
        return onLoad;
    }

    /**
     * @see #getOnLoad()
     * @param onLoad
     */
    public void setOnLoad(String onLoad) {
        this.onLoad = onLoad;
    }

    @Override
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        applyRequestValues(request, response, model, "");
    }

    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
        Functions.applyAttributeValues(model, getBinding(), rowContext, request);
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return Functions.applyAttributeValidation(model, getBinding());
    }

    @Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    renderResponse(request, response, model, "");
	}

    public void renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        w.print(Functions.renderAttribute(model, getBinding(), rowContext, getOnChange(), getOnLoad()));
    }

    @Override
    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        renderPageLevelResponse(request, response, model, "");
    }

    public void renderPageLevelResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        w.print(Functions.renderAttributePageLevel(model, getBinding(), rowContext, request.getPortletSession()));
    }

    /**
     * @return the titleBinding
     */
    public String getTitleBinding() {
        return titleBinding;
    }

    /**
     * @param titleBinding the titleBinding to set
     */
    public void setTitleBinding(String titleBinding) {
        this.titleBinding = titleBinding;
    }

    /**
     * @return the subTitleBinding
     */
    public String getSubTitleBinding() {
        return subTitleBinding;
    }

    /**
     * @param subTitleBinding the subTitleBinding to set
     */
    public void setSubTitleBinding(String subTitleBinding) {
        this.subTitleBinding = subTitleBinding;
    }
}
