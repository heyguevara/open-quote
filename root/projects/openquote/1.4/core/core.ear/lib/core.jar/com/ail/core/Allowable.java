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

package com.ail.core;

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;
import java.util.Collection;
import java.util.Vector;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/Allowable.java,v $
 * @stereotype type
 */
@TypeDefinition
public class Allowable extends Type {

	/**
	 * @link aggregationByValue
	 * @clientCardinality 1
	 * @supplierCardinality 0..*
	 * @directed
	 */

    /*# Allowable lnkAllowable; */
    private Vector<Allowable> allowable = new Vector<Allowable>();

	// field Name
	private String name = null;
	
	// if collection element, type identifier
	private String typeId = "";
	
	// class name of field
	private String className = null;




	/**
	 * Get the className associated with this allowable.
	 *
	 * @return The field className.
	 */
	public String getClassName() {
		return className;
	}

	/**
	 * Set the className associated with this allowable
	 *
	 * @param className New field className
	 */
	public void setClassName(String className) {
		this.className = className;
	}



	/**
	 * Get the typeId associated with this allowable.
	 *
	 * @return The field typeId.
	 */
	public String getTypeId() {
		return typeId;
	}

	/**
	 * Set the typeId associated with this allowable
	 *
	 * @param typeId New field typeId
	 */
	public void setTypeId(String typeId) {
		this.typeId = typeId;
	}



	/**
	 * Get the name associated with this allowable.
	 *
	 * @return The field name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Set the name associated with this allowable
	 *
	 * @param name New field name
	 */
	public void setName(String name) {
		this.name = name;
	}



    /**
     * Get the collection of instances of com.ail.core.Allowable associated with this object.
     * @return A collection of instances of Allowable
     * @see #setAllowable
     */
    public Collection<Allowable> getAllowable() {
        return allowable;
    }

    /**
     * Set the collection of instances of com.ail.core.Allowable associated with this object.
     * @param allowable A collection of instances of Allowable
     * @see #getAllowable
     */
    public void setAllowable(Collection<Allowable> allowable) {
        this.allowable = new Vector<Allowable>(allowable);
    }

    /**
     * Get a count of the number of com.ail.core.Allowable instances associated with this object
     * @return Number of instances
     */
    public int getAllowableCount() {
        return this.allowable.size();
    }

    /**
     * Fetch a spacific com.ail.core.Allowable from the collection by index number.
     * @param i Index of element to return
     * @return The instance of com.ail.core.Allowable at the specified index
     */
    public Allowable getAllowable(int i) {
        return (com.ail.core.Allowable) this.allowable.get(i);
    }

    /**
     * Remove the element specified from the list.
     * @param i Index of element to remove
     */
    public void removeAllowable(int i) {
        this.allowable.remove(i);
    }

    /**
     * Remove the specified instance of com.ail.core.Allowable from the list.
     * @param wording Instance to be removed
     */
    public void removeAllowable(Allowable wording) {
        this.allowable.remove(wording);
    }

    /**
     * Add an instance of com.ail.core.Allowable to the list associated with this object.
     * @param wording Instance to add to list
     */
    public void addAllowable(Allowable allowable) {
        this.allowable.add(allowable);
    }
}
