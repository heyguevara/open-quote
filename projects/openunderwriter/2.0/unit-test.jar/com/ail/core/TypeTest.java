package com.ail.core;

import static org.junit.Assert.assertTrue;

import java.lang.reflect.Field;

import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

public class TypeTest {
    public Type sut;
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        
        sut=new Type();
    }

    @Test
    public void shouldRelyOnClassCloneToCloneNumbers() throws SecurityException, NoSuchFieldException {

        Field field=TypeTest.class.getDeclaredField("sut");
        
        assertTrue(sut.cloneWasHandledByJava(field, Number.class));
    }

}
