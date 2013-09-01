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

package com.ail.pageflow.service;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import java.util.ArrayList;
import java.util.List;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.core.configure.Types;
import com.ail.pageflow.PageFlow;

/**
 * Fetch a list of the PageFlows supported by a named product
 */
@ServiceImplementation
public class ListPageFlowsForProductService extends Service<ListPageFlowsForProductService.ListPageFlowsForProductArgument> {
    public static String CONFIGURATION_NOT_FOUND="getCore().getConfiguration()==null";
    
    @ServiceArgument
    public interface ListPageFlowsForProductArgument extends Argument {
        void setProductNameArg(String productNameArg);

        String getProductNameArg();

        void setPageFlowNameRet(List<String> pageFlowNameRet);

        List<String> getPageFlowNameRet();
    }

    @ServiceCommand(defaultServiceClass = ListPageFlowsForProductService.class)
    public interface ListPageFlowsForProductCommand extends Command, ListPageFlowsForProductArgument {
    }

    @Override
    public String getConfigurationNamespace() {
        return productNameToConfigurationNamespace(args.getProductNameArg());
    }

    @Override
    public void invoke() throws BaseException {
        if (args.getProductNameArg() == null || args.getProductNameArg().length() == 0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }

        if (getCore().getConfiguration() == null) {
            throw new PreconditionException(CONFIGURATION_NOT_FOUND);
        }

        List<String> results = new ArrayList<String>();

        Types types = getCore().getConfiguration().getTypes();

        for (int i = 0; i < types.getTypeCount(); i++) {
            if (PageFlow.class.getName().equals(types.getType(i).getKey())) {
                results.add(types.getType(i).getName());
            }
        }

        args.setPageFlowNameRet(results);

        if (args.getPageFlowNameRet() == null) {
            throw new PostconditionException("args.getProductNameArg()==null");
        }
    }
}