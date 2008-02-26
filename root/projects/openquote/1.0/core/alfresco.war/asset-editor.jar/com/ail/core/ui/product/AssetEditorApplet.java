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
package com.ail.core.ui.product;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.StringBufferInputStream;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JApplet;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextPane;
import javax.swing.JTree;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import netscape.javascript.JSObject;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.ail.core.ui.product.editor.AbstractFormatItem;
import com.ail.core.ui.product.editor.Controls;
import com.ail.core.ui.product.editor.FormatItemFactory;
import com.ail.core.ui.product.editor.IFormatItem;

public class AssetEditorApplet extends JApplet {

    public final static int WINDOW_HEIGHT = 460;
    public final static int LEFT_PANE_WIDTH = 300;
    public final static int RIGHT_PANE_WIDTH = 360;
    public final static int WINDOW_WIDTH = LEFT_PANE_WIDTH + RIGHT_PANE_WIDTH;

    public JSplitPane splitPane = null;
    public JPanel editorPanel = null;
    
    private String editorType = null;
    
    public void init() {
        makeFrame();
    }

    private class EditorPanel extends JPanel {

        public EditorPanel() {

            EmptyBorder emptyBorder = new EmptyBorder(5, 5, 5, 5);
            BevelBorder bevelBorder = new BevelBorder(BevelBorder.LOWERED);
            CompoundBorder compoundBorder = new CompoundBorder(emptyBorder, bevelBorder);
            this.setBorder(new CompoundBorder(compoundBorder, emptyBorder));

            JTree tree = new JTree();
            TreeModelAdapter treeAdapter = new TreeModelAdapter();
          
            tree.setModel(treeAdapter);
            
            JScrollPane treeView = new JScrollPane(tree);
            treeView.setPreferredSize(new Dimension(LEFT_PANE_WIDTH, WINDOW_HEIGHT));

            final JPanel htmlPane = new JPanel();
            JScrollPane htmlView = new JScrollPane(htmlPane);
            htmlView.setPreferredSize(new Dimension(RIGHT_PANE_WIDTH, WINDOW_HEIGHT));

            splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, treeView, htmlView);
            splitPane.setContinuousLayout(true);
            splitPane.setDividerLocation(LEFT_PANE_WIDTH);
            splitPane.setPreferredSize(new Dimension(WINDOW_WIDTH + 10, WINDOW_HEIGHT + 10));

            this.setLayout(new BorderLayout());
            this.add("Center", splitPane);

            htmlView.setPreferredSize(new Dimension(RIGHT_PANE_WIDTH, WINDOW_HEIGHT));

