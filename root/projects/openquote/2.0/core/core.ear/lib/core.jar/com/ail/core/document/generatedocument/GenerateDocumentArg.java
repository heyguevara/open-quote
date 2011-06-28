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
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/09/15 21:06:57 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/document/generatedocument/GenerateDocumentArg.java,v $
 * @stereotype arg
 */
public interface GenerateDocumentArg extends CommandArg {
    /**
     * Getter for the modelArg property. This is the representation of the dynamic data required to render the document
     * @return Value of modelArg, or null if it is unset
     */
    Type getModelArg();

    /**
     * Setter for the modelArg property. 
     * @see #getModelArg
     * @param modelArg new value for property.
     */
    void setModelArg(Type modelArg);

    /**
     * Getter for the documentDefinition property. Defines the type of document to be rendered
     * @return Value of documentDefinition, or null if it is unset
     */
    String getDocumentDefinitionArg();

    /**
     * Setter for the documentDefinition property.
     * @see #getKeyArg
     * @param documentDefinition new value for property.
     */
    void setDocumentDefinitionArg(String documentDefinition);

    /**
     * Getter returning the name of the product for which the document is to be generated,
     * @return Value of productNameArg, or null if it is unset.
     */
    String getProductNameArg();
    
    /**
     * @see #getProductNameArg()
     * @param productName
     */
    void setProductNameArg(String productNameArg);
    
    /**
     * Getter for the renderedDocumentRet property. The result of the rendering process
     * @return Value of renderedDocumentRet, or null if it is unset
     */
    byte[] getRenderedDocumentRet();

    /**
     * Setter for the renderedDocumentRet property. * @see #getRenderedDocumentRet
     * @param renderedDocumentRet new value for property.
     */
    void setRenderedDocumentRet(byte[] renderedDocumentRet);
}


