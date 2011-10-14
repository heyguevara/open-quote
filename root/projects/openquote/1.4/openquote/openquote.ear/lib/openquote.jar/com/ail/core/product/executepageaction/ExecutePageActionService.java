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

package com.ail.core.product.executepageaction;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.BeanShellServiceException;
import com.ail.core.command.CommandArg;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/10/08 20:12:22 $
 * @source $Source: /home/bob/CVSRepository/projects/openquote/openquote.ear/openquote.jar/com/ail/core/product/executepageaction/ExecutePageActionService.java,v $
 * @stereotype service
 */
public class ExecutePageActionService extends Service {
    private static final long serialVersionUID = 8055804041790420800L;
    private ExecutePageActionArg args = null;
    private Core core = null;

    /** Default constructor */
    public ExecutePageActionService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return new Core(args.getCallersCore());
    }

    /**
     * All the product services share this namespace.
     * @return The classes namespace
     */
    public String getConfigurationNamespace() {
        return productNameToConfigurationNamespace(args.getQuotationArgRet().getProductTypeId());
    }

    /**
     * Reset the component's configuration.
     */
    public void resetConfiguration() {
        super.resetConfigurationByNamespace();
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2006. All rights reserved.");
        v.setDate("$Date: 2007/10/08 20:12:22 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/openquote/openquote.ear/openquote.jar/com/ail/core/product/executepageaction/ExecutePageActionService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.3 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (ExecutePageActionArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of ExecutePageActionArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws BaseException {
        if (args.getQuotationArgRet()==null) {
            throw new PreconditionException("args.getQuotationArgRet()==null");
        }
        
        if (args.getQuotationArgRet().getProductTypeId()==null || args.getQuotationArgRet().getProductTypeId().length()==0) {
            throw new PreconditionException("args.getQuotationArgRet().getProductTypeId()==null || args.getQuotationArgRet().getProductTypeId().length()==0");
        }
        
        if (args.getServiceNameArg()==null || args.getServiceNameArg().length()==0) {
            throw new PreconditionException("args.getServiceNameArg()==null || args.getServiceNameArg().length()==0");
        }
        
        try {
            ExecutePageActionCommand c=(ExecutePageActionCommand)getCore().newCommand(args.getServiceNameArg());
            c.setQuotationArgRet(args.getQuotationArgRet());
            c.setServiceNameArg(args.getServiceNameArg());
            c.setPortletSessionArg(args.getPortletSessionArg());
            c.setActionArg(args.getActionArg());
            c.invoke();
            args.setQuotationArgRet(c.getQuotationArgRet());
            args.setValidationFailedRet(c.getValidationFailedRet());
        }
        catch(BeanShellServiceException e) {
            core.logError("Action failed: "+e.getEvalError().getMessage());
        }
    }
}


