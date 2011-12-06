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

package com.ail.core.configure.finder;

import java.util.Collection;

import com.ail.annotation.ArgumentDefinition;
import com.ail.core.command.Argument;

@ArgumentDefinition
public interface GetClassListArgument extends Argument {

    /**
     * Set the value of the search class argument.
     * @see #getSearchClassArg
     * @param searchClassName New value for class argument.
     */
    void setSearchClassArg(String searchClassName);

    /**
     * Fetch the value of the class argument. 
     * @see #setSearchClassArg
     * @return value of searchClass
     */
    String getSearchClassArg();

     /**
     * Set value of search package argument. Defaults to com.ail if not set.
     * @see #getSearchPackageArg
     * @param searchPackage New value for searchPackage argument.
     */
    public void setSearchPackageArg(String searchPackage);

    /**
     * Get value of search package argument.
     * @see #setSearchPackageArg
     * @return value of searchPackage
     */
    public String getSearchPackageArg();

    /**
     * Set value of implementors found argument.
     * @see #getFoundImplementorsRet
     * @param foundImplementors New value for foundImplementors argument.
     */
    public void setFoundImplementorsRet(Collection<String> foundImplementors);

    /**
     * Get value of implementors found argument.
     * @see #setFoundImplementorsRet
     * @return value of foundImplementors
     */
    public Collection<String> getFoundImplementorsRet();
}


