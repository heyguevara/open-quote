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
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.product.listproducts.ListProductsArg;
import com.ail.core.product.newproducttype.NewProductTypeArg;
import com.ail.core.product.registerproduct.RegisterProductArg;
import com.ail.core.product.removeproduct.RemoveProductArg;
import com.ail.core.product.resetallproducts.ResetAllProductsArg;
import com.ail.core.product.resetproduct.ResetProductArg;
import com.ail.core.product.updateproduct.UpdateProductArg;

@Local
public interface ProductManagerLocal {
    String invokeServiceXML(String xml);

    VersionEffectiveDate getVersionEffectiveDate();

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    Version getVersion();

    ListProductsArg getListProducts(ListProductsArg arg) throws BaseException;

    RegisterProductArg registerProduct(RegisterProductArg arg) throws BaseException;

    RemoveProductArg removeProduct(RemoveProductArg arg) throws BaseException;

    ResetProductArg getProductDefinition(ResetProductArg arg) throws BaseException;

    ResetAllProductsArg resetAllProducts(ResetAllProductsArg arg) throws BaseException;

    UpdateProductArg updateProduct(UpdateProductArg arg) throws BaseException;

    NewProductTypeArg newProductType(NewProductTypeArg arg) throws BaseException;
}
