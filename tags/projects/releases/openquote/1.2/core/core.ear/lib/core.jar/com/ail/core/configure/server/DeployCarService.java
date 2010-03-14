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

package com.ail.core.configure.server;

import java.io.ByteArrayInputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.XMLString;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.ConfigurationHandler;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/08/20 15:03:54 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/DeployCarService.java,v $
 * @stereotype service
 */
public class DeployCarService extends Service {
    private DeployCarArg args = null;
    private Core core = null;

    /** Default constructor */
    public DeployCarService() {
        core = new Core(this);
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
        Version v = (Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2006/08/20 15:03:54 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/server/DeployCarService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.3 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (DeployCarArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of DeployParFileArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException {
        if (args.getNamespacesArg()==null) {
            throw new PreconditionException("args.getNamespacesArg()==null");
        }

        if (args.getCarArg()==null) {
            throw new PreconditionException("args.getCarArg()==null");
        }

        if (args.getNamespacesArg().size()==0) {
            // Odd to call deploy and not specifiy anything to deploy. Odd, but not wrong.
            return;
        }

        try {
            ZipInputStream zis=new ZipInputStream(new ByteArrayInputStream(args.getCarArg()));
            Configuration config=null;
            byte[] block=new byte[1024];
            StringBuffer buffer=null;
            int bytesRead=0;

            // Loop through the entries in the zip. Each entry is a configuration, each entry's name is
            // the configurations namespace.
            for(ZipEntry ze=zis.getNextEntry() ; ze!=null ; ze=zis.getNextEntry() ) {
                // Does the list of namespaces we've been asked to deploy include this one?
                if (args.getNamespacesArg().contains(ze.getName())) {

                    core.logInfo("Reading '"+ze.getName()+"' from car file");
                    
                    // Create a buffer to hold the contents of the entry - the configuration as a string of XML
                    buffer=new StringBuffer();
                    
                    // Read all the bytes for this entry from the zip into the buffer
                    for(bytesRead=zis.read(block, 0, block.length) ; bytesRead!=-1 ; bytesRead=zis.read(block, 0, block.length)) {
                        buffer.append(new String(block, 0, bytesRead));
                    }
                    
                    core.logInfo(buffer.toString());
                    
                    // Marshal the String of XML into a configuration object.
                    config=core.fromXML(Configuration.class, new XMLString(buffer.toString()));
                    
                    // This allows (or forces) the existing config to be overwritten. The configuration loaders
                    // check this date in the config we're saving against the one in the database, and if they're
                    // not the same, throw an update collision exception. Setting this to null prevents the check.
                    config.setValidFrom(null);
                    
                    // Deploy the config.
                    ConfigurationHandler.getInstance().saveConfiguration(ze.getName(), config, core);
                }
                
                zis.closeEntry();
            }

            zis.close();
        }
        catch(Exception e) {
            throw new CarProcessingError("Failed to deploy car", e);
        }
    }
}


