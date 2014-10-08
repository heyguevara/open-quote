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

import java.util.ArrayList;
import java.util.Collection;

import com.ail.annotation.Configurable;
import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.PostconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.Parameter;

@ServiceImplementation
@Configurable
public class ListProductsService extends Service<ListProductsService.ListProductsArgument> {
    public static final String SERVICE_NAMESPACE="com.ail.core.product.ListProductsService";
    private String namespace;
    
    @ServiceArgument
    public interface ListProductsArgument extends Argument {
        /**
         * Getter for the productsRet property. A collection of ProductDetails representing all the
         * products know to the system is returned.
         * @return Value of productsRet, or null if it is unset
         */
        Collection<ProductDetails> getProductsRet();

        /**
         * Setter for the productsRet property.
         * @see #getProductsRet
         * @param productsRet new value for property.
         */
        void setProductsRet(Collection<ProductDetails> productsRet);
    }

    @ServiceCommand(defaultServiceClass=ListProductsService.class)
    public interface ListProductsCommand extends Command, ListProductsArgument {
    }

    
    private void setConfigurationNamespace(String namespace) {
        this.namespace=namespace;
    }
    
    /**
     * return the namespace which this service is associated with.
     */
    public String getConfigurationNamespace() {
        return (namespace!=null) ? namespace : super.getConfigurationNamespace();
    }

    /**
     * Reset the component's configuration.
     */
    public void resetConfiguration() {
        namespace=SERVICE_NAMESPACE;
        
        // Reset this service's default config.
        super.resetConfiguration();

        // Clear the cache so the next getParameter() will use the config just reset.
        ConfigurationHandler.reset(SERVICE_NAMESPACE);

        // Switch namespace to the product catalog config namespace named in this service config.
        setConfigurationNamespace(core.getParameterValue("productCatalogNamespace"));

        // Switch the product catalog config.
        super.resetConfigurationByNamespace();
    }

    public void invoke() throws PostconditionException {
        // select the product catalog namespace as our config
        setConfigurationNamespace(null);
        String catalogNamespace=getCore().getParameterValue("productCatalogNamespace");
        setConfigurationNamespace(catalogNamespace);

        args.setProductsRet(new ArrayList<ProductDetails>());
        
        // Each parameter in the 'products' group is a product name. Add them
        // to the return list.
        for(Parameter prod: core.getGroup("products").getParameter()) {
            args.getProductsRet().add(new ProductDetails(prod.getName(), prod.getValue()));
        }
        
        if (args.getProductsRet()==null) {
            throw new PostconditionException("args.getProductsRet()==null");
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


