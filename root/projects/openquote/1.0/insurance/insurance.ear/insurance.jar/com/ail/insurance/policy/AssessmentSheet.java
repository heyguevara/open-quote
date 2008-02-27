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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Map;

import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;
import com.ail.util.Rate;

/**
 * Groups together a collection of assessment lines and provides utility methods for manipulating them.
 * @version $Revision: 1.11 $
 * @state $State: Exp $
 * @date $Date: 2007/03/27 23:00:27 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/AssessmentSheet.java,v $
 * @stereotype type
 */
public class AssessmentSheet extends Type {
    private static final long serialVersionUID = 4050081334317365104L;

    /**
     * @link aggregationByValue
     * @clientCardinality 1
     * @supplierCardinality 0..*
     */
    /*# AssessmentLine lnkAssessmentLine; */
    private Map<String,AssessmentLine> assessmentLine = new Hashtable<String,AssessmentLine>();

    /**
     * An assessment sheet must be locked to an actor before entries can be made. This allows the sheet to know who is making
     * entries and to associate the entries with that actor.
     */
    private transient String lockingActor=null;

    /**
     * Holds the next priority level to be used by the auto priority generator.
     */
    private int autoPriority=1;
    
    /**
     * Default constructor
     */
    public AssessmentSheet() {
    }

    /**
     * Get a hashtable of all the lines in the sheet of a specific type.
     * @param clazz The type to search for.
     * @return Hashtable of lines keyed on Id, this may be an empty table.
     */
    @SuppressWarnings("unchecked")
    public <T> Hashtable<String,T> getLinesOfType(Class<T> clazz) {
        Hashtable<String,T> ret=new Hashtable<String,T>();
        
        for(AssessmentLine asl: assessmentLine.values()) {
            if (!asl.isDisabled() && clazz.isAssignableFrom(asl.getClass())) {
                ret.put(asl.getId(), (T)asl);
            }
        }
        
        return ret;
    }
    
    /**
     * Fetch the collection of line objects associated with this instance. The Collection returned includes
     * {@link AssessmentLine#isDisabled() disabled} lines. Use {@link #getEnabledLine getEnabledLine} to fetch
     * only those that are enabled. 
     * @return A collection of instances of AssessmentLine.
     */
    public Collection<AssessmentLine> getLine() {
        return assessmentLine.values();
    }
    
    /**
     * @deprecated use {@link #getAssessmentLine()} instead.
     * @return
     */
    public Map<String,AssessmentLine> getAssessmentList() {
        return assessmentLine;
    }

    /**
     * @deprecated use {@link #setAssessmentLine(Map)} instead.
     * @param assessmentLine
     */
    public void setAssessmentList(Map<String,AssessmentLine> assessmentLine) {
        this.assessmentLine=assessmentLine;
    }
    
    /**
     * Get a list of the assessment lines associated with this sheet as a Map keyed on the line IDs.
     * @return Lines associated with this sheet.
     */
    public Map<String,AssessmentLine> getAssessmentLine() {
        return assessmentLine;
    }

    /**
     * Set the lines associated with this sheet to the values defined in a Map.
     * @param assessmentLine New values.
     */
    public void setAssessmentLine(Map<String,AssessmentLine> assessmentLine) {
        this.assessmentLine=assessmentLine;
    }
    
    /**
     * Fetch a collection of all the line objects associated with this instance. The Collection returned excludes
     * {@link AssessmentLine#isDisabled() disabled} lines. Use {@link #getEnabledLine getLine} to fetch
     * all line. 
     * @return A collection of instances of AssessmentLine. The list may be empty, but it will never be null.
     */
    public Collection<AssessmentLine> getEnabledLine() {
      Collection<AssessmentLine> ret=new ArrayList<AssessmentLine>();
      
      for(AssessmentLine l: assessmentLine.values()) {
        if (!l.isDisabled()) {
          ret.add(l);
        }
      }

      return ret;
    }
    
