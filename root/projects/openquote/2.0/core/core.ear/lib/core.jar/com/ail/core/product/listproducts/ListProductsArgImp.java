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

package com.ail.core.product.listproducts;

import com.ail.core.command.CommandArgImp;
import com.ail.core.product.ProductDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @version $Revision: 1.4 $
 * @state $State: Exp $s
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/listproducts/ListProductsArgImp.java,v $
 * @stereotype argimp
 */
public class ListProductsArgImp extends CommandArgImp implements ListProductsArg {
    static final long serialVersionUID = 1199346453402049909L;

    /** A collection of strings representing the names of all the products know to the system */
    private Collection<ProductDetails> productsRet=new ArrayList<ProductDetails>();

    /** Default constructor */
    public ListProductsArgImp() {
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Collection<ProductDetails> getProductsRet() {
        return productsRet;
    }

    /**
     * {@inheritDoc}
     * @param productsRet @{inheritDoc}
     */
    public void setProductsRet(Collection<ProductDetails> productsRet) {
        this.productsRet = productsRet;
    }
}


