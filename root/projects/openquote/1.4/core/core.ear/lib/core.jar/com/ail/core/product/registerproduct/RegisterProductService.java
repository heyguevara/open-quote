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

package com.ail.core.product.registerproduct;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.Group;
import com.ail.core.configure.Parameter;
import com.ail.core.product.DuplicateProductException;
import com.ail.core.product.listproducts.ListProductsService;

@ServiceImplementation
public class RegisterProductService extends Service<RegisterProductArgument> {
    private String namespace;
    
    /**
     * This service uses both the ListProductsService.SERVICE_NAMESPACE namespace and the namespace
     * which that namespace defines as the product catalog.
     * @param namespace
     */
    private void setConfigurationNamespace(String namespace) {
        this.namespace=namespace;
    }
    
    /**
     * return the namespace which this service is associated with.
     */
    @Override
    public String getConfigurationNamespace() {
        return (namespace!=null) ? namespace : super.getConfigurationNamespace();
    }

    @Override
    public void invoke() throws PreconditionException, PostconditionException, DuplicateProductException {
        // Check the essential preconditions
        if (args.getProductDetailsArg().getName()==null || args.getProductDetailsArg().getName().length()==0) {
            throw new PreconditionException("args.getProductDetailsArg().getName()==null || args.getProductDetailsArg().getName().length()==0");
        }
        
        // Select the product catalog namespace as our config
        setConfigurationNamespace(ListProductsService.SERVICE_NAMESPACE);
        String catalogNamespace=core.getParameterValue("productCatalogNamespace");
        setConfigurationNamespace(catalogNamespace);

        // Check that the product doesn't already exist
        if (core.getParameter("products."+args.getProductDetailsArg().getName())!=null) {
            throw new DuplicateProductException(args.getProductDetailsArg().getName());
        }
        
        // The "Registry" is implemented as a group in the product catalog's configuration namespace - 
        // add the new products details to the registry.
        Configuration config=core.getConfiguration();
        Group prods=config.findGroup("products");
        Parameter prod=new Parameter();
        prod.setName(args.getProductDetailsArg().getName());
        prod.setValue(args.getProductDetailsArg().getDescription());
        prods.addParameter(prod);
        core.setConfiguration(config);

        // force the catalog to be reloaded on the next attempt to read it.
        ConfigurationHandler.reset(catalogNamespace);
    }

    /**
     * Always use the latest configuration for this and the other product catalog 
     * related services.
     */
    @Override
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }
}


