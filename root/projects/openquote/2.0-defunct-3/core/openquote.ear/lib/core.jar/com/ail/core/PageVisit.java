/* Copyright Applied Industrial Logic Limited 2005. All rights Reserved */
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
package com.ail.core;

import java.util.Date;

import com.ail.annotation.TypeDefinition;

/**
 * Records the detail of a page visit during the processing of quotation.
 */
@TypeDefinition
public class PageVisit extends Type {
	private static final long serialVersionUID = -7207431665904766363L;
	private String pageName;	// Name of the page that was visited
	private Date visited;		// Date the page was visited
	
	public PageVisit() {
	}
	
	public PageVisit(String pageName) {
		this(pageName, new Date());
	}
	
	public PageVisit(String pageName, Date visited) {
		this.pageName=pageName;
		this.visited=visited;
	}
	
	public String getPageName() {
		return pageName;
	}
	
	public void setPageName(String pageName) {
		this.pageName = pageName;
	}
	
	public Date getVisited() {
		return visited;
	}

	public void setVisited(Date visited) {
		this.visited = visited;
	}
}
