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
import static com.ail.openquote.ui.util.Functions.expandRelativeUrl;
import static com.ail.openquote.ui.util.Functions.findError;
import static com.ail.openquote.ui.util.Functions.findErrors;
import static com.ail.openquote.ui.util.Functions.hasErrorMarker;
import static com.ail.openquote.ui.util.Functions.hasErrorMarkers;
import static com.ail.openquote.ui.util.Functions.longDate;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Attribute;
import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.core.TypeEnum;
import com.ail.financial.CurrencyAmount;
import com.ail.insurance.policy.Behaviour;
import com.ail.insurance.policy.BehaviourType;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.insurance.policy.SumBehaviour;
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
import com.ail.openquote.ui.CommandButtonAction;
import com.ail.openquote.ui.InformationPage;
import com.ail.openquote.ui.Label;
import com.ail.openquote.ui.LoginSection;
import com.ail.openquote.ui.NavigationSection;
import com.ail.openquote.ui.Page;
import com.ail.openquote.ui.PageElement;
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

public class Xform extends Type implements Renderer {
	private static final long serialVersionUID = 2918957259222383330L;
	private RenderQuotationSummaryHelper renderQuotationSummaryHelper=new RenderQuotationSummaryHelper();
	private RenderAttributeFieldHelper renderAttributeFieldHelper=new RenderAttributeFieldHelper();

	/**
     * Render a Java Enumeration as an HTML option list to be used in a select. Note that the
     * enumeration must be based on {@link TypeEnum}.
     * @param Enumeration type
     * @return Option list as a string.
     */
    private static String renderEnumerationAsOptions(Class<? extends TypeEnum> clazz) {
        return renderEnumerationAsOptions(clazz, null);
    }
    
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

    public Type renderAnswer(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Answer answer, String title, String answerText) {
		w.printf("<label>%s</label><label>%s</label>", title, answerText);
		return model;
	}

