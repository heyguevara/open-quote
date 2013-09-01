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

package com.ail.insurance.policy;

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;

/**
 * Represents an individual wording either by id or by text.
 */
@TypeDefinition
public class Wording extends Type {
    private static final long serialVersionUID = 2533714090687003463L;

    /**
     * The ID of the wording. This identifies the type of wording being used, and is used by other services to generate the
     * correct text for the wording.
     */
    private String id;

    /** The text of the wording. This may be null if the wording text has not been generated. */
    private String wordingText;

    /**
     * Getter returning the value of the id property. The ID of the wording. This identifies the type of wording being used,
     * and is used by other services to generate the correct text for the wording.
     * @return Value of the id property
     */
    public String getId() {
        return id;
    }

    /**
     * Setter to update the value of the id property. The ID of the wording. This identifies the type of wording being used,
     * and is used by other services to generate the correct text for the wording.
     * @param id New value for the id property
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Getter returning the value of the id property wrapped in CDATA tags. The ID of the wording. This identifies the type of
     * wording being used, and is used by other services to generate the correct text for the wording.
     * @return Value of the id property
     */
    public String getIdCDATA() {
        return "<![CDATA[" + id + "]]>";
    }

    /**
     * Getter returning the value of the wordingText property. The text of the wording. This may be null if the wording text
     * has not been generated.
     * @return Value of the wordingText property
     */
    public String getWordingText() {
        return wordingText;
    }

    /**
     * Setter to update the value of the wordingText property. The text of the wording. This may be null if the wording text
     * has not been generated.
     * @param wordingText New value for the wordingText property
     */
    public void setWordingText(String wordingText) {
        this.wordingText = wordingText;
    }

    /**
     * Getter returning the value of the wordingText property wrapped in CDATA tags. The text of the wording. This may be null
     * if the wording text has not been generated.
     * @return Value of the wordingText property
     */
    public String getWordingTextCDATA() {
        return "<![CDATA[" + wordingText + "]]>";
    }
}
