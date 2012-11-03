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

package com.ail.core.persistence.jdo;

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;

@TypeDefinition
public class Query extends Type {
    /** The text of the query - OQL, SQL, etc */
    private String query;

    /**
     * Getter returning the value of the query property. The text of the query - OQL, SQL, etc
     * @return Value of the query property
     */
    public String getQuery() {
        return query;
    }

    /**
     * Setter to update the value of the query property. The text of the query - OQL, SQL, etc
     * @param query New value for the query property
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * Getter returning the value of the query property wrapped in CDATA tags. The text of the query - OQL, SQL, etc
     * @return Value of the query property
     */
    public String getQueryCDATA() {
        return "<![CDATA["+query + "]]>";
    }

    public void bind(Boolean param) {
    }
}
