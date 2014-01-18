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

package com.ail.core;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;

import com.ail.annotation.XPathFunctionDefinition;
import com.ail.core.command.CommandInvocationError;

/**
 * Utility function class. A collection of useful static methods that are 
 * available for use from java and from XPath queries.
 **/
@XPathFunctionDefinition(namespace="c")
public class Functions {

    /**
     * Utility method to load a class in a standard way. Using this method rather than
     * directly invoking Class.forName to allow for classes to be loaded in a consistent
     * way.
     * @param name Name of the class to be loaded
     * @return Loaded, and initialised class
     * @throws ClassNotFoundException If the class cannot be found
     */
    public static Class<?> classForName(String name) throws ClassNotFoundException {
        return Class.forName(name, true, Thread.currentThread().getContextClassLoader());
    }
    
    /**
	 * Builds a <code>String[]</code> from a <code>String</code> with a
	 * given character separating values. Example "val1|val2|val3".
	 * @param string String containing values.
	 * @param separator Denotes character(s) in between values.
	 * @return String[] of values.
	 */
	public static String[] getStringArrayFromString(String string, String separator) {

			int separatorLength = separator.length();

			if (string.indexOf(separator) == -1) {
				if (string.length() == 0) {
					String[] array = {};
					return array;
				} else {
					String[] array = {string};
					return array;
				}
			}

			ArrayList<String> v = new ArrayList<String>();
			int index1 = 0;
			int index2 = 0;

			while(true) {
				index2 = string.indexOf(separator, index1 + separatorLength);
				if (index2 == -1) break;
				v.add(string.substring(index1, index2));
				index1 = index2 + separatorLength;
			}
			v.add(string.substring(index1));

			String[] array = new String[v.size()];
			for (int i = 0; i < v.size(); i ++) {
				array[i] = (String)v.get(i);
			}

			return array;
	}

    /**
     * Close a connection to a JDBC database in a safe fashion.
     * @param con Connection to be closed (or null)
     * @param st Statement to be closed (or null)
     * @param rs ResultSet to be closed (or null)
     */
    public static void closeJdbc(Connection con, Statement st, ResultSet rs) {
        if (rs!=null) {
            try {
                rs.close();
            }
            catch(SQLException e) {
                System.err.println("Failed to close resultset:"+e);
            }
        }
        if (st!=null) {
            try {
                st.close();
            }
            catch(SQLException e) {
                System.err.println("Failed to close statement:"+e);
            }
        }
        if (con!=null) {
            try {
                con.close();
            }
            catch(SQLException e) {
                System.err.println("Failed to close connection:"+e);
            }
        }
    }


    public static String valuesAsSeparatedString(Collection<Object> objects, String separator) {
        StringBuffer v=null;
        
        for(Object n: objects) {
            if (v==null) {
                v=new StringBuffer(n.toString());
            }
            else {
                v.append(separator+n.toString());
            }
        }

        return v!=null ? v.toString() : "";
    }
    
    /**
     * Turn an array of Objects into a comma separated list where each value in the list
     * is the result of calling toString() on the corresponding Object.<p>
     * If the array of objects is empty, an empty string is returned.
     * @param e An array of objects to be CSV'ed
     * @return A String in comma separated value format.
     */
    public static String arrayAsCsv(Object[] e) {
        return collectionAsCsv(Arrays.asList(e));
    }

    /**
     * Turn a collection of Objects into a comma separated list where each value in the list
     * is the result of calling toString() on the corresponding Object.<p>
     * If the array of objects is empty, an empty string is returned.
     * @param e An array of objects to be CSV'ed
     * @return A String in comma separated value format.
     */
    public static String collectionAsCsv(Collection<Object> objects) {
        return valuesAsSeparatedString(objects, ",");
    }

    /**
     * Convert a collection of objects into a \n separated string of the .toString
     * values of the objects in the collection. If the collection of objects is empty
     * an empty string will be returned.
     * @param objects Collection of objects to be converted
     * @return String representation of the objects.
     */
    public static String collectionAsLineSeparatedString(Collection<Object> objects) {
        return valuesAsSeparatedString(objects, "\n");
    }
    
    /**
     * @see #valuesAsLineSeparatedString(Collection)
     * @param objects Array of objects to place into the string
     * @return String representation of the objects;
     */
    public static String arraysAsLineSeparatedString(Object[] objects) {
        return collectionAsLineSeparatedString(Arrays.asList(objects));
    }

    public static TypeEnum enumForName(String name, TypeEnum[] enums) {
        for(TypeEnum s: enums) {
            if (s.longName().equals(name) || s.name().equals(name)) {
                return s;
            }
        }

        throw new IllegalArgumentException("'"+name+"' is not a valid value in enum "+enums[0].getClass().getName());
        
    }

