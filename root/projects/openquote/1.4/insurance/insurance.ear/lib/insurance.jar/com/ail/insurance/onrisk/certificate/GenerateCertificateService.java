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

package com.ail.insurance.onrisk.certificate;

import static com.ail.insurance.policy.PolicyStatus.ON_RISK;

import com.ail.annotation.ServiceImplementation;
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

/**
 * Service to generate an certificate document. This service delegates to the three document
 * generation phase services: Merge, Style and Render. The actual services used in the
 * generation phases depends on the {@link DocumentDefinition} type defined in the product associated
 * with the policy for which a document is being generated. By convention, this type is named "InvoiceDocument".
 */
@ServiceImplementation
public class GenerateCertificateService extends Service<GenerateCertificateArgument> {
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

    @Override
	public void invoke() throws BaseException {
        XMLString subject=null;

        if (args.getPolicyArg()==null) {
            throw new PreconditionException("args.getPolicyArg()==null");
        }

		if (!ON_RISK.equals(args.getPolicyArg().getStatus())) {
            throw new PreconditionException("!ON_RISK.equals(args.getPolicyArg().getStatus())");
        }
        
        if (args.getPolicyArg().getProductTypeId()==null || args.getPolicyArg().getProductTypeId().length()==0) {
            throw new PreconditionException("args.getPolicyArg().getProductTypeId()==null || args.getPolicyArg().getProductTypeId().length()==0");
        }

        DocumentDefinition docDef=(DocumentDefinition)core.newProductType(args.getPolicyArg().getProductTypeId(), "CertificateDocument");
        
        // 1st step: data merge (if configured)
        if (docDef.getMergeCommand()!=null && docDef.getMergeCommand().length()!=0) {
            MergeDataCommand merge=core.newCommand(docDef.getMergeCommand(), MergeDataCommand.class);
            merge.setDocumentDataArg(docDef.getDocumentData());
            merge.setModelArg(args.getPolicyArg());
            merge.invoke();
            subject=merge.getMergedDataRet();
        }

        // 2nd step: apply style (if configured)
        if (docDef.getStyleCommand()!=null && docDef.getStyleCommand().length()!=0) {
            StyleDocumentCommand style=core.newCommand(docDef.getStyleCommand(), StyleDocumentCommand.class);
            style.setMergedDataArg(subject);
            style.invoke();
            subject=style.getStyledDocumentRet();
        }
        
        // 3rd step: render
        RenderDocumentCommand render=core.newCommand(docDef.getRenderCommand(), RenderDocumentCommand.class);
        render.setSourceDataArg(subject);
        render.invoke();
        
        args.setDocumentRet(render.getRenderedDocumentRet());
        
        if (args.getDocumentRet()==null || args.getDocumentRet().length==0) {
            throw new PostconditionException("args.getDocumentRet()==null || args.getDocumentRet().length==0");
        }
    }
}
