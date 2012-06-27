package com.ail.insurance.pageflow;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.insurance.pageflow.render.Html;
import com.ail.insurance.pageflow.util.QuotationContext;
import com.ail.insurance.policy.Policy;

public class AnswerTest {
    private Answer sut;
    private RenderRequest request;
    private RenderResponse response;
    private Policy policy;
    private StringWriter output;
    private Attribute attribute;
    
    @Before
    public void setupSut() {
        Answer answer=new Answer();
        sut=spy(answer);
        when(sut.getTitle()).thenReturn("Test Title absolute: ${/absolute} relative: ${./relative}");
        when(sut.getBinding()).thenReturn("attribute");
    }
    
    @Before
    public void setupMocks() throws Exception {
        request = mock(RenderRequest.class);
        response = mock(RenderResponse.class);
        policy = mock(Policy.class);
        attribute = mock(Attribute.class);
        output = new StringWriter();
        PrintWriter outputWriter = new PrintWriter(output);
        
        when(response.getWriter()).thenReturn(outputWriter);
        when(policy.xpathGet(eq("/absolute"))).thenReturn("ABSOLUTE");
        when(policy.xpathGet(eq("./relative"))).thenReturn("RELATIVE");
        when(policy.xpathGet(eq("attribute"))).thenReturn(attribute);
        when(attribute.getFormattedValue()).thenReturn("VALUE");
        QuotationContext.setPolicy(policy);
        QuotationContext.setRenderer(new Html());
        QuotationContext.setCore(new CoreProxy("AIL.Base.Registry"));
    }
    
    @Test
    public void testRenderResponse() throws IllegalStateException, IOException {
        sut.renderResponse(request, response, policy);
        verify(response).getWriter();
        assertEquals("<tr><td>Test Title absolute: ABSOLUTE relative: RELATIVE</td><td>VALUE</td></tr>", output.toString());
    }

    @Test
    public void testRenderResponseNullAnswer() throws IllegalStateException, IOException {
        when(sut.getBinding()).thenReturn("nothing");
        sut.renderResponse(request, response, policy);
        verify(response).getWriter();
        assertEquals("<tr><td>Test Title absolute: ABSOLUTE relative: RELATIVE</td><td>Don't know how to format a 'null'</td></tr>", output.toString());
    }

    @Test
    public void testRenderResponseNullAbsoulteTitleVariable() throws IllegalStateException, IOException {
        when(policy.xpathGet(eq("/absolute"))).thenReturn(null);
        sut.renderResponse(request, response, policy);
        verify(response).getWriter();
        assertEquals("<tr><td>Test Title absolute: <b>could not resolve: /absolute</b> relative: RELATIVE</td><td>VALUE</td></tr>", output.toString());
    }

    @Test
    public void testRenderResponseNullRelativeTitleVariable() throws IllegalStateException, IOException {
        when(policy.xpathGet(eq("./relative"))).thenReturn(null);
        sut.renderResponse(request, response, policy);
        verify(response).getWriter();
        assertEquals("<tr><td>Test Title absolute: ABSOLUTE relative: <b>could not resolve: ./relative</b></td><td>VALUE</td></tr>", output.toString());
    }
}
