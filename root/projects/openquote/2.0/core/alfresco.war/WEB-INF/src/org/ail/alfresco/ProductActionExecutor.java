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
package org.ail.alfresco;

import java.net.URL;
import java.util.List;

import org.alfresco.model.ContentModel;
import org.alfresco.repo.action.executer.ActionExecuterAbstractBase;
import org.alfresco.service.cmr.action.Action;
import org.alfresco.service.cmr.action.ParameterDefinition;
import org.alfresco.service.cmr.model.FileFolderService;
import org.alfresco.service.cmr.model.FileInfo;
import org.alfresco.service.cmr.repository.NodeRef;
import org.alfresco.service.cmr.repository.NodeService;
import org.alfresco.service.cmr.security.PermissionService;
import org.alfresco.web.app.servlet.UploadFileServlet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Alfresco action executor which listens for events in the repository which OpenQuote
 * needs to be made aware of. When events are detected, the Core's services are invoked,
 * via the configureOperationServlet, to inform it of the changes.
 * We are interested in two types of changes:<ul>
 * <li><b>Registry.xml updates</b> When a change is detected to any file named Registry.xml 
 * the configure server is informed. Typically it will respond by resetting (i.e. reloading)
 * that registry, and clearing any cache associated with it.</li>
 * <li><b>Content changes</b> Any piece of content which changes the configure server is
 * informed that a product has been updated. The configure server will respond by clearing
 * the product's cache so that the changes made are picked up. The relationship between a
 * piece of content and the product or products which reference it may be complex, indeed
 * there is no way for this executor to determine for sure which products need to be reset.
 * Consider that the content referenced by a product may be anywhere in the CMS system, and
 * that content may reference other pieces of content (XML includes etc). This executor 
 * makes the assumption that a content change in a product's folder, or in any space below
 * that folder should cause that product's cache to be cleared.
 * </ul>
 * Note: The core's configure service are invoked via the configureOperationServlet servlet.
 * The servlet's base URL is injected into this class by spring.   
 * @since 2.0
 */
public class ProductActionExecutor extends ActionExecuterAbstractBase {
    private static final Log logger = LogFactory.getLog(UploadFileServlet.class); 

    private NodeService nodeService = null;
    private PermissionService permissionService = null;
    private FileFolderService fileFolderService = null;
    private String configureRootUrl = null;

    public void setNodeService(NodeService nodeService) {
        this.nodeService = nodeService;
    }

    public void setFileFolderService(FileFolderService fileFolderService) {
        this.fileFolderService = fileFolderService;
    }

    public void setPermissionService(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @Override
    protected void executeImpl(Action action, NodeRef node) {
        if (nodeService.exists(node)) {
            String nodeName = (String)nodeService.getProperty(node, ContentModel.PROP_NAME);

            if (nodeName!=null) {
                if (nodeName.endsWith("Registry.xml")) { 
                    String productPath=nodeRefToProductName(node);
                    resetProduct(productPath);
                    clearCache(productPath);
                }
                else if (!nodeName.contains("(Working Copy)")) {
                    NodeRef productNodeRef=findProductRegistryNode(nodeService.getPrimaryParent(node).getParentRef());
                    if (productNodeRef!=null) {
                        clearCache(nodeRefToProductName(productNodeRef));
                    }
                }
            }
        }
    }

    @Override
    protected void addParameterDefinitions(List<ParameterDefinition> arg0) {
        // do nothing, there are no parameters for this executor
    }

    /**
     * Walk the folder tree up from <i>node</i> looking for a Registry.xml. If one is found, 
     * return it. If we reach the "Product" space without finding one, give up.
     * @param mode
     *            node to find the product name for
     * @return node containing the Registry.xml, or null if none is found.
     */
    private NodeRef findProductRegistryNode(NodeRef node) {
        String nodeName = (String)nodeService.getProperty(node, ContentModel.PROP_NAME);

        if (fileFolderService.getFileInfo(node).isFolder()) {
            if ("Product".equals(nodeName)) {
                return null;
            }
            
            for(FileInfo file: fileFolderService.listFiles(node)) {
                NodeRef ret=findProductRegistryNode(file.getNodeRef());
                if (ret!=null) {
                    return ret;
                }
            }

           return findProductRegistryNode(nodeService.getPrimaryParent(node).getParentRef());
        }
        else {    
            if ("Registry.xml".equals(nodeName)) {
                return node;
            }
            else {
                return null;
            }
        }
    }
    
    /**
     * Translate a node reference into a product name. For example, a node ref pointing at
     * "Company/Product/My/Demo/Thing", would return "My.Demo.Thing".
     * @param node Node to translate
     * @return Product name.
     * @throws IllegalArgumentException if the node cannot be translated. 
     */
    private String nodeRefToProductName(NodeRef node) {
        String spaceName = nodeService.getPath(node).toDisplayPath(nodeService, permissionService);
      
        int startAt = spaceName.indexOf("/Product") + 9;
        
        if (startAt < 9) {
            throw new IllegalArgumentException("The content space '" + spaceName + "' cannot be converted into a product name as it is not a child of the 'Product' space.");
        }
        
        spaceName = spaceName.substring(startAt).replace('/', '.');
        
        return (spaceName.charAt(0)=='.' ? spaceName.substring(1) : spaceName);
    }

    /**
     * Invoke the configure system's product reset service for a specific product.
     * @param productName Product to invoke the service for
     */
    private void resetProduct(String productName) {
        try {
            new URL(getConfigureRootUrl()+"/configureOperationServlet?op=resetProduct&productName=" + productName).getContent();
        } catch (Exception e) {
            logger.error("Failed to reset product '" + productName + "'.", e);
        }
    }

    /**
     * Invoke the configure system's cache clearing service for a specific product.
     * @param productName Product to invoke the service for
     */
    private void clearCache(String productName) {
        try {
            new URL(getConfigureRootUrl()+"/configureOperationServlet?op=clearProductCache&productName=" + productName).getContent();
        } catch (Exception e) {
            logger.error("Failed to clear cache for product '" + productName + "'.", e);
        }
    }

    public void setConfigureRootUrl(String configureRootUrl) {
        this.configureRootUrl = configureRootUrl;
    }

    public String getConfigureRootUrl() {
        return configureRootUrl;
    }
}
