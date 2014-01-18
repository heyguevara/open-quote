/* Copyright Applied Industrial Logic Limited 2008. All rights reserved. */
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

package com.ail.core.urlhandler.product;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Arrays;
import java.util.Iterator;

import com.ail.core.CoreProxy;
import com.ail.core.RunAsProductReader;
import com.ail.core.ThreadLocale;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;

/**
 * The handler deals with URLs of the form: "product://". 
 */
public class Handler extends URLStreamHandler {
    private CoreProxy coreProxy;
    private Long repositoryId;
    private String root;

    public Handler() {
        super();

        coreProxy = new CoreProxy();

        repositoryId = new Long(getCoreProxy().getParameterValue("ProductReader.RepositoryID"));
        root = getCoreProxy().getParameterValue("ProductReader.Root");
    }

    protected URLConnection openConnection(URL productURL) throws IOException {
        DocumentLibraryURLConnection connection = new DocumentLibraryURLConnection(productURL);
        
        try {
            new RunAsProductReader() {
                DocumentLibraryURLConnection connection;
                URL url;
                
                RunAsProductReader with(URL url, DocumentLibraryURLConnection connection) {
                    this.connection = connection;
                    this.url=url;
                    return this;
                }
                
                @Override
                protected void doRun() throws Exception {
                    FileEntry fileEntry = locateFileEntry(url.getPath());

                    connection.setFileEntry(fileEntry);
                }
            }.with(productURL, connection).run();
        } catch (Throwable e) {
            throw new FileNotFoundException(productURL.toString());
        }
        
        return connection;
    }

    FileEntry locateFileEntry(String urlPath) throws PortalException, SystemException {
        String fullPath = root + "/" + urlPath;

        Iterator<String> pathElements = Arrays.asList(fullPath.split("/")).iterator();
            
        return findFileEntry(DLFolderConstants.DEFAULT_PARENT_FOLDER_ID, pathElements);
    }

    /**
     * Drill down in the document library to locate the file specified by <code>pathElements</code>. A
     * locale specific version of the file will be returned if one exists. 
     * @param folder Folder to start search from.
     * @param pathElements Strings making up the folder names of the path (empty strings will be ignored).
     * @return File that was located.
     * @throws PortalException No such file entry exists.
     * @throws SystemException A system error prevented the file being returned.
     */
    FileEntry findFileEntry(long folder, Iterator<String> pathElements) throws PortalException, SystemException {
        String name=null;
        
        // get next path element, skipping all intervening elements that are empty 
        for(name=pathElements.next() ; "".equals(name) ; name=pathElements.next());

        if (pathElements.hasNext()) {
            long folderId = DLAppServiceUtil.getFolder(repositoryId, folder, name).getFolderId();
            return findFileEntry(folderId, pathElements);
        } else {
            try {
                String language = ThreadLocale.getThreadLocale().getLanguage();
                return DLAppServiceUtil.getFileEntry(repositoryId, folder, name + "_" + language);
            } catch (PortalException ex) {
                return DLAppServiceUtil.getFileEntry(repositoryId, folder, name);
            }
        }
    }

    CoreProxy getCoreProxy() {
        return coreProxy;
    }
}
