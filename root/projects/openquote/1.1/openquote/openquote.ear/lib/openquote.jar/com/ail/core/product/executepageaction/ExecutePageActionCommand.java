/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.openquote.Quotation;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/01/30 21:38:26 $
 * @source $Source: /home/bob/CVSRepository/projects/openquote/openquote.ear/openquote.jar/com/ail/core/product/executepageaction/ExecutePageActionCommand.java,v $
 * @stereotype command
 */
public class ExecutePageActionCommand extends Command implements ExecutePageActionArg {
    private static final long serialVersionUID = -2065724981616174435L;
    private ExecutePageActionArg args = null;

    public ExecutePageActionCommand() {
        super();
        args = new ExecutePageActionArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (ExecutePageActionArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Quotation getQuotationArgRet() {
        return args.getQuotationArgRet();
    }

    /**
     * {@inheritDoc}
     */
    public void setQuotationArgRet(Quotation quotationArgRet) {
        args.setQuotationArgRet(quotationArgRet);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getServiceNameArg() {
        return args.getServiceNameArg();
    }

    /**
     * {@inheritDoc}
     */
    public void setServiceNameArg(String serviceNameArg) {
        args.setServiceNameArg(serviceNameArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public PortletSession getPortletSessionArg() {
		return args.getPortletSessionArg();
	}

    /**
     * {@inheritDoc}
     */
	public void setPortletSessionArg(PortletSession portletSession) {
		args.setPortletSessionArg(portletSession);
	}

	public boolean getValidationFailedRet() {
		return args.getValidationFailedRet();
	}

	public void setValidationFailedRet(boolean validationFailed) {
		args.setValidationFailedRet(validationFailed);
	}
}
