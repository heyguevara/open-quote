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

/**
 * Points to another part/element of a policy.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/01/15 19:49:05 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Reference.java,v $
 * @stereotype type 
 */
public class Reference extends Type {
    private static final long serialVersionUID = 7406616018516416788L;

    /** This indicates the type of the object being referenced (e.g. asset, section, excess, ...). */
    private ReferenceType type = null;
    private String id = null;

    /**
     * Default constructor
     */
    public Reference() {
    }

    /**
     * Constructor
     * @param type The type of the object being referenced
     * @param id The Id of the object being referenced
     */
    public Reference(ReferenceType type, String id) {
        this.type=type;
        this.id=id;
    }

    /**
     * Set the type property. This indicates the type of the object being referenced (e.g. asset, section, excess, ...).
     * @param type New value for type property.
     */
    public void setType(ReferenceType type) {
        this.type = type;
    }

    /**
     * Set the type property from a String. This indicates the type of the object being referenced (e.g. asset, section,
     * excess, ...). The argument passed must represent a valid value for calling ReferenceType.forName(arg).
     * @see ReferenceType#forName
     * @see #setType
     * @throws IndexOutOfBoundsException If type is not a valid string representation of ReferenceType.
     * @param type New value for type property.
     */
    public void setTypeAsString(String type) {
        this.type = ReferenceType.valueOf(type);
    }

    /**
     * Get the value of the type property. This indicates the type of the object being referenced (e.g.
     * asset, section, excess, ...).
     * @return Value of type
     */
    public ReferenceType getType() {
        return this.type;
    }

    /**
     * Get the current value of the type property as a String.
     * @see #getType
     * @return Value of type property as a String, or null if type is null.
     */
    public String getTypeAsString() {
        if (type != null) {
            return type.name();
        }
        return null;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
