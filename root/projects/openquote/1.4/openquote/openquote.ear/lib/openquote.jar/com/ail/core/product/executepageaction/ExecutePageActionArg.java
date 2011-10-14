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

package com.ail.core.product.executepageaction;

import javax.portlet.PortletSession;

import com.ail.core.command.CommandArg;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.Action;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/01/30 21:38:26 $
 * @source $Source: /home/bob/CVSRepository/projects/openquote/openquote.ear/openquote.jar/com/ail/core/product/executepageaction/ExecutePageActionArg.java,v $
 * @stereotype arg
 */
public interface ExecutePageActionArg extends CommandArg {
    /**
     * The quotation property holds the Quotation which will be passed
     * into the service.
     * @return Quotation the service will process
     */
    Quotation getQuotationArgRet();

    /**
     * @see #getQuotationArgRet()
     */
    void setQuotationArgRet(Quotation quotationArgRet);

    /**
     * The portlet session associated with the action.
     * @return Action's portlet session
     */
    PortletSession getPortletSessionArg();
    
    /**
     * @see #getPortletSessionArg()
     */
    void setPortletSessionArg(PortletSession portletSession);
    
    /** 
     * The service name property specifies the name of the product service to
     * be executed.
     * @return Name of the service to be executed
     */
    String getServiceNameArg();
    
    /**
     * @see #getServiceNameArg()
     *
     */
    void setServiceNameArg(String serviceNameArg);
    
    /**
     * If the service is performing a validation and the validation fails, it should set this 
     * property to true. For the most part this is only used by commands concerned with cross
     * field validations.
     * @return true if validation has failed, false otherwise
     */
    boolean getValidationFailedRet();
    
    /**
     * @see #setValidationFailedRet(boolean)
     * @param validationFailed
     */
    void setValidationFailedRet(boolean validationFailed);

	void setActionArg(Action action);

	Action getActionArg();
}


