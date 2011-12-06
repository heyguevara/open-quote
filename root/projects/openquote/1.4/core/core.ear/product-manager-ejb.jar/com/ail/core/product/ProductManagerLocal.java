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
import com.ail.core.product.listproducts.ListProductsCommand;
import com.ail.core.product.newproducttype.NewProductTypeCommand;
import com.ail.core.product.registerproduct.RegisterProductCommand;
import com.ail.core.product.removeproduct.RemoveProductCommand;
import com.ail.core.product.resetallproducts.ResetAllProductsCommand;
import com.ail.core.product.resetproduct.ResetProductCommand;
import com.ail.core.product.updateproduct.UpdateProductCommand;

/**
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/product-manager-ejb.jar/com/ail/core/product/ProductManagerLocal.java,v $
 */
public interface ProductManagerLocal extends EJBLocalObject {
    String invokeServiceXML(String xml);

    VersionEffectiveDate getVersionEffectiveDate();

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    ListProductsCommand getListProducts(ListProductsCommand arg) throws BaseException;

    RegisterProductCommand registerProduct(RegisterProductCommand arg) throws BaseException;

    RemoveProductCommand removeProduct(RemoveProductCommand arg) throws BaseException;

    ResetProductCommand getProductDefinition(ResetProductCommand arg) throws BaseException;

    ResetAllProductsCommand resetAllProducts(ResetAllProductsCommand arg) throws BaseException;

    UpdateProductCommand updateProduct(UpdateProductCommand arg) throws BaseException;

    NewProductTypeCommand newProductType(NewProductTypeCommand arg) throws BaseException;
}
