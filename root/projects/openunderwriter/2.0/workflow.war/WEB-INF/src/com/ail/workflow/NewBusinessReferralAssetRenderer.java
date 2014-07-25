package com.ail.workflow;

import com.ail.insurance.policy.Policy;
import com.liferay.portlet.asset.model.BaseAssetRenderer;

import java.util.Locale;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class NewBusinessReferralAssetRenderer extends BaseAssetRenderer {

    private Policy policy;

    public NewBusinessReferralAssetRenderer(Policy policy) {
        this.policy = policy;
    }

    public String getClassName() {
        return Policy.class.getName();
    }

    @Override
    public long getClassPK() {
        return policy.getSystemId();
    }

    @Override
    public long getGroupId() {
        return 0;
    }

    @Override
    public String getSummary(Locale locale) {
        return getTitle(locale);
    }

    @Override
    public String getTitle(Locale locale) {
        StringBuffer summary = new StringBuffer();

        if (policy.getPolicyNumber() != null) {
            summary.append(policy.getPolicyNumber());
        } else if (policy.getQuotationDate() != null) {
            summary.append(policy.getQuotationNumber());
        }
        
        summary.append(", ").append(policy.getProductName());
        
        if (policy.getPolicyHolder()!=null && policy.getPolicyHolder().getLegalName()!=null) {
            summary.append(", ").append(policy.getPolicyHolder().getLegalName());
        }

        return summary.toString();
    }

    @Override
    public long getUserId() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getUserName() {
        return policy.getUsername();
    }

    @Override
    public String getUuid() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String render(RenderRequest renderRequest, RenderResponse renderResponse, String arg2) throws Exception {
        // TODO Auto-generated method stub
        return null;
    }
}