    /**
     * Fetch a collection of the {@link AssessmentLine#isDisabled() disabled} line objects associated with this instance. 
     * @return A collection of instances of AssessmentLine. The list may be empty, but it will never be null.
     */
    public Collection<AssessmentLine> getDisabledLine() {
      Collection<AssessmentLine> ret=new ArrayList<AssessmentLine>();
      
      for(AssessmentLine l: assessmentLine.values()) {
        if (l.isDisabled()) {
          ret.add(l);
        }
      }

      return ret;
    }
    
    /**
     * Set the collection of AssessmentLines associated with this sheet.
     * @param line Collection of AssessmentLines.
     */
    public void setLine(Collection<AssessmentLine> line) {
        this.assessmentLine=new Hashtable<String,AssessmentLine>();

        for(AssessmentLine l: line) {
            this.assessmentLine.put(l.getId(), l);
        }
    }

    /**
     * Fetch a count of the number of line objects associated with this instance.
     * @return Count of line objects. The count includes disabled lines, use {@link #getEnabledLineCount getEnabledLineCount}
     * to get a count of the enabled lines only.
     */
    public int getLineCount() {
        return assessmentLine.size();
    }

    /**
     * Fetch a count of the number of line objects associated with this instance. The count excludes disabled lines, use {@link #getLineCount getLineCount}
     * to get a count of all lines.
     * @return Count of line objects.
     */
    public int getEnabledLineCount() {
      int count=0;
      
      for(AssessmentLine asl: assessmentLine.values()) {
        if (!asl.isDisabled()) {
            count++;
        }
      }
      
      return count;
    }

    /**
     * Fetch a count of the number of disabled line objects associated with this instance.
     * @return Count of line objects.
     */
    public int getDisabledLineCount() {
      int count=0;
      
      for(AssessmentLine asl: assessmentLine.values()) {
        if (asl.isDisabled()) {
            count++;
        }
      }
      
      return count;
    }

    /**
     * Add a line to the collection associated with this object. If the line being added has an origin (creating actor)
     * of null, then this sheet must be locked to an actor (lockingActor) and that actor will be used as the origin.
     * @param line Instance of AssessmentLine to add.
     * @throws IllegalStateException If line.origin is null and this sheet is not locked to an actor.
     * @throws DuplicateAssessmentLineError If a line with the same id as <code>line</code> is already in the sheet
     */
    public void addLine(AssessmentLine line) {
        if (line.getOrigin()==null) {
            if (lockingActor==null) {
                throw new IllegalStateException("Atempt to add a line with null origin to an unlocked AssessmentSheet");
            }
            line.setOrigin(lockingActor);
        }

        if (this.assessmentLine.containsKey(line.getId())) {
            throw new DuplicateAssessmentLineError(line.getId());
        }

        if (line.getPriority()==0) {
            line.setPriority(generateAutoPriority());
        }
        
        this.assessmentLine.put(line.getId(), line);
    }

    /**
     * Remove a specific instance of AssessmentLine from the collection associated with this object.
     * @param line Object to be removed from the collection.
     */
    public void removeLine(AssessmentLine line) {
        this.assessmentLine.remove(line.getId());
    }

    /**
     * Disable the specified line. If the line is already disabled this has no effect. 
     * @throws IllegalStateException If the specified line does not exist.
     * @param id The id of the line to disable.
     */
    public void disableLine(String id) {
      try {
        AssessmentLine l=(AssessmentLine)assessmentLine.get(id);
        l.setDisabled(true);
      } catch(NullPointerException e) {
        throw new IllegalStateException("Line (id='"+id+"') does not exist");
      }
    }
    
    /**
     * Disable the specified line. If the line is already disabled this has no effect. 
     * @param line The line to disable.
     */
    public void disableLine(AssessmentLine line) {
      line.setDisabled(true);
    }
    
    /**
     * Enable the specified line. If the line is already enabled this has no effect. 
     * @throws IllegalStateException If the specified line does not exist.
     * @param id The id of the line to enabled.
     */
    public void enableLine(String id) {
      try {
        AssessmentLine l=(AssessmentLine)assessmentLine.get(id);
        l.setDisabled(false);
      } catch(NullPointerException e) {
        throw new IllegalStateException("Line (id='"+id+"') does not exist");
      }
    }
    
