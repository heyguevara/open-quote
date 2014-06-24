package com.ail.liferay;

import com.ail.core.CoreProxy;
import com.ail.core.XMLException;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.SavedPolicy;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portlet.asset.model.AssetRenderer;
import com.liferay.portlet.asset.model.BaseAssetRendererFactory;

public class AssetRendererFactory extends BaseAssetRendererFactory {

    public AssetRendererFactory() {
        // TODO Auto-generated constructor stub
    }

    @Override
    public AssetRenderer getAssetRenderer(long classPK, int type) throws PortalException, SystemException {
        CoreProxy proxy = new CoreProxy();
        SavedPolicy savedPolicy =(SavedPolicy)proxy.queryUnique("get.savedPolicy.by.systemId'", classPK);
        
        try {
            return new PolicyAssetRenderer(savedPolicy.getPolicy());
        } catch (XMLException e) {
            throw new PortalException("Error loading policy (systemId="+savedPolicy.getSystemId()+")", e);
        }
    }

    @Override
    public String getClassName() {
        return Policy.class.getName();
    }

    @Override
    public String getType() {
        return "Policy";
    }

}
