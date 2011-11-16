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
import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.XMLString;
import com.ail.core.command.CommandArg;
import com.ail.core.document.model.DocumentDefinition;
/**
 * @version $Revision: 1.6 $
 * @state $State: Exp $
 * @date $Date: 2007/06/06 20:54:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/document/generatedocument/GenerateDocumentService.java,v $
 * @stereotype service
 */
public class GenerateDocumentService extends Service {
    private GenerateDocumentArg args = null;
    private Core core = null;

    /** Default constructor */
    public GenerateDocumentService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
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
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2007/06/06 20:54:26 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/document/generatedocument/GenerateDocumentService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.6 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (GenerateDocumentArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of GenerateDocumentArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Return the product name from the arguments as the configuration namespace. 
     * The has the effect of selecting the product's configuration.
     * @return product name
     */
    public String getConfigurationNamespace() {
        return productNameToConfigurationNamespace(args.getProductNameArg());
    }

    /** The 'business logic' of the entry point. */
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


