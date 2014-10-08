package com.ail.workflow;

import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.insurance.policy.SavedPolicy;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;

public class NewBusinessReferralAssetRendererFactory extends BaseAssetRendererFactory {

    @Override
    public AssetRenderer getAssetRenderer(long classPK, int type) throws PortalException, SystemException {
        CoreProxy proxy = new CoreProxy();
        SavedPolicy savedPolicy =(SavedPolicy)proxy.queryUnique("get.savedPolicy.by.systemId", classPK);
        
        try {
            return new NewBusinessReferralAssetRenderer(savedPolicy.getPolicy());
        } catch (XMLException e) {
            throw new PortalException("Error loading policy (systemId="+savedPolicy.getSystemId()+")", e);
        }
    }

    @Override
    public String getClassName() {
        return NewBusinessReferral.class.getName();
    }

    @Override
    public String getType() {
        return "NewBusinessReferral";
    }
}
