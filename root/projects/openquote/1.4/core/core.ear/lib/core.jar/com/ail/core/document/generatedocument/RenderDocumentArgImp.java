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
import com.ail.core.command.CommandArgImp;

public class RenderDocumentArgImp extends CommandArgImp implements RenderDocumentArg {
    private byte[] renderedDocumentRet;
    private XMLString sourceDataArg;
    private String renderOptionsArg;
    private String translationUrlArg;
    private String templateUrlArg;
    
    /**
     * {@inheritDoc}
     */
    public byte[] getRenderedDocumentRet() {
        return renderedDocumentRet;
    }

    /**
     * {@inheritDoc}
     */
    public void setRenderedDocumentRet(byte[] renderedDocumentRet) {
        this.renderedDocumentRet=renderedDocumentRet;
    }

    public String getRenderedDocumentRetAsString() {
        return new String(getRenderedDocumentRet());
    }

    public void setRenderedDocumentRetAsString(String renderedDocumentRetAsString) {
        setRenderedDocumentRet(renderedDocumentRetAsString.getBytes());
    }
    
    /**
     * {@inheritDoc}
     */
    public XMLString getSourceDataArg() {
        return sourceDataArg;
    }

    /**
     * {@inheritDoc}
     */
    public void setSourceDataArg(XMLString sourceDataArg) {
        this.sourceDataArg=sourceDataArg;
    }

    /**
     * {@inheritDoc}
     */
    public String getRenderOptionsArg() {
        return renderOptionsArg;
    }

    /**
     * {@inheritDoc}
     */
    public void setRenderOptionsArg(String renderOptionsArg) {
        this.renderOptionsArg=renderOptionsArg;
    }

    /**
     * {@inheritDoc}
     */
    public String getTranslationUrlArg() {
        return translationUrlArg;
    }

    /**
     * {@inheritDoc}
     */
    public void setTranslationUrlArg(String translationUrlArg) {
        this.translationUrlArg = translationUrlArg;
    }

    /**
     * {@inheritDoc}
     */
    public String getTemplateUrlArg() {
        return templateUrlArg;
    }

    /**
     * {@inheritDoc}
     */
    public void setTemplateUrlArg(String templateUrlArg) {
        this.templateUrlArg = templateUrlArg;
    }
}


