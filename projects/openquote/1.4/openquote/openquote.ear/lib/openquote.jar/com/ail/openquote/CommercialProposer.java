/* Copyright Applied Industrial Logic Limited 2008. All rights Reserved */
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

import com.ail.annotation.TypeDefinition;
import com.ail.core.Type;
import com.ail.party.Organisation;
import com.ail.party.Person;
import com.ail.party.Title;

/**
 * Represents the organisation that the quotation was prepared for. Generally this type is appropriate
 * for use in commercial lines.
 * @see PersonalProposer
 */
@TypeDefinition
public class CommercialProposer extends Organisation implements Proposer {
	private static final long serialVersionUID = 1L;

	private String emailAddress;
	private String telephoneNumber;
	private String mobilephoneNumber;
	private Person contact;
	
	public String getCompanyName() {
		return getLegalName();
	}

	public void setCompanyName(String companyName) {
		setLegalName(companyName);
	}

	public CommercialProposer() {
		contact=new Person();
	}
	
	public String getEmailAddress() {
		return emailAddress;
	}
	
	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}
	
	public String getTelephoneNumber() {
		return telephoneNumber;
	}
	
	public void setTelephoneNumber(String telephoneNumber) {
		this.telephoneNumber = telephoneNumber;
	}

	public String getActualTitle() {
		return contact.getActualTitle();
	}

	public String getFirstName() {
		return contact.getFirstName();
	}

	public String getOtherTitle() {
		return contact.getOtherTitle();
	}

	public String getSurname() {
		return contact.getSurname();
	}

	public Title getTitle() {
		return contact.getTitle();
	}

	public String getTitleAsString() {
		return contact.getTitleAsString();
	}

	public void setFirstName(String firstName) {
		contact.setFirstName(firstName);
	}

	public void setOtherTitle(String otherTitle) {
		contact.setOtherTitle(otherTitle);
	}

	public void setSurname(String surname) {
		contact.setSurname(surname);
	}

	public void setTitle(Title title) {
		contact.setTitle(title);
	}

	public void setTitleAsString(String title) throws IndexOutOfBoundsException {
		contact.setTitleAsString(title);
	}
	
	public Person getContact() {
		return contact;
	}

	public void setContact(Person contact) {
		this.contact = contact;
	}

	public Type getInstance() {
		return this;
	}

	public String getMobilephoneNumber() {
		return mobilephoneNumber;
	}

	public void setMobilephoneNumber(String mobilephoneNumber) {
		this.mobilephoneNumber = mobilephoneNumber;
	}
}
