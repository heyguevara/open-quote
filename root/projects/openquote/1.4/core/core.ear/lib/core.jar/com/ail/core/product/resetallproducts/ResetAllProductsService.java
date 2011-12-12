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

package com.ail.core.product.resetallproducts;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationResetError;
import com.ail.core.product.ProductDetails;
import com.ail.core.product.listproducts.ListProductsCommand;
import com.ail.core.product.resetproduct.ResetProductCommand;
import com.ail.annotation.Configurable;

@Configurable
@ServiceImplementation
public class ResetAllProductsService extends Service<ResetAllProductsArgument> {
    /**
     * This service doesn't have its own configuration, but it takes care of resetting 
     * all the products returned by the ListProducts service.
     */
    @Override
    public void resetConfiguration() {
        try {
            super.resetConfiguration();
            ConfigurationHandler.resetCache();
            invoke();
        }
        catch(PreconditionException e) {
            throw new ConfigurationResetError("Failed to reset products:",e);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void invoke() throws PreconditionException {
        ListProductsCommand listProductsCommand=core.newCommand(ListProductsCommand.class);
        ResetProductCommand resetProductCommand=core.newCommand(ResetProductCommand.class);

        try {
            listProductsCommand.invoke();
        }
        catch(BaseException e) {
            core.logError("Failed to get product list"+e);
            throw new PreconditionException("listProductsCommand.invoke() threw an exception");
        }
                
        for(ProductDetails details: listProductsCommand.getProductsRet() ) {
            resetProductCommand.setProductNameArg(details.getName());

            try {
                resetProductCommand.invoke();
            }
            catch(BaseException e) {
                core.logError("Failed to reset product '"+resetProductCommand.getProductNameArg()+"':"+e);
                throw new PreconditionException("resetProductCommand.invoke() threw an exception");
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
