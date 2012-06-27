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
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.pageflow.util.QuotationContext;
import com.ail.insurance.policy.Clause;
import com.ail.insurance.policy.Policy;

/**
 */
public class ClauseDetails extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;
	private String title;
	private String groupBy;

	public ClauseDetails() {
		super();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGroupBy() {
		return groupBy;
	}

	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}

	@SuppressWarnings("unchecked")
	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
    	if (conditionIsMet(model)) {

        	PrintWriter w=response.getWriter();
        	
    		Map<String,List<Clause>> groupedClauses=new HashMap<String,List<Clause>>();
    		Policy quote=(Policy)model;
    		
    		for(Clause clause: quote.getClause()) {
    			StringBuffer compoundKey=new StringBuffer();
    
    			if (groupBy!=null) {
    				for(Iterator<String> it=clause.xpathIterate(groupBy) ; it.hasNext() ; ) {
    					String gp=it.next();
    					if (compoundKey.length()==0) {
    						if (!it.hasNext()) {
    							compoundKey.append("The following endorsement(s) apply to ").append(gp).append(" only"); // TODO i18n needed
    						}
    						else {
    							compoundKey.append("The following endorsement(s) apply to ").append(gp); // TODO i18n needed
    						}
    					}
    					else if (!it.hasNext()) {
    						compoundKey.append(" and ").append(gp); // TODO i18n needed
    					}
    					else {
    						compoundKey.append(", ").append(gp);
    					}
    				}
    			}
    			else {
    				compoundKey.append("The following endorsement(s) are applicable"); // TODO i18n needed
    			}
    			
    			String key=compoundKey.toString();
    			
    			if (!groupedClauses.containsKey(key)) {
    				groupedClauses.put(key, new ArrayList<Clause>());
    			}
    			
    			groupedClauses.get(key).add(clause);
    		}
    
    		if (groupedClauses.size()==1) {
    			String oldKey=groupedClauses.keySet().iterator().next();
    			List<Clause> clauses=groupedClauses.get(oldKey);
    			groupedClauses.clear();
    			groupedClauses.put("The following endorsement(s) are applicable", clauses); // TODO i18n needed
    		}
    		
        	model=QuotationContext.getRenderer().renderClauseDetails(w, request, response, model, this, i18n(title), groupedClauses);
    	}
    	
    	return model;
	}
}
