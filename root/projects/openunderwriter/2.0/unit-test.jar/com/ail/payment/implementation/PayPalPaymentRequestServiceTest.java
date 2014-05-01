package com.ail.payment.implementation;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import java.net.URL;
import java.util.Arrays;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentMatcher;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ail.core.Core;
import com.ail.core.CoreProxy;
import com.ail.core.CoreUser;
import com.ail.core.Functions;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.configure.Parameter;
import com.ail.financial.CurrencyAmount;
import com.ail.payment.PaymentRequestService;
import com.paypal.api.payments.Amount;
import com.paypal.api.payments.Links;
import com.paypal.api.payments.Payer;
import com.paypal.api.payments.Payment;
import com.paypal.api.payments.RedirectUrls;
import com.paypal.api.payments.Transaction;
import com.paypal.core.rest.APIContext;
import com.paypal.core.rest.OAuthTokenCredential;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ Functions.class, CoreProxy.class, PayPalPaymentRequestService.class, OAuthTokenCredential.class, URL.class, APIContext.class, Amount.class, Transaction.class, Payer.class, Payment.class, RedirectUrls.class })
public class PayPalPaymentRequestServiceTest {

    private PayPalPaymentRequestService sut;

    @Mock 
    private CoreProxy coreProxy;
    @Mock
    private Core core;
    @Mock
    private OAuthTokenCredential oAuthTokenCredential;
    @Mock
    private Parameter clientId;
    @Mock
    private Parameter clientSecret;
    @Mock
    private PaymentRequestService.PaymentRequestArgument paymentRequestArgument;
    @Mock
    private CurrencyAmount currencyAmount;
    @Mock
    private APIContext apiContext;
    @Mock
    private Amount amount;
    @Mock
    private Transaction transaction;
    @Mock
    private Payer payer;
    @Mock
    private Payment payment;
    @Mock
    private RedirectUrls redirectUrls;
    @Mock
    private Links links;
    @Mock
    private CoreUser coreUser;

    private URL approvedURL;
    private URL cancelledURL;
    private URL authorisationURL;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        sut = new PayPalPaymentRequestService();
        sut.setCore(core);
        sut.setArgs(paymentRequestArgument);
        
        mockStatic(Functions.class);
        when(Functions.productNameToConfigurationNamespace(eq("PRODUCT_TYPE"))).thenReturn("PRODUCT_NAMESPACE");
        
        whenNew(OAuthTokenCredential.class).withArguments(eq("CLIENT_ID"), eq("CLIENT_SECRET"), any(Map.class)).thenReturn(oAuthTokenCredential);
        whenNew(APIContext.class).withAnyArguments().thenReturn(apiContext);
        whenNew(Amount.class).withAnyArguments().thenReturn(amount);
        whenNew(Transaction.class).withAnyArguments().thenReturn(transaction);
        whenNew(Payer.class).withAnyArguments().thenReturn(payer);
        whenNew(Payment.class).withNoArguments().thenReturn(payment);
        whenNew(RedirectUrls.class).withAnyArguments().thenReturn(redirectUrls);
        whenNew(CoreProxy.class).withArguments(eq("PRODUCT_NAMESPACE"), eq(coreUser)).thenReturn(coreProxy);
 
        approvedURL = PowerMockito.mock(URL.class);
        cancelledURL = PowerMockito.mock(URL.class);
        authorisationURL = PowerMockito.mock(URL.class);

        doReturn(core).when(coreProxy).getCore();
        doReturn(currencyAmount).when(paymentRequestArgument).getAmountArg();
        doReturn("DESCRIPTION").when(paymentRequestArgument).getDescriptionArg();
        doReturn(approvedURL).when(paymentRequestArgument).getApprovedURLArg();
        doReturn(cancelledURL).when(paymentRequestArgument).getCancelledURLArg();
        doReturn(authorisationURL).when(paymentRequestArgument).getAuthorisationURLRet();
        doReturn("PRODUCT_TYPE").when(paymentRequestArgument).getProductTypeIdArg();
        doReturn(coreUser).when(paymentRequestArgument).getCallersCore();

