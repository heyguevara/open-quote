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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import com.ail.core.Allowable;
import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;
import com.ail.financial.PaymentSchedule;
import com.ail.party.Party;

/**
 * @version $Revision: 1.8 $
 * @state $State: Exp $
 * @date $Date: 2006/09/24 12:44:43 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/Policy.java,v $
 * @stereotype type
 */
public class Policy extends Type {
    static final long serialVersionUID = 3175904078936470552L;

	private List<Allowable> allowable = new ArrayList<Allowable>();
    private List<Excess> excess = new ArrayList<Excess>();
    private List<Asset> asset = new ArrayList<Asset>();
    private List<Section> section = new ArrayList<Section>();
    private List<Coverage> coverage = new ArrayList<Coverage>();
    private String id = null;
    private PolicyStatus status = null;
    private AssessmentSheet assessmentSheet = null;
    private Party policyHolder = null;
    private String productTypeId = null;
    private String productName = null;
    private String policyNumber = null;

    /** The date when the policy was incepted (created) */
    private Date inceptionDate;

    /** The date at which the policy will (of has) expired */
    private Date expiryDate;

    /** A collection of the wordings associated with the policy (additional wordings may exist at the section level). */
    private List<Wording> wording = new ArrayList<Wording>();

    /** The proposed method of payment and its details */
    private PaymentSchedule paymentDetails;

    /** The quotation number associated with this policy */
    private String quotationNumber;

    /**
     * Get the collection of Coverages associagted with this policy. Coverages exist at both the
     * policy and section levels, this method will only return those associated with the policy. 
     * Use {@link Section#getCoverage} to get those associated with specific sections.
     * @return A collection of Coverages
     */
    public List<Coverage> getCoverage() {
        return coverage;
    }
    
    /**
     * Set the collection of Coverages associated with this policy.
     * @see #getCoverage()
     * @param coverage The collection of coverages to be associated with the policy.
     */
    public void setCoverage(List<Coverage> coverage) {
        this.coverage=new ArrayList<Coverage>(coverage);
    }
    
    /**
     * Get the number of Coverages associated with this policy.
     * @return The number of coverages associated with this policy.
     */
    public int getCoverageCount() {
        return coverage.size();
    }
    
    /**
     * Get the Coverage at a specific index in the collection associated with this policy.
     * @param i Index to return the coverage from.
     * @return  Coverage
     */
    public Coverage getCoverage(int i) {
        return (Coverage)coverage.get(i);
    }

    /**
     * Remove the coverage at a specific index in the collection of Coverages associated with this policy.
     * @param i Index of coverage to remove.
     */
    public void removeCoverage(int i) {
        coverage.remove(i);
    }

    /**
     * Add a coverage to the list associated with this policy.
     * @param coverage Coverage to add to the list.
     */
    public void addCoverage(Coverage coverage) {
        this.coverage.add(coverage);
    }

    /**
     * Remove the specified coverage from the list associated with this policy.
     * @param coverage Coverage to be removed
     */
    public void removeCoverage(Coverage coverage) {
        this.coverage.remove(coverage);
    }

    /**
     * Fetch the coverage with a specific id from the collection associated with this policy.
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
     * Fetch the collection of excess objects associated with this instance.
     *
     * @return A collection of instances of Excess.
     */
    public List<Excess> getExcess() {
        return excess;
    }

    /**
     * Set the collection of instances of Excess associated with this object.
     *
     * @param excess A collection of instances of Excess
     * @see #getExcess
     */
    public void setExcess(List<Excess> excess) {
        this.excess = new ArrayList<Excess>(excess);
    }

    /**
     * Fetch a count of the number of excess objects associated with this instance.
     *
     * @return Count of excess objects.
     */
    public int getExcessCount() {
        return excess.size();
    }

    /**
     * Get the instance of excess at a specific index from the collection of Excess objects associated with this instance.
     *
     * @param i Index
     * @return Instance of Excess found at index i.
     */
    public Excess getExcess(int i) {
        return (Excess)excess.get(i);
    }

    /**
     * Fetch a collection of the excesses associated with a specific section.
     * If no excesses are found, or the section is unknown an empty list is returned.
     * @param sectionId ID of the section to fetch excesses for
     * @return Collection of Excess instances
     */
    public List<Excess> getExcessBySectionId(String sectionId) {
      List<Excess> ret=new ArrayList<Excess>();
      Excess that;
      
      Section section=getSectionById(sectionId);
      
      if (section!=null) {
        for(Iterator<Excess> it=excess.iterator() ; it.hasNext() ; ) {
          that=it.next();
          if (section.getExcessId().contains(that.getId())) {
              ret.add(that);
          }
        }
      }

      return ret;
    }

