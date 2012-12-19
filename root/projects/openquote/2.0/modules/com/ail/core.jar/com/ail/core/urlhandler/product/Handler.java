/* Copyright Applied Industrial Logic Limited 2008. All rights reserved. */
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

package com.ail.core.urlhandler.product;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.xml.bind.DatatypeConverter;

import com.ail.core.CoreProxy;
import com.ail.core.ThreadLocale;

/**
 * The handler deals with URLs of the form: "product://". All this class does is
 * to transparently transform a "product://" URL into a reference to a resource
 * in the product repository. The reference is always language sensitive in that
 * the language associated with the current thread's
 * {@link com.ail.core.ThreadLocale} is always passed as a parameter in the URL.
 * The repository therefore has the option of returning language specific
 * variants if it supports them.
 * <p/>
 * The details of the location of the content repository and the credentials to
 * use to access it are picked up from parameters in the configure systemm's
 * "ProductURLHandler" property group. Three properties are referenced in this
 * group: "BaseURL" defines the location of the repository, the "Username" and
 * "Password" parameters define the credentials to use to connect to the repo.
 * For example, a URL like:
 * <code>product://localhost:8080/Demo/Demo/Welcome.html</code> is translated
 * into a call to
 * <code>&lt;BaseURL&gt;/Demo/Demo/Welcome.html</code>.
 * <p/>
 */
public class Handler extends URLStreamHandler {
    
    protected URLConnection openConnection(URL u) throws IOException {
        CoreProxy cp=new CoreProxy();
        
        String username =  cp.getParameterValue("ProductURLHandler.Username");
        String password =  cp.getParameterValue("ProductURLHandler.Password");
        String baseURL = cp.getParameterValue("ProductURLHandler.BaseURL");
        
        String credentials=username+":"+password;
        String authToken="Basic "+DatatypeConverter.printBase64Binary(credentials.getBytes());
        
        String language=ThreadLocale.getThreadLocale().getLanguage();
        
        URL actualURL = new URL(baseURL+u.getPath());

        URLConnection urlConnection = actualURL.openConnection();
        
        urlConnection.setRequestProperty("Authorization", authToken);
        urlConnection.setRequestProperty("Content-Language", language);
        
        return urlConnection;
    }
}
