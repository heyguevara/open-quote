package com.ail.core.urlhandler.product;

import static org.junit.Assert.*;

import org.junit.Test;

public class HandlerTest {

    @Test
    public void testLocalisationOfURLs() {
        Handler handler = new Handler();
        String result;

        result = handler.addLanguageToURL("http://localhost:8080/folder/folder/name.html", "en");
        assertEquals("http://localhost:8080/folder/folder/name_en.html", result);

        result = handler.addLanguageToURL("http://localhost:8080/folder/folder/name", "en");
        assertEquals("http://localhost:8080/folder/folder/name_en", result);

        result = handler.addLanguageToURL("http://localhost:8080/folder/folder/name.html", null);
        assertEquals("http://localhost:8080/folder/folder/name.html", result);

        result = handler.addLanguageToURL("http://localhost:8080/folder/folder/name", null);
        assertEquals("http://localhost:8080/folder/folder/name", result);

        result = handler.addLanguageToURL("http://localhost:8080/folder/folder/name.name.html", "en");
        assertEquals("http://localhost:8080/folder/folder/name.name_en.html", result);

    }
}
