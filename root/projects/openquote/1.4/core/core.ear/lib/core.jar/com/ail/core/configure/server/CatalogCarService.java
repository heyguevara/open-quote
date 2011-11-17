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
import java.util.ArrayList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.command.CommandArg;

/**
 * Service to catalog the contents of a CAR file. This service is passed a car file and
 * returns a list of the namespaces found in the file.
 */
public class CatalogCarService extends Service {
    private CatalogCarArg args = null;
    private Core core = null;

    /** Default constructor */
    public CatalogCarService() {
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
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (CatalogCarArg)args;
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
        if (args.getCarArg()==null) {
            throw new PreconditionException("args.getCarArg()==null");
        }

        try {
            // create a place to put the resutls.
            args.setNamespacesRet(new ArrayList<String>());
            
            // Open the CAR as a zip
            ZipInputStream zis=new ZipInputStream(new ByteArrayInputStream(args.getCarArg()));

            // Loop through the entries in the zip. Each entry is a configuration, each entry's name is
            // the configurations namespace. Simply add the name to the list to be returned.
            for(ZipEntry ze=zis.getNextEntry() ; ze!=null ; ze=zis.getNextEntry() ) {
                args.getNamespacesRet().add(ze.getName());
                zis.closeEntry();
            }

            zis.close();
        }
        catch(Exception e) {
            throw new CarProcessingError("Failed to catalog car", e);
        }
    }
}


