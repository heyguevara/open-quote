package com.ail.ui.server.transformer;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import java.text.DateFormat;
import java.util.Date;

import com.ail.financial.Currency;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.party.Party;
import com.ail.ui.shared.model.PolicyDetailDTO;

import org.junit.*;
import org.mockito.*;

public class PolicyDetailTransformerTest {

    @Mock
    private Party party;
    @Mock
    private Policy policy;
    @Mock
    private SavedPolicy savedPolicy;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testTransform() throws Exception {

        Date quoteDate = new Date();
        Date expiryDate = new Date(quoteDate.getTime() + 1);

        doReturn("POL1").when(savedPolicy).getPolicyNumber();
        doReturn("QF1").when(savedPolicy).getQuotationNumber();
        doReturn("Product1").when(savedPolicy).getProduct();
        doReturn(new CurrencyAmount(10, Currency.AED)).when(savedPolicy).getPremium();
        doReturn(quoteDate).when(savedPolicy).getQuotationDate();
        doReturn(policy).when(savedPolicy).getPolicy();
        doReturn(expiryDate).when(policy).getExpiryDate();
        doReturn(party).when(policy).getProposer();
        doReturn("Name").when(party).getLegalName();

        PolicyDetailDTO policyDetail = new PolicyDetailTransformer().apply(savedPolicy);
        
        assertEquals("POL1", policyDetail.getPolicyNumber());
        assertEquals("QF1", policyDetail.getQuotationNumber());
        assertEquals(DateFormat.getDateInstance().format(expiryDate), policyDetail.getExpiryDate());
        assertEquals(DateFormat.getDateInstance().format(quoteDate), policyDetail.getQuoteDate());
        assertEquals("Product1", policyDetail.getProduct());
        assertEquals(Currency.AED.toString() + "10.00", policyDetail.getPremium());
        assertEquals("Name", policyDetail.getPolicyHolderName());
    }
    
    @Test
    public void testNullTransform() throws Exception {
        
        PolicyDetailDTO policyDetail = new PolicyDetailTransformer().apply(savedPolicy);
        assertEquals(null, policyDetail.getQuotationNumber());
    }
}
