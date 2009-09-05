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

import static com.ail.openquote.ui.messages.I18N.i18n;
import static com.ail.openquote.ui.util.Functions.addError;

import java.io.IOException;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.financial.PaymentSchedule;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * <p>This element displays the payment options available and prompts the user to select
 * one from the list</p>
 * <p>The options to be listed are defined by the {@link com.ail.openquote.Quotation#getPaymentOption() 
 * Quotation.getPaymentOption()}. Each option comprises a {@link com.ail.financial.PaymentSchedule PaymentSchedule}; when
 * an option is selected, the associate schedule is copied to {@link com.ail.openquote.Quotation#setPaymentDetails(PaymentSchedule) 
 * Quotation.setPaymentDetails()} and therefore defines how payments will be made for this policy.</p>
 * <p>Only one option may be selected. Page validation will fail unless one options is selected.</p>
 * <p><img src="doc-files/PaymentOptionSelector.png"/></p>
 * In the example above, three payment options are defined; two a simple single payment schedules; the third is a 
 * more complex schedule with a deposit and multiple follow on payments.
 */
public class PaymentOptionSelector extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        Quotation quote=(Quotation)model;

        // null any existing payment option. In state terms is it okay for a quote to have a null
        // paymentDetails. It just means that none has been selected. We'll use it for validation too.
        quote.setPaymentDetails(null);
        
        // if an option was selected...
        if (request.getParameter("selectedOption")!=null) {
            // the option's value will be the hashcode of the payment option in the model.
            int selectedOption=Integer.parseInt(request.getParameter("selectedOption"));

            // loop though the model's payment options looking for one with a matching hashcode
            for(PaymentSchedule ps: quote.getPaymentOption()) {
                if (ps.hashCode()==selectedOption) {
                    // bingo!
                    quote.setPaymentDetails(ps);
                }
            }
        }
        
        return quote;
    }

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    Quotation quote=(Quotation)model;
        
	    Functions.removeErrorMarkers(quote);
        
        if (quote.getPaymentDetails()==null) {
            addError("paymentDetails", i18n("i18n_payment_option_select_error"), model);
            return true;
        }

        return false;
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return QuotationContext.getRenderer().renderPaymentOptionSelector(response.getWriter(), request, response, (Quotation)model, this);
    }
}
