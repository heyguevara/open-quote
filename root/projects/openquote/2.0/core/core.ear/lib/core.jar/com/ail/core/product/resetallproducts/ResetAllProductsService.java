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

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationResetError;
import com.ail.core.product.ProductDetails;
import com.ail.core.product.listproducts.ListProductsCommand;
import com.ail.core.product.resetproduct.ResetProductCommand;

/**
 * @version $Revision: 1.4 $
 * @state $State: Exp $
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/resetallproducts/ResetAllProductsService.java,v $
 * @stereotype service
 */
public class ResetAllProductsService extends Service {
    private ResetAllProductsArg args = null;
    private Core core = null;

    /** Default constructor */
    public ResetAllProductsService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2007/10/05 22:47:50 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/resetallproducts/ResetAllProductsService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.4 $");
        return v;
    }

    
    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (ResetAllProductsArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of ResetAllProductsArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * This service doesn't have its own configuration, but it takes care of resetting 
     * all the products returned by the ListProducts service.
     */
    public void resetConfiguration() {
        try {
            super.resetConfiguration();
            ConfigurationHandler.reset();
            invoke();
        }
        catch(PreconditionException e) {
            throw new ConfigurationResetError("Failed to reset products:",e);
        }
        catch(NullPointerException e) {
            e.printStackTrace();
        }
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException {
        ListProductsCommand listProductsCommand=(ListProductsCommand)core.newCommand("ListProducts");
        ResetProductCommand resetProductCommand=(ResetProductCommand)core.newCommand("ResetProduct");

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
