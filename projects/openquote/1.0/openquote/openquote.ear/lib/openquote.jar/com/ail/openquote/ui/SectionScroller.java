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
package com.ail.openquote.ui;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;

/**
 * <p>A Section scroller displays repeating blocks of questions. The data for the questions is selected
 * from the model by the scroller's {@link #getBinding() binding} property.</p>
 * <p><img src="doc-files/SectionScroller.png"/></p>
 * <p>The screenshot above shows a SectionScroller which is bound to assets - in this case vehicle
 * assets. The binding has selected two of these assets (as Records) from the {@link com.ail.openquote.Quotation Quotation}
 * being processed, and for each of those assets it renders a section built up from the page elements contained
 * in the SectionScroller. In this example, the SectionScroller contains 8 page elements, two of which have
 * detail associated with them.</p>
 * <p>A SectionScroller also has an optional {@link #getSectionTitle sectionTitle} property which is rendered at the top
 * of each section. The text of this {@link Label label} may be static, or as in this case dynamic. Dynamic titles are rendered 
 * using the context of the selected record, and may therefore be bound to fields in the record. In this case, the 
 * title is bound to the vehicle asset's make, model, and registrationNumber attributes.</p>
 * @see Label
 */
public class SectionScroller extends Repeater {
	private static final long serialVersionUID = -6043887157243002172L;

    /** Label rendered within the repeated sections to identify them */
    private Label sectionTitle;
    
    /**
     * This label is rendered within each repeating section of the SectionScroller. This is intended to help
     * the user identify the record which the sections belong to.
     * @return section label
     */
    public Label getSectionTitle() {
        return sectionTitle;
    }

    /**
     * @see #getSectionTitle()
     * @param sectionTitle
     */
    public void setSectionTitle(Label sectionTitle) {
        this.sectionTitle = sectionTitle;
    }

    @SuppressWarnings("unchecked")
    @Override
    public void renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        
        w.printf("<table width='100%%' border='0' cols='1' cellpadding='0'>");
        int rowCount=0;
        for(Iterator it=model.xpathIterate(getBinding()) ; it.hasNext() ; rowCount++) {
            Type t=(Type)it.next();
            
            w.printf("<tr><td>");
            w.printf(" <table width='100%%' border='0' cols='4' cellpadding='4'>");
            
            if (sectionTitle!=null) {
                w.printf("  <tr class='portlet-section-subheader'><td colspan='4'>");
                sectionTitle.renderResponse(request, response, t);
                w.printf("  </td></tr>");
            }
    
            for (Iterator<AttributeField> question=item.iterator() ; question.hasNext() ; ) {
                w.printf("<tr>");
                question.next().renderResponse(request, response, t, getBinding()+"["+rowCount+"]");
                w.printf("</tr>");
            }
            w.printf(" </table>");
            w.printf("</td></tr>");
        }
        w.printf("</table>");
    }
}

