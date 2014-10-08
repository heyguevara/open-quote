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

import com.ail.core.command.CommandArgImp;
import com.ail.core.configure.Configuration;

/**
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/07/16 10:23:27 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/SetConfigurationArgImp.java,v $
 */
public class SetConfigurationArgImp extends CommandArgImp implements SetConfigurationArg {
    static final long serialVersionUID = 1199346453402049909L;
    private String namespaceArg;
    private Configuration configurationArg;

    /** Default constructor */
    public SetConfigurationArgImp() {
    }

    /**
     * {@inheritDoc}
     * @see #setNamespaceArg
     * @return value of namespacearg
     */
    public String getNamespaceArg() {
        return this.namespaceArg;
    }

    /**
     * {@inheritDoc}
     * @see #getNamespaceArg
     * @param namespaceArg New value for namespaceArg argument.
     */
    public void setNamespaceArg(String namespaceArg) {
        this.namespaceArg = namespaceArg;
    }

    /**
     * {@inheritDoc}
     * @see #setConfigurationArg
     * @return value of configurationArg
     */
    public Configuration getConfigurationArg() {
        return this.configurationArg;
    }

    /**
     * {@inheritDoc}
     * @see #getConfigurationArg
     * @param configurationArg New value for configurationArg argument.
     */
    public void setConfigurationArg(Configuration configurationArg) {
        this.configurationArg = configurationArg;
    }
}


