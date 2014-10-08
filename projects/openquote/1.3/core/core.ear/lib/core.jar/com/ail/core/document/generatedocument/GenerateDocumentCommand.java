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

import com.ail.core.Type;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/09/15 21:06:57 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/document/generatedocument/GenerateDocumentCommand.java,v $
 * @stereotype command
 */
public class GenerateDocumentCommand extends Command implements GenerateDocumentArg {
    private GenerateDocumentArg args = null;

    public GenerateDocumentCommand() {
        super();
        args = new GenerateDocumentArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (GenerateDocumentArg)arg;
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
     * @return @{inheritDoc}
     */
    public String getDocumentDefinitionArg() {
        return args.getDocumentDefinitionArg();
    }

    /**
     * {@inheritDoc}
     * @param documentDefinitionArg @{inheritDoc}
     */
    public void setDocumentDefinitionArg(String documentDefinitionArg) {
        args.setDocumentDefinitionArg(documentDefinitionArg);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public byte[] getRenderedDocumentRet() {
        return args.getRenderedDocumentRet();
    }

    /**
     * {@inheritDoc}
     * @param renderedDocumentRet @{inheritDoc}
     */
    public void setRenderedDocumentRet(byte[] renderedDocumentRet) {
        args.setRenderedDocumentRet(renderedDocumentRet);
    }

    /**
     * {@inheritDoc}
     * @return @{inheritDoc}
     */
    public String getProductNameArg() {
        return args.getProductNameArg();
    }

    /**
     * {@inheritDoc}
     * @param productNameArg @{inheritDoc}
     */
    public void setProductNameArg(String productNameArg) {
        args.setProductNameArg(productNameArg);
    }
}
