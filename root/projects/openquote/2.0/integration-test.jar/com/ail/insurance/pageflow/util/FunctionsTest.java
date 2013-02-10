package com.ail.insurance.pageflow.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.net.MalformedURLException;
import java.net.URL;

import org.junit.Before;
import org.junit.Test;

public class FunctionsTest {

    @Before
    public void setupURLHandler() {
        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
    }
    
    @Test
    public void test() throws MalformedURLException {
        URL result=Functions.convertProductUrlToExternalForm(new URL("product://localhost:8080/MyProduct/MyResource.html"));
        assertNotNull(result);
        assertEquals("http://localhost:8080/api/secure/webdav/guest/document_library/Product/MyProduct/MyResource.html", result.toString());
    }
}
