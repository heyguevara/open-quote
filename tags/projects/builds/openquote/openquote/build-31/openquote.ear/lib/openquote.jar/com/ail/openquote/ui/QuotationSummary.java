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

import static com.ail.core.Functions.expand;
import static com.ail.openquote.ui.util.Functions.expandRelativeUrl;
import static com.ail.openquote.ui.util.Functions.longDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collection;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.Behaviour;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.insurance.policy.SumBehaviour;
import com.ail.openquote.Quotation;

/**
 * <p>Page element to display a summary of a quotation. The QuotationSummary element is designed to render 
 * as a complete page detailing a newly created quotation which is presented to the user following premium 
 * calculation if that calculation results in a quotation (as opposed to a referral or a decline).</p>
 * <p><img src="doc-files/QuotationSummary.png"/></p>
 * <p>The element is made up of three elements:<ul>
 * <li><b>Premium summary</b> Rendered at the top left of the element, this displays a brief summary of the
 * premium and quotation. As shown in the example above, this section includes a number of values taken
 * from the quote - including the total premium, quote number etc. It also includes a URL link to sample
 * policy wordings (see {@link #getWordingsUrl() wordingsUrl}).<br/>
 * This section also includes a {@link NavigationSection navigation section} which presents the user with 
 * options as to what to do next: <ul>
 * <li>The save quote stores the quote for later reference and returns the user
 * to the product home page if they are logged in. If the user isn't logged in they are presented with
 * a {@link LoginSection LoginSection} (below) from which they can login, create a new user, or request
 * a password reminder.<p><img src="doc-files/QuotationSummary-1.png"/></p></li>
 * <li>The requote button discards this quote, and forwards back into the product's page flow to a page
 * specified by the {@link #getRequoteDestinationPageId() requoteDestinationPageId} property. A new quote
 * is created (and a new quote number is issued), but the data from this current quote is used to 
 * populate the new quote.</li>
 * <li>Confirm and page forward the user to the page specified by the {@link #getConfirmAndPayDestinationPageId()
 * confirmAndPayDestinationPageId} property.</li>
 * <li>View opens a new window with opens the quotation document, or form, in it in the form of a PDF.</li>
 * <li>The Quit button closes the users session, discarding the quote and returning the user to the product
 * home page.</li>
 * </ul>
 * <li><b>Terms and conditions</b> The content of this area is taken directly from the URL specified in the
 * {@link #getTermsAndConditionsUrl() termsAndConditionsUrl} property. The content is assumed to be properly
 * formed Html and is parsed by an instance of {@link ParsedUrlContent ParsedUrlContent} so it may include
 * embedded references to quotation data in the format defined {@link ParsedUrlContent here}.</li>
 * <li><b>Cover summary</b>Few restrictions are placed on what PageElements can be included here, but 
 * the intention is that {@link AnswerSection AnswerSections} and {@link AnswerScroller AnswerScrollers}
 * will be used to repeat key information collected during the quotation process. Any page elements 
 * added to the QuotationSummary's {@link #getPageElement() pageElement} list will be rendered in this
 * area.</li>
 * </ul>
 * @see ReferralSummary
 * @see ParsedUrlContent
 * @see AnswerSection
 * @see AnswerScroller
 */
public class QuotationSummary extends PageContainer {
    private static final long serialVersionUID = -4810599045554021748L;
    private NavigationSection navigationSection;
    private LoginSection loginSection;

    /** Externally visible URL pointing at a sample wording. */
    private String wordingsUrl;

    /** 
     * URL pointing at the terms and conditions to be rendered on the page - this is read and 
     * rendered as HTML on the page itself, so it need not be an externally visible URL. The
     * content of the URL is read at runtime and parsed using the same process as is used
     * by the {@link ParsedUrlContent ParsedUrlContent} page element.
     * @see ParsedUrlContent
     */
    private String termsAndConditionsUrl;
    
    /** The ID of a page within the pageflow to forward to when the requote button is selected. */
    private String requoteDestinationPageId;

