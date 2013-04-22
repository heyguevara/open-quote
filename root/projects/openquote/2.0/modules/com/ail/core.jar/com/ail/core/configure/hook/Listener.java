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
package com.ail.core.configure.hook;

import com.ail.core.CoreProxy;
import com.ail.core.product.ClearProductCacheService.ClearProductCacheCommand;
import com.ail.core.product.ResetProductService.ResetProductCommand;
import com.liferay.portal.ModelListenerException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.model.BaseModelListener;
import com.liferay.portlet.documentlibrary.NoSuchFileEntryException;
import com.liferay.portlet.documentlibrary.model.DLFileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolder;
import com.liferay.portlet.documentlibrary.service.DLFileEntryServiceUtil;

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

    Listener(CoreProxy proxy) {
        core = proxy;
        queuedResetProduct = core.newCommand("QueuedResetProductCommand", ResetProductCommand.class);
        queuedClearProductCache = core.newCommand("QueuedClearProductCacheCommand", ClearProductCacheCommand.class);
    }

    public Listener() {
        this(new CoreProxy());
    }

    // Wrapper for static method call to aid testing
    void recordChange(DLFileEntry fileEntry) {
        changeDetector.record(fileEntry);
    }

    // Wrapper for static method call to aid testing
    boolean isChanged(DLFileEntry fileEntry) {
        return changeDetector.isChanged(fileEntry);
    }

    // Wrapper for static method call to aid testing
    DLFileEntry getFileEntry(long groupId, long folderId, String title) throws PortalException, SystemException {
        return DLFileEntryServiceUtil.getFileEntry(groupId, folderId, title);
    }

    void resetProductForFileEntry(DLFileEntry fileEntry) throws Exception {
        core.setVersionEffectiveDateToNow();
        String productName = fileEntry2ProductName(fileEntry.getFolder());
        queuedResetProduct.setCallersCore(core);
        queuedResetProduct.setProductNameArg(productName);
        queuedResetProduct.invoke();
    }

    void clearProductCacheForFileEntry(DLFileEntry fileEntry) throws Exception {
        core.setVersionEffectiveDateToNow();
        String productName = fileEntry2ProductName(fileEntry.getFolder());
        queuedClearProductCache.setCallersCore(core);
        queuedClearProductCache.setProductNameArg(productName);
        queuedClearProductCache.invoke();
    }

    void handleOnAfterEvent(DLFileEntry fileEntry, boolean allowResetProduct) throws ModelListenerException {
        String fullPath = fileEntry2FullPath(fileEntry);

        try {
            if (fullPath.startsWith("/Product/")) {
                if (isChanged(fileEntry)) {
                    if (allowResetProduct && fullPath.endsWith("/Registry.xml")) {
                        resetProductForFileEntry(fileEntry);
                    }

                    clearProductCacheForFileEntry(fileEntry);
                }
            }
        } catch (Exception e) {
            throw new ModelListenerException("Failed to handle product file update.", e);
        }
    }
    
    @Override
    public void onBeforeUpdate(DLFileEntry fileEntry) throws ModelListenerException {
        String fullPath = fileEntry2FullPath(fileEntry);

        if (fullPath.startsWith("/Product/")) {
            recordChange(fileEntry);
        }
    }

    @Override
    public void onAfterRemove(DLFileEntry fileEntry) throws ModelListenerException {
        handleOnAfterEvent(fileEntry, false);
    }

    @Override
    public void onAfterCreate(DLFileEntry fileEntry) throws ModelListenerException {
        handleOnAfterEvent(fileEntry, true);
    }

    @Override
    public void onAfterUpdate(DLFileEntry fileEntry) throws ModelListenerException {
        handleOnAfterEvent(fileEntry, true);
    }

    /**
     * Determine if a folder is the root of a product. If the folder contains a
     * Registry.xml file it is assumed to be a product root.
     * 
     * @param folderEntry
     *            folder to check.
     * @return true if the folder is a product root; false otherwise.
     * @throws Exception
     */
    boolean isFolderAProductRoot(DLFolder folderEntry) throws Exception {
        try {
            // try to get the Registry from the current folder - this will throw
            // a NoSuchFileEntryException if the file doesn't exit.
            getFileEntry(folderEntry.getGroupId(), folderEntry.getFolderId(), "Registry.xml");

            // a Registry.xml exists, so this is a product folder
            return true;

        } catch (NoSuchFileEntryException e) {
            return false;
        }
    }

    /**
     * Convert a CMS full path into a product name. For example:
     * "/Product/AIL/Base/House/File.xml" becomes: "AIL.Base.House"
     * 
     * @param fullPath
     * @return Product name.
     */
    String fileEntry2ProductName(DLFolder folderEntry) throws Exception {
        if (folderEntry == null) {
            return null;
        }

        if (isFolderAProductRoot(folderEntry)) {

            // if we get here, Registry.xml must exist which means that this is
            // the product folder
            StringBuffer productName = new StringBuffer(folderEntry.getPath());

            // delete the "/Product/" from the front of the name
            productName.delete(0, 9);

            // replace all '/' chars with '.'
            for (int i = 0; i < productName.length(); i++) {
                if (productName.charAt(i) == '/') {
                    productName.setCharAt(i, '.');
                }
            }

            return productName.toString();
        } else {
            // stop now if we've reached the root folder, otherwise iterate into
            // the parent folder.
            if (folderEntry.isRoot()) {
                throw new IllegalStateException("Cannot determin the product name for a folder. The folder does not appear to be in the product hierarchy.");
            } else {
                return fileEntry2ProductName(folderEntry.getParentFolder());
            }
        }
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

            return fileEntry.getFolder().getPath() + "/" + fileEntry.getTitle();

        } catch (Exception e) {
            throw new ModelListenerException("Failed to derive full path for: " + fileEntry);
        }
    }
}
