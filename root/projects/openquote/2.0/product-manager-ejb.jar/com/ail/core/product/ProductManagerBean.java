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

import javax.ejb.CreateException;
import javax.ejb.Remote;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.annotation.Configurable;
import com.ail.core.BaseServerException;
import com.ail.core.EJBComponent;
import com.ail.core.product.ListProductsService.ListProductsArgument;
import com.ail.core.product.NewProductTypeService.NewProductTypeArgument;
import com.ail.core.product.RegisterProductService.RegisterProductArgument;
import com.ail.core.product.RemoveProductService.RemoveProductArgument;
import com.ail.core.product.ResetAllProductsService.ResetAllProductsArgument;
import com.ail.core.product.ResetProductService.ResetProductArgument;
import com.ail.core.product.UpdateProductService.UpdateProductArgument;

@Configurable
@Stateless
@Remote(ProductManager.class)
public class ProductManagerBean extends EJBComponent implements ProductManager {
    private static final String NAMESPACE="com.ail.core.product.ProductManagerBean";
    private SessionContext ctx = null;

    public ProductManagerBean() {
        initialise(NAMESPACE);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbCreate() throws CreateException {
        initialise(NAMESPACE);
    }

    /**
     * Service wrapper method for the ListProducts service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ListProductsArgument getListProducts(ListProductsArgument arg) throws BaseServerException {
        return invokeCommand("ListProducts", arg);
    }

    /**
     * Service wrapper method for the RegisterProduct service.
     * @param arg Command to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public RegisterProductArgument registerProduct(RegisterProductArgument arg) throws BaseServerException {
        return invokeCommand("RegisterProduct", arg);
    }

    /**
     * Service wrapper method for the RemoveProduct service.
     * @param arg Command to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public RemoveProductArgument removeProduct(RemoveProductArgument arg) throws BaseServerException {
        return invokeCommand("RemoveProduct", arg);
    }

    /**
     * Service wrapper method for the ResetProduct service.
     * @param arg Command to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ResetProductArgument getProductDefinition(ResetProductArgument arg) throws BaseServerException {
        return invokeCommand("ResetProduct", arg);
    }

    /**
     * Service wrapper method for the ResetAllProductsCommand service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ResetAllProductsArgument resetAllProducts(ResetAllProductsArgument arg) throws BaseServerException {
        return invokeCommand("ResetAllProducts", arg);
    }

    /**
     * Service wrapper method for the UpdateProductDefinition service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public UpdateProductArgument updateProduct(UpdateProductArgument arg) throws BaseServerException {
        return invokeCommand("UpdateProduct", arg);
    }

    /**
     * Service wrapper method for the NewProductType service.
     * @param arg LoggingArgument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public NewProductTypeArgument newProductType(NewProductTypeArgument arg) throws BaseServerException {
        return invokeCommand("NewProductType", arg);
    }
}


