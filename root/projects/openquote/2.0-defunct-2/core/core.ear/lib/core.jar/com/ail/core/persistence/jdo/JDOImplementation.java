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

package com.ail.core.persistence.jdo;

import java.security.Principal;

import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;

public class JDOImplementation implements com.ail.core.configure.ConfigurationOwner {
    static JDOImplementation imp=null;

    private JDOImplementation() {
    }

    public void create() {
    }

    public void update() {
    }

    public QueryResults query(Query query) {
		return null;
    }

    public void delete() {
    }

    public void introduce() {
    }

    public static JDOImplementation getJDOImplementation() {
		return null;
    }

	/* 
	 * @param config
	 */
	public void setConfiguration(Configuration config) {
		// TODO Auto-generated method stub
		
	}

	/* 
	 * @return
	 */
	public Configuration getConfiguration() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * @return
	 */
	public String getConfigurationNamespace() {
		// TODO Auto-generated method stub
		return null;
	}

	/* 
	 * 
	 */
	public void resetConfiguration() {
		// TODO Auto-generated method stub
		
	}

	/* 
	 * @return
	 */
	public VersionEffectiveDate getVersionEffectiveDate() {
		// TODO Auto-generated method stub
		return null;
	}

    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        // TODO Auto-generated method stub
        return null;
    }
}
