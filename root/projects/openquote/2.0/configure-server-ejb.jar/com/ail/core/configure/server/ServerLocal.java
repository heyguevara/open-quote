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

import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.finder.GetClassListCommandImpl;

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

    GetNamespacesCommandImpl getNamespaces(GetNamespacesCommandImpl arg);

    GetConfigurationCommandImpl getConfiguration(GetConfigurationCommandImpl arg);

    SetConfigurationCommandImpl setConfiguration(SetConfigurationCommandImpl arg);

    GetCommandScriptCommandImpl getCommandScript(GetCommandScriptCommandImpl arg);

    SetCommandScriptCommandImpl setCommandScript(SetCommandScriptCommandImpl arg);

    GetClassListCommandImpl getClassList(GetClassListCommandImpl arg);

    DeployCarCommandImpl deployCar(DeployCarCommandImpl arg);
    
    PackageCarCommandImpl packageCar(PackageCarCommandImpl arg);

    CatalogCarCommandImpl catalogCar(CatalogCarCommandImpl arg);
}
