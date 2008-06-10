/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.openquote.ui;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.OrderedLinkedList;

/**
 * A Repeater represents Collections of data on the UI. Subclasses of this type define how the
 * data is actually rendered on the UI, this class provides general support for dealing with
 * repeating UI elements.
 */
public abstract class Repeater extends PageElement {
	private static final long serialVersionUID = -6043887157243002172L;
	/** 
     * The name of the Type being repeated. The type must be recognizable by {@link com.ail.core.CoreProxy#newType(String) Core.newType(String)}.
     * The repeater will create instances of this type in response to the 'add' link being selected. If the {@link #isAddAndDeleteEnabled() addDeleteEnabled} 
     * property is false, this property may be null as it will never be referred to.  
	 */
    protected String type;

    /**
     * Set to true if the user should be able to add and remove elements from the repeater.
     */
    protected boolean addAndDeleteEnabled=true;

    /**
     * Minimum number of repeated items. If {@link #isAddAndDeleteEnabled() addAndDeleteEnabled} is true, the user
     * will be prevented from removing items from the list if removal would take the could below this number. Also, note
     * that the default value of 0 means that users can remove all items.
     */
    protected int minRows=0;
    
    /**
     * The maximum number of items that can appear in the list. The default value of -1 means there is no limit.
     */
    protected int maxRows=-1;   // -1 means 'no limit'

    /**
     * List of repeated fields. In a {@link RowScroller} this defines the columns and what they are bound to; in
     * a {@link SectionScroller} it defines the items listed in each section.
     */
    protected List<AttributeField> item=null;

    /**
     * Default constructor
     */
    public Repeater() {
        super();
        item=new OrderedLinkedList<AttributeField>();
    }

    /**
     * The maximum number of items that can appear in the list. The default value of -1 means there is no limit.
     * @return Number of records allowed in the repeater.
     */
    public int getMaxRows() {
        return maxRows;
    }

    /**
     * @see #getMaxRows()
     * @param maxRows Number of records allowed in the repeater.
     */
    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    /**
     * Minimum number of repeated items. If {@link #isAddAndDeleteEnabled() addAndDeleteEnabled} is true, the user
     * will be prevented from removing items from the list if removal would take the could below this number. Also, note
     * that the default value of 0 means that users can remove all items.
     * @return Minimum number of records allowed in the list.
     */
    public int getMinRows() {
        return minRows;
    }

    /**
     * @see #getMinRows()
     * @param minRows Minimum number of records allowed in the list.
     */
    public void setMinRows(int minRows) {
        this.minRows = minRows;
    }

    /**
     * The name of the Type being repeated. The type must be recognizable by {@link com.ail.core.CoreProxy#newType(String) Core.newType(String)}.
     * The repeater will create instances of this type in response to the 'add' link being selected. If the {@link #isAddAndDeleteEnabled() addDeleteEnabled} 
     * property is false, this property may be null as it will never be referred to.  
     * @return Name of type, or null.
     */
    public String getType() {
        return type;
    }

    /**
     * @see #getType()
     * @param type Name of type, or null.
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * List of repeated fields. In a {@link RowScroller} this defines the columns and what they are bound to; in
     * a {@link SectionScroller} it defines the items listed in each section.
     * @return List of repeating fields.
     */
    public List<AttributeField> getItem() {
        return item;
    }

    /**
     * @see #getId()
     * @param item List of repeating fields.
     */
    public void setItem(List<AttributeField> item) {
        this.item = item;
    }

    /**
     * Alternate method to access the {@link #getItem()} List as a Collection.
     * @return Collection of repeating fields.
     */
    public Collection<AttributeField> getItemCollection() {
        return item;
    }
    
    /**
     * @see #getItemCollection()
     * @param item Collection of repeating fields.
     */
    public void setItemCollection(Collection<AttributeField> item) {
        this.item=new OrderedLinkedList<AttributeField>(item);
    }
    
    /**
     * Determine if the user is allow to add or remove elements from the data.
     * @return True if data can be added or removed, false otherwise.
     */
    public boolean isAddAndDeleteEnabled() {
        return addAndDeleteEnabled;
    }

    /**
     * Enable or disable permission to add and remove data from the table.
     * @param addAndDeleteEnabled
     */
    public void setAddAndDeleteEnabled(boolean addAndDeleteEnabled) {
        this.addAndDeleteEnabled = addAndDeleteEnabled;
    }

    @SuppressWarnings("unchecked")
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        if (isAddAndDeleteEnabled()) {
            Properties opParams=Functions.getOperationParameters(request);
            String op=opParams.getProperty("op");
            String opId=opParams.getProperty("id");

            if ("add".equals(op) && (id==null || id.equals(opId))) {
                // Create the object we'll be adding.
                CoreProxy cp=new CoreProxy();
                Type t=cp.newProductType(((Quotation)model).getProductTypeId(), type);
                
                // Build the xpath that'll get us the collection we need to add to.
                // If the binding has a selector on it e.g. /asset[type='vehicle'] the selector will be
                // chopped off.
                String xpath;
                if (getBinding().indexOf('[')>=0) {
                    xpath=getBinding().substring(0, getBinding().lastIndexOf('['));
                }
                else {
                    xpath=getBinding();
                }
    
                // get the collection and add the new object to it.
                Collection c=(Collection)model.xpathGet(xpath);
                c.add(t);
            }
            else if ("delete".equals(op) && (id==null || id.equals(opId))) {
                // The 'row' param indicates the row number who's trash can has been clicked.
                int row=Integer.parseInt(opParams.getProperty("row"));
    
                // Loop through the collection the scroller is bound to and find the object to be removed.
                Iterator it=model.xpathIterate(getBinding());
                for( ; row>0 ; row--, it.next() );
                Object obj=it.next();
                
                // Build the xpath that'll get us the collection we need to add to.
                // If the binding has a selector on it e.g. /asset[type='vehicle'] the selector will be
                // chopped off.
                String xpath;
                if (getBinding().indexOf('[')>=0) {
                    xpath=getBinding().substring(0, getBinding().lastIndexOf('['));
                }
                else {
                    xpath=getBinding();
                }
    
                // get the collection and add the new object to it.
                Collection c=(Collection)model.xpathGet(xpath);
                c.remove(obj);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    @Override
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        int rowCount=0;

        // Loop through the rows
        for(Iterator it=model.xpathIterate(getBinding()) ; it.hasNext() ; ) {
            Type subModel=(Type)it.next();

            // loop through the columns
            for(AttributeField a: item) {
                a.applyRequestValues(request, response, subModel, getBinding()+"["+rowCount+"]");
            }

            rowCount++;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        boolean error=false;
        int rowCount=0;

        // Loop through the rows
        for(Iterator<Type> it=model.xpathIterate(getBinding()) ; it.hasNext() ; ) {
            Type subModel=it.next();

            // loop through the columns
            for(AttributeField a: item) {
                error|=a.processValidations(request, response, subModel);
            }

            rowCount++;
        }

        return error;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        Iterator<Type> it=model.xpathIterate(getBinding());
        
        if (it.hasNext()) {
            Type t=it.next();
            
            for(AttributeField a: item) {
                a.renderPageLevelResponse(request, response, t, getBinding()+"[0]");
            }
        }
    }
}

