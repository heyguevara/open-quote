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
package com.ail.core.ui;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletContext;
import javax.portlet.PortletException;
import javax.portlet.PortletRequestDispatcher;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.portlet.PortletFileUpload;

import com.ail.core.CoreProxy;
import com.ail.core.configure.server.CatalogCarCommand;
import com.ail.core.configure.server.DeployCarCommand;

public class ConfigureUploadPortlet extends GenericPortlet {
    private CoreProxy core=new CoreProxy();
    private PortletContext pContext;

    public void init(PortletConfig config) throws PortletException {
        super.init(config);
        pContext = config.getPortletContext();
    }

    public void doView(RenderRequest request, RenderResponse response) throws PortletException, IOException {
        response.setContentType(request.getResponseContentType());

        String op = request.getParameter("op");

        if (op==null) {
            try {
                request.getPortletSession().setAttribute("form", new ConfigureUploadForm());
                PortletRequestDispatcher dispatcher = pContext.getRequestDispatcher("/pages/upload.jsp");
                dispatcher.include(request, response);
            } 
            catch (Exception e) {
                throw new PortletException("JSPPortlet.doView exception", e);
            }
        }
        else {
            try {
                PortletRequestDispatcher dispatcher = pContext.getRequestDispatcher("/pages/upload.jsp");
                dispatcher.include(request, response);
            } 
            catch (Exception e) {
                throw new PortletException("JSPPortlet.doView exception", e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public void processAction(ActionRequest request, ActionResponse actionResponse) throws PortletException, java.io.IOException {
        String op = request.getParameter("op");

        if ("fetch".equals(op)) {
            try {
                ConfigureUploadForm form=(ConfigureUploadForm)request.getPortletSession().getAttribute("form");
                
                form.setUploadedFile(readFileFromRequest(request));
                
                CatalogCarCommand ccc=(CatalogCarCommand)core.newCommand("CatalogCar");
                ccc.setCarArg(form.getUploadedFile());
                ccc.invoke();
                
                form.setNamespaceItems(ccc.getNamespacesRet());

                actionResponse.setWindowState(WindowState.MAXIMIZED);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        else  if ("deploy".equals(op)) {
            try {
                ConfigureUploadForm form=(ConfigureUploadForm)request.getPortletSession().getAttribute("form");
                Collection<String> toDeploy=new ArrayList<String>();
                String paramName;
                
                for(Enumeration<String> en=request.getParameterNames() ; en.hasMoreElements() ; ) {
                    paramName=(String)en.nextElement();
                    if ("true".equals(request.getParameter(paramName))) {
                        toDeploy.add(paramName);
                    }
                }

                DeployCarCommand dcc=(DeployCarCommand)core.newCommand("DeployCar");
                dcc.setCarArg(form.getUploadedFile());
                dcc.setNamespacesArg(toDeploy);
                dcc.invoke();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }

        actionResponse.setRenderParameters(request.getParameterMap());
    }

    private byte[] readFileFromRequest(ActionRequest request) throws FileUploadException, IOException {
        StringBuffer file=new StringBuffer();

        DiskFileItemFactory factory = new DiskFileItemFactory();
        PortletFileUpload upload = new PortletFileUpload(factory);

        @SuppressWarnings("unchecked")
        List<FileItem> fileItems = (List<FileItem>)upload.parseRequest(request);

        Iterator<FileItem> itr = fileItems.iterator();
        
        if (itr.hasNext()) {
            FileItem item = itr.next();
            byte[] block=new byte[4096];

            // check if the current item is a form field or an uploaded file
            if (!item.isFormField()) {
                InputStream is = item.getInputStream();
                
                for(int read=is.read(block) ; read>0 ; read=is.read(block)) {
                    file.append(new String(block, 0, read));
                }
            }
        }
        
        return file.toString().getBytes();
    }
}
