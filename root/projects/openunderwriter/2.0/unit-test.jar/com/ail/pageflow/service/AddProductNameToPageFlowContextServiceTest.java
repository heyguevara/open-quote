package com.ail.pageflow.service;

import static com.ail.pageflow.service.AddProductNameToPageFlowContextService.PRODUCT_PORTLET_PREFERENCE_NAME;
import static com.ail.pageflow.service.AddProductNameToPageFlowContextService.PRODUCT_PORTLET_REQUEST_PARAMETER_NAME;
import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Matchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.PreconditionException;
import com.ail.pageflow.ExecutePageActionService;
import com.ail.pageflow.util.PageFlowContext;

public class AddProductNameToPageFlowContextServiceTest {
    AddProductNameToPageFlowContextService sut;

    @Mock
    ExecutePageActionService.ExecutePageActionArgument argument;
    @Mock
    PortletRequest portletRequest;
    @Mock
    PortletPreferences portletPreferences;
    @Mock
    PortletSession portletSession;
    @Mock
    CoreProxy coreProxy;
    
    @Before
    public void setupSut() throws Exception {
        MockitoAnnotations.initMocks(this);
        
        sut = spy(new AddProductNameToPageFlowContextService());
        sut.setArgs(argument);

        doReturn(portletRequest).when(argument).getPortletRequestArg();
        doReturn(portletSession).when(argument).getPortletSessionArg();
        doReturn(portletPreferences).when(argument).getPortletPreferencesArg();
    }

    @Test
    public void checkThatSessionAttributeIsUsed() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getProductNameFromPageFlowContext();
        doReturn(null).when(portletPreferences).getValue(eq(PRODUCT_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn(null).when(portletRequest).getProperty(eq(PRODUCT_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setProductNameToPageFlowContext(eq("TEST_ATTR"));

        sut.invoke();

        verify(sut).setProductNameToPageFlowContext(eq("TEST_ATTR"));
    }

    @Test
    public void checkThatPortletPreferenceIsUsed() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getProductNameFromPageFlowContext();
        doReturn("TEST_PREF").when(portletPreferences).getValue(eq(PRODUCT_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn(null).when(portletRequest).getProperty(eq(PRODUCT_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setProductNameToPageFlowContext(eq("TEST_PREF"));

        sut.invoke();

        verify(sut).setProductNameToPageFlowContext(eq("TEST_PREF"));
    }

    @Test
    public void checkThatRequestParamIsUsed() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getProductNameFromPageFlowContext();
        doReturn("TEST_PREF").when(portletPreferences).getValue(eq(PRODUCT_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn("TEST_PARAM").when(portletRequest).getProperty(eq(PRODUCT_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setProductNameToPageFlowContext(eq("TEST_PARAM"));

        sut.invoke();

        verify(sut).setProductNameToPageFlowContext(eq("TEST_PARAM"));
    }

    @Test(expected = PreconditionException.class)
    public void checkNullPortletRequestPreconditionsIsChecked() throws BaseException {
        doReturn(null).when(argument).getPortletRequestArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void checkNullPortletPreferencesPreconditionsIsChecked() throws BaseException {
        doReturn(null).when(argument).getPortletPreferencesArg();
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void checkNullPortletSessionPreconditionsIsChecked() throws BaseException {
        doReturn(null).when(argument).getPortletSessionArg();
        sut.invoke();
    }

    @Test
    public void checkThatNoSelectionLeadsToNull() throws BaseException {
        doReturn(null).when(sut).getProductNameFromPageFlowContext();
        doReturn(null).when(portletPreferences).getValue(eq(PRODUCT_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn(null).when(portletRequest).getProperty(eq(PRODUCT_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setProductNameToPageFlowContext((String)isNull());

        sut.invoke();

        verify(sut).setProductNameToPageFlowContext((String)isNull());
    }

    @Test
    public void testCoreAddedToPageFlowContext() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getProductNameFromPageFlowContext();
        doNothing().when(sut).setProductNameToPageFlowContext("TEST_ATTR");

        sut.invoke();
        
        assertEquals("TEST_ATTR.Registry", PageFlowContext.getCoreProxy().getConfigurationNamespace());
    }
    
}
