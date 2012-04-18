/* Copyright Applied Industrial Logic Limited 2006. All rights reserved. */
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

package com.ail.openquote.motorplus;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.ail.core.Core;
import com.ail.core.CoreUserBaseCase;
import com.ail.core.XMLString;
import com.ail.core.configure.AbstractConfigurationLoader;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.policy.SavedPolicy;
import com.ail.insurance.policy.SavedPolicySummary;
import com.ail.insurance.policy.Policy;

public class TestQuotationPersistence extends CoreUserBaseCase {
    private static final long serialVersionUID = 2030295330203910171L;
    private static boolean onetimeSetupDone = false;
    private static Properties props;    
    private AbstractConfigurationLoader loader = null;

    /**
     * Sets up the fixture (run before every test). Get an instance of Core, and delete the testnamespace from the config table.
     * @throws ClassNotFoundException 
     */
    @Before
    public void setUp() throws ClassNotFoundException {
        Timer.start("setup");
        
        setCore(new Core(this));
        
        if (!onetimeSetupDone) {
            setupSystemProperties();
     
            resetConfiguration();
            
            loader = AbstractConfigurationLoader.loadLoader();
            props = loader.getLoaderParams();
            
            Class.forName(props.getProperty("driver"));
            
            onetimeSetupDone=true;
        }
        
        Timer.split("setup");
        
        try {
            Connection con = DriverManager.getConnection(props.getProperty("url"), props);
            Statement st = con.createStatement();
            st.execute("delete from saved_quote_summary");
            st.execute("delete from saved_quote");
            st.close();
            con.close();
        }
        catch (Exception e) {
            System.err.println("Failed to clear test tables: "+e);
        }

        Timer.stop("setup");
    }
    
    /**
     * A SavedPolicy is a sub-class of SavedPolicySummary, so it you write a SavedPolicy to the DB you
     * should be able to read it back as a SavedPolicySummary... But can you?
     */
    @Test
    public void testSaveAQuotationGetAQuotationSummary() throws Exception {
        long quoteId;
        
        {
            getCore().openPersistenceSession();
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteFive.xml"));
            Policy quoteIn = getCore().fromXML(Policy.class, quoteXml);
            SavedPolicy sq = getCore().create(new SavedPolicy(quoteIn));
            quoteId=sq.getSystemId();
            getCore().closePersistenceSession();
        }

        {
            getCore().openPersistenceSession();
            getCore().load(SavedPolicySummary.class, quoteId);
            getCore().closePersistenceSession();
        }
    }
 
    @Test
    public void testSaveSomeQuotationsAndQueryThem() throws Exception {
        Timer.start("testSaveSomeQuotationsAndQueryThem");
        
        {
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteFive.xml"));
            Policy quoteIn = getCore().fromXML(Policy.class, quoteXml);
            quoteIn.setUserSaved(true);
            quoteIn.setUsername("jimbo");
            quoteIn.setStatus(PolicyStatus.QUOTATION);
            
            getCore().openPersistenceSession();
            
            getCore().create(new SavedPolicy(quoteIn));
            getCore().create(new SavedPolicy(quoteIn));
            getCore().create(new SavedPolicy(quoteIn));
            getCore().create(new SavedPolicy(quoteIn));
            getCore().create(new SavedPolicy(quoteIn));
            getCore().create(new SavedPolicy(quoteIn));

            SavedPolicy sq=new SavedPolicy(quoteIn);
            Timer.start("create one");
            getCore().create(sq);
            Timer.stop("create one");

            getCore().closePersistenceSession();
        }

        {
            Timer.start("query");

            getCore().openPersistenceSession();
            
            List<?> sl=getCore().query("get.savedPolicySummary.by.username", "jimbo");
            assertEquals(7, sl.size());
            Timer.stop("query");

            getCore().closePersistenceSession();
        }
        
        Timer.stop("testSaveSomeQuotationsAndQueryThem");
    }
 
    /**
     * Had an issues during development where update'ing a quotation always resulted in a new quotation
     * record being created. This test makes sure that doesn't occur.
     */
    @Test
    public void testUpdateQuotationSummary() throws Exception {
        Policy quote=null;

        {
            XMLString quoteXml = new XMLString(this.getClass().getResourceAsStream("TestMotorPlusQuotationQuoteFive.xml"));
            Policy quoteIn = getCore().fromXML(Policy.class, quoteXml);
            getCore().openPersistenceSession();
            SavedPolicy sq = getCore().create(new SavedPolicy(quoteIn));
            getCore().closePersistenceSession();
            quote=sq.getPolicy();
        }

        quote.setStatus(PolicyStatus.DECLINED);
        
        {
            getCore().openPersistenceSession();
            getCore().update(new SavedPolicy(quote));
            getCore().closePersistenceSession();
        }

        {
            Connection con = DriverManager.getConnection(props.getProperty("url"), props);
            Statement st = con.createStatement();
            ResultSet rs=st.executeQuery("select count(*) from saved_quote_summary");
            rs.next();
            assertEquals(1, rs.getInt(1));
            rs.close();
            st.close();
            con.close();
        }
    }
 
    static class Timer {
        static HashMap<String,Long> timers=new HashMap<String,Long>();
        
        static void start(String name) {
            timers.put(name, System.currentTimeMillis());
        }
                
        static void split(String name) {
            System.out.printf("%s split: %dms\n", name, (System.currentTimeMillis()-timers.get(name)));
        }
            
        static void stop(String name) {
            System.out.printf("%s stopped: %dms\n", name, (System.currentTimeMillis()-timers.get(name)));
            timers.remove(name);
        }
    }
}
