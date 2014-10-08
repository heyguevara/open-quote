package com.ail.workflow;

import static com.liferay.portal.kernel.util.GetterUtil.getLong;
import static com.liferay.portal.kernel.workflow.WorkflowConstants.CONTEXT_ENTRY_CLASS_PK;
import static com.liferay.portal.kernel.workflow.WorkflowConstants.getStatusLabel;

import java.io.Serializable;
import java.util.Locale;
import java.util.Map;

import com.ail.core.CoreProxy;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.workflow.BaseWorkflowHandler;

public class NewBusinessReferralWorkflowHandler extends BaseWorkflowHandler {

    @Override
    public String getClassName() {
        return NewBusinessReferral.class.getName();
    }

    @Override
    public String getType(Locale locale) {
        return LanguageUtil.get(locale, "model.resource." + getClassName());
    }

    @Override
    public Object updateStatus(int status, Map<String, Serializable> context) throws PortalException, SystemException {
        long policyID = getLong(context.get(CONTEXT_ENTRY_CLASS_PK));
        
        new CoreProxy().logInfo("NewBusinessReferral workflow status updated to: "+getStatusLabel(status)+", for policyId: "+policyID);
        
        return null;
    }

}
