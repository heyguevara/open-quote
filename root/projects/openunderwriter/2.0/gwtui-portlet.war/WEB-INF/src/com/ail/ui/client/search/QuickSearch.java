/* Copyright Applied Industrial Logic Limited 20014. All rights Reserved */
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
package com.ail.ui.client.search;

import com.ail.ui.client.AbstractEntryPoint;
import com.ail.ui.shared.model.PolicyDetailDTO;
import com.ail.ui.shared.validation.FieldVerifier;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * GWT entry point class for Quick Search.
 */
public class QuickSearch extends AbstractEntryPoint {

	private final QuickSearchServiceAsync quickSearchService = GWT.create(QuickSearchService.class);

	/**
	 * Called on load
	 */
	@Override
	public void onModuleLoad() {
	    
		final Button sendButton = new Button("Send");
		final TextBox searchField = new TextBox();
		final Label validationLabel = new Label();
		final Panel mainPanel = new VerticalPanel();

		// Retrieve elements from jsp
		RootPanel.get("searchFieldContainer").add(searchField);
		RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("errorLabelContainer").add(validationLabel);


		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Quick Search");
		dialogBox.setAnimationEnabled(true);
		
		final Button closeButton = new Button("Close");
		closeButton.getElement().setId("closeButton");
		
		final HTML serverResponseLabel = new HTML();
		serverResponseLabel.addStyleName("serverResponseLabelError");
		
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		
		dialogVPanel.add(mainPanel);
		dialogVPanel.add(serverResponseLabel);
		
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);
		

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Handler for the sendButton and searchField
		class SearchHandler implements ClickHandler, KeyUpHandler {
			
			public void onClick(ClickEvent event) {
				callServer();
			}

			/**
			 * Fire on Enter key.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					callServer();
				}
			}

			private void callServer() {

			    resetDialog();
                
				String searchInput = searchField.getText();
				if (!FieldVerifier.hasLength(searchInput)) {
					validationLabel.setText("Please enter a valid quote or policy number");
					sendButton.setEnabled(true);
					return;
				}
				
				quickSearchService.quickSearchServer(searchInput,
						new AsyncCallback<PolicyDetailDTO>() {

							public void onSuccess(PolicyDetailDTO result) {
								mainPanel.add(new SearchResultPanel(result).display());
								initDialog();
							}
							
							public void onFailure(Throwable caught) {
                                serverResponseLabel.setHTML(SERVER_ERROR);
                                initDialog();
                            }
						});
			}

			private void initDialog() {
                dialogBox.center();
                closeButton.setFocus(true);
            }
			
            private void resetDialog() {
                validationLabel.setText("");
                serverResponseLabel.setText("");
			    mainPanel.clear();
                sendButton.setEnabled(false);
            }
		}

		SearchHandler handler = new SearchHandler();
		sendButton.addClickHandler(handler);
		searchField.addKeyUpHandler(handler);
		searchField.setFocus(true);
	}
}
