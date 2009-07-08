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

package com.ail.core.product.resetproduct;

import com.ail.core.command.CommandArgImp;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/10/02 12:15:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/resetproduct/ResetProductArgImp.java,v $
 * @stereotype argimp
 */
public class ResetProductArgImp extends CommandArgImp implements ResetProductArg {
    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getProductNameArg() {
        return productNameArg;
    }

    /**
     * {@inheritDoc}
     * @param productNameArg @{inheritDoc}
     */
    public void setProductNameArg(String productNameArg) {
        this.productNameArg = productNameArg;
    }

    static final long serialVersionUID = 1199346453402049909L;

    /** The name of the product to be reset */
    private String productNameArg;

    /** Default constructor */
    public ResetProductArgImp() {
    }
}