    /**
     * Remove a specific excess from the collection of Excess objects associated with this object.
     *
     * @param i Index of excess to remove.
     */
    public void removeExcess(int i) {
        excess.remove(i);
    }

    /**
     * Add a excess to the collection associated with this object.
     *
     * @param excess Instance of Excess to add.
     */
    public void addExcess(Excess excess) {
        this.excess.add(excess);
    }

    /**
     * Remove a specific instance of Excess from the collection associated with this object.
     *
     * @param excess Object to be removed from the collection.
     */
    public void removeExcess(Excess excess) {
        this.excess.remove(excess);
    }

    /**
     * Fetch a reference to a excess by its ID. Each excess has a unique id within a policy which
     * other parts of the policy (other excess, sections, etc) use to refer to it. This method searches
     * through the excess in the policy the appropriate excess if found.
     * @param excessId excess's id
     * @return A excess, or null if an excess by this ID cannot be found.
     */
    public Excess getExcessById(String excessId) {
        Excess that;

        for(Iterator<Excess> it=excess.iterator() ; it.hasNext() ; ) {
            that=(Excess)it.next();
            if (that.getId().equals(excessId)) {
                return that;
            }
        }

        return null;
    }

    /**
     * Fetch a collection of reference to excesses of a specific type in this policy. A policy
     * may contain more than one excess of the same type. This method searches through the
     * excess matching the excessTypeId specified against each excess's excessTypeId.
     * @param excessTypeId Type of excess to search for.
     * @return A collection of the excesses found, or an empty Collection if none are found.
     */
    public List<Excess> getExcessByTypeId(String excessTypeId) {
        List<Excess> ret=new ArrayList<Excess>();
        Excess that;

        for(Iterator<Excess> it=excess.iterator() ; it.hasNext() ; ) {
            that=it.next();
            if (that.getExcessTypeId().equals(excessTypeId)) {
                ret.add(that);
            }
        }

        return ret;
    }

    /**
     * Fetch the collection of asset objects associated with this instance.
     *
     * @return A collection of instances of Asset.
     */
    public List<Asset> getAsset() {
        return asset;
    }

    /**
     * Set the collection of instances of Asset associated with this object.
     *
     * @param asset A collection of instances of Asset
     */
    public void setAsset(List<Asset> asset) {
        this.asset = new ArrayList<Asset>(asset);
    }

    /**
     * Fetch a count of the number of asset objects associated with this instance.
     * @return Count of asset objects.
     */
    public int getAssetCount() {
        return asset.size();
    }

    /**
     * Get the instance of asset at a specific index from the collection of Asset objects associated with this instance.
     * @param i Index
     * @return Instance of Asset found at index i.
     */
    public Asset getAsset(int i) {
        return (Asset)asset.get(i);
    }

    /**
     * Remove a specific asset from the collection of Asset objects associated with this object.
     * @param i Index of asset to remove.
     */
    public void removeAsset(int i) {
		removeAsset(getAsset(i));
    }

    /**
     * Add a asset to the collection associated with this object.
     * @param asset Instance of Asset to add.
     */
    public void addAsset(Asset asset) {
        this.asset.add(asset);
    }

    /**
     * Remove a specific instance of Asset from the collection associated with this object.
     * @param asset Object to be removed from the collection.
     */
    public void removeAsset(Asset asset) {
        String id = asset.getId();
        this.asset.remove(asset);
        
        // remove asset from sections
        if(id==null){
            return;
        }
        
		List<Section> sections = getSection();
		if(sections==null || sections.isEmpty()){
			return;       	
		}
		
		for(Iterator<Section> iterator = sections.iterator() ; iterator.hasNext() ; ) {
			Section section = iterator.next();
			section.removeAssetId(id);   	
		}
    }

	/**
	 * Remove a specific instance of Asset from the collection associated with this object by its ID.
	 * Each asset has a unique id within a policy which
     * other parts of the policy (Sections, excesses, etc) use to refer to it. This method seaches
     * through the assets in the policy the appropriate asset if found.
     * @param id AssetId
	 */
	public void removeAssetById(String id) {
		removeAsset(getAssetById(id));
	}

