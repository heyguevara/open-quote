/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.ail.coretest;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import com.ail.core.CoreProxy;
import com.ail.core.ExceptionRecord;

/**
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source: /home/bob/CVSRepository/projects/core/test/com/ail/coretest/TestTypeXpath.java,v $
 */
public class TestExceptionRecord extends TestCase {

    /** Constructs a test case with the given name. */
    public TestExceptionRecord(String name) {
        super(name);
    }

    public static Test suite() {
        return new TestSuite(TestExceptionRecord.class);
    }

    public static void main(String[] args) {
        TestRunner.run(suite());
    }
    
    public void testProperties() throws Exception {
        Exception e=new IllegalStateException("On no!");
        ExceptionRecord er=new ExceptionRecord(e);
        assertEquals(this.getClass().getName(), er.getCatchingClassName());
        assertEquals("java.lang.IllegalStateException: On no!", er.getReason());
    }

    public void testXmlBinding() throws Exception {
        Exception e=new IllegalStateException("On no!");
        ExceptionRecord er=new ExceptionRecord(e);
        String xml=new CoreProxy().toXML(er).toString();
        assertTrue(xml.indexOf("<stack")>0);
        assertTrue(xml.indexOf("java.lang.IllegalStateException: On no!")>0);
        assertTrue(xml.indexOf("catchingClassName=\"com.ail.coretest.TestExceptionRecord\"")>0);
    }
}
