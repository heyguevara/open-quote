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

package com.ail.commercialtest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.ail.party.Address;
import com.ail.party.Person;
import com.ail.party.Title;

public class TestParty {
    /**
     * Test valid arguments for the Rate constructor
     * @throws Exception
     */
    @Test
    public void testGoodFormats() throws Exception {
        Address addr=new Address();
        
        addr.setLine1("The House");
        addr.setLine2("The Road");
        addr.setTown("Town");
        addr.setCounty("County");
        addr.setPostcode("ABC 1DE");
        
        assertEquals("The House, The Road, Town, County. ABC 1DE", addr.toString());
    }
    
    @Test
    public void testOtherTitle() throws Exception {
        Person p=new Person();
        
        p.setTitle(Title.DR);
        p.setFirstName("Jimbo");
        p.setSurname("Clucknasty");
        assertEquals("Dr. Jimbo Clucknasty", p.getLegalName());
        
        p.setTitle(Title.OTHER);
        p.setOtherTitle("Lord");
        assertEquals("Lord Jimbo Clucknasty", p.getLegalName());
        
    }

}
