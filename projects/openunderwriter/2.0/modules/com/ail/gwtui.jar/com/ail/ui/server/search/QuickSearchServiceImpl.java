/* Copyright Applied Industrial Logic Limited 2014. All rights Reserved */
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
package com.ail.ui.server.search;

import java.util.List;

import com.ail.core.CoreProxy;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.ui.client.search.QuickSearchService;
import com.ail.ui.server.UIUtil;
import com.ail.ui.server.transformer.PolicyDetailTransformer;
import com.ail.ui.shared.ServiceException;
import com.ail.ui.shared.model.PolicyDetailDTO;
import com.ail.ui.shared.validation.FieldVerifier;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * The server-side implementation of the Quick Search RPC service.
 */
@SuppressWarnings("serial")
public class QuickSearchServiceImpl extends RemoteServiceServlet implements QuickSearchService {

    /**
     * Servlet request called by client
     * 
     * @param search Search string
     * @return Populated PolicyDetailDTO
     */
	public PolicyDetailDTO quickSearchServer(String search) throws ServiceException {

	    if (!FieldVerifier.hasLength(search)) {
			throw new ServiceException(
			        this.getClass().getName() + ":Invalid seach input", new IllegalArgumentException());
		}

		search = UIUtil.escapeHtml(search);
		
		CoreProxy core =  new CoreProxy();
        List<?> policies = core.query("get.savedPolicy.by.quotationNumber", search);
        if (policies.size() == 0) {
            policies = core.query("get.savedPolicy.by.policyNumber", search);
        }
            
        if (policies.size() != 0) {
            for(Object policy: policies) {
                try {
                    return new PolicyDetailTransformer().apply((SavedPolicy)policy);
                } catch (Exception e) {
                    core.logError(this.getClass().getName() , e);
                    throw new ServiceException(e);
                }
            }
        } 
        
        return new PolicyDetailDTO();
        
	}
	
}
