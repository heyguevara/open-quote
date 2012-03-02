package com.ail.openquote;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.CoreProxy;
import com.ail.core.CoreUserBaseCase;
import com.ail.financial.CurrencyAmount;

@SuppressWarnings("serial")
public class TestCurrencyAmountToFromXML extends CoreUserBaseCase {
    private CoreProxy core;

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    @Before
    public void setUp() { 
        setupSystemProperties();
        core = new CoreProxy();
    }

    @Test
    public void testToXml() {
		CurrencyAmount ca=new CurrencyAmount("321.21", "GBP");
		System.out.println("amount:"+ca.getAmount());
		System.out.println("amountAsString:"+ca.getAmountAsString());
		System.out.println(core.toXML(ca));
	}
}
