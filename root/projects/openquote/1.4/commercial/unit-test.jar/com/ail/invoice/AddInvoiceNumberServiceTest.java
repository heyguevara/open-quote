package com.ail.invoice;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Matchers.eq;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.key.GenerateUniqueKeyCommand;
import com.ail.financial.Invoice;

public class AddInvoiceNumberServiceTest {
    AddInvoiceNumberService SUT = null;
    AddInvoiceNumberArg args=null;
    private Invoice mockInvoice;
    private Core mockCore;
    
    @Before
    public void createMocks() {
        mockInvoice=mock(Invoice.class);
        when(mockInvoice.getInvoiceNumber()).thenReturn(null);
        when(mockInvoice.getProductTypeId()).thenReturn("productTypeId");

        args=mock(AddInvoiceNumberArg.class);
        when(args.getInvoiceArgRet()).thenReturn(mockInvoice);

        mockCore=mock(Core.class);
        
        GenerateUniqueKeyCommand mockGenerateUniqueKeyCommand=mock(GenerateUniqueKeyCommand.class);
        when(mockGenerateUniqueKeyCommand.getKeyRet()).thenReturn(20);
        when(mockCore.newCommand(eq(GenerateUniqueKeyCommand.class))).thenReturn(mockGenerateUniqueKeyCommand);
        
        GenerateInvoiceNumberRuleCommand mockGenerateInvoiceNumberRuleCommand=mock(GenerateInvoiceNumberRuleCommand.class);
        when(mockGenerateInvoiceNumberRuleCommand.getInvoiceNumberRet()).thenReturn("123");
        when(mockCore.newCommand(eq(GenerateInvoiceNumberRuleCommand.class))).thenReturn(mockGenerateInvoiceNumberRuleCommand);

        SUT=new AddInvoiceNumberService(mockCore);
        SUT.setArgs(args);
    }

    @Test(expected=PreconditionException.class)
    public void testInvoicePrecondition() throws Exception{
        when(args.getInvoiceArgRet()).thenReturn(null);
        SUT.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void testProductTypeIdPrecondition() throws Exception {
        when(mockInvoice.getProductTypeId()).thenReturn(null);
        SUT.invoke();
    }
    
    @Test(expected=PreconditionException.class)
    public void testInvoiceNumberPrecondition() throws Exception {
        when(mockInvoice.getInvoiceNumber()).thenReturn("INV1234");
        SUT.invoke();
    }

    @Test(expected=PostconditionException.class)
    public void testInvoiceNumberPostcondition() throws Exception {
        when(mockInvoice.getInvoiceNumber()).thenReturn(null);
        SUT.invoke();
    }
    
    @Test
    public void testHappyPath() throws BaseException {
        when(mockInvoice.getInvoiceNumber()).thenReturn(null, "123");
        SUT.invoke();
        verify(mockInvoice).setInvoiceNumber("123");
        assertEquals("123", args.getInvoiceArgRet().getInvoiceNumber());
    }
}
