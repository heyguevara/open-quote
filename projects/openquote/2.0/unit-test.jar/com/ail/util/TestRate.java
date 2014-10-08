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

package com.ail.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.ail.util.Rate;

import java.math.BigDecimal;

import org.junit.Test;

public class TestRate {

    /**
     * Test valid arguments for the Rate constructor
     * 
     * @throws Exception
     */
    @Test
    public void testGoodFormats() throws Exception {
        new Rate("3/20");
        new Rate("10%");
        new Rate("10percent");
        new Rate("10PeRcEnT");
        new Rate("100PERMIL");
        new Rate("100Permil");
        new Rate("0.5");
    }
    
    /**
     * Test how badly formatted arguments are handled by the constructor.
     */
    @Test(expected= IllegalArgumentException.class)
    public void testBadStringAlpha() {
        new Rate("abcd");
    }

    @Test(expected= IllegalArgumentException.class)
    public void testRateMoreThanOne() {
        new Rate("1.1");
    }

    @Test(expected= IllegalArgumentException.class)
    public void testRateLessThanZero() {
        new Rate("-0.1");
    }

    @Test(expected= IllegalArgumentException.class)
    public void testBadStringNumber() {
        new Rate("1.1.1");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatsMoreThan100() {
        new Rate("101%");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatsLessThanZero() {
        new Rate("-1%");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatsBadRate() {
        new Rate("10plop");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatsMoreThan1000Mil() {
        new Rate("1001permil");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBadFormatsLessThanZeroMill() {
        new Rate("-1permil");
    }

    @Test
    public void testRateCalculations() {
        Rate rate;

        rate = new Rate("10%");
        assertEquals("10%", rate.getRate());
        assertEquals("100", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(100.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("100"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));

        rate = new Rate("10Percent");
        assertEquals("10Percent", rate.getRate());
        assertEquals("100", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(100.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("100"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));

        rate = new Rate("10Permil");
        assertEquals("10Permil", rate.getRate());
        assertEquals("10", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(10.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("10"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));

        // try a fraction
        rate = new Rate("3/20");
        assertEquals("3/20", rate.getRate());
        assertEquals("150", rate.applyTo("1000", 0, BigDecimal.ROUND_HALF_UP));
        assertTrue(150.0 == rate.applyTo(1000, 0, BigDecimal.ROUND_HALF_UP));
        assertEquals(new BigDecimal("150"), rate.applyTo(new BigDecimal(1000), 0, BigDecimal.ROUND_HALF_UP));

        rate = new Rate("10.5%");
        assertEquals("10.5%", rate.getRate());
        assertEquals("1.1", rate.applyTo("10", 1, BigDecimal.ROUND_HALF_UP));
        
        rate = new Rate("0.5");
        assertEquals("0.5", rate.getRate());
        assertEquals("25", rate.applyTo("50", 0, BigDecimal.ROUND_HALF_UP));
    }
}
