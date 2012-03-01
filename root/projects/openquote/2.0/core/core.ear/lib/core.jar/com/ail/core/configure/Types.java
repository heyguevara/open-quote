/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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
package com.ail.core.configure;


/**
 * This class is simply a binder containing a number of objects of type 'Type'.
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/Types.java,v $
 * @stereotype type
 */
public class Types extends Group {
    static final long serialVersionUID = -6746068039877928473L;

    /**
     * @supplierCardinality 0..*
     * @link aggregation 
     */
    /*# Type lnkType; */

	public Types() {
        setName("_Types");
    }

	/**
     * This method is here purely to keep castor XML binding happy.
     * If castor sees something like:
     *   <types xsi:type='java:com.ail.core.configuration.Types'/>
     * it will create an instance of com.ail.core.configuration.Types, and
     * then try to call setType("java:com.ail.core.configuration.Types");
	 * This appears to be because the Types class has a 'type' property.
	 * This method does absolutly nothing.
     */
	public void setType(String plop) {
	}

    public void addType(Type vType) throws IndexOutOfBoundsException {
		addGroup(vType);
    }

    public Type getType(int index) throws IndexOutOfBoundsException {
		return (Type)getGroup(index);
    }

    public int getTypeCount() {
		return getGroupCount();
    }

    public void removeAllType() {
		removeAllGroup();
    }

    public Type removeType(int index) {
		return (Type)(removeGroup(index));
    }

    public void setTypeAt(int index, Type vType) throws IndexOutOfBoundsException {
		setGroup(index, vType);
    }
}
