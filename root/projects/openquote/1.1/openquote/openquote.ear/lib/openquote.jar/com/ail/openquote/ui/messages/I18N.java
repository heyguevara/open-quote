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
package com.ail.openquote.ui.messages;

import java.util.ResourceBundle;

import com.ail.openquote.ui.util.QuotationContext;

public class I18N {
	public static String i18n(String message) {
		if (message!=null) {
	    	try {
	    		return ResourceBundle.getBundle("com.ail.openquote.ui.messages.quotation", QuotationContext.getRequest().getLocale()).getString(message);
	    	}
	    	catch(Throwable e) {
	    		// ignore this - let the default return handle it
	    	}
		}
		return message;
	}
}
