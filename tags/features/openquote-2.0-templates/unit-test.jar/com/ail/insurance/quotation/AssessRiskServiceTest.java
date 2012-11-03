package com.ail.insurance.quotation;

import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.InOrder;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.quotation.AssessPolicyRiskService.AssessPolicyRiskCommand;
import com.ail.insurance.quotation.AssessRiskService.AssessRiskArgument;

public class AssessRiskServiceTest {
    private static final String TEST_PRODUCT_TYPE_ID = "DUMMY";

    private AssessRiskService sut;
    private Core mockCore;
    private AssessRiskArgument mockArgs;
    private Policy mockPolicy;
    private AssessPolicyRiskCommand mockAssessPolicyRiskCommand;
    private AssessmentSheet mockAssessmentSheet;

    @Before
    public void setupSut() {
        mockCore = mock(Core.class);
        mockArgs = mock(AssessRiskArgument.class);
        mockPolicy = mock(Policy.class);
        mockAssessPolicyRiskCommand = mock(AssessPolicyRiskCommand.class);
        mockAssessmentSheet = mock(AssessmentSheet.class);

        sut = spy(new AssessRiskService());
        sut.setCore(mockCore);
        sut.setArgs(mockArgs);

        when(sut.getCore()).thenReturn(mockCore);
        when(mockArgs.getPolicyArgRet()).thenReturn(mockPolicy);
        when(mockArgs.getCallersCore()).thenReturn(mockCore);
        when(mockPolicy.getProductTypeId()).thenReturn(TEST_PRODUCT_TYPE_ID);
        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.APPLICATION);
        when(mockCore.newCommand(eq("AssessPolicyRisk"), eq(AssessPolicyRiskCommand.class)))
                .thenReturn(mockAssessPolicyRiskCommand);
        when(mockAssessPolicyRiskCommand.getAssessmentSheetArgRet()).thenReturn(mockAssessmentSheet);
    }

    @Test
    public void testHappyPath() throws BaseException {
        sut.invoke();
    }
    
    @Test(expected=PreconditionException.class)
    public void testNullPolicy() throws BaseException {
        when(mockArgs.getPolicyArgRet()).thenReturn(null);
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void testNullProductTypeId() throws BaseException {
        when(mockPolicy.getProductTypeId()).thenReturn(null);
        sut.invoke();
    }

    @Test(expected=PreconditionException.class)
    public void testBlankProductTypeId() throws BaseException {
        when(mockPolicy.getProductTypeId()).thenReturn("");
        sut.invoke();
    }

    @Test
    public void testApplicationStatusCheck() throws BaseException {
        PolicyStatus[] statuses={PolicyStatus.DECLINED,
                                 PolicyStatus.ON_RISK,
                                 PolicyStatus.QUOTATION,
                                 PolicyStatus.REFERRED,
                                 PolicyStatus.SUBMITTED};
        
        for(PolicyStatus status: statuses) {
            when(mockPolicy.getStatus()).thenReturn(status);
            try {
                sut.invoke();
            }
            catch(PreconditionException e) {
                // this is a good thing
            }
        }

        when(mockPolicy.getStatus()).thenReturn(PolicyStatus.APPLICATION);
        sut.invoke();
    }
    
    @Test
    public void testLockingAndUnlocking() throws BaseException {
        sut.invoke();
        InOrder order=inOrder(mockAssessmentSheet);
        order.verify(mockAssessmentSheet).setLockingActor(eq("AssessRisk"));
        order.verify(mockAssessmentSheet).clearLockingActor();
    }
    
    @Test
    public void testPolicyLevelIsVisited() throws BaseException {
        sut.invoke();
        verify(mockAssessPolicyRiskCommand).setPolicyArg(eq(mockPolicy));
    }
}
