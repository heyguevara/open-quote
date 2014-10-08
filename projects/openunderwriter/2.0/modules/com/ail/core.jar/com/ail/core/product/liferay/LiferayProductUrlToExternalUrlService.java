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

package com.ail.core.product.liferay;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.CoreProxy;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.RunAsProductReader;
import com.ail.core.Service;
import com.ail.core.product.ProductUrlToExternalUrlService.ProductUrlToExternalUrlArgument;
import com.ail.core.product.liferay.LiferayLocateFileEntryService.LiferayLocateFileEntryCommand;
import com.ail.core.urlhandler.product.FileNotFoundPostcondition;
import com.liferay.portal.kernel.repository.model.FileEntry;

/**
 * Implementation of the ProductUrlToExternalUrlService for Liferay
 * 
 */
@ServiceImplementation
public class LiferayProductUrlToExternalUrlService extends Service<ProductUrlToExternalUrlArgument> {
    private String host = null;
    private String port = null;

    LiferayProductUrlToExternalUrlService(String host, String port) {
        super();
        this.host = host;
        this.port = port;
    }

    LiferayProductUrlToExternalUrlService(CoreProxy coreProxy) {
        this(coreProxy.getParameterValue("ProductRepository.Host"), coreProxy.getParameterValue("ProductRepository.Port"));
    }

    public LiferayProductUrlToExternalUrlService() {
        this(new CoreProxy());
    }

    /**
     * The 'business logic' of the entry point.
     * 
     * @throws PostconditionException
     */
    @Override
    public void invoke() throws PreconditionException, PostconditionException {
        if (args.getProductUrlArg() == null) {
            throw new PreconditionException("args.getProductUrlArg()==null || args.getProductUrlArg().length()==0");
        }

        try {
            new RunAsProductReader() {
                private ProductUrlToExternalUrlArgument args;

                protected RunAsProductReader with(ProductUrlToExternalUrlArgument args) {
                    this.args = args;
                    return this;
                }

                @Override
                protected void doRun() throws Exception {
                    LiferayLocateFileEntryCommand llfec = getCore().newCommand(LiferayLocateFileEntryCommand.class);
                    llfec.setProductUrlArg(args.getProductUrlArg());
                    llfec.invoke();
                    args.setExternalUrlRet(buildUrlPathFromFileEntry(llfec.getFileEntryRet()));
                }

                protected String buildUrlPathFromFileEntry(FileEntry fileEntryRet) {
                    return "http://" + host + ":" + port + "/documents/" +
                           fileEntryRet.getGroupId() + "/" + 
                           fileEntryRet.getFolderId() + "/" + 
                           fileEntryRet.getTitle() + "/" + 
                           fileEntryRet.getUuid();
                }
            }.with(args).run();
        } catch (Throwable e) {
            throw new FileNotFoundPostcondition(args.getProductUrlArg().toString());
        }

        if (args.getExternalUrlRet() == null) {
            throw new PostconditionException("args.getExternalUrlRet()==null");
        }
    }
}
