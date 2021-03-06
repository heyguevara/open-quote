/* Copyright Applied Industrial Logic Limited 2005. All rights Reserved */
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

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.URL;
import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.drools.RuleBase;
import org.drools.RuleBaseFactory;
import org.drools.StatefulSession;
import org.drools.compiler.DrlParser;
import org.drools.compiler.DroolsParserException;
import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.drools.decisiontable.InputType;
import org.drools.decisiontable.SpreadsheetCompiler;
import org.drools.decisiontable.parser.DecisionTableParseException;
import org.drools.lang.descr.PackageDescr;
import org.drools.rule.InvalidRulePackage;

import com.ail.core.Core;
import com.ail.core.Functions;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.factory.AbstractFactory;

/**
 * This Accessor supports the use of Drools decision tables as services. Drools (www.drools.org) is
 * an open source rules engine which supports decision tables in the form of spreadsheets conforming
 * to certain conventions. The usage pattern here is to have an instance of this Accessor for each 
 * decision table based ruleset. The Accessor expects to find a URL pointing at the decision table in
 * it's "Url" parameter (generally this is set by configuration).
 * @version $Revision: 1.12 $
 * @state $State: Exp $
 * @date $Date: 2007/12/22 10:40:49 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/command/DroolsDecisionTableAccessor.java,v $
 */
public class DroolsDecisionTableAccessor extends Accessor implements ConfigurationOwner {
    private Core core=null;
    private CommandArg args=null;
    private String url=null;
    private String extend=null;
    private transient RuleBase ruleBase=null;
    private boolean ruleBaseLoaded=false;

    public void setArgs(CommandArg args) {
        this.args=args;
    }

    public CommandArg getArgs() {
        return args;
    }

    /**
     * Factory life cycle method. See {@link AbstractFactory} for details.
     * @param core
     */
    public void activate(Core core, com.ail.core.configure.Type typeSpec) {
        if (ruleBase==null) {
            try {
                this.core=core;
                core.logInfo("Loading decision table for:"+typeSpec.getName()+" from:"+getUrl());
                loadRuleBase();
            }
            catch(InvalidRulePackage e) {
                core.logError("Failed to compile decision table: \n"+e.getMessage());
            }
            catch(DecisionTableParseException e) {
                core.logError("Failed to parse decision table: \n"+e.getMessage());
            }
            catch(RuntimeException e) {
                core.logError("Failed to compile decision table: \n"+e.getMessage());
            }
            catch(Throwable e) {
                core.logError("Failed to compile decision table: "+getUrl());
                e.printStackTrace();
            }
            finally {
                this.core=null;
            }
        }
    }
    
    private void resolveInheritance(List<PackageDescr>pkgs, String extend) throws Exception {
        boolean mergeDone=false;
        
        if (extend!=null && extend.length()>0) {
            String extendUrl=getCore().getParameterValue("_Types."+extend+".Url");
            URL donorUrl=Functions.absoluteConfigureUrl(getCore(), extendUrl);
            
            PackageDescr donor=loadPackage(donorUrl);

            // Check the ruleBase for a package with the same name as the donor
            for(PackageDescr pd: pkgs) {
                if (pd.getName().equals(donor.getName())) {
                    DroolsAccessor.mergeRulePackages(pd, donor);
                    mergeDone=true;
                }
            }
            
            // If the ruleBase doesn't contain a package matching the donor, add it as a new package.
            if (!mergeDone) {
                pkgs.add(donor);
            }
            
            String nextExtend=getCore().getParameterValue("_Types."+extend+".Extend");

            resolveInheritance(pkgs, nextExtend);
        }
    }
    
    /**
     * Load the rules from a URL into a package descriptor
     * @param drlUrl String of statements
     * @return Loaded package
     * @throws IOException
     * @throws DroolsParserException
     */
    private PackageDescr loadPackage(URL drlUrl) throws IOException, DroolsParserException {
        // Parse the DRL string
        DrlParser parser = new DrlParser();

        // first we compile the decision table into a whole lot of rules.
        SpreadsheetCompiler compiler = new SpreadsheetCompiler();
        String drl = compiler.compile((InputStream)drlUrl.getContent(), InputType.XLS);

        if (AccessorLoggingIndicator.FULL.equals(getLoggingIndicator())) {
            core.logInfo("Rules derived from "+getUrl()+"\n"+drl);
        }
        
        // Add it to the list
        return parser.parse(drl);
    }

    /**
     * Load the rulebase if it hasn't be loaded already.
     * @throws Exception If the load fails
     */
    private void loadRuleBase() throws Exception {
        URL ruleUrl;

        ruleUrl=Functions.absoluteConfigureUrl(getCore(), getUrl());

        ruleBaseLoaded=false;
        
        List<PackageDescr> pkgs=new ArrayList<PackageDescr>();

        pkgs.add(loadPackage(ruleUrl));
        resolveInheritance(pkgs, getExtend());        

        ruleBase = RuleBaseFactory.newRuleBase();

        PackageBuilder builder=null;
        PackageBuilderConfiguration config=new PackageBuilderConfiguration();
        
        for(PackageDescr desc: pkgs) {
            builder = new PackageBuilder(config);
            builder.addPackage( desc );
            ruleBase.addPackage(builder.getPackage());
        }
        
        ruleBaseLoaded=true;
    }

    public void invoke() throws DroolsServiceException {
        if (!ruleBaseLoaded) {
            throw new IllegalStateException("Rule base not loaded ("+getUrl()+"). Was there an error during compilation?");
        }
        
        StatefulSession workingMemory=null;
        
        try {
            workingMemory=ruleBase.newStatefulSession();

            workingMemory.insert(getArgs());

            // create a global (variable) for each arg/ret in the service's arguments
            for(Method m: getArgs().getClass().getMethods()) {
                if (m.getName().startsWith("get") && (m.getName().endsWith("Arg") || m.getName().endsWith("Ret"))) {
                    workingMemory.setGlobal("$"+m.getName().substring(3), m.invoke(args));
                }
            }
            
            workingMemory.fireAllRules();
        } 
        catch(Exception e) {
            e.printStackTrace();
            throw new DroolsServiceException(e);
        }
        finally {
            if (workingMemory!=null) {
                workingMemory.dispose();
            }
        }
    }

    public Version getVersion() {
        throw new CommandInvocationError("Get version cannot be invoked on a DroolsDecisionTableAccessor service");
    }

    public Configuration getConfiguration() {
        throw new CommandInvocationError("Get configuration cannot be invoked on a DroolsDecisionTableAccessor service");
    }

    public void setConfiguration(Configuration properties) {
        throw new CommandInvocationError("Set configuration cannot be invoked on a DroolsDecisionTableAccessor service");
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

    /**
     * Clone this object. This clone method is used by all Type subclasses to handle deep cloning.
     * For the factory to operate correctly it is essential that Types can be deep cloned, as it
     * hangs onto prototyped instances by name and simply clones them when a request is made for
     * an instance of a named type.
     * @throws CloneNotSupportedException If the type cannot be deep cloned.
     */
    public Object clone() throws CloneNotSupportedException {
        DroolsDecisionTableAccessor clone=(DroolsDecisionTableAccessor)super.clone();
        clone.ruleBase=ruleBase;
        return clone;
    }

    public String getExtend() {
        return extend;
    }

    public void setExtend(String extend) {
        this.extend = extend;
    }

    public boolean isRuleBaseLoaded() {
        return ruleBaseLoaded;
    }

    public void setRuleBaseLoaded(boolean ruleBaseLoaded) {
        this.ruleBaseLoaded = ruleBaseLoaded;
    }
}
