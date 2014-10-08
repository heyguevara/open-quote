/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.openquote;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.drools.spi.KnowledgeHelper;

import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.core.TypeXPathException;

/**
 * This class defines a set of unrelated functions which are intended to be
 * used from within Drools rulesets. 
 */
public class Functions {

    /**
     * Calculate the number of whole years between the specified date and 'when'. Typically,
     * this is used to calculate the age of something/someone, given that date represents
     * the date of creation or birth.
     * @param date Attribute of type date describing the date of creation or birth
     * @param when Date to calculate the age at
     * @return The number of whole years between 'date' and 'when'.
     * @throws Exception
     */
    public static Integer yearsSince(Attribute date, Date when) {
        return yearsSince("yearsSince(Attribute, Date)", date, when);
    }
    
    /**
     * Calculate the number of whole years between the specified date and 'when'. Typically,
     * this is used to calculate the age of something/someone, given that date represents
     * the date of creation or birth.
     * @param date date of creation or birth
     * @param when Date to calculate the age at
     * @return The number of whole years between 'date' and 'when'.
     * @throws Exception
     */
    public static Integer yearsSince(Date date, Date when) {
        return yearsSince("yearsSince(Date, Date)", date, when);
    }
    
    
    /**
     * Calcuate the age of something given its date of creation (or birth).
     * @param date Attribute describing the date of birth or creation
     * @return Age in years
     */
    public static Integer ageInYears(Attribute date) {
        return yearsSince("ageInYears(Attribute)", date, new Date());
    }
    
    /**
     * Calculate then age of something at a specific date.
     * @param date Attribute describing the date of birth or creation
     * @param when Date to calculate the date at
     * @return Age in years
     */
    public static Integer ageInYearsAtDate(Attribute date, Date when) {
        return yearsSince("ageInYearsAtDate(Attribute, Date)", date, when);
    }
    
    /**
     * Given a collection of dates, calculate the youngest - closest to today.
     * @param date Attributes describing the date of birth or creation
     * @return The youngest date found in 'dates', or null if no date attributes are found in <i>dates</i>.
     * @throws Exception
     */
    public static Attribute youngestOf(Collection<Attribute> dates) throws Exception {
        Attribute youngest=null;

        if (dates==null) {
            throw new IllegalArgumentException("youngesOf(Collection<Attribute>) function called with an empty list of dates");
        }
        
        for(Attribute attr: dates) {
            if (attr.isDateType()) {
                if (youngest==null || ((Date)youngest.getObject()).before((Date)attr.getObject())) {
                    youngest=attr;
                }
            }
        }

        if (youngest==null) {
            throw new IllegalArgumentException("youngesOf(Collection<Attribute>) function called with a list of attributes that contained no dates");
        }
        
        return youngest;
    }

    /**
     * Given a collection of date attributes, calculate the oldest - furthest to today.
     * @param date Attributes describing the date of birth or creation
     * @return The oldest date in 'dates'.
     * @throws Exception
     */
    public static Attribute oldestOf(Collection<Attribute> dates) throws Exception {
        Attribute oldest=null;

        if (dates==null) {
            throw new IllegalArgumentException("oldestOf(Collection<Attribute>) function called with an empty list of dates");
        }
        
        for(Attribute attr: dates) {
            if (attr.isDateType()) {
                if (oldest==null || ((Date)attr.getObject()).before((Date)oldest.getObject())) {
                    oldest=attr;
                }
            }
        }

        if (oldest==null) {
            throw new IllegalArgumentException("oldestOf(Collection<Attribute>) function called with a list of attributes that contained no dates");
        }
        
        return oldest;
   }
    
    /**
     * Assert a new fact into dools working memory. The fact to be asserted is derived by evaluating an
     * xpath expression against a source object.
     * @param drools KnowledgeHelper to assert the new fact into.
     * @param name Name for facts 
     * @param source Object to evaluate xpath against
     * @param xpath Xpath itself
     */
    @SuppressWarnings("unchecked")
    public static void assertFact(KnowledgeHelper drools, String name, Type source, String xpath) {
       try {
           Iterator it=source.xpathIterate(xpath);
       
           while(it.hasNext()) {
               drools.insert(new Fact(name, it.next()));
           }
       }
       catch(TypeXPathException e) {
           CoreProxy cp=new CoreProxy();
           cp.logInfo("Thread: "+Thread.currentThread().getId()+" Q:"+((Quotation)source).getQuotationNumber());
           cp.logWarning("XPath evaluation failed to return a result: "+xpath+" reason: "+e.getDescription());
       }
   }

    private static Integer yearsSince(String called, Attribute date, Date when) {
        if (!date.isDateType()) {
            throw new IllegalArgumentException(called+" call with an attribute of type: "+date.getFormat()+". Only attributes of type date are valid.");
        }

        return yearsSince(called, (Date)date.getObject(), when);
    }
    
    private static Integer yearsSince(String called, Date date, Date when) {        
        if (date==null) {
            throw new IllegalArgumentException(called+" function called with a null 'date' attribute.");
        }
        if (when==null) {
            throw new IllegalArgumentException(called+" function called with a null 'when' argument");
        }
        
        // create a calendar to represent the date of birth (or whatever)
        Calendar then=Calendar.getInstance();
        then.setTime(date);
        
        // create a calendar for now
        Calendar now=Calendar.getInstance();
        now.setTime(when);
        
        // age is the difference in years between then and now
        int age=now.get(Calendar.YEAR) - then.get(Calendar.YEAR);
        
        // if the anniversay hasn't yet passed this year, subtract 1
        then.add(Calendar.YEAR, age);        
        if (now.before(then)) {
            age--;
        }
        
        return new Integer(age);
    }

    @SuppressWarnings("unchecked")
    public static Object test(Object c) {
        return ((List)c).size()!=0;
    }
}
