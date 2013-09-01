/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.ail.pageflow.service;

import com.ail.annotation.ServiceArgument;
import com.ail.annotation.ServiceCommand;
import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.Service;
import com.ail.core.command.Argument;
import com.ail.core.command.Command;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.pageflow.util.PageFlowContext;

@ServiceImplementation
public class PersistPolicyService extends Service<PersistPolicyService.PersistPolicyArgument> {

    @ServiceArgument
    public interface PersistPolicyArgument extends Argument {
        Policy getPolicyArgRet();

        void setPolicyArgRet(Policy policy);
    }

    @ServiceCommand(defaultServiceClass = PersistPolicyService.class)
    public interface PersistPolicyCommand extends Command, PersistPolicyArgument {
    }

    @Override
    public void invoke() throws BaseException {
        if (args.getPolicyArgRet() != null) {
            Policy policy = (Policy) args.getPolicyArgRet();

            SavedPolicy saved = null;

            CoreProxy proxy = getCoreProxyFromPageFlowContext();

            policy.setPage(getCurrentPageFromPageFlowContext());

            // Update it if we've saved it before, otherwise create it.
            if (policy.isPersisted()) {
                saved = (SavedPolicy) proxy.queryUnique("get.savedPolicy.by.systemId", policy.getSystemId());
                saved.setPolicy(policy);
                saved = proxy.update(saved);
            } else {
                saved = createNewSavedPolicy(policy);
                saved = proxy.create(saved);
            }

            // Copy the system id and serial version number back into the quote
            // for next time.
            policy.setSystemId(saved.getSystemId());
            policy.setSerialVersion(saved.getSerialVersion());

            setPolicyToPageFlowContext(policy);

            args.setPolicyArgRet(policy);
        }
    }

    // Wrapped static PageFlowContext call to help testability
    protected String getCurrentPageFromPageFlowContext() {
        return PageFlowContext.getPageFlow().getCurrentPage();
    }

    // Wrapped static PageFlowContext call to help testability
    protected CoreProxy getCoreProxyFromPageFlowContext() {
        return PageFlowContext.getCoreProxy();
    }

    // Wrapped new to help testability
    protected SavedPolicy createNewSavedPolicy(Policy policy) {
        return new SavedPolicy(policy);
    }

    protected void setPolicyToPageFlowContext(Policy policy) {
        PageFlowContext.setPolicy(policy);
    }
}