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

package com.ail.core.product;

import javax.ejb.CreateException;
import javax.ejb.SessionContext;
import javax.ejb.Stateless;

import com.ail.core.BaseServerException;
import com.ail.core.Core;
import com.ail.core.EJBComponent;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandArg;
import com.ail.core.configure.Configuration;
import com.ail.core.product.listproducts.ListProductsArg;
import com.ail.core.product.newproducttype.NewProductTypeArg;
import com.ail.core.product.registerproduct.RegisterProductArg;
import com.ail.core.product.removeproduct.RemoveProductArg;
import com.ail.core.product.resetallproducts.ResetAllProductsArg;
import com.ail.core.product.resetproduct.ResetProductArg;
import com.ail.core.product.updateproduct.UpdateProductArg;

@Stateless
public class ProductManagerBean extends EJBComponent implements ProductManager, ProductManagerLocal {
    private VersionEffectiveDate versionEffectiveDate = null;
    private Core core = null;
    private SessionContext ctx = null;

    public ProductManagerBean() {
        versionEffectiveDate = new com.ail.core.VersionEffectiveDate();
        core = new Core(this);
    }

    public void setSessionContext(SessionContext context) {
        ctx = context;
    }

    public SessionContext getSessionContext() {
        return ctx;
    }

    public void ejbActivate() {
    }

    public void ejbPassivate() {
    }

    public void ejbRemove() {
    }

    public void ejbCreate() throws CreateException {
        versionEffectiveDate = new com.ail.core.VersionEffectiveDate();
        core = new com.ail.core.Core(this);
    }

    private CommandArg invokeCommand(String name, CommandArg arg) {
        try {
            com.ail.core.command.AbstractCommand command = core.newCommand(name);
            command.setArgs(arg);
            command.invoke();
            return command.getArgs();
        }
        catch (com.ail.core.BaseException e) {
            throw new com.ail.core.BaseServerException(e);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    /**
     * Expose services of this EJB via XML. This method unmarshals the XML argument string into
     * an object, finds a method on the EJB to accept that object type as an argument
     * and invokes it. The result returned from the method is marshalled back into XM and returned.<p>
     * The methods are invoked on the context's local interface if possible (if one
     * exists). If no local interface is found then the remote interface is used instead.
     * Invoking methods via the local/remote interface means that the deployment settings
     * for security and transacts will be honoured.
     * @param xml XML argument to be passed to the service.
     * @return XML returned from the service.
     */
    public String invokeServiceXML(String xml) {
        return super.invokeServiceXML(xml, ctx);
    }

    public Core getCore() {
        return core;
    }

    public VersionEffectiveDate getVersionEffectiveDate() {
        return versionEffectiveDate;
    }

    public void setConfiguration(Configuration config) {
        try {
            super.setConfiguration(config);
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public Configuration getConfiguration() {
        try {
            return super.getConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public String getConfigurationNamespace() {
        return super.getConfigurationNamespace();
    }

    public void resetConfiguration() {
        try {
            super.resetConfiguration();
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    public Version getVersion() {
        try {
            Version v = (com.ail.core.Version) core.newType("Version");
            v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
            v.setDate("$Date: 2007/10/05 22:47:50 $");
            v.setSource("$Source: /home/bob/CVSRepository/projects/core/core.ear/product-manager-ejb.jar/com/ail/core/product/ProductManagerBean.java,v $");
            v.setState("$State: Exp $");
            v.setVersion("$Revision: 1.5 $");
            return v;
        }
        catch (com.ail.core.BaseError e) {
            throw new com.ail.core.BaseServerException(e);
        }
    }

    /**
     * Service wrapper method for the ListProducts service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ListProductsArg getListProducts(ListProductsArg arg) throws BaseServerException {
        return (com.ail.core.product.listproducts.ListProductsArg) invokeCommand("ListProducts", arg);
    }

    /**
     * Service wrapper method for the RegisterProduct service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public RegisterProductArg registerProduct(RegisterProductArg arg) throws BaseServerException {
        return (RegisterProductArg) invokeCommand("RegisterProduct", arg);
    }

    /**
     * Service wrapper method for the RemoveProduct service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public RemoveProductArg removeProduct(RemoveProductArg arg) throws BaseServerException {
        return (RemoveProductArg) invokeCommand("RemoveProduct", arg);
    }

    /**
     * Service wrapper method for the ResetProduct service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ResetProductArg getProductDefinition(ResetProductArg arg) throws BaseServerException {
        return (ResetProductArg) invokeCommand("ResetProduct", arg);
    }

    /**
     * Service wrapper method for the ResetAllProductsArg service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public ResetAllProductsArg resetAllProducts(ResetAllProductsArg arg) throws BaseServerException {
        return (ResetAllProductsArg) invokeCommand("ResetAllProducts", arg);
    }

    /**
     * Service wrapper method for the UpdateProductDefinition service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public UpdateProductArg updateProduct(UpdateProductArg arg) throws BaseServerException {
        return (UpdateProductArg) invokeCommand("UpdateProduct", arg);
    }

    /**
     * Service wrapper method for the NewProductType service.
     * @param arg Argument to pass to the service
     * @return Return value from the service
     * @throws BaseServerException In response to exceptions thrown by the service.
     */
    public NewProductTypeArg newProductType(NewProductTypeArg arg) throws BaseServerException {
        return (NewProductTypeArg) invokeCommand("NewProductType", arg);
    }
}


