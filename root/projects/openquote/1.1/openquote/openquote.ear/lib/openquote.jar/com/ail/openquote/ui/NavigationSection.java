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

/**
 * <p>A navigation section acts as a container for buttons like 'Next' and 'Previous' which are used to navigate through the
 * pageflow. By default navigation sections always contain a 'Quit' button - though this may be turned off
 * by setting the {@link #isQuitDisabled() quitDisabled} property to true.</p>
 * <p><img src="doc-files/NavigationSection.png"/></p>
 * <p>The screenshot shows a NavigationSection containing two {@link CommandButtonAction CommandButtonActions} (Previous and Next),
 * and in this case the quit button has been left enabled, which is the default.</p>
 * @see QuitButtonAction
 * @see CommandButtonAction
 */
public class NavigationSection extends PageContainer {
	private static final long serialVersionUID = 6794522768423045427L;
	boolean quitDisabled=false;
    private QuitButtonAction quitButton=new QuitButtonAction();
    
    /**
     * By default all NavigationSections include a Quit button. If this property is set to 
     * true the quit button will not be included. Note: The quit button is an instance of
     * {@link QuitButtonAction}.
     * @return True if the button is disabled, false otherwise.
     */
    public boolean isQuitDisabled() {
        return quitDisabled;
    }

    /**
     * @see #isQuitDisabled()
     * @param quitDisabled True if the button is to be disabled, false otherwise.
     */
    public void setQuitDisabled(boolean quitDisabled) {
        this.quitDisabled = quitDisabled;
    }

    /**
     * By default the Quit button is an instance of {@link QuitButtonAction} but this may be
     * overridden using this property.
     * @return Quit action
     */
    public QuitButtonAction getQuitButton() {
        return quitButton;
    }

    /**
     * @see #getQuitButton()
     * @param quitButton Quit action
     */
    public void setQuitButton(QuitButtonAction quitButton) {
        this.quitButton = quitButton;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        if (!isQuitDisabled()) {
            model=quitButton.processActions(request, response, model);
        }
        
        for(PageElement element: getPageElement()) {
            model=element.processActions(request, response, model);
        }
        
        return model;
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();

        w.print("<table width='100%' border='0' align='center'><tr>");
        w.print("<td width='15%'>&nbsp;</td>");
        w.print("<td width='70%' align='center'>");
        for(PageElement element: getPageElement()) {
			model=element.renderResponse(request, response, model);
		}
		w.print("</td>");
        w.print("<td width='15%' align='right'>");
        if (isQuitDisabled()) {
            w.print("&nbsp;");
        }
        else {
            model=quitButton.renderResponse(request, response, model);
        }
        w.print("</td></tr></table>");
        
        return model;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        // Nothing to do here
    	return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        // Nothing to do heres
        return false;
    }
}
