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

package com.ail.insurance.quotation.generatedocument;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.XMLString;
import com.ail.core.command.CommandArg;
import com.ail.core.document.generatedocument.MergeDataCommand;
import com.ail.core.document.generatedocument.RenderDocumentCommand;
import com.ail.core.document.generatedocument.StyleDocumentCommand;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.insurance.claim.SectionNotFoundException;
import static com.ail.insurance.policy.PolicyStatus.REFERRED;
import static com.ail.insurance.policy.PolicyStatus.QUOTATION;
import static com.ail.insurance.policy.PolicyStatus.SUBMITTED;

/**
 * Service to generate a quotation document. This service delegates to the three document
 * generation phase services: Merge, Style and Render. The actual services used in the
 * generation phases depends on the {@link DocumentDefinition} type defined in the product associated
 * with the policy for which a document is being generated. By convention, this type is named "QuotationDocument".
 */
public class GenerateDocumentService extends Service {
    private static final long serialVersionUID = 3198893603833694389L;
    private GenerateDocumentArg args=null;
	private Core core=null;

	/**
     * Default constructor
     */
	public GenerateDocumentService() {
		core=new Core(this);
    }

	/**
     * Getter to fetch the entry point's code. This method is demanded by
     * the EntryPoint class.
     * @return This entry point's instance of Core.
     */
	public Core getCore() {
        return core;
    }

	/**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
	public Version getVersion() {
		Version v=(Version)core.newType("Version");
        v.setAuthor("$Author$");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date$");
        v.setSource("$Source$");
        v.setState("$State$");
        v.setVersion("$Revision$");
        return v;
    }

	/**
     * Setter used to the set the entry points arguments. This method will be
     * called before <code>invoke()</code> is called.
     * @param Arguments for invoke
     */
    public void setArgs(CommandArg args){
        this.args = (GenerateDocumentArg)args;
    }

	/**
     * Getter returning the arguments used by this entry point. This entry point
     * doesn't modify the arguments.
     * @return An instance of LoggerArgs.
	 */
    public CommandArg getArgs() {
        return (CommandArg)args;
    }

    /**
     * Return the product name from the arguments as the configuration namespace. 
     * The has the effect of selecting the product's configuration.
     * @return product name
     */
    public String getConfigurationNamespace() {
        return Functions.productNameToConfigurationNamespace(args.getPolicyArg().getProductTypeId());
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     * @throws SectionNotFoundException If one of the sections identified in the
     */
	public void invoke() throws BaseException {
        XMLString subject=null;

        if (args.getPolicyArg()==null) {
            throw new PreconditionException("args.getPolicyArg()==null");
        }

		if (!QUOTATION.equals(args.getPolicyArg().getStatus()) && !REFERRED.equals(args.getPolicyArg().getStatus()) && !SUBMITTED.equals(args.getPolicyArg().getStatus())) {
            throw new PreconditionException("!QUOTATION.equals(args.getPolicyArg().getStatus()) && !REFERRED.equals(args.getPolicyArg().getStatus()) &&  && !SUBMITTED.equals(args.getPolicyArg().getStatus())");
        }
        
        if (args.getPolicyArg().getProductTypeId()==null || args.getPolicyArg().getProductTypeId().length()==0) {
            throw new PreconditionException("args.getPolicyArg().getProductTypeId()==null || args.getPolicyArg().getProductTypeId().length()==0");
        }

        DocumentDefinition docDef=(DocumentDefinition)core.newProductType(args.getPolicyArg().getProductTypeId(), "QuotationDocument");
        
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
