package com.ail.pageflow;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletRequest;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.servlet.http.HttpServletRequest;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.acceptance.PremiumCollectionExecutionService.PremiumCollectionExecutionCommand;
import com.ail.insurance.acceptance.PremiumCollectionRequestService.PremiumCollectionRequestCommand;
import com.ail.insurance.acceptance.PremiumCollectionCancelledService.PremiumCollectionCancelledCommand;
import com.ail.insurance.acceptance.PremiumCollectionAuthorisedService.PremiumCollectionConfirmationCommand;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.util.Functions;
import com.ail.pageflow.util.PageFlowContext;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.util.PortalUtil;

public class BuyNowWithPayPalButtonAction extends CommandButtonAction {
    private static final String PAYPAL_RESULT_PARAMETER_NAME = "paypal-result";
    private static final String PAYPAL_APPROVED = "approved";
    private static final String PAYPAL_CANCELLED = "cancelled";

    private String destinationOnSuccessPageId;
    private String destinationOnFailurePageId;

    public BuyNowWithPayPalButtonAction() {
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        String op = Functions.getOperationParameters(request).getProperty("op");

        if ("buy-now-with-paypal".equals(op)) {
            initiatePayment(response);
        } else if (op == null) {
            String paypalResult = request.getParameter(PAYPAL_RESULT_PARAMETER_NAME);
            if (PAYPAL_APPROVED.equals(paypalResult)) {
                try {
                    recordPaymentApproval();
                    executePayment();
                    PageFlowContext.getPageFlow().setNextPage(destinationOnSuccessPageId);
                } catch (BaseException e) {
                    PageFlowContext.getCoreProxy().logError("Payment was requested and authorised by the user, but payment execution failed.", e);
                    PageFlowContext.getPageFlow().setNextPage(destinationOnFailurePageId);
                }
            } else if (PAYPAL_CANCELLED.equals(paypalResult)) {
                recordPaymentCancelled();
                PageFlowContext.getPageFlow().setNextPage(destinationOnFailurePageId);
            }
        }

        return model;
    }

    /**
     * Invoke the CollectPremium service which (by virtue of the MoneyProvisions
     * within the policy - {@link #addPaymentDetailsToPolicy()}) will in turn
     * send PayPal a payment request using their REST API. Send a redirect to
     * the user's browser to take them to the PayPal login page.
     */
    private void initiatePayment(ActionResponse response) throws RenderingError {
        CoreProxy core = PageFlowContext.getCoreProxy();
        Policy policy = PageFlowContext.getPolicy();

        try {
            StringBuffer approvedURL = new StringBuffer(), cancelledURL = new StringBuffer();
            generateResultForwardingURLs(approvedURL, cancelledURL);

            PremiumCollectionRequestCommand cpc = core.newCommand(PremiumCollectionRequestCommand.class);
            cpc.setPolicyArgRet(policy);
            cpc.setApprovedURLArg(new URL(approvedURL.toString()));
            cpc.setCancelledURLArg(new URL(cancelledURL.toString()));
            cpc.invoke();

            response.sendRedirect(cpc.getAuthorisationURLRet().toString());
        } catch (Exception e) {
            throw new RenderingError("Failed to forward integration with PayPal.", e);
        }
    }

    private void executePayment() throws BaseException {
        CoreProxy core = PageFlowContext.getCoreProxy();
        Policy policy = PageFlowContext.getPolicy();

        PremiumCollectionExecutionCommand pcec = core.newCommand(PremiumCollectionExecutionCommand.class);
        pcec.setPolicyArgRet(policy);
        pcec.setPayerIdArg(fetchPayerIdFromRequest());
        pcec.invoke();
    }

    private String fetchPayerIdFromRequest() {
        PortletRequest request = PageFlowContext.getRequest();

        HttpServletRequest servletRequest=PortalUtil.getHttpServletRequest(request);
        
        return servletRequest.getParameter("PayerID");
    }

    private void recordPaymentCancelled() {
        CoreProxy core = PageFlowContext.getCoreProxy();

        try {
            PremiumCollectionCancelledCommand pcc;
            pcc = core.newCommand(PremiumCollectionCancelledCommand.class);
            pcc.setPolicyArgRet(PageFlowContext.getPolicy());
            pcc.invoke();
        } catch (BaseException e) {
            core.logError("Payment has been sucessfully cancelled, but the policy record could not be updated.", e);
            PageFlowContext.getPageFlow().setNextPage(destinationOnFailurePageId);
        }
    }

    private void recordPaymentApproval() throws BaseException {
        CoreProxy core = PageFlowContext.getCoreProxy();

        PremiumCollectionConfirmationCommand pcc;
        pcc = core.newCommand(PremiumCollectionConfirmationCommand.class);
        pcc.setPolicyArgRet(PageFlowContext.getPolicy());
        pcc.invoke();
    }

    /**
     * Generate forwarding URLs for PayPal to use once it's processes are
     * complete. Two URLs are needed; one for PayPal to use if the payment
     * succeeds; and, a second to use if it fails. Both will point back to the
     * current page (ensuring that this widget will be invoked. A parameter is
     * added to the end of each URL to indicate the type of outcome.
     */
    private void generateResultForwardingURLs(StringBuffer approved, StringBuffer cancelled) throws MalformedURLException, PortalException, SystemException {
        PortletRequest portletRequest = PageFlowContext.getRequest();

        HttpServletRequest httpRequest = PortalUtil.getHttpServletRequest(portletRequest);

        String currentCompleteURL = PortalUtil.getCurrentCompleteURL(httpRequest);

        approved.append(currentCompleteURL + '&' + PAYPAL_RESULT_PARAMETER_NAME + '=' + PAYPAL_APPROVED);
        cancelled.append(currentCompleteURL + '&' + PAYPAL_RESULT_PARAMETER_NAME + '=' + PAYPAL_CANCELLED);
    }

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand("BuyNowWithPayPalButtonCommand", request, response, model);
    }

    public String getDestinationOnSuccessPageId() {
        return destinationOnSuccessPageId;
    }

    public void setDestinationOnSuccessPageId(String destinationOnSuccessPageId) {
        this.destinationOnSuccessPageId = destinationOnSuccessPageId;
    }

    public String getDestinationOnFailurePageId() {
        return destinationOnFailurePageId;
    }

    public void setDestinationOnFailurePageId(String destinationOnFailurePageId) {
        this.destinationOnFailurePageId = destinationOnFailurePageId;
    }
}
