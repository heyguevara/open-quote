package com.ail.core.document;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.XMLString;
import com.ail.core.document.RenderException;
import com.ail.core.document.RenderItextPdfDocumentService;
import com.ail.core.document.RenderDocumentService.RenderDocumentArgument;


public class RenderItextPdfDocumentServiceTest {
    private Core mockCore;
    private RenderItextPdfDocumentService sut;
    private RenderDocumentArgument mockArgs;
    private XMLString mockXMLString;
    private byte[] mockRenderedDocument;

    @Before
    public void setupMocks() {
        mockCore=mock(Core.class);
        mockArgs=mock(RenderDocumentArgument.class);
        mockXMLString=mock(XMLString.class);
        mockRenderedDocument=new byte[10];
        
        String samplePDFSource=this.getClass().getResource("SampleTemplate.pdf").toString();
        
        sut=new RenderItextPdfDocumentService();
        sut.setCore(mockCore);
        sut.setArgs(mockArgs);
        when(mockArgs.getSourceDataArg()).thenReturn(mockXMLString);
        when(mockArgs.getTemplateUrlArg()).thenReturn(samplePDFSource);
        when(mockArgs.getRenderedDocumentRet()).thenReturn(mockRenderedDocument);
    }
    
    @Test(expected=PreconditionException.class)
    public void testSourceDataPrecondition() throws PreconditionException, PostconditionException, RenderException  {
        when(mockArgs.getSourceDataArg()).thenReturn(null);
        sut.invoke();
    }
    
    @Test(expected=PreconditionException.class)
    public void testTranslationURLPrecondition() throws PreconditionException, PostconditionException, RenderException  {
        when(mockArgs.getTemplateUrlArg()).thenReturn(null);
        sut.invoke();
    }

    @Test(expected=PostconditionException.class) 
    public void testRenderedDocumentPostcondition() throws PreconditionException, PostconditionException, RenderException {
        when(mockArgs.getRenderedDocumentRet()).thenReturn(null);
        sut.invoke();
    }
}
