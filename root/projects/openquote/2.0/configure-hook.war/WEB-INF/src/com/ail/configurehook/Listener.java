/* Copyright Applied Industrial Logic Limited 2013. All rights reserved. */
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
package com.ail.configurehook;

import com.ail.core.BaseException;
import com.ail.core.CoreProxy;
import com.ail.core.product.ClearProductCacheService.ClearProductCacheCommand;
import com.ail.core.product.ResetProductService.ResetProductCommand;
import com.liferay.portal.ModelListenerException;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;

/**
 * This listener is called by Liferay in response to changes in the document
 * library. It's purpose is to determine if those changes relate to files that
 * OpenQuote is sensitive to. For example, changes to files named "Registry.xml"
 * that are within the product hierarchy automatically lead to the corresponding
 * configuration namespace in OpenQuote being reset and changes to any other
 * file type in the product structure leads to the owning product's
 * configuration cache being cleared so that the changes will be picked up when
 * the file is next referenced.
 */
public class Listener extends BaseModelListener<DLFileEntry> {
    static ChangeDetector changeDetector = new ChangeDetector();
    private ResetProductCommand queuedResetProduct;
    private ClearProductCacheCommand queuedClearProductCache;
    private CoreProxy core;

    public Listener(CoreProxy proxy) {
        core = proxy;
        queuedResetProduct = core.newCommand("QueuedResetProductCommand", ResetProductCommand.class);
        queuedClearProductCache = core.newCommand("QueuedClearProductCacheCommand", ClearProductCacheCommand.class);
    }

    public Listener() {
        this(new CoreProxy());
    }

    @Override
    public void onAfterCreate(DLFileEntry fileEntry) throws ModelListenerException {

        String fullPath = fileEntry2FullPath(fileEntry);

        if (fullPath.startsWith("/Product/")) {
            if (changeDetector.isChanged(fileEntry)) {
                try {
                    core.setVersionEffectiveDateToNow();

                    core.logInfo("Product file creation detected for " + fullPath);

                    String productName = fullPath2ProductName(fullPath);

                    if (fullPath.endsWith("/Registry.xml")) {
                        queuedResetProduct.setCallersCore(core);
                        queuedResetProduct.setProductNameArg(productName);
                        queuedResetProduct.invoke();
                    }

                    queuedClearProductCache.setCallersCore(core);
                    queuedClearProductCache.setProductNameArg(productName);
                    queuedClearProductCache.invoke();

                } catch (BaseException e) {
                    throw new ModelListenerException("Failed to handle product file update.", e);
                }
            }
        }
    }

    @Override
    public void onAfterRemove(DLFileEntry fileEntry) throws ModelListenerException {

        String fullPath = fileEntry2FullPath(fileEntry);

        if (fullPath.startsWith("/Product/")) {
            if (changeDetector.isChanged(fileEntry)) {
                try {
                    core.setVersionEffectiveDateToNow();

                    core.logInfo("File removal detected for " + fullPath);

                    String productName = fullPath2ProductName(fullPath);

                    queuedClearProductCache.setCallersCore(core);
                    queuedClearProductCache.setProductNameArg(productName);
                    queuedClearProductCache.invoke();

                    for (String namespace : queuedClearProductCache.getNamespacesRet()) {
                        core.logInfo("Cleared cache for namespace: " + namespace);
                    }
                } catch (BaseException e) {
                    throw new ModelListenerException("Failed to handle product file update.", e);
                }
            }
        }
    }

    @Override
    public void onBeforeUpdate(DLFileEntry fileEntry) throws ModelListenerException {
        String fullPath = fileEntry2FullPath(fileEntry);

        if (fullPath.startsWith("/Product/")) {
            changeDetector.record(fileEntry);
        }
    }

    @Override
    public void onAfterUpdate(DLFileEntry fileEntry) throws ModelListenerException {

        String fullPath = fileEntry2FullPath(fileEntry);

        if (fullPath.startsWith("/Product/")) {
            if (changeDetector.isChanged(fileEntry)) {
                try {
                    core.setVersionEffectiveDateToNow();

                    core.logInfo("File update detected for : " + fullPath + ", version=" + fileEntry.getVersion());

                    String productName = fullPath2ProductName(fullPath);

                    if (fullPath.endsWith("/Registry.xml")) {
                        queuedResetProduct.setCallersCore(core);
                        queuedResetProduct.setProductNameArg(productName);
                        queuedResetProduct.invoke();
                    }

                    queuedClearProductCache.setCallersCore(core);
                    queuedClearProductCache.setProductNameArg(productName);
                    queuedClearProductCache.invoke();
                } catch (BaseException e) {
                    throw new ModelListenerException("Failed to handle product file update.", e);
                }
            }
        }
    }

    /**
     * Convert a CMS full path into a product name. For example:
     * "/Product/AIL/Base/House/File.xml" becomes: "AIL.Base.House"
     * 
     * @param fullPath
     * @return Product name.
     */
    String fullPath2ProductName(String fullPath) {
        if (fullPath == null) {
            return null;
        }

        StringBuilder path = new StringBuilder(fullPath);

        // delete the "/Product/" from the front of the name
        path.delete(0, 9);

        // delete the file name from the end of the path
        int indx = path.lastIndexOf("/");
        if (indx != -1) {
            path.setLength(indx);
        }

        // replace all '/' chars with '.'
        for (int i = 0; i < path.length(); i++) {
            if (path.charAt(i) == '/') {
                path.setCharAt(i, '.');
            }
        }

        return path.toString();
    }

    /**
     * Derive the full path for a file based on it's title and iterating up the
     * parent folders to get their folder names.
     * 
     * @param fileEntry
     * @return
     * @throws ModelListenerException
     */
    String fileEntry2FullPath(DLFileEntry fileEntry) throws ModelListenerException {
        try {
            if (fileEntry == null) {
                return null;
            }

            StringBuilder path = new StringBuilder();

            path.append('/').append(fileEntry.getTitle());

            for (DLFolder folder = fileEntry.getFolder(); folder != null; folder = folder.getParentFolder()) {
                path.insert(0, folder.getName());
                path.insert(0, '/');
            }

            return path.toString();
        } catch (Exception e) {
            throw new ModelListenerException("Failed to derive full path for: " + fileEntry);
        }
    }
}
