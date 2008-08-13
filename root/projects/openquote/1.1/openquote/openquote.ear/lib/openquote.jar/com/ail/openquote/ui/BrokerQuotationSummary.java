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
    /* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
package com.ail.openquote.ui;

import static com.ail.insurance.policy.PolicyStatus.QUOTATION;
import static com.ail.insurance.policy.PolicyStatus.SUBMITTED;
import static com.ail.openquote.ui.util.Functions.longDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.financial.DirectDebit;
import com.ail.financial.MoneyProvision;
import com.ail.financial.PaymentCard;
import com.ail.insurance.policy.Behaviour;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.Marker;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;

/**
 * <p>This PageElement renders a summary of a quotation which is aimed at brokers. The detailed format of the element
 * depends on the {@link com.ail.openquote.Quotation#getStatus() state} of the {@link com.ail.openquote.Quotation}.</p>
 * 
 * <p>The three alternative formats are shown below:</p>
 * 
 * <p>The quote is at Quotation status when the premium has been calculated, but the proposer has not indicated
 * whether they would like to proceed or not. The premium detail section displays significant considerations included
 * in the premium, but stops short of displaying the whole assessment sheet ({@link AssessmentSheetDetails})<br/>
 * <img src="doc-files/BrokerQuotationSummary-1.png"/></p>
 * 
 * <p>A quote moved to submitted status once the user goes through "confirm and pay" and has supplied their
 * payment details and ticked the "I confirm the details I have given are correct" button. The primary difference
 * between this and the previous screenshot is that payment details are now included:<br/>
 * <p><img src="doc-files/BrokerQuotationSummary-2.png"/></p>
 * 
 * <p>If risk assessment or premium calculation refers or fails for any other reason, the quote is left at 
 * referred status and the page element displays the following. Payment details are not included as none have
 * been collected at this stage, and the "Assessment summary" focuses on the referral reasons.<br/>
 * <img src="doc-files/BrokerQuotationSummary-3.png"/></p>
 */
public class BrokerQuotationSummary extends PageContainer {
    private static final long serialVersionUID = -4810599045554021748L;
    /** Define the types of lines to include in the premium detail table */
    private static List<BehaviourType> PREMIUM_DETAIL_LINE_TYPES = new ArrayList<BehaviourType>();
    
