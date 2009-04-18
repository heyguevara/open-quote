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

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.Proposer;
import com.ail.openquote.Quotation;
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
import com.ail.openquote.ui.RowScroller;
import com.ail.openquote.ui.SaveButtonAction;
import com.ail.openquote.ui.SavedQuotations;
import com.ail.openquote.ui.SectionScroller;
import com.ail.openquote.ui.ViewQuotationButtonAction;

public class Xform extends Type implements Renderer {
	private static final long serialVersionUID = 2918957259222383330L;

	public Type renderAnswer(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Answer answer, String label, String answerText) {
		// TODO Auto-generated method stub
		return model;
	}

	public Type renderAnswerScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerScroller answerScroller)	throws IOException {
		// TODO Auto-generated method stub
		return model;
	}

	public Type renderAnswerSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerSection answerSection,	String title) throws IOException {
		// TODO Auto-generated method stub
		return model;
	}

	public Type renderAssessmentSheetDetails(PrintWriter w,	RenderRequest request, RenderResponse response, Type model,	AssessmentSheetDetails assessmentSheetDetails) throws IOException {
		// TODO Auto-generated method stub
		return model;
	}

	public Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField,	String boundTo, String id, String onChange, String onLoad) throws IOException {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderInformationPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, InformationPage informationPage, String title, List<PageElement> pageElements) throws IOException {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderLabel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Label label, String format, Object[] params) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderLoginSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, LoginSection loginSection, String usernameGuess, String nameOfForwardToPortal) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderNavigationSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, NavigationSection navigtionSection) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }    

    public Type renderPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Page page) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderPageScriptHeader(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageScript pageScript) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderParsedUrlContent(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ParsedUrlContent parsedUrlContent, String content) {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderQuestionPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionPage questionPage, String title) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }
    
    public Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderQuestionSeparator(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSeparator questionSeparator, String title) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailsd) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderQuitButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Quotation renderQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, QuotationSummary quotationSummary) throws IOException {
		// TODO Auto-generated method stub
    	return quote;
    }

    public Quotation renderReferralSummary(PrintWriter w, RenderRequest request, RenderResponse response, Quotation quote, ReferralSummary referralSummary) throws IOException {
		// TODO Auto-generated method stub
    	return quote;
    }

    public Type renderRequoteButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderRowScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, RowScroller rowScroller) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }
    
    public Type renderSaveButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SaveButtonAction saveButtonAction, String label, boolean remoteUser) { 
		// TODO Auto-generated method stub
    	return model;
    }

   	public List<?> renderSaveQuotations(PrintWriter w, RenderRequest request, RenderResponse response, List<?> quotationSummaries, SavedQuotations saveedQuotations) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return quotationSummaries;
    }

    public Type renderSaveQuotationsFooter(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SavedQuotations saveedQuotations) {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderSectionScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SectionScroller sectionScroller) throws IllegalStateException, IOException {
		// TODO Auto-generated method stub
    	return model;
    }

    public Type renderViewQuotationButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ViewQuotationButtonAction viewQuotationButtonAction, String quoteNumber, String label) {
		// TODO Auto-generated method stub
    	return model;
    }
}
