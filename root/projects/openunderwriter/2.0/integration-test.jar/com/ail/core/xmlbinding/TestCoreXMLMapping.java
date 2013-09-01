/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

package com.ail.core.xmlbinding;

import static org.junit.Assert.*;

import java.io.InputStream;
import java.security.Principal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;
import java.util.Date;
import java.util.Properties;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUser;
import com.ail.core.History;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;
import com.ail.core.configure.ConfigurationOwner;

/**
 * Test the core system's XML data mapping facilities.
 */
public class TestCoreXMLMapping implements CoreUser, ConfigurationOwner {
	private String cr=System.getProperty("line.separator");
	private AbstractConfigurationLoader loader=null;
	private Core core=null;
	private VersionEffectiveDate versionEffectiveDate=null;
	private String TestNamespace="TESTNAMESPACE";
	private String configurationNamespace=TestNamespace;

	/**
     * Tidy up (delete) the testdata generated by this set of tests.
     */
	private void tidyUpTestData() {
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
            st.execute("DELETE FROM "+props.getProperty("table")+" WHERE NAMESPACE='"+this.TestNamespace+"'");
            st.close();
            con.close();
        }
        catch(Exception e) {
            // output the error if it isn't a table unknown error.
            if (e.getMessage().indexOf("Unknown table")==0) {
                System.err.println("ignored: "+e);
            }
        }
    }

    /**
     * Sets up the fixture (run before every test).
     * Get an instance of Core, and delete the testnamespace from the config table.
     */
	@Before
    public void setUp() {
        System.setProperty("org.xml.sax.parser", "org.apache.xerces.parsers.SAXParser"); 
        System.setProperty("javax.xml.transform.TransformerFactory", "net.sf.saxon.TransformerFactoryImpl");
        versionEffectiveDate=new VersionEffectiveDate();
        tidyUpTestData();
        core=new Core(this);
        core.resetConfiguration();
        versionEffectiveDate=new VersionEffectiveDate();
        resetConfiguration();
        versionEffectiveDate=new VersionEffectiveDate();
        ConfigurationHandler.resetCache();
    }

    /**
     * Tears down the fixture (run after each test finishes)
     */
	@After
    public void tearDown() {
		tidyUpTestData();
    }

	/**
     * Method demanded by the CoreUser interface.
     * @return A date to use to selecte the corrent version of config info.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
		return versionEffectiveDate;
    }

    /**
     * Get the security principal associated with this instance.
     * @return The associated security principal - if defined, null otherwise.
     */
    public Principal getSecurityPrincipal() {
        return null;
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
		return configurationNamespace;
    }

    public void setConfigurationNamespace(String configurationNamespace) {
		this.configurationNamespace = configurationNamespace;
	}

	/**
     * Method demanded by the ConfigurationOwner interface.
     */
    public void resetConfiguration() {
        try {
            // load the CoreDefaultConfig resource into an XMLString
            InputStream in=this.getClass().getResourceAsStream("TestCoreXMLMappingDefaultConfig.xml");
            XMLString factoryConfigXML=new XMLString(in);
    
            // marshal the config XML into an instance of Configuration
            Configuration factoryConfig=core.fromXML(Configuration.class, factoryConfigXML);
    
            // reset the configuration
            setConfiguration(factoryConfig);
        }
        catch(Exception e) {
            core.logError("Failed to load configuration:"+e);
        }
    }

    /**
     * Ensure that a simple object can be converted into XML.
     * The XML databinding facilities of the core allow the mapping of XML to/
     * from objects to be defined. This test ensures that ToXML'ing an
     * instance of Version generated the correct results.
     * <ol>
     * <li>Use the factory to create an instance of Version</li>
     * <li>Pass version into the core's toXml server</li>
     * <li>Compare the string returned from the service with
     * a hard coded sample.</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    @Test
    public void testSimpleToXML() throws Exception {
    	String expected="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		                "<noisrev author=\"Jimbo Clucknasty\" date=\"Some time today\" xsi:type=\"java:com.ail.core.Version\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+cr+
						" <comment>The floor is far to dark</comment>"+cr+
						"</noisrev>"+cr;

    	Version version=core.newType(Version.class);
        version.setAuthor("Jimbo Clucknasty");
        version.setComment("The floor is far to dark");
        version.setDate("Some time today");

        XMLString res=core.toXML(version);

        assertEquals(expected, res.toString());
    }

    /**
     * Run a simple performace test on the toXml service.
     * This test ensures that the toXml service responds in a reasonable
     * time.
     * <ol>
     * <li>Create an instance of a Version object.</li>
     * <li>Invoke the core's toXml service once to load classes etc.</li>
     * <li>Record the start time</li>
     * <li>Invoke the service 100 times with the same object.</li>
     * <li>Record the end time</li>
     * <li>Calculate the time taken for each call.</li>
     * <li>Fail if the time is > 2ms</li>
     * <li>Fail if any exceptions are thrown.</li>
     * </ol>
     */
    @Test
    public void testSimpleToXMLPerformance() throws Exception {
        Version version=core.newType(Version.class);
        version.setAuthor("Jimbo Clucknasty");
        version.setComment("The floor is far to dark");
        version.setDate("Some time today");

        // ignore the time taken by the first run.
        core.toXML(version);

        // Time 100 calls
        Date start=new Date();
        for(int i=0 ; i<100 ; i++) {
            core.toXML(version);
        }
        Date end=new Date();

        // calculate time per test
        double perTest=(end.getTime()-start.getTime())/100.0;

        // time per test must be less than 10ms (normal is something like 2-4ms)
        assertTrue("Performance limit is 10ms, test took "+perTest+"ms", perTest<10);
    }

    /**
     * Test the fromXml service with a simple object.
     * <ol>
     * <li>Create a hard coded string representing a valid Version object.</li>
     * <li>Pass the string into the FromXml service.</li>
     * <li>Fail if the object returned is not an instance of Version.</li>
     * <li>Fail if the values of properties on the instance don't match those
     * defined in the hard coded string.</li>
     * </ol>
     */
    @Test
    public void testSimpleFromXML() throws Exception {
        XMLString string=new XMLString("<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"+
                     "<noisrev author=\"Jimbo Clucknasty\" date=\"Some time today\" xsi:type=\"java:com.ail.core.Version\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+
                     "<comment>The floor is far to dark</comment></noisrev>");

        Object o=core.fromXML(Version.class, string);

        assertTrue("Object returned is not an instance of 'Version'", o instanceof Version);

        Version v=(Version)o;

        assertEquals("Jimbo Clucknasty", v.getAuthor());
        assertEquals("Some time today", v.getDate());
        assertEquals("The floor is far to dark", v.getComment());
    }

	/**
	 * Test to ensure that nested types marshal as expected.
	 * <ol>
	 * <li>Create an instance of History, and of a Version<li>
	 * <li>Populate Version with test data</li>
	 * <li>Add Version to History</li>
	 * <li>Pass the History object into core.toXML()</li>
	 * <li>Fail if any of Version's populated fields fail to appear in the result</li>
	 * <li>Fail if any exception occur</li>
	 * <ol>
	 * @throws Exception
	 */
    @Test
	public void testEmbeddedObject() throws Exception {
		String expected="<?xml version=\"1.0\" encoding=\"UTF-8\"?>"+
		                "<history xsi:type=\"java:com.ail.core.History\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"+cr+
						" <MyVersion author=\"Jimbo Clucknasty\" date=\"Some time today\">"+cr+
						"  <comment>The floor is far to dark</comment>"+cr+
						" </MyVersion>"+cr+
						"</history>"+cr;

		History history=core.newType(History.class);
		
		Version version=core.newType(Version.class);
		version.setAuthor("Jimbo Clucknasty");
		version.setComment("The floor is far to dark");
		version.setDate("Some time today");

		history.addVersion(version);
		
		assertEquals(expected, core.toXML(history).toString());
	}
}
