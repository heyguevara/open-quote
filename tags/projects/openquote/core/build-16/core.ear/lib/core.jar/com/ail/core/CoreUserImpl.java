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
package com.ail.core;

import java.security.Principal;

/**
 * Clients of the Core may use an instance of this class to satisfy the Core's
 * callback methods.
 * @version $Revision: 1.6 $
 * @state $State: Exp $
 * @date $Date: 2007/06/10 09:14:06 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/CoreUserImpl.java,v $
 */
public class CoreUserImpl implements CoreUser {
    /**
     * The current date will be returned whenever the version effective date
     * is requested.
     */
    public static final int SelectLatestConfigurations=0;

    /**
     * Whenever CoreUserImpl was instantiated becomes the version effective
     * date for this instance.
     */
    public static final int SelectConsistentConfigurations=1;

    private VersionEffectiveDate ved=null;
    private Principal securityPrincipal=null;
    private String configurationNamespace=null;
    
    
    public CoreUserImpl() {
    }
    
    /**
     * Create a new instance based on an existing CoreUser. The core uses this
     * constructor to build stripped down instance of client's CoreUser to reduce
     * RMI traffic. A client's CoreUser is always passed into the services they
     * use - it's how the service gets to know things about the client. However,
     * client's also often implement CoreUser and pass themselves into the Core
     * constructor. If they do, then we'd end up sending the whole client class
     * across RMI. So the Core uses this constructor to pull the minimum necessary
     * information out of the client's CoreUser and pass that onto the service.
     * @param user The CoreUser to copy properties from.
     */
    public CoreUserImpl(CoreUser user) {
        ved = user.getVersionEffectiveDate();
        securityPrincipal = user.getSecurityPrincipal();
        configurationNamespace = user.getConfigurationNamespace();
    }

    /**
     * Constructor.
     * @deprecated Use {@link #CoreUserImpl(int, Principal)} instead.
     * @param configSelectionFlag Either {@link #SelectConsistentConfigurations SelectConsistentConfigurations} or {@link #SelectLatestConfigurations SelectLatestConfigurations}
     */
    public CoreUserImpl(int configSelectionFlag) {
        switch(configSelectionFlag) {
            case SelectConsistentConfigurations:
                ved=new VersionEffectiveDate();
                break;
            case SelectLatestConfigurations:
                ved=null;
                break;
            default:
                throw new IllegalArgumentException("ConfigSelectionFlag is invalid");
        }

        securityPrincipal=null;
    }

    /**
     * Constructor
     * @param configSelectionFlag Either {@link #SelectConsistentConfigurations SelectConsistentConfigurations} or {@link #SelectLatestConfigurations SelectLatestConfigurations}
     * @param configurationNamespace Namespace to associate with this core user.
     * @param securityPrincipal Principal to associate with this Core User, this may be null.
     */
    public CoreUserImpl(int configSelectionFlag, String configurationNamespace, Principal securityPrincipal) {
        switch(configSelectionFlag) {
            case SelectConsistentConfigurations:
                ved=new VersionEffectiveDate();
                break;
            case SelectLatestConfigurations:
                ved=null;
                break;
            default:
                throw new IllegalArgumentException("ConfigSelectionFlag is invalid");
        }

        this.configurationNamespace=configurationNamespace;
        this.securityPrincipal=securityPrincipal;
    }

    /**
     * The Core uses this callback to determin which versions of artefacts it
     * should use on the CoreUser's behalf.
     * @return The version date that the CoreUser is working at.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        if (ved==null) {
            return new VersionEffectiveDate();
        }

        return ved;
    }

    public void setVersionEffecvtiveDateToNow() {
        ved=new VersionEffectiveDate();
    }
    
    public void setVersionEffectiveDate(VersionEffectiveDate ved) {
        this.ved=ved;
    }
    
    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        return securityPrincipal;
    }

    /**
     * Set the security principal associated with this instance.
     * @param securityPrincipal The security principal of this core user
     */
    public void setSecurityPrincipal(Principal securityPrincipal) {
        this.securityPrincipal=securityPrincipal;
    }

    public String getConfigurationNamespace() {
        return configurationNamespace;
    }

    public void setConfigurationNamespace(String configurationNamespace) {
        this.configurationNamespace=configurationNamespace;
    }
}
