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
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * Search panel results to show retrieved data or 'no results found' message
 */
public class SearchResultPanel extends VerticalPanel {

    private final Messages messages = GWT.create(Messages.class);
    
    private FlexTable grid = new FlexTable();
    
    private PolicyDetailDTO detail;

    public SearchResultPanel(PolicyDetailDTO detail) {
        this.detail = detail;
    }

    public SearchResultPanel display() {
        
        if (detail.isInitialised()) {
            grid.setCellPadding(3);
            
            addRow(messages.quoteNumber(), detail.getQuotationNumber());
            addRow(messages.policyNumber(), detail.getPolicyNumber());
            addRow(messages.name(), detail.getPolicyHolderName());
            
            if (!detail.getPolicyHolderAddress().isEmpty()) {
                addRow(messages.address(), detail.getPolicyHolderAddress().get(0)); // address first line
                for (int i = 1; i < detail.getPolicyHolderAddress().size(); i++) {
                    addRow("", detail.getPolicyHolderAddress().get(i)); // rest of address
                }
            }
            addRow(messages.quoteDate(), detail.getQuoteDate());
            addRow(messages.expiryDate(), detail.getExpiryDate());
            addRow(messages.product(), detail.getProduct());
            addRow(messages.grossPremium(), detail.getPremium());
            
            add(grid);
            
        } else {
            
            add(new Label(messages.noResultsFound()));
        }
        
        return this;
    }

    private void addRow(String label, String value) {
        int row = grid.getRowCount();
        grid.setText(row, 0, label);
        grid.setText(row, 1, value);
    }
}
