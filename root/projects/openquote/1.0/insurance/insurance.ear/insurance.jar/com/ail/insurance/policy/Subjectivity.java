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
import java.util.Calendar;
import java.util.Date;

/**
 * Subjectivity Assessment line. A subjectivity represents a condition that a policy holder must
 * meet before cover will be offered. A typical example in property insurance would be "Subject 
 * to a satisfactory surveyors report".
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2006/11/04 11:33:14 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Subjectivity.java,v $
 * @stereotype type
 */
public class Subjectivity extends Marker {
    private static final long serialVersionUID = 6414533098371760797L;

    private Date createdDate=null;
    private Date expiryDate=null;
    private Date reminderDate=null;
    private boolean manuscript=false;
    private String text=null;
 
    /**
     * Default constructor
     */
    public Subjectivity() {
    }

    /**
     * Constructor. The following defaults are applied:<ul>
     * <li>the {@link #setCreatedDate(Date) created date} is set to today;</li>
     * <li>the {@link #setExpiryDate(Date) expiryDate} is set to today+60 days;</li>
     * <li>the {@link #setReminderDate(Date) reminder date} is set to today+30 days;</li>
     * <li>and the {@link #isManuscript() manuscript} indicator is set to false.</li></ul>
     * @param id The Id for this line.
     * @param reason Free text reason for this subjectivity having been added (e.g. "Building value more than &pound;250,000").
     * @param text The text of the subjectivity (e.g. "Subject to a satisfactory surveyors report.").
     * @param relatedTo Reference to the policy object that caused the marker.
     */
    public Subjectivity(String id, String reason, String text, Reference relatedTo) {
        super(id, reason, relatedTo, MarkerType.SUBJECTIVITY);

        setText(text);
        
        // set creation date to today
        createdDate=new Date();

        Calendar cal=Calendar.getInstance();

        cal.add(Calendar.DATE, 30);
        reminderDate=cal.getTime();
        
        cal.add(Calendar.DATE, 30);
        expiryDate=cal.getTime();
    }

    /**
     * Constructor. The following defaults are applied:<ul>
     * <li>the {@link #setCreatedDate(Date) created date} is set to today;</li>
     * <li>the {@link #setExpiryDate(Date) expiryDate} is set to today+60 days;</li>
     * <li>the {@link #setReminderDate(Date) reminder date} is set to today+30 days;</li>
     * <li>and the {@link #isManuscript() manuscript} indicator is set to false.</li></ul>
     * @param id The Id for this line.
     * @param reason Free text reason for this subjectivity having been added (e.g. "Building value more than &pound;250,000").
     * @param text The text of the subjectivity (e.g. "Subject to a satisfactory surveyors report.").
     * @param relatedTo Reference to the policy object that caused the marker.
     * @param priority The priority of this line (lines with higher priority values are processed first)
     */
    public Subjectivity(String id, String reason, String text, Reference relatedTo, int priority) {
        this(id, reason, text, relatedTo);
        setPriority(priority);
    }
    
    /**
     * Constructor. The following defaults are applied:<ul>
     * <li>the {@link #setCreatedDate(Date) created date} is set to today;</li>
     * <li>the {@link #setExpiryDate(Date) expiryDate} is set to today+60 days;</li>
     * <li>the {@link #setReminderDate(Date) reminder date} is set to today+30 days;</li>
     * <li>and the {@link #isManuscript() manuscript} indicator is set to false.</li></ul>
     * @param id The Id for this line.
     * @param reason Free text reason for this subjectivity having been added (e.g. "Building value more than &pound;250,000").
     * @param text The text of the subjectivity (e.g. "Subject to a satisfactory surveyors report.").
     * @param isManuscript True if this is a manuscript subjectivity, or false if it is standard.
     * @param relatedTo Reference to the policy object that caused the marker.
     * @param priority The priority of this line (lines with higher priority values are processed first)
     */
    public Subjectivity(String id, String reason, String text, boolean isManuscript, Reference relatedTo, int priority) {
        this(id, reason, text, relatedTo);
        setManuscript(isManuscript);
        setPriority(priority);
    }

    /**
     * @return Returns the createdDate.
     */
    public Date getCreatedDate() {
        return createdDate;
    }

    public String getCreatedDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(createdDate);
    }
    
    /**
     * @param createdDate The createdDate to set.
     */
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
    
    public void setCreatedDateAsString(String createdDate) throws ParseException {
        this.createdDate=DateFormat.getDateInstance(DateFormat.SHORT).parse(createdDate);
    }
    
    /**
     * @return Returns the expiryDate.
     */
    public Date getExpiryDate() {
        return expiryDate;
    }
    
    public String getExpiryDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(expiryDate);
    }
  
    /**
     * @param expiryDate The expiryDate to set.
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    public void setExpiryDateAsString(String expiryDate) throws ParseException {
        this.expiryDate=DateFormat.getDateInstance(DateFormat.SHORT).parse(expiryDate);
    }
  
    /**
     * @return Returns the reminderDate.
     */
    public Date getReminderDate() {
        return reminderDate;
    }
    
    /**
     * @param reminderDate The reminderDate to set.
     */
    public void setReminderDate(Date reminderDate) {
        this.reminderDate = reminderDate;
    }

    /**
     * Returns true if the text property represents an "manuscript" subjectivity (e.i. manually defined), or
     * a standard one. 
     * @return Returns the manuscript.
     */
    public boolean isManuscript()
    {
      return manuscript;
    }

    /**
     * @see #isManuscript()
     * @param manuscript The manuscript to set.
     */
    public void setManuscript(boolean manuscript)
    {
      this.manuscript = manuscript;
    }

    /**
     * Return the text of the subjectivity.
     * @return Returns the text.
     */
    public String getText()
    {
      return text;
    }

    /**
     * @see #setText(String)
     * @param text The text to set.
     */
    public void setText(String text)
    {
      this.text = text;
    }
}
