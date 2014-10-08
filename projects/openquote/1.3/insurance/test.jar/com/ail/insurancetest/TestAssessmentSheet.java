/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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

package com.ail.insurancetest;

import junit.framework.Test;
import junit.framework.TestCase;

import com.ail.financial.Currency;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.DuplicateAssessmentLineError;
import com.ail.insurance.policy.FixedSum;
import com.ail.insurance.policy.Marker;
import com.ail.insurance.policy.MarkerType;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.util.Rate;

/**
 * The assessment sheet is at the center of the quotation process. It can be thought of as
 * a restricted spreadsheet. Entries can be absolute amounts (Sums) or rates (Percentage). Each entry may
 * depend on a maximum of one other entry, and may contribute to another. Most of what the calculatePremium
 * service does is to run through this table processing it and producing results from it.
 */
public class TestAssessmentSheet extends TestCase {
    /**
     * Constructs a test case with the given name.
     * @param name The tests name
     */
    public TestAssessmentSheet(String name) {
        super(name);
    }

    /**
     * Create an instance of this test case as a TestSuite.
     * @return Test an instance of this test case.
     */
    public static Test suite() {
        return new junit.framework.TestSuite(TestAssessmentSheet.class);
    }

    /**
     * Run this testcase from the command line.
     * @param args No command line args are required.
     */
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    /**
     * Test summary methods and simple line additions
     */
    public void testSimpleManipulation() {
        AssessmentSheet as=new AssessmentSheet();

        as.setLockingActor("me");

        // should be no line in the sheet
        assertEquals(0, as.getLineCount());

        as.addLine(new FixedSum("base premium", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
        as.addLine(new RateBehaviour("load1", "No reason at all", null, "final premium", "base premium", BehaviourType.LOAD, new Rate("15%")));

        // should be two entries now, but no refers or declines
        assertEquals(2, as.getLineCount());
        assertTrue(!as.isMarkedForDecline());
        assertTrue(!as.isMarkedForRefer());

        // add a refer
        as.addLine(new Marker("ref1", "Just a bad day", null, MarkerType.REFER));

        // Three entries now, marked for refer, but not decline
        assertEquals(3, as.getLineCount());
        assertTrue(!as.isMarkedForDecline());
        assertTrue(as.isMarkedForRefer());

        // get some lines by name (id)
        assertNotNull(as.findLineById("base premium"));
        assertNotNull(as.findLineById("load1"));
        assertNotNull(as.findLineById("ref1"));
        assertNull(as.findLineById("hello!"));
    }

    /**
     * Test assessment sheet double lock. Assessment sheets are locked to an actor before lines can be added.
     * This test ensures that two actors can lock the same sheet, but that a sheet can be locked by the same
     * actor more than once.
     * <ol>
     * <li>Create an assessment sheet</li>
     * <li>Lock the sheet to the actor "me"</li>
     * <li>Attempt to lock the sheet for the actor "you"</li>
     * <li>Fail if an IllegalStateException isn't thrown</li>
     * <li>File if any other exception is thrown</li>
     * <li>Lock the sheet as the actor "me" again</li>
     * <li>Fail if any exceptions af thrown</li>
     * </ol>
     * @throws Exception
     */
    public void testAssessmentSheetDoubleLock() throws Exception {
        AssessmentSheet as=new AssessmentSheet();

        as.setLockingActor("me");

        try {
            as.setLockingActor("you");
            fail("locked sheet twice");
        }
        catch(IllegalStateException e) {
            // good
        }

        as.setLockingActor("me");
    }

    /**
     * Check that lines can only be added to the sheet if either the sheet is locked to an actor, or
     * the lines already have an origin.
     * <ol>
     * <li>Create an assessment sheet</li>
     * <li>Add a line to the sheet (without locking first)</li>
     * <li>Fail if an IllegalStateException isn't thrown</li>
     * <li>Fail if any other exception is thrown</li>
     * <li>Lock the assessment sheet to "me"</li>
     * <li>Try to add the line again</li>
     * <li>Fail if any exceptions are thrown</li>
     * <li>Clear the lock</li>
     * <li>Create a FixedSum line, and set its origin to "me"</li>
     * <li>Try to add the line</li>
     * <li>Fail if any exceptions are thrown</li>
     * </ol>
     */
    public void testAddLineWithoutLock() throws Exception {
        AssessmentSheet as=new AssessmentSheet();

        try {
            as.addLine(new FixedSum("w1", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
            fail("added a line without a lock!");
        }
        catch(IllegalStateException e) {
            // good
        }

        as.setLockingActor("me");

        as.addLine(new FixedSum("w2", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));

        as.clearLockingActor();

        FixedSum sum=new FixedSum("w3", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP));
        sum.setOrigin("me");

        as.addLine(sum);
    }


    /**
     * Test the removal of lines by actor. An actor can remove all of their lines from an assessment sheet
     * using the <code>removeLinesByOrigin</code> method. This should remove all of that actor's lines,
     * but not touch any of the others.
     * <ol>
     * <li>Create an assessment sheet</li>
     * <li>Set the locking actor to "me"</li>
     * <li>Add two lines to the sheet</li>
     * <li>Check that the sheet has two lines (get the count)</li>
     * <li>Unlock the sheet, and re-lock as "you"</li>
     * <li>Add three further lines to the sheet</li>
     * <li>Check that the sheet has five lines</li>
     * <li>Unlock the sheet</li>
     * <li>use 'removeLInesByOrigin' to remove all of the lines created by "me"</li>
     * <li>Check that the sheet has three lines</li>
     * <li>Check that the lines are those added by "you" - by checking their IDs</li>
     * </ol>
     */
    public void testRemoveLinesByActor() {
        AssessmentSheet as=new AssessmentSheet();

        as.setLockingActor("me");

        as.addLine(new FixedSum("l1", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
        as.addLine(new FixedSum("l2", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
        assertEquals(2, as.getLineCount());

        as.clearLockingActor();
        as.setLockingActor("you");

        as.addLine(new FixedSum("l3", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
        as.addLine(new FixedSum("l4", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
        as.addLine(new FixedSum("l5", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
        assertEquals(5, as.getLineCount());

        as.clearLockingActor();

        as.removeLinesByOrigin("me");
        assertEquals(3, as.getLineCount());

        assertNotNull(as.findLineById("l3"));
        assertNotNull(as.findLineById("l4"));
        assertNotNull(as.findLineById("l5"));
    }

    /**
     * Check that duplicate lines are detected. An assessment sheet cannot contain two assessment lines
     * with the same id. This should be detected when an attempt is made to add the second line. This test
     * ensures that that is the case.
     * <ol>
     * <li>Create an assessment sheet</li>
     * <li>Lock the sheet to actor "me"</li>
     * <li>Add a line with the id "l1"</li>
     * <li>Fail if any exceptions are thrown</li>
     * <li>Attempt to add another line with the id "l1"</li>
     * <li>Fail if a DuplicateAssessmentSheetLineError isn't thrown</li>
     * <li>Fail if any other exceptions are thrown.</li>
     * <li>Fail is no exception is thrown</li>
     * </ol>
     * @throws Exception
     */
    public void testDuplicateLineDetection() throws Exception {
        AssessmentSheet as=new AssessmentSheet();

        as.setLockingActor("me");

        as.addLine(new FixedSum("l1", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));

        try {
            as.addLine(new FixedSum("l1", "calculate value", null, "final premium", new CurrencyAmount("100.00", Currency.GBP)));
            fail("added to lines with the same ID");
        }
        catch(DuplicateAssessmentLineError e) {
            // good!
        }
    }
}
