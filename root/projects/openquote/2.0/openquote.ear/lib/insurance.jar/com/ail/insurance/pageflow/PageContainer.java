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
package com.ail.insurance.pageflow;

import java.util.Collection;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import com.ail.core.Type;
import com.ail.insurance.pageflow.util.OrderedLinkedList;

/**
 * An abstract UI element which provides support for any concrete page element which itself contains other page
 * elements.
 */
public abstract class PageContainer extends PageElement {
    private static final long serialVersionUID = 297215265083279666L;

    /** List of elements contained. */
    private List<PageElement> pageElement;

    public PageContainer() {
        super();
        pageElement = new OrderedLinkedList<PageElement>();
    }

    /**
     * Get the list of elements contained.
     * @return List of elements.
     */
    public List<PageElement> getPageElement() {
        return pageElement;
    }

    /**
     * Set the list of page elements contained.
     * @param pageElement list of elements
     */
    public void setPageElement(List<PageElement> pageElement) {
         this.pageElement=pageElement;
    }

    /**
     * Get the list of elements as a collection (this is provided to support Castor).
     * @return Collection of elements.
     */
    public Collection<PageElement> getPageElementCollection() {
        return pageElement;
    }

    /**
     * @see #getPageElementCollection()
     * @param pageElements Collection of elements
     */
    public void setPageElementCollection(Collection<PageElement> pageElements) {
        this.pageElement = new OrderedLinkedList<PageElement>(pageElements);
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        for (PageElement e : pageElement) {
            model=e.applyRequestValues(request, response, model);
        }
        return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
		// If our condition isn't met, validate nothing.
	    if (!conditionIsMet(model)) {
    		return false;
    	}

	    boolean result = false;

        for (PageElement e : getPageElement()) {
            result |= e.processValidations(request, response, model);
        }

        return result;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        for (PageElement e : getPageElement()) {
            model=e.processActions(request, response, model);
        }
        
        return model;
    }

    @Override
    public void applyElementId(String basedId) {
    	int idx=0;
    	for(PageElement e: pageElement) {
    		e.applyElementId(basedId+ID_SEPARATOR+(idx++));
    	}
    	super.applyElementId(basedId);
   	}
}
