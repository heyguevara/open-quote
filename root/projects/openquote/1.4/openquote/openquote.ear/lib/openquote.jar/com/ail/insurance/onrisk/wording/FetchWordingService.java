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

package com.ail.insurance.onrisk.wording;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.openquote.SavedQuotation;
/**
 * This service fetches the wording document associated with a quotation. If the document hasn't been generated
 * in the past, the service will generate it and persist it with the quotation for reuse later.
 */
public class FetchWordingService extends Service {
    private static final long serialVersionUID = 3198893603833694389L;
    private FetchWordingArg args=null;
	private Core core=null;

	/**
     * Default constructor
     */
	public FetchWordingService() {
		core=new Core(this);
    }

	/**
     * Getter to fetch the entry point's code. This method is demanded by
     * the EntryPoint class.
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
		Version v=(Version)core.newType("Version");
        v.setAuthor("$Author$");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date$");
        v.setSource("$Source$");
        v.setState("$State$");
        v.setVersion("$Revision$");
        return v;
    }

	/**
     * Setter used to the set the entry points arguments. This method will be
     * called before <code>invoke()</code> is called.
     * @param Arguments for invoke
     */
    public void setArgs(CommandArg args){
        this.args = (FetchWordingArg)args;
    }

	/**
     * Getter returning the arguments used by this entry point. This entry point
     * doesn't modify the arguments.
     * @return An instance of LoggerArgs.
	 */
    public CommandArg getArgs() {
        return (CommandArg)args;
    }

    /**
     * The 'business logic' of the entry point.
     * @throws PreconditionException If one of the preconditions is not met
     */
	public void invoke() throws BaseException {
        if (args.getPolicyNumberArg()==null || args.getPolicyNumberArg().length()==0) {
            throw new PreconditionException("args.getPolicyNumberArg()==null || args.getPolicyNumberArg().length()==0");
        }

        // Fetch the saved quote from persistence
        SavedQuotation savedQuotation=(SavedQuotation)getCore().queryUnique("get.savedPolicy.by.policyNumber", args.getPolicyNumberArg());
        
        if (savedQuotation==null) {
            throw new PreconditionException("core.queryUnique(get.savedPolicy.by.policyNumbe, "+args.getPolicyNumberArg()+")==null");
        }
        
        // We only generate quote docs on demand, so if there isn't one - generate it.
        if (savedQuotation.getWordingDocument()==null) {
        	GenerateWordingCommand cmd=(GenerateWordingCommand)getCore().newCommand("GenerateWordingDocument");
            cmd.setPolicyArg(savedQuotation.getQuotation());
            cmd.invoke();
            savedQuotation.setWordingDocument(cmd.getDocumentRet());
            savedQuotation=getCore().update(savedQuotation);
            args.setPolicyRet(savedQuotation.getQuotation());
        }

        args.setDocumentRet(savedQuotation.getWordingDocument());
    }
}
