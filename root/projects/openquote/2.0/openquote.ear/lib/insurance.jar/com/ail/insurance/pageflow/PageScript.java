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

import static com.ail.core.Functions.configurationNamespaceToProductName;
import static com.ail.core.Functions.productNameToConfigurationNamespace;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.policy.Policy;

/**
 * The PageScript element inserts JavaSript in the generated page. The JavaSript to be inserted can be referenced by
 * a URL (including product URLs), or picked up directly from the PageScript properties. It may also be inserted into 
 * the page header, or be written directly into the page relative to other elements in the PageFlow's definition.<p/>
 * <p>For example, the following will include a reference to somesite.com's somescript.js into the header of a page:<br/> 
 * &nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;pageScript url='http://somesite.com/somescript.js' pageHeader='true'/&gt;</code></p>
 * <p>The following will include a reference to a script defined in the HTML sub folder of the product. Again, the script
 * reference will be place in the header of the generated page:<br/> 
 * &nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;pageScript url='~/HTML/MyScript.js' pageHeader='true'/&gt;</code><br/>
 * Note that this would also search the product's parent and grandparent (etc) if the script is not found locally.</p>
 * <p>It is also possible to insert code directly into the page as follows:<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;<code>&lt;pageScript script='alert("Hello World!");'/&gt;</code><br/>
 * Here the <code>pageHeader</code> property has been left to it's default value of 'false' so the script will be placed
 * directly into the generated page's body in a position relative to the other elements in the PageFlow's definition of 
 * the page. 
 * @since 1.2
 */
public class PageScript extends PageElement {
	private static final long serialVersionUID = -1320299722728499324L;
	
	/** @see #isPageHeader() */
	private boolean pageHeader=false;
	
    /** @see #getUrl() */
    private String url=null;
    
    /** @see #getScript() */
    private String script=null;
        
    /** 
     * Internal representation of the URL. If the {@link PageScript#url} is a
     * product relative URL (i.e. starts with a "~"), this property will contain
     * the expanded form. Otherwise it will contain whatever the URL property contains.
     */
	private String canonicalUrl=null;
    
	private boolean initialized=false;
	
	/**
     * Default constructor
     */
    public PageScript() {
		super();
	}

    /**
	 * Set to true if the script should be included in the header of the page. 
	 * Otherwise the script is included in the location suggested by the PageScript's
	 * relative position in the page. By default the script is included in the 
	 * relative position, and not in the header.
     * @return
     */
    public boolean isPageHeader() {
		return pageHeader;
	}

    /**
     * @see #isPageHeader()
     * @param pageHeader
     */
    public void setPageHeader(boolean pageHeader) {
		this.pageHeader = pageHeader;
	}

	/**
	 * URL to JavasSript resource. This may be an absolute or product relative URL. See also
	 * the {@link #getScript() script} property. If both script and URL properties are non-null
	 * script will be used in preference.
     * @return JavaScript URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * @see #getUrl()
     * @param url JavasSript URL
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * In-line JavaSript - an alternative to using the {@link #getUrl() URL} property. If a 
     * script is defined, it's contents is assumed to be raw JavaSript and
     * is simply written to the page. If both script and URL properties are non-null
	 * script will be used in preference.
     * @return Script as a string
     */
    public String getScript() {
		return script;
	}

    /**
     * @see #getScript()
     * @param script Text of the JavaScript
     */
    public void setScript(String script) {
		this.script = script;
	}

    public String getCanonicalUrl() {
		return canonicalUrl;
	}

	public void setCanonicalUrl(String canonicalUrl) {
		this.canonicalUrl = canonicalUrl;
	}

	@Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
		// nothing to do here - PageScript doesn't have a value
		return model;
	}

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
		// nothing to do here - PageScript doesn't have validations
        return false;
	}

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
		if (!pageHeader) {
			model=renderScript(request, response, model);
		}
		
		return model;
    }

	@Override
    public Type renderPageHeader(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
		if (pageHeader) {
			renderScript(request, response, model);
		}
		return model;
    }

	private Type renderScript(RenderRequest request, RenderResponse response, Type model) throws IOException {
		initialise(request, model);

		return executeTemplateCommand("PageScript", request, response, model);
	}
	
	/**
	 * Perform one time initializations. 
	 * @param request
	 * @param model
	 */
	private synchronized void initialise(RenderRequest request, Type model) {
		if (!initialized) {
			// If a product relative URL was specified... 
			if (url!=null && url.charAt(0)=='~') {
				// ...find the actual resource it relates to by searching the product
				// hierarchy. We have to do this because the script may be defined in
				// in one of the current product's ancestors.
				String productName=null;
		        boolean success=false;
		
		        Policy quote=(com.ail.insurance.policy.Policy)model;
		        CoreProxy cp=new CoreProxy(productNameToConfigurationNamespace(quote.getProductTypeId()));
		        Collection<String> namespaces=cp.getConfigurationNamespaceParent();
		        
		        for(String namespace: namespaces) {
		            try {
		                if (namespace.endsWith("Registry")) {
		                    productName=configurationNamespaceToProductName(namespace);
		                    URL productUrl=new URL(expandRelativeUrlToProductUrl(getUrl(), request, productName));
		                    productUrl.openStream();
		                    success=true;
		                    canonicalUrl=convertProductUrlToExternalForm(productUrl).toString();
		                    break;
		                }
		            }
		            catch(Exception e) {
		                // next! This just indicates that we didn't find it in this namespace
		            }
		        }
		        
		        if (!success) {
		        	script="alert('PageFlow error: PageScript resource not found:"+getUrl()+"')";
		        }
			}
			else {
				canonicalUrl=url;
			}
		}
	}
}
