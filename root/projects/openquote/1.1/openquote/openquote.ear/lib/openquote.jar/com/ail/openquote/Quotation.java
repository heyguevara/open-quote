/* Copyright Applied Industrial Logic Limited 2005. All rights Reserved */
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

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import com.ail.insurance.policy.Policy;
import com.ail.financial.PaymentSchedule;
import com.ail.party.Party;
import com.ail.core.ExceptionRecord;

/**
 * A quotation can generally be represented as an instance of a Policy at the quotation state, however
 * in the context of the openquote system a policy can only exist up to the quoted/referred/declined 
 * states and hence never becomes a policy. For that reason, this sub-type of Policy was created. It
 * also includes some additional properties which are specific to the openquote system.
 */
public class Quotation extends Policy {
	private static final long serialVersionUID = -414267648671599243L;
	private Party proposer;
    private Broker broker;
	private String page=null; // name of the page in the product wizard where the quote currently is
	private Date quotationDate;
    private Date quotationExpiryDate;
    private ArrayList<PaymentSchedule> paymentOption;
    private String username;
    private boolean userSaved; // true if the user requested that this quote be saved
    private boolean testCase; // true if this quote has been saved as a test case
    private Collection<ExceptionRecord> exception; // details of all exceptions thrown during the processing of this quote.
    private Collection<PageVisit> pageVisit;
    
    public Quotation() {
        paymentOption=new ArrayList<PaymentSchedule>(0);
        exception=new ArrayList<ExceptionRecord>(0);
        pageVisit=new ArrayList<PageVisit>(0);
    }
    
    public Party getProposer() {
		return proposer;
	}

	public void setProposer(Party proposer) {
		this.proposer = proposer;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		if (this.page!=null) {
			addPageVisit(new PageVisit(this.page));
		}
		
		this.page = page;
	}

    public Date getQuotationDate() {
        return quotationDate;
    }

    public String getQuotationDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(getQuotationDate());
    }

    public void setQuotationDate(Date quotationDate) {
        this.quotationDate = quotationDate;
    }

    public Date getQuotationExpiryDate() {
        return quotationExpiryDate;
    }

    public void setQuotationExpiryDate(Date quotationExpiryDate) {
        this.quotationExpiryDate = quotationExpiryDate;
    }

    public String getQuotationExpiryDateAsString() {
        return DateFormat.getDateInstance(DateFormat.SHORT).format(getQuotationExpiryDate());
    }

    public ArrayList<PaymentSchedule> getPaymentOption() {
        return paymentOption;
    }

    public void setPaymentOption(ArrayList<PaymentSchedule> paymentOption) {
        this.paymentOption = paymentOption;
    }

    public Broker getBroker() {
        return broker;
    }

    public void setBroker(Broker broker) {
        this.broker = broker;
    }

    public boolean isUserSaved() {
        return userSaved;
    }

    public void setUserSaved(boolean userSaved) {
        this.userSaved = userSaved;
    }
    
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public boolean isTestCase() {
        return testCase;
    }

    public void setTestCase(boolean testCase) {
        this.testCase = testCase;
    }

	public Collection<ExceptionRecord> getException() {
		if (exception==null) {
	        exception=new ArrayList<ExceptionRecord>(0);
		}
		return exception;
	}

	public void setException(Collection<ExceptionRecord> exception) {
		this.exception = exception;
	}
	
	public void addException(ExceptionRecord exception) {
		getException().add(exception);
	}

	public Collection<PageVisit> getPageVisit() {
		return pageVisit;
	}

	public void setPageVisit(Collection<PageVisit> pageVisit) {
		this.pageVisit = pageVisit;
	}

	public void addPageVisit(PageVisit pageVisit) {
		this.pageVisit.add(pageVisit);
	}
}
