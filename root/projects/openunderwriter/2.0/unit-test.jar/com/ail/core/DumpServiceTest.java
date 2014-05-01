package com.ail.core;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ail.core.validator.ValidatorService.ValidatorArgument;

public class DumpServiceTest {

    @Mock
    private ValidatorArgument argument;

    private DumpService sut;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        sut = spy(new DumpService());
        sut.setArgs(argument);
    }

    @Test
    public void checkThatArgGettersAreCalled() {
        sut.invoke();
        
        verify(argument).getKeyArg();
        verify(argument).getValidationSpecArg();
        verify(argument).getValueArg();
    }

    @Test
    public void checkThatRetGettersAndOtherMethodsAreNotCalled() {
        sut.invoke();
        
        verify(argument, never()).getValidatorResultRet();
        verify(argument, never()).getCallersCore();
    }
}
