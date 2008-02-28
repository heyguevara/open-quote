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
package org.alfresco.web.app.portlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletSecurityException;
import javax.portlet.PortletURL;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

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
import org.springframework.web.context.WebApplicationContext;

@SuppressWarnings("deprecation")
public class ContentBrowserPortlet extends GenericPortlet {
    private String indexpage;
    private static String storeValue = "workspace://SpacesStore";
    private static String rootPath = "/app:company_home";
    private ServiceRegistry serviceRegistry;
    private NodeRef rootNodeRef;

    private static final String regex = 
              "((?:href|src)\\s*=\\s*)     # Capture preliminaries in $1.  \n"
            + "(?:                         # First look for URL in quotes. \n"
            + "   ([\"\'])                 #   Capture open quote in $2.   \n"
            + "   (?!http:)                #   If it isn't absolute...     \n"
            + "   /?(.+?)                  #    ...capture URL in $3       \n"
            + "   \\2                      #   Match the closing quote     \n"
            + " |                          # Look for non-quoted URL.      \n"
            + "   (?![\"\']|http:)         #   If it isn't absolute...     \n"
            + "   /?([^\\s>]+)             #    ...capture URL in $4       \n"
            + ")";

    private static final Pattern RELATIVE_URI_PATTERN = Pattern.compile(regex, Pattern.MULTILINE | Pattern.CASE_INSENSITIVE | Pattern.COMMENTS);

    @Override
    public void init(PortletConfig pconfig) throws PortletException {
        super.init(pconfig);
        indexpage = this.getInitParameter("indexpage");
        PortletContext pc=pconfig.getPortletContext();
        WebApplicationContext ctx = (WebApplicationContext)pc.getAttribute(WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE);
        serviceRegistry = (ServiceRegistry) ctx.getBean(ServiceRegistry.SERVICE_REGISTRY);
    }

    public void processAction(final ActionRequest req, final ActionResponse resp) throws PortletException, PortletSecurityException, IOException
    {
       String path = req.getParameter("path");
       resp.setRenderParameter("path", path);
    }
    
    public void doView(final RenderRequest req, final RenderResponse resp) throws PortletException, PortletSecurityException, IOException {
        AuthenticationService authenticationService = serviceRegistry.getAuthenticationService();
        authenticationService.authenticate("admin", "admin".toCharArray());
        
        TransactionService transactionService = serviceRegistry.getTransactionService();
 
        TransactionWork<Object> work = new TransactionWork<Object>() {
            public Object doWork() throws Exception {
                fetchContent(req, resp, serviceRegistry);
                return null;
            }
        };

        TransactionUtil.executeInUserTransaction(transactionService, work);
    }
    
    private void fetchContent(RenderRequest req, RenderResponse resp, ServiceRegistry reg) throws IllegalStateException, IOException {
        String path = req.getParameter("path");
        StringBuffer dstHtml = new StringBuffer();
        FileFolderService fileFolderService = reg.getFileFolderService();
        FileInfo fileInfo = null;

        if (path==null) {
            path=req.getPreferences().getValue("indexpage", indexpage);
        }

        // split the path
        StringTokenizer token = new StringTokenizer(path, "/");
        List<String> pathElements = new ArrayList<String>(10);
        while (token.hasMoreTokens()) {
            pathElements.add(token.nextToken());
        }

        try {
            fileInfo = fileFolderService.resolveNamePath(getRootNodeRef(), pathElements);
        }
        catch (FileNotFoundException e) {
            resp.setContentType("text/html");
            resp.getWriter().print("CMS File not found: "+path);
            return;
        }

        // set the response's content type to the mime type from the file.
        resp.setContentType(fileInfo.getContentData().getMimetype());
        resp.setTitle(fileInfo.getName());
        
        ContentReader reader = fileFolderService.getReader(fileInfo.getNodeRef());

        // ensure that we generate something, even if the content is missing.
        reader = FileContentReader.getSafeContentReader((ContentReader)reader, "CMS content not found", fileInfo.getNodeRef(), reader);

        Matcher m = RELATIVE_URI_PATTERN.matcher(reader.getContentString());
        while (m.find()) {
            String relURI = m.group(3) != null ? m.group(3) : m.group(4);
            if (!relURI.startsWith("mailto")) {
                if ("href=".equals(m.group(1))) {
                    String absoluteURI = buildURL(resp, "/" + relURI);
                    m.appendReplacement(dstHtml, "$1\"" + absoluteURI + "\"");
                }
                else if ("src=".equals(m.group(1))) {
                    String absoluteURI = "http://"+req.getServerName()+":"+req.getServerPort()+"/alfresco/cms/"+relURI; 
                    m.appendReplacement(dstHtml, "$1\"" + absoluteURI + "\"");
                }
            }
        }
        m.appendTail(dstHtml);

        PrintWriter writer = resp.getWriter();
        writer.write(dstHtml.toString());
        writer.close();
    }
    
    /**
     * Rewrites urls. Typically, this is used for image src calls from the html, so they route thru the CMSObjectCommandMapper and
     * invoke the StreamObjectCommand. <p/> 
     */
    public String buildURL(RenderResponse resp, String path) {
        PortletURL url = resp.createActionURL();
        url.setParameter("path", path);
        return url.toString();
    }

    private NodeRef getRootNodeRef() {
        if (rootNodeRef==null) {

            SearchService searchService = serviceRegistry.getSearchService();
            NamespaceService namespaceService = serviceRegistry.getNamespaceService();
            NodeService nodeService = serviceRegistry.getNodeService();
    
            StoreRef storeRef = new StoreRef(storeValue);
            NodeRef storeRootNodeRef = nodeService.getRootNode(storeRef);
    
            List<NodeRef> nodeRefs = searchService.selectNodes(storeRootNodeRef, rootPath, null, namespaceService, false);
    
            rootNodeRef = nodeRefs.get(0);
        }
        
        return rootNodeRef;
    }
}
