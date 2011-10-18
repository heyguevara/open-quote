package com.ail.insurance.onrisk.wording;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.document.generatedocument.RenderDocumentCommand;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;

public class TestGenerateWordingService {
    GenerateWordingService service;
    GenerateWordingArg args;
    Core mockCore;
    Policy mockPolicy;

    @Before
    public void setUp() {
        mockCore = mock(Core.class);
        service = new GenerateWordingService(mockCore);
        args = new GenerateWordingArgImp();
        mockPolicy = mock(Policy.class);
        service.setArgs(args);
    }

    @Test
    public void testNullPolicyPreconditions() throws Exception {
        args.setPolicyArg(null);

        try {
            service.invoke();
            fail("Expected predondition (policy is null) not thrown.");
        }
        catch (PreconditionException e) {
            // Ignore. This is a good thing.
        }
    }

    @Test
    public void testPolicyStatusPrecondition() throws Exception {
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.APPLICATION);
        args.setPolicyArg(mockPolicy);

        try {
            service.invoke();
            fail("Expected predondition (policy.status is wrong) not thrown.");
        }
        catch (PreconditionException e) {
            // Ignore. This is a good thing.
        }
    }

    @Test
    public void testPolicyProductPrecondition() throws Exception {
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.ON_RISK);
        args.setPolicyArg(mockPolicy);

        try {
            service.invoke();
            fail("Expected predondition (product type is null) not thrown.");
        }
        catch (PreconditionException e) {
            // Ignore. This is a good thing.
        }
    }

    @Test
    public void testHappyPath() throws Exception {
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.ON_RISK);
        when(mockPolicy.getProductTypeId()).thenReturn("ProductTypeID");
        args.setPolicyArg(mockPolicy);

        DocumentDefinition mockDocumentDefinition = mock(DocumentDefinition.class);
        when(mockCore.newProductType(anyString(), eq("Wording"))).thenReturn(mockDocumentDefinition);

        RenderDocumentCommand mockRenderDocumentCommand = mock(RenderDocumentCommand.class);
        when(mockCore.newCommand(anyString())).thenReturn(mockRenderDocumentCommand);
        when(mockRenderDocumentCommand.getRenderedDocumentRet()).thenReturn(new byte[1]);

        service.invoke();

        assertTrue(mockRenderDocumentCommand.getRenderedDocumentRet().length == 1);
    }

    @Test
    public void testPostcondition() throws Exception {
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.ON_RISK);
        when(mockPolicy.getProductTypeId()).thenReturn("ProductTypeID");
        args.setPolicyArg(mockPolicy);

        DocumentDefinition mockDocumentDefinition = mock(DocumentDefinition.class);
        when(mockCore.newProductType(anyString(), eq("Wording"))).thenReturn(mockDocumentDefinition);

        RenderDocumentCommand mockRenderDocumentCommand = mock(RenderDocumentCommand.class);
        when(mockCore.newCommand(anyString())).thenReturn(mockRenderDocumentCommand);
        when(mockRenderDocumentCommand.getRenderedDocumentRet()).thenReturn(null);

        try {
            service.invoke();
            fail("Expected postcondition (getRenderedDocumentRet() is null) not thrown.");
        }
        catch (PostconditionException e) {
            // Ignore. This is a good thing.
        }
    }
}
