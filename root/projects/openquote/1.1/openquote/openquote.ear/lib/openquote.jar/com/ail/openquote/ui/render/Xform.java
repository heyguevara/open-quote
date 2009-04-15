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
import com.ail.openquote.ui.PageElement;

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
}
