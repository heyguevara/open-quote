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
import java.util.List;

import com.ail.core.Type;
import com.ail.util.YesNo;

/**
 * A section with the policy.
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2006/07/26 21:00:28 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Section.java,v $
 * @stereotype type
 */
public class Section extends Type {
    static final long serialVersionUID = -1451057528960584717L;

    private String id;
    private List<String> assetId = new ArrayList<String>();
    private List<String> uninsuredAssetId = new ArrayList<String>();
    private List<Coverage> coverage=new ArrayList<Coverage>();
    private AssessmentSheet assessmentSheet;
    private String sectionTypeId;

    /** A collection of the wordings associated with this section. */
    private List<Wording> wording = new ArrayList<Wording>();

    /**
     * If a section is "excluded" (this property==Yes), then there will be no sum insured
     * or premium calculated but a warranty will be printed on the proposal form
     * e.g. earthquake excluded.
     */
    private YesNo excluded;

    /**
     * If a section is included (this property==Yes), then no sum insured is collected or
     * premium calculated but this is an active cover for a claim and is not excluded.
     * The fixed sum insured, if defined, is added into the aggregate limit.
     */
    private YesNo included;

    /** A collecion of the IDs of the excesses associated with this section */
    private List<String> excessId = new ArrayList<String>();

    /**
     * Get this section's ID. This ID is unique within a policy.
     * @return Section's ID
     */
    public String getId() {
        return id;
    }

    /**
     * Set this section's ID.
     * @see #getId
     * @param id Section's ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the collection of Coverages associagted with this section. Coverages exist at both the
     * section and section levels, this method will only return those associated with the section. 
     * Use {@link Section#getCoverage} to get those associated with specific sections.
     * @return A collection of Coverages
     */
    public List<Coverage> getCoverage() {
        return coverage;
    }
    
    /**
     * Set the collection of Coverages associated with this section.
     * @see #getCoverage()
     * @param coverage The collection of coverages to be associated with the section.
     */
    public void setCoverage(List<Coverage> coverage) {
        this.coverage=new ArrayList<Coverage>(coverage);
    }
    
    /**
     * Get the number of Coverages associated with this section.
     * @return The number of coverages associated with this section.
     */
    public int getCoverageCount() {
        return coverage.size();
    }
    
    /**
     * Get the Coverage at a specific index in the collection associated with this section.
     * @param i Index to return the coverage from.
     * @return  Coverage
     */
    public Coverage getCoverage(int i) {
        return (Coverage)coverage.get(i);
    }

    /**
     * Remove the coverage at a specific index in the collection of Coverages associated with this section.
     * @param i Index of coverage to remove.
     */
    public void removeCoverage(int i) {
        coverage.remove(i);
    }

    /**
     * Add a coverage to the list associated with this section.
     * @param coverage Coverage to add to the list.
     */
    public void addCoverage(Coverage coverage) {
        this.coverage.add(coverage);
    }

    /**
     * Remove the specified coverage from the list associated with this section.
     * @param coverage Coverage to be removed
     */
    public void removeCoverage(List<Coverage> coverage) {
        this.coverage.remove(coverage);
    }

    /**
     * Fetch the coverage with a specific id from the collection associated with this section.
     * @param coverageId ID of coverage to be returned
     * @return Coverage, or null if no match is found.
     */
    public Coverage getCoverageById(String coverageId) {
        for(Coverage c: coverage) {
            if (c.getId().equals(coverageId)) {
                return c;
            }
        }

        return null;
    }

    /**
     * Fetch the collection of assetId objects associated with this instance.
     * @return A collection of instances of String.
     */
    public List<String> getAssetId() {
        return assetId;
    }

    /**
     * Set the collection of instances of String associated with this object.
     * @param assetId A collection of instances of String
     */
    public void setAssetId(List<String> assetId) {
        this.assetId = new ArrayList<String>(assetId);
    }

    /**
     * Fetch a count of the number of assetId objects associated with this instance.
     * @return Count of assetId objects.
     */
    public int getAssetIdCount() {
        return assetId.size();
    }

    /**
     * Get the instance of assetId at a specific index from the collection of String objects associated with this instance.
     * @param i Index
     * @return Instance of String found at index i.
     */
    public String getAssetId(int i) {
        return (String)assetId.get(i);
    }

    /**
     * Remove a specific assetId from the collection of String objects associated with this object.
     * @param i Index of assetId to remove.
     */
    public void removeAssetId(int i) {
        assetId.remove(i);
    }

