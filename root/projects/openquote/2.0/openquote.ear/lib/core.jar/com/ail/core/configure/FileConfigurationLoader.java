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
package com.ail.core.configure;

import com.ail.core.NotImplementedError;
import com.ail.core.VersionEffectiveDate;

import java.util.Collection;

/**
 * This class handles the loading and saving of property (configuration)
 * information held in files.
 */
public class FileConfigurationLoader extends AbstractConfigurationLoader {
	public Configuration loadConfiguration(String namespace, VersionEffectiveDate date) {
		throw new NotImplementedError("FileConfigurationLoader.loadConfiguration");
    }

    public void saveConfiguration(String namespace, Configuration config) {
		throw new NotImplementedError("FileConfigurationLoader.saveConfiguration");
    }

    public Collection<String> getNamespaces() {
        throw new NotImplementedError("FileConfigurationLoader.getNamespaces");
    }

    public void reset() {
        throw new NotImplementedError("FileConfigurationLoader.reset");
    }

    /**
     * Delete ALL configuration information. This will include all historical configuration
     * information. <p>
     * <b>NOTE: ALL CONFIGURATION INFORMATION WILL BE LOST!</b>
     */
    public void purgeAllConfigurations() {
        throw new NotImplementedError("FileConfigurationLoader.purgeAllConfigurations");
    }

    /**
     * Delete the repository holding configuration information. This not only removes all
     * configuration information {@link #purgeAllConfigurations} but also removes the
     * repository itself.<p>
     * <b>NOTE: ALL CONFIGURATION INFORMATION WILL BE LOST!</b>
     */
    public void deleteConfigurationRepository() {
        throw new NotImplementedError("FileConfigurationLoader.deleteConfigurationRepository");
    }

    /**
     * Fetch the summary details for all the configurations that are current. In this context "current"
     * means the latest version (i.e. with a validTo of zero). The collection returned is made up of
     * instances of {@link ConfigurationSummary ConfigurationSummary}.
     * @return A collection of {@link ConfigurationSummary ConfigurationSummary}
     */
    public Collection<ConfigurationSummary> getNamespacesSummary() {
        throw new NotImplementedError("FileConfigurationLoader.getNamespacesDetail");
    }

    /**
     * Fetch the details of a specific configurations history. 
     * @param namespace Namespace to get history for.
     * @return A collection of {@link ConfigurationSummary ConfigurationSummary}
     */
    public Collection<ConfigurationSummary> getNamespacesHistorySummary(String namespace) {
        throw new NotImplementedError("FileConfigurationLoader.getNamespacesHistorySummary");
    }
}
