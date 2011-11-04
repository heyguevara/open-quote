/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

package com.ail.core.key;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

public class GenerateUniqueKeyCommand extends Command implements GenerateUniqueKeyArg {
    private GenerateUniqueKeyArg args = null;

    public GenerateUniqueKeyCommand() {
        super();
        args = new GenerateUniqueKeyArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (GenerateUniqueKeyArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    public void setKeyIdArg(String keyIdArg) {
        args.setKeyIdArg(keyIdArg);
    }

    public String getKeyIdArg() {
        return args.getKeyIdArg();
    }

    public void setKeyRet(Integer keyRet) {
        args.setKeyRet(keyRet);
    }

    public Integer getKeyRet() {
        return args.getKeyRet();
    }

    public void setProductTypeIdArg(String productTypeId) {
        args.setProductTypeIdArg(productTypeId);
    }

    public String getProductTypeIdArg() {
        return args.getProductTypeIdArg();
    }
}
