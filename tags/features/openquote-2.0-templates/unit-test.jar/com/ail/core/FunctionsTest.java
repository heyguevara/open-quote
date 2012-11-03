package com.ail.core;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.Collection;

import org.junit.Test;


public class FunctionsTest {
    @Test
    public void testObjectListToLineSeperatedString() {
        Collection<Object> collection=new ArrayList<Object>();
        
        collection.add("Hello");
        collection.add("World!!");
        collection.add(new Integer(21));
        
        String value=Functions.collectionAsLineSeparatedString(collection);
        
        assertNotNull(value);
        assertEquals("Hello\nWorld!!\n21", value);
    }

    @Test
    public void testObjectListToLineSeperatedStringWithEmptyList() {
        Collection<Object> collection=new ArrayList<Object>();
        
        String value=Functions.collectionAsLineSeparatedString(collection);
        
        assertNotNull(value);
        assertEquals("", value);
    }

    @Test
    public void testStringExpand() throws Exception {
        Version v=new Version();
        
        v.setAuthor("H.G. Wells");
        v.setComment("Nice House, good tea");
        v.setCopyright("(c) me");

        String st=Functions.expand("Author: ${/author}, '${/comment}'. ${/copyright}", v);
        assertEquals("Author: H.G. Wells, 'Nice House, good tea'. (c) me", st);
    }
}