    /** ID of a page in the pageflow to forward to when the "save quote" button is selected. */
    private String saveDestinationPageId="SavedQuotes";

    /** ID of a page in the pageflow to forward to when the confirm and pay button is selected. */
    private String confirmAndPayDestinationPageId="ConfirmAndPay";
    
    /** Question section rendered immediately after the premium summary */ 
    private PageSection premiumSummaryFooter;

    /**
     * @return the premiumSummaryQuestions
     */
    public PageSection getPremiumSummaryFooter() {
        return premiumSummaryFooter;
    }

    /**
     * @param premiumSummaryQuestions the premiumSummaryQuestions to set
     */
    public void setPremiumSummaryFooter(PageSection premiumSummaryFooter) {
        this.premiumSummaryFooter = premiumSummaryFooter;
    }

    /**
     * Default constructor 
     */
    public QuotationSummary() {
        super();
    }

    /**
     * URL pointing at the terms and conditions to be rendered on the page - this is read and 
     * rendered as HTML on the page itself, so it need not be an externally visible URL. The
     * content of the URL is read at runtime and parsed using the same process as is used
     * by the {@link ParsedUrlContent ParsedUrlContent} page element.
     * @see ParsedUrlContent
     * @return URL pointing to terms and conditions.
     */
    public String getTermsAndConditionsUrl() {
        return termsAndConditionsUrl;
    }

    /**
     * @see #getTermsAndConditionsUrl()
     * @param termsAndConditionsUrl URL pointing to terms and conditions.
     */
    public void setTermsAndConditionsUrl(String termsAndConditionsUrl) {
        this.termsAndConditionsUrl = termsAndConditionsUrl;
    }

    /**
     * Externally visible URL pointing at a sample wording. This URL is included in the 
     * rendered page as an HTML anchor, so the content pointed to must be available
     * to the external client. 
     * @return URL pointing at sample wordings
     */
    public String getWordingsUrl() {
        return wordingsUrl;
    }

    /**
     * @see #getWordingsUrl()
     * @param wordingsUrl URL pointing at sample wordings
     */
    public void setWordingsUrl(String wordingsUrl) {
        this.wordingsUrl = wordingsUrl;
    }

    /**
     * ID of a page in the pageflow to forward to when the confirm and pay button is selected.     
     * @return "confirm and pay" destination page id
     */
    public String getConfirmAndPayDestinationPageId() {
        return confirmAndPayDestinationPageId;
    }

    /**
     * @see #getConfirmAndPayDestinationPageId()
     * @param confirmAndPayDestinationPageId "confirm and pay" destination page id
     */
    public void setConfirmAndPayDestinationPageId(String confirmAndPayDestinationPageId) {
        this.confirmAndPayDestinationPageId = confirmAndPayDestinationPageId;
    }

    /**
     * The ID of a page within the pageflow to forward to when the requote button is selected. 
     * @return "requote" button's destination page id.
     */
    public String getRequoteDestinationPageId() {
        return requoteDestinationPageId;
    }

    /**
     * @see #getRequoteDestinationPageId()
     * @param requoteDestinationPageId "requote" button's destination page id.
     */
    public void setRequoteDestinationPageId(String requoteDestinationPageId) {
        this.requoteDestinationPageId = requoteDestinationPageId;
    }

    /**
     * ID of a page in the pageflow to forward to when the "save quote" button is selected.
     * @return "save quote" button's destination page id.
     */
    public String getSaveDestinationPageId() {
        return saveDestinationPageId;
    }

    /**
     * @see #getSaveDestinationPageId()
     * @param saveDestinationPageId  "save quote" button's destination page id.
     */
    public void setSaveDestinationPageId(String saveDestinationPageId) {
        this.saveDestinationPageId = saveDestinationPageId;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
        model=super.applyRequestValues(request, response, model);
        model=loginSection(model).applyRequestValues(request, response, model);
        model=navigationSection().applyRequestValues(request, response, model);
        if (premiumSummaryFooter!=null) {
            model=premiumSummaryFooter.applyRequestValues(request, response, model);
        }
        return model;
    }

