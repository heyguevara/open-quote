package com.ail.pageflow.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.insurance.policy.Policy;
import com.ail.pageflow.ExecutePageActionService;
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.PageFlowContext;

@RunWith(PowerMockRunner.class)
@PrepareForTest(PageFlowContext.class)
public class CreateNewBusinessQuotationServiceTest {
    private static final String TEST_PRODUCT_NAME = "TEST PRODUCT";
    private static final String TEST_START_PAGE = "TEST_PAGE";

    CreateNewBusinessQuotationService sut;

    @Mock
    ExecutePageActionService.ExecutePageActionArgument argument;
    @Mock
    PortletRequest portletRequest;
    @Mock
    PortletSession portletSession;
    @Mock
    CoreProxy coreProxy;
    @Mock
    Policy policy;
    @Mock
    PageFlow pageflow;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        PowerMockito.mockStatic(PageFlowContext.class);

        sut = spy(new CreateNewBusinessQuotationService());
        sut.setArgs(argument);
        
        doReturn(portletRequest).when(argument).getPortletRequestArg();
        doReturn(portletSession).when(argument).getPortletSessionArg();
        doReturn(TEST_START_PAGE).when(pageflow).getStartPage();
        doReturn(policy).when(coreProxy).newProductType(eq(TEST_PRODUCT_NAME), eq("Policy"));
        doReturn((Policy)null).when(sut).getPolicyFromPageFlowContext();
        
        when(PageFlowContext.getPageFlow()).thenReturn(pageflow);
        when(PageFlowContext.getProductName()).thenReturn(TEST_PRODUCT_NAME);
        when(PageFlowContext.getCoreProxy()).thenReturn(coreProxy);
        when(PageFlowContext.getPolicy()).thenReturn(policy);
    }

    @Test(expected=PreconditionException.class)
    public void ensureNullPortletRequestArgErrorIsTrapped() throws BaseException {
        doReturn(null).when(argument).getPortletRequestArg();
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void ensureNullProductNameInPageflowContextErrorIsTrapped() throws BaseException {
        when(PageFlowContext.getProductName()).thenReturn(null);
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void ensureEmptyStringProductNameInPageflowContextErrorIsTrapped() throws BaseException {
        when(PageFlowContext.getProductName()).thenReturn("");
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void ensureNullPageflowInPageflowContextErrorIsTrapped() throws BaseException {
        when(PageFlowContext.getPageFlow()).thenReturn(null);
        sut.invoke();
    }
    
    @Test(expected=PreconditionException.class)
    public void ensureExistingPolicyInSessionErrorIsTrapped() throws BaseException {
        doReturn(policy).when(sut).getPolicyFromPageFlowContext();
        sut.invoke();
        verify(sut, never()).setPolicyToPageFlowContext(any(Policy.class));
    }
    
    @Test(expected=PostconditionException.class) 
    public void ensurePostconditionIsChecked() throws BaseException {
        when(PageFlowContext.getPolicy()).thenReturn(null);
        sut.invoke();
        PowerMockito.verifyStatic();
        PageFlowContext.setPolicy(eq(policy));
    }

    @Test 
    public void testHappyPath() throws BaseException {
        sut.invoke();
        verify(sut).setPolicyToPageFlowContext(eq(policy));
    }
    
    @Test
    public void testModelArgRetIsPopulated() throws BaseException {
        sut.invoke();
        verify(argument).setModelArgRet(eq(policy));
    }
    
    @Test
    public void shouldPopulatePolicyWithProductTypeId() throws BaseException {
        doReturn("TEST_PRODUCT_NAME").when(sut).getProductNameFromPageFlowContext();
        sut.invoke();
        verify(policy).setProductTypeId(eq("TEST_PRODUCT_NAME"));
    }
}
