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
package com.ail.core.ui.product.editor;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.StringTokenizer;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public class FormatItemFactory {

    GridBagLayout layout = null;

    GridBagConstraints constraints = null;

    public FormatItemFactory(GridBagLayout layout, GridBagConstraints constraints) {
        if (layout == null) {
            layout = new GridBagLayout();
            constraints = Controls.getDefaultConstraints();
        }

        this.layout = layout;
        this.constraints = constraints;

    }

    public IFormatItem getForamtItem(String id) throws IllegalArgumentException {

        if ("string".equals(id)) {
            return new StringFormatItem(id);
        }
        else if ("number".equals(id)) {
            return new NumberFormatItem(id);
        }
        else if ("choice".equals(id)) {
            return new ChoiceFormatItem(id);
        }
        else if ("yesorno".equals(id)) {
            return new YesOrNoFormatItem(id);
        }
        else if ("date".equals(id)) {
            return new DateFormatItem(id);
        }
        else if ("currency".equals(id)) {
            return new CurrencyFormatItem(id);
        }

        throw new IllegalArgumentException(id + " is not a valid FormatItem id.");
    }

    private class StringFormatItem extends AbstractFormatItem {

        public StringFormatItem(String id) {
            super(id, "String");
        }

        public JPanel getFormatPanel() {
            return getFormatArgsPane(layout, constraints);
        }
    }

    private class NumberFormatItem extends AbstractFormatItem {

        public NumberFormatItem(String id) {
            super(id, "Number");
        }

        public JPanel getFormatPanel() {
            return getFormatArgsPane(layout, constraints);
        }

    }

    private class ChoiceFormatItem extends AbstractFormatItem {

        public ChoiceFormatItem(String id) {
            super(id, "Choice");
        }

        public JPanel getFormatPanel() {

            JPanel panel = getDefaultPanel();
            //JLabel choicesLabel = Controls.getDefaultLabel("");
            JList choicesList = new JList();
            JButton addButton = Controls.getDefaultButton("+");
            JButton removeButton = Controls.getDefaultButton("-");
            
            panel.setLayout(layout);
            
            //choicesLabel.setVerticalAlignment(JLabel.NORTH);
            choicesList.setPreferredSize(new Dimension(Controls.STANDARD_TEXT_WIDTH, Controls.STANDARD_TEXT_HEIGHT * 4));
            choicesList.setBorder(BorderFactory.createEtchedBorder());
            populateList(choicesList);
            
            addButton.setText("+");
            removeButton.setText("-");
           
            layout.setConstraints(choicesList, constraints);
            
            panel.add(Controls.getSpaceLabel());
            panel.add(choicesList);
            panel.add(Controls.getSpaceLabel());
            panel.add(addButton);
            panel.add(removeButton);
            panel.add(Controls.getDefaultLabel(""));
            
            return panel;
        }

        private void populateList(JList choicesList) {
            DefaultListModel listModel = new DefaultListModel();
            if (getArgs() != null && !"".equals(getArgs())) {
                StringTokenizer tokens = new StringTokenizer(getArgs(), "|");
                while(tokens.hasMoreTokens()) {
                    String choice = tokens.nextToken();
                    int index = choice.indexOf("#");
                    
                    listModel.addElement(choice.substring(index + 1));
                }
                choicesList.setModel(listModel);
            }
        }
    }

    private class YesOrNoFormatItem extends AbstractFormatItem {

        public YesOrNoFormatItem(String id) {
            super(id, "Yes or no");
        }
    }

    private class DateFormatItem extends AbstractFormatItem {

        public DateFormatItem(String id) {
            super(id, "Date");
        }

        public JPanel getFormatPanel() {
            return getFormatArgsPane(layout, constraints);
        }
    }

    private class CurrencyFormatItem extends AbstractFormatItem {

        public CurrencyFormatItem(String id) {
            super(id, "Currency");
        }

        public JPanel getFormatPanel() {

            JPanel panel = getFormatArgsPane(layout, constraints);
            panel.setLayout(layout);
            
            JLabel unitLabel = Controls.getDefaultLabel("Unit");
            JTextPane unitText = Controls.getDefaultTextPane(getUnit());

            layout.setConstraints(unitText, constraints);

            panel.add(unitLabel);
            panel.add(unitText);

            return panel;
        }
    }
}
