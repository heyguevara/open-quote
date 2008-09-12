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
 * @version 1.1
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

    /**
     * The fixed sub title to be displayed with the answer. This method returns the raw sub title without
     * expanding embedded variables (i.e. xpath references like ${person/firstname}).
     * @see #getExpendedSubTitle(Type)
     * @return value of title
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

    /**
     * Get the sub title with all variable references expanded. References are expanded with 
     * reference to the models passed in. Relative xpaths (i.e. those starting ./) are
     * expanded with respect to <i>local</i>, all others are expanded with respect to
     * <i>root</i>. 
     * @param root Model to expand references with respect to.
     * @param local Model to expand local references (xpaths starting ./) with respect to.
     * @return Title with embedded references expanded
     * @since 1.1
     */
    public String getExpandedSubTitle(Type root, Type local) {
    	if (getTitle()!=null) {
    		return expand(getSubTitle(), root, local);
    	}
    	// TODO Check getTitleBinding for backward compatibility only - remove for OQ2.0
    	else if (getSubTitleBinding()!=null) {
    		return local.xpathGet(getSubTitleBinding(), String.class);
    	}
    	else {
    		return null;
    	}
    }
    
    /**
     * The fixed title to be displayed with the answer. This method returns the raw title without
     * expanding embedded variables (i.e. xpath references like ${person/firstname}).
     * @see #getExpandedTitle(Type)
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
    	// TODO Check getTitleBinding for backward compatibility only - remove for OQ2.0
    	else if (getTitleBinding()!=null) {
    		return local.xpathGet(getTitleBinding(), String.class);
    	}
    	else {
    		return null;
    	}
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
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        return applyRequestValues(request, response, model, "");
    }

    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model, String rowContext) {
        return Functions.applyAttributeValues(model, getBinding(), rowContext, request);
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return Functions.applyAttributeValidation(model, getBinding());
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    return renderResponse(request, response, model, "");
	}

    public Type renderResponse(RenderRequest request, RenderResponse response, Type model, String rowContext) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        
        w.print(Functions.renderAttribute(model, getBinding(), rowContext, getOnChange(), getOnLoad()));
        
        return model;
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
     * @deprecated Use {@link #getExpandedTitle(Type)} in combination with embedded xpath references within the title.
     */
    public String getTitleBinding() {
        return titleBinding;
    }

    /**
     * @param titleBinding the titleBinding to set
     * @deprecated Use {@link #getExpandedTitle(Type)} in combination with embedded xpath references within the title.
     */
    public void setTitleBinding(String titleBinding) {
        this.titleBinding = titleBinding;
    }

    /**
     * @return the subTitleBinding
     * @deprecated Use {@link #getExpendedSubTitle(Type)} in combination with embedded xpath references within the title.
     */
    public String getSubTitleBinding() {
        return subTitleBinding;
    }

    /**
     * @param subTitleBinding the subTitleBinding to set
     * @deprecated Use {@link #getExpendedSubTitle(Type)} in combination with embedded xpath references within the title.
     */
    public void setSubTitleBinding(String subTitleBinding) {
        this.subTitleBinding = subTitleBinding;
    }
}
