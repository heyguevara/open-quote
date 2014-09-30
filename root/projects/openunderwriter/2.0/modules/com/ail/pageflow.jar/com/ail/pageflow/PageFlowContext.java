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
package com.ail.pageflow;

import javax.portlet.PortletRequest;
import javax.portlet.PortletResponse;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.ExceptionRecord;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.service.InitialisePageFlowContextService.InitialisePageFlowContextCommand;

/**
 * This class wraps a number of ThreadLocal objects which are initialised at the
 * beginning of each Portal request/response calls and are available to any code
 * executed within the portal container during the processing of the
 * request/response.
 */
public class PageFlowContext {
    private static ThreadLocal<PortletRequest> request = new ThreadLocal<PortletRequest>();
    private static ThreadLocal<PortletResponse> response = new ThreadLocal<PortletResponse>();
    private static ThreadLocal<CoreProxy> coreProxy = new ThreadLocal<CoreProxy>();
    private static ThreadLocal<Policy> policy = new ThreadLocal<Policy>();
    private static ThreadLocal<StringBuffer> productName = new ThreadLocal<StringBuffer>();

    /**
     * Initialize the PageFlowContext for the current thread.
     * 
     * @param request
     *            Initialise the context with respect to this request
     * @throws BaseException
     */
    public static void initialise(PortletRequest request, PortletResponse response) {
        try {
            CoreProxy coreProxy = new CoreProxy();
            InitialisePageFlowContextCommand command = coreProxy.newCommand(InitialisePageFlowContextCommand.class);
            command.setPortletRequestArg(request);
            command.setPortletResponseArg(response);
            command.invoke();
        } catch (Exception e) {
            if (getPolicy() != null) {
                getPolicy().addException(new ExceptionRecord(e));
            } else {
                throw new RenderingError("Failed to initialise PageFlowContext: " + e, e);
            }
        }
    }
    
    public static void destroy() {
        request.remove();
        response.remove();
        coreProxy.remove();
        policy.remove();
        productName.remove();
    }
    
    public static Policy getPolicy() {
        return policy.get();
    }

    public static void setPolicy(Policy policyArg) {
        policy.set(policyArg);
        setPolicySystemId(policyArg != null ? policyArg.getSystemId() : null);
    }

    public static void setPolicySystemId(Long policySystemId) {
        SessionWrapper.setPolicySystemId(policySystemId);
    }
    
    public static Long getPolicySystemId() {
        return SessionWrapper.getPolicySystemId();
    }
    
    public static CoreProxy getCoreProxy() {
        return coreProxy.get();
    }

    public static void setCoreProxy(CoreProxy coreProxyArg) {
        coreProxy.set(coreProxyArg);
    }

    public static PortletRequest getRequest() {
        return request.get();
    }

    public static void setRequest(PortletRequest requestArg) {
        request.set(requestArg);
    }

    public static PortletResponse getResponse() {
        return response.get();
    }

    public static void setResponse(PortletResponse responseArg) {
        response.set(responseArg);
    }

    public static PageFlow getPageFlow() {
        return SessionWrapper.getPageFlow();
    }

    public static void setPageFlow(PageFlow pageFlowArg) {
        SessionWrapper.setPageFlow(pageFlowArg);
    }

    /**
     * Get the pageflow name (if any) associated with this pageflow. In the
     * course of a normal pageflow (i.e. one started from the PageflowPortlet),
     * this will always return a name, but the for pageflow running in the
     * SandpitPortlet it may return null.
     * 
     * @return pageflow name, or null.
     */
    public static String getPageFlowName() {
        return SessionWrapper.getPageFlowName();
    }

    /**
     * @see #getPageFlowName()
     * @param pageFlowName
     */
    public static void setPageFlowName(String pageFlowNameArg) {
        SessionWrapper.setPageFlowName(pageFlowNameArg);
    }

    /**
     * Get the product name (if any) associated with this pageflow. In the
     * course of a normal pageflow (i.e. one started from the PageflowPortlet),
     * this will always return a name, but the for pageflow running in the
     * SandpitPortlet it may return null.
     * 
     * @return product name, or null.
     */
    public static String getProductName() {
        if (SessionWrapper.getProductName() != null) {
            return SessionWrapper.getProductName();
        }
        else if (productName.get()!=null) {
            return productName.get().toString();
        }
        else {
            return null;
        }
    }

    /**
     * @see #getProductName()
     * @param productName
     */
    public static void setProductName(String productNameArg) {
        SessionWrapper.setProductName(productNameArg);
        productName.set(productNameArg==null ? null : new StringBuffer(productNameArg));
    }

    public static String getCurrentPageName() {
        return SessionWrapper.getCurrentPageName();
    }
    
