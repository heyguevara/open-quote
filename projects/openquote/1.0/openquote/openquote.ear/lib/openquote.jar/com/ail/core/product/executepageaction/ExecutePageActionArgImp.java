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

import com.ail.core.command.CommandArgImp;
import com.ail.openquote.Quotation;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/01/30 21:38:26 $
 * @source $Source: /home/bob/CVSRepository/projects/openquote/openquote.ear/openquote.jar/com/ail/core/product/executepageaction/ExecutePageActionArgImp.java,v $
 * @stereotype argimp
 */
public class ExecutePageActionArgImp extends CommandArgImp implements ExecutePageActionArg {
    static final long serialVersionUID = 1199346453402049909L;

    private Quotation quotationArgRet;
    private String serviceNameArg;
    
    /** Default constructor */
    public ExecutePageActionArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Quotation getQuotationArgRet() {
        return quotationArgRet;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public void setQuotationArgRet(Quotation quotation) {
        quotationArgRet=quotation;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getServiceNameArg() {
        return serviceNameArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public void setServiceNameArg(String serviceNameArg) {
        this.serviceNameArg=serviceNameArg;
    }
}


