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

package com.ail.core.product.newproducttype;

import com.ail.core.command.CommandArgImp;
import com.ail.core.Type;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/10/02 12:15:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/newproducttype/NewProductTypeArgImp.java,v $
 * @stereotype argimp
 */
public class NewProductTypeArgImp extends CommandArgImp implements NewProductTypeArg {
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

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getTypeNameArg() {
        return typeNameArg;
    }

    /**
     * {@inheritDoc}
     * @param typeNameArg @{inheritDoc}
     */
    public void setTypeNameArg(String typeNameArg) {
        this.typeNameArg = typeNameArg;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Type getTypeRet() {
        return typeRet;
    }

    /**
     * {@inheritDoc}
     * @param typeRet @{inheritDoc}
     */
    public void setTypeRet(Type typeRet) {
        this.typeRet = typeRet;
    }

    static final long serialVersionUID = 1199346453402049909L;

    /** The name of the product to instantiate the type for. */
    private String productNameArg;

    /** The name of the type to be instantiated, or null if the products default type is required. */
    private String typeNameArg;

    /** The instantiated type */
    private Type typeRet;

    /** Default constructor */
    public NewProductTypeArgImp() {
    }
}


