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

package com.ail.core.validator;

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;
import java.util.Vector;
import java.util.Collection;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/validator/ValidatorResult.java,v $
 * @stereotype type
 */
@TypeDefinition
public class ValidatorResult extends Type {
    /**
     * @link aggregationByValue
     * @supplierCardinality 0..*
     * @directed
     * @clientCardinality 1
     */
    /*# ValidatorFailureDetail failureDetailLink; */

    /** failure details */
    private Vector<ValidatorFailureDetail> failureDetail = new Vector<ValidatorFailureDetail>();

    /**
     * Get the collection of instances of ValidatorFailureDetail associated with this object.
     * @return failureDetail A collection of instances of Excess
     * @see #setFailureDetail
     */
    public Collection<ValidatorFailureDetail> getFailureDetail() {
        return failureDetail;
    }

    /**
     * Set the collection of instances of ValidatorFailureDetail associated with this object.
     * @param failureDetail A collection of instances of Excess
     * @see #getFailureDetail
     */
    public void setFailureDetail(Collection<ValidatorFailureDetail> failureDetail) {
        this.failureDetail = new Vector<ValidatorFailureDetail>(failureDetail);
    }

    /**
     * Get a count of the number of ValidatorFailureDetail instances associated with this object
     * @return Number of instances
     */
    public int getFailureDetailCount() {
        return this.failureDetail.size();
    }

    /**
     * Fetch a specific ValidatorFailureDetail from the collection by index number.
     * @param i Index of element to return
     * @return The instance of ValidatorFailureDetail at the specified index
     */
    public ValidatorFailureDetail getFailureDetail(int i) {
        return (ValidatorFailureDetail)this.failureDetail.get(i);
    }

    /**
     * Remove the element specified from the list.
     * @param i Index of element to remove
     */
    public void removeFailureDetail(int i) {
        this.failureDetail.remove(i);
    }

    /**
     * Remove the specified instance of ValidatorFailureDetail from the list.
     * @param failureDetail Instance to be removed
     */
    public void removeFailureDetail(ValidatorFailureDetail failureDetail) {
        this.failureDetail.remove(failureDetail);
    }

    /**
     * Add an instance of ValidatorFailureDetail to the list associated with this object.
     * @param failureDetail Instance to add to list
     */
    public void addFailureDetail(ValidatorFailureDetail failureDetail) {
        this.failureDetail.add(failureDetail);
    }
}
