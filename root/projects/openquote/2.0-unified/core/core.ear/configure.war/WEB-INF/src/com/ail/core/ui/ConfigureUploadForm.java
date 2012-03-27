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

package com.ail.core.ui;

import java.util.Collection;

import com.ail.core.Type;

/**
 * Form object used to back the CAR file upload portlet 
 */
public class ConfigureUploadForm extends Type {
    private Collection<String> namespaceItems;
    private byte[] uploadedFile;
    
    public Collection<String> getNamespaceItems() {
        return namespaceItems;
    }
    
    public void setNamespaceItems(Collection<String> namespaceItems) {
        this.namespaceItems=namespaceItems;
    }
    
    public byte[] getUploadedFile() {
        return uploadedFile;
    }
    
    public void setUploadedFile(byte[] uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public void clearUploadedFile() {
        this.uploadedFile=null;
    }
}
