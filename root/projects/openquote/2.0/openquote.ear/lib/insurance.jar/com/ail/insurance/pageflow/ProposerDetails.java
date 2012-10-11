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
package com.ail.insurance.pageflow;

import static com.ail.insurance.pageflow.util.Functions.addError;
import static com.ail.insurance.pageflow.util.Functions.isEmpty;

import java.io.IOException;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.pageflow.util.Functions;
import com.ail.insurance.policy.CommercialProposer;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.Proposer;
import com.ail.party.Title;

/**
 * <p>Page element to capture the proposer's party details. The element has two distinct modes: Personal or Commercial. The actual mode
 * used for a given quotation depends on the type of the class returned by quotation.getProposer().</p>
 * <p><img src="doc-files/ProposerDetails.png"/></p>
 * <p><img src="doc-files/ProposerDetailsCommercial.png"/></p>
 * <p>This page element applies the following validation rules:<ul>
 * <li>A value (other than "Title?") must be selected in the "Title" drop-down.</li>
 * <li>If a "Title" of "Other" is selected in the drop down, a value must be entered in "Other title".</li>
 * <li>Both "First name" and "Surname" must be supplied and must match the regular expression: ^[\\p{L}\\p{N}-,. ()]*$</li>
 * <li>Address lines 1 & 2 must be supplied and must match the regular expression above.</li>
 * <li>Address lines 3 & 4 are optional, but if supplied must match the regular expression above.</li>
 * <li>Postcode must be supplied, and must match the regular expression: ^[a-zA-Z0-9 -]*$</li>
 * <li>Telephone number must be supplied, and must match the regular expression: (^[+()0-9 -]*$)|(^[+()0-9 -]*[extEXT]{0,3}[ ()0-9]*$)</li>
 * <li>Mobile phone number are optional and must match the regular expression: (^[+()0-9 -]*$)</li>
 * <li>Email address must be supplied, and must match the regular expression: ^[0-9a-zA-Z.-]*@[0-9a-zA-Z.-]*[.][0-9a-zA-Z.-]*$</li>
 * <li>If in Commercial mode, company name must be supplied and must match the regular expression: ^[\\p{L}\\p{N}-,. ()]*$.</li>
 * </ul>
 */
public class ProposerDetails extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;
    private static final Pattern namePattern=Pattern.compile("^[\\p{L}\\p{N}-,. &()]*$");
    private static final Pattern postcodePattern=Pattern.compile("^[a-zA-Z0-9 -]*$");
    private static final Pattern emailPattern=Pattern.compile("^[0-9a-zA-Z.-]*@[0-9a-zA-Z.-]*[.][0-9a-zA-Z.-]*$");
    private static final Pattern mobilePattern=Pattern.compile("(^[+()0-9 -]*$)");
    private static final Pattern phonePattern=Pattern.compile("(^[+()0-9 -]*$)|(^[+()0-9 -]*[extEXT]{0,3}[ ()0-9]*$)");

	public ProposerDetails() {
		super();
	}

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        Proposer proposer=(Proposer)((Policy)model).getProposer();

        proposer.setTitle(Title.forName(request.getParameter("title")));
        proposer.setOtherTitle(request.getParameter("otherTitle"));
        proposer.setFirstName(request.getParameter("firstname"));
        proposer.setSurname(request.getParameter("surname"));
        proposer.getAddress().setLine1(request.getParameter("address1"));
        proposer.getAddress().setLine2(request.getParameter("address2"));
        proposer.getAddress().setTown(request.getParameter("town")); 
        proposer.getAddress().setCounty(request.getParameter("county"));
        proposer.getAddress().setPostcode(request.getParameter("postcode"));
        proposer.setTelephoneNumber(request.getParameter("phone"));
        proposer.setMobilephoneNumber(request.getParameter("mobile"));
        proposer.setEmailAddress(request.getParameter("email"));
        
        if (proposer instanceof CommercialProposer) {
        	((CommercialProposer)proposer).setCompanyName(request.getParameter("companyName"));
        }
        
        return model;
    }

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        Proposer proposer=(Proposer)((Policy)model).getProposer();
        
        Functions.removeErrorMarkers(proposer.getInstance());
        
        // Check the proposer for errors.
        if (Title.UNDEFINED.equals(proposer.getTitle())) {
            addError("title", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (Title.OTHER.equals(proposer.getTitle()) && isEmpty(proposer.getOtherTitle())) {
            addError("otherTitle", i18n("i18n_required_error"), proposer.getInstance());
        }

        if (isEmpty(proposer.getFirstName())) {
        	addError("firstName", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!namePattern.matcher(proposer.getFirstName()).find()) {
        	addError("firstName", i18n("i18n_invalid_error"), proposer.getInstance());
        }
        
        if (isEmpty(proposer.getSurname())) {
        	addError("surname", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!namePattern.matcher(proposer.getSurname()).find()) {
        	addError("surname", i18n("i18n_invalid_error"), proposer.getInstance());
        }
        
        if (isEmpty(proposer.getAddress().getLine1())) {
        	addError("address1", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!namePattern.matcher(proposer.getAddress().getLine1()).find()) {
        	addError("address1", i18n("i18n_invalid_error"), proposer.getInstance());
        }
        
        if (isEmpty(proposer.getAddress().getLine2())) {
        	addError("address2", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!namePattern.matcher(proposer.getAddress().getLine2()).find()) {
        	addError("address2", i18n("i18n_invalid_error"), proposer.getInstance());
        }

        if (!isEmpty(proposer.getAddress().getLine3()) && !namePattern.matcher(proposer.getAddress().getLine3()).find()) {
        	addError("address3", i18n("i18n_invalid_error"), proposer.getInstance());
        }

        if (!isEmpty(proposer.getAddress().getLine4()) && !namePattern.matcher(proposer.getAddress().getLine4()).find()) {
        	addError("address4", i18n("i18n_invalid_error"), proposer.getInstance());
        }

        if (isEmpty(proposer.getAddress().getPostcode())) {
        	addError("postcode", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!postcodePattern.matcher(proposer.getAddress().getPostcode()).find()) {
        	addError("postcode", i18n("i18n_invalid_error"), proposer.getInstance());
        }

        if (isEmpty(proposer.getTelephoneNumber())) {
        	addError("phone", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!phonePattern.matcher(proposer.getTelephoneNumber()).find()) {
        	addError("phone", i18n("i18n_invalid_error"), proposer.getInstance());
        }

        if (!mobilePattern.matcher(proposer.getMobilephoneNumber()).find()) {
        	addError("mobile", i18n("i18n_invalid_error"), proposer.getInstance());
        }

        if (isEmpty(proposer.getEmailAddress())) {
        	addError("email", i18n("i18n_required_error"), proposer.getInstance());
        }
        else if (!emailPattern.matcher(proposer.getEmailAddress()).find()) {
        	addError("email", i18n("i18n_invalid_error"), proposer.getInstance());
        }
        
        if (proposer instanceof CommercialProposer) {
        	String companyName=((CommercialProposer)proposer).getCompanyName();
        	if (isEmpty(companyName)) {
        		addError("companyName", i18n("i18n_required_error"), proposer.getInstance());
        	}
            else if (!namePattern.matcher(companyName).find()) {
            	addError("companyName", i18n("i18n_invalid_error"), proposer.getInstance());
            }
        }
        return Functions.hasErrorMarkers(proposer.getInstance());
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand("ProposerDetails", request, response, model);
	}
}
