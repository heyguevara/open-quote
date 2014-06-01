package com.ail.insurance.acceptance;

import static org.junit.Assert.fail;
import static org.mockito.Mockito.doReturn;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.financial.MoneyProvision;
import com.ail.financial.PaymentSchedule;
import com.ail.insurance.acceptance.PremiumCollectionExecutionService.PremiumCollectionExecutionArgument;
import com.ail.insurance.policy.Policy;

import static com.ail.insurance.policy.PolicyStatus.ON_RISK;
import static com.ail.insurance.policy.PolicyStatus.QUOTATION;

public class PremiumCollectionExecutionServiceTest {

    PremiumCollectionExecutionService sut;

    @Mock
    PremiumCollectionExecutionArgument argument;
    @Mock
    Core core;
    @Mock
    Policy policy;
    @Mock
    PaymentSchedule paymentDetails;
    @Mock
    MoneyProvision moneyProvision;
    
    List<MoneyProvision> moneyProvisions=new ArrayList<MoneyProvision>();
    
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        sut=new PremiumCollectionExecutionService();
        sut.setArgs(argument);
        sut.setCore(core);
        
        doReturn(policy).when(argument).getPolicyArgRet();
        doReturn(ON_RISK).when(policy).getStatus();
        doReturn(paymentDetails).when(policy).getPaymentDetails();
        doReturn(moneyProvisions).when(paymentDetails).getMoneyProvision();
        moneyProvisions.add(moneyProvision);
    }
    
    @Test(expected=PreconditionException.class)
    public void checkThatNullPolicyArgIsTrapped() throws BaseException {
        doReturn(null).when(argument).getPolicyArgRet();
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void checkThatPolicyNotOnRiskIsTrapped() throws BaseException {
        doReturn(QUOTATION).when(policy).getStatus();
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void checkThatNullPaymentDetailsIsTrapped() throws BaseException {
        doReturn(null).when(policy).getPaymentDetails();
        sut.invoke();
    }
    
    @Test
    public void checkThatNullAndEmptyMoneyProvisionsListIsTrapped() throws BaseException {
        try {
            doReturn(null).when(paymentDetails).getMoneyProvision();
            sut.invoke();
            fail("Null money provision was not trapped");
        } catch(PreconditionException e) {
            // ignore this, it's what we want.
        }

        try {
            doReturn(new ArrayList<MoneyProvision>()).when(paymentDetails).getMoneyProvision();
            sut.invoke();
            fail("Null money provision was not trapped");
        } catch(PreconditionException e) {
            // ignore this, it's what we want.
        }
    }
}
