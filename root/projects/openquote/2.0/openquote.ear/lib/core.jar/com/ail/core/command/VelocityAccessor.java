/* Copyright Applied Industrial Logic Limited 2010. All rights Reserved */
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

package com.ail.core.command;

import java.io.Writer;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;

import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.apache.velocity.runtime.resource.loader.StringResourceLoader;
import org.apache.velocity.runtime.resource.util.StringResourceRepository;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.factory.AbstractFactory;

/**
 * The Velocity Accessor provides access to services implemented using Apache
 * Velocity.<p/>
 * Unlike most service types supported by the core, Velocity can only
 * produce one result - the result of performing a merge using the command args
 * as the only context variable. As a result of this limitation (i.e. the 
 * results cannot be mapped back to the command argument passed in) a 
 * convention is adopted in order to determine where the output of the merge
 * should be written. The accessor searches the command args for the method
 * <code>getWriterArg</code> which returns a {@link java.io.Writer Writer}
 * and will then output the results of the merge to the Writer returned when it is 
 * invoked.<p/>
 * 
 * The VelocityAccessor does not implement the concept of inheritance which is
 * common to other Accessors as there is no natural way for templates to inherit 
 * from one another.</p>
 */
public class VelocityAccessor extends Accessor implements ConfigurationOwner {
    private Core core=null;
    private Argument args=null;
    private String script=null;
    private String url=null;
    private transient VelocityEngine velocityEngine=null;
    private transient Throwable velocityInitialiseError=null;
    private boolean velocityEngineInitialised=false;

    public void setArgs(Argument args) {
        this.args=args;
    }

    public Argument getArgs() {
        return args;
    }

    /**
     * The convention in this Accessor is that the result of the velocity merge
     * are written to a Writer which is returned by a method named getWriterArg
     * on the command used to invoke the accessor. This method searches for the
     * getWriterArg method and invokes it returning the value that it returns.
     * @throws VelocityServiceException If either no appropriate argument can be found, or if there is more than one.
     * @param result Contains the result of the merge.
     * @throws InvocationTargetException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    private Writer fetchWriter() throws VelocityServiceException {
        Method getter=null;
        
        for(Method m: args.getClass().getMethods()) {
            if (m.getName().equals("getWriterArg")) {
                if (m.getParameterTypes().length==0 && Writer.class.isAssignableFrom(m.getReturnType())) {
                    if (getter!=null) {
                        throw new VelocityServiceException("CommandImpl argument '"+args.getClass().getName()+"' cannot be used by the Velocity accessor: more than on method matches the return convention (e.g. '"+getter.getName()+"' and '"+m.getName()+"')");
                    }
                    else {
                        getter=m;
                    }
                }
            }
        }
        
        if (getter!=null) {
            try {
                return (Writer)getter.invoke(args);
            } catch (Exception e) {
                throw new VelocityServiceException(e);
            }
        }
        else {
            throw new VelocityServiceException("CommandImpl argument '"+args.getClass().getName()+"' cannot be used by the Velocity accessor: no method matche the return convention (i.e. void set*Ret(String))");
        }
    }
    
    private void initialiseEngine() throws Throwable {
        velocityEngine=new VelocityEngine();
        
        velocityEngine.setProperty("resource.loader", "string");
        velocityEngine.setProperty("string.resource.loader.class", "org.apache.velocity.runtime.resource.loader.StringResourceLoader");
        velocityEngine.setProperty("string.resource.loader.repository.name", core.getConfigurationNamespace());
        velocityEngine.setProperty("runtime.references.strict", true);
        velocityEngine.init();

        StringResourceRepository repo=StringResourceLoader.getRepository(core.getConfigurationNamespace());

        String template=Functions.loadScriptOrUrlContent(getCore(), getUrl(), getScript());
        
        repo.putStringResource(getUrl(), template);
        
        velocityEngineInitialised=true;
    }
    
    /**
     * Factory life cycle method. See {@link AbstractFactory} for details.
     * @param core
     * @param typeSpec
     */
    public void activate(Core core, com.ail.core.configure.Type typeSpec) {
        if (velocityEngine==null) {
            try {
                this.core=core;
                core.logInfo("Initialising velocity engine for: "+typeSpec.getName());
                initialiseEngine();
            } catch (Throwable e) {
                core.logError("Failed to initialise velocity engine: \n"+e.getMessage());
                velocityInitialiseError=e;
            }
        }
    }

    public void invoke() throws BaseException {
        super.logEntry();

        if (!velocityEngineInitialised) {
            if (velocityInitialiseError!=null) {
                throw new VelocityServiceException("Velocity engine initialisation failure", velocityInitialiseError);
            }
            else {
                throw new IllegalStateException("Velocity engine is not available for ("+getUrl()+"). Was there an error during initialiseation?");
            }
        }

        try {
            Template template=velocityEngine.getTemplate(getUrl());
            
            VelocityContext velocityContext=new VelocityContext();
            
            velocityContext.put("args", args);
            
            template.merge(velocityContext, fetchWriter());
        } catch (Exception e) {
            throw new VelocityServiceException(e);
        }
        
        super.logExit();
    }

    public Configuration getConfiguration() {
        throw new CommandInvocationError("Get configuration cannot be invoked on a XSLT service");
    }

    public void setConfiguration(Configuration properties) {
        throw new CommandInvocationError("Set configuration cannot be invoked on a XSLT service");
    }

    public void setScript(String script) {
        this.script=script;
    }

    public String getScript() {
        return script;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public VersionEffectiveDate getVersionEffectiveDate() {
        return args.getCallersCore().getVersionEffectiveDate();
    }

    public Principal getSecurityPrincipal() {
        return args.getCallersCore().getSecurityPrincipal();
    }

    /**
     * Return the caller's configuration namespace.
     * @return Caller's namespace
     */
    public String getConfigurationNamespace() {
        return args.getCallersCore().getConfigurationNamespace();
    }

    public void resetConfiguration() {
        // do nothing, this class doesn't "own" a configuration - it just uses other peoples.
    }

    public Core getCore() {
        if (core==null) {
            core=new Core(this);
        }
        
        return core;
    }
}
