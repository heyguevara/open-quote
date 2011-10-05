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
package com.ail.coretest;

import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Properties;

import junit.framework.TestCase;

import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.CoreUserImpl;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationOwner;
import com.ail.core.configure.ConfigurationResetError;

/**
 * This class should be extended by any test that needs to act like a core user. I
 * implements the CoreUser interface on behalf of test itself - making the test class
 * cleaner. It also handles common tasks like cleaning up test data from the database
 * when tests close.
 * @version $Revision$
 * @author $Author$
 * @state $State$
 * @date $Date$
 * @source $Source$
 */
public class CoreUserTestCase extends TestCase implements CoreUser, ConfigurationOwner {
    private static final long serialVersionUID = -5384098123545310572L;
    private AbstractConfigurationLoader loader=null;
    private Core core=null;
    CoreUserImpl coreUser=null;
    private String namespace="TestNamespace";

    public CoreUserTestCase(String s) {
        super(s);
        coreUser=new CoreUserImpl(CoreUserImpl.SelectConsistentConfigurations, namespace, null);
    }

    public VersionEffectiveDate getVersionEffectiveDate() {
        return coreUser.getVersionEffectiveDate();
    }

    public void setVersionEffectiveDate(VersionEffectiveDate versionEffectiveDate) {
        coreUser.setVersionEffectiveDate(versionEffectiveDate);
    }

    /**
     * Tidy up (delete) the config database records, this means deleting the
     * config records for the core namespace!!
     */
    protected void tidyUpTestData() {
        // load the loader
        if (loader==null) {
            loader=AbstractConfigurationLoader.loadLoader();
        }

        // fetch the loader's properties
        Properties props=loader.getLoaderParams();

        // load the JDBC driver
        try {
            Class.forName(props.getProperty("driver"));
            Connection con=DriverManager.getConnection(props.getProperty("url")+props.getProperty("databaseName"), props);
            Statement st=con.createStatement();
            st.execute("DELETE FROM "+props.getProperty("table")+" WHERE namespace='"+namespace+"'");
            st.close();
            con.close();
        }
        catch(Exception e) {
            if (e.toString().indexOf("doesn't exist")!=-1) {
                // this is ok to ignore - the table isn't always there
            }
            else {
                System.err.println("ignored: "+e);
            }
        }
    }

    public Core getCore() {
        return core;
    }

    public void setCore(Core core) {
        this.core = core;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @param config Configuration to use from now on.
     */
    public void setConfiguration(Configuration config) {
        core.setConfiguration(config);
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @return The current configuration (at versionEffectiveDate).
     */
    public Configuration getConfiguration() {
        return core.getConfiguration();
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @return The configuration namespace we're using
     */
    public String getConfigurationNamespace() {
        return namespace;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     * @return The configuration namespace we're using
     */
    public void setConfigurationNamespace(String namespace) {
        this.namespace=namespace;
    }

    /**
     * Method demanded by the ConfigurationOwner interface.
     */
    public void resetConfiguration() {
        if (getCore()==null) {
            return;
        }

        // Derive the configuration's name
        String cname=this.getClass().getName();
        String name=cname.substring(cname.lastIndexOf('.')+1);

        try {
            // load the <name>DefaultConfig resource into an XMLString
            InputStream in=this.getClass().getResourceAsStream(name+"DefaultConfig.xml");
            XMLString factoryConfigXML=new XMLString(in);

            // marshal the config XML into an instance of Configuration
            Configuration factoryConfig=(Configuration)getCore().fromXML(Configuration.class, factoryConfigXML);

            // reset the configuration
            setConfiguration(factoryConfig);
        }
        catch(Exception e) {
        	e.printStackTrace();
            throw new ConfigurationResetError("Failed to reset "+name+" configuration: "+e);
        }
    }

    /**
     * Setup the standard system properties that most test will need.
     */
    public void setupSystemProperties() {
        System.setProperty("java.naming.factory.initial","org.jnp.interfaces.NamingContextFactory");
        System.setProperty("java.naming.provider.url","jnp://localhost:1099");
        System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser"); 

        System.setProperty("java.protocol.handler.pkgs", "com.ail.core.urlhandler");
        
        System.setProperty("com.ail.core.configure.loader", "com.ail.core.configure.JDBCConfigurationLoader");
        System.setProperty("com.ail.core.configure.loaderParam.driver", "org.gjt.mm.mysql.Driver");
        System.setProperty("com.ail.core.configure.loaderParam.url", "jdbc:mysql://localhost:3306/openquote_1_4");
        System.setProperty("com.ail.core.configure.loaderParam.table", "config");
        System.setProperty("com.ail.core.configure.loaderParam.user", "openquote");
        System.setProperty("com.ail.core.configure.loaderParam.password", "openquote");
    }

    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        return coreUser.getSecurityPrincipal();
    }
}
