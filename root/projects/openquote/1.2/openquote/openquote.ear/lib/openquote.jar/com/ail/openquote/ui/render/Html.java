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
package com.ail.openquote.ui.render;

import static com.ail.core.Attribute.YES_OR_NO_FORMAT;
import static com.ail.core.Functions.expand;
import static com.ail.openquote.ui.messages.I18N.i18n;
import static com.ail.openquote.ui.util.Functions.expandRelativeUrlToProductUrl;
import static com.ail.openquote.ui.util.Functions.findError;
import static com.ail.openquote.ui.util.Functions.findErrors;
import static com.ail.openquote.ui.util.Functions.hasErrorMarker;
import static com.ail.openquote.ui.util.Functions.hasErrorMarkers;
import static com.ail.openquote.ui.util.Functions.hideNull;
import static com.ail.openquote.ui.util.Functions.longDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.core.TypeEnum;
import com.ail.financial.CurrencyAmount;
import com.ail.financial.DirectDebit;
import com.ail.financial.MoneyProvision;
import com.ail.financial.PaymentCard;
import com.ail.financial.PaymentSchedule;
import com.ail.insurance.policy.AssessmentLine;
import com.ail.insurance.policy.AssessmentNote;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Behaviour;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.CalculationLine;
import com.ail.insurance.policy.Clause;
import com.ail.insurance.policy.FixedSum;
import com.ail.insurance.policy.Marker;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.insurance.policy.Section;
import com.ail.insurance.policy.SumBehaviour;
import com.ail.openquote.CommercialProposer;
import com.ail.openquote.PersonalProposer;
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;
import com.ail.openquote.SavedQuotationSummary;
import com.ail.openquote.ui.Answer;
import com.ail.openquote.ui.AnswerScroller;
import com.ail.openquote.ui.AnswerSection;
import com.ail.openquote.ui.AssessmentSheetDetails;
import com.ail.openquote.ui.AttributeField;
import com.ail.openquote.ui.Blank;
import com.ail.openquote.ui.BrokerQuotationSummary;
import com.ail.openquote.ui.ClauseDetails;
import com.ail.openquote.ui.CommandButtonAction;
import com.ail.openquote.ui.InformationPage;
import com.ail.openquote.ui.Label;
import com.ail.openquote.ui.LoginSection;
import com.ail.openquote.ui.NavigationSection;
import com.ail.openquote.ui.Page;
import com.ail.openquote.ui.PageElement;
import com.ail.openquote.ui.PageFlow;
import com.ail.openquote.ui.PageScript;
import com.ail.openquote.ui.PageSection;
import com.ail.openquote.ui.ParsedUrlContent;
import com.ail.openquote.ui.PaymentDetails;
import com.ail.openquote.ui.PaymentOptionSelector;
import com.ail.openquote.ui.ProposerDetails;
import com.ail.openquote.ui.Question;
import com.ail.openquote.ui.QuestionPage;
import com.ail.openquote.ui.QuestionSection;
import com.ail.openquote.ui.QuestionSeparator;
import com.ail.openquote.ui.QuestionWithDetails;
import com.ail.openquote.ui.QuestionWithSubSection;
import com.ail.openquote.ui.QuotationSummary;
import com.ail.openquote.ui.ReferralSummary;
import com.ail.openquote.ui.RenderingError;
import com.ail.openquote.ui.RowScroller;
import com.ail.openquote.ui.SaveButtonAction;
import com.ail.openquote.ui.SavedQuotations;
import com.ail.openquote.ui.SectionScroller;
import com.ail.openquote.ui.ViewQuotationButtonAction;
import com.ail.openquote.ui.util.Choice;
import com.ail.openquote.ui.util.Functions;
import com.ail.openquote.ui.util.QuotationContext;
import com.ail.party.Title;

@SuppressWarnings("deprecation")
public class Html extends Type implements Renderer {
	private static final long serialVersionUID = 1L;
	private RenderBrokerQuotationSummaryHelper renderBrokerQuotationSummaryHelper=new RenderBrokerQuotationSummaryHelper();
	private RenderAssessmentSheetDetailsHelper renderAssessmentSheetDetailsHelper=new RenderAssessmentSheetDetailsHelper();
	private RenderAttributeFieldHelper renderAttributeFieldHelper=new RenderAttributeFieldHelper();
	private RenderPaymentDetailsHelper renderPaymentDetailsHelper=new RenderPaymentDetailsHelper();
	private RenderQuotationSummaryHelper renderQuotationSummaryHelper=new RenderQuotationSummaryHelper();
	private RenderReferralSummaryHelper renderReferralSummaryHelper=new RenderReferralSummaryHelper();
	    
    /**
     * Render a Java Enumeration as an HTML option list to be used in a select. Note that the
     * enumeration must be based on {@link TypeEnum}.
     * @param clazz Enumeration type
     * @param selected Enum value to show as selected.
     * @return Option list as a string.
     */
    private static String renderEnumerationAsOptions(Class<? extends TypeEnum> clazz, TypeEnum selected) {
        try {
            StringBuffer ret=new StringBuffer();
            TypeEnum[] values;
            values = (TypeEnum[])clazz.getMethod("values").invoke(null);
            
            for(TypeEnum en: values) {
                if (selected!=null && selected.equals(en)) {
                    ret.append("<option selected='yes'>"+en.longName()+"</option>");
                }
                else {
                    ret.append("<option>"+en.longName()+"</option>");
                }
            }
            
            return ret.toString();
        }
        catch(Exception e) {
            throw new AssertionError("Failed to build an option list for: "+clazz.getName()+". Cause was: "+e.toString());
        }
    }

	public Type renderAnswer(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Answer answer, String title, String answerText) throws IOException {
		 w.printf("<tr><td>%s</td><td>%s</td></tr>", title, answerText);
		 
		 return model;
	}

