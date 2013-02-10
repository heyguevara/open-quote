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

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;

import javax.xml.bind.DatatypeConverter;

import com.ail.core.CoreProxy;
import com.ail.core.ThreadLocale;

/**
 * The handler deals with URLs of the form: "product://". All this class does is
 * to transparently transform a "product://" URL into a reference to a resource
 * in the product repository. The Handler is sensitive to the convention used in
 * the repository to store locale specific content. Specifically, the language
 * code is appended to the content path. If the path ends in a file extension,
 * the language is appended immediately before the extension. If locale specific
 * content cannot be found, then the default content (without any language
 * specified) is returned. </p> For example, for an German language locale the
 * URL "http://localhost/file.html" would initially be searched for as
 * "http://localhost/file_de.html". If this is not found, then
 * "http://localhost/file.html" would be resolved instead.
 * <p/>
 * The details of the location of the content repository and the credentials to
 * use to access it are picked up from parameters in the configure systemm's
 * "ProductURLHandler" property group. Three properties are referenced in this
 * group: "BaseURL" defines the location of the repository, the "Username" and
 * "Password" parameters define the credentials to use to connect to the repo.
 * For example, a URL like:
 * <code>product://localhost:8080/Demo/Demo/Welcome.html</code> is translated
 * into a call to <code>&lt;BaseURL&gt;/Demo/Demo/Welcome.html</code>.
 * <p/>
 */
public class Handler extends URLStreamHandler {
    private static final String HTTP_ERROR_412_STRING = "HTTP response code: 412";

    protected URLConnection openConnection(URL productURL) throws IOException {
        URLConnection urlConnection = null;
        CoreProxy cp = new CoreProxy();

        String username = cp.getParameterValue("ProductURLHandler.Username");
        String password = cp.getParameterValue("ProductURLHandler.Password");
        String protocol = cp.getParameterValue("ProductURLHandler.Protocol");
        String host = cp.getParameterValue("ProductURLHandler.Host");
        Integer port = new Integer(cp.getParameterValue("ProductURLHandler.Port"));
        String path = cp.getParameterValue("ProductURLHandler.Path");
        
        String baseURL=new URL(protocol, host, port, path).toExternalForm();

        String credentials = username + ":" + password;
        String authToken = "Basic " + DatatypeConverter.printBase64Binary(credentials.getBytes());

        String language = ThreadLocale.getThreadLocale().getLanguage();

        // 1) First try to fetch the content with the thread's locale.
        // 2) If that yields a FileNotFound or a 412, try to fetch it without the locale.
        // 3) If that fails with a FileNotFound or a 412, throw a FileNotFound
        // 4) Any other exceptions are passed on as they are.
        try {
            urlConnection = connectUsingLanguage(productURL, baseURL, authToken, language);
        } catch (IOException e1) {
            if (e1 instanceof FileNotFoundException || e1.getMessage().contains(HTTP_ERROR_412_STRING)) {
                try {
                    urlConnection = connectUsingLanguage(productURL, baseURL, authToken, "");
                } catch (IOException e2) {
                    if (e1 instanceof FileNotFoundException || e2.getMessage().contains(HTTP_ERROR_412_STRING)) {
                        throw new FileNotFoundException(productURL.toString());
                    }
                    throw e2;
                }
            } else {
                throw e1;
            }
        }

        return urlConnection;
    }

    /**
     * Attempt to connect to a specified piece of content using specified
     * authentication and language. The actual URL is built from the URL,
     * baseURL and language passed in. 
     * @param productURL
     * @param baseURL
     * @param authToken
     * @param language
     * @return
     * @throws MalformedURLException
     * @throws IOException
     */
    URLConnection connectUsingLanguage(URL productURL, String baseURL, String authToken, String language) throws MalformedURLException, IOException {
        String canonicalPath = baseURL + productURL.getPath();

        URL actualURL = new URL(addLanguageToURL(canonicalPath, language));

        URLConnection urlConnection = actualURL.openConnection();

        urlConnection.setRequestProperty("Authorization", authToken);
        urlConnection.setRequestProperty("Content-Language", language);

        urlConnection.getInputStream();

        return urlConnection;
    }

    /**
     * Add a language code to a content path. The convention is to include the
     * language (ISO code) before the last '.' in the URL. For example, the URL
     * is "http://localhost:8080/folder/file.html", would become
     * "http://localhost:8080/folder/file_en.html". Where the filename does not
     * contain a '.', the language is simply appended. So
     * "http://localhot:8080/folder/file" would become
     * "http://8080/folder/file_en".
     * 
     * @param path
     * @param language
     * @return canonical path modified to include the language
     */
    String addLanguageToURL(final String path, final String language) {
        // if we're not sent a language, return the path as it is.
        if ("".equals(language) || language == null) {
            return path;
        }

        String modifiedPath;

        // if the path ends with a file extension (e.g. .html, .txt, .whatever),
        // then insert _<language> just before the '.'. Otherwise, leave the
        // path as it is.
        modifiedPath = path.replaceAll("(\\.[^/.]*$)", "_" + language + "$1");

        if (path.equals(modifiedPath)) {
            modifiedPath = path + "_" + language;
        }

        return modifiedPath;
    }
}
