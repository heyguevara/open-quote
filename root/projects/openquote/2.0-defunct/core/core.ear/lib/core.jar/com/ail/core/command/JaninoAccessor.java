/* Copyright Applied Industrial Logic Limited 2007. All rights Reserved */
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

import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.codehaus.janino.CompileException;
import org.codehaus.janino.SimpleCompiler;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;

/**
 * This accessor provides access to services implemented using <a href="http://www.janino.net/">Janino</a>. Services
 * that use this accessor either define a script inline (using a Script parameter in the configuration), of they point
 * at a script via a URL (using the Url parameter). Either way, the script is compiled to java byte code by Janino
 * at runtime and the resulting code is invoked by the accssor.<p/>
 * Compilation is only carried on when the service is initially loaded - on the first attempt to execute the service.
 * Once compiled, the resulting class is cached by the accessor for faster access later on.<p/>
 * As used by this accessor, Janino scripts adopt a simple contract: they must define an invoke method which accepts
 * only one argument of a type which is suitable for the command being serviced.<p/>
 * 
 * In the following example the command called 'TestCommand' had been bound to the Janino based 'TestService'. The convension
 * within the core is to have a the command class (TestCommand) paired with an argument implementation (TestArgImp); therefore, 
 * the invoke method accepts an argument of that type.
 * <pre>
 * &lt;service name="TestService" builder="CachingClassBuilder" key="com.ail.core.command.JaninoAccessor" &gt;
 *   &lt;parameter name="Script"&gt;&lt;![CDATA[
 *     import com.ail.coretest.service.TestArgImp;
 *
 *     public static void invoke(TestArgImp args) {
 *       if (args.getX()&gt;1000) {
 *         args.setR(args.getX()-(2*args.getY()));
 *       }
 *     }
 *   ]]&gt;&lt;/parameter&gt;
 * &lt;/service&gt;
 * 
 * &lt;command name="TestCommand" builder="ClassBuilder" key="com.ail.coretest.service.TestCommand"&gt;
 *    &lt;parameter name="Service"&gt;TestService&lt;/parameter&gt;
 * &lt;/command&gt;
 * </pre>
 * The JaninoAccessor supports the concept of inheritance (or extension) between services based on it. The concept of one service 'Extend'ing
 * another is common to all Core accessors, but in the case of the JaninoAccessor the approach adopted is better thought of as a chain. In effect
 * the service which form part of the chain are each invoked in turn by the accessor.<p/> 
 *
 * Building on the sample code above, an 'extending' service could be added as follows:
 * <pre>
 * &lt;service name="TestExtendingService" builder="CachingClassBuilder" key="com.ail.core.command.JaninoAccessor" &gt;
 *   &lt;parameter name="Extends"&gt;TestService&lt;/parameter&gt;
 *   &lt;parameter name="Script"&gt;&lt;![CDATA[
 *     import com.ail.coretest.service.TestArgImp;
 *
 *     public static void invoke(TestArgImp args) {
 *       if (args.getX()&lt;1000) {
 *         args.setR(args.getX()-(4*args.getY()));
 *       }
 *     }
 *   ]]&gt;&lt;/parameter&gt;
 * &lt;/service&gt;
 * 
 * &lt;command name="TestExtendingCommand" builder="ClassBuilder" key="com.ail.coretest.service.TestCommand"&gt;
 *    &lt;parameter name="Service"&gt;TestExtendingService&lt;/parameter&gt;
 * &lt;/command&gt;
 * </pre>
 * The 'Extends' parameter in 'TestExtendingService' tells the accessor to execute the named service before this one - a
 * chain with two links. So in this case invoking the 'TestExtendingCommand' will lead to a 'TestService' being invoked first, 
 * and then 'TestExtendingService' being invoked.<p/>
 * Note that the invoke() methods in all the services in a chain must accept an argument of the same type.  
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class JaninoAccessor extends Accessor implements ConfigurationOwner {
    private CommandArg args=null;
    private Core core=null;
    private String script=null;
    private String url=null;
    private String extend=null;
    @SuppressWarnings("unchecked")
    private transient List<Class> clazz=null;
    private String name=null;
    
    public void setArgs(CommandArg args) {
        this.args=args;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * Use Janino to compile and load a class. 
     * @param name The name of the class
     * @param source The source of the class as a String
     * @return The Class representing the compiled source
     * @throws Exception
     */
    @SuppressWarnings("unchecked")
    private Class loadClass(String name, String source) throws Exception {
        SimpleCompiler sc=new SimpleCompiler();
        sc.setParentClassLoader(Thread.currentThread().getContextClassLoader());
        sc.cook(name, new StringReader(source));
        return sc.getClassLoader().loadClass(name);
    }
    
    private void activateExtended(String extended) throws Exception {
        String nextExtend=getCore().getParameterValue("_Types."+extended+".Extend");

        if (nextExtend!=null) {
            activateExtended(nextExtend);
        }
        
        if (extended!=null) {
            String thisScript=getCore().getParameterValue("_Types."+extended+".Script");
            String thisUrl=getCore().getParameterValue("_Types."+extended+".Url");
            
            if (thisUrl!=null) {
                core.logInfo("Compiling janino class body from url: "+thisUrl);
            }
            else {
                core.logInfo("Compiling janino class body from inline script:\n "+thisScript);
            }
                        
            clazz.add(loadClass(extended, Functions.loadScriptOrUrlContent(getCore(), thisUrl, thisScript)));
        }
    }
    
    @SuppressWarnings("unchecked")
    public void activate(Core core, com.ail.core.configure.Type typeSpec) {
        if (clazz==null) {
            try {
                name=typeSpec.getName();
                
                clazz=new ArrayList<Class>();
                
                this.core=core;
    
                activateExtended(extend);
    
                core.logInfo("Compiling janino class body for service: "+name);
                
                clazz.add(loadClass(name, (Functions.loadScriptOrUrlContent(core, url, script))));

                core.logInfo("Compilation for service: "+name+" successful");
            }
            catch (CompileException e) {
                core.logError("Janino compilation failure: \n"+e.getMessage());
            }
            catch (ParseException e) {
                core.logError("Janino parse failure: \n"+e.getMessage());
            }
            catch (ScanException e) {
                core.logError("Janino scan failure: \n"+e.getMessage());
            }
            catch(Throwable t) {
                t.printStackTrace();
            }
            finally {
                this.core=null;
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public void invoke() throws BaseException {
        super.logEntry();

        if (clazz==null) {
            throw new IllegalStateException("Janino class not loaded for service: "+name+". Was there an error during compilation?");
        }
        
        try {
            for(Class c: clazz) {
                Method m=c.getMethod("invoke", args.getClass());
                m.invoke(null, args);
            }
        }
        catch (SecurityException e) {
            throw new JaninoServiceException("Security exception accessing 'void invoke(...)' method in janino service: "+name+".", e);
        }
        catch (NoSuchMethodException e) {
            throw new JaninoServiceException("The janino class must define an 'void invoke(...)' method in service: "+name, e);
        }
        catch (RuntimeException e) {
            throw new JaninoServiceException("Error executing janino method for service: "+name+". Error:"+e.getCause(), e);
        }
        catch (IllegalAccessException e) {
            throw new JaninoServiceException("Cannot access 'void invoke(...)' method in janino service:"+name, e);
        }
        catch (InvocationTargetException e) {
            throw new JaninoServiceException("Exception from janino method in service: "+name+" Exception:"+e.getCause(), e.getCause());
        }

        super.logExit();
    }

    public Version getVersion() {
        throw new CommandInvocationError("Get version cannot be invoked on a Janino service");
    }

    public Configuration getConfiguration() {
        throw new CommandInvocationError("Get configuration cannot be invoked on a Janino service");
    }

    public void setConfiguration(Configuration properties) {
        throw new CommandInvocationError("Set configuration cannot be invoked on a Janino service");
    }

    public void setScript(String script) {
        this.script=script;
    }

    public String getScript() {
        return script;
    }

    public void setUrl(String url) {
        this.url=url;
    }

    public String getUrl() {
        return url;
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

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }
}
