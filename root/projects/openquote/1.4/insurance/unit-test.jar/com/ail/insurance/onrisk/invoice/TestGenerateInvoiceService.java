package com.ail.insurance.onrisk.invoice;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.document.generatedocument.RenderDocumentCommand;
import com.ail.core.document.model.DocumentDefinition;
import com.ail.financial.CurrencyAmount;
import com.ail.financial.MoneyProvision;
import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.party.Party;

public class TestGenerateInvoiceService {
    GenerateInvoiceService service;
    GenerateInvoiceArg args;
    Core mockCore;
    Policy mockPolicy;
    PaymentSchedule mockPaymentSchedule;
    List<MoneyProvision> mockMoneyProvision;

    @Before
    public void setUp() {
        mockCore = mock(Core.class);
        service = new GenerateInvoiceService();
        service.setCore(mockCore);
        args = new GenerateInvoiceArgImp();
        mockPolicy = mock(Policy.class);
        mockPaymentSchedule = mock(PaymentSchedule.class);
        mockMoneyProvision = new ArrayList<MoneyProvision>();
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
    public void testPaymentInfoPresentPrecondition() throws Exception {
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.ON_RISK);
        when(mockPolicy.getProductTypeId()).thenReturn("ProductTypeID");
        args.setPolicyArg(mockPolicy);

        try {
            service.invoke();
            fail("Expected predondition (payment info not present) not thrown.");
        }
        catch (PreconditionException e) {
            // Ignore. This is a good thing.
        }
    }

    @Test
    public void testHappyPath() throws Exception {
        CurrencyAmount mockTotalPrmium=mock(CurrencyAmount.class);
        Party mockPolicyHolder=mock(Party.class);
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.ON_RISK);
        when(mockPolicy.getProductTypeId()).thenReturn("ProductTypeID");
        when(mockPolicy.getPaymentDetails()).thenReturn(mockPaymentSchedule);
        when(mockPolicy.getTotalPremium()).thenReturn(mockTotalPrmium);
        when(mockPolicy.getPolicyHolder()).thenReturn(mockPolicyHolder);
        when(mockPaymentSchedule.getMoneyProvision()).thenReturn(mockMoneyProvision);
        args.setPolicyArg(mockPolicy);

        DocumentDefinition mockDocumentDefinition = mock(DocumentDefinition.class);
        when(mockCore.newProductType(anyString(), eq("InvoiceDocument"))).thenReturn(mockDocumentDefinition);

        RenderDocumentCommand mockRenderDocumentCommand = mock(RenderDocumentCommand.class);
        when(mockCore.newCommand(anyString())).thenReturn(mockRenderDocumentCommand);
        when(mockRenderDocumentCommand.getRenderedDocumentRet()).thenReturn(new byte[1]);

        service.invoke();

        assertTrue(mockRenderDocumentCommand.getRenderedDocumentRet().length == 1);
    }

    @Test
    public void testPostcondition() throws Exception {
        CurrencyAmount mockTotalPrmium=mock(CurrencyAmount.class);
        Party mockPolicyHolder=mock(Party.class);

        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.ON_RISK);
        when(mockPolicy.getProductTypeId()).thenReturn("ProductTypeID");
        when(mockPolicy.getPaymentDetails()).thenReturn(mockPaymentSchedule);
        when(mockPolicy.getTotalPremium()).thenReturn(mockTotalPrmium);
        when(mockPolicy.getPolicyHolder()).thenReturn(mockPolicyHolder);
        when(mockPaymentSchedule.getMoneyProvision()).thenReturn(mockMoneyProvision);
        args.setPolicyArg(mockPolicy);

        DocumentDefinition mockDocumentDefinition = mock(DocumentDefinition.class);
        when(mockCore.newProductType(anyString(), eq("InvoiceDocument"))).thenReturn(mockDocumentDefinition);

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
