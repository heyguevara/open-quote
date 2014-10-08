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

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.Group;
import com.ail.core.configure.Parameter;

@ServiceImplementation
public class UpdateProductService extends Service<UpdateProductService.UpdateProductArgument> {
    private String namespace;
    
    @ServiceArgument
    public interface UpdateProductArgument extends Argument {
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
        
        /**
         * The name of the product to be updated.
         * @return The name of the product to be updated.
         */
        String getProductNameArg();
        
        /**
         * The name of the product to be updated.
         * @param productName The name of the product to update 
         */
        void setProductNameArg(String productName);
    }
    
    @ServiceCommand(defaultServiceClass=UpdateProductService.class)
    public interface UpdateProductCommand extends Command, UpdateProductArgument {
    }

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
    public void invoke() throws PreconditionException, PostconditionException, UnknownProductException {
        // Check the essential preconditions
        if (args.getProductDetailsArg().getName()==null || args.getProductDetailsArg().getName().length()==0) {
            throw new PreconditionException("args.getProductDetailsArg().getName()==null || args.getProductDetailsArg().getName().length()==0");
        }
        
        if (args.getProductNameArg()==null || args.getProductNameArg().length()==0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }
        
        // Select the product catalog namespace as our config
        setConfigurationNamespace(ListProductsService.SERVICE_NAMESPACE);
        String catalogNamespace=core.getParameterValue("productCatalogNamespace");
        setConfigurationNamespace(catalogNamespace);

        // Check that the product exist
        if (core.getParameter("products."+args.getProductNameArg())==null) {
            throw new UnknownProductException(args.getProductNameArg());
        }
        
        // The "Registry" is implemented as a group in the product catalog's configuration namespace - 
        // remove the 
        Configuration config=core.getConfiguration();
        Group prods=config.findGroup("products");
        
        // Find the entry for the product 'as loaded' - it may be that the update
        // changes the product's name, so we find the one that was loaded.
        for(int i=0 ; i<prods.getParameterCount() ; i++) {
            if (prods.getParameter(i).getName().equals(args.getProductNameArg())) {
                Parameter prod=prods.getParameter(i);
                prod.setName(args.getProductDetailsArg().getName());
                prod.setValue(args.getProductDetailsArg().getDescription());
                core.setConfiguration(config);
                ConfigurationHandler.reset(catalogNamespace);
                break;
            }
        }
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


