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

import java.util.List;

import com.ail.core.Type;
import com.ail.core.command.CommandArgImp;

/**
 * {@inheritDoc}
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/QueryArgImp.java,v $
 * @stereotype argimp
 */
public class QueryArgImp extends CommandArgImp implements QueryArg {
    static final long serialVersionUID = 1199346453402049909L;

    private Object[] queryArgumentsArg=null;
    private List<Object> resultsListRet;
    private Type uniqueResultsRet;
    private String queryNameArg;
    
    /** Default constructor */
    public QueryArgImp() {
    }

    public void setQueryArgumentsArg(Object... queryArgumentsArg) {
        this.queryArgumentsArg=queryArgumentsArg;
    }

    public Object[] getQueryArgumentsArg() {
        return queryArgumentsArg;
    }

    public String getQueryNameArg() {
        return queryNameArg;
    }

    public List<Object> getResultsListRet() {
        return resultsListRet;
    }

    public void setResultsListRet(List<Object> resultsListRet) {
        this.resultsListRet=resultsListRet;
    }

    public Type getUniqueResultRet() {
        return uniqueResultsRet;
    }

    public void setUniqueResultRet(Type uniqueResultsRet) {
        this.uniqueResultsRet=uniqueResultsRet;
    }

    public void setQueryNameArg(String queryNameArg) {
        this.queryNameArg=queryNameArg;
    }
}


