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

import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

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

	Version getVersion() throws EJBException;

    String invokeServiceXML(String xml) throws EJBException;

    Element[] invokeServiceSoap(Element[] xml) throws EJBException;

    GetNamespacesArg getNamespaces(GetNamespacesArg arg) throws EJBException;

    GetConfigurationArg getConfiguration(GetConfigurationArg arg) throws EJBException;

    SetConfigurationArg setConfiguration(SetConfigurationArg arg) throws EJBException;

    GetCommandScriptArg getCommandScript(GetCommandScriptArg arg) throws EJBException;

    SetCommandScriptArg setCommandScript(SetCommandScriptArg arg) throws EJBException;

    DeployCarArg deployCar(DeployCarArg arg) throws EJBException;
    
    PackageCarArg packageCar(PackageCarArg arg) throws EJBException;

    CatalogCarArg catalogCar(CatalogCarArg arg) throws EJBException;
}
