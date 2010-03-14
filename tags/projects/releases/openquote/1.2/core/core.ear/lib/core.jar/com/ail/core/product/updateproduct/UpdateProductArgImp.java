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

package com.ail.core.product.updateproduct;

import com.ail.core.command.CommandArgImp;
import com.ail.core.product.ProductDetails;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$s
 * @date $Date$
 * @source $Source$
 * @stereotype argimp
 */
public class UpdateProductArgImp extends CommandArgImp implements UpdateProductArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** Details of the product to be update */
    private ProductDetails productDetailsArg;

    /** The name of the product to be updated */
    private String productNameArg;
    
    /** Default constructor */
    public UpdateProductArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public ProductDetails getProductDetailsArg() {
        return productDetailsArg;
    }

    /**
     * {@inheritDoc}
     * @param productsRet @{inheritDoc}
     */
    public void setProductDetailsArg(ProductDetails productDetailsArg) {
        this.productDetailsArg = productDetailsArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getProductNameArg() {
        return this.productNameArg;
    }

    /**
     * {@inheritDoc}
     * @param productNameArg @{inheritDoc}
     */
    public void setProductNameArg(String productNameArg) {
        this.productNameArg=productNameArg;
    }
}


