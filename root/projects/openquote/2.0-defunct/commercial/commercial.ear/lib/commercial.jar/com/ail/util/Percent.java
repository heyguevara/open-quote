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

import com.ail.core.Type;

/**
 * This class represents a percentage value based on a double.
 * todo A rate should be between 0 and 100 not 0 and 1
 * todo The rate should be represented as a BigDecimal - not a double.
 * @deprecated Use com.ail.util.Rate instead.
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2005/08/16 21:08:54 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/util/Percent.java,v $
 * @stereotype type
 */
public class Percent extends Type {
    static final long serialVersionUID = -2314786106419874334L;
    private double rate;

    /**
     * Default constructor
     */
    public Percent() {
    }

    /**
     * Initialising constructor
     * @param rate The rate to be represented
     * @throws IllegalArgumentException If rate is not >=0 && <=1
     */
    public Percent(double rate) throws IllegalArgumentException {
        setRate(rate);
    }

    /**
     * Get the current rate.
     * @return Rate as a double
     */
    public double getRate() {
        return rate;
    }

    /**
     * Set the rate.
     * @param rate New rate.
     * @throws IllegalArgumentException If rate argument is not >=0 && <=1
     */
    public void setRate(double rate) throws IllegalArgumentException {
        if (rate < 0 || rate >100.0) {
            throw new IllegalArgumentException("Percent value can only represent 0 <= value <= 100 (value was "+rate+")");
        }

        this.rate = rate;
    }

    /**
     * Apply this percentage to a value
     * @param value The value to apply to
     * @return The result
     */
    public double applyTo(double value) {
        return value*(rate/100.0);
    }
}
