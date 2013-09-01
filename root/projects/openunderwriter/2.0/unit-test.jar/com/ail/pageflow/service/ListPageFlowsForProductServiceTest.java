package com.ail.pageflow.service;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.Type;
import com.ail.core.configure.Types;
import com.ail.pageflow.PageFlow;
import com.ail.pageflow.service.ListPageFlowsForProductService;

public class ListPageFlowsForProductServiceTest {

    ListPageFlowsForProductService sut;

    @Mock
    ListPageFlowsForProductService.ListPageFlowsForProductArgument argument;
    @Mock
    Core core;
    @Mock
    Configuration configuration;
    @Mock
    Types types;
    @Mock
    Type typeOne;
    @Mock
    Type typeTwo;
    @Mock
    Type typeThree;
    
    @Before
    public void setup() throws Exception {
        MockitoAnnotations.initMocks(this);

        sut=spy(new ListPageFlowsForProductService());
        sut.setArgs(argument);
        
        doReturn("My.Product.Name").when(argument).getProductNameArg();
        doReturn(core).when(sut).getCore();
        doReturn(configuration).when(core).getConfiguration();
        doReturn(types).when(configuration).getTypes();
        doReturn(3).when(types).getTypeCount();

        doReturn(typeOne).when(types).getType(eq(0));
        doReturn(typeTwo).when(types).getType(eq(1));
        doReturn(typeThree).when(types).getType(eq(2));

        doReturn("com.ail.not.a.pageflow").when(typeOne).getKey();
        
        doReturn(PageFlow.class.getName()).when(typeTwo).getKey();
        doReturn("TypeTwo").when(typeTwo).getName();

        doReturn(PageFlow.class.getName()).when(typeThree).getKey();
        doReturn("TypeThree").when(typeThree).getName();
    }
    
    @Test(expected=PreconditionException.class)
    public void testProductNamePreconditionCatchesNullStrings() throws BaseException {
        doReturn(null).when(argument).getProductNameArg();
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void testProductNamePreconditionCatchesEmptyStrings() throws BaseException {
        doReturn("").when(argument).getProductNameArg();
        sut.invoke();
    }
    
    @Test(expected=PreconditionException.class)
    public void testConfigurationPreconditionCatchesNulls() throws BaseException {
        doReturn(null).when(core).getConfiguration();
        sut.invoke();
    }
    
    @Test(expected=PostconditionException.class) 
    public void testPostconditionExceptionForNullResults() throws BaseException {
        doReturn(null).when(argument).getPageFlowNameRet();
        sut.invoke();
    }
    
    @Test
    public void testHappyPath() throws BaseException {
        sut.invoke();

        verify(typeOne, times(1)).getKey();
        verify(typeOne, never()).getName();
        verify(typeTwo, times(1)).getKey();
        verify(typeTwo, times(1)).getName();
        verify(typeThree, times(1)).getKey();
        verify(typeThree, times(1)).getName();
    }
}
