package com.ail.pageflow.util;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

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
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.util.SelectPageflowService.SelectPageflowArgument;

@RunWith(PowerMockRunner.class)
@PrepareForTest({ PageflowContext.class, SelectPageflowService.class })
public class SelectPageflowServiceTest {
    private static final String TEST_PRODUCT_NAME = "TEST PRODUCT";
    private static final String TEST_PAGEFLOW_NAME = "TEST PAGEFLOW";

    SelectPageflowService sut;

    @Mock
    SelectPageflowArgument argument;
    @Mock
    CoreProxy coreProxy;
    @Mock 
    PageFlow pageflow;

    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);
        sut=new SelectPageflowService();
        sut.setArgs(argument);

        whenNew(CoreProxy.class).withAnyArguments().thenReturn(coreProxy);
        
        when(argument.getPageflowNameArg()).thenReturn(TEST_PAGEFLOW_NAME);
        when(argument.getProductNameArg()).thenReturn(TEST_PRODUCT_NAME);
        when(coreProxy.newProductType(eq(TEST_PAGEFLOW_NAME))).thenReturn(pageflow);
    }

    @Test(expected = PreconditionException.class)
    public void testPageflowNameNotDefinedPrecondition() throws BaseException {
        when(argument.getPageflowNameArg()).thenReturn((String) null);
        sut.invoke();
    }

    @Test(expected = PreconditionException.class)
    public void testProductNameNotDefinedPrecondition() throws BaseException {
        when(argument.getProductNameArg()).thenReturn((String) null);
        sut.invoke();
    }

    @Test
    public void testPageflowAddedToPageflowContext() throws BaseException {
        sut.invoke();
        PowerMockito.verifyStatic(times(1));
        PageflowContext.setPageFlow(eq(pageflow));
    }

    @Test
    public void testCoreAddedToPageflowContext() throws BaseException {
        sut.invoke();
        PowerMockito.verifyStatic(times(1));
        PageflowContext.setCore(eq(coreProxy));
    }
}
