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
package com.ail.openquote.ui;

import static com.ail.openquote.ui.util.Functions.error;
import static com.ail.openquote.ui.util.Functions.hideNull;
import static com.ail.openquote.ui.util.Functions.isEmpty;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Pattern;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Attribute;
import com.ail.core.Type;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Functions;
import com.ail.party.Title;

/**
 * <p>Page element to capture the proposer's party details.</p>
 * <p><img src="doc-files/ProposerDetails.png"/></p>
 * <p>This page element applies the following validation rules:<ul>
 * <li>A value (other than "Title?") must be selected in the "Title" dropdown.</li>
 * <li>If a "Title" of "Other" is selected in the drop down, a value must be entered in "Other title".</li>
 * <li>Both "First name" and "Surname" must be supplied and must match the regular expression: ^[a-zA-Z0-9-,. ()]*$</li>
 * <li>Address lines 1 & 2 must be supplied and must match the regular expression above.</li>
 * <li>Address lines 3 & 4 are optional, but if supplied must match the regular expression above.</li>
 * <li>Postcode must be supplied, and must match the regular expression: ^[a-zA-Z0-9 -]*$</li>
 * <li>Telephone number must be supplied, and must match the regular expression: (^[+()0-9 -]*$)|(^[+()0-9 -]*[extEXT]{0,3}[ ()0-9]*$)</li>
 * <li>Email address must be supplied, and must match the regular expression: ^[0-9a-zA-Z.-]*@[0-9a-zA-Z.-]*[.][0-9a-zA-Z.-]*$</li>
 * </ul>
 */