    /**
     * Fetch a reference to an asset by its ID. Each asset has a unique id within a policy which
     * other parts of the policy (Sections, excesses, etc) use to refer to it. This method seaches
     * through the assets in the policy the appropriate asset if found.
     * @param id AssetId
     * @return An asset, or null if an asset by this ID cannot be found.
     */
    public Asset getAssetById(String id) {
        Asset that;

        for(Iterator<Asset> it=asset.iterator() ; it.hasNext() ; ) {
            that=it.next();
            if (that.getId().equals(id)) {
                return that;
            }
        }

        return null;
    }

    /**
     * Fetch a collection of reference to assets of a specific type in this policy. A policy
     * may contain more than one asset of the same type (more than one car on a motor policy
     * for example). This method searches through the assets matching the typeId specified
     * against each asset's assetTypeId.
     * @param typeId Type of Asset to search for.
     * @return A collection of the assets found, or an empty Collection if none are found.
     */
    public List<Asset> getAssetByTypeId(String typeId) {
        List<Asset> ret=new ArrayList<Asset>();
        Asset that;

        for(Iterator<Asset> it=asset.iterator() ; it.hasNext() ; ) {
            that=it.next();
            if (that.getAssetTypeId().equals(typeId)) {
                ret.add(that);
            }
        }

        return ret;
    }

    /**
     * Fetch a collection of the assets associated with a specific section. This method
     * returns <u>all</u> of the assets associated, those in the section's insured and uninsured
     * asset lists.</p>
     * If no assets are found, or the section is unknown an empty list is returned.
     * @param sectionId ID of the section to fetch assets for
     * @return Collection of Asset instances
     */
    public List<Asset> getAssetBySectionId(String sectionId) {
      List<Asset> ret=new ArrayList<Asset>();
      Asset that;
      
      Section section=getSectionById(sectionId);
      
      if (section!=null) {
        for(Iterator<Asset> it=asset.iterator() ; it.hasNext() ; ) {
          that=it.next();
          if (section.getAssetId().contains(that.getId())
          ||  section.getUninsuredAssetId().contains(that.getId())) {
              ret.add(that);
          }
        }
      }

      return ret;
    }
    
    /**
     * Fetch a collection of the assets associated with a specific section. This method
     * returns only the section's insured assets.</p>
     * If no assets are found, or the section is unknown an empty list is returned.
     * @see #getAssetBySectionId(String)
     * @see #getUninsuredAssetBySectionId(String)
     * @param sectionId ID of the section to fetch assets for
     * @return Collection of Asset instances
     */
    public List<Asset> getInsuredAssetBySectionId(String sectionId) {
      List<Asset> ret=new ArrayList<Asset>();
      Asset that;
      
      Section section=getSectionById(sectionId);
      
      if (section!=null) {
        for(Iterator<Asset> it=asset.iterator() ; it.hasNext() ; ) {
          that=it.next();
          if (section.getAssetId().contains(that.getId())) {
              ret.add(that);
          }
        }
      }

      return ret;
    }
    
    /**
     * Fetch a collection of the assets associated with a specific section. This method
     * returns only the section's uninsured assets.</p>
     * If no assets are found, or the section is unknown an empty list is returned.
     * @see #getAssetBySectionId(String)
     * @see #getInsuredAssetBySectionId(String)
     * @param sectionId ID of the section to fetch assets for
     * @return Collection of Asset instances
     */
    public List<Asset> getUninsuredAssetBySectionId(String sectionId) {
      List<Asset> ret=new ArrayList<Asset>();
      Asset that;
      
      Section section=getSectionById(sectionId);
      
      if (section!=null) {
        for(Iterator<Asset> it=asset.iterator() ; it.hasNext() ; ) {
          that=it.next();
          if (section.getUninsuredAssetId().contains(that.getId())) {
              ret.add(that);
          }
        }
      }

      return ret;
    }

    /**
     * Fetch the collection of section objects associated with this instance.
     * @return A collection of instances of Section.
     */
    public List<Section> getSection() {
        return section;
    }

    /**
     * Set the collection of instances of Section associated with this object.
     * @param section A collection of instances of Section
     */
    public void setSection(List<Section> section) {
        this.section = new ArrayList<Section>(section);
    }

    /**
     * Fetch a count of the number of section objects associated with this instance.
     * @return Count of section objects.
     */
    public int getSectionCount() {
        return section.size();
    }

    /**
     * Get the instance of section at a specific index from the collection of Section objects
     * associated with this policy.
     * @param i Index
     * @return Instance of Section found at index i.
     */
    public Section getSection(int i) {
        return (Section)section.get(i);
    }

