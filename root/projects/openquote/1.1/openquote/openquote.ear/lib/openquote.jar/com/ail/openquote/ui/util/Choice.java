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
package com.ail.openquote.ui.util;

import java.util.ArrayList;
import java.util.List;

import com.ail.core.Type;

/**
 * Utility class which assist in the rendering of Choice types. The values made available to the user in
 * a choice may be hard wired into the {@link com.ail.core.Atribute Attribute} or derived from a Choice
 * Type. Typically, a choice Type is used when the number of options becomes unmanagebly large.
 * The Choice type also supports sub-choices, for example vehicle Make and Model choices.
 */
public class Choice extends Type {
    private static final long serialVersionUID = -7252168449721481890L;
    private List<Choice> choice=null;
    private String name;
    private String compiled;
    
    public Choice() {
        choice=new ArrayList<Choice>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Choice> getChoice() {
        return choice;
    }

    public void setChoice(List<Choice> choice) {
        this.choice = choice;
    }
    
    public String renderAsJavaScriptArray(String arrayName) {
        if (compiled==null) {
            StringBuffer buf=new StringBuffer();
            int i=1;
            
            buf.append("<script type='text/javascript'>");
            buf.append(arrayName).append("=new Array();");
            
            for(Choice m: choice) {
                buf.append(arrayName).append("[").append(i++).append("]=new Array('").append(m.getName()).append("'");
                for(Choice s: m.getChoice()) {
                    buf.append(",'").append(s.getName()).append("'");
                }
                buf.append(");");
            }
            
            buf.append("</script>");
            
            compiled=buf.toString();
        }

        return compiled;
    }
}
