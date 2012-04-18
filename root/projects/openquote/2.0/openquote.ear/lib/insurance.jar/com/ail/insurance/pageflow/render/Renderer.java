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
package com.ail.insurance.pageflow.render;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.pageflow.Answer;
import com.ail.insurance.pageflow.AnswerScroller;
import com.ail.insurance.pageflow.AnswerSection;
import com.ail.insurance.pageflow.AssessmentSheetDetails;
import com.ail.insurance.pageflow.AttributeField;
import com.ail.insurance.pageflow.Blank;
import com.ail.insurance.pageflow.ClauseDetails;
import com.ail.insurance.pageflow.CommandButtonAction;
import com.ail.insurance.pageflow.InformationPage;
import com.ail.insurance.pageflow.Label;
import com.ail.insurance.pageflow.LoginSection;
import com.ail.insurance.pageflow.NavigationSection;
import com.ail.insurance.pageflow.Page;
import com.ail.insurance.pageflow.PageElement;
import com.ail.insurance.pageflow.PageScript;
import com.ail.insurance.pageflow.PageSection;
import com.ail.insurance.pageflow.ParsedUrlContent;
import com.ail.insurance.pageflow.PaymentDetails;
import com.ail.insurance.pageflow.PaymentOptionSelector;
import com.ail.insurance.pageflow.ProposerDetails;
import com.ail.insurance.pageflow.Question;
import com.ail.insurance.pageflow.QuestionPage;
import com.ail.insurance.pageflow.QuestionSection;
import com.ail.insurance.pageflow.QuestionSeparator;
import com.ail.insurance.pageflow.QuestionWithDetails;
import com.ail.insurance.pageflow.QuestionWithSubSection;
import com.ail.insurance.pageflow.QuotationSummary;
import com.ail.insurance.pageflow.ReferralSummary;
import com.ail.insurance.pageflow.RowScroller;
import com.ail.insurance.pageflow.SaveButtonAction;
import com.ail.insurance.pageflow.SavedQuotations;
import com.ail.insurance.pageflow.SectionScroller;
import com.ail.insurance.pageflow.ViewQuotationButtonAction;
import com.ail.insurance.policy.Clause;
import com.ail.insurance.policy.Proposer;
import com.ail.insurance.policy.Policy;

@SuppressWarnings("deprecation")
public interface Renderer {
	String getMimeType();
	
	Type renderAnswer(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Answer answer, String title, String answerText) throws IOException;
	
	Type renderAnswerScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerScroller answerScroller) throws IOException;
	
	Type renderAnswerSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerSection answerSection, String title)	throws IOException;

	Type renderAssessmentSheetDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AssessmentSheetDetails assessmentSheetDetails)	throws IOException;

	Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad)	throws IOException;
	
	Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad, String title, String styleClass, String ref)	throws IOException;

	Type renderAttributeFieldPageLevel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id)	throws IOException;

	Type renderBlank(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Blank blank) throws IOException;
	
    Type renderCommandButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label, boolean immediate); 
    
    Type renderInformationPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, InformationPage informationPage, String title, List<PageElement> pageElements) throws IOException;

	Type renderLabel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Label label, String format, Object[] params);

    Type renderLoginSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, LoginSection loginSection, String usernameGuess, String nameOfForwardToPortal);

    Type renderNavigationSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, NavigationSection navigtionSection) throws IllegalStateException, IOException ;

    Type renderPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Page page);

    Type renderPageScript(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageScript pageScript);

    Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title) throws IllegalStateException, IOException;
    
    Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection, String title, String styleClass, String ref) throws IllegalStateException, IOException;

    Type renderParsedUrlContent(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ParsedUrlContent parsedUrlContent, String content);

    Type renderPaymentDetails(PrintWriter w, RenderRequest request, RenderResponse response, Policy model, PaymentDetails paymentDetails);

    Policy renderPaymentOptionSelector(PrintWriter w, RenderRequest request, RenderResponse response, Policy model, PaymentOptionSelector paymentOptionSelector);

    Proposer renderProposerDetails(PrintWriter w, RenderRequest request, RenderResponse response, Proposer proposer, ProposerDetails proposerDetails);

    Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext) throws IllegalStateException, IOException;
    
    Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext, String styleClass, String ref) throws IllegalStateException, IOException;

    Type renderQuestionPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionPage questionPage, String title) throws IllegalStateException, IOException;

    Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title) throws IllegalStateException, IOException;
    
    Type renderQuestionSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSection questionSection, String title, String styleClass, String ref) throws IllegalStateException, IOException;

    Type renderQuestionSeparator(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionSeparator questionSeparator, String title);

    Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId) throws IllegalStateException, IOException;
    
    Type renderQuestionWithDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithDetails questionWithDetails, String title, String detailTitle, String rowContext, String questionId, String detailId, String styleClass, String ref) throws IllegalStateException, IOException;

    Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId) throws IllegalStateException, IOException;
    
    Type renderQuestionWithSubSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionWithSubSection questionWithSubSection, String title, String rowContext, String questionId, String styleClass, String ref) throws IllegalStateException, IOException;

    Type renderQuitButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label); 

    Policy renderQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Policy policy, QuotationSummary quotationSummary) throws IOException;

    Policy renderReferralSummary(PrintWriter w, RenderRequest request, RenderResponse response, Policy policy, ReferralSummary referralSummary) throws IOException;

    Type renderRequoteButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label);

    Type renderRowScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, RowScroller rowScroller) throws IllegalStateException, IOException;

    Type renderSaveButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SaveButtonAction saveButtonAction, String label, boolean remoteUser);

    List<?> renderSaveQuotations(PrintWriter w, RenderRequest request, RenderResponse response, List<?> quotationSummaries, SavedQuotations saveedQuotations) throws IllegalStateException, IOException;

    Type renderSaveQuotationsFooter(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SavedQuotations saveedQuotations);

    Type renderSectionScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, SectionScroller sectionScroller) throws IllegalStateException, IOException;

    Type renderViewQuotationButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ViewQuotationButtonAction saveButtonAction, String quoteNumber, String label);

    Type renderClauseDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ClauseDetails clauseDetails, String title, Map<String,List<Clause>> groupedClauses);
}