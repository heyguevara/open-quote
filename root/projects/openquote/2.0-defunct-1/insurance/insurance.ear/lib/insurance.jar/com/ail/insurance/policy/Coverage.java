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

package com.ail.insurance.policy;

import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;

/**
 * Coverages define or constrain the types of cover offered by a Section. Typically, a section will contain a collection
 * of coverages. Coverages may exist within a section but not be in effect ('enabled' indicator). They may also be
 * optional - selected at the policy holders descretion ('optional' flag).
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/03/07 20:58:10 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Coverage.java,v $
 * @stereotype type
 */
public class Coverage extends Type {
    static final long serialVersionUID = 7326823306523810654L;
    private String id;
    private boolean enabled=false;
    private boolean optional=false;
    private String description;
    private CurrencyAmount limit;
    private CurrencyAmount deductible;
    
    public Coverage() {
    }
    
    public Coverage(String id, CurrencyAmount limit, CurrencyAmount deductible) {
        this.id=id;
        this.limit=limit;
        this.deductible=deductible;
    }
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public CurrencyAmount getDeductible() {
        return deductible;
    }

    public void setDeductible(CurrencyAmount deductible) {
        this.deductible = deductible;
    }

    public CurrencyAmount getLimit() {
        return limit;
    }

    public void setLimit(CurrencyAmount limit) {
        this.limit = limit;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean isOptional() {
        return optional;
    }

    public void setOptional(boolean optional) {
        this.optional = optional;
    }
}
