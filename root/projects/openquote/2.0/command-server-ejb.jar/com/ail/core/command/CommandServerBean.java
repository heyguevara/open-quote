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
package com.ail.core.command;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.SessionContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.ail.annotation.Configurable;
import com.ail.core.EJBComponent;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.XMLString;

/**
 * Message Driven Bean which listens on a queue for commands to execute.
 */
@Configurable
@MessageDriven(activationConfig = {
@ActivationConfigProperty(propertyName="destinationType", propertyValue="javax.jms.Queue"),
@ActivationConfigProperty(propertyName="destination", propertyValue="queue/AilCommandQueue")}
)
public class CommandServerBean extends EJBComponent implements MessageListener {
    private static final String namespace="com.ail.core.command.CommandServerBean";
    private SessionContext ctx; 
    
    public CommandServerBean() {
        initialise(namespace);
    }
    
    @PostConstruct
    public void postConstruct() {
        initialise(namespace);
    }
    
    public void onMessage(Message msg) {
        try {
            TextMessage tm = (TextMessage) msg;
            
            setVersionEffectiveDate(new VersionEffectiveDate(tm.getLongProperty("VersionEffectiveDate")));
            
            // the command comes to us as a string of XML
            XMLString argumentXml=new XMLString(tm.getText());
            
            Argument argument=(Argument)getCore().fromXML(argumentXml.getType(), argumentXml);

            // We take the base name of the class as the command name: i.e. if the command class is
            // "com.ail.core.logging.LoggerArgumentImpl" the command name will be "LoggerArgumentImpl".
            String commandName=argument.getClass().getName();
            commandName=commandName.substring(commandName.lastIndexOf('.')+1);
            
            com.ail.core.command.Command command = getCore().newCommand(commandName, Command.class);
            command.setArgs(argument);
            command.setCallersCore(this);
            command.invoke();
        }
        catch(Throwable t) {
            t.printStackTrace();
            ctx.setRollbackOnly();
        }
    }

    @Resource
    private void setSessionContext(SessionContext ctx) {
      this.ctx = ctx;
    }
    
    @Override
    public SessionContext getSessionContext() {
        return ctx;
    }
}
