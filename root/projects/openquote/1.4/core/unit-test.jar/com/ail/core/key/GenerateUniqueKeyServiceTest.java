package com.ail.core.key;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.Parameter;

public class GenerateUniqueKeyServiceTest {
    private Core mockCore;
    private GenerateUniqueKeyArg mockArgs;
    private Configuration mockConfiguration;
    private GenerateUniqueKeyService SUT;
    private Parameter mockNextNumberParameter;
    private Parameter mockBlockSizeParameter;
    
    @Before
    public void setupMocks() {
        mockCore=mock(Core.class);
        mockArgs=mock(GenerateUniqueKeyArg.class);
        mockConfiguration=mock(Configuration.class);
        mockNextNumberParameter=mock(Parameter.class);
        mockBlockSizeParameter=mock(Parameter.class);

        GenerateUniqueKeyService GUKS;
        GUKS=new GenerateUniqueKeyService(mockCore);
        GUKS.setArgs(mockArgs);
        SUT=spy(GUKS);
        doReturn(mockConfiguration).when(SUT).getConfiguration();
        doNothing().when(SUT).setConfiguration(mockConfiguration);

        when(mockBlockSizeParameter.getValue()).thenReturn("20");
        when(mockNextNumberParameter.getValue()).thenReturn("20");
        
        when(mockConfiguration.findParameter(eq("TestNextNumber"))).thenReturn(mockNextNumberParameter);
        when(mockConfiguration.findParameter(eq("TestBlockSize"))).thenReturn(mockBlockSizeParameter);
    }
    
    @Test(expected=PreconditionException.class)
    public void testKeyTypeNullPrecondidition() throws Exception {
        when(mockArgs.getKeyIdArg()).thenReturn(null);
        SUT.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void testKeyTypeZeroLengthPrecondidition() throws Exception {
        when(mockArgs.getKeyIdArg()).thenReturn("");
        SUT.invoke();
    }

    @Test(expected=PostconditionException.class)
    public void testKeyIdPostcondition() throws Exception {
        when(mockArgs.getKeyIdArg()).thenReturn("123");
        when(mockArgs.getKeyRet()).thenReturn(null);
        SUT.invoke();
    }
    
    @Test
    public void testUniqueNumber() throws Exception {
        when(mockArgs.getKeyIdArg()).thenReturn("Test");
        SUT.invoke();
        SUT.invoke();
        verify(mockArgs, times(1)).setKeyRet(20);
        verify(mockArgs, times(1)).setKeyRet(21);
    }
}
