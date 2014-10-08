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

package com.ail.openquote.ui.render.html;

import static com.ail.openquote.ui.messages.I18N.i18n;
import static com.ail.openquote.ui.util.Functions.longDate;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.BaseException;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Type;
import com.ail.financial.DirectDebit;
import com.ail.financial.MoneyProvision;
import com.ail.financial.PaymentCard;
import com.ail.insurance.policy.Behaviour;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.Marker;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.openquote.CommercialProposer;
import com.ail.openquote.PersonalProposer;
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.render.RenderService.RenderArgument;

/**
 * Default Java implementation of the broker quotation summary widget for HTML.
 */
@ServiceImplementation
public class BrokerQuotationSummaryService extends Service<RenderArgument> {
    private static final long serialVersionUID = -4915889686192216902L;
    private static final List<BehaviourType> PREMIUM_DETAIL_LINE_TYPES=new ArrayList<BehaviourType>(
    		Arrays.asList(
    				BehaviourType.TAX, 
    				BehaviourType.COMMISSION, 
    				BehaviourType.MANAGEMENT_CHARGE, 
    				BehaviourType.BROKERAGE
    		)
    );

    /** The 'business logic' of the entry point. */
    public void invoke() throws PreconditionException, BaseException {
    	if (args.getModelArg()==null || !(args.getModelArg() instanceof Quotation)) {
    		throw new PreconditionException("args.getModelArg()==null || !(args.getModelArg() instanceof Quotation)");
    	}

    	StringWriter output=new StringWriter();
    	
    	renderBrokerQuotationSummary(new PrintWriter(output), (Quotation)args.getModelArg());
    	
    	args.setRenderedOutputRet(output.toString());

    	if (args.getRenderedOutputRet()==null || args.getRenderedOutputRet().length()==0) {
    		throw new PostconditionException("args.getPageElementArg()==null || !(args.getPageElementArg() instanceof BrokerQuotationSummary)");
    	}
    }

	/**
     * Render the page element. This method is split out from {@link #renderResponse(RenderRequest, RenderResponse, Type) renderResponse()}
     * to allow it to be used outside of a PageFlow - i.e. for inclusion in broker notification emails.
     * @param w Writer to render to.
     * @param model Model (Quotation) to render data for.
     */
    private void renderBrokerQuotationSummary(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' align='center'>");
        w.printf(  "<tr>");
        w.printf(    "<td>");
        w.printf(      "<table width='100%%' style='border-collapse: collapse;'>");
        w.printf(        "<tr class='portlet-section-header'>");
        w.printf(           "<td>%s: %s</td>",quote.getStatus().longName(), quote.getQuotationNumber());
        renderPremium(w, quote);
        w.printf(        "</tr>");
        w.printf(        "<tr>");
        w.printf(          "<td colspan='2'>");
        w.printf(            "<table cellpadding='15' width='100%%'>");
        w.printf(              "<tr>");
        w.printf(                "<td colspan='2' class='portlet-font' valign='top'>");
        renderSummary(w, quote);

        switch(quote.getStatus()) {
        case SUBMITTED:
            w.printf("<br/>");
            renderPayments(w, quote);
            // fall through
        case QUOTATION:
            w.printf("<br/>");
            renderPremiumDetail(w, quote);
            break;                
        default:
        	w.printf("<br/>");
        	renderAssessmentSummary(w, quote);
        }
                
        w.printf(                "</td>");
        w.printf(                "<td rowspan='2' valign='top' align='center'>");
        renderProposerDetails(w, quote);
        w.printf(                "</td>");
        w.printf(              "</tr>");
        w.printf(            "</table>");
        w.printf(          "</td>");
        w.printf(        "</tr>");
        w.printf(      "</table>");
        w.printf(    "</td>");
        w.printf(  "</tr>");
        w.printf("</table>");
    }

    private void renderPremium(PrintWriter w, Quotation quote) {
		// We'll get an IllegalStateException if there is no premium on the quote; which is the case sometimes for Referrals and Declines.
		try {
            w.printf("<td align='right'>"+i18n("i18n_broker_quotation_summary_premium_title")+"</td>", quote.getTotalPremium());
		}
		catch(IllegalStateException e) {
            w.printf("<td>&nbsp;</td>");
        }
	}

