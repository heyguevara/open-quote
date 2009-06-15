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

package com.ail.openquote.ui;

import com.ail.core.Type;

/**
 * Help text is associated with a PageElement and allows help text to be configured for use in screen display. 
 */
public class HelpText extends Type {
    private static final long serialVersionUID = 6414533098371760797L;

    /** The text of the hint  */
    private String text = null;

    /** The error type (error, invalid or null) indicating when hint should be displayed */
    private String error = null;

    /**
     * Default constructor
     */
    public HelpText() {
    }

    /**
     * Create help text specifying all fields.
     * @param text Help Text (may include markup). May be null.
     * @param error type (error, invalid or null) indicating when the help text should be displayed - null means always display
     */
    public HelpText(String text, String error) {
        super();
        this.text = text;
        this.error = error;
    }
    
    /**
     * The text of the help if it exists. Null otherwise.
     * 
     * @return Returns the text.
     */
    public String getText() {
        return text;
    }

    /**
     * @see #setText(String)
     * @param text The text to set.
     */
    public void setText(String text) {
        this.text = text;
    }

    /**
     * The error type of the help text if it exists. Null otherwise.
     * 
     * @return Returns the error.
     */
    public String getError() {
        return error;
    }

    /**
     * @see #setError(String)
     * @param error The error to set.
     */
    public void setError(String error) {
        this.error = error;
    }

}
