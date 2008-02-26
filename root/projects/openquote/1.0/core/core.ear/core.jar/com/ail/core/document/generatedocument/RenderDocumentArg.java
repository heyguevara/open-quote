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
import com.ail.core.command.CommandArg;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 * @stereotype arg
 */
public interface RenderDocumentArg extends CommandArg {
    
    /**
     * Get the XML data which the renderer will use as a source.
     * @return Source data
     */
    XMLString getSourceDataArg();
    
    /**
     * @see #getSourceDataArg()
     * @param sourceDataArg
     */
    void setSourceDataArg(XMLString sourceDataArg);
    
    /**
     * Get the rendered document. This document is the result of the rendering process - 
     * a fully formed PDF, RTF, or whatever other format of file the implementing 
     * services support.
     * @ret The document resulting from the render process
     */
    byte[] getRenderedDocumentRet();

    /**
     * @see #getRenderedDocumentRet()
     * @param renderedDocumentRet
     */
    void setRenderedDocumentRet(byte[] renderedDocumentRet);
    
    /**
     * Get the list of render service specific options which will applied during the
     * render operation. See the javadocs associated with each type of render service
     * for a list of the options supported, and a description of how the list is
     * interpreted.
     * @return Comma separated list of options
     */
    String getRenderOptionsArg();
    
    /**
     * @see #getRenderOptionsArg()
     * @param renderOptionsArg
     */
    void setRenderOptionsArg(String renderOptionsArg);
    
    /**
     * Set the translation (if any) to be applied to the document pre-render. The interpretation
     * of this argument is dependent on the render service implementation. See the javadocs associated
     * with each render service for details of this option's usage.
     * @param translationUrlArg Translation URL
     */
    void setTranslationUrlArg(String translationUrlArg);
    
    /**
     * @see #setTranslationUrlArg(String)
     * @return translationUrlArg
     */
    String getTranslationUrlArg();
}


