/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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

import javax.ejb.Local;

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

@Local
public interface ServerLocal {
    VersionEffectiveDate getVersionEffectiveDate();

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    void resetCoreConfiguration();

    void resetNamedConfiguration(String name);

    void clearConfigurationCache();

    String invokeServiceXML(String xml);

    Element[] invokeServiceSoap(Element[] xml);

    GetNamespacesCommand getNamespaces(GetNamespacesCommand arg);

    GetConfigurationCommand getConfiguration(GetConfigurationCommand arg);

    SetConfigurationCommand setConfiguration(SetConfigurationCommand arg);

    GetCommandScriptCommand getCommandScript(GetCommandScriptCommand arg);

    SetCommandScriptCommand setCommandScript(SetCommandScriptCommand arg);

    GetClassListCommand getClassList(GetClassListCommand arg);

    DeployCarCommand deployCar(DeployCarCommand arg);
    
    PackageCarCommand packageCar(PackageCarCommand arg);

    CatalogCarCommand catalogCar(CatalogCarCommand arg);
}