    /**
     * Enable the specified line. If the line is already enabled this has no effect. 
     * @param line The line to enable.
     */
    public void enableLine(AssessmentLine line) {
      line.setDisabled(false);
    }
    
    /**
     * Remove the lines "owned" (or created) by an actor. Each assessment line has an origin(ating) actor - the name
     * of the actor who created the line. That actor may remove the lines later using this method. Lines will be removed
     * even if they are disabled.
     * @param origin Name of the origin to remove lines for
     */
    public void removeLinesByOrigin(String origin) {
        ArrayList<String> delete=new ArrayList<String>(); // list of the id's of lines to delete
        
        // This method may look odd - why not simply iterate through the 'line' hashtable's
        // values deleting the appropriate ones as we go? Well, before JDK1.5 that worked, but
        // since then it throws a 'ConcurrentModificationException' because you can't delete
        // objects from a collection that you're iterating across.
        
        // check for lines that can be deleted
        for(AssessmentLine l: assessmentLine.values()) {
            if (origin.equals(l.getOrigin())) {
                delete.add(l.getId());
            }
        }

        // remove each of the lines
        for(String id: delete) {
            assessmentLine.remove(id);
        }
    }

    /**
     * Fetch a named entry from the sheet. This method will <u>not</u> return 
     * {@link AssessmentLine#isDisabled() disabled} lines.
     * @param id The id of the entry to return
     * @return Instance of line for id, or null if id is not known.
     */
    public AssessmentLine findLineById(String id) {
      AssessmentLine l=(AssessmentLine)assessmentLine.get(id);

      if (l!=null && l.isDisabled()) {
        return null;
      }
      else {
        return l;
      }
    }

    /**
     * Find and return the resolution for a marker. {@link AssessmentLine#isDisabled() Disabled} lines are 
     * ignored by this method.
     * @param id Marker's id
     * @return resolution, or null if the marker is not resolved.
     */
    public MarkerResolution findResolutionByMarkerId(String id) {
        Hashtable<String,MarkerResolution> resolutions=getLinesOfType(MarkerResolution.class);
        MarkerResolution markerRes=null;

        for(Enumeration<MarkerResolution> en=resolutions.elements() ; en.hasMoreElements() ; ) {
            markerRes=en.nextElement();
            if (markerRes.getRelatesTo()!=null && markerRes.getRelatesTo().getId().equals(id)) {
                return markerRes;
            }
        }

        return null;
    }

