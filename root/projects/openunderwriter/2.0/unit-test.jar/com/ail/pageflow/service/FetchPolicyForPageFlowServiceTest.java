package com.ail.pageflow.service;

import static com.ail.pageflow.PageFlowContext.SessionAttributes.POLICY_ATTRIBUTE;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Locale;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.PreconditionException;
import com.ail.core.ThreadLocale;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.ExecutePageActionService;
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.PageFlowContext;
import com.ail.pageflow.service.FetchPolicyForPageFlowService;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PageFlowContext.class)
public class FetchPolicyForPageFlowServiceTest {
    private static final String TEST_PRODUCT_NAME = "TEST PRODUCT";
    private static final String TEST_START_PAGE = "TEST_PAGE";

    FetchPolicyForPageFlowService sut;
    Locale locale=Locale.CANADA;

    @Mock
    ExecutePageActionService.ExecutePageActionArgument argument;
    @Mock
    CoreProxy coreProxy;
    @Mock
    PortletRequest portletRequest;
    @Mock
    PortletSession portletSession;
    @Mock
    Policy policy;
    @Mock
    PageFlow pageflow;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(PageFlowContext.class);

        sut = new FetchPolicyForPageFlowService();
        sut.setArgs(argument);

        doReturn(portletSession).when(argument).getPortletSessionArg();
        doReturn(portletRequest).when(argument).getPortletRequestArg();
        doReturn(policy).when(portletSession).getAttribute(eq(POLICY_ATTRIBUTE));
        doReturn(TEST_START_PAGE).when(pageflow).getStartPage();
        doReturn(locale).when(portletRequest).getLocale();
        
        when(PageFlowContext.getCoreProxy()).thenReturn(coreProxy);
        when(PageFlowContext.getProductName()).thenReturn(TEST_PRODUCT_NAME);
        when(PageFlowContext.getPageFlow()).thenReturn(pageflow);
        when(PageFlowContext.getPolicy()).thenReturn(policy);
    }

    @Test(expected = PreconditionException.class)
    public void ensureNullPortletRequestArgIsTrapped() throws BaseException {
        doReturn(null).when(argument).getPortletRequestArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void ensureNullPortletSessionArgIsTrapped() throws BaseException {
        doReturn(null).when(argument).getPortletSessionArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void ensureNullProductNameArgIsTrapped() throws BaseException {
        when(PageFlowContext.getProductName()).thenReturn(null);
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void ensureZeroLengthProductNameArgIsTrapped() throws BaseException {
        when(PageFlowContext.getProductName()).thenReturn("");
        sut.invoke();
    }

    @Test
    public void ensureThatSessionPolicyIsUsedIfItExists() throws BaseException {
        sut.invoke();
        verify(coreProxy, never()).newProductType(eq(TEST_PRODUCT_NAME), eq("Policy"));
        verify(policy, times(1)).setLocale(any(ThreadLocale.class));
        PowerMockito.verifyStatic();
        PageFlowContext.setPolicy(eq(policy));
    }

    @Test
    public void testModelArgRetIsPopulated() throws BaseException {
        sut.invoke();
        verify(argument).setModelArgRet(eq(policy));
    }
}
