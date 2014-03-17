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
import java.util.Collection;
import java.util.Iterator;

import com.ail.core.CoreProxy;
import com.ail.core.RunAsProductReader;
import com.ail.core.ThreadLocale;
import com.ail.core.configure.ConfigurationHandler;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portlet.documentlibrary.model.DLFolderConstants;
import com.liferay.portlet.documentlibrary.service.DLAppServiceUtil;

/**
 * The handler deals with URLs of the form: "product://".
 * <p>
 * This handler is sensitive to the structure of the product namespace hierarchy
 * and will look for the content being referenced in the product namespace that
 * the URL implies, and in all of that namespace's parents.
 * </p>
 * <p>
 * A product URL contains within it the namespace of the product owning the
 * content. For example:
 * product://localhost:8080/AIL/Demo/LifePlus/Rules/SomeRulesResource.xml
 * addresses content in the AIL.Demo.LifePlus product namespace. Where a
 * namespace has a parent namespace, the content will be searched for using the
 * exact URL supplied, but also in that namespace's ancestors.
 * </p>
 * <p>
 * In the example above, the product has the parent AIL.Base. Therefore if the
 * content "Rules/SomeRulesResource.xml" is not found in AIL.Demo.LifePlus the
 * handler will search for it in AIL.Base - giving it the effective URL
 * product://localhost:8080/AIL/Base/Rules/SomeRuleResource.xml.
 * </p>
 */
public class Handler extends URLStreamHandler {
    private Long repositoryId;
    private String root;

    public Handler() {
        this(new CoreProxy());
    }
    
    Handler(CoreProxy coreProxy) {
        this(new Long(coreProxy.getParameterValue("ProductReader.RepositoryID")), coreProxy.getParameterValue("ProductReader.Root"));
    }
    
    Handler(Long repositoryId, String root) {
        super();
        this.repositoryId = repositoryId;
        this.root = root;
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
                    FileEntry fileEntry;
                    if (url.getPath().endsWith("/Registry.xml")) {
                        fileEntry = locateFileEntry(url.getPath());
                    }
                    else {
                        fileEntry = locateFileEntryInNamespaceHierarchy(url.getPath());
                    }

                    connection.setFileEntry(fileEntry);
                }
            }.with(productURL, connection).run();
        } catch (Throwable e) {
            throw new FileNotFoundException(productURL.toString());
        }
        
        return connection;
    }

    /**
     * Locate a file entry by walking up the namespace ancestor tree.
     * @param urlPath
     * @return Located file. 
     * @throws PortalException The file was not found locally or in any ancestor namespace.
     * @throws SystemException A system error occurred during the search
     */
    FileEntry locateFileEntryInNamespaceHierarchy(String urlPath) throws PortalException, SystemException {
        String namespace = findNamespaceForUrlPath(urlPath);
        return locateFileEntryFromNamespace(urlPath, namespace);
    }

    String findNamespaceForUrlPath(String urlPath) throws PortalException {
        for (String namespace : fetchNamespaces()) {
            String namespacePath = namespace.replace('.', '/').replace("/Registry", "/");
            if (namespace.endsWith(".Registry") && urlPath.contains(namespacePath)) {
                return namespace;
            }
        }

        throw new PortalException("Product URL path (" + urlPath + ") is not withing a product.");
    }

    FileEntry locateFileEntryFromNamespace(String urlPath, String namespace) throws PortalException {
        String namespacePath = namespace.replace('.', '/').replace("/Registry", "");

        CoreProxy cp = createCoreProxyForNamespace(namespace);

        for (String ancestorNamespace : cp.getConfigurationNamespaceParent()) {

            String ancestorNamespacePath = ancestorNamespace.replace('.', '/');

            String ancestorUrlPath = urlPath.replaceAll(namespacePath, ancestorNamespacePath).replace("/Registry/", "/");

            try {
                return locateFileEntry(ancestorUrlPath);
            } catch (Throwable th) {
                // Ignore this. continue to loop through the other ancestors
            }
        }

        throw new PortalException("File not found: " + urlPath);
    }
    
    /**
     * Locate a file based on its urlPath.
     * @param urlPath   
     * @return
     * @throws PortalException File could not be found.
     * @throws SystemException An error occurred during the search.
     */
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
            long folderId = getFolderId(folder, name);
            return findFileEntry(folderId, pathElements);
        } else {
            try {
                return getFileEntry(folder, name + "_" + getThreadLanguage());
            } catch (PortalException ex) {
                return getFileEntry(folder, name);
            }
        }
    }

    String getThreadLanguage() {
        return ThreadLocale.getThreadLocale().getLanguage();
    }

    FileEntry getFileEntry(long folder, String name) throws PortalException, SystemException {
        return DLAppServiceUtil.getFileEntry(repositoryId, folder, name);
    }

    Long getFolderId(long folder, String name) throws PortalException, SystemException {
        return DLAppServiceUtil.getFolder(repositoryId, folder, name).getFolderId();
    }

    Collection<String> fetchNamespaces() {
        return ConfigurationHandler.getInstance().getNamespaces();
    }

    CoreProxy createCoreProxyForNamespace(String namespace) {
        return new CoreProxy(namespace);
    }
}
