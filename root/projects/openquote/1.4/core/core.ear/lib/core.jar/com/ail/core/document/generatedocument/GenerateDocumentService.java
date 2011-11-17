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

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import com.ail.core.BaseException;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.XMLString;
import com.ail.core.document.model.DocumentDefinition;

public class GenerateDocumentService extends Service<GenerateDocumentArg> {

    /**
     * Return the product name from the arguments as the configuration namespace. 
     * The has the effect of selecting the product's configuration.
     * @return product name
     */
    @Override
    public String getConfigurationNamespace() {
        return productNameToConfigurationNamespace(args.getProductNameArg());
    }

    /** The 'business logic' of the entry point. */
    @Override
    public void invoke() throws BaseException, PreconditionException, PostconditionException {
        XMLString subject=null;
        
        if (args.getModelArg()==null) {
            throw new PreconditionException("args.getModelArg()==null");
        }
        
        if (args.getDocumentDefinitionArg()==null || args.getDocumentDefinitionArg().length()==0) {
            throw new PreconditionException("args.getDocumentDefinitionArg()==null  || args.getDocumentDefinitionArg().length()==0");
        }

        if (args.getProductNameArg()==null || args.getProductNameArg().length()==0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }

        DocumentDefinition docDef=(DocumentDefinition)core.newProductType(args.getProductNameArg(), args.getDocumentDefinitionArg());
        
        // 1st step: data merge (if configured)
        if (docDef.getMergeCommand()!=null) {
            MergeDataCommand merge=(MergeDataCommand)core.newCommand(docDef.getMergeCommand());
            merge.setDocumentDataArg(docDef.getDocumentData());
            merge.setModelArg(args.getModelArg());
            merge.invoke();
            subject=merge.getMergedDataRet();
        }
        else {
            subject=core.toXML(args.getModelArg());
        }
        
        // 2nd step: apply style
        if (docDef.getStyleCommand()!=null) {
            StyleDocumentCommand style=(StyleDocumentCommand)core.newCommand(docDef.getStyleCommand());
            style.setMergedDataArg(subject);
            style.invoke();
            subject=style.getStyledDocumentRet();
        }
        
        // 3rd step: render
        RenderDocumentCommand render=(RenderDocumentCommand)core.newCommand(docDef.getRenderCommand());
        render.setSourceDataArg(subject);
        render.invoke();
        
        args.setRenderedDocumentRet(render.getRenderedDocumentRet());
        
        if (args.getRenderedDocumentRet()==null || args.getRenderedDocumentRet().length==0) {
            throw new PostconditionException("args.getRenderedDocumentRet()==null || args.getRenderedDocumentRet().length==0");
        }
    }
}


