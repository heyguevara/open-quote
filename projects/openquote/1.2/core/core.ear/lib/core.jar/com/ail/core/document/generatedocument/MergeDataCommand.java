/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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

package com.ail.core.document.generatedocument;

import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;
import com.ail.core.document.model.DocumentData;
import com.ail.core.Type;
import com.ail.core.XMLString;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype command
 */
public class MergeDataCommand extends Command implements MergeDataArg {
    private MergeDataArg args = null;

    public MergeDataCommand() {
        super();
        args = new MergeDataArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (MergeDataArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public Type getModelArg() {
        return args.getModelArg();
    }

    /**
     * {@inheritDoc}
     * @param modelArg @{inheritDoc}
     */
    public void setModelArg(Type modelArg) {
        args.setModelArg(modelArg);
    }

    /**
     * {@inheritDoc}
     */
    public DocumentData getDocumentDataArg() {
        return args.getDocumentDataArg();
    }

    /**
     * {@inheritDoc}
     */
    public XMLString getMergedDataRet() {
        return args.getMergedDataRet();
    }

    /**
     * {@inheritDoc}
     */
    public void setDocumentDataArg(DocumentData documentDataArg) {
        args.setDocumentDataArg(documentDataArg);
    }

    /**
     * {@inheritDoc}
     */
    public void setMergedDataRet(XMLString mergedDataRet) {
        args.setMergedDataRet(mergedDataRet);
    }

    /**
     * {@inheritDoc}
     */
    public String getProductNameArg() {
        return args.getProductNameArg();
    }

    /**
     * {@inheritDoc}
     */
    public void setProductNameArg(String productNameArg) {
        args.setProductNameArg(productNameArg);
    }
}
