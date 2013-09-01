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

import javax.annotation.Resource;
import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.MessageDrivenContext;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import com.ail.core.MessagingComponent;
import com.ail.core.command.OnMessageService.OnMessageCommand;

/**
 * Message Driven Bean which listens on a queue for commands to execute.
 */
@MessageDriven(name = "LastChanceCommandServerBean", activationConfig = { @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "queue/OpenQuoteLastChanceCommandQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "AUTO_ACKNOWLEDGE"), @ActivationConfigProperty(propertyName = "minSessions", propertyValue = "25"),
        @ActivationConfigProperty(propertyName = "maxSessions", propertyValue = "50") })
public class LastChanceServerBean extends MessagingComponent implements MessageListener {

    public LastChanceServerBean() {
        initialise("com.ail.core.command.LastChanceServerBean");
    }

    @Resource
    public void setSessionContext(MessageDrivenContext context) {
        super.setSessionContext(context);
    }

    @Override
    public void onMessage(final Message msg) {
        String messageId = null;
        
        try {
            messageId = msg.getJMSMessageID();

            OnMessageCommand command = getCore().newCommand(OnMessageCommand.class);
            command.setMessageArg((TextMessage)msg);
            command.invoke();

            getCore().logInfo("Message (id=" + messageId + ") processed successfully.");
        } catch (Throwable t) {
            getCore().logInfo("Message (id=" + messageId + ") failed to process after the maximum number of retries.");
            t.printStackTrace(System.err);
            getSessionContext().setRollbackOnly();
        }
    }
}
