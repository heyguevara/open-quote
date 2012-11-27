/* Copyright Applied Industrial Logic Limited 2005. All rights Reserved */
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
package com.ail.core.configure.server;

import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import org.w3c.dom.Element;

import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.finder.GetClassListService.GetClassListCommand;
import com.ail.core.configure.server.CatalogCarService.CatalogCarCommand;
import com.ail.core.configure.server.DeployCarService.DeployCarCommand;
import com.ail.core.configure.server.GetCommandScriptService.GetCommandScriptCommand;
import com.ail.core.configure.server.GetConfigurationService.GetConfigurationCommand;
import com.ail.core.configure.server.GetNamespacesService.GetNamespacesCommand;
import com.ail.core.configure.server.PackageCarService.PackageCarCommand;
import com.ail.core.configure.server.SetCommandScriptService.SetCommandScriptCommand;
import com.ail.core.configure.server.SetConfigurationService.SetConfigurationCommand;

/**
 * Web service endpoint for the configuration server. This endpoint simply
 * delegates to an instance of {@link com.ail.core.configure.server.ServerDeligate ServerDeligate} passing
 * on the XML String argument is was given along with the security principal that the caller supplied.
 */
@Stateless
public class ServerEndpointBean implements ServerLocal {
    private SessionContext sessionContext=null;
    private ServerDeligate serverDeligate=null;
    
    public void ejbCreate() {
    }
    
    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void setSessionContext(SessionContext ctx) {
        sessionContext=ctx;
    }

    private ServerDeligate getServerDeligate() {
        if (serverDeligate==null) {
            serverDeligate=new ServerDeligate(sessionContext.getCallerPrincipal());
        }
        
        return serverDeligate;
    }
    
    public String invokeServiceXML(String xml) {
        try {
            return getServerDeligate().invokeServiceXML(xml);
        }
        catch(Exception e) {
            // TODO better handling needed here!
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public VersionEffectiveDate getVersionEffectiveDate() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void setConfiguration(Configuration config) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Configuration getConfiguration() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public String getConfigurationNamespace() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void resetConfiguration() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetCoreConfiguration() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void resetNamedConfiguration(String name) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void clearConfigurationCache() {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Element[] invokeServiceSoap(Element[] xml) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetNamespacesCommand getNamespaces(GetNamespacesCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetConfigurationCommand getConfiguration(GetConfigurationCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SetConfigurationCommand setConfiguration(SetConfigurationCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetCommandScriptCommand getCommandScript(GetCommandScriptCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SetCommandScriptCommand setCommandScript(SetCommandScriptCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GetClassListCommand getClassList(GetClassListCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public DeployCarCommand deployCar(DeployCarCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public PackageCarCommand packageCar(PackageCarCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CatalogCarCommand catalogCar(CatalogCarCommand arg) {
        // TODO Auto-generated method stub
        return null;
    }
}
