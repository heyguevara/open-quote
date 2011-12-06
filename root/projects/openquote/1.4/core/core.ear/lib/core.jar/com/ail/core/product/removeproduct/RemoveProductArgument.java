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

package com.ail.core.product.removeproduct;

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;
import com.ail.core.product.ProductDetails;

@ArgumentDefinition
public interface RemoveProductArgument extends Argument {
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
}


