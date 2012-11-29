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

import javax.ejb.Local;

import com.ail.core.BaseException;
import com.ail.core.product.ListProductsService.ListProductsArgument;
import com.ail.core.product.NewProductTypeService.NewProductTypeArgument;
import com.ail.core.product.RegisterProductService.RegisterProductArgument;
import com.ail.core.product.RemoveProductService.RemoveProductArgument;
import com.ail.core.product.ResetAllProductsService.ResetAllProductsArgument;
import com.ail.core.product.ResetProductService.ResetProductArgument;
import com.ail.core.product.UpdateProductService.UpdateProductArgument;

@Local
public interface ProductManagerLocal {
    ListProductsArgument getListProducts(ListProductsArgument arg) throws BaseException;

    RegisterProductArgument registerProduct(RegisterProductArgument arg) throws BaseException;

    RemoveProductArgument removeProduct(RemoveProductArgument arg) throws BaseException;

    ResetProductArgument getProductDefinition(ResetProductArgument arg) throws BaseException;

    ResetAllProductsArgument resetAllProducts(ResetAllProductsArgument arg) throws BaseException;

    UpdateProductArgument updateProduct(UpdateProductArgument arg) throws BaseException;

    NewProductTypeArgument newProductType(NewProductTypeArgument arg) throws BaseException;
}
