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

/**
 * An assessment line indicating that a 'Marker' has been resolved. Also know as an override. These are attached to 
 * markers (referrals and declines) raised by the system to indicate that they should be ignored when the assessment
 * sheet is processed. Typically the system indicates applies a marker to indicate that the policy should be referred,
 * but an underwriter "resolves" the marker to indicate that he/she is happy for the referral to be ignored. 
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/19 20:20:58 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/MarkerResolution.java,v $
 * @stereotype type
 */
public class MarkerResolution extends AssessmentLine {
    private static final long serialVersionUID = -5354541886808104720L;

    private String behaviourId;

    /**
     * Default constructor
     */
    public MarkerResolution() {
    }

    /**
     * Constructor
     * @param id The Id for this line.
     * @param reason Free text reson for the marker
     * @param relatedTo Reference to the policy object that caused the marker.
     * @param behaviourId The behaviour created as part of this resolution
     */
    public MarkerResolution(String id, String reason, Reference relatedTo, String behaviourId) {
        super(id, reason, relatedTo);
        this.behaviourId=behaviourId;
    }

    /**
     * Constructor
     * @param id The Id for this line.
     * @param reason Free text reson for the marker
     * @param relatedTo Reference to the policy object that caused the marker.
     * @param behaviourId The behaviour created as part of this resolution
     * @param priority The priority of this line wrt other lines in the same sheet (lines with higher priority values are processed first)
     */
    public MarkerResolution(String id, String reason, Reference relatedTo, String behaviourId, int priority) {
      super(id, reason, relatedTo, priority);
      this.behaviourId=behaviourId;
    }

    public String getBehaviourId() {
        return behaviourId;
    }

    public void setBehaviourId(String behaviourId) {
        this.behaviourId = behaviourId;
    }
}