    /**
     * Remove a specific section from the collection of Section objects associated with this policy.
     * @param i Index of section to remove.
     */
    public void removeSection(int i) {
        section.remove(i);
    }

    /**
     * Add a section to the collection associated with this policy.
     * @param section Instance of Section to add.
     */
    public void addSection(Section section) {
        this.section.add(section);
    }

    /**
     * Remove a specific instance of Asset from the collection associated with this object.
     * @param section Object to be removed from the collection.
     */
    public void removeSection(Section section) {
        this.section.remove(section);
    }

    /**
     * Fetch a reference to a section by its ID. Each section has a unique id within a policy which
     * other parts of the policy (other sections, excesses, etc) use to refer to it. This method seaches
     * through the sections in the policy the appropriate section if found.
     * @param sectionId section's id
     * @return A section, or null if an section by this ID cannot be found.
     */
    public Section getSectionById(String sectionId) {
        Section that;

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            that=it.next();
            if (that.getId().equals(sectionId)) {
                return that;
            }
        }

        return null;
    }

    /**
     * Fetch a collection of reference to sections of a specific type in this policy. A policy
     * may contain more than one section of the same type. This method searches through the
     * sections matching the sectionTypeId specified against each section's sectionTypeId.
     * @param sectionTypeId Type of Section to search for.
     * @return A collection of the sections found, or an empty Collection if none are found.
     */
    public List<Section> getSectionByTypeId(String sectionTypeId) {
        List<Section> ret=new ArrayList<Section>();
        Section that;

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            that=it.next();
            if (that.getSectionTypeId().equals(sectionTypeId)) {
                ret.add(that);
            }
        }

        return ret;
    }

    /**
     * Get this policy ID. This id is unique to the polict
     *
     * @return The policy ID.
     */
    public String getId() {
        return id;
    }

    /**
     * Set the policy ID
     * @param id New policy ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Get the policy status. This status indicates where the policy is in its lifecycle.
     * Examples are Application, Quotation, OnRisk...
     *
     * @return The current policy status.
     */
    public PolicyStatus getStatus() {
        return status;
    }

    /**
     * Get the policy status as a string (as opposed to an instance of PolicyStatus).
     * @return String representation of the policy status, or null if the status property has not been set.
     */
    public String getStatusAsString() {
        if (status!=null) {
            return status.name();
        }
        return null;
    }

    /**
     * Move the policy to another status.
     *
     * @see #getStatus
     */
    public void setStatus(PolicyStatus status) {
        this.status = status;
    }

    /**
     * Set the policy status as a String. The String must represents a valid
     * policy status. i.e. it must be suitable for a call to PolicyStatus.forName().
     * @see PolicyStatus#forName
     * @param status New policy status
     * @throws IndexOutOfBoundsException If status is not a valid policy status.
     */
    public void setStatusAsString(String status) throws IndexOutOfBoundsException {
        this.status=PolicyStatus.valueOf(status);
    }

    /**
     * Get the AssessmentSheet associated with this policy.
     *
     * @return Assessment sheet. An empty assessment sheet may be returned, but null is never returned.
     */
    public AssessmentSheet getAssessmentSheet() {
        if (assessmentSheet==null) {
            assessmentSheet=new AssessmentSheet();
        }
        
        return assessmentSheet;
    }


    /**
     * Set the assessment sheet for this policy
     *
     * @param assessmentSheet Assessment sheet
     */
    public void setAssessmentSheet(AssessmentSheet assessmentSheet) {
        this.assessmentSheet = assessmentSheet;
    }

    /**
     * Get the policy holder associated with this policy
     *
     * @return An instance of policy holder (Organisation or Person)
     */
    public com.ail.party.Party getPolicyHolder() {
        return policyHolder;
    }

    /**
     * Set the policy holder associated with this policy
     *
     * @param policyHolder New policy holder
     */
    public void setPolicyHolder(Party policyHolder) {
        this.policyHolder = policyHolder;
    }

    /**
     * Get the ProductTypeId. This string identifies the product that the policy
     * relates to. Among other things it allows services (e.g. assessRisk) to identify
     * the appropriate rules to run when processing this policy.
     *
     * @return The product type id.
     */
    public String getProductTypeId() {
        return productTypeId;
    }

    /**
     * Set the product type id associated with this policy.
     *
     * @param productTypeId New product type id
     * @see #getProductTypeId
     */
    public void setProductTypeId(String productTypeId) {
        this.productTypeId = productTypeId;
    }

    /**
     * Get the policy number associated with this policy.
     *
     * @return The policy number.
     */
    public String getPolicyNumber() {
        return policyNumber;
    }

    /**
     * Set the policy number associated with this policy
     *
     * @param policyNumber New policy number
     */
    public void setPolicyNumber(String policyNumber) {
        this.policyNumber = policyNumber;
    }

    /**
     * Getter returning the value of the quotationNumber property. The quotation number associated with this policy
     * @return Value of the quotationNumber property
     */
    public String getQuotationNumber() {
        return quotationNumber;
    }

    /**
     * Setter to update the value of the quotationNumber property. The quotation number associated with this policy
     * @param quotationNumber New value for the quotationNumber property
     */
    public void setQuotationNumber(String quotationNumber) {
        this.quotationNumber = quotationNumber;
    }

    /**
     * Check if this policy is marked as declined. This involves checking the
     * assessment sheet on the policy itself and on all of its sections.
     *
     * @return true if a decline flag is found, false otherwise.
     */
    public boolean isMarkedForDecline() {
        if (assessmentSheet==null) {
            return false;
        }
        else if (assessmentSheet.isMarkedForDecline()) {
            return true;
        }

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            if (it.next().getAssessmentSheet().isMarkedForDecline()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if this policy is marked as refer. This involves checking the
     * assessment sheet on the policy itself and on all of its sections.
     *
     * @return true if a refer flag is found, false otherwise.
     */
    public boolean isMarkedForRefer() {
        if (assessmentSheet==null) {
            return false;
        }
        else if (assessmentSheet.isMarkedForRefer()) {
            return true;
        }

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            if (it.next().getAssessmentSheet().isMarkedForRefer()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Check if this policy is marked with a subjectivity. This involves checking the
     * assessment sheet on the policy itself and on all of its sections.
     *
     * @return true if a refer flag is found, false otherwise.
     */
    public boolean isMarkedForSubjectivity() {
        if (assessmentSheet==null) {
            return false;
        }
        else if (assessmentSheet.isMarkedForSubjectivity()) {
            return true;
        }

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            if (it.next().getAssessmentSheet().isMarkedForSubjectivity()) {
                return true;
            }
        }

        return false;
    }

    /**
     * Get the total premium. The premium is a calculate value held in the assessment sheeet,
     * it will only be present once the premium has been calculated.
     *
     * @return Total premium, or null if the field is not present in the assessment sheet.
     */
    public CurrencyAmount getTotalPremium() {
        if (assessmentSheet!=null) {
            CalculationLine line=(CalculationLine)assessmentSheet.findLineById("total premium");
            if (line!=null) {
                return line.getAmount();
            }
        }

        throw new IllegalStateException("Assessment line: 'total premium' is not defined for this policy");
    }

    /**
     * Lock all the assessment sheets in the policy to the named actor
     * @param actor The name of the locking actor
     */
    public void lockAllAssessmentSheets(String actor) {
        if (assessmentSheet!=null) {
            assessmentSheet.setLockingActor(actor);
        }

        AssessmentSheet as;

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            as=it.next().getAssessmentSheet();
            if (as!=null) {
                as.setLockingActor(actor);
            }
        }
    }

    /**
     * Unlock all the assessment sheets in the policy
     */
    public void unlockAllAssessmentSheets() {
        if (assessmentSheet!=null) {
            assessmentSheet.clearLockingActor();
        }

        AssessmentSheet as;

        for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
            as=it.next().getAssessmentSheet();
            if (as!=null) {
                as.clearLockingActor();
            }
        }
    }

    /**
     * Remove all the assessment sheet lines added by the specified origin. Lines
     * are removed from both the policy and section levels.
     * @param origin The name of the origin to remove lines for.
     */
    public void removeAssessmentLinesByOrigin(String origin) {
        Section sect=null;

        // If the policy has an assessment sheet, clean it up
        if (assessmentSheet!=null) {
            assessmentSheet.removeLinesByOrigin(origin);
        }

        // loop through the sections
        if (section!=null) {
            for(Iterator<Section> it=section.iterator() ; it.hasNext() ; ) {
                sect=it.next();

                if (sect.getAssessmentSheet()!=null) {
                    sect.getAssessmentSheet().removeLinesByOrigin(origin);
                }
            }
        }
    }

    /**
     * Getter returning the value of the inceptionDate property. The date when the policy was incepted (created)
     * @return Value of the inceptionDate property
     */
    public Date getInceptionDate() {
        return inceptionDate;
    }

    /**
     * Setter to update the value of the inceptionDate property. The date when the policy was incepted (created)
     * @param inceptionDate New value for the inceptionDate property
     */
    public void setInceptionDate(Date inceptionDate) {
        this.inceptionDate = inceptionDate;
    }

    /**
     * Getter returning the value of the inceptionDate property wrapped in CDATA tags. The date when the
     * policy was incepted (created)
     * @return Value of the inceptionDate property
     */
    public String getInceptionDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(getInceptionDate());
    }

    /**
     * Setter to update the value of the inceptionDate property from a String. The supplied date must be in
     * DateFormat.SHORT format.
     * @param inceptionDate New value for the inceptionDate property
     */
    public void setInceptionDateAsString(String inceptionDate) throws ParseException {
        setInceptionDate(DateFormat.getDateInstance(DateFormat.SHORT).parse(inceptionDate));
    }

    /**
     * Getter returning the value of the expiryDate property. The date at which the policy will (of has) expired
     * @return Value of the expiryDate property
     */
    public Date getExpiryDate() {
        return expiryDate;
    }

    /**
     * Setter to update the value of the expiryDate property. The date at which the policy will (of has) expired
     * @param expiryDate New value for the expiryDate property
     */
    public void setExpiryDate(Date expiryDate) {
        this.expiryDate = expiryDate;
    }

    /**
     * Getter returning the value of the expiryDate property wrapped in CDATA tags. The date at which the
     * policy will (of has) expired
     * @return Value of the expiryDate property
     */
    public String getExpiryDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(getExpiryDate());
    }

    public void setExpiryDateAsString(String expiryDate) throws ParseException {
        setExpiryDate(DateFormat.getDateInstance(DateFormat.SHORT).parse(expiryDate));
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
        return (com.ail.insurance.policy.Wording) this.wording.get(i);
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
     * Getter returning the value of the paymentDetails property. The proposed method of payment and its details
     * @return Value of the paymentDetails property
     */
    public PaymentSchedule getPaymentDetails() {
        return paymentDetails;
    }

    /**
     * Setter to update the value of the paymentDetails property. The proposed method of payment and its details
     * @param paymentDetails New value for the paymentDetails property
     */
    public void setPaymentDetails(PaymentSchedule paymentDetails) {
        this.paymentDetails = paymentDetails;
    }
    



	/**
	 * Get the collection of instances of com.ail.core.Allowable associated with this object.
	 * @return A collection of instances of Allowable
	 * @see #setAllowable
	 */
	public List<Allowable> getAllowable() {
		return allowable;
	}

	/**
	 * Set the collection of instances of com.ail.core.Allowable associated with this object.
	 * @param allowable A collection of instances of Allowable
	 * @see #getAllowable
	 */
	public void setAllowable(List<Allowable> allowable) {
		this.allowable = new ArrayList<Allowable>(allowable);
	}

	/**
	 * Get a count of the number of com.ail.core.Allowable instances associated with this object
	 * @return Number of instances
	 */
	public int getAllowableCount() {
		return this.allowable.size();
	}

	/**
	 * Fetch a spacific com.ail.core.Allowable from the collection by index number.
	 * @param i Index of element to return
	 * @return The instance of com.ail.core.Allowable at the specified index
	 */
	public Allowable getAllowable(int i) {
		return (com.ail.core.Allowable) this.allowable.get(i);
	}

	/**
	 * Remove the element specified from the list.
	 * @param i Index of element to remove
	 */
	public void removeAllowable(int i) {
		this.allowable.remove(i);
	}

	/**
	 * Remove the specified instance of com.ail.core.Allowable from the list.
	 * @param wording Instance to be removed
	 */
	public void removeAllowable(Allowable wording) {
		this.allowable.remove(wording);
	}

	/**
	 * Add an instance of com.ail.core.Allowable to the list associated with this object.
	 * @param wording Instance to add to list
	 */
	public void addAllowable(Allowable allowable) {
		this.allowable.add(allowable);
	}

	/**
     * Get the textual name of the product which this policy is an instance of. This is the
     * name which the product is known by outside of the system, and will appear on screens 
     * and documents. 
     * @return product name
	 */
    public String getProductName() {
        return productName;
    }

    /**
     * @see #getProductName()
     * @param productName
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }
    
}