        doReturn("MODE").when(core).getParameterValue(eq("PaymentMehtods.PayPal.Mode"));
        doReturn("CLIENT_ID").when(core).getParameterValue(eq("PaymentMehtods.PayPal.ClientID"));
        doReturn("CLIENT_SECRET").when(core).getParameterValue(eq("PaymentMehtods.PayPal.ClientSecret"));
        doReturn("TOKEN").when(oAuthTokenCredential).getAccessToken();
        doReturn(payment).when(payment).create(eq(apiContext));
        doReturn(redirectUrls).when(payment).getRedirectUrls();
        doReturn(Arrays.asList(links)).when(payment).getLinks();
        doReturn("approval_url").when(links).getRel();
        doReturn("http://localhost:8080").when(links).getHref();
        doReturn("CURRENCY").when(currencyAmount).getCurrencyAsString();
        doReturn("AMOUNT").when(currencyAmount).getAmountAsString();

        when(approvedURL.toExternalForm()).thenReturn("APPROVED_EXTERNAL_URL");
        when(cancelledURL.toExternalForm()).thenReturn("CANCELLED_EXTERNAL_URL");

    }

    @Test(expected = PreconditionException.class)
    public void testProductTypeIdNotSupplied() throws PreconditionException, PostconditionException {
        doReturn(null).when(paymentRequestArgument).getProductTypeIdArg();
        sut.invoke();
    }
    
    @Test(expected = PreconditionException.class)
    public void testClientIdNotConfigured() throws PreconditionException, PostconditionException {
        doReturn(null).when(core).getParameterValue(eq("PaymentMehtods.PayPal.ClientID"));
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void testModeNotConfigured() throws PreconditionException, PostconditionException {
        doReturn(null).when(core).getParameterValue(eq("PaymentMehtods.PayPal.Mode"));
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void testClientSecretNotConfigured() throws PreconditionException, PostconditionException {
        doReturn(null).when(core).getParameterValue(eq("PaymentMehtods.PayPal.ClientSecret"));
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void checkThatNullCurrencyAmountArgIsChecked() throws PreconditionException, PostconditionException {
        doReturn(null).when(paymentRequestArgument).getAmountArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void ensureApprovedURLArgIsChecked() throws PreconditionException, PostconditionException {
        doReturn(null).when(paymentRequestArgument).getApprovedURLArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void ensureCancelledURLArgIsChecked() throws PreconditionException, PostconditionException {
        doReturn(null).when(paymentRequestArgument).getCancelledURLArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void ensureDescriptionArgIsChecked() throws PreconditionException, PostconditionException {
        doReturn(null).when(paymentRequestArgument).getDescriptionArg();
        sut.invoke();
    }

    @Test
    public void ensureCurrencyAmountIsPassedToPayPal() throws PreconditionException, PostconditionException {
        sut.invoke();
        verify(amount).setCurrency(eq("CURRENCY"));
        verify(amount).setTotal(eq("AMOUNT"));
    }

    @Test
    public void verifyThatApproveURLIsFoundInPaypalPayment() throws PreconditionException, PostconditionException {
        sut.invoke();
        verify(links).getHref();
    }

    @Test(expected = PostconditionException.class)
    public void verifyThatPostconditionCheckCatchesNullURL() throws PreconditionException, PostconditionException {
        doReturn(null).when(paymentRequestArgument).getAuthorisationURLRet();
        sut.invoke();
    }

    @Test
    public void verifyThatForwardingURLArePassedToPayPal() throws PreconditionException, PostconditionException {
        sut.invoke();
        verify(redirectUrls).setCancelUrl(eq("CANCELLED_EXTERNAL_URL"));
        verify(redirectUrls).setReturnUrl(eq("APPROVED_EXTERNAL_URL"));
    }

    @Test
    public void verifyThatDescriptionIsPassedToPayPal() throws PreconditionException, PostconditionException {
        sut.invoke();
        verify(transaction).setDescription(eq("DESCRIPTION"));
    }

    @Test
    public void verifyThatTransactionModeIsPassedToPayPal() throws PreconditionException, PostconditionException {
        sut.invoke();
        verify(apiContext).setConfigurationMap(mapContains("mode", "MODE"));
    }

    private Map<String, String> mapContains(String key, String value) {
        return argThat(new MapContains(key, value));
    }

    class MapContains extends ArgumentMatcher<Map<String,String>>{
        private String key;
        private String value;

        public MapContains(String key, String value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean matches(Object argument) {
            @SuppressWarnings("unchecked")
            Map<String,String> arg=(Map<String,String>)argument;
            return (arg.containsKey(key) && value.equals(arg.get(key)));
        }
    }
}