    /**
     * Load the content from a URL into a String.
     * @param url URL to load content from 
     * @return Content
     * @throws IOException
     */
    public static String loadUrlContentAsString(URL url) throws IOException {
        char[] buf=new char[2048];
        StringBuffer ret=new StringBuffer();

        BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));

        for(int chars=reader.read(buf) ; chars!=-1 ; chars=reader.read(buf)) {
            ret.append(buf, 0, chars);
        }
        
        reader.close();

        return ret.toString();
    }
    
    /**
     * Utility method to expand 'variables' embedded in a string with respect to a model. Variables
     * are in the form '${&lt;xpath&gt;}', where xpath is an expression compatible with JXPath. The 
     * xpath expression is evaluated against <i>root</i> and the result placed into the string returned.</p>
     * For example: if the <i>src</i> value of <b>"Your quote number is: ${/quoteNumber}"</b> is passed in with a
     * <i>model</i> containing value of 'FQ1234' in it's <code>quoteNumber</code> property; this method would
     * return <b>"Your quote number is: FQ1234"</b>.
     * @param src Source string containing embedded variables
     * @param root All xpath expressions are evaluated against this object
     * @return A string matching <i>src</i> but with variable references expanded.
     * @see #expand(String, Type, Type)
     */
    public static String expand(String src, Type root) {
        return expand(src, root, root);
    }
    
    /**
     * Utility method to expand 'variables' embedded in a string with respect to a model. Variables
     * are in the form '${&lt;xpath&gt;}', where xpath is an expression compatible with JXPath. The 
     * xpath expression is evaluated against a <i>model</i> and the result placed into the string returned.</p>
     * <p>Two models are supported: <code>root</code> and <code>local</code>. XPath expressions starting with '.' 
     * are evaluated against <code>local</code>; all others are evaluated against <code>root</code>.</p>
     * For example: if the <i>src</i> value of <b>"Your quote number is: ${/quoteNumber}"</b> is passed in with a
     * <i>model</i> containing value of 'FQ1234' in it's <code>quoteNumber</code> property; this method would
     * return <b>"Your quote number is: FQ1234"</b>.
     * @param src Source string containing embedded variables
     * @param root Any xpath expression not starting with '.' is evaluated against this instance
     * @param local Any xpath expression starting with '.' is evaluated against this instance
     * @return A string matching <i>src</i> but with variable references expanded.
     * @see #expand(String, Type)
     */
    public static String expand(String src, Type root, Type local) {
        if (src!=null) { 
            int tokenStart, tokenEnd;
            StringBuilder buf=new StringBuilder(src);
    
            do {
                tokenStart=buf.indexOf("${");
                tokenEnd=buf.indexOf("}", tokenStart);
                
                if (tokenStart>=0 && tokenEnd>tokenStart) {
                    String val=null;
                    
                    try {
                        if (buf.charAt(tokenStart+2)=='.') {
                            val=(String)local.xpathGet(buf.substring(tokenStart+2, tokenEnd));
                        }
                        else {
                            val=(String)root.xpathGet(buf.substring(tokenStart+2, tokenEnd));
                        }
                    }
                    catch(Throwable t) {
                        // ignore this - the 'if val==null' below will handle the problem.
                    }
    
                    if (val==null) {
                        val="<b>could not resolve: "+buf.substring(tokenStart+2, tokenEnd)+"</b>";                    
                    }
                    
                    buf.replace(tokenStart, tokenEnd+1, val);
                }
            } while(tokenStart>=0 && tokenEnd>=0);
            
            return buf.toString();
        }
        else {
            return null;
        }
    }
    
    /**
     * Utility method which expands any variables it finds embedded in the passed in 
     * content against the model supplied, and writes the output to a specified writer.
     * @param writer Where the expanded output is written no.
     * @param content The String to read content from 
     * @param model The model to resolve variable references against
     */
    public static void expand(Writer writer, String content, Type model) {
        try {
            writer.write(expand(content, model));
        }
        catch(Exception e) {
            new CoreProxy().logError("Failed to read/expand content:", e);
        }
    }
    
    /**
     * Utility method which reads content from a url, expands any variables it finds embedded in
     * the content against the model supplied, and writes the output to a specified writer.
     * @param writer Where the expanded output is written no.
     * @param url The URL to read content from 
     * @param model The model to resolve variable references against
     */
    public static void expand(Writer writer, URL url, Type model) {
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(url.openStream()));
            
            for(String line=reader.readLine() ; line!=null ; line=reader.readLine()) {
                writer.write(expand(line, model));
            }

            reader.close();
        }
        catch(Exception e) {
            new CoreProxy().logError("Failed to read URL: '"+url+"'", e);
        }
    }

    /**
     * Utility method which reads content from a url, expands any variables it finds embedded in
     * the content against the model supplied, and writes the output to a specified writer.
     * @param writer Where the expanded output is written no.
     * @param content Stream to read content from. 
     * @param model The model to resolve variable references against
     */
    public static void expand(Writer writer, InputStream content, Type model) {
        try {
            BufferedReader reader=new BufferedReader(new InputStreamReader(content));
            
            for(String line=reader.readLine() ; line!=null ; line=reader.readLine()) {
                writer.write(expand(line, model));
            }

            reader.close();
        }
        catch(Exception e) {
            new CoreProxy().logError("Failed to read input stream.", e);
        }
    }

    /**
     * Convert null strings into empty strings. When a UI component is rendering it'll frequently
     * want to render null strings. The default java behaviour when you ask to output a null String
     * is to write "null" to the output - which isn't what we typically want on the UI. 
     * @param s String to check
     * @return "" if the string was null, or the value of the string if it was not.
     */
    public static String hideNull(String s) {
        return (s==null) ? "" : s;
    }
    
    /**
     * Translate a Date into a String using the specified format.
     * @param date Date to be converted
     * @param pattern Format for to return date
     * @return <i>date</i> formatted as <i>pattern</i>
     */
    public static String format(Date date, String pattern) {
        return new SimpleDateFormat(pattern).format(date);
    }

    public static URL absoluteConfigureUrl(Core core, String suppliedUrl) throws MalformedURLException {
        if (!suppliedUrl.startsWith("~/")) {
            return new URL(suppliedUrl);
        }
        else {
            String leafUrl=suppliedUrl.substring(2);
            // The URL we've been passed isn't complete, we'll assume it's relative.
            for(String derivedUrl: core.getConfigurationSource()) {
                try {
                    derivedUrl=derivedUrl.substring(0, derivedUrl.lastIndexOf('/')+1)+leafUrl;
                    return new URL(derivedUrl);
                }
                catch(Exception ex) {
                    core.logDebug("Loading content failed from:"+derivedUrl+", reason:"+ex.toString());
                }
            }
            throw new CommandInvocationError("Exausted possible content locations for:"+suppliedUrl);
        }
    }
    
    /**
     * The script may be local (the value of the script property), or remote (loaded using the
     * url property). This method will return the script no matter where it is.
     * @return A String representing the script
     */
    public static String loadScriptOrUrlContent(Core core, String suppliedUrl, String script) {
        if (script==null && suppliedUrl==null) {
            throw new CommandInvocationError("No script or url property was defined");
        }
    
        if (script!=null) {
            return script;
        }
    
        // The url may be absolute or it may be relative to the source URL of
        // the owning configuration, or one of it's parents.
        if (!suppliedUrl.startsWith("~/")) {
            try {             
                return Functions.loadUrlContentAsString(new URL(suppliedUrl));
            }
            catch(MalformedURLException e) {
                throw new CommandInvocationError("Failed to load content from: '"+suppliedUrl+"'. URL is malformed.");
            }
            catch(IOException e) {
                throw new CommandInvocationError("Failed to read content from: '"+suppliedUrl+"'. IOException.");
            }
        }
        else {
            // ignore the leading "~/"
            String leafUrl=suppliedUrl.substring(2);
            
            Exception lastException=null;
            
            // The URL we've been passed isn't complete, we'll assume it's relative.
            for(String derivedUrl: core.getConfigurationSource()) {
                derivedUrl=derivedUrl.substring(0, derivedUrl.lastIndexOf('/')+1)+leafUrl;
    
                try {
                    String content=Functions.loadUrlContentAsString(new URL(derivedUrl));
                    core.logDebug("Content found in:"+derivedUrl);
                    return content;
                }
                catch(Exception ex) {
                    core.logDebug("Content not found in:"+derivedUrl+", trying next.");
                    lastException=ex;
                }
            }
            throw new CommandInvocationError("Exausted possible content locations.", lastException);
        }
    }

    /**
     * Return the configuration namespace associated with a product. The convention is
     * that each product has it's own configuration namespace. The productName is
     * in fact the name of the package containing (as a minimum) the product's
     * Registry.
     * @param productName Name of the product to get the namespace.
     * @return Configuration namespace where the product's configuration can be found.
     */
    public static String productNameToConfigurationNamespace(String productName) {
        if (productName.endsWith(".Registry")) {
            throw new IllegalStateException("Cannot convert product name into namespace: product name already ends with .Registry");
        }
        
        return productName+".Registry";
    }

    /**
     * Return the product name associated with a configuration namespace. The convention is
     * that each product has it's own configuration namespace. The productName is
     * in fact the name of the package containing (as a minimum) the product's
     * Registry.
     * @param configuratioNamespace Namespace to fetch the product name for.
     * @return Product name associated with the namespace
     */
    public static String configurationNamespaceToProductName(String configuratioNamespace) {
        if (!configuratioNamespace.endsWith(".Registry")) {
            throw new IllegalStateException("Cannot convert namespace into product name: namespace doesn't end with .Registry");
        }
        
        return configuratioNamespace.substring(0, configuratioNamespace.length()-9);
    }

    /**
     * Return the name of a product's default type based on it's namespace.
     * @param productName Name of product to get the default type for.
     * @return Name of the default type.
     */
    public static String productNameToDefaultType(String productName) {
        int idx=productName.lastIndexOf('.');

        // namespace might not include a '.' at all. If it doesn't, use the whole thing.
        return (idx<0) ? productName : productName.substring(idx+1);
    }
}
