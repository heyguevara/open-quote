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
package com.ail.insurance.policy;

import static com.ail.core.Functions.classForName;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class DataGenerator {
    
    
    // Records will be generated between START_DATE and END_DATE
    private static final Long ONE_DAY = (long) (24 * 60 * 60 * 1000); // Milliseconds in a day
    private static final Long ONE_YEAR = (long) (365 * ONE_DAY); 
    private static final Long START_DATE = System.currentTimeMillis() - (1 * ONE_YEAR); // 1 years ago today
    private static final Long END_DATE = System.currentTimeMillis();
    
    private static final Long NUMBER_OF_RECORDS = 1000L;

    // Enum definitions
    private static final String[] REASONS =  {"NEW_BUSINESS", "CLAIM", "CANCELLATION", "RENEWAL", "RENEWAL", "RENEWAL"}; 
    private static final String[] PRODUCTS = { "MotorPlus", "LifePlus", "WaterSports" };
    private static final String[] BROKERS = { "Harvey Munx Ltd", "Berkley Thames-Valley", "James Hewel & Co", "Shark, Graham & Timpson", "Salvador Rush" };
    private static final String[] FIRST_NAMES = { "John", "Tim", "Sarah", "Hillary", "Bill", "Samuel", "Daisy", "Harvey", "Jane", "Morris", "Claire" };
    private static final String[] LAST_NAMES = { "Johnson", "Baites", "Smith", "Jones", "Hall", "Robson", "Bryson", "Britten", "Hilliard", "Garvey", "Brompton", "Palmer", "Plant", "Selbey", "Dawson"};
        
    

    // Policy numbers will start from BASE_POLICY_NUMBER
    private static final double BASE_POLICY_NUMBER = 10000;

    private PolicyNumberGenerator policyNumberGenerator = new PolicyNumberGenerator();

    private Context context=null;
    
    private String selectFrom(String[] options) {
        return options[(int) (context.policyNumber % options.length)];
    }
        
    private String sqlDate(long date) {
        return new SimpleDateFormat("yy-MM-dd").format(new Date(date));
    }
    
    private void outputBordereauRecord() throws SQLException {
        
        String reason = selectFrom(REASONS);
        int premium = (int)(300 + (context.policyNumber % 2000));
        if ("CLAIM".equals(reason) || "CANCELLATION".equals(reason)) {
            premium = premium * -1;
        }
    
        String sql = "insert into bordereau ("+
                "broker,"+
                "quote_number,"+
                "policy_number,"+
                "quote_date,"+
                "accepted_date,"+
                "status,"+
                "policy_holder_name,"+
                "premium_amount,"+
                "premium_currency,"+
                "product"+
            ")"+
            " values ("+
                "'"+selectFrom(BROKERS)+"', "+
                "'QF"+context.policyNumber+"', "+
                "'POL"+context.policyNumber+"', "+
                "'"+sqlDate(context.quoteDate)+"', "+
                "'"+sqlDate(context.inceptionDate)+"', "+
                "'"+reason.replace('_', ' ')+"', "+
                "'"+selectFrom(FIRST_NAMES) + " " + selectFrom(LAST_NAMES) + "', "+
                ""+premium+", "+
                "'GBP', "+
                
                "'"+selectFrom(PRODUCTS)+"'"+
            ");";
                
        executeSQL(sql);
    }

    private void executeSQL(String sql) throws SQLException {
        PreparedStatement statement=context.connection.prepareStatement(sql);
        statement.execute();
        statement.close();
    }
    
    private void generateBordereau() throws SQLException {
        outputBordereauRecord();
    }

    void setupContext() {
        context.policyNumber = policyNumberGenerator.next();
        context.quoteDate = (long) (START_DATE + (Math.random() * (END_DATE - START_DATE)));
        context.inceptionDate = (long) (context.quoteDate + ((Math.random() * 7) * ONE_DAY));
    }
    
    private void run(String args[]) throws SQLException {
        context=new Context(args);

        System.out.println("Generating dummy bordereau data...");
        
        for (int i = 0; i < NUMBER_OF_RECORDS; i++) {
            setupContext();

            System.out.print("generating record "+(i+1)+" of "+NUMBER_OF_RECORDS+"\r");
            generateBordereau();
            
        }
        
        System.out.println("Generation of dummy bordereau data complete.");
    }

    public static void main(String[] args) {
        if (args.length != 3 && args.length != 4) {
            System.err.println("Usage: DataGenerator <JDBC Driver Class Name> <JDBC Connection URL> <Database Username> <Database Password>");
            System.exit(1);
        } 
        else {
            try {
                new DataGenerator().run(args);
            }
            catch(Throwable e) {
                e.printStackTrace(System.err);
                System.exit(1);
            }
        }
    }

    /*
     * Generate policy numbers that are spread randomly over a range but never recur.
     */
    class PolicyNumberGenerator {
        private List<Long> usedPolicyNumbers = new ArrayList<Long>();

        public Long next() {
            long policyNumber;

            do {
                policyNumber = (long) (BASE_POLICY_NUMBER + (Math.random() * (NUMBER_OF_RECORDS*10)));
            } while (usedPolicyNumbers.contains(policyNumber));

            usedPolicyNumbers.add(policyNumber);

            return policyNumber;
        }
    }
        
    private class Context {
        private Connection connection;
        private long policyNumber;
        private long inceptionDate;
        private long quoteDate;
        private String dbDriver;
        private String dbUrl;
        private String dbUsername;
        private String dbPassword = "";;
        
        private Context(String[] args) {
            dbDriver=args[0];
            dbUrl=args[1];
            dbUsername=args[2];
            if (args.length == 4) {
                dbPassword=args[3];
            }
            populateConnection();
        }
        
        private void populateConnection() {
            try {
                classForName(dbDriver);

                Properties properties = new Properties();
                properties.put("user", dbUsername);
                if (dbPassword!=null && dbPassword.length() > 0) {
                    properties.put("password", dbPassword);
                }

                connection = DriverManager.getConnection(dbUrl, properties);
            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("JDBC Driver Class (" + dbDriver + ") not found.");
            } catch (SQLException e) {
                throw new IllegalArgumentException("Database access error (driver:" + dbDriver + ", url:" + dbUrl + ") " + e);
            }
        }
    }
}
