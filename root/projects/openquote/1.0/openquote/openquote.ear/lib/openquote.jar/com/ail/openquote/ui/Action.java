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

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.core.product.executepageaction.ExecutePageActionCommand;
import com.ail.openquote.Quotation;

/**
 * Actions allow arbitrary commands to be invoked during a page flow. A number of page elements (e.g. {@link Page} 
 * and {@link CommandButtonAction}) allow Actions to be associated with them and will invoke the commands they define
 * in response to events.</p>
 * For example, any number of Actions can be attached to a CommandButtonAction. When the button is selected all the 
 * commands associated with the actions are executed (see {@link #condition}). 
 */
public class Action extends PageElement {
	private static final long serialVersionUID = -1320299722728499324L;
	/** 
     * The point during the page processing cycle when the action be fired. Must be one of: 
     * "onApplyRequestValues", "onProcessActions", "onProcessValidations", "onRenderResponse"
     * @see PageElement for details of the page processing cycle.
     */
    private String when;

    /** 
     * The name of the command to be invoked. This command must be a service which implements 
     * {@link com.ail.core.product.executepageaction.ExecutePageActionCommand ExecutePageActionCommand}.
     */
    private String commandName;
    
    /**
     * An optional XPath expression. If defined the expression is evaluated against the quotation
     * immediately before the action is to be executed. The action will only be executed if the expression 
     * returns true (i.e. <code>(Boolean)model.xpathGet(condition)==true</code> 
     */
    private String condition;
    
	public Action() {
		super();
	}

	public Action(String when, String commandName, String condition) {
        super();
        this.when = when;
        this.commandName = commandName;
        this.condition = condition;
    }

    protected boolean conditionIsMet(Type model) {
        return condition==null || (Boolean)model.xpathGet(condition)==true;
    }
    
    @Override
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        if ("onProcessActions".equals(when) && conditionIsMet(model)) {
            executeAction(model);
        }
    }

    @Override
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        if ("onApplyRequestValues".equals(when) && conditionIsMet(model)) {
            executeAction(model);
        }
	}

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        if ("onProcessValidations".equals(when) && conditionIsMet(model)) {
            executeAction(model);
        }

        return false;
	}

	@Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {	    
	    if ("onRenderResponse".equals(when)) {
	        executeAction(model);
        }
    }

    /**
     * get the action's condition
     * @see #condition
     * @return current value of property
     */
    public String getCondition() {
        return condition;
    }

    /**
     * Set the action's condition.
     * @see #condition
     * @param condition
     */
    public void setCondition(String condition) {
        this.condition = condition;
    }

    /**
     * Get the action's command
     * @see #commandName
     * @return The action's command name
     */
    public String getCommandName() {
        return commandName;
    }

    /**
     * Set the actions command name
     * @see #commandName
     * @param commandName
     */
    public void setCommandName(String commandName) {
        this.commandName = commandName;
    }

    /**
     * Get the action's when value
     * @see #when
     * @return The action's when value
     */
    public String getWhen() {
        return when;
    }

    /**
     * Set the action's when value
     * @see #when
     * @param when When value
     */
    public void setWhen(String when) {
        this.when = when;
    }

    private void executeAction(Type model) {
        Quotation quote=(Quotation)model;
        
        CoreProxy cp=new CoreProxy(productNameToConfigurationNamespace(quote.getProductTypeId()));
        ExecutePageActionCommand c=(ExecutePageActionCommand)cp.newCommand("ExecutePageAction");
        c.setQuotationArgRet(quote);
        c.setServiceNameArg(commandName);

        try {
            c.invoke();
        }
        catch (Throwable e) {
            cp.logError("Error executing Action '"+getCommandName());
            e.printStackTrace();
        }
    }
}