    public static void setCurrentPageName(String pageName) {
        SessionWrapper.setCurrentPageName(pageName);
    }
    
    public static String getNextPageName() {
        return SessionWrapper.getNextPageName();
    }
    
    public static void setNextPageName(String pageName) {
        SessionWrapper.setNextPageName(pageName);
    }

    public static void selectProductName(String productName) {
        clear();
        setProductName(productName);
    }
    
    public static void selectPageFlowName(String pageFlow) {
        String productName=getProductName();
        clear();
        setProductName(productName);
        setPageFlowName(pageFlow);
    }
    
    /**
     * Reset the Context clearing the current state and moving the flow back to
     * the start page. This clears the state of the current PageFlow, but the
     * context still remains tied to that PageFlow. From the user's perspective
     * this will take them back to the first page with fresh state (i.e. any
     * data they have entered will not be retained).
     */
    public static void restart() {
        setPolicy(null);
        SessionWrapper.setCurrentPageName(null);
        SessionWrapper.setNextPageName(null);
    }
    
    private static void clear() {
        restart();
        SessionWrapper.setProductName(null);
        SessionWrapper.setPageFlowName(null);
        SessionWrapper.setPageFlow(null);
    }

    /**
     * This class encapsulates access to the portlet session attributes used by the pageflow context.
     * Each attribute has it's own getter/setter method pair. Having them here helps keep the calling
     * code a little cleaner and also centralises the names used for each one.
     */
    public static class SessionWrapper {
        public static final String PRODUCT_NAME_SESSION_ATTRIBUTE = "product";
        public static final String PAGEFLOW_NAME_SESSION_ATTRIBUTE = "pageflow-name";
        public static final String CURRENT_PAGE_NAME_ATTRIBUTE = "current-page";
        public static final String NEXT_PAGE_NAME_ATTRIBUTE = "next-page";
        public static final String PAGE_FLOW_ATTRIBUTE = "pageflow";
        public static final String POLICY_SYSTEM_ID_ATTRIBUTE = "policy-system-id";
        
        @SuppressWarnings("unchecked")
        private static <T extends Object> T get(String name, Class<T> clazz) {
            if (PageFlowContext.getRequest()!=null) {
                try {
                    return (T)PageFlowContext.getRequest().getPortletSession().getAttribute(name);
                }
                catch(NullPointerException e) {
                    new CoreProxy().logInfo("Failed to fetch attribute: '"+name+"' from PageContext. Continuing with partial PageFlowContext.");
                }
            }
            
            return null;
        }
        
        private static void set(String name, Object value) {
            if (PageFlowContext.getRequest()!=null) {
                try {
                    PageFlowContext.getRequest().getPortletSession().setAttribute(name, value);
                }
                catch(NullPointerException e) {
                    new CoreProxy().logInfo("Failed to fetch attribute: '"+name+"' from PageContext. Continuing with partial PageFlowContext.");
                }
            }
        }
        
        private static String getPageFlowName() {
            return get(PAGEFLOW_NAME_SESSION_ATTRIBUTE, String.class);
        }
        
        private static void setPageFlowName(String pageFlow) {
            set(PAGEFLOW_NAME_SESSION_ATTRIBUTE, pageFlow);
        }

        private static String getProductName() {
            return get(PRODUCT_NAME_SESSION_ATTRIBUTE, String.class);
        }
        
        private static void setProductName(String productName) {
            set(PRODUCT_NAME_SESSION_ATTRIBUTE, productName);
        }
        
        public static String getCurrentPageName() {
            return get(CURRENT_PAGE_NAME_ATTRIBUTE, String.class);
        }
        
        private static void setCurrentPageName(String pageName) {
            set(CURRENT_PAGE_NAME_ATTRIBUTE, pageName);
        }

        private static String getNextPageName() {
            return get(NEXT_PAGE_NAME_ATTRIBUTE, String.class);
        }
        
        private static void setNextPageName(String pageName) {
            set(NEXT_PAGE_NAME_ATTRIBUTE, pageName);
        }
        
        private static PageFlow getPageFlow() {
            return get(PAGE_FLOW_ATTRIBUTE, PageFlow.class);
        }
        
        private static void setPageFlow(PageFlow pageFlow) {
            set(PAGE_FLOW_ATTRIBUTE, pageFlow);
        }

        private static Long getPolicySystemId() {
            return get(POLICY_SYSTEM_ID_ATTRIBUTE, Long.class);
        }
        
        private static void setPolicySystemId(Long policySystemId) {
            set(POLICY_SYSTEM_ID_ATTRIBUTE, policySystemId);
        }
    }
}