    private void renderProposerDetails(PrintWriter w, Quotation quote) {
    	Proposer proposer=(Proposer)quote.getProposer();
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr><td class='portlet-section-selected' colspan='2'>"+i18n("i18n_broker_quotation_summary_proposer_title")+"</td></tr>");
        if (proposer instanceof PersonalProposer) {
            w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_legal_name_label")+"</td><td>%s</td></tr>", proposer.getLegalName());
            w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_address_label")+"</td><td>%s</td></tr>", proposer.getAddress().getLine1());
        }
        else { // Commercial proposer
            CommercialProposer cp=(CommercialProposer)proposer;
            w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_contact_name_label")+"</td><td>%s</td></tr>", cp.getContact().getLegalName());
            w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_address_label")+"</td><td>%s</td></tr>", proposer.getLegalName());
            w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getLine1());
        }
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getLine2());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getTown());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getCounty());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getPostcode());
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_phone_label")+"</td><td>%s</td></tr>", proposer.getTelephoneNumber());
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_email_label")+"</td><td><a href='mailto:%1$s'>%1$s</a></td></tr>", proposer.getEmailAddress());
        w.printf("</table>");
    }

    private void renderPremiumDetail(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr class='portlet-section-selected'><td colspan='5'>Premium detail</td></tr>");
        w.printf(  "<tr><td colspan='5'>"+i18n("i18n_broker_quotation_summary_premium_message")+"</td></tr>");
        for(Behaviour line: quote.getAssessmentSheet().getLinesOfType(Behaviour.class).values()) {
            if (PREMIUM_DETAIL_LINE_TYPES.contains(line.getType())) {
                w.printf("<tr><td>&nbsp;</td><td>%s</td><td>%s</td><td align='right'>%s</td><td align='right'>%s</td></tr>", 
                        line.getType().getLongName(),
                        line.getReason(),
                        (line instanceof RateBehaviour) ? ((RateBehaviour)line).getRate().getRate() : "&nbsp;",
                        line.getAmountAsString());
            }
        }
        w.printf("</table>");
    }

    private void renderPayments(PrintWriter w, Quotation quote) {
        if (quote.getPaymentDetails()!=null) {
            w.printf("<table width='100%%' class='portlet-font'>");
            w.printf(  "<tr class='portlet-section-selected'><td colspan='5'>"+i18n("i18n_broker_quotation_summary_payments_title")+"</td></tr>");
            w.printf(  "<tr><td colspan='5'>%s</td></tr>", quote.getPaymentDetails().getDescription());
            
            for(MoneyProvision provision: quote.getPaymentDetails().getMoneyProvision()) {
                if (provision.getPaymentMethod() instanceof PaymentCard) {
                    DateFormat expiry=new SimpleDateFormat("dd/yy");
                    PaymentCard card=(PaymentCard)provision.getPaymentMethod();
                    w.printf(  "<tr><td>&nbsp;</td><td colspan='4'><u>"+i18n("i18n_broker_quotation_summary_card_details_title")+"</u></td></tr>");
                    w.printf(  "<tr><td>&nbsp;</td><td>"+i18n("i18n_broker_quotation_summary_card_number_label")+"</td><td>%s</td></tr>", card.getCardNumber());
                    w.printf(  "<tr><td>&nbsp;</td><td>"+i18n("i18n_broker_quotation_summary_name_on_card_label")+"</td><td>%s</td></tr>", card.getCardHoldersName());
                    w.printf(  "<tr><td>&nbsp;</td><td>"+i18n("i18n_broker_quotation_summary_issue_number_label")+"</td><td>%s</td></tr>", card.getIssueNumber());
                    w.printf(  "<tr><td>&nbsp;</td><td>"+i18n("i18n_broker_quotation_summary_expiry_date_label")+"</td><td>%s</td></tr>", expiry.format(card.getExpiryDate()));
                }
                else if (provision.getPaymentMethod() instanceof DirectDebit) {
                    DirectDebit dd=(DirectDebit)provision.getPaymentMethod();
                    w.printf(  "<tr><td>&nbsp;</td><td colspan='4'><u>"+i18n("i18n_broker_quotation_summary_account_details_title")+"</u></td></tr>");
                    w.printf(  "<tr><td>&nbsp;</td><td>"+i18n("i18n_broker_quotation_summary_account_number_label")+"</td><td>%s</td></tr>", dd.getAccountNumber());
                    w.printf(  "<tr><td>&nbsp;</td><td>"+i18n("i18n_broker_quotation_summary_sort_code_label")+"</td><td>%s</td></tr>", dd.getSortCode());
                }
            }
            
            w.printf("</table>");
        }
    }

    private void renderSummary(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr class='portlet-section-selected'><td colspan='2'>"+i18n("i18n_broker_quotation_summary_title")+"</td></tr>");
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_product_label")+"</td><td>%s</td></tr>", quote.getProductName());
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_quotation_date_label")+"</td><td>%s</td></tr>", longDate(quote.getQuotationDate()));
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_quotation_expiry_date_label")+"</td><td>%s</td></tr>", longDate(quote.getQuotationExpiryDate()));
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_cover_start_date_label")+"</td><td>%s</td></tr>", longDate(quote.getInceptionDate()));
        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_cover_end_date_label")+"</td><td>%s</td></tr>", longDate(quote.getExpiryDate()));
        w.printf("</table>");
    }
    
    private void renderAssessmentSummary(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr class='portlet-section-selected'><td colspan='5'>"+i18n("i18n_broker_quotation_summary_assessment_sheet_title")+"</td></tr>");
        for(Marker line: quote.getAssessmentSheet().getLinesOfType(Marker.class).values()) {
            w.printf("<tr><td>%s</td><td>%s</td></tr>", 
                    line.getType().getLongName(),
                    line.getReason());
        }
        w.printf("</table>");
    }
}


