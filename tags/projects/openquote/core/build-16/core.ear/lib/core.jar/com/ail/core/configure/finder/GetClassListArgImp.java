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

import java.util.ArrayList;
import java.util.Collection;

import com.ail.core.command.CommandArgImp;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/finder/GetClassListArgImp.java,v $
 */
public class GetClassListArgImp extends CommandArgImp implements GetClassListArg {
    static final long serialVersionUID = 1199346453402049909L;

    /* Class to check for inherited/implemented classes in cp */
    private String searchClassArg = null;
    /* Optional - Package to check for inherited/implemented classes in cp */
    private String searchPackageArg = null;
    /* Impementors found  */
    private Collection<String> foundImplementorsRet = new ArrayList<String>();

    /** Default constructor */
    public GetClassListArgImp() {
    }

    /**
     * {@inheritDoc}
     * @see #getSearchClassArg
     * @param searchClass New value for sc argument.
     */
    public void setSearchClassArg(String searchClass) {
        this.searchClassArg = searchClass;
    }

    /**
     * {@inheritDoc}
     * @see #setSearchClassArg
     * @return value of searchClass
     */
    public String getSearchClassArg() {
    	return searchClassArg;
    }

     /**
     * {@inheritDoc}
     * @see #getSearchPackageArg
     * @param searchPackage New value for searchPackage argument.
     */
    public void setSearchPackageArg(String searchPackage) {
        this.searchPackageArg = searchPackage;
    }
    /**
     * {@inheritDoc}
     * @see #setSearchPackageArg
     * @return value of searchPackage
     */
    public String getSearchPackageArg() {
    	return searchPackageArg;
    }
    /**
     * {@inheritDoc}
     * @see #getFoundImplementorsRet
     * @param foundImplementors New value for foundImplementors argument.
     */
    public void setFoundImplementorsRet(Collection<String> foundImplementors) {
        this.foundImplementorsRet = foundImplementors;
    }
    /**
     * {@inheritDoc}
     * @see #setFoundImplementorsRet
     * @return value of foundImplementors
     */
    public Collection<String> getFoundImplementorsRet() {
    	return foundImplementorsRet;
    }
}