    /**
     * Add a assetId to the collection associated with this object.
     * @param assetId Instance of String to add.
     */
    public void addAssetId(String assetId) {
        this.assetId.add(assetId);
    }

    /**
     * Remove a specific instance of String from the collection associated with this object.
     * @param assetId Object to be removed from the collection.
     */
    public void removeAssetId(String assetId) {
        this.assetId.remove(assetId);
    }

    /**
     * Fetch the collection of uninsuredAssetId objects associated with this instance.
     * @return A collection of instances of String.
     */
    public List<String> getUninsuredAssetId() {
        return uninsuredAssetId;
    }

    /**
     * Set the collection of instances of uninsuredAssetId associated with this object.
     * @param uninsuredAssetId A collection of instances of String
     */
    public void setUninsuredAssetId(List<String> uninsuredAssetId) {
        this.uninsuredAssetId = new ArrayList<String>(uninsuredAssetId);
    }

    /**
     * Fetch a count of the number of uninsuredAssetId objects associated with this instance.
     * @return Count of uninsuredAssetId objects.
     */
    public int getUninsuredAssetIdCount() {
        return uninsuredAssetId.size();
    }

    /**
     * Get the instance of uninsuredAssetId at a specific index from the collection of String objects associated with this instance.
     * @param i Index
     * @return Instance of String found at index i.
     */
    public String getUninsuredAssetId(int i) {
        return (String)uninsuredAssetId.get(i);
    }

    /**
     * Remove a specific uninsuredAssetId from the collection of String objects associated with this object.
     * @param i Index of uninsuredAssetId to remove.
     */
    public void removeUninsuredAssetId(int i) {
      uninsuredAssetId.remove(i);
    }

    /**
     * Add a uninsuredAssetId to the collection associated with this object.
     * @param uninsuredAssetId Instance of String to add.
     */
    public void addUninsuredAssetId(String uninsuredAssetId) {
        this.uninsuredAssetId.add(uninsuredAssetId);
    }

    /**
     * Remove a specific instance of uninsuredAssetId from the collection associated with this object.
     * @param uninsuredAssetId Object to be removed from the collection.
     */
    public void removeUninsuredAssetId(String uninsuredAssetId) {
        this.uninsuredAssetId.remove(uninsuredAssetId);
    }

    /**
     * Get the assessment sheet  associated with this section.
     * @return The assessment sheet.
     */
    public AssessmentSheet getAssessmentSheet() {
        return assessmentSheet;
    }

    /**
     * Set the assessment sheet associated with this section.
     * @param assessmentSheet The assessment sheet
     */
    public void setAssessmentSheet(AssessmentSheet assessmentSheet) {
        this.assessmentSheet = assessmentSheet;
    }

    /**
     * Get the section type id associated with this section. This id is used to associate
     * an instance of a section with the rules used to process it.
     * @return Section type id.
     */
    public String getSectionTypeId() {
        return sectionTypeId;
    }

    /**
     * Set the section type id.
     * @see #getSectionTypeId
     * @param sectionTypeId
     */
    public void setSectionTypeId(String sectionTypeId) {
        this.sectionTypeId = sectionTypeId;
    }

    /**
     * Get the collection of instances of com.ail.insurance.policy.Wording associated with this object.
     * @return wording A collection of instances of Excess
     * @see #setWording
     */
    public List<Wording> getWording() {
        return wording;
    }

    /**
     * Set the collection of instances of com.ail.insurance.policy.Wording associated with this object.
     * @param wording A collection of instances of Excess
     * @see #getWording
     */
    public void setWording(List<Wording> wording) {
        this.wording = new ArrayList<Wording>(wording);
    }

    /**
     * Get a count of the number of com.ail.insurance.policy.Wording instances associated with this object
     * @return Number of instances
     */
    public int getWordingCount() {
        return this.wording.size();
    }

    /**
     * Fetch a spacific com.ail.insurance.policy.Wording from the collection by index number.
     * @param i Index of element to return
     * @return The instance of com.ail.insurance.policy.Wording at the specified index
     */
    public Wording getWording(int i) {
        return (Wording) this.wording.get(i);
    }

    /**
     * Remove the element specified from the list.
     * @param i Index of element to remove
     */
    public void removeWording(int i) {
        this.wording.remove(i);
    }

