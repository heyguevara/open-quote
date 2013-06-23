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
package com.ail.pageflow.portlet;

import static ch.lambdaj.Lambda.extract;
import static ch.lambdaj.Lambda.on;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.CoreUserImpl;
import com.ail.core.product.ListProductsService.ListProductsCommand;
import com.ail.core.product.ProductDetails;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.RowScroller;
import com.ail.pageflow.service.ListPageFlowsForProductService.ListPageFlowsForProductCommand;
import com.ail.pageflow.service.ListToOptionService.ListToOptionCommand;
import com.ail.pageflow.service.PersistPolicyService.PersistPolicyCommand;
import com.ail.pageflow.util.Functions;
import com.ail.pageflow.util.PageFlowContext;


/**
 * This class defines methods which are common to both the quotation and sandpit
 * Portlets.
 */
public class QuotationCommon {
    
    private ListProductsCommand listProductsCommand;
    private ListToOptionCommand listToOptionCommand;
    private ListPageFlowsForProductCommand listPageFlowsForProductCommand;
    private PersistPolicyCommand persistPolicyCommand;

    QuotationCommon() {
        CoreProxy coreProxy = new CoreProxy();

        listProductsCommand = coreProxy.newCommand(ListProductsCommand.class);
        listPageFlowsForProductCommand = coreProxy.newCommand(ListPageFlowsForProductCommand.class);
        listToOptionCommand = coreProxy.newCommand(ListToOptionCommand.class);
        persistPolicyCommand = coreProxy.newCommand(PersistPolicyCommand.class);
    }

    void processAction(ActionRequest request, ActionResponse response) throws BaseException {
        PageFlow pageFlow = PageFlowContext.getPageFlow();
        Policy policy = PageFlowContext.getPolicy();

        // apply values from the request back into the model
        pageFlow.applyRequestValues(request, response, policy);

        // if the request is immediate, or if the page passes validation...
        if (immediate(request) || !pageFlow.processValidations(request, response, policy)) {
            // ...process the actions in the request (e.g. move to next page)
            pageFlow.processActions(request, response, policy);

            // update the persisted quote
            persistQuote();
        }
    }

    void doView(RenderRequest request, RenderResponse response) throws IOException {
        PageFlow pageFlow = PageFlowContext.getPageFlow();
        Policy policy = PageFlowContext.getPolicy();

        pageFlow.renderResponse(request, response, policy);
    }

    /**
     * Fetch the productTypeID of the policy currently being quoted, or an empty
     * String if no product is currently being quoted.
     * 
     * @return ProductTypeId of the policy being processed, or "" if none is
     *         being processed.
     */
    String currentProduct() {
        if (PageFlowContext.getPolicy() != null && PageFlowContext.getPolicy().getProductTypeId() != null) {
            return PageFlowContext.getPolicy().getProductTypeId();
        } else {
            return "";
        }
    }

    /**
     * Return true if this request is 'immediate'. Immediate requests are used
     * to indicate that the actions associated with the request should be
     * immediately executed - even if the page would fail validation. This is
     * useful, for example, when implementing a '&lt;&lt;Back' button on a page
     * as it will allow the user to move back even if the current page isn't
     * fully filled in yet. It's also used by the {@link RowScroller} to allow
     * the 'Add' and 'Delete' buttons to work even when the contents of the
     * scroller may not be valid.
     * 
     * @return true if the request is immediate, false otherwise.
     */
    boolean immediate(ActionRequest request) {
        String im = Functions.getOperationParameters(request).getProperty("immediate");

        return "true".equals(im);
    }

    /**
     * Persist the current quotation. Take the quotation from the session, and
     * persist it to the database. If a record does not already exist, one is
     * created; if one does exist, it is updated. Because persisting a object
     * may modify it (a systemId may be added, or a serial version incremented),
     * the session's quotation object is replaced.
     * 
     * @param session
     * @throws BaseException 
     */
    void persistQuote() throws BaseException {
        Policy policy = PageFlowContext.getPolicy();

        if (policy != null) {
            policy = persistQuotation(policy);
        }
    }

    Policy persistQuotation(Policy quote) throws BaseException {
        persistPolicyCommand.setPolicyArgRet(quote);
        persistPolicyCommand.invoke();
        return (Policy)persistPolicyCommand.getPolicyArgRet();
    }

    /**
     * Build an HTML select option string listing all the available products.
     * 
     * @param product
     *            Name of the currently
     * @return String of products in option format.
     * @throws BaseException 
     */
    String buildProductSelectOptions(String product) throws BaseException {
        listProductsCommand.invoke();

        List<String> productNames=extract(listProductsCommand.getProductsRet(), on(ProductDetails.class).getName());
        
        listToOptionCommand.setOptionsArg(productNames);
        listToOptionCommand.setSelectedArg(product);
        listToOptionCommand.setUnknownOptionArg("Product?");
        listToOptionCommand.invoke();

        return listToOptionCommand.getOptionMarkupRet();
    }

    /**
     * Builds an HTML select option string listing all the names of PageFlows
     * supported by the named product.
     * 
     * @param product
     *            Product to return PageFlows for
     * @param pageFlowName
     *            The currently selected PageFlow, if any.
     * @return String of page flow names in option format.
     * @throws BaseException 
     */
    String buildPageFlowSelectOptions(String product, String pageFlowName) throws BaseException {
        List<String> pageFlowNames=new ArrayList<String>();

        if (product!=null && product.length()!=0) {
            listPageFlowsForProductCommand.setCallersCore(new CoreUserImpl());
            listPageFlowsForProductCommand.setProductNameArg(product);
            listPageFlowsForProductCommand.invoke();
            pageFlowNames = listPageFlowsForProductCommand.getPageFlowNameRet();
        }
        
        listToOptionCommand.setOptionsArg(pageFlowNames);
        listToOptionCommand.setSelectedArg(pageFlowName);
        listToOptionCommand.setUnknownOptionArg("PageFlow?");
        listToOptionCommand.invoke();

        return listToOptionCommand.getOptionMarkupRet();
    }
}
