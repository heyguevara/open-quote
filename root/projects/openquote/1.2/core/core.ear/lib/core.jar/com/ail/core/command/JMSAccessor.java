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

package com.ail.core.command;

import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.ail.core.CoreProxy;
import com.ail.core.Version;
import com.ail.core.configure.Configuration;

/**
 * Deployment specific command for use with JMS. This accessor object acts
 * as a client to a point-to-point JMS queue. The accessor takes two parameters: Factory and Queue which it uses to
 * locate the ConnectionFacctory and Queue via JNDI.</p>
 * The following example shows how a JMSAccessor can be configured:
 * <pre>
 *   &lt;service name="NotifyPartyByEmailService" builder="ClassBuilder" key="com.ail.core.command.JMSAccessor"&gt;
 *     &lt;parameter name="Factory"&gt;ConnectionFactory&lt;/parameter&gt;
 *     &lt;parameter name="Queue"&gt;queue/AilCommandQueue&lt;/parameter&gt;
 *   &lt;/service&gt;
 * </pre>
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/02/16 21:33:41 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/command/JMSAccessor.java,v $
 */
public class JMSAccessor extends Accessor {
    private String factory=null;
    private String queue=null;
    private CommandArg args=null;
    private transient QueueConnectionFactory connectionFactoryInstance=null;
    private transient Queue queueInstance=null;

    public void invoke() throws JMSServiceException {
        TextMessage msg;
        QueueConnection conn=null;
        QueueSession session=null;
        QueueSender sender=null;

        try {
            conn = getConnectionFactoryInstance().createQueueConnection();
            session = conn.createQueueSession(false, QueueSession.AUTO_ACKNOWLEDGE);
            conn.start();
                        
            msg=session.createTextMessage(new CoreProxy().toXML(args).toString());
            msg.setStringProperty("ArgType", args.getClass().getName());
            msg.setLongProperty("VersionEffectiveDate", args.getCallersCore().getVersionEffectiveDate().getTime());
            
            sender = session.createSender(getQueueInstance());
            sender.send(msg);
        }
        catch (Exception e) {
            throw new JMSServiceException(e);
        }
        finally {
            try {
                if (sender!=null) sender.close();
                if (conn!=null) conn.stop();
                if (session!=null) session.close();
                if (conn!=null) conn.close();
            }
            catch (Exception e) {
                throw new JMSServiceException(e);
            }
        }
    }

    public void setArgs(CommandArg args) {
        this.args=args;
    }

    public CommandArg getArgs() {
        return args;
    }

	public Version getVersion() {
        throw new CommandInvocationError("Get version cannot be invoked on a JMSAccessor service");
	}

	public Configuration getConfiguration() {
        throw new CommandInvocationError("Get configuration cannot be invoked on a JMSAccessor service");
    }

	public void setConfiguration(Configuration properties) {
        throw new CommandInvocationError("Set configuration cannot be invoked on a JMSAccessor service");
    }

    public void setFactory(String factory) {
		this.factory=factory;
    }

    public String getFactory() {
		return factory;
    }

    public void setQueue(String queue) {
		this.queue=queue;
    }

    public String getQueue() {
		return queue;
    }

    private QueueConnectionFactory getConnectionFactoryInstance() throws NamingException {
        if (connectionFactoryInstance==null) {
            InitialContext iniCtx = new InitialContext();;
            connectionFactoryInstance = (QueueConnectionFactory)iniCtx.lookup(getFactory());
        }
        
        return connectionFactoryInstance;
    }

    private Queue getQueueInstance() throws NamingException {
        if (queueInstance==null) {
            InitialContext iniCtx = new InitialContext();;
            queueInstance = (Queue) iniCtx.lookup(getQueue());
        }
        
        return queueInstance;
    }
}
