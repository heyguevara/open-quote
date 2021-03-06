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

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.openquote.ui.util.QuotationContext;

/**
 * A RowScroller represents a Collection of records in a table format with one row per record.<br>
 * <p><img src="doc-files/RowScroller.png"/></p>
 * <p>The records included in the scroller are selected by the scroller's {@link #getBinding() binding}, and
 * the columns are defined by the {@link AttributeField AttributeFields} included in the scroller's
 * {@link #getItem() items} list.</p>
 * <p>The form elements used to render each item cell and the validations applied to them are both defined by
 * the nature of the {@link Attribute} that the item is bound to. The binding itself is evaluated relative to the binding
 * of the scroller. See {@link SectionScoller SectionScroller} for an example.</p>
 * <p>RowScroller supports the addition and removal of rows from the scroller if the {@link #isAddAndDeleteEnabled() 
 * addAndDeleteEnabled} property is set to true. If this is the case, then the <b>row controls</b> (see screenshot)
 * are rendered. The {@link #getMinRows() minRows} and {@link #getMaxRows() maxRows} properties limit what the user
 * can do. A user cannot remove rows if that would leave the scroller with less than <i>minRows</i>, and the <b>add 
 * row</b> icon will be disabled if the scroller already contains <i>maxRows</i>. In the screenshot above, the scroller
 * has <i>minRows</i> set to 1, so the user cannot remove the first row. <i>maxRows</i> is set to 4, so in its 
 * current state the user can add one more row.</p>
 * @see SectionScroller
 * @see AttributeField
 */
public class RowScroller extends Repeater {
	private static final long serialVersionUID = -6043887157243002172L;
    
    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    	if (!conditionIsMet(model)) {
    		return model;
    	}

    	PrintWriter w=response.getWriter();

        QuotationContext.getRenderer().renderRowScroller(w, request, response, model, this);
        
        return model;
    }

    /**
     * SHOULD BE ON ROWSCROLLER NO?
     */
    public boolean isBoundToRequiredColumnAttribute(Type model, String binding, int column) {
		Type t=model.xpathGet(binding, Type.class);
		if (t!=null) {
			return t.getAttribute(column).isRequired();
		}
		else {
			return false;
		}
    }
}

