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

package com.ail.core.product.resetproduct;

import static com.ail.core.Functions.productNameToConfigurationNamespace;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.ConfigurationResetError;

/**
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2007/10/05 22:47:50 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/resetproduct/ResetProductService.java,v $
 * @stereotype service
 */
public class ResetProductService extends Service {
    private ResetProductArg args = null;
    private Core core = null;
    private String namespace=null;

    /** Default constructor */
    public ResetProductService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2007/10/05 22:47:50 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/product/resetproduct/ResetProductService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.5 $");
        return v;
    }

    public String getConfigurationNamespace() {
        return namespace;
    }

    /**
     * Reset the component's configuration.
     */
    public void resetConfiguration() {
        super.resetConfigurationByNamespace();
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (ResetProductArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of ResetProductArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException {
        if (args.getProductNameArg()==null || args.getProductNameArg().length()==0) {
            throw new PreconditionException("args.getProductNameArg()==null || args.getProductNameArg().length()==0");
        }

        try {
            namespace=productNameToConfigurationNamespace(args.getProductNameArg());
            resetConfiguration();
            core.logInfo("Product reset:"+namespace);
        }
        catch(ConfigurationResetError e) {
            e.printStackTrace();
            throw new PreconditionException("Product not found: "+namespace);
        }
    }

    /**
     * Always use the latest configuration for this and the other product catalog 
     * related services.
     */
    @Override
    public VersionEffectiveDate getVersionEffectiveDate() {
        return new VersionEffectiveDate();
    }
}


