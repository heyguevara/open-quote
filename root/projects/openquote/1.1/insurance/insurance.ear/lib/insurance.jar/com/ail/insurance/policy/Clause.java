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

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;

import com.ail.core.Type;

/**
 * A clause indicates some condition associated with the element to which it is attached. Clauses may be attached (typically to a {@link Policy} or a {@link Section})
 * and describe some special condition which has been applied above and beyond whatever conditions are defined by the associated {@link Wording}. Clauses may also
 * indicate a change to cover which is applied within the lifetime of the element it is attached to (i.e. a Mid Term Adjustment Endorsement); for example, a change of 
 * address endorsement. 
 */
public class Clause extends Type {
    private static final long serialVersionUID = 6414533098371760797L;

    /** The type of the clause indicates how it should be interpreted, and frequently how it should be grouped with other clauses */
    private ClauseType type;

    /** Date at which the clause was added to the policy/section */
    private Date createdDate = null;

    /** Date at which the clause comes into effect */
    private Date startDate = null;

    /** Date at which the clause's effect ends */
    private Date endDate = null;

    /**
     * Date at which a reminder should be sent. This is most relevant to subjectivities but may be used on other types of clause
     * too.
     */
    private Date reminderDate = null;

    /**
     * True if the text of the clause is specific to this policy - i.e. is not a standard clause; and appears in the
     * <code>text</code> property.
     */
    private boolean manuscript = false;

    /** The title of the clause recognizable to business users. */
    private String title = null;

    /** The reference number of the the clause; a unique reference for contract certainty. */
    private String reference = null;

    /** The text of the clause if it is a manuscript clause. Null otherwise. */
    private String text = null;

    /**
     * Default constructor
     */
    public Clause() {
    }

    /**
     * The clause type dictates how the clause should be interpreted.
     * 
     * @return the type
     */
    public ClauseType getType() {
        return type;
    }

    /**
     * @see #getType()
     * @param type the type to set
     */
    public void setType(ClauseType type) {
        this.type = type;
    }

    /**
     * Set the clause types as a String. The String must represents a valid clause type. i.e. it must be suitable for a call to
     * ClauseType.forName().
     * 
     * @see ClauseType#forName
     * @param type New clause type
     * @throws IndexOutOfBoundsException If clause type is not valid
     */
    public void setTypeAsString(String type) {
        this.type = ClauseType.valueOf(type);
    }

    /**
     * Get the clause type as a string (as opposed to an instance of ClauseType).
     * 
     * @return String representation of the clause type, or null if the clause type has not been set.
     * @return
     */
    public String getTypeAsString() {
        if (type != null) {
            return type.name();
        }
        return null;
    }

    /**
     * Date at which the clause comes into effect
     * 
     * @return the startDate
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Get the start date in Format.SHORT string format.
     * @return formatted date
     */
    public String getStartDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(startDate);
    }

    /**
     * @see #getStartDate()
     * @param startDate the startDate to set
     */
    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    /**
     * Setter to update the value of the startDate property from a String. The supplied date must be in
     * DateFormat.SHORT format.
     * 
     * @param startDate
     * @throws ParseException
     */
    public void setStartDateAsString(String startDate) throws ParseException {
        this.startDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(startDate);
    }

    /**
     * Date at which the clause's effect ends
     * 
     * @return the endDate
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Get the end date in Format.SHORT string format.
     * @return formatted date
     */
    public String getEndDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(endDate);
    }

    /**
     * @see #getEndDate()
     * @param endDate the endDate to set
     */
    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    /**
     * Setter to update the value of the endDate property from a String. The supplied date must be in
     * DateFormat.SHORT format.
     * @see #getEndDate()
     * @param endDate
     * @throws ParseException
     */
    public void setEndDateAsString(String endDate) throws ParseException {
        this.endDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(endDate);
    }

    /**
     * The title of the clause recognizable to business users.
     * 
     * @return the title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @see #getTitle()
     * @param title the title to set
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * The reference number of the the clause; a unique reference for contract certainty.
     * 
     * @return the reference
     */
    public String getReference() {
        return reference;
    }

    /**
     * @see #getReference()
     * @param reference the reference to set
     */
    public void setReference(String reference) {
        this.reference = reference;
    }

    /**
     * Date at which the clause was added to the policy/section
     * 
     * @return Returns the createdDate.
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    /**
     * Get the creation date as a String in DateFormat.SHORT format
     * @see #getCreatedDate()
     * @return formatted date
     */
    public String getCreatedDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(createdDate);
    }

    /**
     * @see #getCreatedDate()
     * @param createdDate The createdDate to set.
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Set create data from a string in DateFormat.SHORT format.
     * @param createdDate
     * @throws ParseException
     */
    public void setCreatedDateAsString(String createdDate) throws ParseException {
        this.createdDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(createdDate);
    }

    /**
     * Date at which a reminder should be sent. This is most relevant to subjectivities but may be used on other types of clause
     * too.
     * 
     * @return Returns the reminderDate.
     */
    public Date getReminderDate() {
        return reminderDate;
    }

    /**
     * Get the reminder date as a String in DateFormat.SHORT format
     * @see #getReminderDate()
     * @return formatted date
     */
    public String getReminderDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(reminderDate);
    }

    /**
     * @see #getReminderDate()
     * @param reminderDate The reminderDate to set.
     */
    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    /**
     * Set reminder data from a string in DateFormat.SHORT format.
     * @param reminderDate
     * @throws ParseException
     */
    public void setReminderDateAsString(String reminderDate) throws ParseException {
        this.createdDate = DateFormat.getDateInstance(DateFormat.SHORT).parse(reminderDate);
    }

    /**
     * True if the text of the clause is specific to this policy - i.e. is not a standard clause; and appears in the
     * <code>text</code> property.
     * 
     * @return Returns the manuscript.
     */
    public boolean isManuscript() {
        return manuscript;
    }

    /**
     * @see #isManuscript()
     * @param manuscript The manuscript to set.
     */
    public void setManuscript(boolean manuscript) {
        this.manuscript = manuscript;
    }

    /**
     * The text of the clause if it is a manuscript clause. Null otherwise.
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
}
