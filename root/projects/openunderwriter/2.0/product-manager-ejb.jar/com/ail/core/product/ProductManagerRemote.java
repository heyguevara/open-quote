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

package com.ail.core.product;

import javax.ejb.Remote;

import com.ail.core.product.ListProductsService.ListProductsArgument;
import com.ail.core.product.NewProductTypeService.NewProductTypeArgument;
import com.ail.core.product.RegisterProductService.RegisterProductArgument;
import com.ail.core.product.RemoveProductService.RemoveProductArgument;
import com.ail.core.product.ResetAllProductsService.ResetAllProductsArgument;
import com.ail.core.product.ResetProductService.ResetProductArgument;
import com.ail.core.product.UpdateProductService.UpdateProductArgument;

@Remote
public interface ProductManagerRemote {
    ListProductsArgument listProducts(ListProductsArgument arg);

    RegisterProductArgument registerProduct(RegisterProductArgument arg);

    RemoveProductArgument removeProduct(RemoveProductArgument arg);

    ResetProductArgument resetProduct(ResetProductArgument arg);

    ResetAllProductsArgument resetAllProducts(ResetAllProductsArgument arg);

    UpdateProductArgument updateProduct(UpdateProductArgument arg);

    NewProductTypeArgument newProductType(NewProductTypeArgument arg);
}


