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

    Version getVersion() throws RemoteException;

    ListProductsArg getListProducts(ListProductsArg arg) throws RemoteException;

    RegisterProductArg registerProduct(RegisterProductArg arg) throws RemoteException;

    RemoveProductArg removeProduct(RemoveProductArg arg) throws RemoteException;

    ResetProductArg getProductDefinition(ResetProductArg arg) throws RemoteException;

    ResetAllProductsArg resetAllProducts(ResetAllProductsArg arg) throws RemoteException;

    UpdateProductArg updateProduct(UpdateProductArg arg) throws RemoteException;

    NewProductTypeArg newProductType(NewProductTypeArg arg) throws RemoteException;
}


