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

package com.ail.payment.implementation;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.CoreProxy;
import com.ail.core.Functions;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.payment.PaymentRequestService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;
import com.paypal.core.rest.PayPalRESTException;

/**
 * Implementation of the payment request service using PayPal's REST API. This
 * service relies on configuration parameters and service arguments in order to
 * build the PayPal transaction. Once the transaction's details have been passed
 * to PayPal, the service will return a single return parameter indicating the
 * URL that the client (browser) should be forwarded to in order to complete the
 * process.
 */
@ServiceImplementation
public class PayPalPaymentRequestService extends Service<PaymentRequestService.PaymentRequestArgument> {

    @Override
    public void invoke() throws PreconditionException, PostconditionException {
        if (args.getProductTypeIdArg()==null || args.getProductTypeIdArg().length()==0) {
            throw new PreconditionException("args.getProductTypeIdArg()==null || args.getProductTypeIdArg().length()==0");
        }
        
        String namespace = Functions.productNameToConfigurationNamespace(args.getProductTypeIdArg());
        setCore(new CoreProxy(namespace, args.getCallersCore()).getCore());
        
        String mode = core.getParameterValue("PaymentMehtods.PayPal.Mode");
        String clientID = core.getParameterValue("PaymentMehtods.PayPal.ClientID");
        String clientSecret = core.getParameterValue("PaymentMehtods.PayPal.ClientSecret");

        if (mode == null) {
            throw new PreconditionException("core.getParameterValue(\"PaymentMehtods.PayPal.Mode\")==null");
        }

        if (clientID == null) {
            throw new PreconditionException("core.getParameterValue(\"PaymentMehtods.PayPal.ClientID\")==null");
        }

        if (clientSecret == null) {
            throw new PreconditionException("core.getParameterValue(\"PaymentMehtods.PayPal.ClientSecret\")==null");
        }

        if (args.getAmountArg() == null) {
            throw new PreconditionException("args.getAmountArg()==null");
        }

        if (args.getApprovedURLArg() == null) {
            throw new PreconditionException("args.getApprovedURLArg()==null");
        }

        if (args.getCancelledURLArg() == null) {
            throw new PreconditionException("args.getCancelledURLArg()==null");
        }

        if (args.getDescriptionArg() == null) {
            throw new PreconditionException("args.getDescriptionArg()==null");
        }

        String accessToken = fetchPayPalAccessToken(mode, clientID, clientSecret);

        try {
            Payment payment = createPayment(accessToken, mode);

            args.setAuthorisationURLRet(fetchAuthorisationURL(payment));
        } catch (Exception e) {
            throw new PostconditionException("Encountered Exception interfacing with PayPal.", e);
        }

        if (args.getAuthorisationURLRet() == null) {
            throw new PostconditionException("args.getAuthorisationURLRet()==null");
        }
    }

    private URL fetchAuthorisationURL(Payment payment) throws MalformedURLException {
        for (Links link : payment.getLinks()) {
            if (link.getRel().equalsIgnoreCase("approval_url")) {
                return new URL(link.getHref());
            }
        }

        return null;
    }

    private Payment createPayment(String accessToken, String mode) throws PayPalRESTException {
        Map<String, String> sdkConfig = new HashMap<String, String>();
        sdkConfig.put("mode", mode);

        APIContext apiContext = new APIContext(accessToken);
        apiContext.setConfigurationMap(sdkConfig);

        Amount amount = new Amount();
        amount.setCurrency(args.getAmountArg().getCurrencyAsString());
        amount.setTotal(args.getAmountArg().getAmountAsString());

        Transaction transaction = new Transaction();
        transaction.setDescription(args.getDescriptionArg());
        transaction.setAmount(amount);

        List<Transaction> transactions = new ArrayList<Transaction>();
        transactions.add(transaction);

        Payer payer = new Payer();
        payer.setPaymentMethod("paypal");

        Payment payment = new Payment();
        payment.setIntent("sale");
        payment.setPayer(payer);
        payment.setTransactions(transactions);
        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(args.getCancelledURLArg().toExternalForm());
        redirectUrls.setReturnUrl(args.getApprovedURLArg().toExternalForm());
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    private String fetchPayPalAccessToken(String mode, String clientID, String clientSecret) throws PreconditionException {
        try {
            Map<String, String> sdkConfig = new HashMap<String, String>();

            sdkConfig.put("mode", mode);

            return new OAuthTokenCredential(clientID, clientSecret, sdkConfig).getAccessToken();
        } catch (PayPalRESTException e) {
            throw new PreconditionException("PayPal REST invocation failed: " + e);
        }
    }
}
