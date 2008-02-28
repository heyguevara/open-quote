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
package org.alfresco.web.app.servlet;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.alfresco.repo.content.filestore.FileContentReader;
import org.alfresco.repo.transaction.TransactionUtil;
import org.alfresco.repo.transaction.TransactionUtil.TransactionWork;
import org.alfresco.service.ServiceRegistry;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.model.FileNotFoundException;
import org.alfresco.service.cmr.repository.ContentReader;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.repository.StoreRef;
import org.alfresco.service.cmr.search.SearchService;
import org.alfresco.service.cmr.security.AuthenticationService;
import org.alfresco.service.namespace.NamespaceService;
import org.alfresco.service.transaction.TransactionService;
import org.springframework.context.ApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 */
@SuppressWarnings("deprecation")
public class CmsContentServlet extends HttpServlet {
    private static final long serialVersionUID = -4558907921887235966L;
    private static String storeValue = "workspace://SpacesStore";
    private static String rootPath = "/app:company_home";
    private ServiceRegistry serviceRegistry;
    private NodeRef rootNodeRef;

    /**
     * Initialize the servlet
     * 
     * @param config ServletConfig
     * @exception ServletException
     */
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        if (serviceRegistry==null) {
            ApplicationContext ctx = WebApplicationContextUtils.getRequiredWebApplicationContext(config.getServletContext());
            serviceRegistry = (ServiceRegistry) ctx.getBean(ServiceRegistry.SERVICE_REGISTRY);
        }
        
    }
    
    protected void doGet(final HttpServletRequest req, final HttpServletResponse res) throws ServletException, IOException {
        // authenticate
        
        AuthenticationService authenticationService = getServiceRegistry().getAuthenticationService();
        authenticationService.authenticate("admin", "admin".toCharArray());
        
        TransactionService transactionService = getServiceRegistry().getTransactionService();
 
        TransactionWork<Object> work = new TransactionWork<Object>() {
            public Object doWork() throws Exception {
                fetchContent(req, res, getServiceRegistry());
                return null;
            }
        };

        TransactionUtil.executeInUserTransaction(transactionService, work);
    }

    private void fetchContent(HttpServletRequest req, HttpServletResponse res, ServiceRegistry serviceRegistry) throws UnsupportedEncodingException, IOException {
        FileFolderService fileFolderService = serviceRegistry.getFileFolderService();
        String strPath = URLDecoder.decode(req.getRequestURI(), "UTF-8");
        String servletPath = req.getServletPath();

        int rootPos = strPath.indexOf(servletPath);
        if (rootPos != -1) {
            strPath = strPath.substring(rootPos);
        }

        if (strPath == null) {
            strPath = req.getServletPath();
        }

        // If the path is still empty, make it a "/"; or if it ends with '/' chop if off.
        if (strPath == null || strPath.length() == 0) {
            strPath = "/";
        }
        else if (strPath.endsWith("/")) {
            strPath = strPath.substring(0, strPath.length() - 1);
        }

        // Check if the path starts with the base servlet path
        if (strPath.startsWith(req.getServletPath())) {
            int len = req.getServletPath().length();

            if (strPath.length() > len)
                strPath = strPath.substring(len);
            else
                strPath = "/";
        }

        // split the path
        StringTokenizer token = new StringTokenizer(strPath, "/");
        List<String> pathElements = new ArrayList<String>(10);
        while (token.hasMoreTokens()) {
            pathElements.add(token.nextToken());
        }

        FileInfo fileInfo=null;
        
        try {
            fileInfo = fileFolderService.resolveNamePath(getRootNodeRef(), pathElements);
        }
        catch (FileNotFoundException e) {
            res.sendError(HttpServletResponse.SC_NOT_FOUND, "CMS file not found: '"+strPath+"'");
            return;
        }

        ContentReader reader = fileFolderService.getReader(fileInfo.getNodeRef());

        // ensure that we generate something, even if the content is missing.
        reader = FileContentReader.getSafeContentReader((ContentReader) reader, "CMS content not found", fileInfo.getNodeRef(), reader);

        // set the response's content type to the mime type from the file.
        res.setContentType(fileInfo.getContentData().getMimetype());

        // get the content to the servlets output stream.
        reader.getContent(res.getOutputStream());

    }

    private NodeRef getRootNodeRef() {
        if (rootNodeRef==null) {

            SearchService searchService = getServiceRegistry().getSearchService();
            NamespaceService namespaceService = getServiceRegistry().getNamespaceService();
            NodeService nodeService = getServiceRegistry().getNodeService();
    
            StoreRef storeRef = new StoreRef(storeValue);
            NodeRef storeRootNodeRef = nodeService.getRootNode(storeRef);
    
            List<NodeRef> nodeRefs = searchService.selectNodes(storeRootNodeRef, rootPath, null, namespaceService, false);
    
            rootNodeRef = nodeRefs.get(0);
        }
        
        return rootNodeRef;
    }

    private ServiceRegistry getServiceRegistry() {
        return serviceRegistry;
    }
}