    @Override
    public Type processActions(ActionRequest request, ActionResponse response, Type model) {
        model=super.processActions(request, response, model);
        model=loginSection(model).processActions(request, response, model);
        model=navigationSection().processActions(request, response, model);
        if (premiumSummaryFooter!=null) {
            model=premiumSummaryFooter.processActions(request, response, model);
        }
        return model;
    }

    @Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        boolean ret=false;
        
        ret|=loginSection(model).processValidations(request, response, model);
        
        ret|=super.processValidations(request, response, model);
        
        if (premiumSummaryFooter!=null) {
            ret|=premiumSummaryFooter.processValidations(request, response, model);
        }
        return ret;
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        Quotation quote=(com.ail.openquote.Quotation)model;

        w.printf("<form name='%s' action='%s' method='post'>", getId(), response.createActionURL());

        w.printf("<table width='100%%' cellpadding='15'>");
        w.printf(" <tr>");
        w.printf("  <td>");
        renderPremiumSummary(w, request, response, quote);
        w.printf("  </td>");
        w.printf("  <td rowspan='2' valign='top'>");
        renderCoverSummary(w, request, response, quote);
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf(" <tr>");
        w.printf("  <td align='left' width='50%%'>");
        renderTermsAndConditions(w, request, quote);
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf("</table>");
        
        w.printf("</form>");
        
