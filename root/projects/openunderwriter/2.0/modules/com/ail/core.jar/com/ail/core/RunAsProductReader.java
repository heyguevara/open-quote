/* Copyright Applied Industrial Logic Limited 2013. All rights Reserved */
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
package com.ail.core;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;

public abstract class RunAsProductReader {
    private CoreProxy coreProxy;
    private Long companyId;
    private String username;

    public RunAsProductReader() {
        coreProxy = new CoreProxy();

        companyId = new Long(getCoreProxy().getParameterValue("ProductReader.CompanyID"));
        username = getCoreProxy().getParameterValue("ProductReader.Username");
    }

    protected CoreProxy getCoreProxy() {
        return coreProxy;
    }

    protected abstract void doRun() throws Exception;
    
    public void run() throws PortalException, SystemException, Exception {
        PermissionChecker savedPermissionChecker = PermissionThreadLocal.getPermissionChecker();
        String savedUsername = PrincipalThreadLocal.getName();
        
        try {
            PermissionChecker permissionChecker;
            
            User productreader = UserLocalServiceUtil.getUserByScreenName(companyId, username);
            
            PrincipalThreadLocal.setName(productreader.getUserId());
            
            try {
                permissionChecker = PermissionCheckerFactoryUtil.create(productreader);
            } catch (Exception e) {
                throw new PortalException(e);
            }
            
            PermissionThreadLocal.setPermissionChecker(permissionChecker);
            
            doRun();
        }
        finally {
            PrincipalThreadLocal.setName(savedUsername);
            PermissionThreadLocal.setPermissionChecker(savedPermissionChecker);
        }
    }
}
