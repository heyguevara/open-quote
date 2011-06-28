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

package com.ail.core;

import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;

/**
 * This entry point does nothing. It is intended to be use in place of a real
 * entry point when either one has to be configured, but no functionality is
 * required, or as a placeholder when the real entry point isn't yet ready.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:26 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/NullService.java,v $
 */
public class NullService extends Service {
    private CommandArg args=null;

	/**
     * Return the version details of this entry point.
     * @return Version specification
     */
    public Version getVersion() {
		Version v=new Version();
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2005/07/16 10:23:26 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/NullService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.2 $");
        return v;
    }

    public void invoke() {
    }

	/**
     * This entry point has no Core requirements, so simply return null.
     * @return null
     */
	public Core getCore() {
        return null;
    }

    public void setArgs(CommandArg args) {
        this.args=args;
    }

    public CommandArg getArgs() {
        return args;
    }

    public void setConfiguration(Configuration configuration) {
    }

    public VersionEffectiveDate getVersionEffectiveDate() {
        return null;
    }

    public Configuration getConfiguration() {
        return null;
    }

    public String getConfigurationNamespace() {
        return null;
    }

    public void resetConfiguration() {
    }
}
