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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.core.Type;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/10/02 12:15:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/newproducttype/NewProductTypeCommand.java,v $
 * @stereotype command
 */
public class NewProductTypeCommand extends Command implements NewProductTypeArg {
    private NewProductTypeArg args = null;

    public NewProductTypeCommand() {
        super();
        args = new NewProductTypeArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (NewProductTypeArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getProductNameArg() {
        return args.getProductNameArg();
    }

    /**
     * {@inheritDoc}
     * @param productNameArg @{inheritDoc}
     */
    public void setProductNameArg(String productNameArg) {
        args.setProductNameArg(productNameArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getTypeNameArg() {
        return args.getTypeNameArg();
    }

    /**
     * {@inheritDoc}
     * @param typeNameArg @{inheritDoc}
     */
    public void setTypeNameArg(String typeNameArg) {
        args.setTypeNameArg(typeNameArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Type getTypeRet() {
        return args.getTypeRet();
    }

    /**
     * {@inheritDoc}
     * @param typeRet @{inheritDoc}
     */
    public void setTypeRet(Type typeRet) {
        args.setTypeRet(typeRet);
    }
}
