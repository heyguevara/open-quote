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

package com.ail.financial;

/**
 * @version $Revision: 1.1 $
 * @state $State: Exp $
 * @date $Date: 2006/04/21 19:57:33 $
 * @source $Source: /home/bob/CVSRepository/projects/common/commercial.ear/commercial.jar/com/ail/financial/DirectDebit.java,v $
 * @stereotype type
 */
public class DirectDebit extends PaymentMethod {
    private static final long serialVersionUID = 1L;

    /** The bank account's number */
    private String accountNumber;

    /** The account's sort code */
    private String sortCode;

    public DirectDebit() {
        super();
    }

    public DirectDebit(String accountNumber, String sortCode) {
        super();
        this.accountNumber = accountNumber;
        this.sortCode = sortCode;
    }

    /**
     * Getter returning the value of the accountNumber property. The bank account's number
     * @return Value of the accountNumber property
     */
    public String getAccountNumber() {
        return accountNumber;
    }

    /**
     * Setter to update the value of the accountNumber property. The bank account's number
     * @param accountNumber New value for the accountNumber property
     */
    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    /**
     * Getter returning the value of the sortCode property. The account's sort code
     * @return Value of the sortCode property
     */
    public String getSortCode() {
        return sortCode;
    }

    /**
     * Setter to update the value of the sortCode property. The account's sort code
     * @param sortCode New value for the sortCode property
     */
    public void setSortCode(String sortCode) {
        this.sortCode = sortCode;
    }
}
