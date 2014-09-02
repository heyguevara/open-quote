package com.ail.core.product.liferay;

import static org.mockito.Mockito.doReturn;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ail.core.BaseException;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.product.ProductUrlToExternalUrlService.ProductUrlToExternalUrlArgument;

public class LiferayProductUrlToExternalUrlServiceTest {

    private LiferayProductUrlToExternalUrlService sut;

    @Mock
    ProductUrlToExternalUrlArgument args;
    URL productUrl;
    
    @Before
    public void setup() throws MalformedURLException {
        MockitoAnnotations.initMocks(this);

        sut = new LiferayProductUrlToExternalUrlService();
        sut.setArgs(args);

        productUrl = new URL("http://localhost:8080");
        
        doReturn(productUrl).when(args).getProductUrlArg();
    }

    @Test(expected=PreconditionException.class)
    public void nullProductUrlArgShouldBeTrapped() throws BaseException {
        doReturn((URL)null).when(args).getProductUrlArg();
        sut.invoke();
    }

    @Test(expected=PostconditionException.class)
    public void checkThatNullExternalUrlPostconditionIsTrapped() throws BaseException {
        doReturn(null).when(args).getExternalUrlRet();
        sut.invoke();
    }
    
}