    static {
        PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.TAX);
        PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.COMMISSION);
        PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.MANAGEMENT_CHARGE);
        PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.BROKERAGE);
    }

    public BrokerQuotationSummary() {
        super();
    }

    @Override
    public void applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        super.applyRequestValues(request, response, model);
    }

    @Override
    public void processActions(ActionRequest request, ActionResponse response, Type model) {
        super.processActions(request, response, model);
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return super.processValidations(request, response, model);
    }

    /**
     * Render the page element. This method is split out from {@link #renderResponse(RenderRequest, RenderResponse, Type) renderResponse()}
     * to allow it to be used outside of a PageFlow - i.e. for inclusion in broker notification emails.
     * @param w Writer to render to.
     * @param model Model (Quotation) to render data for.
     */
    public void render(PrintWriter w, Type model) {
        Quotation quote=(com.ail.openquote.Quotation)model;
        
        w.printf("<table width='100%%' align='center'>");
        w.printf(  "<tr>");
        w.printf(    "<td>");
        w.printf(      "<table width='100%%' style='border-collapse: collapse;'>");
        w.printf(        "<tr class='portlet-section-header'>");
        w.printf(           "<td>%s: %s</td>",quote.getStatus().longName(), quote.getQuotationNumber());
        if (QUOTATION.equals(quote.getStatus()) || SUBMITTED.equals(quote.getStatus())) {
            w.printf(           "<td align='right'>Gross Premium: %s</td>", quote.getTotalPremium());
        }
        else {
            w.printf(           "<td>&nbsp;</td>");
        }
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

    private void renderProposerDetails(PrintWriter w, Quotation quote) {
    	Proposer proposer=(Proposer)quote.getProposer();
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr><td class='portlet-section-selected' colspan='2'>Proposer</td></tr>");
        w.printf(  "<tr><td>Name</td><td>%s</td></tr>", proposer.getLegalName());
        w.printf(  "<tr><td>Address</td><td>%s</td></tr>", proposer.getAddress().getLine1());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getLine2());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getTown());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getCounty());
        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getPostcode());
        w.printf(  "<tr><td>Phone</td><td>%s</td></tr>", proposer.getTelephoneNumber());
        w.printf(  "<tr><td>Email</td><td><a href='mailto:rand@shadows.demon.co.uk'>%s</a></td></tr>", proposer.getEmailAddress());
        w.printf("</table>");
    }

    private void renderPremiumDetail(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr class='portlet-section-selected'><td colspan='5'>Premium detail</td></tr>");
        w.printf(  "<tr><td colspan='5'>The total premium shown above includes:</td></tr>");
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
            w.printf(  "<tr class='portlet-section-selected'><td colspan='5'>Payment</td></tr>");
            w.printf(  "<tr><td colspan='5'>%s</td></tr>", quote.getPaymentDetails().getDescription());
            
            for(MoneyProvision provision: quote.getPaymentDetails().getMoneyProvision()) {
                if (provision.getPaymentMethod() instanceof PaymentCard) {
                    DateFormat expiry=new SimpleDateFormat("dd/yy");
                    PaymentCard card=(PaymentCard)provision.getPaymentMethod();
                    w.printf(  "<tr><td>&nbsp;</td><td colspan='4'><u>Card details</u></td></tr>");
                    w.printf(  "<tr><td>&nbsp;</td><td>Card number</td><td>%s</td></tr>", card.getCardNumber());
                    w.printf(  "<tr><td>&nbsp;</td><td>Name on card</td><td>%s</td></tr>", card.getCardHoldersName());
                    w.printf(  "<tr><td>&nbsp;</td><td>Issue number</td><td>%s</td></tr>", card.getIssueNumber());
                    w.printf(  "<tr><td>&nbsp;</td><td>Expiry date</td><td>%s</td></tr>", expiry.format(card.getExpiryDate()));
                }
                else if (provision.getPaymentMethod() instanceof DirectDebit) {
                    DirectDebit dd=(DirectDebit)provision.getPaymentMethod();
                    w.printf(  "<tr><td>&nbsp;</td><td colspan='4'><u>Account details</u></td></tr>");
                    w.printf(  "<tr><td>&nbsp;</td><td>Account number</td><td>%s</td></tr>", dd.getAccountNumber());
                    w.printf(  "<tr><td>&nbsp;</td><td>Sort code</td><td>%s</td></tr>", dd.getSortCode());
                }
            }
            
            w.printf("</table>");
        }
    }

    private void renderSummary(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr class='portlet-section-selected'><td colspan='2'>Summary</td></tr>");
        w.printf(  "<tr><td>Product</td><td>%s</td></tr>", quote.getProductName());
        w.printf(  "<tr><td>Quotation date</td><td>%s</td></tr>", longDate(quote.getQuotationDate()));
        w.printf(  "<tr><td>Quotation expiry date</td><td>%s</td></tr>", longDate(quote.getQuotationExpiryDate()));
        w.printf(  "<tr><td>Cover start date</td><td>%s</td></tr>", longDate(quote.getInceptionDate()));
        w.printf(  "<tr><td>Cover end date</td><td>%s</td></tr>", longDate(quote.getExpiryDate()));
        w.printf("</table>");
    }
    
    private void renderAssessmentSummary(PrintWriter w, Quotation quote) {
        w.printf("<table width='100%%' class='portlet-font'>");
        w.printf(  "<tr class='portlet-section-selected'><td colspan='5'>Assessment summary</td></tr>");
        for(Marker line: quote.getAssessmentSheet().getLinesOfType(Marker.class).values()) {
            w.printf("<tr><td>%s</td><td>%s</td></tr>", 
                    line.getType().getLongName(),
                    line.getReason());
        }
        w.printf("</table>");
    }

    /**
     * @inheritDoc
     */
    @Override
	public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        render(w, model);
	}
}
