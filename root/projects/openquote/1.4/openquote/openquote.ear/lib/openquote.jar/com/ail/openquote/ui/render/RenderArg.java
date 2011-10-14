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
import com.ail.core.command.CommandArg;
import com.ail.openquote.ui.PageElement;

/**
 * Generic service argument defining the interface to all render services. The actual requirements
 * of render services for each type of widget vary in the arguments that they need. Having this
 * one Arg for all render services has the advantage that we have to maintain far fewer classes, 
 * but the disadvantage that it isn't clear which arguments have to be populated for a given
 * widget and which do not. However, all render services will require a that the {@link #getModelArg() Model}
 * argument is populated; and all will populate the {@link #getRenderedOutputRet RenderedOutputRet} with 
 * the rendered results.
 */
public interface RenderArg extends CommandArg {
	/**
	 * String returning the rendered output.
	 * Typically, render services generate some output whether in the form of
	 * HTML or XForms. The services should write this output into this 
	 * String property.
	 * @return String to which output should be written.
	 */
	String getRenderedOutputRet();

	/**
	 * @see #getRenderedOutputRet()
	 * @param renderedOutputRet
	 */
	void setRenderedOutputRet(String renderedOutputRet);
	
	/**
	 * The portal request associated with this invocation. Render services are invoked
	 * as a part of a portal request/response process. This service may refer to this
	 * property to fetch request specific information.
	 * @return The request that this service is being invoked for.
	 */
	RenderRequest getRequestArg();

	/**
	 * @see #getRequestArg()
	 * @param requestArg
	 */
	void setRequestArg(RenderRequest requestArg);

	/**
	 * The portal response associated with this invocation. Render services are invoked
	 * as a part of a portal request/response process. This service may refer to this
	 * property to fetch response specific information.
	 * @return The response that this service is being invoked for.
	 */
	RenderResponse getResponseArgRet();

	/**
	 * @see #getResponseArgRet()
	 * @param responseArgRet
	 */
	void setResponseArgRet(RenderResponse responseRet);

	/**
	 * The model represents the data which the request to render is being made. It is the
	 * M in MVC.
	 * @return The model being rendered
	 */
	Type getModelArg();

	/**
	 * @see #getModelArg()
	 * @param modelArg
	 */
	void setModelArg(Type modelArg);

	/**
	 * The page element or widget being rendered. This represents the V in MVC and defines
	 * how the model needs to be rendered.
	 * @return page element being rendered
	 */
	PageElement getPageElementArg();

	/**
	 * @see #getPageElementArg()
	 * @param pageElementArg
	 */
	void setPageElementArg(PageElement pageElementArg);
}


