/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import com.ail.core.CoreProxy;
import com.ail.core.ExceptionRecord;
import com.ail.core.ThreadLocale;
import com.ail.core.VersionEffectiveDate;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.RenderingError;

/**
 * This class wraps a number of ThreadLocal objects which are initialised at the beginning of each Portal request/response 
 * calls and are available to any code executed within the portal container during the processing of the request/response.
 */
public class PageflowContext {
	private static ThreadLocal<Policy> policy = new ThreadLocal<Policy>();
	private static ThreadLocal<PageFlow> pageFlow = new ThreadLocal<PageFlow>();	
	private static ThreadLocal<PortletRequest> request = new ThreadLocal<PortletRequest>();	
	private static ThreadLocal<CoreProxy> core = new ThreadLocal<CoreProxy>();
	
	/**
	 * Initialize the PageFlowContext for the current thread. 
	 * @param request Initialise the context with respect to this request
	 */
    public static void initialise(PortletRequest request) {
        boolean newPolicy = false;
        Policy policy = null;
    	PageFlow pageFlow = null;
    	PortletSession session=request.getPortletSession();

        CoreProxy core = null;

        try {
        	// get the name of the product we're quoting for. In the sandpit this may be null.
        	String productName=QuotationCommon.productName(request);
        	
        	// if we have a product name, make sure we pick up the product's config
        	core = (productName!=null) ? new CoreProxy(productNameToConfigurationNamespace(productName)) 
        						       : new CoreProxy();
        	
            setCore(core);

            // if there's no policy in the session, create one.
            policy = (Policy)session.getAttribute("policy");
            if (policy == null && productName != null) {
                policy=(Policy)core.newProductType(productName, "Policy");
                policy.setProductTypeId(productName);
                newPolicy=true;
            }
    
            // The request's ThreadLocale could change from one request to the
            // next, if the user switches their browser settings for example, so
            // always use the current settings.
        	if (policy != null) {
        		policy.setLocale(new ThreadLocale(request.getLocale()));
        	}
        	
    		// Set the thread's locale 
        	ThreadLocale.setThreadLocale(request.getLocale());

            // Fetch the appropriate PageFlow object for this session. The
            // 'appropriate' pageflow is the one associated with the product
            // we're quoting for in this session. We get the product's name from
            // the quote.
            // If the quote already has a version effective date then use it; if
            // it has a quote date, use that as the VED; if it doesn't have
            // either (as in the case of a new quote), use the date now.
        	if (productName!=null) {
        	    if (policy.getVersionEffectiveDate() != null) {
        	        core.setVersionEffectiveDate(policy.getVersionEffectiveDate());
        	    }
        	    else if (policy.getQuotationDate() != null) {
        	        core.setVersionEffectiveDate(new VersionEffectiveDate(policy.getQuotationDate()));
        	    }
        	    else {
        	        core.setVersionEffectiveDateToNow();
        	    }

        	    pageFlow=(PageFlow)core.newProductType(productName, "QuotationPageFlow");

	            pageFlow.applyElementId("OQ0");
        	}
        	
            // if the policy was just create and the PageFlow defines the page
            // to start on, use it. Otherwise we rely on the setting defined in
            // the policy in the product definition.
        	if (newPolicy &&  pageFlow.getStartPage()!=null) {
                policy.setPage(pageFlow.getStartPage());
        	}
        }
        catch(Error e) {
        	if (policy!=null) {
        		policy.addException(new ExceptionRecord(e));
        	}
        	else {
        		throw new RenderingError("Failed to initialise policy instance for product: '"+QuotationCommon.productName(request)+"'", e);
        	}
        }
        
        // initialize the thread context
		setRequest(request);
		setPolicy(policy);
		setPageFlow(pageFlow);
    }
    
	/**
	 * Fetch the policy currently being processed but this thread.
	 * @return current quote, or null if none is defined
	 */
	public static Policy getPolicy() {
		return policy.get();
	}
	
	/**
	 * Set the policy currently being processed.
	 * @param policyArg
	 */
	public static void setPolicy(Policy policyArg) {
		policy.set(policyArg);
		if (request!=null && request.get()!=null) {
			request.get().getPortletSession().setAttribute("policy", policyArg);
		}
	}
	
	public static CoreProxy getCore() {
		return core.get();
	}
	
	public static void setCore(CoreProxy coreArg) {
		core.set(coreArg);
	}
	
	/**
	 * Fetch the request which is currently being processed.
	 * @return Current request.
	 */
	public static PortletRequest getRequest() {
		return request.get();
	}
	
	/**
	 * Set the portlet request currently being processed.
	 * @param requestArg
	 */
	public static void setRequest(PortletRequest requestArg) {
		request.set(requestArg);
	}

	/**
	 * Fetch the request which is currently being processed.
	 * @return Current request.
	 */
	public static PageFlow getPageFlow() {
		return pageFlow.get();
	}
	
	/**
	 * Set the portlet request currently being processed.
	 * @param requestArg
	 */
	public static void setPageFlow(PageFlow pageFlowArg) {
		pageFlow.set(pageFlowArg);
	}
}
