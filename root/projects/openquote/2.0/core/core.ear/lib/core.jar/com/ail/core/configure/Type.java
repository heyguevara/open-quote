/* Copyright Applied Industrial Logic Limited 2002. All rights reserved. */
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
package com.ail.core.configure;

/**
 * @version $Revision: 1.5 $
 * @state $State: Exp $
 * @date $Date: 2006/04/06 20:49:35 $
 * @source $Source: /home/bob/CVSRepository/projects/core/core.ear/core.jar/com/ail/core/configure/Type.java,v $
 * @stereotype type
 */
public class Type extends Group {
    static final long serialVersionUID = 7585495513368222454L;

    private String builder = null;

    private String key = null;

    private boolean singleInstance = false;

    private transient com.ail.core.Type prototype = null;

    /**
     * Default constructor.
     */
    public Type() {
    }

    public String getBuilder() {
        return builder;
    }

    public void setBuilder(String builder) {
        this.builder = builder;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public com.ail.core.Type getPrototype() {
        return prototype;
    }

    public void setPrototype(com.ail.core.Type prototype) {
        this.prototype = (com.ail.core.Type) prototype;
    }

    public boolean isSingleInstance() {
        return singleInstance;
    }

    public void setSingleInstance(boolean singleInstance) {
        this.singleInstance = singleInstance;
    }
}
