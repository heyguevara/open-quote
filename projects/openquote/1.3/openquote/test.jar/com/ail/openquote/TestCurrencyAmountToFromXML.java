package com.ail.openquote;

import com.ail.core.CoreProxy;
import com.ail.coretest.CoreUserTestCase;
import com.ail.financial.CurrencyAmount;

@SuppressWarnings("serial")
public class TestCurrencyAmountToFromXML extends CoreUserTestCase {
    private CoreProxy core;

	public TestCurrencyAmountToFromXML(String name) {
        super(name);
	}

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     */
    protected void setUp() { 
        setupSystemProperties();
        core = new CoreProxy();
    }

    public void testToXml() {
		CurrencyAmount ca=new CurrencyAmount("321.21", "GBP");
		System.out.println("amount:"+ca.getAmount());
		System.out.println("amountAsString:"+ca.getAmountAsString());
		System.out.println(core.toXML(ca));
	}
}
