/* Copyright Applied Industrial Logic Limited 2014. All rights Reserved */
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

import com.ail.ui.client.i18n.Messages;
import com.ail.ui.shared.model.PolicyDetailDTO;
import com.ail.ui.shared.validation.FieldVerifier;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Panel;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * GWT entry point class for Quick Search.
 */
public class QuickSearch implements EntryPoint {

    // Proxy used to interact with the server
	private final QuickSearchServiceAsync quickSearchService = GWT.create(QuickSearchService.class);
	
	private final Messages messages = GWT.create(Messages.class);
	
	private final Button searchButton = new Button(messages.search());
	private final Button closeButton = new Button(messages.close());
	private final TextBox searchField = new TextBox();
	private final Label serverResponseLabel = new Label();
	private final Label validationLabel = new Label();
	private final Panel mainPanel = new VerticalPanel();
	private final DialogBox dialogBox = new DialogBox();
	private final VerticalPanel resultsDialogPanel = new VerticalPanel();
	private final Panel closeButtonPanel = new HorizontalPanel();
	
	/**
	 * Called on load
	 */
	@Override
	public void onModuleLoad() {

		// Retrieve elements from jsp
		RootPanel.get("gui-quicksearch-search-field-container").add(searchField);
		RootPanel.get("gui-quicksearch-search-button-container").add(searchButton);
		RootPanel.get("gui-quicksearch-error-label-container").add(validationLabel);
		
		setComponentStyles();
		
		closeButtonPanel.add(closeButton);
		resultsDialogPanel.add(mainPanel);
		resultsDialogPanel.add(serverResponseLabel);
		resultsDialogPanel.add(closeButtonPanel);
		
		dialogBox.setText(messages.quickSearchResults().toUpperCase());
        dialogBox.setAnimationEnabled(true);
		dialogBox.setWidget(resultsDialogPanel);
		
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

			    resetControls();
                
				String searchInput = searchField.getText();
				if (!FieldVerifier.hasLength(searchInput)) {
					validationLabel.setText(messages.quickSearchValidation());
					enableInputControls(true);
					return;
				}
				
				quickSearchService.quickSearchServer(searchInput,
						new AsyncCallback<PolicyDetailDTO>() {

							public void onSuccess(PolicyDetailDTO result) {
							    if (result.isInitialised()) {
    								mainPanel.add(new SearchResultPanel(result).display());
    								initDialog();
							    } else {
							        validationLabel.setText(messages.noResultsFound());
							        enableInputControls(true);
							    }
							}
							
							public void onFailure(Throwable caught) {
                                serverResponseLabel.setText(messages.serverError());
                                initDialog();
                            }
						});
			}
            
		}

		SearchHandler handler = new SearchHandler();
		searchButton.addClickHandler(handler);
		searchField.addKeyUpHandler(handler);
		
		closeButton.addClickHandler(new ClickHandler() {
            public void onClick(ClickEvent event) {
                dialogBox.hide();
                enableInputControls(true);
            }
        });
		
	}

	private void initDialog() {
        dialogBox.center();
        closeButton.setFocus(true);
    }
    
	// clear error messages and disable input controls
    private void resetControls() {
        validationLabel.setText("");
        serverResponseLabel.setText("");
        mainPanel.clear();
        enableInputControls(false);
    }
    
	private void enableInputControls(boolean enabled) {
        searchField.setEnabled(enabled);
        searchButton.setEnabled(enabled);
        searchButton.setFocus(enabled);
    }
	
	private void setComponentStyles() {
        serverResponseLabel.addStyleName("gui-error-label gui-error-label");
        validationLabel.addStyleName("gui-error-label gui-error-label");
        searchField.setStylePrimaryName("gui-text-box");
        resultsDialogPanel.getElement().setId("gui-quicksearch-dialog");
        closeButtonPanel.getElement().setId("gui-quicksearch-dialog-close-button");
        searchField.getElement().setId("gui-quick-search-input");
    }
	
}
