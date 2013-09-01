/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.insurance.policy;

import java.util.ArrayList;
import java.util.List;

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;

/**
 * This class is used to wrap a collection of policy summary instances.
 */
@TypeDefinition
public class SavedPolicySummaries extends Type {
    List<SavedPolicySummary> policySummary=new ArrayList<SavedPolicySummary>();

    public List<SavedPolicySummary> getPolicySummary() {
        return policySummary;
    }

    public void setPolicySummary(List<SavedPolicySummary> policySummary) {
        this.policySummary = policySummary;
    }
}
