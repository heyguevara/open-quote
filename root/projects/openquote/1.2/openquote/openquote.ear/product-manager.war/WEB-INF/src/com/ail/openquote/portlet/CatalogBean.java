/* Copyright Applied Industrial Logic Limited 2006. All rights reserved. */
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
package com.ail.openquote.portlet;

import static javax.faces.application.FacesMessage.SEVERITY_ERROR;

import java.security.Principal;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.ListDataModel;

import com.ail.core.CoreProxy;
import com.ail.core.Functions;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.configure.Configuration;
import com.ail.core.configure.Group;
import com.ail.core.configure.Parameter;
import com.ail.core.configure.server.ServerDeligate;

/**
 * JSF backing bean for the product catalog portlet.
 */
public class CatalogBean {
    private CoreProxy core=new CoreProxy();
    private ListDataModel products=new ListDataModel();
    private Throwable errorCause;
    private String errorMessage;
    private String productName;
    private String productNameAsLoaded;
    private String productDescription;
    private ServerDeligate serverDeligate;

    public CatalogBean() {
    }

    public ListDataModel getProducts() {
        return products;
    }

    public void setProducts(ListDataModel products) {
        this.products = products;
    }

    public Throwable getErrorCause() {
        return errorCause;
    }

    public void setError(Throwable errorCause) {
        this.errorCause = errorCause;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductNameAsLoaded() {
        return productNameAsLoaded;
    }

    public void setProductNameAsLoaded(String productNameAsLoaded) {
        this.productNameAsLoaded = productNameAsLoaded;
    }

    public boolean isRemovable() {
        return productNameAsLoaded!=null && productNameAsLoaded.length()!=0;
    }
    
    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String displayPropertiesAction() {
        Parameter prod=(Parameter)getProducts().getRowData();
        setProductName(prod.getName());
        setProductNameAsLoaded(prod.getName());
        setProductDescription(prod.getValue());
        return "displayPropertiesAction.success";
    }
    
    public String resetAllProductsAction() {
        try {
            core.logInfo("Resetting all products.");
            serverDeligate().resetNamedConfiguration("com.ail.core.product.resetallproducts.ResetAllProductsService");
            core.logInfo("Reset successful.");
        }
        catch(Exception e) {
            core.logError("Reset failed", e);
        }

        return null;
    }
    
    /**
     * Action called when the user opts to register a new product. This action
     * leads into the product property screen with everything set to blank.
     */
    public String registerNewProductAction() {
        setProductName("");
        setProductDescription("");

        // An empty string in 'as loaded' indicates that this is a new registration.
        setProductNameAsLoaded("");

        return "registerNewProductAction.success";
    }
    
    /**
     * Action to clear the configuration cache associated with a product.
     * This action is executed when the user clicks the recycle icon next to a
     * product name. During product development you frequently want to foce the
     * system to reload a product's artifacts rather than relying on what it has
     * previously cached. For example, if you've just made a change to a page flow,
     * or updated the rating rules, you want to ensure that the latest version 
     * are picked up for the next quotation.
     */
    public String clearProductCacheAction() {
        try {
            Parameter prod=(Parameter)getProducts().getRowData();
            String selectedProduct=prod.getName();
            String configurationName=Functions.productNameToConfigurationNamespace(selectedProduct);
            core.logInfo("Clearing cache for product: "+selectedProduct);
            serverDeligate().clearNamedConfigurationCache(configurationName);
            core.logInfo("Clear successful.");
        }
        catch(Exception e) {
            core.logError("Reset failed", e);
            setErrorMessage("A system error prevented the product cache from being successfully cleared.");
            return "clearProductCacheAction.failure";
        }

        return null;
    }
    
    public String saveProductPropertiesAction() {
        Parameter prod=null;

        if (getProductName().length()==0) {
            addError("properties:form_location", SEVERITY_ERROR, "Required", "Location cannot be blank");
            return null;
        }
        
        // if this is a newly registered product...
        if (getProductNameAsLoaded().length()==0) {
            // check if a product by this name is alredy defined
            if (core.getParameter("products."+getProductName())!=null) {
                addError("properties:form_location", SEVERITY_ERROR, "Invalid", "A product using this location already exists.");
                return null;
            }
            // else it doesn't exist, so go ahead and create the entry
            else {
                try {
                    core.logInfo("Registering new product: "+getProductName());
                    Configuration config=core.getConfiguration();
                    Group prods=config.findGroup("products");
                    prod=new Parameter();
                    prod.setName(getProductName());
                    prod.setValue(getProductDescription());
                    prods.addParameter(prod);
                    core.setConfiguration(config);
                    resetAllProductsAction();
                    core.logInfo("Registration successful.");
                }
                catch(Exception e) {
                    core.logError("Registration failed.", e);
                    setErrorMessage("A system error prevented the product from being successfully regustered.");
                    return "saveProductPropertiesAction.failure";
                }
            }
        }
        else {
            // it's not a new registration, so it must be an update to an existing entry
            try {
                core.logInfo("Updating product registration: "+getProductName());
                Configuration config=core.getConfiguration();
                
                // find the entry for the product 'as loaded' - it may be that the update
                // changes the product's name, so we find the one that was loaded.
                for(Parameter p: config.findGroup("products").getParameter()) {
                    if (p.getName().equals(getProductNameAsLoaded())) {
                        prod=p;
                    }
                }
                
                // update the product's details if it was found
                if (prod!=null) {
                    prod.setName(getProductName());
                    prod.setValue(getProductDescription());
                    core.setConfiguration(config);
                    serverDeligate().clearNamedConfigurationCache("ProductCatalog");
                }
                else {
                    core.logError("Attempted to update a product ("+getProductNameAsLoaded()+") that no longer exists.");
                    setErrorMessage("The product updated failed because the catalog entry has been removed by another user.");
                    return "saveProductPropertiesAction.failure";
                }
                
                core.logInfo("Update successful.");
            }
            catch(Exception e) {
                core.logError("Failed to update product registration.", e);
                setErrorMessage("A system error prevented the product catalog from being updated.");
                return "saveProductPropertiesAction.failure";
            }
        }

        return "saveProductPropertiesAction.success";
    }
    
    public String cancelProductPropertiesAction() {
        return "cancelProductPropertiesAction.success";
    }
    
    public String continueAction() {
        return "continueAction.success";
    }
    
    public String removeProductAction() {
        if (getProductName().length()==0) {
            addError("properties:form_location", SEVERITY_ERROR, "Required", "A product location is required.");
            return null;
        }
        
        try {
            core.logInfo("Removing product registration: "+getProductName());
            Configuration config=core.getConfiguration();
            
            Group prods=config.findGroup("products");
            
            // find the entry for the product 'as loaded' - it may be that the update
            // changes the product's name, so we find the one that was loaded.
            for(int i=0 ; i<prods.getParameterCount() ; i++) {
                if (prods.getParameter(i).getName().equals(getProductNameAsLoaded())) {
                    prods.removeParameter(i);
                    core.setConfiguration(config);
                    serverDeligate().clearNamedConfigurationCache("ProductCatalog");
                    core.logInfo("Product removed successfully.");
                    return "removeProductAction.success";
                }
            }
        }
        catch(Exception e) {
            core.logError("Failed to remove product: "+getProductNameAsLoaded(), e);
            return "removeProductAction.failure";
        }
            
        addError("properties:form_location", SEVERITY_ERROR, "Unknown", "Product does not exist.");
        return "removeProductAction.success";        
    }
    
    private ServerDeligate serverDeligate() {
        if (serverDeligate==null) {
            serverDeligate=new ServerDeligate(core.getSecurityPrincipal());
        }
        
        return serverDeligate;
    }    

    public void refresh() {
        Principal p=FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal();
        VersionEffectiveDate ved=new VersionEffectiveDate();
        core=new CoreProxy("ProductCatalog", ved, p);        
        Group g=core.getGroup("products");        
        products.setWrappedData(g.getParameter());
    }

    /** 
     * Add a validation error to the page context.
     * @param context Page context (component IDs)
     * @param severity Message severity
     * @param summary Error message summary
     * @param detail Error message detail
     */
    private void addError(String context, FacesMessage.Severity severity, String summary, String detail) {
        FacesContext ctx=FacesContext.getCurrentInstance();        
        FacesMessage message=new FacesMessage(severity, summary, detail);
        ctx.addMessage(context, message);        
    }
}
