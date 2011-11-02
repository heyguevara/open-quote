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

package com.ail.core.key;

import java.util.HashMap;
import java.util.Map;

import com.ail.core.Core;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;

public class GenerateUniqueKeyService extends Service {
    private GenerateUniqueKeyArg args = null;
    private static Map<String,UniqueNumberHandler> uniqueNumberHandlers=new HashMap<String,UniqueNumberHandler>();

    private Core core;

    public GenerateUniqueKeyService() {
        super();
        core = new Core(this);
    }

    public GenerateUniqueKeyService(Core core) {
        super();
        this.core = core;
    }
    
    
    /**
     * Getter to fetch the entry point's core. This method is demanded by the EntryPoint class.
     * 
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * 
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) getCore().newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2006/08/20 15:03:54 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/persistence/hibernate/GenerateUniqueKeyService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.2 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * 
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (GenerateUniqueKeyArg) args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * 
     * @return An instance of UpdateArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, PostconditionException {
        if (args.getKeyIdArg()==null || args.getKeyIdArg().length()==0) {
            throw new PreconditionException("args.getKeyIdArg()==null || args.getKeyIdArg().length()==0");
        }
        
        String keyIdArg=args.getKeyIdArg();
        
        synchronized(uniqueNumberHandlers) {
            if (!uniqueNumberHandlers.containsKey(keyIdArg)) {
                uniqueNumberHandlers.put(keyIdArg, new UniqueNumberHandler());
            }
        }
            
        UniqueNumberHandler uniqueNumberHandler=uniqueNumberHandlers.get(keyIdArg);
            
        synchronized(uniqueNumberHandler) {
            if (uniqueNumberHandler.isBlockEmpty()) {
                Configuration config=getConfiguration();
                int nextNumber=Integer.parseInt(config.findParameter(keyIdArg+"NextNumber").getValue());
                int blockSize=Integer.parseInt(config.findParameter(keyIdArg+"BlockSize").getValue());
    
                uniqueNumberHandler.setNextNumber(nextNumber);
                uniqueNumberHandler.setBlockEnd(nextNumber+blockSize);
    
                config.findParameter(keyIdArg+"NextNumber").setValue(Integer.toString(nextNumber+blockSize+1));
    
                setConfiguration(config);
            }
            
            args.setKeyRet(uniqueNumberHandler.getNextNumber());
        }

        if (args.getKeyRet()==null) {
            throw new PostconditionException("args.getKeyRet()==null");
        }
    }
}

/**
 * Private class used to track the usage of unique numbers.
 */
class UniqueNumberHandler {
    private int blockEnd=0;
    private int nextNumber=0;

    public boolean isBlockEmpty() {
        return blockEnd==nextNumber;
    }

    public int getNextNumber() {
        return nextNumber++;
    }

    public void setNextNumber(int next) {
        nextNumber=next;
    }

    public void setBlockEnd(int blockEnd) {
        this.blockEnd=blockEnd;
    }
}