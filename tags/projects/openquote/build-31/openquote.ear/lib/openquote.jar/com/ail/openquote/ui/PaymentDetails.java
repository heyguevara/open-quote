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

import static com.ail.openquote.ui.util.Functions.addError;
import static com.ail.openquote.ui.util.Functions.findError;
import static com.ail.openquote.ui.util.Functions.hideNull;
import static com.ail.openquote.ui.util.Functions.isEmpty;

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
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;
import com.ail.openquote.ui.util.Functions;

/**
 * <p>This element takes care of the collection of payment details. Once the payment schedule
 * has been added to the {@link com.ail.openquote.Quotation Quotation}, this element will 
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
 * @see com.ail.openquote.Quotation
 * @see com.ail.financial.PaymentSchedule
 * @see com.ail.financial.DirectDebit
 * @see com.ail.financial.PaymentCard
 */
public class PaymentDetails extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;
	private static SimpleDateFormat monthFormat=new SimpleDateFormat("MM");
    private static SimpleDateFormat yearFormat=new SimpleDateFormat("yy");
    private static SimpleDateFormat monthYearFormat=new SimpleDateFormat("MMyy");

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        Quotation quote=(Quotation)model;
 
        for(MoneyProvision mp: quote.getPaymentDetails().getMoneyProvision()) {
            if (mp.getPaymentMethod() instanceof PaymentCard) {
                PaymentCard pc=(PaymentCard)mp.getPaymentMethod();
                pc.setCardNumber(request.getParameter("cardNumber"));
                pc.setIssueNumber(request.getParameter("issueNumber"));
                pc.setCardHoldersName(request.getParameter("cardHoldersName"));
                
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
	    Quotation quote=(Quotation)model;
        boolean error=false;
        PaymentSchedule schedule=quote.getPaymentDetails();

        Functions.removeErrorMarkers(schedule);
        
        if (request.getParameter("confirm")==null) {
            addError("confirm", "check to confirm", schedule);
            error=true;
        }
        
        for(MoneyProvision mp: schedule.getMoneyProvision()) {
            if (mp.getPaymentMethod() instanceof PaymentCard) {
                PaymentCard pc=(PaymentCard)mp.getPaymentMethod();
                
                if (isEmpty(pc.getCardNumber())) {
                    addError("pc.cardNumber", "required", schedule);
                    error=true;
                }
                else if (!pc.getCardNumber().matches("[0-9 ]*")) {
                    addError("pc.cardNumber", "invalid", schedule);
                    error=true;
                }
                
                if (pc.getExpiryDate()==null) {
                    addError("pc.expiryDate", "required", schedule);
                    error=true;
                }

                if (!isEmpty(pc.getIssueNumber()) && !pc.getIssueNumber().matches("[0-9]*")) {
                    addError("pc.issueNumber", "invalid", schedule);
                    error=true;
                }

                if (isEmpty(pc.getCardHoldersName())) {
                    addError("pc.cardHoldersName", "required", schedule);
                    error=true;
                }
                else if (!pc.getCardHoldersName().matches("[a-zA-Z0-9 .&]*")) {
                    addError("pc.cardHoldersName", "invalid", schedule);
                    error=true;
                }
            }
            else if (mp.getPaymentMethod() instanceof DirectDebit) {
                DirectDebit dd=(DirectDebit)mp.getPaymentMethod();
                
                if (isEmpty(dd.getAccountNumber())) {
                    addError("dd.account", "required", schedule);
                    error=true;
                }
                else if (!dd.getAccountNumber().matches("[0-9]{8,10}")) {
                    addError("dd.account", "invalid", schedule);
                    error=true;
                }
                
                if (isEmpty(dd.getSortCode()) || "--".equals(dd.getSortCode())) {
                    addError("dd.sort", "required", schedule);
                    error=true;
                }
                else if (!dd.getSortCode().matches("[0-9]{2}-[0-9]{2}-[0-9]{2}")) {
                    addError("dd.sort", "invalid", schedule);
                    error=true;
                }
            }
        }

        return error; // false: there are no errors.
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
	    Quotation quote=(Quotation)model;
        PrintWriter w=response.getWriter();

        w.printf("<table cellpadding='4' width='100%%'>");
        renderSummary(w, quote);
        renderPaymentDetails(w, quote);
        renderSubmit(w, quote);
        w.printf("</table>");
        
        return quote;
    }

    private void renderSummary(PrintWriter w, Quotation quote) {
        SimpleDateFormat f=new SimpleDateFormat("d MMMMM, yyyy");
        Proposer proposer=(Proposer)quote.getProposer();

        w.printf("<tr class='portlet-font'>");
        w.printf("    <td class='portlet-section-alternate'>Your quotation</td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf("    <td>");
        w.printf("        <table width='100%%'>");
        w.printf("            <tr class='portlet-font'><td width='15%%'><b>Premium</b></td><td>%s</td></tr>", quote.getTotalPremium());
        w.printf("            <tr class='portlet-font'><td width='15%%'><b>Cover start date</b></td><td>%s</td></tr>", f.format(quote.getInceptionDate()));
        w.printf("            <tr class='portlet-font'><td width='15%%'><b>Cover end date</b></td><td>%s</td></tr>", f.format(quote.getExpiryDate()));
        w.printf("        </table>");
        w.printf("  </td>");
        w.printf("</tr>");
        w.printf("<tr class='portlet-font'>");
        w.printf("    <td class='portlet-section-alternate'>Your contact information</td>");
        w.printf("</tr> ");
        w.printf("<tr>");
        w.printf("    <td>");
        w.printf("        <table width='100%%'>");
        w.printf("            <tr class='portlet-font'><td width='15%%'><b>Name</b></td><td>%s %s</td></tr>", proposer.getFirstName(), proposer.getSurname());
        w.printf("            <tr class='portlet-font'><td width='15%%'><b>Address</b></td><td>%s</td></tr>", proposer.getAddress());
        w.printf("            <tr class='portlet-font'><td width='15%%'><b>Email address</b></td><td>%s</td></tr>", proposer.getEmailAddress());
        w.printf("        </table>");
        w.printf("    </td>");
        w.printf("</tr>");
    }

    private void renderPaymentDetails(PrintWriter w, Quotation quote) {
        w.printf("<tr class='portlet-font'>");
        w.printf("    <td class='portlet-section-alternate'>Payment details</td>");
        w.printf("</tr> ");
        w.printf("<tr>");
        w.printf("    <td>");
        for(MoneyProvision mp: quote.getPaymentDetails().getMoneyProvision()) {
            if (mp.getPaymentMethod() instanceof PaymentCard) {
                renderCardDetails(w, quote, mp);
            }
            else if (mp.getPaymentMethod() instanceof DirectDebit) {
                renderBankDetails(w, quote, mp);
            }
        }
        w.printf("    </td>");
        w.printf("</tr> ");
    }

    private void renderBankDetails(PrintWriter w, Quotation quote, MoneyProvision mp) {
        PaymentSchedule schedule=quote.getPaymentDetails();

        DirectDebit dd=(DirectDebit)mp.getPaymentMethod();
        String accountNumber=hideNull(dd.getAccountNumber());
        String sortCode=(dd.getSortCode()!=null && dd.getSortCode().length()>0)? dd.getSortCode() : "--";
        String sc1=sortCode.substring(0, sortCode.indexOf('-'));
        String sc2=sortCode.substring(sortCode.indexOf('-')+1, sortCode.lastIndexOf('-'));
        String sc3=sortCode.substring(sortCode.lastIndexOf('-')+1);
        
        w.printf("<table width='100%%' cols='2'>");
        w.printf(" <tr class='portlet-font'><td width='25%%' colspan='2'>Please enter your bank account number and sort code so that we can take payment from your account</td></tr>");
        w.printf(" <tr class='portlet-font'><td width='25%%'><b>Originator</b></td><td>%s</td></tr>", quote.getBroker().getLegalName()+", "+quote.getBroker().getAddress());
        w.printf(" <tr class='portlet-font'><td width='25%%'><b>Originator's identification number</b></td><td>%s</td></tr>", quote.getBroker().getDirectDebitIdentificationNumber());
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td width='25%%'><b>Account number</b></td>");
        w.printf("  <td>");
        w.printf("    <table border='0'><tr>");
        w.printf("     <td><input name='acc' size='8' type='text' maxlength='10' value='%s'/></td>", accountNumber);
        w.printf("     <td class='portlet-msg-error'>%s</td>", findError("dd.account", schedule));
        w.printf("   </table>");
        w.printf("  </tr>");
        w.printf(" </tr>");
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td width='25%%'><b>Sort code</b></td>");
        w.printf("  <td>");
        w.printf("    <table border='0'><tr>");
        w.printf("     <td>");
        w.printf(       "<input name='sc1' size='2' maxlength='2' type='text' value='%s'/>-", sc1);
        w.printf(       "<input name='sc2' size='2' maxlength='2' type='text' value='%s'/>-", sc2);
        w.printf(       "<input name='sc3' size='2' maxlength='2' type='text' value='%s'/>", sc3);
        w.printf("     </td>");
        w.printf("     <td class='portlet-msg-error'>%s</td>", findError("dd.sort", schedule));
        w.printf("   </table>");
        w.printf("  </tr>");
        w.printf(" </tr>");
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td colspan='2'>");
        w.printf("Please pay %s from the account detailed in this instruction subject to the safeguards assured by the ", quote.getBroker().getLegalName());
        w.printf("direct debit guarantee. I understand that this instruction may remain with the originator mentioned above and, if so, details will be ");
        w.printf("passed electronically to my bank or building society.<br/><br/>");
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td colspan='2'>");
        w.printf("<u>Direct Debit Guarantee</u><br/>");
        w.printf("This guarantee is offered by all banks and building societies that take part in the Direct Debit scheme. The efficiency ");
        w.printf("and security of the scheme is monitored and protected by your own bank or building society.");
        w.printf("<ul> ");
        w.printf("<li>If the amounts to be paid or the payment dates change, you will be told of this 8 days in advance of your account ");
        w.printf("being debited.</li> ");
        w.printf("<li>If an error is made by %s or your bank or building society, you are guaranteed a full and immediate ", quote.getBroker().getLegalName());
        w.printf("refund from your branch of the amount paid.</li>");
        w.printf("<li>You can cancel a Direct Debit at any time by writing to your bank or building society. Please also send a copy of your ");
        w.printf("letter to us.</li>");
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf("</table>");
    }

    private void renderCardDetails(PrintWriter w, Quotation quote, MoneyProvision mp) {
        PaymentSchedule schedule=quote.getPaymentDetails();
        PaymentCard pc=(PaymentCard)mp.getPaymentMethod();
        
        String month=pc.getExpiryDate()!=null ? monthFormat.format(pc.getExpiryDate()) : "";
        String year=pc.getExpiryDate()!=null ? yearFormat.format(pc.getExpiryDate()) : "";
        
        if (pc.getCardHoldersName()==null) {
        	Proposer proposer=(Proposer)quote.getProposer();
            pc.setCardHoldersName(proposer.getFirstName()+" "+proposer.getSurname());
        }
        
        w.printf("<table width='100%%' cols='2'>");
        w.printf(" <tr class='portlet-font'><td width='15%%' colspan='2'>Please enter your credit/debit card details</td></tr>");

        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td width='15%%'><b>Card number</b></td>");
        w.printf("  <td>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td><input name='cardNumber' size='20' type='text' value='%s'/></td>", hideNull(pc.getCardNumber()));
        w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.cardNumber", schedule));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");

        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td width='15%%'><b>Expiry date</b></td>");
        w.printf("  <td>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td>");
        w.printf("     <input name='expiryMonth' size='2' maxlength='2' type='text' value='%s'/>", month);
        w.printf("     <input name='expiryYear' size='2' type='text' maxlength='2' value='%s'/>", year);
        w.printf("    </td>");
        w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.expiryDate", schedule));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");
        
        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td width='15%%'><b>Issue number</b></td>");
        w.printf("  <td>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td><input name='issueNumber' size='2' maxlength='2' type='text' value='%s'/></td>", hideNull(pc.getIssueNumber()));
        w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.issueNumber", schedule));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");

        w.printf(" <tr class='portlet-font'>");
        w.printf("  <td width='15%%'><b>Cardholders name</b></td>");
        w.printf("  <td>");
        w.printf("   <table border='0'><tr>");
        w.printf("    <td><input name='cardHoldersName' size='20' type='text' value='%s'/></td>", pc.getCardHoldersName());
        w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.cardHoldersName", schedule));
        w.printf("   </tr></table>");
        w.printf("  </td>");
        w.printf(" </tr>");

        w.printf("</table>");
    }

    private void renderSubmit(PrintWriter w, Quotation quote) {
        w.printf("<tr class='portlet-font'>");
        w.printf("    <td class='portlet-section-alternate'>Submit</td>");
        w.printf("</tr>");
        w.printf("<tr class='portlet-font'>");
        w.printf(" <td>");
        w.printf("Please note you have confirmed that you are not falsely representing yourself or impersonating someone else within the ");
        w.printf("details you are submitting for this policy and payment. You also confirm that you are the only person required to ");
        w.printf("authorise this debit from your account. If you can not comply please contact us.<br/><br/>");
        w.printf("You can contacted us on %s<br/><br/>", quote.getBroker().getPaymentTelephoneNumber());
        w.printf(" <table border='0'><tr>");
        w.printf("   <td>Please tick this box to confirm that the details you have entered are correct <input name='confirm' type='checkbox'/></td>");
        w.printf("   <td class='portlet-msg-error'>%s</td>", findError("confirm", quote.getPaymentDetails()));
        w.printf(" </table>");
        w.printf(" </td>");
        w.printf("</tr>");
    }
}
