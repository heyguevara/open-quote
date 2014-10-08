/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

package com.ail.openquote.ui.render;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.core.command.CommandArgImp;
import com.ail.openquote.ui.PageElement;

/**
 */
public class RenderArgImp extends CommandArgImp implements RenderArg {
    static final long serialVersionUID = 1199346453402049909L;

	private String renderedOutputRet=null;
	private RenderRequest requestArg=null;
	private RenderResponse responseArgRet=null;
	private Type modelArg=null;
	private PageElement pageElementArg=null;
	
	/** Default constructor */
    public RenderArgImp() {
    }
    
    /**
     * {@inheritDoc}
     */
    public String getRenderedOutputRet() {
		return renderedOutputRet;
	}

    /**
     * {@inheritDoc}
     */
	public void setRenderedOutputRet(String renderedOutputRet) {
		this.renderedOutputRet = renderedOutputRet;
	}

    /**
     * {@inheritDoc}
     */
	public RenderRequest getRequestArg() {
		return requestArg;
	}

    /**
     * {@inheritDoc}
     */
	public void setRequestArg(RenderRequest requestArg) {
		this.requestArg = requestArg;
	}

    /**
     * {@inheritDoc}
     */
	public RenderResponse getResponseArgRet() {
		return responseArgRet;
	}

    /**
     * {@inheritDoc}
     */
	public void setResponseArgRet(RenderResponse responseArgRet) {
		this.responseArgRet = responseArgRet;
	}

    /**
     * {@inheritDoc}
     */
	public Type getModelArg() {
		return modelArg;
	}

    /**
     * {@inheritDoc}
     */
	public void setModelArg(Type modelArg) {
		this.modelArg = modelArg;
	}

    /**
     * {@inheritDoc}
     */
	public PageElement getPageElementArg() {
		return pageElementArg;
	}

    /**
     * {@inheritDoc}
     */
	public void setPageElementArg(PageElement pageElementArg) {
		this.pageElementArg = pageElementArg;
	}
}