	@SuppressWarnings("unchecked")
	public Type renderAnswerScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerScroller answerScroller)	throws IOException {
		int rowCount=0;
		
        for(Iterator it=model.xpathIterate(answerScroller.getBinding()) ; it.hasNext() ; rowCount++) {
            Type t=(Type)it.next();
               
            for (Answer a: answerScroller.getAnswer()) {
                a.renderResponse(request, response, t);
            }
        }
        
        return model;
	}

	public Type renderAnswerSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerSection answerSection,	String title) throws IOException {
		// output the title row if a title was defined
		if (title!=null) {
		    w.printf("<label>%s</label>", title);
		}
		
		for(Answer a: answerSection.getAnswer()) { 
		    model=a.renderResponse(request, response, model);
		}
		
		return model;
	}

	public Type renderAssessmentSheetDetails(PrintWriter w,	RenderRequest request, RenderResponse response, Type model,	AssessmentSheetDetails assessmentSheetDetails) throws IOException {
		// TODO Auto-generated method stub
		return model;
	}

	public Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField,	String boundTo, String id, String onChange, String onLoad) throws IOException {
	    Pattern formattedCurrencyPattern=Pattern.compile("([^0-9,.']*)([0-9,.']*)([^0-9,.']*)");

	    String onChangeEvent=(onChange!=null) ? "onchange='"+onChange+"'" : "";
	    
	    Attribute attr=(Attribute)model;
	    
	    try {
	        if (model==null) {
	             w.printf("<hint>undefined: %s</hint>", boundTo);              
	        }
	        else {
	            if (attr.isStringType()) {
					String size=attr.getFormatOption("size");
					size=(size!=null) ? "size='"+size+"'" : "";                
					w.printf("<input ref=\"%s\" %s %s class='text'>", id, size, onChangeEvent);
					w.printf("<label>%s</label>",attributeField.getTitle());
					w.printf("<value>%s</value>",attr.getValue());
					w.printf("<hint>%s</hint></input>", Functions.findErrors(attr));
	            }
	            else if (attr.isNumberType()) {
	                String pattern=attr.getFormatOption("pattern");
	                String trailer=(attr.getFormat().endsWith("percent")) ? "%" : ""; 
	                int size=(pattern==null) ? 7 : pattern.length();
					w.printf("<input ref=\"%s\" %s %s class='number' trailer='%s'>", id, size, onChangeEvent, trailer);
					w.printf("<label>%s</label>",attributeField.getTitle());
					w.printf("<value>%s</value>",attr.getValue());
					w.printf("<hint>%s</hint></input>", Functions.findErrors(attr));
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
	                
	                w.printf("<input ref=\"%s\" %s size='7' class='number' unit='%s'>", id, onChangeEvent, attr.getUnit());
	                w.printf("<label>%s</label>",attributeField.getTitle());
	                w.printf("<value>%s%s%s</value>",pre, value, post);
	                w.printf("<hint>%s</hint></input>", Functions.findErrors(attr));
	            }
	            else if (attr.isChoiceMasterType()) {
	            	onLoad="loadChoiceOptions($this,$value,"+attr.getChoiceTypeName()+")";
	            	onChange="updateSlaveChoiceOptions(findElementsByName('"+id+"')[0], "+attr.getChoiceTypeName()+", '"+attr.getId()+"', '"+attr.getChoiceSlave()+"');";
	                 w.printf("<select1 ref=\"%s\" onchange=\"%s\" appearance=\"%s\" />", id, onChange, attributeField.getRenderHint());
	            }
	            else if (attr.isChoiceSlaveType()) {
	            	onLoad="loadSlaveChoiceOptions($this,$value,"+attr.getChoiceTypeName()+",'"+attr.getChoiceMaster()+"','"+attr.getId()+"')";
	                 w.printf("<select1 ref=\"%s\" appearance=\"%s\" />", id, attributeField.getRenderHint());
	            }
	            else if (attr.isChoiceType()) {
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
	                        w.printf("<select1 ref=\"%s\" class='keep_empty' appearance=\"%s\"><value>%s</value><label>%s</label>%s</select1>", id, attributeField.getRenderHint(), attr.getValue(), attributeField.getTitle(), renderAttributeFieldHelper.renderEnumerationAsOptions(attr.getFormatOption("options"), attr.getValue()));
	                	}
	                }
	                else {
	                    String optionTypeName=attr.getFormatOption("type");

	                    Choice choice=(Choice)(new CoreProxy().newProductType(QuotationContext.getQuotation().getProductTypeId(), optionTypeName));

	                    w.printf("<select1 ref=\"%s\" appearance=\"%s\"><label>%s</label>", id, attributeField.getRenderHint(), attributeField.getTitle());

	                    for(Choice c: choice.getChoice()) {
	                    	w.printf("<item><label>%1s</label><value>%1s</value></item>", c.getName());
	                    }

	                    w.printf("</select1>");
	                }
	            }
	            else if (attr.isDateType()) {
	                String dateFormat=attr.getFormatOption("pattern");
	                int size=(dateFormat==null) ? 10 : dateFormat.length();
	                 w.printf("<input ref=\"%s\" size='%s' %s class='date'><label>s%</label><value>%s</value></input>", id, size, onChangeEvent, attributeField.getTitle(), attr.getValue());
	            }
	            else if (attr.isYesornoType()) {
	                 w.printf("<input ref=\"%s\" %s class='text'><label>s%</label><value>%s</value></input>", id, onChangeEvent, attributeField.getTitle(), renderAttributeFieldHelper.renderEnumerationAsOptions(YES_OR_NO_FORMAT, attr.getValue()));
	            }
	            else if (attr.isNoteType()) {
	                 w.printf("<textarea name=\"%s\" class='portlet-form-input-field' %s rows='3' style='width:100%%'>%s</textarea>", id, onChangeEvent, attr.getValue());
	            }
	            
	             String error = Functions.findErrors(attr);
	             if (error.length() > 6){
	               //For some reason the empty ones are length 6 ?!
	               w.printf("<hint>%s</hint>", error);
	             }
	    
	            if (onLoad!=null) {
	                String s=onLoad;
	    
	                if (s.contains("$this")) {
	                    s=s.replace("$this", "findElementsByName(\""+id+"\")[0]");
	                }
	    
	                if (s.contains("$value")) {
	                    s=s.replace("$value", "'"+attr.getValue()+"'");
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

	public Type renderAttributeFieldPageLevel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id) throws IOException {
		// TODO Auto-generated method stub
		return model;
	}

	public Type renderBlank(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Blank blank) throws IOException {
		// TODO Auto-generated method stub
		return model;
	}

    public Type renderBrokerQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Type model, BrokerQuotationSummary brokerQuotationSummary) {
		// TODO Auto-generated method stub
		return model;
	}

    public Type renderCommandButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label, boolean immediate) {
        w.printf("<submit submission='' ref='op=%1$s:immediate=%2$b' class='principle'><label>%1$s</label></submit>", label, immediate);
    	return model;
    }

    public Type renderInformationPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, InformationPage informationPage, String title, List<PageElement> pageElements) throws IOException {
        if (title!=null) {
            w.printf("<label>%s</label>", title);
        }

        for (PageElement e : pageElements) {
            model=e.renderResponse(request, response, model);
        }
        
        return model;
    }

    public Type renderLabel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Label label, String format, Object[] params) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderLoginSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, LoginSection loginSection, String usernameGuess, String nameOfForwardToPortal) {
        String lnk="<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Create Login\");'>"+loginSection.getInvitationLinkText()+"</a>";
        
        // Group #1: login and save the quote. Note: if the usernameGuess is a known user we'll pre-populate the form with 
        // the guess to save the user some typing; otherwise we'll leave the username field blank.
        w.printf("<group id='Proposer Login'>");
        w.printf( loginSection.getInvitationMessageText(), lnk);
        w.printf(    "<form method='post' action='%s' name='loginform' id='loginForm'>", response.createActionURL());
        w.printf(        "<label>Email address:</label>");
        w.printf(        "<input class='text' type='text' ref='username' id='username' value='%s'/>", usernameGuess);
        w.printf(        "<hint>%s</hint>", findError("username", model));
        w.printf(        "<label>Password:</label>");
        w.printf(        "<input class='text' type='password' ref='password' id='password' value=''/>");
        w.printf(        "<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Forgotten Password\");'>Forgotten password?</a>");
        w.printf(        "<submit submission='' id='loginButton' class='principle' ref='op=%1$s:page=%2$s:portal=%3$s'><label>%1$s</label></submit>", loginSection.getLoginButtonLabel(), loginSection.getForwardToPageName(), nameOfForwardToPortal);
        w.printf(    "</form>");
        w.printf("</group>");

        // Group #2: create a new user and save quote
        w.printf("<group id='Create Login'>");
        w.printf( "<label>Create a new account here. If you have an existing account, please <a onClick='showDivDisplay(\"Proposer Login\");hideDivDisplay(\"Create Login\");'>login here</a>.</label>");
        w.printf(    "<label>Email address:</label>");
        w.printf(    "<input class='text' ref='username' value=''/>");
        w.printf(    "<hint>%s</hint>", findError("username", model));
        w.printf(    "<label>Confirm email address:</label>");
        w.printf(    "<input class='text' ref='cusername' value=''/>");
        w.printf(    "<hint>%s</hint>", findError("cusername", model));
        w.printf(    "<label>Password:</label>");
        w.printf(    "<input class='password' ref='password' value=''/>");
        w.printf(    "<hint>%s</hint>", findError("password", model));
        w.printf(    "<label>Confirm password:</label>");
        w.printf(    "<input class='password' ref='cpassword' value=''/>");
        w.printf(    "<hint>%s</hint>", findError("cpassword", model));
        w.printf(    "<submit ref='op=Create:page=%s:portal=%s'><label>Create & Save</label></submit>", loginSection.getForwardToPageName(), nameOfForwardToPortal);
        w.printf("</group>");

        // Group #3: Send a password reminder
        w.printf("<group id='Forgotten Password'>");
        w.printf(    "<label>Enter your email address below and your password will be emailed to you.</label>");
        w.printf(    "<label>Email address:</label>");
        w.printf(    "<input class='text' ref='username' value='%s'/>",  usernameGuess);
        w.printf(    "<submit ref='op=Reminder'><label>Send Reminder</label></submit>");
        w.printf("</group>");

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
        for(PageElement element: navigationSection.getPageElement()) {
			model=element.renderResponse(request, response, model);
		}
        if (navigationSection.isQuitDisabled()) {
            w.print("&nbsp;");
        }
        else {
            model=navigationSection.getQuitButton().renderResponse(request, response, model);
        }

    	return model;
    }    

    public Type renderPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Page page) {
		// Nothing to do here for xforms
    	return model;
    }

    public Type renderPageScriptHeader(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageScript pageScript) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title) throws IllegalStateException, IOException {
        // Output the section title if there is one.
        if (!Functions.isEmpty(title)) {
            w.printf("<label>%s</label>", pageSection.getColumns(), title);
        }

        Iterator<? extends PageElement> it=pageSection.getPageElement().iterator();
        
        while(it.hasNext()) {
            for(int col=0 ; col<pageSection.getColumns() ; col++) {
                if (it.hasNext()) {
                    model=it.next().renderResponse(request, response, model);
                }
                else {
                    w.printf("&nbsp;");
                }
            }
        }
        
    	return model;
    }

    public Type renderParsedUrlContent(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ParsedUrlContent parsedUrlContent, String content) {
        w.print(content);
        return model;
    }

    public Type renderPaymentDetails(PrintWriter w, RenderRequest request, RenderResponse response, Quotation model, PaymentDetails paymentDetails) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Quotation renderPaymentOptionSelector(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quotation, PaymentOptionSelector paymentOptionSelector) {
		// TODO Auto-generated method stub
    	return quotation;
    }

    public Proposer renderProposerDetails(PrintWriter w, RenderRequest request, RenderResponse response, Proposer proposer, ProposerDetails proposerDetails) {
		// TODO Auto-generated method stub
    	return proposer;
    }

    public Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext) throws IllegalStateException, IOException {
        w.printf("<label>%s</label>", title);
        w.printf(question.renderAttribute(request, response, model, question.getBinding(), rowContext, question.getOnChange(), question.getOnLoad()));
    	return model;
    }

    public Type renderQuestionPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionPage questionPage, String title) throws IllegalStateException, IOException {
        w.printf("<div class='xform'>");
        w.printf(    "<model>");
        w.printf(        "<instance>%s</instance>", new CoreProxy().toXML(model));        
        w.printf(        "<submission id='%s' action='%s' method='post' event='return form_check(this)'/>", questionPage.getId(), response.createActionURL());
        w.printf(    "</model>");
        w.printf(    "<group ref='%s'>", questionPage.getId());
        
        if (title!=null) {
            w.printf("  <label>%s</label>", title);
        }

        for (PageElement e : questionPage.getPageElement()) {
            model=e.renderResponse(request, response, model);
        }

        w.printf(    "</group>");        
        w.printf("</div>");

    	return model;
    }
    
    public Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title) throws IllegalStateException, IOException {
        w.printf("<group>");

        // output the title row if a title was defined
        if (title!=null) {
            w.printf("<label>%s</label>", title);
        }

        Iterator<? extends Question> it=questionSection.getQuestion().iterator();
        
        while(it.hasNext()) {
            it.next().renderResponse(request, response, model);
        }
        w.printf("</group>");

        return model;
    }

    public Type renderQuestionSeparator(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSeparator questionSeparator, String title) {
        if (title==null) {
            if (questionSeparator.getBinding()!=null && hasErrorMarkers(model.xpathGet(questionSeparator.getBinding(), Type.class))) {
        		w.printf("<td class='portlet-section-subheader' colspan='4'>%s</td>", findErrors(model.xpathGet(questionSeparator.getBinding(), Type.class)));
        	}
        	else {
        		w.printf("<td class='portlet-section-subheader' colspan='4'>&nbsp;</td>");
        	}
        }
        else {
            w.printf("<td colspan='4'><table width='100%%''>"); 
            w.printf("<tr><td class='portlet-section-subheader' colspan='4'>%s</td></tr>", Functions.hideNull(title)); 
            if (questionSeparator.getBinding()!=null && hasErrorMarkers(model.xpathGet(questionSeparator.getBinding(), Type.class))) {
            	w.printf("<tr><td class='portlet-msg-error' colspan='4'>%s</td>", findErrors(model.xpathGet(questionSeparator.getBinding(), Type.class)));
            }
            w.printf("</table></td>");
        }

        return model;
    }

    public Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId) throws IllegalStateException, IOException {
    	String onChange = null;

    	if ("radio".equals(questionWithDetails.getRenderHint())) {
        	onChange="enableTargetIf(this.checked && this.value==\"Yes\", \""+detailId+"\")";
        }
        else if ("checkbox".equals(questionWithDetails.getRenderHint())) {
        	onChange="enableTargetIf(this.checked, \""+detailId+"\")";
        }
        else {
        	onChange="enableTargetIf(this.options[this.selectedIndex].text==\"Yes\", \""+detailId+"\")";
        }
        
        w.printf("<label>%s</label>", title);
        w.printf("<label>%s</label>", questionWithDetails.renderAttribute(request, response, model, questionWithDetails.getBinding(), rowContext, onChange, questionWithDetails.getOnLoad()));
        w.printf("<label>%s</label>", detailTitle);
        w.printf("<label>%s</label>", questionWithDetails.renderAttribute(request, response, model, questionWithDetails.getDetailsBinding(), rowContext, questionWithDetails.getOnChange(), questionWithDetails.getOnLoad()));
        
        // Disable the 'detail' textarea unless the question's answer is 'Yes'
        if ("radio".equals(questionWithDetails.getRenderHint())) {
            w.printf("<script type='text/javascript'>radio=findElementsByName(\"%s\")[1];enableTargetIf(radio.checked, \"%s\")</script>",
                    questionId, detailId);
        }
        else if ("checkbox".equals(questionWithDetails.getRenderHint())) {
            w.printf("<script type='text/javascript'>cbox=findElementsByName(\"%s\")[0];enableTargetIf(cbox.checked, \"%s\")</script>",
                    questionId, detailId);
        }
        else {
            w.printf("<script type='text/javascript'>elem=findElementsByName(\"%s\")[0];enableTargetIf(elem.options[elem.selectedIndex].text==\"Yes\", \"%s\")</script>",
                    questionId, detailId);
        }

        return model;
    }

    public Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId) throws IllegalStateException, IOException {
    	String onChange=null;

    	if ("radio".equals(questionWithSubSection.getRenderHint())) {
        	onChange="showHideDivDisplay(this.checked && this.value==\"Yes\", this.checked && this.value==\"No\", \""+questionWithSubSection.getId()+"\")";
        }
        else if ("checkbox".equals(questionWithSubSection.getRenderHint())) {
        	onChange="showHideDivDisplay(this.checked, !this.checked, \""+questionWithSubSection.getId()+"\")";
        }
        else {
        	onChange="showHideDivDisplay(this.options[this.selectedIndex].text==\"Yes\", this.value!=\"Yes\", \""+questionWithSubSection.getId()+"\")";
        }
        
        w.printf("<label>%s</label>", title);
        w.printf("<label>%s</label>", questionWithSubSection.renderAttribute(request, response, model, questionWithSubSection.getBinding(), rowContext, onChange, questionWithSubSection.getOnLoad()));
        w.printf("<div id='%s' style='visibility:hidden;display:none'>", questionWithSubSection.getId());
        model=questionWithSubSection.getSubSection().renderResponse(request, response, model);
        w.printf("</div>");

        // Disable the 'detail' area unless the question's answer is 'Yes'
        if ("radio".equals(questionWithSubSection.getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
                    "radio=findElementsByName(\"%1$s\")[1];" +
                    "showHideDivDisplay(radio.checked, !radio.checked, \"%2$s\");"+
                    "</script>", questionId, questionWithSubSection.getId());
        }
        else if ("checkbox".equals(questionWithSubSection.getRenderHint())) {
            w.printf("<script type='text/javascript'>"+
                    "cbox=findElementsByName(\"%1$s\")[0];" +
                    "showHideDivDisplay(" +
                      "cbox.checked,"+
                      "!cbox.checked, "+
                      "\"%2$s\")</script>", questionId, questionWithSubSection.getId());
        }
        else {
            w.printf("<script type='text/javascript'>"+
                    "opt=findElementsByName(\"%1$s\")[0];" +
                    "showHideDivDisplay(" +
                      "opt.options[opt.selectedIndex].text==\"Yes\", "+
                      "opt.options[opt.selectedIndex].text==\"No\", "+
                      "\"%2$s\")</script>", questionId, questionWithSubSection.getId());
        }

        return model;
    }

    public Type renderQuitButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
        w.printf("<submit submission='' ref='op=%1$s:immediate=true'><label>%1$s</label></submit>", label);
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
        w.printf("<label>");
        referralSummary.getReferralNotificationSection().renderResponse(request, response, quote);
        w.printf("<label>");

        referralSummary.getNavigationSection().renderResponse(request, response, quote);

        // output the summary sections.
        for(PageElement e: referralSummary.getPageElement()) {
            if (e instanceof AnswerSection) {
                e.renderResponse(request, response, quote);
            }
        }

        return quote;
    }

    public Type renderRequoteButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
        w.printf("<submit submission='' ref='op=%1$s'><label>%1$s</label></submit>", label);
    	return model;
    }

    @SuppressWarnings("unchecked")
	public Type renderRowScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, RowScroller rowScroller) throws IllegalStateException, IOException {

        if (rowScroller.getTitle()!=null) {
            w.print(rowScroller.getExpandedRepeatedTitle(model));
        }
        
        // Output the column headers
        for(AttributeField a: rowScroller.getItem()) {
            if (a.getExpandedSubTitle(model)!=null) {
                w.printf("<label>%s</label><label>%s</label>", a.getExpandedTitle(model), a.getExpandedSubTitle(model));
            }
            else {
                w.printf("<label>%s</label>", a.getExpandedTitle(model));                
            }
        }

        // Add a column for the trash can, if it's enabled
        if (rowScroller.isAddAndDeleteEnabled()) {
            w.printf("&nbsp;");
        }
        
        // Now output the rows of data
        int rowCount=0;
        for(Iterator it=model.xpathIterate(rowScroller.getBinding()) ; it.hasNext() ; ) {
            Type t=(Type)it.next();
            
            for(AttributeField a: rowScroller.getItem()) {
                a.renderResponse(request, response, t, rowScroller.getBinding()+"["+rowCount+"]");
            }

            // Add the trash can for this row if enabled.
            if (rowScroller.isAddAndDeleteEnabled()) {
                if (rowCount>=rowScroller.getMinRows()) {
                    w.printf("<input type='image' src='/quotation/images/delete.gif' name='op=delete:id=%s:row=%d:immediate=true:'/>", rowScroller.getId(), rowCount);
                }
                else {
                    w.printf("&nbsp;");
                }
            }
            
            rowCount++;
        }

        // if Add and Delete are enabled, put an 'Add' button to the bottom right of the scroller.
        if (rowScroller.isAddAndDeleteEnabled()) {
            for(int i=0 ; i<rowScroller.getItem().size() ; i++) {
                w.print("&nbsp;");
            }
            
            // disabled the button if we've reached 'maxRows'.
            if (rowScroller.getMaxRows()!=-1 && rowCount==rowScroller.getMaxRows()) {
                w.printf("<input type='image' src='/quotation/images/add-disabled.gif' name='op=add:id=%s:immediate=true:' disabled='true'/>", rowScroller.getId());
            }
            else {
                w.printf("<input type='image' src='/quotation/images/add.gif' name='op=add:id=%s:immediate=true:'/>", rowScroller.getId());
            }
            
        }

        return model;
    }
    
    public Type renderSaveButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SaveButtonAction saveButtonAction, String label, boolean remoteUser) { 
        if (remoteUser) {
            w.printf("<submit submission='' ref='op=%1$s'><label>%1$s</label></submit>", label);
        }
        else {
            w.printf("<submit submission='' onClick='%s' ref='op=%2$s'><label>%2$s</label></submit>", LoginSection.reset, label);
        }

        return model;
    }

   	public List<?> renderSaveQuotations(PrintWriter w, RenderRequest request, RenderResponse response, List<?> quotationSummaries, SavedQuotations savedQuotations) throws IllegalStateException, IOException {
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
            w.printf(    "<input type='submit' name='op=confirm:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(savedQuotations.getConfirmAndPayLabel()));
            w.printf(    "<input type='submit' name='op=requote:id=%s' class='portlet-form-input-field' value='%s'/>", savedQuote.getQuotationNumber(), i18n(savedQuotations.getRequoteLabel()));
            savedQuotations.getViewQuotationButtonAction().renderResponse(request, response, savedQuote);
            w.printf(  "</td>");
            w.printf("</tr>");
        }
        
        w.printf("</table>");

    	return quotationSummaries;
    }

    public Type renderSaveQuotationsFooter(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SavedQuotations savedQuotations) {
        String pageName=Functions.getPortalPageName(response);
        String portalName=Functions.getPortalName(response);
        
        w.printf(  "<label>"+i18n("i18n_saved_quotations_login_message")+"</label>");
        w.printf(  "<group id='Proposer Login'>");
        w.printf(      "<label>"+i18n("i18n_saved_quotations_username_label")+"</label>");
        w.printf(      "<input class='text' ref='username' value=''/>");
        w.printf(      "<label>&nbsp;</label>");
        w.printf(      "<label>"+i18n("i18n_saved_quotations_password_label")+"</label>");
        w.printf(      "<input class='password' ref='password' value=''/>");
        w.printf(      "<a onClick='hideDivDisplay(\"Proposer Login\");showDivDisplay(\"Forgotten Password\");'>"+i18n("i18n_saved_quotations_forgotten_password_message")+"</a>");
        w.printf(      "<submit ref='op=login:portal=%s:page=%s'><label>Login</label></submit>", portalName, pageName);
        w.printf(  "</group>");
        // ?? Doubtful that this group will have button functionality atm
        w.printf("<script type='text/javascript'>hideDivDisplay('Proposer Login')</script>");

        return model;
    }

    @SuppressWarnings("unchecked")
	public Type renderSectionScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SectionScroller sectionScroller) throws IllegalStateException, IOException {
        if (sectionScroller.getTitle()!=null) {
            w.print(i18n(sectionScroller.getExpandedRepeatedTitle(model)));
        }

        int rowCount=0;
        for(Iterator<Type> it=(Iterator<Type>)model.xpathIterate(sectionScroller.getBinding()) ; it.hasNext() ; rowCount++) {
            Type t=it.next();
            
            // TODO sectionTitle should be removed for 2.0
            if (sectionScroller.getSectionTitle()!=null) {
                sectionScroller.getSectionTitle().renderResponse(request, response, t);
            }

            if (sectionScroller.getRepeatedTitle()!=null) {
                w.print(i18n(sectionScroller.getExpandedRepeatedTitle(t)));
            }
    
            for (Iterator<AttributeField> question=sectionScroller.getItem().iterator() ; question.hasNext() ; ) {
                question.next().renderResponse(request, response, t, sectionScroller.getBinding()+"["+rowCount+"]");
            }
        }

        return model;
    }

    public Type renderViewQuotationButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ViewQuotationButtonAction viewQuotationButtonAction, String quoteNumber, String label) {
        w.printf("<submit submission='' ref='op=%1$s:id=%2$s'><label>%1$s</label></submit>", label, quoteNumber);
    	return model;
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
                w.printf("               <li>"+i18n("i18n_quotation_summary_sample_wording_message")+"</li>", expandRelativeUrl(quotationSummary.getWordingsUrl(), request, quote.getProductTypeId()));
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
                w.printf("<label>"+i18n("i18n_quotation_summary_inclusive_header_message")+" ");
                if (taxLine instanceof RateBehaviour) {
                    w.printf(i18n("i18n_quotation_summary_inclusive_rate_message"), taxLine.getReason(), ((RateBehaviour)taxLine).getRate().getRate());
                }
                else if (taxLine instanceof SumBehaviour) {
                    w.printf(i18n("i18n_quotation_summary_inclusive_sum_message"), taxLine.getReason(), ((SumBehaviour)taxLine).getAmount().toFormattedString());
                }
                w.printf("</label>");
            }
            else if (taxLines.size()>1) {
                w.printf("<label>"+i18n("i18n_quotation_summary_inclusive_header_message")+":");
                for(Behaviour taxLine: taxLines) {
                    if (taxLine instanceof RateBehaviour) {
                        w.printf("<label>"+i18n("i18n_quotation_summary_inclusive_rate_message")+"</label>", taxLine.getReason(), ((RateBehaviour)taxLine).getRate().getRate());
                    }
                    else if (taxLine instanceof SumBehaviour) {
                        w.printf("<label>"+i18n("i18n_quotation_summary_inclusive_sum_message")+"</label>", taxLine.getReason(), ((SumBehaviour)taxLine).getAmount().toFormattedString());
                    }
                }
                w.printf("</label>");
            }
        }

        private void renderCoverSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {

            // output the summary sections.
            for(PageElement e: quotationSummary.getPageElement()) {
                if (e instanceof AnswerSection) {
                    w.printf("<label>");
                    e.renderResponse(request, response, quote);
                    w.printf("<label>");
                }
            }
        }
        
        private void renderTermsAndConditions(PrintWriter w, RenderRequest request, Quotation quote, QuotationSummary quotationSummary) {
            if (quotationSummary.getTermsAndConditionsUrl()!=null) {
                String fullUrl=expandRelativeUrl(quotationSummary.getTermsAndConditionsUrl(), request, quote.getProductTypeId());
                
                w.printf("<label>");
                try {
                    expand(w, new URL(fullUrl), quote);
                }
                catch(MalformedURLException e) {
                    w.printf(i18n("i18n_quotation_summary_missing_tandc_message"), quote.getBroker().getQuoteEmailAddress());
                    new CoreProxy().logError("Failed to display terms and conditions for quote: '"+quote.getQuotationNumber()+"', product: '"+quote.getProductTypeId()+"' url:'"+fullUrl+"'");
                }
                w.printf("</label>");
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
}