    /**
     * Determine if the sheet is marked for decline or not. The
     * sheet is marked for decline if any Markers of type decline
     * are found, and are not resolved. {@link AssessmentLine#isDisabled() Disabled}
     * lines are ignored by this method.
     * @return true if the sheet is marked for decline, false otherwise.
     */
    public boolean isMarkedForDecline() {
        Hashtable<String,Marker> markers=getLinesOfType(Marker.class);
        Marker marker=null;

        for(Enumeration<Marker> en=markers.elements() ; en.hasMoreElements() ; ) {
            marker=en.nextElement();
            if (marker.getType().equals(MarkerType.DECLINE)
            &&  findResolutionByMarkerId(marker.getId())==null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determine if the sheet is marked for referral or not. The
     * sheet is marked for referral if any Markers of type refer
     * are found, and are not resolved. {@link AssessmentLine#isDisabled() Disabled}
     * lines are ignored by this method.
     * @return true if the sheet is marked for referral, false otherwise.
     */
    public boolean isMarkedForRefer() {
        Hashtable<String,Marker> markers=getLinesOfType(Marker.class);
        Marker marker=null;

        for(Enumeration<Marker> en=markers.elements() ; en.hasMoreElements() ; ) {
            marker=en.nextElement();
            if (marker.getType().equals(MarkerType.REFER)
            &&  findResolutionByMarkerId(marker.getId())==null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Determine if the sheet includes an unresolved subjectivity. The
     * sheet is marked for subjectivity if any Markers of type {@link Subjectivity subjectivity}
     * are found, that are not resolved. {@link AssessmentLine#isDisabled() Disabled}
     * lines are ignored by this method.
     * @return true if the sheet is marked for decline, false otherwise.
     */
    public boolean isMarkedForSubjectivity() {
        Hashtable<String,Marker> markers=getLinesOfType(Marker.class);
        Marker marker=null;

        for(Enumeration<Marker> en=markers.elements() ; en.hasMoreElements() ; ) {
            marker=en.nextElement();
            if (marker.getType().equals(MarkerType.SUBJECTIVITY)
            &&  findResolutionByMarkerId(marker.getId())==null) {
                return true;
            }
        }

        return false;
    }

    /**
     * Generate a unique lineId. Each line in an assessment sheet must have a
     * unique id - lines refer to each other using this id. This method makes up
     * a random id, checks that it is not in use and returns it.
     * @return unique (unused) line id
     */
    public synchronized String generateLineId() {
        String id;

        // generate IDs until we find one that isn't being used
        do {
            id=Integer.toHexString(((int)(Math.random()*Integer.MAX_VALUE))).toUpperCase();
        } while (id.length()!=8 && assessmentLine.containsKey(id));

        return id;
    }
    
    /**
     * Return the next auto priority. This method of line priorotizaion orders lines in the
     * sheet by the order they were added to the sheet.
     * @return next priority;
     */
    public synchronized int generateAutoPriority() {
        return autoPriority++;
    }

    /**
     * Add a rate based loading entry to the sheet. This helper method simply
     * creates a {@link RateBehaviour RateBehaviour} instance with the arguments
     * supplied and adds that instance to the sheet as a new line.<p>
     * The suggested rule syntax is:<pre>
     * Load by {rate} of {dependsOn} to {contributesTo} because {reason}. This relates to {relatesTo}. Use id {id}.
     * </pre>
     * Note: Lines added using this methods are automatically assigned a priority based on the order they are added.
     * @param id The Id to use for this line
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one cntributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param rate The rate to be used in the calculation.
     */
    public void addLoading(String id, String reason, Reference relatesTo, String contributesTo, String dependsOn, Rate rate) {
        addLine(new RateBehaviour(id, reason, relatesTo, contributesTo, dependsOn,  BehaviourType.LOAD, rate, generateAutoPriority()));
    }

    /**
     * Add a loading with a generated lineId, and the specified arguments.
     * <br/>Note: Lines added using this methods are automatically assigned a priority based on the order they are added.
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one cntributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param rate The rate to be used in the calculation.
     */
    public void addLoading(String reason, Reference relatesTo, String contributesTo, String dependsOn, Rate rate) {
        addLine(new RateBehaviour(generateLineId(), reason, relatesTo, contributesTo, dependsOn,  BehaviourType.LOAD, rate));
    }

    /**
     * Add a loading with a generated lineId, and the specified arguments.
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one cntributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param rate The rate to be used in the calculation.
     * @param priority The priority of this line wrt other lines in this sheet (low value=low priority)
     */
    public void addLoading(String reason, Reference relatesTo, String contributesTo, String dependsOn, Rate rate, int priority) {
        addLine(new RateBehaviour(generateLineId(), reason, relatesTo, contributesTo, dependsOn,  BehaviourType.LOAD, rate, priority));
    }

    /**
     * Add a rate based dicount entry to the sheet. This helper method simply
     * creates a {@link RateBehaviour RateBehaviour} instance with the arguments
     * supplied and adds that instance to the sheet as a new line.
     * <br/>Note: Lines added using this methods are automatically assigned a priority based on the order they are added.
     * @param id The Id to use for this line
     * @param reason Free text reson for this discount being created.
     * @param relatesTo Optional reference to the part of the policy that caused this discount.
     * @param contributesTo The Id of the line that this one contributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param rate The rate to be used in the calculation.
     */
    public void addDiscount(String id, String reason, Reference relatesTo, String contributesTo, String dependsOn, Rate rate) {
        addLine(new RateBehaviour(id, reason, relatesTo, contributesTo, dependsOn,  BehaviourType.DISCOUNT, rate));
    }

    /**
     * Same as addDiscount, but generates the line id automatically.
     * <br/>Note: Lines added using this methods are automatically assigned a priority based on the order they are added.
     * @param reason Free text reson for this discount being created.
     * @param relatesTo Optional reference to the part of the policy that caused this discount.
     * @param contributesTo The Id of the line that this one contributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param rate The rate to be used in the calculation.
     * @see #addDiscount
     */
    public void addDiscount(String reason, Reference relatesTo, String contributesTo, String dependsOn, Rate rate) {
        addLine(new RateBehaviour(generateLineId(), reason, relatesTo, contributesTo, dependsOn,  BehaviourType.DISCOUNT, rate));
    }

    /**
     * Same as addDiscount, but generates the line id automatically.
     * @param reason Free text reson for this discount being created.
     * @param relatesTo Optional reference to the part of the policy that caused this discount.
     * @param contributesTo The Id of the line that this one contributes to.
     * @param dependsOn The Id of the line that this on is derived from.
     * @param rate The rate to be used in the calculation.
     * @param priority The priority of this line wrt other lines in this sheet (low value=low priority)
     * @see #addDiscount
     */
    public void addDiscount(String reason, Reference relatesTo, String contributesTo, String dependsOn, Rate rate, int priority) {
        addLine(new RateBehaviour(generateLineId(), reason, relatesTo, contributesTo, dependsOn,  BehaviourType.DISCOUNT, rate, priority));
    }

    /**
     * Add a referral line to this sheet. This helper method simply creates a
     * {@link Marker Marker} instance with the arguments supplied and adds it to
     * the sheet as a new line.
     * @param id The Id to use for this line
     * @param reason Free text reson for this referral being created.
     * @param relatesTo Optional reference to the part of the policy that caused referral.
     */
    public void addReferral(String id, String reason, Reference relatesTo) {
        addLine(new Marker(id, reason, relatesTo, MarkerType.REFER));
    }

    /**
     * Same as addReferral, but automatically generates the line id.
     * @param reason Free text reson for this referral being created.
     * @param relatesTo Optional reference to the part of the policy that caused referral.
     * @see #addReferral
     */
    public void addReferral(String reason, Reference relatesTo) {
        addLine(new Marker(generateLineId(), reason, relatesTo, MarkerType.REFER));
    }

    /**
     * Add a decline line to this sheet. This helper method simply creates a
     * {@link Marker Marker} instance with the arguments supplied and adds it to
     * the sheet as a new line.
     * @param id The Id to use for this line
     * @param reason Free text reson for this decline being created.
     * @param relatesTo Optional reference to the part of the policy that caused decline line.
     */
    public void addDecline(String id, String reason, Reference relatesTo) {
        addLine(new Marker(id, reason, relatesTo, MarkerType.DECLINE));
    }

    /**
     * Same as addDecline, but automatically generates a line id
     * @param reason Free text reson for this decline being created.
     * @param relatesTo Optional reference to the part of the policy that caused decline line.
     * @see #addDecline
     */
    public void addDecline(String reason, Reference relatesTo) {
        addLine(new Marker(generateLineId(), reason, relatesTo, MarkerType.DECLINE));
    }

    /**
     * Add a fixed sum line to this sheet. This helper method simply creates a
     * {@link FixedSum FixedSum} instance using the arguments supplied and adds
     * it to the sheet as a new line.
     * <br/>Note: Lines added using this methods are automatically assigned a priority based on the order they are added.
     * @param id This line's Id
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param amount The amount to be contributed.
     */
    public void addFixedSum(String id, String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount) {
        addLine(new FixedSum(id, reason, relatesTo, contributesTo, amount));
    }

    /**
     * Add a fixed sum line to this sheet. This helper method simply creates a
     * {@link FixedSum FixedSum} instance using the arguments supplied and adds
     * it to the sheet as a new line.
     * @param id This line's Id
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param amount The amount to be contributed.#
     * @param priority The priority of this line wrt other lines in this sheet (low value=low priority)
     */
    public void addFixedSum(String id, String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount, int priority) {
        addLine(new FixedSum(id, reason, relatesTo, contributesTo, amount, priority));
    }

    /**
     * Same as addFixedSum, but generates a line id automatically.
     * <br/>Note: Lines added using this methods are automatically assigned a priority based on the order they are added.
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param amount The amount to be contributed.
     * @see #addFixedSum
     */
    public void addFixedSum(String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount) {
        addLine(new FixedSum(generateLineId(), reason, relatesTo, contributesTo, amount));
    }

    /**
     * Same as addFixedSum, but generates a line id automatically.
     * @param reason Free text reson for this behaviour being created.
     * @param relatesTo Optional reference to the part of the policy that caused this behaviour.
     * @param contributesTo The Id of the line that this one will contribute to.
     * @param amount The amount to be contributed.
     * @param priority The priority of this line wrt other lines in this sheet (low value=low priority)
     * @see #addFixedSum
     */
    public void addFixedSum(String reason, Reference relatesTo, String contributesTo, CurrencyAmount amount, int priority) {
        addLine(new FixedSum(generateLineId(), reason, relatesTo, contributesTo, amount, priority));
    }

    /**
     * Add an assessment note to this sheet. This helper method simply creates a {@link AssessmentNote AssessmentNote}
     * to the sheet using the reason and releatesTo values passed in. The line's ID is automatically generated.
     * @param reason Free text of note.
     * @param relatesTo Optional reference to the part of the policy that the note related to (may be null).
     * @see #addAssessmentNote
     */
    public void addAssessmentNote(String reason, Reference relatesTo) {
        addLine(new AssessmentNote(generateLineId(), reason, relatesTo));
    }

    /**
     * Add an assessment note to this sheet. This helper method simply creates a {@link AssessmentNote AssessmentNote}
     * to the sheet using the reason and releatesTo values passed in. The line's ID is automatically generated.
     * @param id The Id to use for this line
     * @param reason Free text of note.
     * @param relatesTo Optional reference to the part of the policy that the note related to (may be null).
     * @see #addAssessmentNote
     */
    public void addAssessmentNote(String id, String reason, Reference relatesTo) {
        addLine(new AssessmentNote(id, reason, relatesTo));
    }
    
    /**
     * Getter returning the value of the lockingActor property. An assessment sheet must be locked to an actor before entries
     * can be made. This allows the sheet to know who is making entries and to associate the entries with that actor.
     * @return Value of the lockingActor property
     */
    public String getLockingActor() {
        return lockingActor;
    }

    /**
     * Setter to update the value of the lockingActor property. An assessment sheet must be locked to an actor before entries
     * can be made. This allows the sheet to know who is making entries and to associate the entries with that actor.
     * @param lockingActor New value for the lockingActor property
     * @throws IllegalStateException If the sheet is already locked by another actor
     */
    public synchronized void setLockingActor(String lockingActor) {
        if (this.lockingActor!=null 
        && !this.lockingActor.equals(lockingActor)
        && !lockingActor.startsWith(this.lockingActor+'.')) {
            throw new IllegalStateException("Attempt to lock locked sheet. Attempt by:"+lockingActor+" already locked by:"+this.lockingActor);
        }
        this.lockingActor = lockingActor;
    }

    /**
     * Unlock the assessment sheet - set locking actor to null.
     */
    public void clearLockingActor() {
        int lastDot=lockingActor.lastIndexOf('.');
        if (lastDot==-1) {
            this.lockingActor=null;
        }
        else {
            this.lockingActor=lockingActor.substring(0, lastDot);
        }
    }

    public Object clone() throws CloneNotSupportedException {
        AssessmentSheet that=(AssessmentSheet)super.clone();
        that.setLockingActor(null);
        return that;
    }
}
