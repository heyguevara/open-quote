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

package com.ail.core.configure;

import java.util.Collection;

import javax.ejb.Local;

import com.ail.core.VersionEffectiveDate;

@Local
public interface EJBLoaderLocal {
    public Configuration loadConfiguration(String namespace, VersionEffectiveDate date) throws EJBConfigurationException;

    public void saveConfiguration(String namespace, Configuration config) throws EJBConfigurationException;

    public Collection<String> getNamespaces() throws EJBConfigurationException;
    
    public Collection<ConfigurationSummary> getNamespacesDetail() throws EJBConfigurationException;

    public Collection<ConfigurationSummary> getNamespacesHistoryDetail(String namespace) throws EJBConfigurationException;

    public byte[] loadConfigurationAsByteArray(String namespace, VersionEffectiveDate date) throws EJBConfigurationException;

    public int saveConfiguration(String namespace, byte[] config) throws EJBConfigurationException;

    public int reset();

    public int purgeAllConfigurations();

    public int deleteConfigurationRepository();
}
