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
import java.util.Vector;
import java.util.Collection;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/jdo/QueryResults.java,v $
 * @stereotype type
 */
@TypeDefinition
public class QueryResults extends Type {
    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @directed
     * @clientCardinality 1
     */
    /*# Object resultLink; */

    /** Results of the query */
    private Vector<Object> result = new Vector<Object>();

    /**
     * Get the collection of instances of Object associated with this object.
     * @return result A collection of instances of Excess
     * @see #setResult
     */
    public Collection<Object> getResult() {
        return result;
    }

    /**
     * Set the collection of instances of Object associated with this object.
     * @param result A collection of instances of Excess
     * @see #getResult
     */
    public void setResult(Collection<Object> result) {
        this.result = new Vector<Object>(result);
    }

    /**
     * Get a count of the number of Object instances associated with this object
     * @return Number of instances
     */
    public int getResultCount() {
        return this.result.size();
    }

    /**
     * Fetch a spacific Object from the collection by index number.
     * @param i Index of element to return
     * @retun The instance of Object at the specified index
     */
    public Object getResult(int i) {
        return (Object)this.result.get(i);
    }

    /**
     * Remove the element specified from the list.
     * @param i Index of element to remove
     */
    public void removeResult(int i) {
        this.result.remove(i);
    }

    /**
     * Remove the specified instance of Object from the list.
     * @param result Instance to be removed
     */
    public void removeResult(Object result) {
        this.result.remove(result);
    }

    /**
     * Add an instance of Object to the list associated with this object.
     * @param result Instance to add to list
     */
    public void addResult(Object result) {
        this.result.add(result);
    }
}
