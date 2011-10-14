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

import com.ail.core.command.CommandArgImp;
import com.ail.core.document.model.DocumentData;
import com.ail.core.Type;
import com.ail.core.XMLString;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype argimp
 */
public class MergeDataArgImp extends CommandArgImp implements MergeDataArg {
    private DocumentData documentDataArg;
    private Type modelArg;
    private XMLString mergedDataRet;
    private String productNameArg;
    
    /**
     * @inheritDoc
     */
    public String getProductNameArg() {
        return productNameArg;
    }

    /**
     * @inheritDoc
     */
    public void setProductNameArg(String productNameArg) {
        this.productNameArg = productNameArg;
    }

    /**
     * @inheritDoc
     */
    public DocumentData getDocumentDataArg() {
        return documentDataArg;
    }

    /**
     * @inheritDoc
     */
    public XMLString getMergedDataRet() {
        return mergedDataRet;
    }

    /**
     * @inheritDoc
     */
    public Type getModelArg() {
        return modelArg;
    }

    /**
     * @inheritDoc
     */
    public void setDocumentDataArg(DocumentData documentDataArg) {
        this.documentDataArg=documentDataArg;
    }

    /**
     * @inheritDoc
     */
    public void setMergedDataRet(XMLString mergedDataRet) {
        this.mergedDataRet=mergedDataRet;
    }

    /**
     * @inheritDoc
     */
    public void setModelArg(Type modelArg) {
        this.modelArg=modelArg;
    }
}


