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

package com.ail.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.rmi.RemoteException;
import java.security.Principal;

import javax.ejb.EJBContext;
import javax.ejb.EJBException;
import javax.ejb.SessionContext;

import com.ail.core.command.Command;

/**
 * This class is use as a superclass by all EJB components. 
 */
public abstract class EJBComponent extends Component {

    /**
     * This method must be used by all EJB components to expose their functionality as
     * XML. The session bean extends this class and deligates calls to its public
     * 'invokeServiceXML' method to this method. The EJBs remote/local interfaces
     * expose the EJB's invokeServiceXML making it available to clients.<p>
     * Take an argument defined as XML, unmarshal it into a service arg object, and invoke
     * the appropriate service to deal with it. Marshal the object returned back into XML
     * and return it as a String.<p>
     * This method depends on a number of conversions in order to work. It assumes the
     * the following relationship between type of the argument received (indicated by the
     * xsi:type of the argument's root element) and the method to be invoked:
     * <ol>
     * <li>The method will return an instance of the arg type - or a super type of arg type.</li>
     * <li>The method's name will include type types name. e.g. If the type is
     * "com.ail.core.configure.server.GetConfigurationArgImp" then the method will be
     * called "getConfiguration".</li>
     * </ol>
     * @param xml Service arg as XML
     * @return Service arg returned from the service
     */
    protected String invokeServiceXML(String xml, SessionContext ctx) throws EJBException {
        try {
            Object ejb=null;
            Class<?> argType=null;

            XMLString xmls=new XMLString(xml);
            argType=xmls.getType();

            // try to get a local reference. If that fails fall back on
            // a remote one.
            try {
                ejb=ctx.getEJBLocalObject();
            }
            catch(IllegalStateException e) {
                ejb=ctx.getEJBObject();
            }

            // Figure out the method name: typeName of 'com.ail.arg.SomeServiceArgImp' gives 'someService'
            String typeName=argType.getName();
            int lastDot=typeName.lastIndexOf('.');
            String methodName=Character.toLowerCase(typeName.charAt(lastDot+1))+
                              typeName.substring(lastDot+2, typeName.length()-6);

            // Find the method on obj that returns the arg type we're using
            Method[] methods=ejb.getClass().getMethods();
            Method method=null;
            for(int i=methods.length-1 ; i>=0 ; i--) {
                if (methods[i].getName().equals(methodName) && methods[i].getReturnType().isAssignableFrom(argType)) {
                    method=methods[i];
                    break;
                }
            }

            // If we didn't find a method, bail out now.
            if (method==null) {
                throw new EJBException("Fail to find method to accept an argument of type: "+argType.getName());
            }

            // Invoke the method
            Object[] args=new Object[]{getCore().fromXML(argType, xmls)};
            Object ret=method.invoke(ejb, args);

            // convert the return value back into xml
            return getCore().toXML(ret).toString();
        }
        catch(XMLException e) {
            throw new EJBException(e);
        }
        catch(ClassNotFoundException e) {
            throw new EJBException(e);
        }
        catch(IllegalAccessException e) {
            throw new EJBException(e);
        }
        catch(InvocationTargetException e) {
            Throwable t=e.getTargetException();

            // Don't wrap one EJBException in another!
            if (t instanceof EJBException) {
                throw (EJBException)t;
            }
            // Unwrap the detail of a RemoteException if possible.
            if (t instanceof RemoteException) {
                RemoteException re=(RemoteException)t;
                if (re.detail instanceof BaseServerException) {
                    throw (BaseServerException)(re.detail);
                }
                else {
                    throw new EJBException(e);
                }
            }
            else {
                throw new EJBException(e.getTargetException().toString());
            }
        }
    }

    /**
     * Returns the context passed to the EJB's setSessionContext method.
     * @return session context.
     */
    public abstract EJBContext getSessionContext();

    /**
     * Return the security principal associated with this EJB. This will be the
     * same principal passed to the bean in the SessionContext.
     */
    public Principal getSecurityPrincipal() {
        try {
            return getSessionContext().getCallerPrincipal();
        }
        catch(NullPointerException e) {
            return null;
        }
    }

    protected <T extends Command> T invokeCommand(Core core, String name, T sourceCommand) {
        Command command = core.newCommand(name, Command.class);
        return invokeCommand(command, sourceCommand);
    }

    private <T extends Command> T invokeCommand(Command localCommand, T sourceCommand) {
        try {
            localCommand.setArgs(sourceCommand.getArgs());
            localCommand.invoke();
            sourceCommand.setArgs(localCommand.getArgs());
            return sourceCommand;
        }
        catch (com.ail.core.BaseException e) {
            throw new com.ail.core.BaseServerException(e);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

}
