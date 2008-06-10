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

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

import java.util.Collection;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2005/12/18 17:01:18 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/finder/GetClassListCommand.java,v $
 */
public class GetClassListCommand extends Command implements GetClassListArg {
    private GetClassListArg args = null;

    public GetClassListCommand() {
        super();
        args = new GetClassListArgImp();
    }

    /**
     * {@inheritDoc}
     * @see #setSearchClassArg
     * @return value of sc
     */
    public String getSearchClassArg() {
        return args.getSearchClassArg();
    }

    /**
     * {@inheritDoc}
     * @see #getSearchClassArg
     * @param sc New value for sc argument.
     */
    public void setSearchClassArg(String sc) {
        args.setSearchClassArg(sc);
    }
     /**
     * {@inheritDoc}
     * @see #getSearchPackageArg
     * @param sp New value for sp argument.
     */
    public void setSearchPackageArg(String sp) {
        args.setSearchPackageArg(sp);
    }
    /**
     * {@inheritDoc}
     * @see #setSearchPackageArg
     * @return value of sp
     */
    public String getSearchPackageArg() {
    	return args.getSearchPackageArg();
    }
    /**
     * {@inheritDoc}
     * @see #getFoundImplementorsRet
     * @param foundImplementors New value for foundImplementors argument.
     */
    public void setFoundImplementorsRet(Collection<String> foundImplementors) {
        args.setFoundImplementorsRet(foundImplementors);
    }
    /**
     * {@inheritDoc}
     * @see #setFoundImplementorsRet
     * @return value of foundImplementors
     */
    public Collection<String> getFoundImplementorsRet() {
    	return args.getFoundImplementorsRet();
    }

    public void setArgs(CommandArg arg) {
        this.args = (GetClassListArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }
}