	@SuppressWarnings("unchecked")
	public Type renderAnswerScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerScroller answerScroller) throws IOException {
		int rowCount=0;
		
        for(Iterator it=model.xpathIterate(answerScroller.getBinding()) ; it.hasNext() ; rowCount++) {
            Type t=(Type)it.next();
               
            for (Answer a: answerScroller.getAnswer()) {
                w.printf("<tr>");
                a.renderResponse(request, response, t);
                w.printf("</tr>");
            }

            w.printf("<tr><td height='4' colspan='2'></td></tr>");
        }
        
        return model;
	}

	public Type renderAnswerSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerSection answerSection, String title)	throws IOException {
		w.printf(" <table width='100%%' border='0' cols='2'>");
		
		// output the title row if a title was defined
		if (title!=null) {
		    w.printf("  <tr class='portlet-section-subheader'><td colspan='2'><u>%s</u></td></tr>", title);
		}
		
		for(Answer a: answerSection.getAnswer()) { 
		    model=a.renderResponse(request, response, model);
		}
		
		w.printf("</table>");
		
		return model;
	}

    /**
     * Render the assessment sheet as an HTML table into a specified PrintWriter. AssessmentSheetDetail is a {@link PageElement}
     * so it can be used within a {@link PageFlow}, though this isn't typical. It is used in broker notification emails and
     * from the product development sandpit portlet {@link com.ail.openquote.portlet.SandpitPortlet}.
     * @param w Render HTML is written here.
     * @param model Quote to render the assessment sheet for
     */
	public Type renderAssessmentSheetDetails(PrintWriter w,	RenderRequest request, RenderResponse response, Type model,	AssessmentSheetDetails assessmentSheetDetails) throws IOException {
        Quotation quote=(Quotation)model;

        w.printf("<table width='100%%'>");
        w.printf(  "<tr width='100%%'>");
        w.printf(    "<td>");
        w.printf(      "<table width='100%%' class='portlet-section-header'");
        w.printf(        "<tr width='100%%'>");
        w.printf(          "<td>"+i18n("i18n_assessment_sheet_details_title")+"</td>", quote.getQuotationNumber());
        w.printf(          "<td align='right'>");
        w.printf(            "<table>");
        w.printf(              "<tr><td class='portlet-font'>"+i18n("i18n_assessment_sheet_details_product_title")+"</td><td class='portlet-font'>%s</td></tr>", quote.getProductName());
        w.printf(              "<tr><td class='portlet-font'>"+i18n("i18n_assessment_sheet_details_status_title")+"</td><td class='portlet-font'>%s</td></tr>", quote.getStatusAsString());
        w.printf(              "<tr><td class='portlet-font'>"+i18n("i18n_assessment_sheet_details_total_premium_title")+"</td><td class='portlet-font'>%s</td></tr>", renderAssessmentSheetDetailsHelper.totalPremium(quote));
        w.printf(            "</table>");
        w.printf(          "</td>");
        w.printf(        "</tr>");
        w.printf(      "</table>");
        w.printf(    "</td>");
        w.printf(  "</tr>");
        w.printf(  "<tr>");
        w.printf(    "<td height='10'></td>");
        w.printf(  "</tr>");
        w.printf(  "<tr>");
        w.printf(    "<td colspan='2'>");
        w.printf(      "<table width='100%%' style='border-collapse: collapse;'>");

        renderAssessmentSheetDetailsHelper.renderAssessmentSheet(w, i18n("i18n_assessment_sheet_details_policy_title"), quote.getAssessmentSheet());

        w.printf(  "<tr>");
        w.printf(    "<td height='10'></td>");
        w.printf(  "</tr>");

        for(Section s: quote.getSection()) {
        	renderAssessmentSheetDetailsHelper.renderAssessmentSheet(w, i18n("i18n_assessment_sheet_details_section_title")+" "+s.getSectionTypeId(), s.getAssessmentSheet());
        }
        
        w.printf(      "</table>");
        w.printf(    "</td>");
        w.printf(  "</tr>");
        w.printf("</table>");

        return model;
	}

	public Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad) throws IOException {
	    Pattern formattedCurrencyPattern=Pattern.compile("([^0-9,.']*)([0-9,.']*)([^0-9,.']*)");

	    String onChangeEvent=(onChange!=null) ? "onchange='"+onChange+"'" : "";
	    
	    Attribute attr=(Attribute)model;
	    
	    try {
	        w.printf("<table><tr>");
	        
	        if (model==null) {
	            w.printf("<td width='25px'>&nbsp;</td><td><b>undefined: %s</b></td>", boundTo);
	        }
	        else {
	            if (attr.isStringType()) {
	                String size=attr.getFormatOption("size");
	                size=(size!=null) ? "size='"+size+"'" : "";                
	                w.printf("<td width='25px'>&nbsp;</td><td><input name=\"%s\" class='portlet-form-input-field' %s %s type='text' value='%s'/></td>", id, size, onChangeEvent, attr.getValue());
	            }
	            else if (attr.isNumberType()) {
	                String pattern=attr.getFormatOption("pattern");
	                String trailer=(attr.getFormat().endsWith("percent")) ? "%" : ""; 
	                int size=(pattern==null) ? 7 : pattern.length();
	                w.printf("<td width='25px'>&nbsp;</td><td><input name=\"%s\" class='portlet-form-input-field' style='text-align:right' size='%d' %s type='text' value='%s'/>%s", id, size, onChangeEvent, attr.getValue(), trailer);
	            }
	            else if (attr.isCurrencyType()) {
	            	String value, pre, post;

	            	// If we can get the formatted value from the currency, this will give use all the locale specific symbols
	            	// to place before or after the value. However, as Attributes are allowed to hold invalid values, we will 
	            	// adopt the fall back position of of using the value as the fields content with nothing to the left of the
	            	// field, and the currency's ISO on the right.
	            	try {
	            		Matcher m=formattedCurrencyPattern.matcher(attr.getFormattedValue());
	            		m.matches();
	            		pre=m.group(1);
	            		value=m.group(2);
	            		post=m.group(3);
	            	}
	            	catch(Throwable e) {
		            	pre="&nbsp;";
		            	value=attr.getValue();
		            	post=attr.getUnit();
	            	}
	                
	            	w.printf("<td align='right' width='25px'>%s</td><td><input name=\"%s\" class='portlet-form-input-field' style='text-align:right' %s size='10' type='text' value='%s'/>%s</td>", pre, id, onChangeEvent, value, post);
	            }
	            else if (attr.isChoiceMasterType()) {
	            	onLoad="loadChoiceOptions($this,$value,"+attr.getChoiceTypeName()+")";
	            	onChange="updateSlaveChoiceOptions(findElementsByName('"+id+"')[0], "+attr.getChoiceTypeName()+", '"+attr.getId()+"', '"+attr.getChoiceSlave()+"');";
	                w.printf("<td width='25px'>&nbsp;</td><td><select name=\"%s\" onchange=\"%s\" class='pn-normal'/></td>", id, onChange);
	            }
	            else if (attr.isChoiceSlaveType()) {
	            	onLoad="loadSlaveChoiceOptions($this,$value,"+attr.getChoiceTypeName()+",'"+attr.getChoiceMaster()+"','"+attr.getId()+"')";
	                w.printf("<td width='25px'>&nbsp;</td><td><select name=\"%s\" class='pn-normal'/></td>", id);
	            }
	            else if (attr.isChoiceType()) {
                    w.printf("<td width='25px'>&nbsp;</td><td>");

                    if (attr.getFormatOption("type")==null) {
	                	if ("radio".equals(attributeField.getRenderHint())) {
	                        String[] opts=attr.getFormatOption("options").split("[|#]");


	                        for(int i=1 ; i<opts.length ; i+=2) {
	                        	if (!"?".equals(opts[i])) {
	                        		w.printf("<input type='radio' name=\"%s\" value='%s' %s>%s</input>&nbsp;&nbsp;", id, opts[i], (opts[i].equals(attr.getValue())) ? "checked='checked'" : "", opts[i]);
	                        	}
	                        }
	                	}
	                	else {
	                		w.printf("<select name=\"%s\" class='pn-normal' %s>%s</select>", id, onChangeEvent, renderAttributeFieldHelper.renderEnumerationAsOptions(attr.getFormatOption("options"), attr.getValue()));
	                	}
	                }
	                else {
	                	onLoad="loadChoiceOptions($this,$value,"+attr.getChoiceTypeName()+")";
	                    w.printf("<select name=\"%s\" class='pn-normal'/>", id);
	                }

                    w.printf("</td>");
	            }
	            else if (attr.isDateType()) {
	                String dateFormat=attr.getFormatOption("pattern");
	                int size=(dateFormat==null) ? 10 : dateFormat.length();
	                w.printf("<td width='25px'>&nbsp;</td><td><input name=\"%s\" class='portlet-form-input-field' %s size='%d' type='text' value='%s'/></td>", id, onChangeEvent, size, attr.getValue());
	            }
	            else if (attr.isYesornoType()) {
                    w.printf("<td width='25px'>&nbsp;</td><td>");
	            	if ("checkbox".equals(attributeField.getRenderHint())) {
	            		w.printf("<input name=\"%s\" type='checkbox' value='Yes' class='pn-normal' %s %s/>", id, ("Yes".equals(attr.getValue())) ? "checked='checked'" : "", onChangeEvent);
	            	}
	            	else if("radio".equals(attributeField.getRenderHint())) {
	            		w.printf("<input name=\"%s\" type='radio' value='No' class='pn-normal' %s %s>No</input>&nbsp;&nbsp;", id, ("No".equals(attr.getValue())) ? "checked='checked'" : "", onChangeEvent);
	            		w.printf("<input name=\"%s\" type='radio' value='Yes' class='pn-normal' %s %s>Yes</input>&nbsp;&nbsp;", id, ("Yes".equals(attr.getValue())) ? "checked='checked'" : "", onChangeEvent);
	            	}
	            	else {
	            		w.printf("<select name=\"%s\" class='pn-normal' %s>%s</select>", id, onChangeEvent, renderAttributeFieldHelper.renderEnumerationAsOptions(YES_OR_NO_FORMAT, attr.getValue()));
	            	}
                    w.printf("</td>");
	            }
	            else if (attr.isNoteType()) {
	                w.printf("<td width='25px'>&nbsp;</td><td><textarea name=\"%s\" class='portlet-form-input-field' %s rows='3' style='width:100%%'>%s</textarea></td>", id, onChangeEvent, attr.getValue());
	            }
	            
	            w.printf("<td class='portlet-msg-error'>%s</td></tr></table>", Functions.findErrors(model, attributeField));
	    
	            if (onLoad!=null) {
	                String s=onLoad;
	    
	                if (s.contains("$this")) {
	                    s=s.replace("$this", "findElementsByName(\""+id+"\")[0]");
	                }
	    
	                if (s.contains("$value")) {
	                    s=s.replace("$value", "'"+attr.getValue().replace("'", "\\'")+"'");
	                }
	                
	                w.printf("<script type='text/javascript'>%s</script>", s);
	            }
	        }
		}
		catch(Throwable t) {
			throw new RenderingError("Failed to render attribute id:'"+attr.getId()+"', format:'"+attr.getFormat()+"' value:'"+attr.getValue()+"'", t);
		}
		
		return model;
	}

	public Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad, String title, String styleClass, String ref) throws IOException {
		return renderAttributeField(w, request, response, model, attributeField, boundTo, id, onChange, onLoad);
		}

	
	public Type renderAttributeFieldPageLevel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id) throws IOException {
		Attribute attr=(Attribute)model;
		
	    if (attr.isChoiceType() && !attr.isChoiceSlaveType() && attr.getFormatOption("type")!=null) {
	        String optionTypeName=attr.getFormatOption("type");
	        
	        Choice choice=(Choice)(new CoreProxy().newProductType(QuotationContext.getQuotation().getProductTypeId(), optionTypeName));
	        w.print(choice.renderAsJavaScriptArray(optionTypeName));
	    }
	
		return model;
	}
	
	public Type renderBlank(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Blank blank) throws IOException {
		w.write("&nbsp;");
		return model;
	}

    /**
     * Render the page element. This method is split out from {@link #renderResponse(RenderRequest, RenderResponse, Type) renderResponse()}
     * to allow it to be used outside of a PageFlow - i.e. for inclusion in broker notification emails.
     * @param w Writer to render to.
     * @param model Model (Quotation) to render data for.
     */
    public Type renderBrokerQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Type model, BrokerQuotationSummary brokerQuotationSummary) {
        Quotation quote=(com.ail.openquote.Quotation)model;
        
        w.printf("<table width='100%%' align='center'>");
        w.printf(  "<tr>");
        w.printf(    "<td>");
        w.printf(      "<table width='100%%' style='border-collapse: collapse;'>");
        w.printf(        "<tr class='portlet-section-header'>");
        w.printf(           "<td>%s: %s</td>",quote.getStatus().longName(), quote.getQuotationNumber());
        renderBrokerQuotationSummaryHelper.renderPremium(w, quote);
        w.printf(        "</tr>");
        w.printf(        "<tr>");
        w.printf(          "<td colspan='2'>");
        w.printf(            "<table cellpadding='15' width='100%%'>");
        w.printf(              "<tr>");
        w.printf(                "<td colspan='2' class='portlet-font' valign='top'>");
        renderBrokerQuotationSummaryHelper.renderSummary(w, quote);

        switch(quote.getStatus()) {
        case SUBMITTED:
            w.printf("<br/>");
            renderBrokerQuotationSummaryHelper.renderPayments(w, quote);
            // fall through
        case QUOTATION:
            w.printf("<br/>");
            renderBrokerQuotationSummaryHelper.renderPremiumDetail(w, quote);
            break;                
        default:
        	w.printf("<br/>");
        	renderBrokerQuotationSummaryHelper.renderAssessmentSummary(w, quote);
        }
                
        w.printf(                "</td>");
        w.printf(                "<td rowspan='2' valign='top' align='center'>");
        renderBrokerQuotationSummaryHelper.renderProposerDetails(w, quote);
        w.printf(                "</td>");
        w.printf(              "</tr>");
        w.printf(            "</table>");
        w.printf(          "</td>");
        w.printf(        "</tr>");
        w.printf(      "</table>");
        w.printf(    "</td>");
        w.printf(  "</tr>");
        w.printf("</table>");
        
        return model;
    }

    public Type renderCommandButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label, boolean immediate) {
        w.printf("<input type='submit' name='op=%s:immediate=%b' value='%s' class='portlet-form-input-field'/>", label, immediate, i18n(label));
    	return model;
    }
    
    public Type renderLabel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Label label, String format, Object[] params) {
    	w.printf(format, params);
    	return model;
    }
    
    public Type renderInformationPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, InformationPage informationPage, String title, List<PageElement> pageElements) throws IOException {
        w.printf(" <table width='100%%' border='0' cols='1'>");

        if (title!=null) {
            w.printf("<tr class='portlet-section-header'><td>%s</td></tr>", title);
        }

        for (PageElement e : pageElements) {
            w.printf("<tr><td>");
            model=e.renderResponse(request, response, model);
            w.printf("</td></tr>");
        }
        
        w.printf(" </table>");

        return model;
    }
    
    public Type renderLoginSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, LoginSection loginSection, String usernameGuess, String nameOfForwardToPortal) {
        String lnk="<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Create Login\");'>"+loginSection.getInvitationLinkText()+"</a>";
        
        w.printf("<table>");
        w.printf( "<tr>");
        w.printf(  "<td align='center'>");

        // Div #1: login and save the quote. Note: if the usernameGuess is a known user we'll pre-populate the form with 
        // the guess to save the user some typing; otherwise we'll leave the username field blank.
        w.printf(   "<div class='portlet-font' id='Proposer Login'>");
        w.printf(    loginSection.getInvitationMessageText(), lnk);
        w.printf(    "<form method='post' action='%s' name='loginform' id='loginForm'>", response.createActionURL());
        w.printf(     "<table>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value='%s'/></td>", usernameGuess);
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("username", model, loginSection));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td valign='center'>"+i18n("i18n_login_section_password_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='password' name='password' id='password' value=''/></td>");
        w.printf(       "<td><a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Forgotten Password\");'>Forgotten password?</a></td>");
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td colspan='3'><input type='submit' id='loginButton' class='portlet-form-input-field' name='op=%1$s:page=%2$s:portal=%3$s' value='%1$s'/></td>", i18n(loginSection.getLoginButtonLabel()), loginSection.getForwardToPageName(), nameOfForwardToPortal);
        w.printf(      "</tr>");
        w.printf(     "</table>");
        w.printf(    "</form>");
        w.printf(   "</div>");

        // Div #2: create a new user and save quote
        w.printf(   "<div class='portlet-font' id='Create Login'>");
        w.printf(    "Create a new account here. If you have an existing account, please <a onClick='showDivDisplay(\"Proposer Login\");hideDivDisplay(\"Create Login\");'>login here</a>.");
        w.printf(    "<form method='post' action='%s'>", response.createActionURL());
        w.printf(     "<table>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("username", model, loginSection));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_confirm_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='cusername' id='cusername' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("cusername", model, loginSection));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td valign='center'>"+i18n("i18n_login_section_password_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='password' name='password' id='password' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("password", model, loginSection));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td valign='center'>"+i18n("i18n_login_section_confirm_password_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='password' name='cpassword' id='cpassword' value=''/></td>");
        w.printf(       "<td class='portlet-msg-error'>%s</td>", findError("cpassword", model, loginSection));
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td colspan='3'><input type='submit' id='createLoginButton' class='portlet-form-input-field' name='op=Create:page=%s:portal=%s' value='"+i18n("i18n_login_section_create_and_save_button_label")+"'/></td>", loginSection.getForwardToPageName(), nameOfForwardToPortal);
        w.printf(      "</tr>");
        w.printf(     "</table>");
        w.printf(    "</form>");
        w.printf(   "</div>");

        // Div #3: Send a password reminder
        w.printf(   "<div class='portlet-font' id='Forgotten Password'>");
        w.printf(    i18n("i18n_login_section_email_password_message"));
        w.printf(    "<form method='post'>");
        w.printf(     "<table>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td>"+i18n("i18n_login_section_username_label")+"</td>");
        w.printf(       "<td><input class='portlet-form-input-field' type='text' name='username' id='username' value='%s'/></td>",  usernameGuess);
        w.printf(      "</tr>");
        w.printf(      "<tr class='portlet-font'>");
        w.printf(       "<td colspan='2'><input type='submit' id='email' class='portlet-form-input-field' name='op=Reminder' value='"+i18n("i18n_login_section_send_reminder_button_label")+"'/></td>");
        w.printf(      "</tr>");
        w.printf(     "</table>");
        w.printf(    "</form>");
        w.printf(   "</div>");

        w.printf(  "</td>");
        w.printf( "</tr>");
        w.printf("</table>");

        w.printf("<script type='text/javascript'>");

        // hide the 'create login' form unless there's an error associated with it.
        if (hasErrorMarker("create", model)) {
            w.printf( "hideDivDisplay('Forgotten Password');");
            w.printf( "hideDivDisplay('Proposer Login');");
            w.printf( "showDivDisplay('Create Login');");
        }
        else if (hasErrorMarker("login", model)) {
            w.printf( "hideDivDisplay('Create Login');");
            w.printf( "hideDivDisplay('Forgotten Password');");
            w.printf( "showDivDisplay('Proposer Login');");
        }
        else {
            w.printf( "hideDivDisplay('Create Login');");
            w.printf( "hideDivDisplay('Forgotten Password');");
            w.printf( "hideDivDisplay('Proposer Login');");
        }

        w.printf("</script>");

        return model;
    }
    
    public Type renderNavigationSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, NavigationSection navigationSection) throws IllegalStateException, IOException {
        w.print("<table width='100%' border='0' align='center'><tr>");
        w.print("<td width='15%'>&nbsp;</td>");
        w.print("<td width='70%' align='center'>");
        for(PageElement element: navigationSection.getPageElement()) {
			model=element.renderResponse(request, response, model);
		}
		w.print("</td>");
        w.print("<td width='15%' align='right'>");
        if (navigationSection.isQuitDisabled()) {
            w.print("&nbsp;");
        }
        else {
            model=navigationSection.getQuitButton().renderResponse(request, response, model);
        }
        w.print("</td></tr></table>");

        return model;
    }
    
    public Type renderPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Page page) {
        w.printf("<script type='text/javascript' src='/quotation/jscript/tiny_mce/tiny_mce.js'/>");        

        // Always include the openquote script
        w.printf("<script type='text/javascript' src='/quotation/jscript/openquote.js'/>");
        
    	return model;
    }
    
    public Type renderPageScript(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageScript pageScript) {
        if (pageScript.getScript()!=null) {
            w.printf("<script type='text/javascript'>%s</script>", pageScript.getScript());
        }
        else {
            w.printf("<script type='text/javascript' src='%s'/>", pageScript.getCanonicalUrl());
        }
    	return model;
    }
    
    public Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title, String styleClass, String ref) throws IllegalStateException, IOException {
    	return renderPageSection(w, request, response, model, pageSection, title);
    }
    
    public Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title) throws IllegalStateException, IOException {
        w.printf(" <table width='100%%' border='0' cols='%d'>", pageSection.getColumns());

        // Output the section title if there is one.
        if (!Functions.isEmpty(title)) {
            w.printf("  <tr class='portlet-section-subheader'><td colspan='%d'>%s</td></tr>", pageSection.getColumns(), title);
        }

        Iterator<? extends PageElement> it=pageSection.getPageElement().iterator();
        
        while(it.hasNext()) {
            w.printf("<tr>");
            for(int col=0 ; col<pageSection.getColumns() ; col++) {
                w.printf("<td>");
                if (it.hasNext()) {
                    model=it.next().renderResponse(request, response, model);
                }
                else {
                    w.printf("&nbsp;");
                }
                w.printf("</td>");
            }
            w.printf("</tr>");
        }
        w.printf("</table>");
        
        return model;
    }
    
    public Type renderParsedUrlContent(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ParsedUrlContent parsedUrlContent, String content) {
        w.printf("<table>");
        w.printf("<tr>");
        w.printf("<td class='portlet-font' width='100%%'>");
        
        w.print(content);
        
        w.printf("</td>");
        w.printf("</tr>");
        w.printf("</table>");
        
        return model;
    }
    
    public Type renderPaymentDetails(PrintWriter w, RenderRequest request, RenderResponse response, Quotation model, PaymentDetails paymentDetails) {
        w.printf("<table cellpadding='4' width='100%%'>");
        renderPaymentDetailsHelper.renderSummary(w, model, paymentDetails);
        renderPaymentDetailsHelper.renderPaymentDetails(w, model, paymentDetails);
        renderPaymentDetailsHelper.renderSubmit(w, model, paymentDetails);
        w.printf("</table>");
        
    	return model;
    }
    
    public Quotation renderPaymentOptionSelector(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quotation, PaymentOptionSelector paymentOptionSelector) {
        w.printf("<table cellpadding='4' width='100%%' col='2'>");
        w.printf("   <tr class='portlet-font'>");
        w.printf("       <td class='portlet-section-alternate' colspan='2'>"+i18n("i18n_payment_option_selector_title")+"</td>");
        w.printf("   </tr>");       

        // output the error if there is one
        w.printf("<tr><tr><td>&nbsp;</td><td align='center' class='portlet-msg-error'>%s</td></tr>", findError("paymentDetails", quotation, paymentOptionSelector));
        
        w.printf("   <tr><td colspan='2' height='15'><hr/></td></tr>");

        // loop through the options outputting each to its own row
        for(PaymentSchedule ps: quotation.getPaymentOption()) {
            w.printf("   <tr class='portlet-font'>");
            w.printf("       <td class='portal-section'>");
            w.printf("           <b>%s</b>", ps.getDescription());
            if (ps.getMoneyProvision().size() > 1) {
                for(MoneyProvision mp: ps.getMoneyProvision()) {
                    w.printf("<br/>%s", mp.getDescription());
                }
            }
            w.printf("       </td>");
            w.printf("       <td align='center'><input name='selectedOption' value='%d' %s type='radio'/></td>", 
                 ps.hashCode(), (quotation.getPaymentDetails()==ps) ? "checked='yes'" : "");
            w.printf("   </tr>");
            w.printf("   <tr><td colspan='2'><hr/></td></tr>");
        }

        w.printf("</table>");

        return quotation;
    }

    public Proposer renderProposerDetails(PrintWriter w, RenderRequest request, RenderResponse response, Proposer proposer, ProposerDetails proposerDetails) {
        w.printf("<table width='100%%' border='0' cols='6'>");
        w.printf( "<tr><td height='15' colspan='6'>&nbsp;</td></tr>");
        
        if (proposer instanceof PersonalProposer) {
            w.printf( "<tr class='portlet-font'>");
            w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_title_label")+"</td>");
            w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'>");
	        w.printf(    "<tr>");
	        w.printf(     "<td>");
	        w.printf(      "<select id='title' name='title' class='pn-normal' class='portlet-form-input-field', onchange='disableTargetIf(this.options[this.selectedIndex].text!=\"Other\", \"otherTitle\")'>%s</select>", renderEnumerationAsOptions(Title.class, proposer.getTitle()));
	        w.printf(     "</td>");
	        w.printf(     "<td class='portlet-msg-error'>%s</td>", findError("title", proposer.getInstance(), proposerDetails));
	        w.printf(    "</tr>");
	        w.printf(   "</table>");
	        w.printf(  "</td>");
	        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_other_title_label")+"</td>");
	        w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'><tr>");
	        w.printf(    "<td>");
	        w.printf(     "<input name='otherTitle' id='otherTitle' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", hideNull(proposer.getOtherTitle()));
	        w.printf(    "</td>");
	        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("otherTitle", proposer.getInstance(), proposerDetails));
	        w.printf(   "</tr></table>");
	        w.printf(  "</td>");
	        w.printf( "</tr>");

	        w.printf( "<tr class='portlet-font'>");
	        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_first_name_label")+"</td>");
	        w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'><tr>");
	        w.printf(    "<td>");
	        w.printf(     "<input name='firstname' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getFirstName());
	        w.printf(    "</td>");
	        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("firstName", proposer.getInstance(), proposerDetails));
	        w.printf(   "</tr></table>");
	        w.printf(  "</td>");
	        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_surname_label")+"</td>");
	        w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'><tr>");
	        w.printf(    "<td>");
	        w.printf(     "<input name='surname' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getSurname());
	        w.printf(    "</td>");
	        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("surname", proposer.getInstance(), proposerDetails));
	        w.printf(   "</tr></table>");
	        w.printf(  "</td>");
	        w.printf( "</tr>");
        }
        else if (proposer instanceof CommercialProposer) {
            w.printf( "<tr class='portlet-font'>");
            w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_company_label")+"</td>");
            w.printf(  "<td colspan='4'>");
            w.printf(   "<table border='0'><tr>");
            w.printf(    "<td>");
            w.printf(     "<input name='companyName' class='portlet-form-input-field' type='text' value='%s' size='100'>", (((CommercialProposer)proposer).getCompanyName()));
            w.printf(    "</td>");
            w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("companyName", proposer.getInstance(), proposerDetails));
            w.printf(   "</tr></table>");
            w.printf(  "</td>");
            w.printf( "</tr>");
        }
                
        w.printf( "<tr><td height='15'></td></tr>");
        
        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_address_label")+"</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='address1' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getLine1());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("address1", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf( "</tr>");
        
        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td>&nbsp;</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='address2' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getLine2());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("address2", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf( "</tr>");
        
        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td>&nbsp;</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='town' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getTown());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("town", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf( "</tr>");
        
        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td>&nbsp;</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='county' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getAddress().getCounty());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("county", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf( "</tr>");

        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td>&nbsp;</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='postcode' class='portlet-form-input-field' type='text' value='%s' size='8' maxlength='8'>", proposer.getAddress().getPostcode());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("postcode", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf( "</tr>");
        
        w.printf( "<tr><td height='15'></td></tr>");
        
        w.printf("</table>");

        w.printf("<table border='0' cols='6'>");

        if (proposer instanceof CommercialProposer) {
            w.printf( "<tr><td height='15'></td></tr>");
            w.printf( "<tr><td class='portlet-section-subheader' colspan='4'>Contact details</td></tr>");
            w.printf( "<tr class='portlet-font'>");
            w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_title_label")+"</td>");
            w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'>");
	        w.printf(    "<tr>");
	        w.printf(     "<td>");
	        w.printf(      "<select id='title' name='title' class='pn-normal' class='portlet-form-input-field', onchange='disableTargetIf(this.options[this.selectedIndex].text!=\"Other\", \"otherTitle\")'>%s</select>", renderEnumerationAsOptions(Title.class, proposer.getTitle()));
	        w.printf(     "</td>");
	        w.printf(     "<td class='portlet-msg-error'>%s</td>", findError("title", proposer.getInstance(), proposerDetails));
	        w.printf(    "</tr>");
	        w.printf(   "</table>");
	        w.printf(  "</td>");
	        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_other_title_label")+"</td>");
	        w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'><tr>");
	        w.printf(    "<td>");
	        w.printf(     "<input name='otherTitle' id='otherTitle' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", hideNull(proposer.getOtherTitle()));
	        w.printf(    "</td>");
	        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("otherTitle", proposer.getInstance(), proposerDetails));
	        w.printf(   "</tr></table>");
	        w.printf(  "</td>");
	        w.printf( "</tr>");

	        w.printf( "<tr class='portlet-font'>");
	        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_first_name_label")+"</td>");
	        w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'><tr>");
	        w.printf(    "<td>");
	        w.printf(     "<input name='firstname' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getFirstName());
	        w.printf(    "</td>");
	        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("firstName", proposer.getInstance(), proposerDetails));
	        w.printf(   "</tr></table>");
	        w.printf(  "</td>");
	        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_surname_label")+"</td>");
	        w.printf(  "<td colspan='2'>");
	        w.printf(   "<table border='0'><tr>");
	        w.printf(    "<td>");
	        w.printf(     "<input name='surname' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getSurname());
	        w.printf(    "</td>");
	        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("surname", proposer.getInstance(), proposerDetails));
	        w.printf(   "</tr></table>");
	        w.printf(  "</td>");
	        w.printf( "</tr>");
        }
        
        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_telephone_label")+"</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='phone' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getTelephoneNumber());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("phone", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf(  "<td colspan='4'>&nbsp;</td>");
        w.printf( "</tr>");

        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_mobilephone_label")+"</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='mobile' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getMobilephoneNumber());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("mobile", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf(  "<td colspan='4'>&nbsp;</td>");
        w.printf( "</tr>");

        w.printf( "<tr class='portlet-font'>");
        w.printf(  "<td class='portal-form-label'>"+i18n("i18n_proposer_details_email_label")+"</td>");
        w.printf(  "<td colspan='2'>");
        w.printf(   "<table border='0'><tr>");
        w.printf(    "<td>");
        w.printf(     "<input name='email' class='portlet-form-input-field' type='text' value='%s' size='30' maxlength='100'>", proposer.getEmailAddress());
        w.printf(    "</td>");
        w.printf(    "<td class='portlet-msg-error'>%s</td>", findError("email", proposer.getInstance(), proposerDetails));
        w.printf(   "</tr></table>");
        w.printf(  "</td>");
        w.printf(  "<td colspan='4'>&nbsp;</td>");
        w.printf( "</tr>");

        w.printf( "<tr><td height='15' colspan='6'>&nbsp;</td></tr>");

        // Disable the 'otherTitle' field on page load if 'title' isn't 'Other'.
        w.printf( "<script type='text/javascript'>\n");
        w.printf( "elm=findElementsByName(\"title\")[0];\n");
        w.printf( "val=elm.options[elm.selectedIndex].text;\n");
        w.printf( "disableTargetIf(val!=\"Other\", \"otherTitle\");\n");
        w.printf( "</script>");

        w.printf("</table>");
        
        return proposer;
    }
    
    public Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext) throws IllegalStateException, IOException {
        w.printf("<td>%s</td>", title);
        w.printf("<td colspan='3' align='left'>%s</td>", question.renderAttribute(request, response, model, question.getBinding(), rowContext, question.getOnChange(), question.getOnLoad()));
    	return model;
    }

    public Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext, String styleClass, String ref) throws IllegalStateException, IOException {
        model = renderQuestion(w, request, response, model, question, title, rowContext);
    	return model;
    }    
    
    public Type renderQuestionPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionPage questionPage, String title) throws IllegalStateException, IOException {
        w.printf("<form name='%s' action='%s' method='post'>", questionPage.getId(), response.createActionURL());
        w.printf(" <table width='100%%' border='0' cols='1'>");

        if (title!=null) {
            w.printf("  <tr class='portlet-section-header'><td>%s</td></tr>", title);
        }

        for (PageElement e : questionPage.getPageElement()) {
            w.printf("<tr><td>");
            model=e.renderResponse(request, response, model);
            w.printf("</td></tr>");
        }

        w.printf(" </table>");
        w.printf("</form>");
        
    	return model;
    }
    
    public Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title, String styleClass, String ref) throws IllegalStateException, IOException {
    	return renderQuestionSection(w, request, response, model, questionSection, title);
    }
    
    public Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title) throws IllegalStateException, IOException {
        w.printf(" <table width='100%%' border='0' cols='4' cellpadding='4'>");

        // output the title row if a title was defined
        if (title!=null) {
            w.printf("  <tr class='portlet-section-subheader'><td colspan='4'>%s</td></tr>", title);
        }

        Iterator<? extends Question> it=questionSection.getQuestion().iterator();
        
        while(it.hasNext()) {
            w.printf("<tr>");
            it.next().renderResponse(request, response, model);
            w.printf("</tr>");
        }

        w.printf("</table>");

        return model;
    }

    public Type renderQuestionSeparator(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSeparator questionSeparator, String title) {
        if (title==null) {
            if (questionSeparator.getBinding()!=null && hasErrorMarkers(model.xpathGet(questionSeparator.getBinding(), Type.class))) {
        		w.printf("<td class='portlet-section-subheader' colspan='4'>%s</td>", findErrors(model.xpathGet(questionSeparator.getBinding(), Type.class), questionSeparator));
        	}
        	else {
        		w.printf("<td class='portlet-section-subheader' colspan='4'>&nbsp;</td>");
        	}
        }
        else {
            w.printf("<td colspan='4'><table width='100%%''>"); 
            w.printf("<tr><td class='portlet-section-subheader' colspan='4'>%s</td></tr>", Functions.hideNull(title)); 
            if (questionSeparator.getBinding()!=null && hasErrorMarkers(model.xpathGet(questionSeparator.getBinding(), Type.class))) {
            	w.printf("<tr><td class='portlet-msg-error' colspan='4'>%s</td>", findErrors(model.xpathGet(questionSeparator.getBinding(), Type.class), questionSeparator));
            }
            w.printf("</table></td>");
        }

        return model;
    }

    public Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId, String styleClass, String ref) throws IllegalStateException, IOException {
    	return renderQuestionWithDetails(w, request, response, model, questionWithDetails, title, detailTitle, rowContext, questionId, detailId);
    }
    
    public Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId) throws IllegalStateException, IOException {
    	String onChange = null;

    	if ("radio".equals(questionWithDetails.getRenderHint())) {
        	onChange="enableTargetIf(this.checked && isInList(this.value, \""+questionWithDetails.getDetailsEnabledFor()+"\"), \""+detailId+"\")";
        }
        else if ("checkbox".equals(questionWithDetails.getRenderHint())) {
        	onChange="enableTargetIf(this.checked, \""+detailId+"\")";
        }
        else {
        	onChange="enableTargetIf(isInList(this.options[this.selectedIndex].text, \""+questionWithDetails.getDetailsEnabledFor()+"\"), \""+detailId+"\")";
        }
        
        w.printf("<td>%s</td>", title);
        w.printf("<td>%s</td>", questionWithDetails.renderAttribute(request, response, model, questionWithDetails.getBinding(), rowContext, onChange, questionWithDetails.getOnLoad()));
        w.printf("<td>%s</td>", detailTitle);
        w.printf("<td>%s</td>", questionWithDetails.renderAttribute(request, response, model, questionWithDetails.getDetailsBinding(), rowContext, questionWithDetails.getOnChange(), questionWithDetails.getOnLoad()));
        
        // Disable the 'detail' textarea unless the question's answer is 'Yes'
        if ("radio".equals(questionWithDetails.getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
            		   "radio=findElementsByName(\"%s\")[1];"+
            		   "enableTargetIf(radio.checked, \"%s\")"+
            		 "</script>",
                    questionId, detailId);
        }
        else if ("checkbox".equals(questionWithDetails.getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
            		   "cbox=findElementsByName(\"%s\")[0];"+
            		   "enableTargetIf(cbox.checked, \"%s\")"+
            		 "</script>",
                    questionId, detailId);
        }
        else {
            w.printf("<script type='text/javascript'>"+
            		   "elem=findElementsByName(\"%s\")[0];"+
            		   "enableTargetIf(isInList(elem.options[elem.selectedIndex].text, \"%s\"), \"%s\")"+
            		 "</script>",
                    questionId, questionWithDetails.getDetailsEnabledFor(), detailId);
        }

        return model;
    }
    
    public Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId, String styleClass, String ref) throws IllegalStateException, IOException {
    	return renderQuestionWithSubSection(w, request, response, model, questionWithSubSection, title, rowContext, questionId);
    }
    
    public Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId) throws IllegalStateException, IOException {
    	String onChange=null;

    	if ("radio".equals(questionWithSubSection.getRenderHint())) {
        	onChange="showHideDivDisplay(this.checked && isInList(this.value, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), "+
        	                            "this.checked && !isInList(this.value, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), "+
        	                            "\""+questionWithSubSection.getId()+"\")";
        }
        else if ("checkbox".equals(questionWithSubSection.getRenderHint())) {
        	onChange="showHideDivDisplay(this.checked, !this.checked, \""+questionWithSubSection.getId()+"\")";
        }
        else {
        	onChange="showHideDivDisplay(isInList(this.options[this.selectedIndex].text, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), "+
        	                           "!isInList(this.value, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), \""+questionWithSubSection.getId()+"\")";
        }
        
        w.printf("<td>%s</td>", title);
        w.printf("<td colspan='3'>%s</td>", questionWithSubSection.renderAttribute(request, response, model, questionWithSubSection.getBinding(), rowContext, onChange, questionWithSubSection.getOnLoad()));
        w.printf("</tr>");
        w.printf("<tr><td colspan='4'>");
        w.printf(" <div id='%s' style='visibility:hidden;display:none'>", questionWithSubSection.getId());
        w.print("   <table width='90%'><tr><td width='5%'/><td>");
        model=questionWithSubSection.getSubSection().renderResponse(request, response, model);
        w.printf("  </td></tr></table>");
        w.printf(" </div>");
        w.printf("</td>");

        // Disable the 'detail' area unless the question's answer is 'Yes'
        if ("radio".equals(questionWithSubSection.getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
                       "radio=findElementsByName(\"%1$s\")[1];" +
                       "showHideDivDisplay(radio.checked && isInList(radio.value, \""+questionWithSubSection.getDetailsEnabledFor()+"\"),"+
                                          "radio.checked && !isInList(radio.value, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), "+
                                          "\"%2$s\");"+
                     "</script>", questionId, questionWithSubSection.getId());
        }
        else if ("checkbox".equals(questionWithSubSection.getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
                       "cbox=findElementsByName(\"%1$s\")[0];" +
                       "showHideDivDisplay(" +
                       "cbox.checked, !cbox.checked, "+
                       "\"%2$s\")"+
                     "</script>", questionId, questionWithSubSection.getId());
        }
        else {
            w.printf("<script type='text/javascript'>"+
                       "opt=findElementsByName(\"%1$s\")[0];" +
                       "showHideDivDisplay(" +
                       "isInList(opt.options[opt.selectedIndex].text, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), "+
                       "!isInList(opt.options[opt.selectedIndex].text, \""+questionWithSubSection.getDetailsEnabledFor()+"\"), "+
                       "\"%2$s\")"+
                     "</script>", questionId, questionWithSubSection.getId());
        }

        return model;
    }

    public Type renderQuitButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
        w.printf("<input type='submit' name='op=quit:immediate=true' value='%1$s' class='portlet-form-input-field'/>", label);
        
    	return model;
    }

    public Quotation renderQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
        w.printf("<form name='%s' action='%s' method='post'>", quotationSummary.getId(), response.createActionURL());

        w.printf("<table width='100%%' cellpadding='15'>");
        w.printf(" <tr>");
        w.printf("  <td>");
        renderQuotationSummaryHelper.renderPremiumSummary(w, request, response, quote, quotationSummary);
        w.printf("  </td>");
        w.printf("  <td rowspan='2' valign='top'>");
        renderQuotationSummaryHelper.renderCoverSummary(w, request, response, quote, quotationSummary);
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf(" <tr>");
        w.printf("  <td align='left' width='50%%'>");
        renderQuotationSummaryHelper.renderTermsAndConditions(w, request, quote, quotationSummary);
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf("</table>");
        
        w.printf("</form>");

        return quote;
    }
    
    public Quotation renderReferralSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, ReferralSummary referralSummary) throws IOException {
        w.printf("<table columns='2' width='100%%' cellpadding='15'>");
        w.printf(" <tr>");
        w.printf("  <td>");
        renderReferralSummaryHelper.renderReferralNotification(w, request, response, quote, referralSummary);
        w.printf("  </td>");
        w.printf("  <td rowspan='2' valign='top' align='center'>");
        renderReferralSummaryHelper.renderRequirementSummary(w, request, response, quote, referralSummary);
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf(" <tr>");
        w.printf("  <td align='center' width='50%%'>");
        w.printf("  </td>");
        w.printf(" </tr>");
        w.printf("</table>");

        return quote;
    }
    
    public Type renderRequoteButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
        w.printf("<input type='submit' name='op=requote' value='%s' class='portlet-form-input-field'/>", label);
    	return model;
    }

    @SuppressWarnings("unchecked")
	public Type renderRowScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, RowScroller rowScroller) throws IllegalStateException, IOException {
        // Start the table for the scroller (size()+1 to allow for the trash can column)
        int columns=rowScroller.isAddAndDeleteEnabled() ? rowScroller.getItem().size()+1 : rowScroller.getItem().size();

        w.printf("<table width='100%%' border='0' cols='%d'>", columns);

        if (rowScroller.getTitle()!=null) {
            w.printf("  <tr class='portlet-section-subheader'><td colspan='%d'>", columns);
            w.print(rowScroller.getExpandedRepeatedTitle(model));
            w.printf("  </td></tr>");
        }
        
        // Output the column headers
        w.printf(" <tr class='portlet-section-alternate'>");
        for(AttributeField a: rowScroller.getItem()) {
            if (a.getExpandedSubTitle(model)!=null) {
                w.printf("<td align='center'><table><tr><td valign='middle' align='center'>%s<br/>%s</td><td>&nbsp;</td></tr></table></td>", i18n(a.getExpandedTitle(model)), i18n(a.getExpandedSubTitle(model)));
            }
            else {
                w.printf("<td align='center'><table><tr><td valign='middle' align='center'>%s</td><td>&nbsp;</td></tr></table></td>", i18n(a.getExpandedTitle(model)));
            }
        }

        // Add a column for the trash can, if it's enabled
        if (rowScroller.isAddAndDeleteEnabled()) {
            w.printf("<td>&nbsp;</td>");
            w.printf(" </tr>");
        }
        
        // Now output the rows of data
        int rowCount=0;
        for(Iterator it=model.xpathIterate(rowScroller.getBinding()) ; it.hasNext() ; ) {
            Type t=(Type)it.next();
            
            w.printf("<tr>");

            for(AttributeField a: rowScroller.getItem()) {
                w.printf("<td align='center'>");
                a.renderResponse(request, response, t, rowScroller.getBinding()+"["+rowCount+"]");
                w.printf("</td>");
            }

            // Add the trash can for this row if enabled.
            if (rowScroller.isAddAndDeleteEnabled()) {
                if (rowCount>=rowScroller.getMinRows()) {
                    w.printf("<td align='center'>");
                    w.printf("<input type='image' src='/quotation/images/delete.gif' name='op=delete:id=%s:row=%d:immediate=true:'/>", rowScroller.getId(), rowCount);
                    w.printf("</td>");
                }
                else {
                    w.printf("<td>&nbsp;</td>");
                }
            }
            
            w.printf("</tr>");
           
            rowCount++;
        }

        // if Add and Delete are enabled, put an 'Add' button to the bottom right of the scroller.
        if (rowScroller.isAddAndDeleteEnabled()) {
            w.print("<tr>");
            for(int i=0 ; i<rowScroller.getItem().size() ; i++) {
                w.print("<td>&nbsp;</td>");
            }
            
            w.printf("<td align='center'>");

            // disabled the button if we've reached 'maxRows'.
            if (rowScroller.getMaxRows()!=-1 && rowCount==rowScroller.getMaxRows()) {
                w.printf("<input type='image' src='/quotation/images/add-disabled.gif' name='op=add:id=%s:immediate=true:' disabled='true'/>", rowScroller.getId());
            }
            else {
                w.printf("<input type='image' src='/quotation/images/add.gif' name='op=add:id=%s:immediate=true:'/>", rowScroller.getId());
            }
            
            w.printf("</td></tr>");
        }
        
        w.printf("</table>");

        return model;
    }

    public Type renderSaveButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SaveButtonAction saveButtonAction, String label, boolean remoteUser) {
        if (remoteUser) {
            w.printf("<input type='submit' name='op=save' value='%s' class='portlet-form-input-field'/>", label);
        }
        else {
            w.printf("<input type='button' onClick='%s' name='op=save' value='%s' class='portlet-form-input-field'/>", LoginSection.reset, label);
        }
        
    	return model;
    }

    public List<?> renderSaveQuotations(PrintWriter w, RenderRequest request, RenderResponse response, List<?> quotationSummaries, SavedQuotations saveedQuotations) throws IllegalStateException, IOException {
        SimpleDateFormat dateFormat=new SimpleDateFormat("d MMMMM, yyyy");

        w.printf("<table width='100%%' border='0' cols='5'>");
        w.printf(  "<tr><td cols='5'>"+i18n("i18n_saved_quotations_title")+"</td></tr>", quotationSummaries.size()==1 ? "quote" : "quotes");
        w.printf(  "<tr><td height='10' cols='5'/></tr>");
        w.printf(  "<tr class='portlet-font'>");
        w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_quote_number_heading")+"</td>");
        w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_quote_date_heading")+"</td>");
        w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_expiry_date_heading")+"</td>");
        w.printf(    "<td align='center' class='portlet-section-alternate'>"+i18n("i18n_saved_quotations_premium_heading")+"</td>");
        w.printf(    "<td class='portlet-section-alternate'>&nbsp</td>");
        w.printf(  "</tr>");

        for(Object o: quotationSummaries) {
            SavedQuotationSummary savedQuote=(SavedQuotationSummary)o;
            w.printf("<tr>");
            w.printf(  "<td align='center' class='portal-form-label'>%s</td>", savedQuote.getQuotationNumber());
            w.printf(  "<td align='center' class='portal-form-label'>%s</td>", dateFormat.format(savedQuote.getQuotationDate()));
            w.printf(  "<td align='center' class='portal-form-label'>%s</td>", dateFormat.format(savedQuote.getQuotationExpiryDate()));
            w.printf(  "<td align='center' class='portal-form-label'>%s</td>", savedQuote.getPremium().toFormattedString());
            w.printf(  "<td align='left'>");
            w.printf(    "<input type='submit' name='op=confirm:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(saveedQuotations.getConfirmAndPayLabel()));
            w.printf(    "<input type='submit' name='op=requote:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(saveedQuotations.getRequoteLabel()));
            saveedQuotations.getViewQuotationButtonAction().renderResponse(request, response, savedQuote);
            w.printf(  "</td>");
            w.printf("</tr>");
        }
        
        w.printf("</table>");

        return quotationSummaries;
    }
    
    public Type renderSaveQuotationsFooter(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SavedQuotations saveedQuotations) {
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

        return model;
    }
    
    @SuppressWarnings("unchecked")
	public Type renderSectionScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SectionScroller sectionScroller) throws IllegalStateException, IOException {
        w.printf("<table width='100%%' border='0' cols='1' cellpadding='0'>");
        
        if (sectionScroller.getTitle()!=null) {
            w.printf("  <tr class='portlet-section-subheader'><td colspan='4'>");
            w.print(i18n(sectionScroller.getExpandedRepeatedTitle(model)));
            w.printf("  </td></tr>");
        }

        int rowCount=0;
        for(Iterator<Type> it=(Iterator<Type>)model.xpathIterate(sectionScroller.getBinding()) ; it.hasNext() ; rowCount++) {
            Type t=it.next();
            
            w.printf("<tr><td>");
            w.printf(" <table width='100%%' border='0' cols='4' cellpadding='4'>");
            
            // TODO sectionTitle should be removed for 2.0
            if (sectionScroller.getSectionTitle()!=null) {
                w.printf("  <tr class='portlet-section-subheader'><td colspan='4'>");
                sectionScroller.getSectionTitle().renderResponse(request, response, t);
                w.printf("  </td></tr>");
            }

            if (sectionScroller.getRepeatedTitle()!=null) {
                w.printf("  <tr class='portlet-section-subheader'><td colspan='4'>");
                w.print(i18n(sectionScroller.getExpandedRepeatedTitle(t)));
                w.printf("  </td></tr>");
            }
    
            for (Iterator<AttributeField> question=sectionScroller.getItem().iterator() ; question.hasNext() ; ) {
                w.printf("<tr>");
                question.next().renderResponse(request, response, t, sectionScroller.getBinding()+"["+rowCount+"]");
                w.printf("</tr>");
            }
            w.printf(" </table>");
            w.printf("</td></tr>");
        }
        w.printf("</table>");

        return model;
    }
    
    public Type renderViewQuotationButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ViewQuotationButtonAction viewQuotationButtonAction, String quoteNumber, String label) {
        w.printf("<input type='submit' name='op=view:id=%s' value='%s' class='portlet-form-input-field'/>", quoteNumber, label);
    	return model;
    }
    
	public Type renderClauseDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ClauseDetails clauseDetails, String title, Map<String,List<Clause>> groupedClauses) {
		if (groupedClauses.size()!=0) {
			w.printf("<table width='100%%' border='0' cols='1'>");
			for(String subTitle: groupedClauses.keySet()) {
				w.printf("<tr><td class='portlet-section-subheader'>%s</td></tr>", subTitle);
	
				for(Clause clause: groupedClauses.get(subTitle)) {
					w.printf("<tr><td>%s %s</td></tr>", clause.getReference(), clause.getTitle());
					w.printf("<tr><td>%s</td></tr>", clause.getText());
					w.printf("<tr><td height='5'>&nbsp;</td></tr>");
				}
				w.printf("<tr><td height='15'>&nbsp;</td></tr>");
			}
			w.printf("</table>");
		}

		return model;
	}

	private class RenderAssessmentSheetDetailsHelper {
	    private String cellStyle="class='portlet-font' style='border: 1px solid gray;'";

	    public void renderAssessmentSheet(PrintWriter w, String title, AssessmentSheet sheet) {
	        w.printf("<tr>");
	        w.printf(  "<td colspan='6' style='border: 1px solid gray;' class='portlet-section-selected'><b>"+i18n("i18n_assessment_sheet_section_title")+"</b></td>", title);
	        w.printf("</tr>");

	        renderNotesLines(w, sheet);

	        renderCalculationLines(w, sheet);

	        renderMarkerLines(w, sheet);
	    }

	    private void renderNotesLines(PrintWriter w, AssessmentSheet sheet) {
	        w.printf("<tr>");
	        w.printf(  "<td colspan='6' style='border: 1px solid gray;' colspan='6' height='5'></td>");
	        w.printf("</tr>");
	        w.printf("<tr>");
	        w.printf(  "<td style='border: 1px solid gray;' colspan='6' class='portlet-section-header'><b>"+i18n("i18n_assessment_sheet_notes_title")+"</b></td>");
	        w.printf("</tr>");
	        w.printf("<tr>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_id_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' width='40%%' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_description_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_source_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
	        w.printf("</tr>");

	        ArrayList<AssessmentNote> sortedLines=new ArrayList<AssessmentNote>(sheet.getLinesOfType(AssessmentNote.class).values());
	        Collections.sort(sortedLines);

	        if (sortedLines.size()!=0) {
	            for(AssessmentNote note: sortedLines) {
	                w.printf("</tr>");
	                w.printf(  "<td "+cellStyle+">%s</td>", note.getId());
	                w.printf(  "<td width='40%%' "+cellStyle+">%s</td>", note.getReason());
	                w.printf(  "<td "+cellStyle+">%s</td>", note.getOrigin());
	                w.printf(  "<td "+cellStyle+">&nbsp</td>");
	                w.printf(  "<td "+cellStyle+" align='right'>&nbsp;</td>");
	                w.printf(  "<td "+cellStyle+" align='right'>&nbsp</td>");
	                w.printf("</tr>");
	            }
	        }
	        else {
	            w.printf("</tr>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td width='40%%' "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+">&nbsp</td>");
	            w.printf(  "<td class='portlet-font' align='right' style='border: 1px solid gray;'>&nbsp;</td>");
	            w.printf(  "<td class='portlet-font' align='right' style='border: 1px solid gray;'>&nbsp;</td>");
	            w.printf("</tr>");
	        }
	    }

	    private void renderCalculationLines(PrintWriter w, AssessmentSheet sheet) {
	        w.printf("<tr>");
	        w.printf(  "<td colspan='6' style='border: 1px solid gray;' height='5'></td>");
	        w.printf("</tr>");
	        w.printf("<tr>");
	        w.printf(  "<td style='border: 1px solid gray;' colspan='6' class='portlet-section-header'><b>"+i18n("i18n_assessment_sheet_calculations_title")+"</b></td>");
	        w.printf("</tr>");
	        w.printf("<tr>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_id_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' width='40%%' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_description_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_source_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_type_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader' align='center'>"+i18n("i18n_assessment_sheet_rate_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader' align='center'>"+i18n("i18n_assessment_sheet_amount_title")+"</td>");
	        w.printf("</tr>");
	 
	        ArrayList<CalculationLine> sortedLines=new ArrayList<CalculationLine>(sheet.getLinesOfType(CalculationLine.class).values());

	        Collections.sort(sortedLines, new Comparator<AssessmentLine>() {
	            public int compare(AssessmentLine a1, AssessmentLine a2) {
	                if (!a1.getOrigin().equals(a2.getOrigin())) {
	                    if ("AssessRisk".equals(a1.getOrigin())) {
	                        return -1;
	                    }
	                    if ("CalculatePremium".equals(a2.getOrigin())) {
	                        return -1;
	                    }
	                }
	                return a1.getCalculatedOrder()-a2.getCalculatedOrder();
	            }
	        });
	        
	        if (sortedLines.size()!=0) {
	            for(CalculationLine line: sortedLines) {
	                w.printf("<tr>");
	                w.printf(  "<td class='%s' style='border: 1px solid gray;'>%s</td>", line.getId().matches("[0-9A-F]{8}") ? "portlet-font-dim" : "portlet-font", line.getId());
	                w.printf(  "<td width='40%%' "+cellStyle+">%s</td>", line.getReason());
	                w.printf(  "<td "+cellStyle+">%s</td>", line.getOrigin());
	                w.printf(  "<td "+cellStyle+">%s</td>", calculationLineType(line));
	                w.printf(  "<td "+cellStyle+" align='right'>%s</td>", calculationLineRate(line));
	                w.printf(  "<td "+cellStyle+" align='right'>%s</td>", line.getAmountAsString());
	                w.printf("</tr>");
	            }
	        }
	        else {
	            w.printf("<tr>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td width='40%%' "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+" align='right'>-</td>");
	            w.printf(  "<td "+cellStyle+" align='right'>-</td>");
	            w.printf("</tr>");
	        }
	    }

	    private void renderMarkerLines(PrintWriter w, AssessmentSheet sheet) {
	        w.printf("<tr>");
	        w.printf(  "<td colspan='6' style='border: 1px solid gray;' height='5'></td>");
	        w.printf("</tr>");
	        w.printf("<tr>");
	        w.printf(  "<td style='border: 1px solid gray;' colspan='6' class='portlet-section-header'><b>"+i18n("i18n_assessment_sheet_markers_title")+"</b></td>");
	        w.printf("</tr>");
	        w.printf("<tr>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_id_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' width='40%%' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_description_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_source_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_type_title")+"</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
	        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
	        w.printf("</tr>");
	        
	        ArrayList<Marker> sortedLines=new ArrayList<Marker>(sheet.getLinesOfType(Marker.class).values());
	        Collections.sort(sortedLines);

	        if (sortedLines.size()!=0) {
	            for(Marker marker: sortedLines) {
	                w.printf("</tr>");
	                w.printf(  "<td "+cellStyle+">%s</td>", marker.getId());
	                w.printf(  "<td width='40%%' "+cellStyle+">%s</td>", marker.getReason());
	                w.printf(  "<td "+cellStyle+">%s</td>", marker.getOrigin());
	                w.printf(  "<td "+cellStyle+">%s</td>", marker.getType());
	                w.printf(  "<td "+cellStyle+" align='right'>&nbsp;</td>");
	                w.printf(  "<td "+cellStyle+" align='right'>&nbsp</td>");
	                w.printf("</tr>");                
	            }
	        }
	        else {
	            w.printf("</tr>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td width='40%%' "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+">-</td>");
	            w.printf(  "<td "+cellStyle+" align='right'>&nbsp;</td>");
	            w.printf(  "<td "+cellStyle+" align='right'>&nbsp</td>");
	            w.printf("</tr>");
	        }
	    }

	    private String totalPremium(Quotation quote) {
	        try {
	            return quote.getTotalPremium().toString();
	        }
	        catch(Throwable e) {
	            return "-";
	        }
	    }

	    private String calculationLineRate(CalculationLine line) {
	        try {
	            return ((RateBehaviour)line).getRate().getRate();
	        }
	        catch(ClassCastException e) {
	            return "&nbsp;";
	        }
	    }
	    
	    private String calculationLineType(CalculationLine line) {
	        if (line instanceof RateBehaviour) {
	            RateBehaviour r=(RateBehaviour)line;
	            return String.format("<table width='100%%' style='border-collapse: collapse;'>"+
	                                   "<tr><td class='portlet-font' rowspan='2'>%s</td><td align='right' class='portlet-section-footer'>%s</td></tr>"+
	                                   "<tr><td align='right' class='portlet-section-footer'>%s</td></tr>"+
	                                 "</table>", 
	                                 r.getType().getLongName(), 
	                                 r.getDependsOn(), 
	                                 r.getContributesTo());
	        }
	        else if (line instanceof FixedSum) {
	            FixedSum f=(FixedSum)line;
	            return String.format("<table width='100%%' style='border-collapse: collapse;'>"+
	                                   "<tr><td class='portlet-font' rowspan='2'>"+i18n("i18n_assessment_sheet_fixedsum_title")+"</td><td align='right' class='portlet-section-footer'>&nbsp;</td></tr>"+
	                                   "<tr><td align='right' class='portlet-section-footer'>%s</td></tr>"+
	                                 "</table>", 
	                                 hideNull(f.getContributesTo()));
	        }
	        else if (line instanceof SumBehaviour) {
	            SumBehaviour s=(SumBehaviour)line;
	            return String.format("<table width='100%%' style='border-collapse: collapse;'>"+
	                                    "<tr><td class='portlet-font' rowspan='2'>%s</td><td align='right' class='portlet-section-footer'>&nbsp;</td></tr>"+
	                                    "<tr><td align='right' class='portlet-section-footer'>%s</td></tr>"+
	                                  "</table>", 
	                                  s.getType().getLongName(), 
	                                  s.getContributesTo());
	        }
	        else {
	            return "";
	        }
	    }
	}
	
	private class RenderAttributeFieldHelper {
	    /**
	     * Render an AttributeField's choice list as a set of HTML options for use in a select element.
	     * See {@link com.ail.core.Attribute} for details of the choice format.
	     * @param format Choice string
	     * @param selected The value of the option to show as selected, or null if no value is selected.
	     * @return Option line as a string.
	     */
	    private String renderEnumerationAsOptions(String format, String selected) {
	        StringBuffer ret=new StringBuffer();

	        String[] opts=format.split("[|#]");

	        for(int i=1 ; i<opts.length ; i+=2) {
	            if (opts[i].equals(selected)) {
	                ret.append("<option selected='yes'>"+opts[i]+"</option>");
	            }
	            else {
	                ret.append("<option>"+opts[i]+"</option>");
	            }
	        }
	        
	        return ret.toString();
	    }
	}

    private class RenderBrokerQuotationSummaryHelper {
        /** Define the types of lines to include in the premium detail table */
        private List<BehaviourType> PREMIUM_DETAIL_LINE_TYPES = new ArrayList<BehaviourType>();
        
        RenderBrokerQuotationSummaryHelper() {
            PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.TAX);
            PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.COMMISSION);
            PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.MANAGEMENT_CHARGE);
            PREMIUM_DETAIL_LINE_TYPES.add(BehaviourType.BROKERAGE);
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
	        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_legal_name_label")+"</td><td>%s</td></tr>", proposer.getLegalName());
	        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_address_label")+"</td><td>%s</td></tr>", proposer.getAddress().getLine1());
	        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getLine2());
	        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getTown());
	        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getCounty());
	        w.printf(  "<tr><td>&nbsp;</td><td>%s</td></tr>", proposer.getAddress().getPostcode());
	        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_phone_label")+"</td><td>%s</td></tr>", proposer.getTelephoneNumber());
	        w.printf(  "<tr><td>"+i18n("i18n_broker_quotation_summary_email_label")+"</td><td><a href='mailto:%1s'>%1s</a></td></tr>", proposer.getEmailAddress());
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
    
    private class RenderPaymentDetailsHelper {
    	private SimpleDateFormat monthFormat=new SimpleDateFormat("MM");
        private SimpleDateFormat yearFormat=new SimpleDateFormat("yy");

        private void renderSummary(PrintWriter w, Quotation quote, PaymentDetails paymentDetails) {
            SimpleDateFormat f=new SimpleDateFormat("d MMMMM, yyyy");
            Proposer proposer=(Proposer)quote.getProposer();

            w.printf("<tr class='portlet-font'>");
            w.printf("    <td class='portlet-section-alternate'>"+i18n("i18n_payment_details_title")+"</td>");
            w.printf("</tr>");
            w.printf("<tr>");
            w.printf("    <td>");
            w.printf("        <table width='100%%'>");
            w.printf("            <tr class='portlet-font'><td width='15%%'><b>"+i18n("i18n_payment_details_premium_label")+"</b></td><td>%s</td></tr>", quote.getTotalPremium());
            w.printf("            <tr class='portlet-font'><td width='15%%'><b>"+i18n("i18n_payment_details_cover_start_date_label")+"</b></td><td>%s</td></tr>", f.format(quote.getInceptionDate()));
            w.printf("            <tr class='portlet-font'><td width='15%%'><b>"+i18n("i18n_payment_details_cover_end_date_label")+"</b></td><td>%s</td></tr>", f.format(quote.getExpiryDate()));
            w.printf("        </table>");
            w.printf("  </td>");
            w.printf("</tr>");
            w.printf("<tr class='portlet-font'>");
            w.printf("    <td class='portlet-section-alternate'>"+i18n("i18n_payment_details_contact_details_title")+"</td>");
            w.printf("</tr> ");
            w.printf("<tr>");
            w.printf("    <td>");
            w.printf("        <table width='100%%'>");
            w.printf("            <tr class='portlet-font'><td width='15%%'><b>"+i18n("i18n_payment_details_name_label")+"</b></td><td>%s %s</td></tr>", proposer.getFirstName(), proposer.getSurname());
            w.printf("            <tr class='portlet-font'><td width='15%%'><b>"+i18n("i18n_payment_details_address_label")+"</b></td><td>%s</td></tr>", proposer.getAddress());
            w.printf("            <tr class='portlet-font'><td width='15%%'><b>"+i18n("i18n_payment_details_email_address_label")+"</b></td><td>%s</td></tr>", proposer.getEmailAddress());
            w.printf("        </table>");
            w.printf("    </td>");
            w.printf("</tr>");
        }

        private void renderPaymentDetails(PrintWriter w, Quotation quote, PaymentDetails paymentDetails) {
            w.printf("<tr class='portlet-font'>");
            w.printf("    <td class='portlet-section-alternate'>"+i18n("i18n_payment_details_payment_details_title")+"</td>");
            w.printf("</tr> ");
            w.printf("<tr>");
            w.printf("    <td>");
            for(MoneyProvision mp: quote.getPaymentDetails().getMoneyProvision()) {
                if (mp.getPaymentMethod() instanceof PaymentCard) {
                    renderCardDetails(w, quote, mp, paymentDetails);
                }
                else if (mp.getPaymentMethod() instanceof DirectDebit) {
                    renderBankDetails(w, quote, mp, paymentDetails);
                }
            }
            w.printf("    </td>");
            w.printf("</tr> ");
        }

        private void renderBankDetails(PrintWriter w, Quotation quote, MoneyProvision mp, PaymentDetails paymentDetails) {
            PaymentSchedule schedule=quote.getPaymentDetails();

            DirectDebit dd=(DirectDebit)mp.getPaymentMethod();
            String accountNumber=hideNull(dd.getAccountNumber());
            String sortCode=(dd.getSortCode()!=null && dd.getSortCode().length()>0)? dd.getSortCode() : "--";
            String sc1=sortCode.substring(0, sortCode.indexOf('-'));
            String sc2=sortCode.substring(sortCode.indexOf('-')+1, sortCode.lastIndexOf('-'));
            String sc3=sortCode.substring(sortCode.lastIndexOf('-')+1);
            
            w.printf("<table width='100%%' cols='2'>");
            w.printf(" <tr class='portlet-font'><td width='25%%' colspan='2'>"+i18n("i18n_payment_details_bank_account_message")+"</td></tr>");
            w.printf(" <tr class='portlet-font'><td width='25%%'><b>"+i18n("i18n_payment_details_originator_label")+"</b></td><td>%s</td></tr>", quote.getBroker().getLegalName()+", "+quote.getBroker().getAddress());
            w.printf(" <tr class='portlet-font'><td width='25%%'><b>"+i18n("i18n_payment_details_originator_id_label")+"</b></td><td>%s</td></tr>", quote.getBroker().getDirectDebitIdentificationNumber());
            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td width='25%%'><b>"+i18n("i18n_payment_details_account_number_label")+"</b></td>");
            w.printf("  <td>");
            w.printf("    <table border='0'><tr>");
            w.printf("     <td><input name='acc' size='8' type='text' maxlength='10' value='%s'/></td>", accountNumber);
            w.printf("     <td class='portlet-msg-error'>%s</td>", findError("dd.account", schedule, paymentDetails));
            w.printf("   </table>");
            w.printf("  </tr>");
            w.printf(" </tr>");
            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td width='25%%'><b>"+i18n("i18n_payment_details_sort_code_label")+"</b></td>");
            w.printf("  <td>");
            w.printf("    <table border='0'><tr>");
            w.printf("     <td>");
            w.printf(       "<input name='sc1' size='2' maxlength='2' type='text' value='%s'/>-", sc1);
            w.printf(       "<input name='sc2' size='2' maxlength='2' type='text' value='%s'/>-", sc2);
            w.printf(       "<input name='sc3' size='2' maxlength='2' type='text' value='%s'/>", sc3);
            w.printf("     </td>");
            w.printf("     <td class='portlet-msg-error'>%s</td>", findError("dd.sort", schedule, paymentDetails));
            w.printf("   </table>");
            w.printf("  </tr>");
            w.printf(" </tr>");
            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td colspan='2'>");
            w.printf(   i18n("i18n_payment_details_direct_debit_message"), quote.getBroker().getLegalName());
            w.printf("  </td>");
            w.printf(" </tr>");
            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td colspan='2'>");
            w.printf(   i18n("i18n_payment_details_guarantee_message"), quote.getBroker().getLegalName());
            w.printf("  </td>");
            w.printf(" </tr>");
            w.printf("</table>");
        }

        private void renderCardDetails(PrintWriter w, Quotation quote, MoneyProvision mp, PaymentDetails paymentDetails) {
            PaymentSchedule schedule=quote.getPaymentDetails();
            PaymentCard pc=(PaymentCard)mp.getPaymentMethod();
            
            String month=pc.getExpiryDate()!=null ? monthFormat.format(pc.getExpiryDate()) : "";
            String year=pc.getExpiryDate()!=null ? yearFormat.format(pc.getExpiryDate()) : "";
            
            if (pc.getCardHoldersName()==null) {
            	Proposer proposer=(Proposer)quote.getProposer();
                pc.setCardHoldersName(proposer.getFirstName()+" "+proposer.getSurname());
            }
            
            w.printf("<table width='100%%' cols='2'>");
            w.printf(" <tr class='portlet-font'><td width='15%%' colspan='2'>"+i18n("i18n_payment_details_direct_debit_title")+"</td></tr>");

            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td width='15%%'><b>Card number</b></td>");
            w.printf("  <td>");
            w.printf("   <table border='0'><tr>");
            w.printf("    <td><input name='cardNumber' size='20' type='text' value='%s'/></td>", hideNull(pc.getCardNumber()));
            w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.cardNumber", schedule, paymentDetails));
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
            w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.expiryDate", schedule, paymentDetails));
            w.printf("   </tr></table>");
            w.printf("  </td>");
            w.printf(" </tr>");
            
            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td width='15%%'><b>Issue number</b></td>");
            w.printf("  <td>");
            w.printf("   <table border='0'><tr>");
            w.printf("    <td><input name='issueNumber' size='2' maxlength='2' type='text' value='%s'/></td>", hideNull(pc.getIssueNumber()));
            w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.issueNumber", schedule, paymentDetails));
            w.printf("   </tr></table>");
            w.printf("  </td>");
            w.printf(" </tr>");

            w.printf(" <tr class='portlet-font'>");
            w.printf("  <td width='15%%'><b>Cardholders name</b></td>");
            w.printf("  <td>");
            w.printf("   <table border='0'><tr>");
            w.printf("    <td><input name='cardHoldersName' size='20' type='text' value='%s'/></td>", pc.getCardHoldersName());
            w.printf("    <td class='portlet-msg-error'>%s</td>", findError("pc.cardHoldersName", schedule, paymentDetails));
            w.printf("   </tr></table>");
            w.printf("  </td>");
            w.printf(" </tr>");

            w.printf("</table>");
        }

        private void renderSubmit(PrintWriter w, Quotation quote, PaymentDetails paymentDetails) {
            w.printf("<tr class='portlet-font'>");
            w.printf("    <td class='portlet-section-alternate'>Submit</td>");
            w.printf("</tr>");
            w.printf("<tr class='portlet-font'>");
            w.printf(" <td>");
            w.printf(  i18n("i18n_payment_details_confirm_message"), quote.getBroker().getPaymentTelephoneNumber());
            w.printf(" <table border='0'><tr>");
            w.printf("   <td>"+i18n("i18n_payment_details_confirm_label")+"<input name='confirm' type='checkbox'/></td>");
            w.printf("   <td class='portlet-msg-error'>%s</td>", findError("confirm", quote.getPaymentDetails(), paymentDetails));
            w.printf(" </tr></table>");
            w.printf(" </td>");
            w.printf("</tr>");
        }
    }
    
    private class RenderQuotationSummaryHelper {
    	private void renderPremiumSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
            CurrencyAmount premium=quote.getTotalPremium();
     
            w.printf("<table width='100%%'>");
            w.printf("   <tr valign='middle' class='portlet-table-subheader'><td>"+i18n("i18n_quotation_summary_quote_message")+"</td></tr>", premium.toFormattedString());
            w.printf("   <tr>");
            w.printf("       <td height='15'></td>");
            w.printf("   </tr>");
            w.printf("   <tr>");
            w.printf("       <td class='portlet-font'>");
            w.printf("           <ul>");
            w.printf("               <li>"+i18n("i18n_quotation_summary_quote_number_message")+"</li>", quote.getQuotationNumber());
            w.printf("               <li>"+i18n("i18n_quotation_summary_valid_until_message")+"</li>", longDate(quote.getQuotationExpiryDate()));
            
            renderTaxSummary(w, quote);
            
            if (quotationSummary.getWordingsUrl()!=null) {
                w.printf("               <li>"+i18n("i18n_quotation_summary_sample_wording_message")+"</li>", expandRelativeUrlToProductUrl(quotationSummary.getWordingsUrl(), request, quote.getProductTypeId()));
            }
            w.printf("           </ul>");
            w.printf("       </td>");
            w.printf("   </tr>");

            if (quotationSummary.getPremiumSummaryFooter()!=null) {
                w.printf( "<tr>");
                w.printf(  "<td class='portlet-font'>");
                quotationSummary.getPremiumSummaryFooter().renderResponse(request, response, quote);
                w.printf(  "</td>");
                w.printf( "</tr>");
            }

            w.printf( "<tr>");
            w.printf(  "<td class='portlet-font'>");

            quotationSummary.navigationSection().renderResponse(request, response, quote);

            w.printf(  "</td>");
            w.printf( "</tr>");
            w.printf( "<tr>");
            w.printf(  "<td>");

            quotationSummary.loginSection(quote).renderResponse(request, response, quote);

            w.printf(  "</td>");
            w.printf( "</tr>");
            w.printf("</table>");
    	}

    	/**
    	 * Render the tax panel. There are two formats here: If there is just one tax, we want to display something like:
    	 * <p><b>This premium is inclusive of IPT at 5%</b></p>
    	 * <p>If there is more than one tax, it is broken out into a list:</p>
    	 * <p><b>This premium is inclusive of:<ul>
    	 * <li>IPT at 5%</li>
    	 * <li>Stamp duty of £3.00</li>
    	 * </ul></b></p>
    	 * @param w
    	 * @param quote
    	 */
    	private void renderTaxSummary(PrintWriter w, Quotation quote) {
            Collection<Behaviour> taxLines=quote.getAssessmentSheet().getLinesOfBehaviourType(BehaviourType.TAX).values();

            if (taxLines.size()==1) {
                Behaviour taxLine=taxLines.iterator().next();
                w.printf("<li>"+i18n("i18n_quotation_summary_inclusive_header_message")+" ");
                if (taxLine instanceof RateBehaviour) {
                    w.printf(i18n("i18n_quotation_summary_inclusive_rate_message"), taxLine.getReason(), ((RateBehaviour)taxLine).getRate().getRate());
                }
                else if (taxLine instanceof SumBehaviour) {
                    w.printf(i18n("i18n_quotation_summary_inclusive_sum_message"), taxLine.getReason(), ((SumBehaviour)taxLine).getAmount().toFormattedString());
                }
                w.printf("</li>");
            }
            else if (taxLines.size()>1) {
                w.printf("<li>"+i18n("i18n_quotation_summary_inclusive_header_message")+":<ul>");
                for(Behaviour taxLine: taxLines) {
                    if (taxLine instanceof RateBehaviour) {
                        w.printf("<li>"+i18n("i18n_quotation_summary_inclusive_rate_message")+"</li>", taxLine.getReason(), ((RateBehaviour)taxLine).getRate().getRate());
                    }
                    else if (taxLine instanceof SumBehaviour) {
                        w.printf("<li>"+i18n("i18n_quotation_summary_inclusive_sum_message")+"</li>", taxLine.getReason(), ((SumBehaviour)taxLine).getAmount().toFormattedString());
                    }
                }
                w.printf("</ul></li>");
            }
        }

        private void renderCoverSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
            w.printf("<table class='portlet-font'>");

            // output the summary sections.
            for(PageElement e: quotationSummary.getPageElement()) {
                if (e instanceof AnswerSection) {
                    w.printf("<tr><td>");
                    e.renderResponse(request, response, quote);
                    w.printf("</td></tr>");
                }
                w.printf("<tr><td height='15' colspan='2'></td></tr>");
            }

            w.printf("</table>");
        }
        
        private void renderTermsAndConditions(PrintWriter w, RenderRequest request, Quotation quote, QuotationSummary quotationSummary) {
            w.printf("<table>");
            w.printf("<tr>");
            w.printf("<td class='portlet-font' width='50%%'>");
            
            if (quotationSummary.getTermsAndConditionsUrl()!=null) {
                String fullUrl=expandRelativeUrlToProductUrl(quotationSummary.getTermsAndConditionsUrl(), request, quote.getProductTypeId());
                
                try {
                    expand(w, new URL(fullUrl), quote);
                }
                catch(MalformedURLException e) {
                    w.printf(i18n("i18n_quotation_summary_missing_tandc_message"), quote.getBroker().getQuoteEmailAddress());
                    new CoreProxy().logError("Failed to display terms and conditions for quote: '"+quote.getQuotationNumber()+"', product: '"+quote.getProductTypeId()+"' url:'"+fullUrl+"'");
                }
            }
            
            w.printf("</td>");
            w.printf("</tr>");
            w.printf("</table>");
        }
    }

    private class RenderReferralSummaryHelper {
    	private void renderReferralNotification(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, ReferralSummary referralSummary) throws IOException {
            w.printf("<table>");
            w.printf("   <tr>");
            w.printf("       <td class='portlet-font'>");
            referralSummary.getReferralNotificationSection().renderResponse(request, response, quote);
            w.printf("       </td>");
            w.printf("   </tr>");
            w.printf("   <tr>");
            w.printf("       <td class='portlet-font'>");
            referralSummary.getNavigationSection().renderResponse(request, response, quote);
            w.printf("       </td>");
            w.printf("   </tr>");
            w.printf("</table>");
        }
        
        private void renderRequirementSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, ReferralSummary referralSummary) throws IOException {
            w.printf("<table class='portlet-font'>");

            // output the summary sections.
            for(PageElement e: referralSummary.getPageElement()) {
                if (e instanceof AnswerSection) {
                    w.printf("<tr><td>");
                    e.renderResponse(request, response, quote);
                    w.printf("</td></tr>");
                }
                w.printf("<tr><td height='15' colspan='2'></td></tr>");
            }

            w.printf("</table>");
        }
    }
}
