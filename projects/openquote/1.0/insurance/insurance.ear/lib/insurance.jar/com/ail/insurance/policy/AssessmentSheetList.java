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

import java.util.Collection;
import java.util.Hashtable;

/**
 * This helper class simplifies the process of mapping assessment line ids to the
 * lines themselves.<p>
 * Each
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2005/12/19 22:28:30 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/policy/AssessmentSheetList.java,v $
 */
public class AssessmentSheetList {
    private Hashtable<String,AssessmentSheet> sheets=null;

    public AssessmentSheetList(Policy policy) {
        sheets=new Hashtable<String,AssessmentSheet>();

        for(Section section: policy.getSection()) {
            sheets.put(section.getId(), section.getAssessmentSheet());
        }

        sheets.put("policy", policy.getAssessmentSheet());
    }

    /**
     * Find the assessment line with the long name specified
     * @param longId
     * @return The AssessmentLine found, or null if none is found.
     */
    public AssessmentLine findAssessmentLine(String longId) {
        int dot=longId.indexOf('.');

        // If there's no '.', this isn't a longId
        if (dot==-1) {
            return null;
        }

        // e.g. longId="policy.base premium": sheetName="policy", lineName="base premium"
        String sheetName=longId.substring(0, dot);
        String lineName=longId.substring(dot+1);

        AssessmentSheet sheet=(AssessmentSheet)sheets.get(sheetName);

        if (sheet==null) {
            return null;
        }

        AssessmentLine line=sheet.findLineById(lineName);

        return line;
    }

    /**
     * Find a line by long or short id. If the line cannot be found as a long id
     * (using findAssessmentLine), this method will attempt to find the line as
     * a short name in the current Assessment Sheet.
     * @param id Line Id to search for.
     * @param current Current assessment sheet - used if id is a short name.
     * @return The assessment line found, or null if none is found.
     */
    public AssessmentLine findAssessmentLine(String id, AssessmentSheet current) {
        AssessmentLine line=findAssessmentLine(id);

        if (line==null) {
            line=current.findLineById(id);
        }

        return line;
    }

    /**
     * Add an assessment line. The line's Id may be in long or short form. If it is in
     * long form, the appropriate sheet will be added to (the line added will have a short Id).
     * If it is in short form, it is added to the current sheet.
     * @param line Line to be addded
     * @param current Current assessment sheet.
     * @throws java.lang.IllegalArgumentException If the name is in long form, and a sheet by the name cannot be found.
     */
    public void addAssessmentLine(AssessmentLine line, AssessmentSheet current) throws IllegalArgumentException {
        int dot=line.getId().indexOf('.');

        // If there's no '.' in the line's Id add it to the current sheet.
        if (dot==-1) {
            current.addLine(line);
        }
        else {
            // e.g. longId="policy.base premium": sheetName="policy", lineName="base premium"
            String sheetName=line.getId().substring(0, dot);
            String lineName=line.getId().substring(dot+1);

            AssessmentSheet as=(AssessmentSheet)sheets.get(sheetName);
            if (as==null) {
                throw new IllegalArgumentException("Assessment sheet (id="+sheetName+"does not exist");
            }

            line.setId(lineName);
            as.addLine(line);
        }
    }

    /**
     * Return a collection of sheets.
     * @return Collection of instances of AssessmentSheet.
     */
    public Collection<AssessmentSheet> getSheets() {
        return sheets.values();
    }
}
