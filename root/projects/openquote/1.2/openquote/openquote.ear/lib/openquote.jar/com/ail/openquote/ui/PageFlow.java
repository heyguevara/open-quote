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
import com.ail.openquote.Quotation;

/**
 * The PageFlow class is the root of the openquote product UI concept. It contains
 * a collection of pages and implements life-cycle methods which simply delegate to
 * the "current" page.<br/>
 * In all cases the page flow assumes that the model it is processing is a {@link com.ail.openquote.Quotation Quotation}.
 * The name of the "current" page is indicated by the Quotation's {@link com.ail.openquote.Quotation#getPage() page} 
 * property.
 * @see com.ail.openquote.Quotation
 * @see com.ail.openquote.portlet.QuotationPortlet
 * @see com.ail.openquote.portlet.SandpitPortlet
 */
public class PageFlow extends PageElement {
	private static final long serialVersionUID = -3699440857377974385L;

    /** List of pages which are part of this flow */
    private ArrayList<Page> page;
    
    /** Optional definition of the page on which the quote process should start. */
    private String startPage=null;
    
    /** Set true when elements IDs have been applied so we can avoid applying them twice. */
    private transient boolean appliedElementId=false;

	/**
	 * Default constructor
	 */
	public PageFlow() {
		super();
		page=new ArrayList<Page>();
	}

    /**
     * The list of pages associated with this PageFlow
     * @return list of pages
     */
    public ArrayList<Page> getPage() {
        return page;
	}

	/**
     * @see #getPage()
	 * @param page List of pages
	 */
	public void setPage(ArrayList<Page> page) {
		this.page = page;
	}

	/**
	 * Get the name of the page on which the quote process should start. This can also
	 * be defined in the Quotation's XML. If defined in both places, the setting here 
	 * takes precedence. 
	 * @return Name of the page to start the process on.
	 */
	public String getStartPage() {
		return startPage;
	}

	/**
	 * @see #getStartPage()
	 * @param startPage
	 */
	public void setStartPage(String startPage) {
		this.startPage = startPage;
	}

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
		for(Page p: page) {
			if (p.getId().equals(((Quotation)model).getPage())) {
	            model=p.applyRequestValues(request, response, model);
                break;
			}
		}
		return model;
	}

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        boolean errors=false;
        
		for(Page p: page) {
			if (p.getId().equals(((Quotation)model).getPage())) {
				errors |= p.processValidations(request, response, model);
				break;
            }
		}

		return errors;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
		for(Page p: page) {
			if (p.getId().equals(((Quotation)model).getPage())) {
				model=p.processActions(request, response, model);
                 break;
			}
		}
		return model;
	}

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
		for(Page p: page) {
			if (p.getId().equals(((Quotation)model).getPage())) {
				model=p.renderResponse(request, response, model);
                 break;
			}
		}
		return model;
	}

	@Override
	public void applyElementId(String baseId) {
		if (!appliedElementId) {
			int idx=-0;
			for(Page p: page) {
				p.applyElementId(baseId+"."+(idx++));
			}
			appliedElementId=true;
		}
	}
}
