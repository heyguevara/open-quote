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

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextPane;

public class Controls {

    public static final int STANDARD_TEXT_WIDTH = 170;
    public static final int STANDARD_TEXT_HEIGHT = 23;
    public static final int STANDARD_LABEL_WIDTH = 50;
    public static final int STANDARD_BUTTON_WIDTH = 40;
    
    public static JTextPane getDefaultTextPane() {
        
        return getDefaultTextPane(null);
    }
    public static JTextPane getDefaultTextPane(String text) {
        
        JTextPane textPane = new JTextPane();
        textPane.setPreferredSize(new Dimension(STANDARD_TEXT_WIDTH, STANDARD_TEXT_HEIGHT));
        textPane.setBorder(BorderFactory.createEtchedBorder());
        
        if (text != null) {
            textPane.setText(text);
        }
        return textPane;
    }
    
    public static JLabel getDefaultLabel(String text) {
        
        JLabel label = new JLabel(text);
        label.setPreferredSize(new Dimension(STANDARD_LABEL_WIDTH, STANDARD_TEXT_HEIGHT));
        label.setHorizontalTextPosition(JLabel.RIGHT);
        //label.setBorder(BorderFactory.createEtchedBorder());
        
        return label;
    }
    
   public static JLabel getSpaceLabel() {
        
        JLabel spaceLabel = new JLabel("");
        spaceLabel.setPreferredSize(new Dimension(50,18));
        
        return spaceLabel;
    }
   
   public static GridBagConstraints getDefaultConstraints() {
       
       GridBagConstraints constraints = new GridBagConstraints();
       //constraints.fill = GridBagConstraints.BOTH;
       constraints.gridwidth = GridBagConstraints.REMAINDER;
       
       return constraints;
   }
   
   public static JButton getDefaultButton(String text) {
       
       JButton button = new JButton();
       button.setText(text);
       button.setPreferredSize(new Dimension(STANDARD_LABEL_WIDTH, 26));
       
       return button;
   }
}
