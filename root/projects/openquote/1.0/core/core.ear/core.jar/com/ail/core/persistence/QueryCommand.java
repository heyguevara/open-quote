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
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/07/15 15:01:44 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/QueryCommand.java,v $
 * @stereotype command
 */
public class QueryCommand extends Command implements QueryArg {
    private QueryArg args = null;

    public QueryCommand() {
        super();
        args = new QueryArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (QueryArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    public void setQueryArgumentsArg(Object... queryArgumentsArg) {
        args.setQueryArgumentsArg(queryArgumentsArg);
    }

    public Object[] getQueryArgumentsArg() {
        return args.getQueryArgumentsArg();
    }

    public void setQueryNameArg(String queryName) {
        args.setQueryNameArg(queryName);
    }

    public String getQueryNameArg() {
        return args.getQueryNameArg();
    }

    public List<?> getResultsListRet() {
        return args.getResultsListRet();
    }

    public void setResultsListRet(List<Object> resultsListRet) {
        args.setResultsListRet(resultsListRet);
    }

    public Type getUniqueResultRet() {
        return args.getUniqueResultRet();
    }

    public void setUniqueResultRet(Type type) {
        args.setUniqueResultRet(type);
    }

}
