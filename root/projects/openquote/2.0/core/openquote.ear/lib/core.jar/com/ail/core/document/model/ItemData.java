/* Copyright Applied Industrial Logic Limited 2006. All rights reserved. */
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
package com.ail.core.document.model;

import com.ail.core.Identified;
import com.ail.core.Type;

/**
 * Node of the document structure object graph. 
 */
public abstract class ItemData extends Type implements Identified {
    private String id;
    private String title;
    private Long order;
    private String value;
    private String style="string";
    
    public String getStyle() {
        return style;
    }

    public void setStyle(String format) {
        this.style = format;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    /**
     * The <i>order</i> of this item with respect to other items owned by the same parent.
     * Note: lower order items appear first.
     * @return container's order
     */
    public Long getOrder() {
        return order;
    }

    /**
     * @see #getOrder()
     * @param order
     */
    public void setOrder(Long order) {
        this.order = order;
    }
    
    
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id=id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    protected String idAsAttribute() {
        return (id!=null) ? " id=\""+id+"\"" : "";
    }
    
    protected String titleAsAttribute() {
        return (title!=null) ? " title=\""+title+"\"" : "";
    }

    protected String orderAsAttribute() {
        return (order!=null) ? " order=\""+order+"\"" : "";
    }

    protected String styleClassAsAttribute() {
        return (style!=null) ? " class=\""+style+"\"" : "";
    }

    public boolean compareById(Object that) {
        if (that instanceof ItemData) {
            return (id!=null && id.endsWith(((ItemData)that).getId()));
        }
        else {
            return false;
        }
    }

    public abstract void render(RenderContext context);
}
