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

package com.ail.insurance.onrisk.invoice;

import com.ail.core.BaseException;
import com.ail.core.Functions;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.XMLString;
import com.ail.core.document.generatedocument.MergeDataCommand;
import com.ail.core.document.generatedocument.RenderDocumentCommand;
import com.ail.core.document.generatedocument.StyleDocumentCommand;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.insurance.claim.SectionNotFoundException;

/**
 * Service to generate an invoice document. This service delegates to the three document
 * generation phase services: Merge, Style and Render. The actual services used in the
 * generation phases depends on the {@link DocumentDefinition} type defined in the product associated
 * with the policy for which a document is being generated. By convention, this type is named "InvoiceDocument".
 */
public class GenerateInvoiceService extends Service<GenerateInvoiceArg> {
    private static final long serialVersionUID = 3198893603833694389L;

    /**
     * Return the product name from the arguments as the configuration namespace. 
     * The has the effect of selecting the product's configuration.
     * @return product name
     */
    @Override
    public String getConfigurationNamespace() {
        return Functions.productNameToConfigurationNamespace(args.getPolicyArg().getProductTypeId());
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     * @throws SectionNotFoundException If one of the sections identified in the
     */
    @Override
	public void invoke() throws BaseException {
        XMLString subject=null;

        if (args.getPolicyArg()==null) {
            throw new PreconditionException("args.getPolicyArg()==null");
        }

        if (args.getPolicyArg().getProductTypeId()==null || args.getPolicyArg().getProductTypeId().length()==0) {
            throw new PreconditionException("args.getPolicyArg().getProductTypeId()==null || args.getPolicyArg().getProductTypeId().length()==0");
        }
        
        if (args.getPolicyArg().getTotalPremium()==null) { 
            throw new PreconditionException("args.getPolicyArg().getTotalPremium()==null");
        }
        
        if (args.getPolicyArg().getPolicyHolder()==null) {
            throw new PreconditionException("args.getPolicyArg().getPolicyHolder()==null");
        }
        
        DocumentDefinition docDef=(DocumentDefinition)core.newProductType(args.getPolicyArg().getProductTypeId(), "InvoiceDocument");
        
        // 1st step: data merge (if configured)
        if (docDef.getMergeCommand()!=null && docDef.getMergeCommand().length()!=0) {
            MergeDataCommand merge=(MergeDataCommand)core.newCommand(docDef.getMergeCommand());
            merge.setDocumentDataArg(docDef.getDocumentData());
            merge.setModelArg(args.getPolicyArg());
            merge.invoke();
            subject=merge.getMergedDataRet();
        }

        // 2nd step: apply style (if configured)
        if (docDef.getStyleCommand()!=null && docDef.getStyleCommand().length()!=0) {
            StyleDocumentCommand style=(StyleDocumentCommand)core.newCommand(docDef.getStyleCommand());
            style.setMergedDataArg(subject);
            style.invoke();
            subject=style.getStyledDocumentRet();
        }
        
        // 3rd step: render
        RenderDocumentCommand render=(RenderDocumentCommand)core.newCommand(docDef.getRenderCommand());
        render.setSourceDataArg(subject);
        render.invoke();
        
        args.setDocumentRet(render.getRenderedDocumentRet());
        
        if (args.getDocumentRet()==null || args.getDocumentRet().length==0) {
            throw new PostconditionException("args.getDocumentRet()==null || args.getDocumentRet().length==0");
        }
    }
}
