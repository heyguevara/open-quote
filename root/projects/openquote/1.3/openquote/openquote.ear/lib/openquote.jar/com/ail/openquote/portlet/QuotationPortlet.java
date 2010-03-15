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
package com.ail.openquote.portlet;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.GenericPortlet;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.ExceptionRecord;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.QuotationCommon;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * This Portlet acts as the controller (in MVC terms) for the quotation process. On initially being called, it
 * inspects the value of the 'product' init parameter to discover the name of the product it has been 
 * instantiated to quote for. The product name is used to create an instance of a quotation object (the Model)
 * - which is placed in the session, and an instance of the PageFlow (View). As the PageFlow holds no state, it 
 * can be instantiated by the core each time it is needed without significant overhead (the assumption being that
 * the PageFlow type is marked 'singleInstance' in the product's configuration.
 */
public class QuotationPortlet extends GenericPortlet {
    
    public void processAction(ActionRequest request, ActionResponse response) {
    	  
    	QuotationContext.initialise(request);
   	
    	try {
    		QuotationCommon.processAction(request, response);
        }
        catch(Throwable t) {
        	Quotation quotation = QuotationContext.getQuotation();
        	
            if (quotation==null) {
            	t.printStackTrace();
            }
            else {
            	quotation.addException(new ExceptionRecord(t));
            }
            
            // TODO Forward to an error page
        }
    }

    public void doView(RenderRequest request, RenderResponse response) throws IOException {

    	QuotationContext.initialise(request);

    	try {
    		QuotationCommon.doView(request, response);
        }
        catch(Throwable t) {
        	Quotation quotation = QuotationContext.getQuotation();
        	
            if (quotation==null) {
            	t.printStackTrace();
            }
            else {
            	quotation.addException(new ExceptionRecord(t));
            }

            // TODO Forward to an error page
        }
    }    
}
