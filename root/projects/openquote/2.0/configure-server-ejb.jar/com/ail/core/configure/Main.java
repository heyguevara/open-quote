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
package com.ail.core.configure;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.ejb.EJB;

import com.ail.core.configure.server.Server;

public class Main {
    @EJB private Server server;
    private String username;
    private String password;
    private String operation;
    private String factory;
    private String provider;
    
    public static void main(String[] args) {
        Main cat=new Main();
        
        for(int i=0 ; i<args.length ; i++) {
            if (args[i].startsWith("username=")) {
                cat.setUsername(args[i].split("=")[1]);
            }
            else if (args[i].startsWith("password=")) {
                cat.setPassword(args[i].split("=")[1]);
            }
            else if (args[i].startsWith("operation=")) {
                cat.setOperation(args[i].split("=")[1]);
            }
            else if (args[i].startsWith("factory=")) {
                cat.setFactory(args[i].split("=")[1]);
            }
            else if (args[i].startsWith("provider=")) {
                cat.setProvider(args[i].split("=")[1]);
            }
        }
        
        if (cat.errors().length()!=0) {
            System.err.println("Usage: ConfigurationAntTask -username <username> -password <password> -operation <operation> -provider <provider> -factory <factory>");
            System.err.println("The following were not supplied: "+cat.errors());
        }
        else {
            cat.execute();
            System.out.println("Configure server opertaion: "+cat.getOperation()+", successful.");
        }
    }
    
    public String getFactory() {
        return factory;
    }

    public void setFactory(String factory) {
        this.factory = factory;
    }

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String errors() {
        StringBuffer errors=new StringBuffer();
        
        if (username==null || username.length()==0) {
            errors.append("username ");
        }
        
        if (password==null || password.length()==0) {
            errors.append("password ");
        }

        if (operation==null || operation.length()==0) {
            errors.append("operation ");
        }

        if (factory==null || factory.length()==0) {
            errors.append("factory ");
        }

        if (provider==null || provider.length()==0) {
            errors.append("provider ");
        }

        return errors.toString();
    }
    
    public void execute() {
        System.setProperty("java.naming.factory.initial", factory);
        System.setProperty("java.naming.provider.url", provider);
        
        try {
            try {    
                Method method=server.getClass().getMethod(operation);
                
                method.invoke(server);
            }
            catch(InvocationTargetException e) {
                throw e.getCause();
            }
        }
        catch(Throwable e) {
            e.printStackTrace();
            throw new Error("Failed to upload content: "+e);
        }
    }
}
