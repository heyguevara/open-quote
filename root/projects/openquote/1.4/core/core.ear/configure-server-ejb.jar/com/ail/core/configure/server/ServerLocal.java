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

import javax.ejb.EJBException;
import javax.ejb.EJBLocalObject;

import org.w3c.dom.Element;

import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.finder.GetClassListCommand;

public interface ServerLocal extends EJBLocalObject {
    /** @link dependency */
    /*# ServerBean lnkServerBean; */

    VersionEffectiveDate getVersionEffectiveDate() throws EJBException;

    void setConfiguration(Configuration config);

    Configuration getConfiguration();

    String getConfigurationNamespace();

    void resetConfiguration();

    void resetCoreConfiguration() throws EJBException;

    void resetNamedConfiguration(String name) throws EJBException;

    void clearConfigurationCache() throws EJBException;

    String invokeServiceXML(String xml) throws EJBException;

    Element[] invokeServiceSoap(Element[] xml) throws EJBException;

    GetNamespacesCommand getNamespaces(GetNamespacesCommand arg) throws EJBException;

    GetConfigurationCommand getConfiguration(GetConfigurationCommand arg) throws EJBException;

    SetConfigurationCommand setConfiguration(SetConfigurationCommand arg) throws EJBException;

    GetCommandScriptCommand getCommandScript(GetCommandScriptCommand arg) throws EJBException;

    SetCommandScriptCommand setCommandScript(SetCommandScriptCommand arg) throws EJBException;

    GetClassListCommand getClassList(GetClassListCommand arg) throws EJBException;

    DeployCarCommand deployCar(DeployCarCommand arg) throws EJBException;
    
    PackageCarCommand packageCar(PackageCarCommand arg) throws EJBException;

    CatalogCarCommand catalogCar(CatalogCarCommand arg) throws EJBException;
}
