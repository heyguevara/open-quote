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

import java.util.HashSet;
import java.util.Set;

import com.ail.core.Type;
import com.ail.core.Version;

/**
 * Sample Type with a Set to help in testing the core's clonning
 */
public class TypeWithSet extends Type {
    private Set<Version> mySet=new HashSet<Version>();
    
    public Set<Version> getMySet() {
        return mySet;
    }
    
    public void setMySet(Set<Version> mySet) {
        this.mySet=mySet;
    }
}
