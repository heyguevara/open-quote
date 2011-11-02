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

import com.ail.core.command.CommandArgImp;

/**
 * {@inheritDoc}
 */
public class GenerateUniqueKeyArgImp extends CommandArgImp implements GenerateUniqueKeyArg {
    static final long serialVersionUID = 1199346453402049909L;
    private String keyIdArg;
    private Integer keyRet;
    
    /** Default constructor */
    public GenerateUniqueKeyArgImp() {
    }

    public void setKeyIdArg(String keyIdArg) {
        this.keyIdArg=keyIdArg;
    }

    public String getKeyIdArg() {
        return keyIdArg;
    }

    public void setKeyRet(Integer keyRet) {
        this.keyRet=keyRet;
    }

    public Integer getKeyRet() {
        return keyRet;
    }
}


