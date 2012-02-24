/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

import java.io.File;
import java.io.FileOutputStream;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Collection;

import javax.faces.context.FacesContext;
import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.WindowState;
import javax.portlet.WindowStateException;

import com.ail.core.CoreProxy;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationSummary;
import com.ail.core.configure.server.CatalogCarService.CatalogCarCommand;
import com.ail.core.configure.server.GetConfigurationService.GetConfigurationCommand;
import com.ail.core.configure.server.GetNamespacesHistoryService.GetNamespacesHistoryCommand;
import com.ail.core.configure.server.PackageCarService.PackageCarCommand;
import com.ail.core.configure.server.ServerDeligate;
import com.ail.core.configure.server.SetConfigurationService.SetConfigurationCommand;

/**
 * Manager (controller) for the configure editor.
 */
public class ConfigureManager {
    private CoreProxy core;
    private ServerDeligate serverDeligate=null;
    
    public ConfigureManager() throws Exception {
        core=new CoreProxy();
        Principal p=FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        core.setSecurityPrincipal(p);
        serverDeligate=new ServerDeligate(core.getSecurityPrincipal());
    }

    public String resetAllAction() {
        try {
            serverDeligate.resetNamedConfiguration("all");
            core.setVersionEffectiveDateToNow();
            //getForm().refresh();

            return "resetAllAction.success";
        }
        catch(Throwable e) {
            return fail("resetAllAction.failure", e);
        }
    }
    
    public String clearCacheAction() {
        try {
            serverDeligate.clearConfigurationCache();
            core.setVersionEffectiveDateToNow();
            return "clearCacheAction.success";
        }
        catch(Throwable t) {
            return fail("clearCacheAction.failure", t);
        }
    }

    public String resetConfigurationAction() {
        try {
            ConfigurationSummary selected=(ConfigurationSummary)getForm().getNamespaces().getRowData();
            getForm().setSelected(selected);
            core.setVersionEffectiveDateToNow();

            if ("com.ail.core.Core".equals(selected.getNamespace())) {
                serverDeligate.resetCoreConfiguration();
            }
            else {
                serverDeligate.resetNamedConfiguration(selected.getManager());
            }

            getForm().refresh();
            
            return "resetConfigurationAction.success";
        }
        catch(Throwable t) {
            return fail("resetConfigurationAction.failure", t);
        }
    }

    public String editorResetAction() {
        try {
            core.setVersionEffectiveDateToNow();
            loadSelectedConfiguration();
        }
        catch(Throwable t) {
            return fail("editorResetAction.failure", t);
        }
            
        return "editorResetAction.success";
    }

    public String uploadCarFileAction() {
        if (getForm().getUploadedCarFileAvailable()) {
            try {
                CatalogCarCommand ccc=core.newCommand(CatalogCarCommand.class);
                ccc.setCarArg(getForm().getUploadedCarFile().getBytes());
                ccc.invoke();
            }
            catch (Exception ex) {
                return fail("uploadCarFileAction.failed", ex);
            }
        }
        return null;
    }
    
    public String uploadConfigurationAction() {
        getForm().setUploadedCarFile(null);
        return "uploadConfigurationAction.success";
    }
    
    public String download() {
        getForm().setCarFileName(null);
        return "download.success";
    }
    
    public String downloadConfigurationAction() {
        try {
            getForm().refresh();
            return "downloadConfigurationAction.success";
        }
        catch(Throwable t) {
            return fail("downloadConfigurationAction.failed", t);
        }
    }
    
    public String historyFromListAction() {
        try {
            ConfigurationSummary selected=(ConfigurationSummary)getForm().getNamespaces().getRowData();
            getForm().setSelected(selected);        
            loadHistory();
            return "historyFromListAction.success";
        }
        catch(Throwable t) {
            return fail("historyFromListAction.failed", t);
        }
    }
    
    public String historyFromEditorAction() {
        try {
            loadHistory();
            return "historyFromEditorAction.success";
        }
        catch(Throwable t) {
            return fail("historyFromEditorAction.failed", t);
        }
    }
    
    private void loadHistory() throws Exception {
        GetNamespacesHistoryCommand gnhc=core.newCommand(GetNamespacesHistoryCommand.class);
        gnhc.setNamespaceArg(getForm().getSelected().getNamespace());
        gnhc.invoke();
        getForm().setHistory(gnhc.getNamespacesRet());
    }
    
    public String editLatestAction() {
        // get the details of the line that was selected
        ConfigurationSummary selected=(ConfigurationSummary)getForm().getNamespaces().getRowData();
        getForm().setSelected(selected);        

        // Make sure we get the latest configuration
        core.setVersionEffectiveDateToNow();

        try {
            loadSelectedConfiguration();
        }
        catch(Throwable t) {
            return fail("editLatestAction.failure", t);
        }

        return "editLatestAction.success";
    }
    
