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
package com.ail.insurance.pageflow;

import java.io.IOException;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;

/**
 * A PageSection renders as a section on the page containing a number of columns (1 by default). The section may
 * contain any number of sub-elements. A section may optionally define a title to be rendered above the 
 * section itself.
 */
public class PageSection extends PageContainer {
	private static final long serialVersionUID = 6794522768423045427L;

    /** Number of columns to be rendered */
    private int columns=1;

    /**
     * The number of columns to be rendered in this section. This defaults to 1.
     * @return number of columns
     */
    public int getColumns() {
        return columns;
    }

    /**
     * @see #getColumns()
     * @param columns Number of columns to render.
     */
    public void setColumns(int columns) {
        this.columns = columns;
    }

    @Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand("PageSection", request, response, model);
    }

    @Override
    public Type renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        return executeTemplateCommand("PageSectionHeader", request, response, model);
    }
}