public class ProposerDetails extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;
    private static final Pattern namePattern=Pattern.compile("^[a-zA-Z0-9-,. ()]*$");
    private static final Pattern postcodePattern=Pattern.compile("^[a-zA-Z0-9 -]*$");
    private static final Pattern emailPattern=Pattern.compile("^[0-9a-zA-Z.-]*@[0-9a-zA-Z.-]*[.][0-9a-zA-Z.-]*$");
    private static final Pattern phonePattern=Pattern.compile("(^[+()0-9 -]*$)|(^[+()0-9 -]*[extEXT]{0,3}[ ()0-9]*$)");
	private String title;

	public ProposerDetails() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        com.ail.openquote.Proposer proposer=((Quotation)model).getProposer();

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
        proposer.setEmailAddress(request.getParameter("email"));
    }

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    boolean error=false;
        com.ail.openquote.Proposer proposer=((Quotation)model).getProposer();
        
        Functions.removeErrorMarkers(proposer);
        
        // Check the proposer for errors.
        if (Title.UNDEFINED.equals(proposer.getTitle())) {
            proposer.addAttribute(new Attribute("error.title", "required", "string"));
            error=true;
        }
        else if (Title.OTHER.equals(proposer.getTitle()) && isEmpty(proposer.getOtherTitle())) {
            proposer.addAttribute(new Attribute("error.otherTitle", "required", "string"));
            error=true;
        }

        if (isEmpty(proposer.getFirstName())) {
            proposer.addAttribute(new Attribute("error.firstName", "required", "string"));
            error=true;
        }
        else if (!namePattern.matcher(proposer.getFirstName()).find()) {
            proposer.addAttribute(new Attribute("error.firstName", "invalid", "string"));
            error=true;
        }
        
        if (isEmpty(proposer.getSurname())) {
            proposer.addAttribute(new Attribute("error.surname", "required", "string"));
            error=true;
        }
        else if (!namePattern.matcher(proposer.getSurname()).find()) {
            proposer.addAttribute(new Attribute("error.surname", "invalid", "string"));
            error=true;
        }
        
        if (isEmpty(proposer.getAddress().getLine1())) {
            proposer.addAttribute(new Attribute("error.address1", "required", "string"));
            error=true;
        }
        else if (!namePattern.matcher(proposer.getAddress().getLine1()).find()) {
            proposer.addAttribute(new Attribute("error.address1", "invalid", "string"));
            error=true;
        }
        
        if (isEmpty(proposer.getAddress().getLine2())) {
            proposer.addAttribute(new Attribute("error.address2", "required", "string"));
            error=true;
        }
        else if (!namePattern.matcher(proposer.getAddress().getLine2()).find()) {
            proposer.addAttribute(new Attribute("error.address2", "invalid", "string"));
            error=true;
        }

        if (!isEmpty(proposer.getAddress().getLine3()) && !namePattern.matcher(proposer.getAddress().getLine3()).find()) {
            proposer.addAttribute(new Attribute("error.address3", "invalid", "string"));
            error=true;
        }

        if (!isEmpty(proposer.getAddress().getLine4()) && !namePattern.matcher(proposer.getAddress().getLine4()).find()) {
            proposer.addAttribute(new Attribute("error.address4", "invalid", "string"));
            error=true;
        }

        if (isEmpty(proposer.getAddress().getPostcode())) {
            proposer.addAttribute(new Attribute("error.postcode", "required", "string"));
            error=true;
        }
        else if (!postcodePattern.matcher(proposer.getAddress().getPostcode()).find()) {
            proposer.addAttribute(new Attribute("error.postcode", "invalid", "string"));
            error=true;
        }

        if (isEmpty(proposer.getTelephoneNumber())) {
            proposer.addAttribute(new Attribute("error.phone", "required", "string"));
            error=true;
        }
        else if (!phonePattern.matcher(proposer.getTelephoneNumber()).find()) {
            proposer.addAttribute(new Attribute("error.phone", "invalid", "string"));
            error=true;
        }

        if (isEmpty(proposer.getEmailAddress())) {
            proposer.addAttribute(new Attribute("error.email", "required", "string"));
            error=true;
        }
        else if (!emailPattern.matcher(proposer.getEmailAddress()).find()) {
            proposer.addAttribute(new Attribute("error.email", "invalid", "string"));
            error=true;
        }
        
        return error;
    }

	@Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        com.ail.openquote.Proposer proposer=((Quotation)model).getProposer();

        w.printf("<table width='100%%' border='0' cols='6'>");
        w.printf(" <tr><td height='15' colspan='6'>&nbsp;</td></tr>");
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td class='portal-form-label'>Title:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <select id='title' name='title' class='pn-normal' class='portlet-form-input-field', onchange='disableTargetIf(this.value!=\"Other\", \"otherTitle\")'>%s</select>", Functions.renderEnumerationAsOptions(Title.class, proposer.getTitle()));
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.title']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf("  <td class='portal-form-label'>Other:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='otherTitle' id='otherTitle' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", hideNull(proposer.getOtherTitle()));
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.otherTitle']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td class='portal-form-label'>First name:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='firstname' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getFirstName());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.firstName']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf("  <td class='portal-form-label'>Surname:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='surname' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getSurname());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.surname']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr><td height='15'></td></tr>");
        
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td class='portal-form-label'>Address:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='address1' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getLine1());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.address1']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td>&nbsp;</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='address2' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getLine2());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.address2']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td>&nbsp;</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='town' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getTown());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.town']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td>&nbsp;</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='county' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getCounty());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.county']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");

        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td>&nbsp;</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='postcode' class='portlet-form-input-field' type='text' value='%s' size='8' maxlength='8'>", proposer.getAddress().getPostcode());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.postcode']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr><td height='15'></td></tr>");

        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td class='portal-form-label'>Telephone number:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='phone' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getTelephoneNumber());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.phone']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");

        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td class='portal-form-label'>Email address:</td>");
        w.printf("  <td colspan='2'>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='email' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getEmailAddress());
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", error("proposer/attribute[id='error.email']", model));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");

        w.printf(" <tr><td height='15'></td></tr>");

        // Disable the 'otherTitle' field on page load if 'title' isn't 'Other'.
        w.printf(" <script type='text/javascript'>disableTargetIf(document.getElementById(\"title\").value!=\"Other\", \"otherTitle\")</script>");

        w.printf("</table>");
	}
}
