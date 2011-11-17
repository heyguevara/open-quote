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

package com.ail.core.product.newproducttype;

import static com.ail.core.Functions.productNameToConfigurationNamespace;
import static com.ail.core.Functions.productNameToDefaultType;

import com.ail.core.CoreProxy;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;

/**
 * Instantiate a type defined by a product.
 */
public class NewProductTypeService extends Service<NewProductTypeArg> {
    private CoreProxy core;
    
    public String getConfigurationNamespace() {
        return core.getConfigurationNamespace();
    }

    @Override
    public VersionEffectiveDate getVersionEffectiveDate() {
        return core.getVersionEffectiveDate();
    }
    
    @Override
    public void invoke() throws PreconditionException {
        
        if (args.getProductNameArg()==null || args.getProductNameArg().length()==0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }
        
        // Create a proxy to work on behalf of the caller, but using the product name as a configuration namespace.
        String namespace=productNameToConfigurationNamespace(args.getProductNameArg());
        core=new CoreProxy(namespace, args.getCallersCore());
        
        String typename=args.getTypeNameArg();

        // If no type name was specified, we'll use the last part of the namespace's name.
        // i.e. if the namespace is 'com.ail.core.Plop', we'll use Plop. This is how the
        // convention of 'default' type is implemented. 
        if (typename==null) {
            typename=productNameToDefaultType(args.getProductNameArg());
        }

        args.setTypeRet(core.newType(typename));
    }
}


