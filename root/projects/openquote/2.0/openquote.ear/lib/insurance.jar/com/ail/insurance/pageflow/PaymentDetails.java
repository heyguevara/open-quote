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

import static com.ail.insurance.pageflow.util.I18N.i18n;
import static com.ail.insurance.pageflow.util.Functions.addError;
import static com.ail.insurance.pageflow.util.Functions.isEmpty;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.financial.DirectDebit;
import com.ail.financial.MoneyProvision;
import com.ail.financial.PaymentCard;
import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.pageflow.util.Functions;
import com.ail.insurance.pageflow.util.QuotationContext;

/**
 * <p>This element takes care of the collection of payment details. Once the payment schedule
 * has been added to the {@link com.ail.openquote.Policy Policy}, this element will 
 * collect the necessary information to populate the schedule with appropriate details (e.g.
 * for direct debits account numbers and sort codes are collected; for credit cards card 
 * numbers, issue numbers etc are collected).</p>
 * <p><img src="doc-files/PaymentDetails.png"/></p>
 * <p>As the screenshot shows, the two panels in the "Payment details" section collect the
 * specific details. These panels are shown or hidden as is appropriate based on the types
 * of payment found in the {@link com.ail.financial.PaymentSchedule PaymentSchedule}. If
 * the schedule only contains payments from credit cards, only the credit card section is 
 * shown; likewise for direct debits. If both types appear in the schedule (as in the case
 * above), both panels appear.</p>
 * <p>If the {@link com.ail.financial.PaymentSchedule PaymentSchedule} includes multiple
 * entries of the same type, the element assumes that they will all be made from the same
 * account/card and so displays the panel for that type once, but places the details
 * collected into each entry.</p>
 * <p>The 'Conform' tickbox is not bound to any field in the quotation, but must be
 * ticked for the page to pass validation.</p> 
 * @see com.ail.openquote.Policy
 * @see com.ail.financial.PaymentSchedule
 * @see com.ail.financial.DirectDebit
 * @see com.ail.financial.PaymentCard
 */
public class PaymentDetails extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;
    private static SimpleDateFormat monthYearFormat=new SimpleDateFormat("MMyy");

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        Policy quote=(Policy)model;
 
        for(MoneyProvision mp: quote.getPaymentDetails().getMoneyProvision()) {
            if (mp.getPaymentMethod() instanceof PaymentCard) {
                PaymentCard pc=(PaymentCard)mp.getPaymentMethod();
                pc.setCardNumber(request.getParameter("cardNumber"));
                pc.setIssueNumber(request.getParameter("issueNumber"));
                pc.setCardHoldersName(request.getParameter("cardHoldersName"));
                
                try {
                    Date d=monthYearFormat.parse(request.getParameter("startMonth")+request.getParameter("startYear"));
                    pc.setStartDate(d);
                }
                catch (Exception e) {
                    pc.setExpiryDate(null);
                }

                try {
                    Date d=monthYearFormat.parse(request.getParameter("expiryMonth")+request.getParameter("expiryYear"));
                    pc.setExpiryDate(d);
                }
                catch (Exception e) {
                    pc.setExpiryDate(null);
                }
            }
            else if (mp.getPaymentMethod() instanceof DirectDebit) {
                mp.xpathSet("paymentMethod/accountNumber", request.getParameter("acc"));
                mp.xpathSet("paymentMethod/sortCode", request.getParameter("sc1")+"-"+request.getParameter("sc2")+"-"+request.getParameter("sc3"));
            }
        }
        
        return quote;
    }

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    Policy quote=(Policy)model;
        boolean error=false;
        PaymentSchedule schedule=quote.getPaymentDetails();

        Functions.removeErrorMarkers(schedule);
        
        if (request.getParameter("confirm")==null) {
            addError("confirm", i18n("i18n_payment_details_confirm_error_label"), schedule);
            error=true;
        }
        
        for(MoneyProvision mp: schedule.getMoneyProvision()) {
            if (mp.getPaymentMethod() instanceof PaymentCard) {
                PaymentCard pc=(PaymentCard)mp.getPaymentMethod();
                
                if (isEmpty(pc.getCardNumber())) {
                    addError("pc.cardNumber", i18n("i18n_required_error"), schedule);
                    error=true;
                }
                else if (!pc.getCardNumber().matches("[0-9 ]*")) {
                    addError("pc.cardNumber", i18n("i18n_invalid_error"), schedule);
                    error=true;
                }
                
                if (pc.getStartDate()==null) {
                    addError("pc.startDate", i18n("i18n_required_error"), schedule);
                    error=true;
                }

                if (pc.getExpiryDate()==null) {
                    addError("pc.expiryDate", i18n("i18n_required_error"), schedule);
                    error=true;
                }

                if (!isEmpty(pc.getIssueNumber()) && !pc.getIssueNumber().matches("[0-9]*")) {
                    addError("pc.issueNumber", i18n("i18n_invalid_error"), schedule);
                    error=true;
                }

                if (isEmpty(pc.getCardHoldersName())) {
                    addError("pc.cardHoldersName", i18n("i18n_required_error"), schedule);
                    error=true;
                }
                else if (!pc.getCardHoldersName().matches("[a-zA-Z0-9 .&]*")) {
                    addError("pc.cardHoldersName", i18n("i18n_invalid_error"), schedule);
                    error=true;
                }
            }
            else if (mp.getPaymentMethod() instanceof DirectDebit) {
                DirectDebit dd=(DirectDebit)mp.getPaymentMethod();
                
                if (isEmpty(dd.getAccountNumber())) {
                    addError("dd.account", i18n("i18n_required_error"), schedule);
                    error=true;
                }
                else if (!dd.getAccountNumber().matches("[0-9]{8,10}")) {
                    addError("dd.account", i18n("i18n_invalid_error"), schedule);
                    error=true;
                }
                
                if (isEmpty(dd.getSortCode()) || "--".equals(dd.getSortCode())) {
                    addError("dd.sort", i18n("i18n_required_error"), schedule);
                    error=true;
                }
                else if (!dd.getSortCode().matches("[0-9]{2}-[0-9]{2}-[0-9]{2}")) {
                    addError("dd.sort", i18n("i18n_invalid_error"), schedule);
                    error=true;
                }
            }
        }

        return error; // false: there are no errors.
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    Policy quote=(Policy)model;
        PrintWriter w=response.getWriter();

        return QuotationContext.getRenderer().renderPaymentDetails(w, request, response, quote, this);
    }

}
