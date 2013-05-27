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

package com.ail.pageflow.util;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.pageflow.PageFlow;

@ServiceImplementation
public class SelectPageflowService extends Service<SelectPageflowService.SelectPageflowArgument> {
    private static final long serialVersionUID = 3198893603833694389L;

    @ServiceArgument
    public interface SelectPageflowArgument extends Argument {
        String getProductNameArg();

        void setProductNameArg(String productNameArg);

        String getPageflowNameArg();

        void setPageflowNameArg(String pageflowNameArg);
    }

    @ServiceCommand(defaultServiceClass = SelectPageflowService.class)
    public interface SelectPageflowCommand extends Command, SelectPageflowArgument {
    }

    @Override
    public void invoke() throws BaseException {
        if (args.getPageflowNameArg() == null || args.getPageflowNameArg().length() == 0) {
            throw new PreconditionException("args.getPageflowNameArg()==null || args.getPageflowNameArg().length()==0");
        }

        if (args.getProductNameArg() == null || args.getProductNameArg().length() == 0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }

        String namespace = productNameToConfigurationNamespace(args.getProductNameArg());

        PageflowContext.setCore(new CoreProxy(namespace));

        PageFlow pageflow = (PageFlow) PageflowContext.getCore().newProductType(args.getPageflowNameArg());
        
        PageflowContext.setPageFlow(pageflow);
    }
}