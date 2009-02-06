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
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Properties;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotation;
import com.ail.openquote.SavedQuotationSummary;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * <p>Display a list of a user's saved quotations. If the user is logged in, a list of their saved
 * quotes is displayed with options to process them. If the user is not logged in, an invitation
 * to "login to view your saved quotes" is displayed. This page element also manages the login 
 * process should the user accept the invitation.</p>
 * <p>The following three screen shots show how the page is rendered when the user is not logged in; 
 * when they are in the process of logging in; and listing their quotes when they have logged in.</p>
 * <p><img src="doc-files/SavedQuotations-1.png"/></p>
 * <p><img src="doc-files/SavedQuotations-2.png"/></p>
 * <p><img src="doc-files/SavedQuotations-3.png"/></p>
 * <p>The quotations are listed with three options: <ul>
 * <li> <b>Confirm and Pay</b> This take the user to a
 * page (specified by the {@link #getConfirmAndPayDestinationPageId() cofirmAndPayDestinationPageId} 
 * property. This could be any page in the flow, but the expectation is that this takes the user
 * to either select payment options, or enter payment details as is appropriate.</li>
 * <li>
 * <li><b>Requote</b> Takes the user to the page specified by the {@link #getRequoteDestinationPageId()
 * requotePageDestination} property and in doing so creates a new quotation based on the one selected.</li>
 * <li>View</li>View the quotation as a PDF. The system generates PDFs to details a quote only on demand.
 * If this button is selected, the quote PDF will be generated if it has not already been done.</li>
 * </ul>
 */
public class SavedQuotations extends PageElement {
    private static SimpleDateFormat dateFormat=new SimpleDateFormat("d MMMMM, yyyy");
	private static final long serialVersionUID = -4810599045554021748L;

    /** Id of the page to forward to in the pageflow if the user selected "requote" */
    private String requoteDestinationPageId;

    /** Id of the page to forward to in the pageflow if the user selected "confirm and pay" */
    private String confirmAndPayDestinationPageId;

    /** Label to appear on the confirm button. Defaults to "Confirm and Pay" */
    private String confirmAndPayLabel="i18n_saved_quotations_confirm_button_label";
    
    /** Label to appear on the requote button. Defaults to "Requote" */
    private String requoteLabel="i18n_saved_quotations_requote_button_label";
    
    /** Button to handle the "view quote" action.  */
    private ViewQuotationButtonAction viewQuotationButtonAction=new ViewQuotationButtonAction();
    
	public SavedQuotations() {
		super();
	}

    /**
     *  Id of the page to forward to in the pageflow if the user selected "confirm and pay".
     * @return Page ID
     */
    public String getConfirmAndPayDestinationPageId() {
        return confirmAndPayDestinationPageId;
    }

    /**
     * @see #getConfirmAndPayDestinationPageId()
     * @param confirmAndPayDestinationPageId Page ID
     */
    public void setConfirmAndPayDestinationPageId(String confirmAndPayDestinationPageId) {
        this.confirmAndPayDestinationPageId = confirmAndPayDestinationPageId;
    }

    /**
     * Id of the page to forward to in the pageflow if the user selected "requote"
     * @return Page ID
     */
    public String getRequoteDestinationPageId() {
        return requoteDestinationPageId;
    }

    /**
     * @see #getRequoteDestinationPageId()
     * @param requoteDestinationPageId
     */
    public void setRequoteDestinationPageId(String requoteDestinationPageId) {
        this.requoteDestinationPageId = requoteDestinationPageId;
    }

    /**
     * Button to handle the "view quote" action.  
     * @return View button
     */
    public ViewQuotationButtonAction getViewQuotationButtonAction() {
        return viewQuotationButtonAction;
    }

    /**
     * @see #getViewQuotationButtonAction()
     * @param viewQuotationButtonAction
     */
    public void setViewQuotationButtonAction(ViewQuotationButtonAction viewQuotationButtonAction) {
        this.viewQuotationButtonAction = viewQuotationButtonAction;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
    	return model;
    }

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
	    return false;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        CoreProxy core=new CoreProxy();
        Properties opParams=Functions.getOperationParameters(request);
        String op=opParams.getProperty("op");
        String quoteNumber=opParams.getProperty("id");
        Quotation quote=(Quotation)model;
        
        if (quoteNumber!=null) {
            try {
		        SavedQuotation savedQuotation=(SavedQuotation)core.queryUnique("get.savedQuotation.by.quotationNumber", quoteNumber);
		
		        viewQuotationButtonAction.processActions(request, response, savedQuotation);
		
		        if ("confirm".equals(op) || "requote".equals(op)) {
	                //TODO check saveQuotation's "owner" == current user. goto the error page if it doesn't
	                
	                //TODO check that savedQuotation hasn't expired - goto error page if it has.
	        
	                quote=savedQuotation.getQuotation();
	        
	                if ("confirm".equals(op)) {
	                    quote.setPage(confirmAndPayDestinationPageId);
	                    QuotationContext.setQuotation(quote);
	                }
	                else if ("requote".equals(op)) {
	                    quote.setPage(requoteDestinationPageId);
	                    quote.setStatus(PolicyStatus.APPLICATION);
	                    quote.setQuotationNumber(null);
	                    quote.markAsNotPersisted();
	                    QuotationContext.setQuotation(quote);
	                }
	            }
	        }
            catch(Exception e) {
                e.printStackTrace();
            }
        }
        else if ("login".equals(op) && request.getUserPrincipal()==null) {
            // We're performing a save and the user isn't logged in yet.
            String password=request.getParameter("password");
            String username=request.getParameter("username");
            String url="Unknown";
            
            try {
                String pageName=Functions.getOperationParameters(request).getProperty("page");
                String portalName=Functions.getOperationParameters(request).getProperty("portal");
                url="/portal/auth/portal/"+portalName+"/"+pageName+"/QuoteWindow?action=1&username="+username+"&password="+password;
                response.sendRedirect(url);
            }
            catch (Exception e) {
            	throw new RenderingError("Page forward on login failed. Forward was to: "+url, e);
            }
        }
        
        return model;
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        Quotation quote=(Quotation)model;

        // If the user is logged in...
        if (request.getRemoteUser()!=null) {
            // get a list of the user's saved quotes.
            List<?> quotes=new CoreProxy().query("get.savedQuotationSummary.by.username.and.product", request.getRemoteUser(), quote.getProductTypeId());

            // If the user has saved quotes...
            if (quotes.size()!=0) {
                w.printf("<table width='100%%' border='0' cols='5'>");
                w.printf(  "<tr><td cols='5'>"+i18n("i18n_saved_quotations_title")+"</td></tr>", quotes.size()==1 ? "quote" : "quotes");
                w.printf(  "<tr><td height='10' cols='5'/></tr>");
                w.printf(  "<tr class='portlet-font'>");
                w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_quote_number_heading")+"</td>");
                w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_quote_date_heading")+"</td>");
                w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_expiry_date_heading")+"</td>");
                w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_premium_heading")+"</td>");
                w.printf(    "<td class='portlet-section-alternate'>&nbsp</td>");
                w.printf(  "</tr>");
        
                for(Object o: quotes) {
                    SavedQuotationSummary savedQuote=(SavedQuotationSummary)o;
                    w.printf("<tr>");
                    w.printf(  "<td align='center' class='portal-form-label'>%s</td>", savedQuote.getQuotationNumber());
                    w.printf(  "<td align='center' class='portal-form-label'>%s</td>", dateFormat.format(savedQuote.getQuotationDate()));
                    w.printf(  "<td align='center' class='portal-form-label'>%s</td>", dateFormat.format(savedQuote.getQuotationExpiryDate()));
                    w.printf(  "<td align='center' class='portal-form-label'>%s</td>", savedQuote.getPremium().toFormattedString());
                    w.printf(  "<td align='left'>");
                    w.printf(    "<input type='submit' name='op=confirm:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(confirmAndPayLabel));
                    w.printf(    "<input type='submit' name='op=requote:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(requoteLabel));
                    viewQuotationButtonAction.renderResponse(request, response, savedQuote);
                    w.printf(  "</td>");
                    w.printf("</tr>");
                }
                
                w.printf("</table>");
            }
        }
        
        return quote;
	}

    @Override
    public void renderPageFooter(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();

        if (request.getRemoteUser()==null) {
            String pageName=Functions.getPortalPageName(response);
            String portalName=Functions.getPortalName(response);
            
            w.printf("<table width='100%%' cols='3'>");
            w.printf( "<tr>");
            w.printf(  "<td colspan='3' class='portlet-font'>"+i18n("i18n_saved_quotations_login_message")+"</td>");
            w.printf( "</tr>");
            w.printf( "<tr><td height='15'/></tr>");
            w.printf( "<tr>");
            w.printf(  "<td width='30%%'/>");
            w.printf(  "<td align='center'>");
            w.printf(  "<div class='portlet-font' id='Proposer Login'>");
            w.printf(   "<form method='post' action='%s' name='loginform' id='loginForm'>", response.createActionURL());
            w.printf(    "<table>");
            w.printf(     "<tr class='portlet-font'>");
            w.printf(      "<td>"+i18n("i18n_saved_quotations_username_label")+"</td>");
            w.printf(      "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value=''/></td>");
            w.printf(      "<td>&nbsp;</td>");
            w.printf(     "</tr>");
            w.printf(     "<tr class='portlet-font'>");
            w.printf(      "<td valign='center'>"+i18n("i18n_saved_quotations_password_label")+"</td>");
            w.printf(      "<td><input class='portlet-form-input-field' type='password' name='password' id='password' value=''/></td>");
            w.printf(      "<td><a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Forgotten Password\");'>"+i18n("i18n_saved_quotations_forgotten_password_message")+"</a></td>");
            w.printf(     "</tr>");
            w.printf(     "<tr class='portlet-font'>");
            w.printf(      "<td align='center' colspan='3'><input type='submit' id='loginButton' class='portlet-form-input-field' name='op=login:portal=%s:page=%s' value='Login'/></td>", portalName, pageName);
            w.printf(     "</tr>");
            w.printf(    "</table>");
            w.printf(   "</form>");
            w.printf(  "</div>");
            w.printf(  "</td>");
            w.printf(  "<td width='30%%'/>");
            w.printf( "</tr>");
            w.printf("</table>");
            w.printf("<script type='text/javascript'>hideDivDisplay('Proposer Login')</script>");
        }
    }

    /**
     * @see #setConfirmAndPayLabel(String)
     * @return the confirmAndPayLabel
     */
    public String getConfirmAndPayLabel() {
        return confirmAndPayLabel;
    }

    /**
     * Set the label to appear on the confirm button. The default is "Confirm and Pay".
     * @param confirmAndPayLabel the confirmAndPayLabel to set
     */
    public void setConfirmAndPayLabel(String confirmAndPayLabel) {
        this.confirmAndPayLabel = confirmAndPayLabel;
    }

    /**
     * @return the requoteLabel
     */
    public String getRequoteLabel() {
        return requoteLabel;
    }

    /**
     * Set the label to appear on the requote button. The default is "Requote"
     * @param requoteLabel the requoteLabel to set
     */
    public void setRequoteLabel(String requoteLabel) {
        this.requoteLabel = requoteLabel;
    }
}
