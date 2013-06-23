package com.ail.pageflow.service;

import static com.ail.pageflow.service.AddPageFlowNameToPageFlowContextService.PAGEFLOW_PORTLET_PREFERENCE_NAME;
import static com.ail.pageflow.service.AddPageFlowNameToPageFlowContextService.PAGEFLOW_PORTLET_REQUEST_PARAMETER_NAME;
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
import com.ail.core.PreconditionException;
import com.ail.pageflow.ExecutePageActionService;

public class AddPageFlowNameToPageFlowContextServiceTest {
    AddPageFlowNameToPageFlowContextService sut;

    @Mock
    ExecutePageActionService.ExecutePageActionArgument argument;
    @Mock
    PortletRequest portletRequest;
    @Mock
    PortletPreferences portletPreferences;
    @Mock
    PortletSession portletSession;

    @Before
    public void setupSut() {
        MockitoAnnotations.initMocks(this);
        sut = spy(new AddPageFlowNameToPageFlowContextService());
        sut.setArgs(argument);
        doReturn(portletRequest).when(argument).getPortletRequestArg();
        doReturn(portletSession).when(argument).getPortletSessionArg();
        doReturn(portletPreferences).when(argument).getPortletPreferencesArg();
    }

    @Test
    public void checkThatSessionAttributeIsUsed() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getPageFlowNameFromPageFlowContext(); 
        doReturn(null).when(portletPreferences).getValue(eq(PAGEFLOW_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn(null).when(portletRequest).getProperty(eq(PAGEFLOW_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setPageFlowNameToPageFlowContext(eq("TEST_ATTR"));

        sut.invoke();

        verify(sut).setPageFlowNameToPageFlowContext(eq("TEST_ATTR"));
    }

    @Test
    public void checkThatPortletPreferenceIsUsed() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getPageFlowNameFromPageFlowContext(); 
        doReturn("TEST_PREF").when(portletPreferences).getValue(eq(PAGEFLOW_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn(null).when(portletRequest).getProperty(eq(PAGEFLOW_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setPageFlowNameToPageFlowContext(eq("TEST_PREF"));

        sut.invoke();

        verify(sut).setPageFlowNameToPageFlowContext(eq("TEST_PREF"));
    }

    @Test
    public void checkThatRequestParamIsUsed() throws BaseException {
        doReturn("TEST_ATTR").when(sut).getPageFlowNameFromPageFlowContext(); 
        doReturn("TEST_PREF").when(portletPreferences).getValue(eq(PAGEFLOW_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn("TEST_PARAM").when(portletRequest).getProperty(eq(PAGEFLOW_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setPageFlowNameToPageFlowContext(eq("TEST_PARAM"));

        sut.invoke();

        verify(sut).setPageFlowNameToPageFlowContext(eq("TEST_PARAM"));
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
    public void checkThatPostconditionsAreChecked() throws BaseException {
        doReturn(null).when(sut).getPageFlowNameFromPageFlowContext(); 
        doReturn(null).when(portletPreferences).getValue(eq(PAGEFLOW_PORTLET_PREFERENCE_NAME), (String) isNull());
        doReturn(null).when(portletRequest).getProperty(eq(PAGEFLOW_PORTLET_REQUEST_PARAMETER_NAME));
        doNothing().when(sut).setPageFlowNameToPageFlowContext((String)isNull());

        sut.invoke();
        
        verify(sut).setPageFlowNameToPageFlowContext((String)isNull());
    }
}
