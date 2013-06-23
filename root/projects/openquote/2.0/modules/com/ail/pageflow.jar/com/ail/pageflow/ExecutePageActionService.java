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

package com.ail.pageflow;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Type;
import com.ail.core.command.Argument;
import com.ail.core.command.BeanShellServiceException;
import com.ail.core.command.Command;
import com.ail.pageflow.util.PageFlowContext;

/**
 * TODO Move this service into util
 * @author richarda
 *
 */
@ServiceImplementation
public class ExecutePageActionService extends Service<ExecutePageActionService.ExecutePageActionArgument> {
    private static final long serialVersionUID = 8055804041790420800L;

    @ServiceArgument
    public interface ExecutePageActionArgument extends Argument {

        /**
         * The model property holds the type which will be passed
         * into the service.
         * @return Policy the service will process
         */
        Type getModelArgRet();

        /**
         * @see #getModelArgRet()
         */
        void setModelArgRet(Type modelArgRet);

        /** 
         * The service name property specifies the name of the product service to
         * be executed.
         * @return Name of the service to be executed
         */
        String getServiceNameArg();
        
        /**
         * @see #getServiceNameArg()
         *
         */
        void setServiceNameArg(String serviceNameArg);
        
        /**
         * If the service is performing a validation and the validation fails, it should set this 
         * property to true. For the most part this is only used by commands concerned with cross
         * field validations.
         * @return true if validation has failed, false otherwise
         */
        boolean getValidationFailedRet();
        
        /**
         * @see #setValidationFailedRet(boolean)
         * @param validationFailed
         */
        void setValidationFailedRet(boolean validationFailed);

        /**
         * The Action element from the PageFlow
         * @return Action element
         */
        Action getActionArg();

        /**
         * @see #getActionArg()
         * @param action
         */
        void setActionArg(Action action);

        Map<String, String[]> getRequestParameterArg();

        void setRequestParameterArg(Map<String, String[]> parameters);

        PortletSession getPortletSessionArg();
        
        void setPortletSessionArg(PortletSession portletSession);
        
        PortletPreferences getPortletPreferencesArg();
        
        void setPortletPreferencesArg(PortletPreferences portletPreferencesArg);
        
        PortletRequest getPortletRequestArg();
        
        void setPortletRequestArg(PortletRequest portletRequestArg);
    }

    @ServiceCommand(defaultServiceClass=ExecutePageActionService.class)
    public interface ExecutePageActionCommand extends Command, ExecutePageActionArgument {
    }

    /**
     * All the product services share this namespace.
     * @return The classes namespace
     */
    public String getConfigurationNamespace() {
        return productNameToConfigurationNamespace(PageFlowContext.getProductName());
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws BaseException {
        super.setCore(new Core(args.getCallersCore()));
        
        if (args.getServiceNameArg()==null || args.getServiceNameArg().length()==0) {
            throw new PreconditionException("args.getServiceNameArg()==null || args.getServiceNameArg().length()==0");
        }
        
        try {
            ExecutePageActionCommand c=(ExecutePageActionCommand)getCore().newCommand(args.getServiceNameArg(), ExecutePageActionCommand.class);

            c.setModelArgRet(args.getModelArgRet());
            c.setServiceNameArg(args.getServiceNameArg());
            c.setPortletRequestArg(args.getPortletRequestArg());
            c.setPortletSessionArg(args.getPortletSessionArg());
            c.setRequestParameterArg(args.getRequestParameterArg());
            c.setPortletPreferencesArg(args.getPortletPreferencesArg());
            c.setActionArg(args.getActionArg());
            
            c.invoke();
            
            args.setModelArgRet(c.getModelArgRet());
            args.setValidationFailedRet(c.getValidationFailedRet());
        }
        catch(BeanShellServiceException e) {
            core.logError("Action failed: "+e.getEvalError().getMessage());
        }
    }
}


