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

import com.ail.core.XMLString;
import com.ail.core.command.Command;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype command
 */
public class RenderDocumentCommand extends Command implements RenderDocumentArg {
    private RenderDocumentArg args = null;

    public RenderDocumentCommand() {
        super();
        args = new RenderDocumentArgImp();
    }

    public void setArgs(CommandArg arg) {
        this.args = (RenderDocumentArg)arg;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * {@inheritDoc}
     */
    public XMLString getSourceDataArg() {
        return args.getSourceDataArg();
    }

    /**
     * {@inheritDoc}
     */
    public void setSourceDataArg(XMLString sourceDataArg) {
        args.setSourceDataArg(sourceDataArg);
    }


    /**
     * {@inheritDoc}
     */
    public String getRenderOptionsArg() {
        return args.getRenderOptionsArg();
    }

    /**
     * {@inheritDoc}
     */
    public void setRenderOptionsArg(String renderOptionsArg) {
        args.setRenderOptionsArg(renderOptionsArg);
    }

    /**
     * {@inheritDoc}
     */
    public byte[] getRenderedDocumentRet() {
        return args.getRenderedDocumentRet();
    }

    /**
     * {@inheritDoc}
     */
    public void setRenderedDocumentRet(byte[] renderedDocumentRet) {
        args.setRenderedDocumentRet(renderedDocumentRet);
    }
    
    /**
     * {@inheritDoc}
     */
    public String getTranslationUrlArg() {
        return args.getTranslationUrlArg();
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslationUrlArg(String translationUrlArg) {
        args.setTranslationUrlArg(translationUrlArg);
    }
}