            tree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent event) {
                    TreePath path = event.getNewLeadSelectionPath();
                    if (path != null) {
                        AdapterNode adpNode = (AdapterNode) path.getLastPathComponent();
                        htmlPane.removeAll();
                        htmlPane.add(getEditorPanel(adpNode.node));
                        
                        refresh();

                    }
                }
            });
        }

    }

    private JPanel getEditorPanel(Node node) {
        
        editorPanel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints constraints = Controls.getDefaultConstraints();

        editorPanel.setLayout(layout);

        JTextPane valueText = Controls.getDefaultTextPane();
        valueText.setText(node.getAttributes().getNamedItem("value").getNodeValue());
        
        final JComboBox formatCombo = new JComboBox();
        formatCombo.addItemListener(new ItemListener() {
            
            public void itemStateChanged(ItemEvent event) {

                setFormatArgsPanel(layout, constraints, formatCombo);
            }

        });
        
        DefaultComboBoxModel formatModel = new DefaultComboBoxModel();
        FormatItemFactory itemFactory = new FormatItemFactory(layout, constraints);
        formatModel.addElement(itemFactory.getForamtItem("string"));
        formatModel.addElement(itemFactory.getForamtItem("choice"));
        formatModel.addElement(itemFactory.getForamtItem("yesorno"));
        formatModel.addElement(itemFactory.getForamtItem("number"));
        formatModel.addElement(itemFactory.getForamtItem("currency"));
        formatModel.addElement(itemFactory.getForamtItem("date"));
        formatCombo.setModel(formatModel);
        
        
        formatCombo.setPreferredSize(new Dimension(Controls.STANDARD_TEXT_WIDTH, Controls.STANDARD_TEXT_HEIGHT));

        IFormatItem selectedFormatItem = getSelectedFormatItem(node, layout, constraints);
        
        //JPanel selectedFormatPanel = selectedFormatItem.getFormatPanel();
        JLabel spaceLabel1 = Controls.getSpaceLabel();
        JLabel spaceLabel2 = Controls.getSpaceLabel();
        
        layout.setConstraints(spaceLabel1, constraints);
        layout.setConstraints(valueText, constraints);
        layout.setConstraints(spaceLabel2, constraints);
        layout.setConstraints(formatCombo, constraints);
        
        editorPanel.add(spaceLabel1);
        editorPanel.add(Controls.getDefaultLabel("Value"));
        editorPanel.add(valueText);
        editorPanel.add(spaceLabel2);
        editorPanel.add(Controls.getDefaultLabel("Format"));
        editorPanel.add(formatCombo);
        
        formatCombo.setSelectedItem(selectedFormatItem);
        
        if (formatCombo.getSelectedIndex() == 0) {
            setFormatArgsPanel(layout, constraints, formatCombo);
        }
        
        return editorPanel;
    }

    private void setFormatArgsPanel(final GridBagLayout layout, final GridBagConstraints constraints, final JComboBox formatCombo) {
        Component[] components = editorPanel.getComponents();
        for (int i = 0; i < components.length; i ++) {
            if (AbstractFormatItem.FORMAT_PANEL.equals(components[i].getName())) {
                editorPanel.remove(components[i]);
                break;
            }
        }
        
        IFormatItem selectedFormatItem = (IFormatItem)formatCombo.getSelectedItem();
        JPanel selectedFormatPanel = selectedFormatItem.getFormatPanel();
        layout.setConstraints(selectedFormatPanel, constraints);
        editorPanel.add(selectedFormatPanel);
        
        refresh();
    }

    private void refresh() {
        int divLocation = splitPane.getDividerLocation();
        splitPane.setDividerLocation(divLocation + 1);
        splitPane.setDividerLocation(divLocation);
    }
    
    private IFormatItem getSelectedFormatItem(Node node, GridBagLayout layout, GridBagConstraints constraints) {
        
        String formatArgs = "";
        String unit = "";
        
        String format = node.getAttributes().getNamedItem("format").getNodeValue();
        String formatId = format;
        int formatArgsStart = format.indexOf(",");
        if (formatArgsStart != -1) {
            formatId = format.substring(0, formatArgsStart);
            formatArgs = format.substring(formatArgsStart + 1);
        }
        
        IFormatItem selectedFormatItem = new FormatItemFactory(layout, constraints).getForamtItem(formatId);
        
        try {
            unit = node.getAttributes().getNamedItem("unit").getNodeValue();
        } catch (NullPointerException e)  {
            //  allow through
        }
        
        selectedFormatItem.setUnit(unit);
        selectedFormatItem.setArgs(formatArgs);
        
        return selectedFormatItem;
    }

    
    public void makeFrame() {

        try {
            EditorPanel editorPanel = new EditorPanel();
            JSObject win = JSObject.getWindow(this);
//            if ("asset".equals(editorType)) {
//                getContentPane().add("Center", editorPanel);
//                setVisible(true);
//                show();
//                
//                win.call("showAppletEditor", null);
//                win = null;
//            } else {
                win.call("showTextEditor", null);
                validate();
                win = null;
//            }
            
        } catch (NullPointerException npe) {
            JOptionPane.showMessageDialog(this, "catch (NullPointerException npe)");
        }
    }

    private Document getAssetDocument() throws Exception {

        try {

            JSObject win = JSObject.getWindow(this);
            JSObject doc = (JSObject) win.getMember("document");
            JSObject form = (JSObject) doc.getMember("edit-file");
            JSObject assetTextArea = (JSObject) form.getMember("edit-file:textArea");
            String assetXml = (String) assetTextArea.getMember("value");
            //JOptionPane.showMessageDialog(this, assetXml);
                
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document assetDocument = builder.parse(new StringBufferInputStream(assetXml));
            
            if (assetDocument != null && assetDocument.getDocumentElement() != null) {
                //JOptionPane.showMessageDialog(this, assetDocument.getDocumentElement().getNodeName());
                if ("asset".equals(assetDocument.getDocumentElement().getNodeName())) {
                    editorType = "asset";
                }
            }
            return assetDocument;

        }
        catch (Exception e) {
            throw e;
        }
    }

    public class AdapterNode {
        Node node;

        // Construct an Adapter node from a DOM node
        public AdapterNode(Node node) {
            this.node = node;
            removeUnwantedNodes(node);
        }

        private void removeUnwantedNodes(Node node) {
            NodeList childNodes = node.getChildNodes();
            for (int i = 0; i < childNodes.getLength(); i++) {
                Node childNode = childNodes.item(i);
                if ("#text".equals(childNode.getNodeName())) {
                    node.removeChild(childNode);
                }
            }
        }

         public String toString() {

            String nodeId = "";
            try {
                nodeId = node.getAttributes().getNamedItem("id").getNodeValue();
            }
            catch (NullPointerException e1) {
                try {
                    nodeId = node.getAttributes().getNamedItem("assetTypeId").getNodeValue();
                }
                catch (NullPointerException e2) {
                    // nay worries
                }
            }
            if ("#document".equals(nodeId)) {
                nodeId = "Assets";
            }
          
            return nodeId;
        }

        public int index(AdapterNode child) {

            int count = childCount();
            for (int i = 0; i < count; i++) {
                AdapterNode n = this.child(i);
                if (child == n)
                    return i;
            }
            return -1; 
        }

        public AdapterNode child(int searchIndex) {
            return new AdapterNode(node.getChildNodes().item(searchIndex));
        }

        public int childCount() {
            return node.getChildNodes().getLength();
        }

    }

    public class TreeModelAdapter implements TreeModel {

        @SuppressWarnings("unchecked")
        private Vector listenerList = new Vector();
        
        public Object getRoot() {
            try {
                return new AdapterNode(getAssetDocument());
            }
            catch (Exception e) {
                return null;
            }
        }

        public boolean isLeaf(Object node) {
         
            return ((AdapterNode)node).childCount() == 0;
            
        }

        public int getChildCount(Object parent) {
            AdapterNode node = (AdapterNode) parent;
            return node.childCount();
        }

        public Object getChild(Object parent, int index) {
            AdapterNode node = (AdapterNode) parent;
            return node.child(index);
        }

        public int getIndexOfChild(Object parent, Object child) {
            AdapterNode node = (AdapterNode) parent;
            return node.index((AdapterNode) child);
        }

        public void valueForPathChanged(TreePath path, Object newValue) {
          // null
        }

        @SuppressWarnings("unchecked")
        public void addTreeModelListener(TreeModelListener listener) {
            if (listener != null && !listenerList.contains(listener)) {
                listenerList.addElement(listener);
            }
        }

        public void removeTreeModelListener(TreeModelListener listener) {
            if (listener != null) {
                listenerList.removeElement(listener);
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeNodesChanged(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener = (TreeModelListener) listeners.nextElement();
                listener.treeNodesChanged(e);
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeNodesInserted(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener = (TreeModelListener) listeners.nextElement();
                listener.treeNodesInserted(e);
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeNodesRemoved(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener = (TreeModelListener) listeners.nextElement();
                listener.treeNodesRemoved(e);
            }
        }

        @SuppressWarnings("unchecked")
        public void fireTreeStructureChanged(TreeModelEvent e) {
            Enumeration listeners = listenerList.elements();
            while (listeners.hasMoreElements()) {
                TreeModelListener listener = (TreeModelListener) listeners.nextElement();
                listener.treeStructureChanged(e);
            }
        }
    }
}
