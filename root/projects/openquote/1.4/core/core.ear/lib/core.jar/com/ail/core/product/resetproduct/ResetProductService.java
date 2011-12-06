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

package com.ail.core.product.resetproduct;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.ConfigurationResetError;

@ServiceImplementation
public class ResetProductService extends Service<ResetProductArgument> {
    private String namespace=null;

    @Override
    public String getConfigurationNamespace() {
        return namespace;
    }

    /**
     * Reset the component's configuration.
     */
    @Override
    public void resetConfiguration() {
        super.resetConfigurationByNamespace();
    }

    @Override
    public void invoke() throws PreconditionException {
        if (args.getProductNameArg()==null || args.getProductNameArg().length()==0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }

        try {
            namespace=productNameToConfigurationNamespace(args.getProductNameArg());
            resetConfiguration();
            core.logInfo("Product reset:"+namespace);
        }
        catch(ConfigurationResetError e) {
            e.printStackTrace();
            throw new PreconditionException("Product not found: "+namespace);
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


