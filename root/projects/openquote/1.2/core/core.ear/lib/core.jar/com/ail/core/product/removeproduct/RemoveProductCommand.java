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

package com.ail.core.product.removeproduct;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.core.product.ProductDetails;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype command
 */
public class RemoveProductCommand extends Command implements RemoveProductArg {
    private RemoveProductArg args = null;

    public RemoveProductCommand() {
        super();
        args = new RemoveProductArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (RemoveProductArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public ProductDetails getProductDetailsArg() {
        return args.getProductDetailsArg();
    }

    /**
     * {@inheritDoc}
     * @param productsRet @{inheritDoc}
     */
    public void setProductDetailsArg(ProductDetails productDetailsArg) {
        args.setProductDetailsArg(productDetailsArg);
    }
}
