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

package com.ail.core.persistence;

import com.ail.core.Type;
import com.ail.core.command.CommandArg;

/**
 * Arguments required by create service
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/UpdateArg.java,v $
 * @stereotype arg
 */
public interface UpdateArg extends CommandArg {
	/**
	 * Setter for the objectArg property. * @see #getObjectArg
	 * @param objectArg new value for property.
	 */
	void setObjectArg(Type objectArg);

    /**
     * Getter for the objectArg property. Object to persist
     * @return Value of objectArg
     */
    Type getObjectArg();
}


