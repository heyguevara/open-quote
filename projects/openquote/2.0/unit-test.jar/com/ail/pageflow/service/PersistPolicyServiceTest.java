package com.ail.pageflow.service;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.pageflow.service.PersistPolicyService.PersistPolicyArgument;

@RunWith(MockitoJUnitRunner.class)
public class PersistPolicyServiceTest {
    private static long POLICY_SYSTEM_ID = 1L;
    private PersistPolicyService sut = new PersistPolicyService();

    @Mock
    private PersistPolicyArgument args;
    @Mock
    private CoreProxy coreProxy;
    @Mock
    private Policy policy;
    @Mock
    private SavedPolicy savedPolicy;
    
    @Before
    public void setup() {
        sut = spy(new PersistPolicyService());
        sut.setArgs(args);

        doReturn(coreProxy).when(sut).getCoreProxyFromPageFlowContext();
        doReturn(savedPolicy).when(sut).createNewSavedPolicy(eq(policy));
        doReturn(policy).when(args).getPolicyArgRet();
        doReturn("pagename").when(sut).getCurrentPageFromPageFlowContext();
        doNothing().when(sut).setPolicyToPageFlowContext(any(Policy.class));
    }

    @Test
    public void shouldDoNothingWhenPolicyIsNull() throws BaseException {
        doReturn(null).when(args).getPolicyArgRet();
        sut.invoke();
        verify(coreProxy, never()).create(any(Type.class));
        verify(coreProxy, never()).update(any(Type.class));
    }

    @Test
    public void shouldCreateUnsavedPolicy() throws BaseException {
        doReturn(false).when(policy).isPersisted();
        doReturn(savedPolicy).when(coreProxy).create(any(SavedPolicy.class));
        sut.invoke();
        verify(coreProxy).create(any(SavedPolicy.class));
        verify(coreProxy, never()).update(any(Type.class));
    }

    @Test
    public void shouldUpdateSavedPolicy() throws BaseException {
        doReturn(savedPolicy).when(coreProxy).queryUnique(anyString(), eq(POLICY_SYSTEM_ID));
        doReturn(savedPolicy).when(coreProxy).update(any(SavedPolicy.class));
        doReturn(POLICY_SYSTEM_ID).when(policy).getSystemId();
        doReturn(true).when(policy).isPersisted();
        sut.invoke();
        verify(savedPolicy).setPolicy(eq(policy));
        verify(coreProxy).update(eq(savedPolicy));
        verify(coreProxy, never()).create(any(SavedPolicy.class));
    }
}
