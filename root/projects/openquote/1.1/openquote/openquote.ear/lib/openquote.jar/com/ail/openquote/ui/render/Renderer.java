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

public interface Renderer {
	Type renderAnswer(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Answer answer, String title, String answerText) throws IOException;
	
	Type renderAnswerScroller(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerScroller answerScroller) throws IOException;
	
	Type renderAnswerSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AnswerSection answerSection, String title)	throws IOException;

	Type renderAssessmentSheetDetails(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AssessmentSheetDetails assessmentSheetDetails)	throws IOException;

	Type renderAttributeField(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id, String onChange, String onLoad)	throws IOException;

	Type renderAttributeFieldPageLevel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, AttributeField attributeField, String boundTo, String id)	throws IOException;

	Type renderBlank(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Blank blank) throws IOException;
	
    Type renderBrokerQuotationSummary(PrintWriter w, RenderRequest request, RenderResponse response, Type model, BrokerQuotationSummary brokerQuotationSummary);

    Type renderCommandButtonAction(PrintWriter w, RenderRequest request, RenderResponse response, Type model, CommandButtonAction commandButtonAction, String label, boolean immediate); 
    
    Type renderInformationPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, InformationPage informationPage, String title, List<PageElement> pageElements) throws IOException;

    Type renderLabel(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Label label, String format, Object[] params);

    Type renderLoginSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, LoginSection loginSection, String usernameGuess, String nameOfForwardToPortal);

    Type renderNavigationSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, NavigationSection navigtionSection) throws IllegalStateException, IOException ;

    Type renderPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Page page);

    Type renderPageScriptHeader(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageScript pageScript);

    Type renderPageSection(PrintWriter w, RenderRequest request, RenderResponse response, Type model, PageSection pageSection) throws IllegalStateException, IOException;

    Type renderParsedUrlContent(PrintWriter w, RenderRequest request, RenderResponse response, Type model, ParsedUrlContent parsedUrlContent, String content);

    Type renderPaymentDetails(PrintWriter w, RenderRequest request, RenderResponse response, Quotation model, PaymentDetails paymentDetails);

    Quotation renderPaymentOptionSelector(PrintWriter w, RenderRequest request, RenderResponse response, Quotation model, PaymentOptionSelector paymentOptionSelector);

    Proposer renderProposerDetails(PrintWriter w, RenderRequest request, RenderResponse response, Proposer proposer, ProposerDetails proposerDetails);

    Type renderQuestion(PrintWriter w, RenderRequest request, RenderResponse response, Type model, Question question, String title, String rowContext) throws IllegalStateException, IOException;

    Type renderQuestionPage(PrintWriter w, RenderRequest request, RenderResponse response, Type model, QuestionPage questionPage, String title) throws IllegalStateException, IOException;
}