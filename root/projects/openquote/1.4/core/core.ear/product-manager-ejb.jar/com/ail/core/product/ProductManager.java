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

import java.rmi.RemoteException;

import javax.ejb.EJBObject;

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
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2007/12/22 10:40:49 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/product-manager-ejb.jar/com/ail/core/product/ProductManager.java,v $
 */
public interface ProductManager extends EJBObject {
    /** @link dependency */

    /*# ProductManagerBean lnkProductManagerBean; */

    String invokeServiceXML(String xml) throws RemoteException;

    VersionEffectiveDate getVersionEffectiveDate() throws RemoteException;

    void setConfiguration(Configuration config) throws RemoteException;

    Configuration getConfiguration() throws RemoteException;

    String getConfigurationNamespace() throws RemoteException;

    void resetConfiguration() throws RemoteException;

    ListProductsCommand getListProducts(ListProductsCommand arg) throws RemoteException;

    RegisterProductCommand registerProduct(RegisterProductCommand arg) throws RemoteException;

    RemoveProductCommand removeProduct(RemoveProductCommand arg) throws RemoteException;

    ResetProductCommand getProductDefinition(ResetProductCommand arg) throws RemoteException;

    ResetAllProductsCommand resetAllProducts(ResetAllProductsCommand arg) throws RemoteException;

    UpdateProductCommand updateProduct(UpdateProductCommand arg) throws RemoteException;

    NewProductTypeCommand newProductType(NewProductTypeCommand arg) throws RemoteException;
}


