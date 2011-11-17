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

import com.ail.core.Core;
import com.ail.core.CoreProxy;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandArg;

/**
 * Instantiate a type defined by a product.
 */
public class NewProductTypeService extends Service {
    private NewProductTypeArg args = null;
    private CoreProxy core = null;

    /** Default constructor */
    public NewProductTypeService() {
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core.getCore();
    }

    public String getConfigurationNamespace() {
        return core.getConfigurationNamespace();
    }

    @Override
    public VersionEffectiveDate getVersionEffectiveDate() {
        return core.getVersionEffectiveDate();
    }
    
    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (NewProductTypeArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of NewProductTypeArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
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


