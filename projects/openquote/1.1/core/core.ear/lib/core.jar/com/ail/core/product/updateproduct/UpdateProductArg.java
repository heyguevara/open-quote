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

import com.ail.core.command.CommandArg;
import com.ail.core.product.ProductDetails;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype arg
 */
public interface UpdateProductArg extends CommandArg {
    /**
     * Details of the product to be removed from the registry.
     * @return Value of productDetaisArg, or null if it is unset
     */
    ProductDetails getProductDetailsArg();

    /**
     * Details of the product to be removed from the registry
     * @see #getProductDetailsArg
     * @param productsDetailsArg new value for property.
     */
    void setProductDetailsArg(ProductDetails productsDetailsArg);
    
    /**
     * The name of the product to be updated.
     * @return The name of the product to be updated.
     */
    String getProductNameArg();
    
    /**
     * The name of the product to be updated.
     * @param productName The name of the product to update 
     */
    void setProductNameArg(String productName);
}


