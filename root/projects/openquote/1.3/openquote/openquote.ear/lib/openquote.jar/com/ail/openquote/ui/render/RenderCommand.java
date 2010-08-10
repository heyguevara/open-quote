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
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.openquote.ui.PageElement;

/**
 * Generic command for all render services.
 */
public class RenderCommand extends Command implements RenderArg {
    private static final long serialVersionUID = 6020205183308695083L;
    private RenderArg args = null;

    public RenderCommand() {
        super();
        args = new RenderArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (RenderArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     */
    public String getRenderedOutputRet() {
		return args.getRenderedOutputRet();
	}

    /**
     * {@inheritDoc}
     */
	public void setRenderedOutputRet(String renderedOutputRet) {
		args.setRenderedOutputRet(renderedOutputRet);
	}

    /**
     * {@inheritDoc}
     */
	public RenderRequest getRequestArg() {
		return args.getRequestArg();
	}

    /**
     * {@inheritDoc}
     */
	public void setRequestArg(RenderRequest requestArg) {
		args.setRequestArg(requestArg);
	}

    /**
     * {@inheritDoc}
     */
	public RenderResponse getResponseArgRet() {
		return args.getResponseArgRet();
	}

    /**
     * {@inheritDoc}
     */
	public void setResponseArgRet(RenderResponse responseArgRet) {
		args.setResponseArgRet(responseArgRet);
	}

    /**
     * {@inheritDoc}
     */
	public Type getModelArg() {
		return args.getModelArg();
	}

    /**
     * {@inheritDoc}
     */
	public void setModelArg(Type modelArg) {
		args.setModelArg(modelArg);
	}

    /**
     * {@inheritDoc}
     */
	public PageElement getPageElementArg() {
		return args.getPageElementArg();
	}

    /**
     * {@inheritDoc}
     */
	public void setPageElementArg(PageElement pageElementArg) {
		args.setPageElementArg(pageElementArg);
	}
}
