package com.ail.core.product.liferay;

import static org.mockito.Mockito.doReturn;

import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ail.core.BaseException;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.product.ProductUrlToConnectionService.ProductUrlToConnectionArgument;
import com.liferay.portal.kernel.repository.model.FileEntry;

public class LiferayProductUrlToConnectionServiceTest {
    LiferayProductUrlToConnectionService sut;

    @Mock
    ProductUrlToConnectionArgument args;

    @Mock
    FileEntry fileEntry;
    @Mock 
    URLConnection urlConnection;
    URL url=null;
    
    @Before
    public void setup() throws MalformedURLException {
        MockitoAnnotations.initMocks(this);
        
        sut=new LiferayProductUrlToConnectionService();
        sut.setArgs(args);
        
        url=new URL("http://localhost:8080");
        
        doReturn(url).when(args).getProductUrlArg();
        doReturn(urlConnection).when(args).getURLConnectionRet();
    }
    
    
    @Test(expected=PreconditionException.class)
    public void productUrlArgCannotBeNull() throws BaseException {
        doReturn(null).when(args).getProductUrlArg();
        sut.invoke();
    }

    @Test(expected=PostconditionException.class)
    public void urlConnectionRetCannotBeNull() throws BaseException {
        doReturn(null).when(args).getURLConnectionRet();
        sut.invoke();
    }
}
