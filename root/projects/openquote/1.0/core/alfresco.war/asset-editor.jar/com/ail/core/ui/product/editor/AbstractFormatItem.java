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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextPane;

public abstract class AbstractFormatItem implements IFormatItem {

    
    public static final String FORMAT_PANEL = "FormatPanel";
    private String id;
    private String name;
    private String args;
    private String unit;
    
    public AbstractFormatItem(String id, String name) {
        this.id = id;
        this.name = name;
    }
    public AbstractFormatItem(String name) {
        this.id = name;
        this.name = name;
    }
    public String toString() {
        return getName();
    }
    
    public boolean equals(Object id) {
        if (id instanceof AbstractFormatItem) {
            return getId().equals(((AbstractFormatItem)id).getId());
        } else {
            return getName().equals(id.toString());
        }
    }
    
    public JPanel getFormatPanel() {
        return getDefaultPanel();
    }
    
    public JPanel getDefaultPanel() {
        
        JPanel panel = new JPanel();
        panel.setName(FORMAT_PANEL);
        
        return panel;
    }
    
    public JPanel getFormatArgsPane(GridBagLayout layout, GridBagConstraints constraints) {
             
        JPanel panel = getDefaultPanel();
        panel.setLayout(layout);
        
        JLabel argsLabel = Controls.getDefaultLabel("Args");
        JTextPane argsText = Controls.getDefaultTextPane(getArgs());
        
        layout.setConstraints(argsText, constraints);
        
        panel.add(argsLabel);
        panel.add(argsText);
        
        return panel;
    }
    
    public String getArgs() {
        return args;
    }
    public void setArgs(String args) {
        this.args = args;
    }
    public String getUnit() {
        return unit;
    }
    public void setUnit(String unit) {
        this.unit = unit;
    }
    

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
