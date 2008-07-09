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

import com.ail.core.Type;

/**
 * @version $Revision: 1.3 $
 * @state $State: Exp $
 * @date $Date: 2007/02/12 23:10:04 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/AssessmentLine.java,v $
 * @stereotype type
 */
public class AssessmentLine extends Type implements Comparable<AssessmentLine> {
    private static final long serialVersionUID = 1357488757251866318L;
    private String id=null;
    private String reason=null;
    private boolean disabled=false;
    private int priority=0;
    private int calculatedOrder=0;

    /**
     * @link aggregationByValue 
     */
    private Reference relatesTo;

    /** Origin describes the actor (system or user) who created this AssessmenLine. */
    private String origin;

    /**
     * Default constructor
     */
    public AssessmentLine() {
    }

    /**
     * Constructor. A default priority of 0 is assumed.
     * @param id Value for Id property.
     * @param reason
     * @param relatesTo
     */
    public AssessmentLine(String id, String reason, Reference relatesTo) {
        this.id=id;
        this.reason=reason;
        this.relatesTo=relatesTo;
    }

    /**
     * Constructor
     * @param id Value for Id property.
     * @param reason The reason for creating this line.
     * @param relatesTo The id of another line that this one relates to.
     * @param priority The priority of this line wrt other lines in the same assessment sheet.
     */
    public AssessmentLine(String id, String reason, Reference relatesTo, int priority) {
        this.id=id;
        this.reason=reason;
        this.relatesTo=relatesTo;
        this.priority=priority;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Reference getRelatesTo(){ 
      return relatesTo; 
    }

    public void setRelatesTo(Reference relatesTo){ 
      this.relatesTo = relatesTo; 
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * Getter returning the value of the origin property. Origin describes the actor (system or user) who
     * created this AssessmenLine.
     * @return Value of the origin property
     */
    public String getOrigin() {
        return origin;
    }

    /**
     * Setter to update the value of the origin property. Origin describes the actor (system or user) who
     * created this AssessmenLine.
     * @param origin New value for the origin property
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * Determine if this line is disabled. By default all lines are enabled when created, and as such are 
     * included in the calculations and assessments carried out by services, but individual lines may 
     * be disabled. When a line is disabled it is simple ignored by all services that operate on 
     * assessment sheets.
     * @return Returns the disabled.
     */
    public boolean isDisabled()
    {
      return disabled;
    }

    /**
     * @see #isDisabled()
     * @param disabled The disabled to set.
     */
    public void setDisabled(boolean disabled)
    {
      this.disabled = disabled;
    }

    /**
     * The priority of a line affects the order in which it is processed (with respect to other lines) by services. 
     * Some services (for example {@link com.ail.insurance.quotation.refreshassessmentsheet.RefreshAssessmentSheetService RefreshAssessmentSheetService})
     * process all the lines in an assessment sheet. Such services use the priority of the lines to determine the order in 
     * which the lines should be processed.<p>
     * The priority may be any integer value, higher values are processed before lower.
     * @return Returns the priority.
     */
    public int getPriority()
    {
      return priority;
    }
    
    /**
     * @see #getPriority()
     * @param priority The priority to set.
     */
    public void setPriority(int priority)
    {
      this.priority = priority;
    }

    /* Order lines based on each lines priority.
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(AssessmentLine that)
    {
      return  that.priority - this.priority;
    }

    public int getCalculatedOrder() {
        return calculatedOrder;
    }

    public void setCalculatedOrder(int calculatedOrder) {
        this.calculatedOrder = calculatedOrder;
    }
}
