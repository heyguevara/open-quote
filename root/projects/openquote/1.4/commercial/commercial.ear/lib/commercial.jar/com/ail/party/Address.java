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

package com.ail.party;

import com.ail.core.Type;

/**
 * Class to encapsulate the details of a postal address.
 * @version $Revision: 1.2 $
 * @state $State: Exp $
 * @date $Date: 2006/05/23 21:24:56 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/party/Address.java,v $
 * @stereotype type
 */
public class Address extends Type {
    static final long serialVersionUID = 6426298969158775043L;
    private String line1;
    private String line2;
    private String line3;
    private String line4;
    private String line5;
    private String town;
    private String county;
    private String country;
    private String postcode;

    public String getLine1() {
        return line1;
    }

    public void setLine1(String line1) {
        this.line1 = line1;
    }

    public String getLine2() {
        return line2;
    }

    public void setLine2(String line2) {
        this.line2 = line2;
    }

    public String getLine3() {
        return line3;
    }

    public void setLine3(String line3) {
        this.line3 = line3;
    }

    public String getLine4() {
        return line4;
    }

    public void setLine4(String line4) {
        this.line4 = line4;
    }

    public String getLine5() {
        return line5;
    }

    public void setLine5(String line5) {
        this.line5 = line5;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    private String nn(String s) {
        return (s==null) ? "" : s.trim();
    }
    
    /**
     * Return the address formatted on a single line with comma seperated fields.
     * @return formatted address
     */
    public String toString() {
        // Take some care not to mess up any formatting in the fields. If line1 was "Small Fellows, Flat 2b" we don't want to mess
        // with the internal ",".
        String ret=nn(line1)+",\07"+nn(line2)+",\07"+nn(line3)+",\07"+nn(line4)+",\07"+nn(line5)+",\07"+nn(town)+",\07"+nn(county)+",\07"+nn(country)+".\07"+nn(postcode);

        return ret.replaceAll("\07,", "").replaceAll(",\07[.]\07", ". ").replaceAll("\07", " ");
    }
}
