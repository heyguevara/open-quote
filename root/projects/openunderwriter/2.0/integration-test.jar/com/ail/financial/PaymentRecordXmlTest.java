package com.ail.financial;

import static com.ail.financial.PaymentRecordType.NEW;
import static org.junit.Assert.assertEquals;

import java.util.Date;

import org.junit.Test;

import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.core.XMLString;

public class PaymentRecordXmlTest {

    @Test
    public void testConstructor() throws XMLException {
        CoreProxy coreProxy = new CoreProxy();

        PaymentRecord before = new PaymentRecord(new CurrencyAmount("1233", "GBP"), "trans", "method", NEW, new Date());
        
        XMLString xml = coreProxy.toXML(before);
        
        PaymentRecord after = coreProxy.fromXML(PaymentRecord.class, xml);
        
        assertEquals(before.getAmount(), after.getAmount());
        assertEquals(before.getTransactionReference(), after.getTransactionReference());
        assertEquals(before.getMethodIdentifier(), after.getMethodIdentifier());
        assertEquals(before.getType(), after.getType());
        assertEquals(before.getDate(), after.getDate());
    }
}