        return model;
	}

	private void renderPremiumSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote) throws IOException {
        CurrencyAmount premium=quote.getTotalPremium();
 
        w.printf("<table width='100%%'>");
        w.printf("   <tr valign='middle' class='portlet-table-subheader'><td>Your Quotation: %s</td></tr>", premium.toString());
        w.printf("   <tr>");
        w.printf("       <td height='15'></td>");
        w.printf("   </tr>");
        w.printf("   <tr>");
        w.printf("       <td class='portlet-font'>");
        w.printf("           <ul>");
        w.printf("               <li>Your quote number: <b>%s</b></li>", quote.getQuotationNumber());
        w.printf("               <li>This quote is valid for 30 days, until %s.</li>", longDate(quote.getQuotationExpiryDate()));
        
        renderTaxSummary(w, quote);
        
        if (wordingsUrl!=null) {
            w.printf("               <li>A sample of the policy wordings is available <a target='wordings' href='%s'>here</a>.</li>", expandRelativeUrl(wordingsUrl, request, quote.getProductTypeId()));
        }
        w.printf("           </ul>");
        w.printf("       </td>");
        w.printf("   </tr>");

        if (premiumSummaryFooter!=null) {
            w.printf( "<tr>");
            w.printf(  "<td class='portlet-font'>");
            premiumSummaryFooter.renderResponse(request, response, quote);
            w.printf(  "</td>");
            w.printf( "</tr>");
        }

        w.printf( "<tr>");
        w.printf(  "<td class='portlet-font'>");

        navigationSection().renderResponse(request, response, quote);

        w.printf(  "</td>");
        w.printf( "</tr>");
        w.printf( "<tr>");
        w.printf(  "<td>");

        loginSection(quote).renderResponse(request, response, quote);

        w.printf(  "</td>");
        w.printf( "</tr>");
        w.printf("</table>");
	}

    private void renderTaxSummary(PrintWriter w, Quotation quote) {
        Collection<Behaviour> taxLines=quote.getAssessmentSheet().getLinesOfBehaviourType(BehaviourType.TAX).values();

        if (taxLines.size()==1) {
            Behaviour taxLine=taxLines.iterator().next();
            w.printf("<li>This quote is inclusive of ");
            if (taxLine instanceof RateBehaviour) {
                w.printf("%s at %s", taxLine.getReason(), ((RateBehaviour)taxLine).getRate().getRate());
            }
            else if (taxLine instanceof SumBehaviour) {
                w.printf("%s of %s", taxLine.getReason(), ((SumBehaviour)taxLine).getAmount().toString());
            }
            w.printf("</li>");
        }
        else if (taxLines.size()>1) {
            w.printf("<li>This quote is inclusive of:<ul>");
            for(Behaviour taxLine: taxLines) {
                if (taxLine instanceof RateBehaviour) {
                    w.printf("<li>%s at %s</li>", taxLine.getReason(), ((RateBehaviour)taxLine).getRate().getRate());
                }
                else if (taxLine instanceof SumBehaviour) {
                    w.printf("<li>%s of %s</li>", taxLine.getReason(), ((SumBehaviour)taxLine).getAmount().toString());
                }
            }
            w.printf("</ul></li>");
        }
    }

    private void renderCoverSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote) throws IOException {
        w.printf("<table class='portlet-font'>");

        // output the summary sections.
        for(PageElement e: super.getPageElement()) {
            if (e instanceof AnswerSection) {
                w.printf("<tr><td>");
                e.renderResponse(request, response, quote);
                w.printf("</td></tr>");
            }
            w.printf("<tr><td height='15' colspan='2'></td></tr>");
        }

        w.printf("</table>");
    }
    
    private void renderTermsAndConditions(PrintWriter w, RenderRequest request, Quotation quote) {
        w.printf("<table>");
        w.printf("<tr>");
        w.printf("<td class='portlet-font' width='50%%'>");
        
        if (termsAndConditionsUrl!=null) {
            String fullUrl=expandRelativeUrl(termsAndConditionsUrl, request, quote.getProductTypeId());
            
            try {
                expand(w, new URL(fullUrl), quote);
            }
            catch(MalformedURLException e) {
                w.printf("Please contact us on "+quote.getBroker().getQuoteEmailAddress()+" for a copy of our Terms & Conditions.");
                new CoreProxy().logError("Failed to display terms and conditions for quote: '"+quote.getQuotationNumber()+"', product: '"+quote.getProductTypeId()+"' url:'"+fullUrl+"'");
            }
        }
        
        w.printf("</td>");
        w.printf("</tr>");
        w.printf("</table>");
    }

    private PageElement loginSection(Type model) {
        if (loginSection==null) {
            Quotation q=(Quotation)model;
            loginSection=new LoginSection();
            loginSection.setInvitationMessageText("If you have an existing account, please login here. If not, please %s.");
            loginSection.setInvitationLinkText("create one");
            // The page we're forwarding to is just the last part of the product ID, 
            // e.g. for AIL.Demo.MotorPlus, the page is MotorPlus
            loginSection.setForwardToPageName(q.getProductTypeId().substring(q.getProductTypeId().lastIndexOf('.')+1));
            loginSection.setLoginButtonLabel("Login & Save");
        }
        return loginSection;
    }
    
    private PageElement navigationSection() {
        if (navigationSection==null) {
            navigationSection=new NavigationSection();
            
            SaveButtonAction save=new SaveButtonAction();
            save.setDestinationPageId(saveDestinationPageId);
            navigationSection.getPageElement().add(save);
        
            RequoteButtonAction requote=new RequoteButtonAction();
            requote.setLabel("Requote");
            requote.setDestinationPageId(requoteDestinationPageId);
            navigationSection.getPageElement().add(requote);
    
            CommandButtonAction confirmAndPay=new CommandButtonAction();
            confirmAndPay.setLabel("Confirm and Pay");
            confirmAndPay.setDestinationPageId(confirmAndPayDestinationPageId);
            navigationSection.getPageElement().add(confirmAndPay);

            CommandButtonAction view=new ViewQuotationButtonAction();
            view.setLabel("View");
            navigationSection.getPageElement().add(view);
        }
        
        return navigationSection;
    }

    /**
     * @return the navigationSection
     */
    public NavigationSection getNavigationSection() {
        return navigationSection;
    }

    /**
     * @param navigationSection the navigationSection to set
     */
    public void setNavigationSection(NavigationSection navigationSection) {
        this.navigationSection = navigationSection;
    }    
}
