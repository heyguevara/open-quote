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

package com.ail.party;

import com.ail.annotation.TypeDefinition;
import com.ail.util.DateOfBirth;
import static com.ail.party.Title.OTHER;

@TypeDefinition
public class Person extends Party {
    static final long serialVersionUID = 2593580726988142332L;

    /** Person's date of birth */
    private DateOfBirth dateOfBirth;

    /** The person's first name */
    private String firstName;

    /** The person's surname */
    private String surname;

    /** Text of title if not defined by Title property (i.e. title property==Other) */
    private String otherTitle;

    /** The person's title */
    private Title title;

    /**
     * Getter returning the value of the dateOfBirth property. Person's date of birth
     * @return Value of the dateOfBirth property
     */
    public DateOfBirth getDateOfBirth() {
        return dateOfBirth;
    }

    /**
     * Setter to update the value of the dateOfBirth property. Person's date of birth
     * @param dateOfBirth New value for the dateOfBirth property
     */
    public void setDateOfBirth(DateOfBirth dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    /**
     * Getter returning the value of the firstName property. The person's first name
     * @return Value of the firstName property
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Setter to update the value of the firstName property. The person's first name
     * @param firstName New value for the firstName property
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Getter returning the value of the surname property. The person's surname
     * @return Value of the surname property
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Setter to update the value of the surname property. The person's surname
     * @param surname New value for the surname property
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Getter returning the value of the otherTitle property. Text of title if not defined by Title property
     * (i.e. title property==Other)
     * @return Value of the otherTitle property
     */
    public String getOtherTitle() {
        return otherTitle;
    }

    /**
     * Setter to update the value of the otherTitle property. Text of title if not defined by Title property
     * (i.e. title property==Other)
     * @param otherTitle New value for the otherTitle property
     */
    public void setOtherTitle(String otherTitle) {
        this.otherTitle = otherTitle;
    }

    /**
     * Getter returning the value of the title property. The person's title
     * @return Value of the title property
     */
    public Title getTitle() {
        return title;
    }

    /**
     * Setter to update the value of the title property. The person's title
     * @param title New value for the title property
     */
    public void setTitle(Title title) {
        this.title = title;
    }

     /**
      * Set the title property as a String. The String must represents a valid
      * com.ail.party.Title. i.e. it must be suitable for a call to
      * com.ail.party.Title.forName().
      * @param title New value for property.
      * @throws IndexOutOfBoundsException If title is not a valid com.ail.party.Title.
      */
     public void setTitleAsString(String title) throws IndexOutOfBoundsException{
        this.title=Title.valueOf(title);
    }

    /**
     * Get the value of the title property as a string (as opposed to an instance of
     * com.ail.party.Title).
     * @return String representation of the title, or null if the property has not been set.
     */
    public String getTitleAsString() {
        if (title!=null) {
            return title.name();
        }
        return null;
    }

    /**
     * "actual title" is either the value of the <i>title</i> property, or the value of <i>otherTitle</i>
     * if <i>title</i>'s value is 'OTHER'.
     * @return actual title.
     */
    public String getActualTitle() {
        if (title!=null) {
            return (OTHER.equals(title) ? otherTitle : title.getLongName());
        }
        else {
            return "";
        }
    }

    /**
     * Return the person's legal name. This takes the form "&lt;title&gt &lt;firstName&gt; &lt;surname&gt;"
     */
    public String getLegalName() {
        return getActualTitle() + " " + firstName + " " + surname;
    }
}
