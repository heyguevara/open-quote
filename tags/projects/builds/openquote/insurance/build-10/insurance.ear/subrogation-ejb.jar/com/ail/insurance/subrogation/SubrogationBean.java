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

package com.ail.insurance.subrogation;

import javax.ejb.CreateException;
import javax.ejb.SessionBean;
import javax.ejb.SessionContext;

import com.ail.core.BaseError;
import com.ail.core.BaseException;
import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.EJBComponent;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.AbstractCommand;
import com.ail.insurance.subrogation.makearecovery.MakeARecoveryArg;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/subrogation-ejb.jar/com/ail/insurance/subrogation/SubrogationBean.java,v $
 * @undefined
 * @displayName
 * @ejbHome <{com.ail.insurance.subrogation.SubrogationHome}>
 * @ejbRemote <{com.ail.insurance.subrogation.Subrogation}>
 */
public class SubrogationBean extends EJBComponent implements SessionBean {
    private static final long serialVersionUID = 6506879017396687519L;
    private VersionEffectiveDate versionEffectiveDate=null;
	private Core core=null;
    private SessionContext ctx=null;
    
    public SubrogationBean() {
        core=new Core(this);
        versionEffectiveDate=new VersionEffectiveDate();
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void ejbCreate() throws CreateException {
		versionEffectiveDate=new VersionEffectiveDate();
		core=new Core(this);
	}

    public VersionEffectiveDate getVersionEffectiveDate() {
		return versionEffectiveDate;
    }

    public MakeARecoveryArg makeARecovery(MakeARecoveryArg arg) {
        try {
            AbstractCommand command=core.newCommand("MakeARecovery");
            command.setArgs(arg);
            command.invoke();
            return (MakeARecoveryArg)command.getArgs();
        }
        catch(BaseException e) {
            throw new BaseServerException(e);
        }
        catch(BaseError e) {
            throw new BaseServerException(e);
        }
    }

    /**
     * Getter to return the core this component is using.
     * @return Core instance.
     */
    public Core getCore() {
        return core;
    }

    public Version getVersion() {
        Version v=(Version)core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2005/08/19 20:20:59 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/subrogation-ejb.jar/com/ail/insurance/subrogation/SubrogationBean.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.1 $");
        return v;
    }

    /**
     * Hard code the namespace to "com.ail.insurance.quotation.QuotationBean". Generally,
     * the super class will automatically provide a namespace based on the class name, 
     * but for EJBs this can be a problem. Some app server generated containers effect
     * the name of the class causing the configuration to fail. Weblogic is one such.
     * @return The namespace of the configuration.
     */
    public String getConfigurationNamespace() {
        return "com.ail.insurance.subrogation.SubrogationBean";
    }
}
