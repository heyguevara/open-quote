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

/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
package com.ail.core.ui;

import java.util.Collection;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;

import org.apache.myfaces.custom.fileupload.UploadedFile;

import com.ail.core.CoreProxy;
import com.ail.core.configure.ConfigurationSummary;
import com.ail.core.configure.server.GetNamespacesCommand;

/**
 * Form (model) for the configure editor.
 * @version $Revision: 1.7 $
 * @state $State: Exp $
 * @date $Date: 2005/09/27 05:30:51 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/configure.war/WEB-INF/src/com/ail/core/ui/ConfigureForm.java,v $
 */
@SuppressWarnings("unchecked")
public class ConfigureForm {
    private CoreProxy core=new CoreProxy();
    private ListDataModel namespaces=new ListDataModel();
    private ListDataModel history=new ListDataModel();
    private String configurationXML;
    private ConfigurationSummary selected;
    private Throwable error;
    private String carFileName=null;
    private UploadedFile uploadedCarFile=null;
    
    public ConfigureForm() throws Exception {
        refresh();
    }

    public DataModel getNamespaces() {
        return namespaces;
    }

    public void setNamespaces(Collection namespaces) {
        this.namespaces.setWrappedData(namespaces);
    }
    
    public DataModel getHistory() {
        return history;
    }

    public void setHistory(Collection history) {
        this.history.setWrappedData(history);
    }
    
    public String getConfigurationXML() {
        return configurationXML;
    }

    public void setConfigurationXML(String configurationXML) {
        this.configurationXML = configurationXML;
    }

    public ConfigurationSummary getSelected() {
        return selected;
    }

    public void setSelected(ConfigurationSummary selected) {
        this.selected = selected;
    }

    public void refresh() throws Exception {
        GetNamespacesCommand gnc=(GetNamespacesCommand)core.newCommand("GetNamespaces");
        gnc.invoke();
        setNamespaces(gnc.getNamespaces());
    }

    public boolean getEditorDisabled() {
        // Editor disabled if: no configuration has been selected, or the selected one's
        // validTo != 0 (which indicates that it's a historical version).
        if (selected!=null && selected.getValidTo()==null) {
            return false;
        }
        else {
            return true;
        }
    }
        
    public void setError(Throwable t) {
        this.error=t;
    }
    
    public String getErrorMessage() {
        return error.toString();
    }

    public String getErrorStack() {
        StringWriter sw=new java.io.StringWriter();
        
        error.printStackTrace(new PrintWriter(sw));
        
        return sw.toString();
    }

    public String getCarFileName() {
        return carFileName;
    }

    public void setCarFileName(String carFileName) {
        this.carFileName = carFileName;
    }

    public boolean getCarFileAvailable() {
        return carFileName!=null;
    }

    public UploadedFile getUploadedCarFile() {
        return uploadedCarFile;
    }

    public void setUploadedCarFile(UploadedFile uploadCarFile) {
        this.uploadedCarFile = uploadCarFile;
    }

    public boolean getUploadedCarFileAvailable() {
        return this.uploadedCarFile!=null;
    }
}