    public String viewHistoricalAction() {
        // get the details of the line that was selected
        ConfigurationSummary selected=(ConfigurationSummary)getForm().getHistory().getRowData();
        getForm().setSelected(selected);        

        // Make sure we get the specified version configuration
        core.setVersionEffectiveDate(new VersionEffectiveDate(selected.getValidFrom()));

        try {
            loadSelectedConfiguration();
        }
        catch(Throwable t) {
            return fail("viewHistoricalAction.failure", t);
        }

        return "viewHistoricalAction.success";
    }
    
    public String editorSaveAction() {
        ConfigurationSummary selected=getForm().getSelected();
        
        try {
            Configuration config=core.fromXML(Configuration.class, new XMLString(getForm().getConfigurationXML()));
            SetConfigurationCommand scc=core.newCommand(SetConfigurationCommand.class);
            scc.setNamespaceArg(selected.getNamespace());
            scc.setConfigurationArg(config);
            scc.invoke();
        }
        catch(Throwable t) {
            return fail("editorSaveAction.failure", t);
        }

        return "editorSaveAction.success";
    }

    public String backToListAction() {
        try {
            getForm().refresh();
        }
        catch(Throwable t) {
            return fail("backToListAction.failure", t);
        }
        
        return "backToListAction.success";
    }

    public String restoreToAction() {
        return "restoreToAction.success";
    }
    
    /**
     * Action invoked when the "donwload selected" button on the download page is selected.
     */
    public String downloadSelectedAction() {
        @SuppressWarnings("unchecked")
        Collection<ConfigurationSummary> rows=(Collection<ConfigurationSummary>)getForm().getNamespaces().getWrappedData();

        getForm().setCarFileName(null);
        
        try {
            PackageCarCommand pcc=core.newCommand(PackageCarCommand.class);
            pcc.setNamespacesArg(new ArrayList<String>());
            
            for(ConfigurationSummary summary: rows ) {
                if (summary.getLock()) {
                    pcc.getNamespacesArg().add(summary.getNamespace());
                }
            }

            pcc.invoke();

            File carFile=File.createTempFile("CarDownload", "car");
            FileOutputStream fos=new FileOutputStream(carFile);
            fos.write(pcc.getCarRet());
            fos.close();
            
            getForm().setCarFileName(carFile.getAbsolutePath());
        }
        catch(Exception ex) {
            return fail("downloadSelectedAction.failed", ex);
        }
        
        // return to the current page
        return null;
    }
    
    public String maximizeDownloadAction() {
        Object resp=FacesContext.getCurrentInstance().getExternalContext().getResponse();

        if (resp instanceof ActionResponse) {
            try {
                ((ActionResponse)resp).setWindowState(WindowState.MAXIMIZED);
            }
            catch(WindowStateException ex) {
                core.logWarning("Window state exception:"+ex);
            }
        }
        
        return null;
    }
    
    /**
     * Determin if the portlet's portal window is maximized or not.
     * @return true if the winow is maximized, false otherwise
     */
    public boolean getIsWindowMaximized() {
        Object req=FacesContext.getCurrentInstance().getExternalContext().getRequest();

        if (req instanceof RenderRequest) {
            return (WindowState.MAXIMIZED.equals(((RenderRequest)req).getWindowState()));
        }
        else if (req instanceof ActionRequest) {
            try {
                return (WindowState.MAXIMIZED.equals(((ActionRequest)req).getWindowState()));
            }
            catch(NullPointerException e) {
                // This is a bit shit. In jboss 4.0.3SP1/portal 2.2, the above getWindowState
                // sometimes throws a NullPointer. As it happen, this is only ever the case when
                // the window is maximised, so this kind of okay.
                return true;
            }
        }
        
        return false;
    }

    private String fail(String ret, Throwable t) {
        core.logError(ret+" "+t);
        getForm().setError(t);
        return ret;
    }

    private ConfigureForm getForm() {
        Object form=FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("form");
        return (ConfigureForm)form;
    }

    private void loadSelectedConfiguration() throws Exception {
        // Get the configuration
        GetConfigurationCommand gcc=core.newCommand(GetConfigurationCommand.class);
        gcc.setNamespaceArg(getForm().getSelected().getNamespace());
        gcc.invoke();
        
        // Store the result in the Form as an XML String
        getForm().setConfigurationXML(core.toXML(gcc.getConfigurationRet()).toString());
    }
}
