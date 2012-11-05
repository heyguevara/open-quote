/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
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

import java.security.Principal;

import javax.ejb.EJB;

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
 * This class acts as a business delegate to the ConfigureServer EJB.
 */
public class ServerDeligate {
    @EJB
    private Server server;
    private Principal user=null;
    private String username=null;
    private String password=null;

    public ServerDeligate(Principal principal) {
        this.user=principal;
    }
    
    public ServerDeligate(String username, String password) {
        this.username=username;
        this.password=password;
    }
    
    public void resetCoreConfiguration() throws Exception {
        server.resetCoreConfiguration();
    }

    public void resetNamedConfiguration(String name) throws Exception {
        server.resetNamedConfiguration(name);
    }

    public void resetAllConfigurations() throws Exception {
        server.resetNamedConfiguration("all");
    }

    public void clearConfigurationCache() throws Exception {
        server.clearConfigurationCache();
    }

    public void clearNamedConfigurationCache(String namespace) throws Exception {
        server.clearNamedConfigurationCache(namespace);
    }

    public String invokeServiceXML(String xml) throws Exception {
        return server.invokeServiceXML(xml);
    }

    public GetNamespacesCommand getNamespaces(GetNamespacesCommand arg) throws Exception {
        return server.getNamespaces(arg);
    }

    public GetConfigurationCommand getConfiguration(GetConfigurationCommand arg) throws Exception {
        return server.getConfiguration(arg);
    }

    public SetConfigurationCommand setConfiguration(SetConfigurationCommand arg) throws Exception {
        return server.setConfiguration(arg);
    }

    public GetCommandScriptCommand getCommandScript(GetCommandScriptCommand arg) throws Exception {
        return server.getCommandScript(arg);
    }

    public SetCommandScriptCommand setCommandScript(SetCommandScriptCommand arg) throws Exception {
        return server.setCommandScript(arg);
    }

    public GetClassListCommand getClassList(GetClassListCommand arg) throws Exception  {
        return server.getClassList(arg);
    }

    public DeployCarCommand deployCar(DeployCarCommand arg) throws Exception  {
        return server.deployCar(arg);
    }

    public PackageCarCommand packageCar(PackageCarCommand arg) throws Exception  {
        return server.packageCar(arg);
    }

    public CatalogCarCommand catalogCar(CatalogCarCommand arg) throws Exception  {
        return server.catalogCar(arg);
    }

    public Principal getUser() {
        return user;
    }
}