    /**
     * Remove the specified instance of com.ail.insurance.policy.Wording from the list.
     * @param wording Instance to be removed
     */
    public void removeWording(Wording wording) {
        this.wording.remove(wording);
    }

    /**
     * Add an instance of com.ail.insurance.policy.Wording to the list associated with this object.
     * @param wording Instance to add to list
     */
    public void addWording(Wording wording) {
        this.wording.add(wording);
    }

    /**
     * Getter returning the value of the excluded property. If a section is "excluded" (this property==Yes), then there will
     * be no sum insured or premium calculated but a warranty will be printed on the proposal form e.g. earthquake excluded.
     * @return Value of the excluded property
     */
    public YesNo getExcluded() {
        return excluded;
    }

    /**
     * Setter to update the value of the excluded property. If a section is "excluded" (this property==Yes), then there will
     * be no sum insured or premium calculated but a warranty will be printed on the proposal form e.g. earthquake excluded.
     * @param excluded New value for the excluded property
     */
    public void setExcluded(YesNo excluded) {
        this.excluded = excluded;
    }

    /**
     * Get the value of the excluded property as a string (as opposed to an instance of YesNo).
     * @return String representation of the excluded, or null if the property has not been set.
     */
    public String getExcludedAsString() {
        if (excluded != null) {
            return excluded.name();
        }
        return null;
    }

    /**
     * Set the excluded property as a String. The String must represents a valid
     * YesNo. i.e. it must be suitable for a call to YesNo.forName().
     * @param excluded New value for property.
     * @throws IndexOutOfBoundsException If excluded is not a valid YesNo.
     */
    public void setExcludedAsString(String excluded) throws IndexOutOfBoundsException {
        this.excluded = YesNo.valueOf(excluded);
    }

    /**
     * Getter returning the value of the included property. If a section is included (this property==Yes), then no sum insured
     * is collected or premium calculated but that is an active cover for a claim and is not excluded. The fixed sum insured,
     * if exists, is added into the aggregate limit.
     * @return Value of the included property
     */
    public YesNo getIncluded() {
        return included;
    }

    /**
     * Setter to update the value of the included property. If a section is included (this property==Yes), then no sum insured
     * is collected or premium calculated but that is an active cover for a claim and is not excluded. The fixed sum insured,
     * if exists, is added into the aggregate limit.
     * @param included New value for the included property
     */
    public void setIncluded(YesNo included) {
        this.included = included;
    }

    /**
     * Get the value of the included property as a string (as opposed to an instance of YesNo).
     * @return String representation of the included, or null if the property has not been set.
     */
    public String getIncludedAsString() {
        if (included != null) {
            return included.name();
        }
        return null;
    }

    /**
     * Set the included property as a String. The String must represents a valid
     * YesNo. i.e. it must be suitable for a call to YesNo.forName().
     * @param included New value for property.
     * @throws IndexOutOfBoundsException If included is not a valid YesNo.
     */
    public void setIncludedAsString(String included) throws IndexOutOfBoundsException {
        this.included = YesNo.valueOf(included);
    }

    /**
     * Get the collection of instances of String associated with this object.
     * @return excessId A collection of instances of Excess
     * @see #setExcessId
     */
    public List<String> getExcessId() {
        return excessId;
    }

    /**
     * Set the collection of instances of String associated with this object.
     * @param excessId A collection of instances of Excess
     * @see #getExcessId
     */
    public void setExcessId(List<String> excessId) {
        this.excessId = new ArrayList<String>(excessId);
    }

    /**
     * Get a count of the number of String instances associated with this object
     * @return Number of instances
     */
    public int getExcessIdCount() {
        return this.excessId.size();
    }

    /**
     * Fetch a spacific String from the collection by index number.
     * @param i Index of element to return
     * @return The instance of String at the specified index
     */
    public String getExcessId(int i) {
        return (String)this.excessId.get(i);
    }

    /**
     * Remove the element specified from the list.
     * @param i Index of element to remove
     */
    public void removeExcessId(int i) {
        this.excessId.remove(i);
    }

    /**
     * Remove the specified instance of String from the list.
     * @param excessId Instance to be removed
     */
    public void removeExcessId(String excessId) {
        this.excessId.remove(excessId);
    }

    /**
     * Add an instance of String to the list associated with this object.
     * @param excessId Instance to add to list
     */
    public void addExcessId(String excessId) {
        this.excessId.add(excessId);
    }
}
