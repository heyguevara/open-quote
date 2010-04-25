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

package com.ail.insurance.quotation.refreshassessmentsheets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import com.ail.core.Core;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.AssessmentLine;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.AssessmentSheetList;
import com.ail.insurance.policy.CalculationLine;
import com.ail.insurance.policy.AssessmentStage;
import com.ail.insurance.policy.Policy;

/**
 */
public class RefreshAssessmentSheetsService extends Service {
    private static final long serialVersionUID = 3642886757932052003L;
    private RefreshAssessmentSheetsArg args = null;
    private Core core = null;

    /** Default constructor */
    public RefreshAssessmentSheetsService() {
        core = new com.ail.core.Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2003. All rights reserved.");
        v.setDate("$Date: 2007/02/12 23:10:04 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/refreshassessmentsheets/RefreshAssessmentSheetsService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.3 $");
        return v;
    }

    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (RefreshAssessmentSheetsArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of RefreshAssessmentSheetsArgs.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Process the contents of an AssessmentSheet. Run through the contents of the sheet checking each line
     * in order of priority. If a line hasn't already been calculated (i.e. it doesn't appear in the <i>processed</i>
     * collection) then it's <i>calculate()</i> method is called. If that call returns <i>true</i> the line is assumed
     * to have been processed and will be added to the <i>processed<i> list and marked with an integer indicating the 
     * order it was processed in relation to other lines.
     * @param sheet The sheet to process
     * @param sheets Collection of all sheets being processed
     * @param processed Collection of the AssessmentLines that have been processed so far.
     * @param calculationCountOffset Index to start numbering processed lines from. 
     * @return true If any line is processed, false otherwise
     */
    private int processSheet(AssessmentSheet sheet, AssessmentSheetList sheets, Collection<AssessmentLine> processed, int calculationCountOffset) {
        int processedLineCount=calculationCountOffset+1;
        CalculationLine cl=null;
        ArrayList<CalculationLine> sortedLines=null;

        // get the CalculationLines in priority order
        sortedLines=new ArrayList<CalculationLine>(sheet.getLinesOfType(CalculationLine.class).values());
        Collections.sort(sortedLines);
        
        // make sure that each line knows about the assessment sheet it is in
        for(CalculationLine line: sortedLines) {
            line.setAssessmentSheet(sheet);
        }
        
        // iterate through the lines
        for(Iterator<CalculationLine> it=sortedLines.iterator() ; it.hasNext() ; ) {
            cl=it.next();

            // if we've processed this line already, skip to the next.
            if (processed.contains(cl)) {
                continue;
            }

            // calculate the line, if we get a true back then the line has been done
            if (cl.calculate(sheets, sheet)==true) {
                processed.add(cl);
                cl.setCalculatedOrder(processedLineCount++);
            }
        }

        return processedLineCount;
    }

    private void processAssessmentSheets(Policy policy) {
        boolean again=false;
        int calculationOrder=0;
        AssessmentSheetList sheets=new AssessmentSheetList(policy);
        Collection<AssessmentLine> processed=new ArrayList<AssessmentLine>();

        // execute all the control lines appropriate to the before phase
        sheets.executeControlLinesForAssessmentStage(AssessmentStage.BEFORE_RATING);

        // the following loop performs all the rating calcs, so set the phase accordingly
        sheets.setAssessmentStage(AssessmentStage.RATING);
        
        do {
            again=false;

            for(Iterator<AssessmentSheet> it=sheets.getSheets().iterator() ; it.hasNext() ; ) {
                
                int count=processSheet(it.next(), sheets, processed, calculationOrder);
                
                if (count!=0) {
                    calculationOrder+=count;
                    again=true;
                }
            }
        } while(again==true);

        // execute all the control lines appropriate to the before phase
        sheets.executeControlLinesForAssessmentStage(AssessmentStage.AFTER_RATING);
    }

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException {
        // check preconditions
        if (args.getPolicyArgRet()==null) {
            throw new PreconditionException("args.getPolicyArgRet()==null");
        }

        if (args.getOriginArg()==null || args.getOriginArg().length()==0) {
            throw new PreconditionException("args.getOriginArg()==null || args.getOriginArg().length()==0");
        }

        Policy policy=args.getPolicyArgRet();

        // remove only lines that this origin added in the past
        policy.removeAssessmentLinesByOrigin(args.getOriginArg());

        // lock the sheets to this origin
        policy.lockAllAssessmentSheets(args.getOriginArg());

        // perform the refresh
        processAssessmentSheets(args.getPolicyArgRet());

        // unlock the assessment sheets
        policy.unlockAllAssessmentSheets();
    }
}


