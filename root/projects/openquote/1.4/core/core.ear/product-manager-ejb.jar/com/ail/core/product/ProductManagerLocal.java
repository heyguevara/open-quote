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

import javax.ejb.EJBLocalObject;

import com.ail.core.BaseException;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.product.listproducts.ListProductsArgument;
import com.ail.core.product.newproducttype.NewProductTypeArgument;
import com.ail.core.product.registerproduct.RegisterProductArgument;
import com.ail.core.product.removeproduct.RemoveProductArgument;
import com.ail.core.product.resetallproducts.ResetAllProductsArgument;
import com.ail.core.product.resetproduct.ResetProductArgument;
import com.ail.core.product.updateproduct.UpdateProductArgument;

public interface ProductManagerLocal extends EJBLocalObject {
    String invokeServiceXML(String xml);

    VersionEffectiveDate getVersionEffectiveDate();

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    ListProductsArgument getListProducts(ListProductsArgument arg) throws BaseException;

    RegisterProductArgument registerProduct(RegisterProductArgument arg) throws BaseException;

    RemoveProductArgument removeProduct(RemoveProductArgument arg) throws BaseException;

    ResetProductArgument getProductDefinition(ResetProductArgument arg) throws BaseException;

    ResetAllProductsArgument resetAllProducts(ResetAllProductsArgument arg) throws BaseException;

    UpdateProductArgument updateProduct(UpdateProductArgument arg) throws BaseException;

    NewProductTypeArgument newProductType(NewProductTypeArgument arg) throws BaseException;
}
