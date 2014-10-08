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
package com.ail.coretest.service;

import java.util.Hashtable;

import com.ail.core.Type;

/**
 * Type class with a Hashtable property, to help TestTypeClone.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:29 $
 * @source $Source: /home/bob/CVSRepository/projects/core/test.jar/com/ail/coretest/service/TypeWithHashtable.java,v $
 */
public class TypeWithHashtable extends Type {
    private Hashtable<String,Object> table;
    private String name;

    public Hashtable<String,Object> getTable() {
        return table;
    }

    public void setTable(Hashtable<String,Object> table) {
        this.table = table;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
